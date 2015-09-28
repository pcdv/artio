/*
 * Copyright 2015 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.fix_gateway.engine;

import uk.co.real_logic.aeron.Publication;
import uk.co.real_logic.aeron.Subscription;
import uk.co.real_logic.agrona.concurrent.*;
import uk.co.real_logic.fix_gateway.ErrorPrinter;
import uk.co.real_logic.fix_gateway.FixCounters;
import uk.co.real_logic.fix_gateway.GatewayProcess;
import uk.co.real_logic.fix_gateway.engine.framer.*;
import uk.co.real_logic.fix_gateway.engine.logger.Logger;
import uk.co.real_logic.fix_gateway.engine.logger.SequenceNumberIndex;
import uk.co.real_logic.fix_gateway.engine.logger.SequenceNumbers;
import uk.co.real_logic.fix_gateway.session.SessionIdStrategy;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

import static uk.co.real_logic.agrona.CloseHelper.quietClose;
import static uk.co.real_logic.agrona.concurrent.AgentRunner.startOnThread;

public final class FixEngine extends GatewayProcess
{
    public static final long COMMAND_QUEUE_IDLE_NS = 1;

    private QueuedPipe<AdminCommand> adminCommands = new ManyToOneConcurrentArrayQueue<>(16);

    private final SequenceNumberIndex sequenceNumberIndex;
    private final EngineConfiguration configuration;

    private AgentRunner errorPrinterRunner;
    private AgentRunner framerRunner;
    private Logger logger;

    public static FixEngine launch(final EngineConfiguration configuration)
    {
        configuration.conclude();

        return new FixEngine(configuration).start();
    }

    /**
     * Query the engine for the list of libraries currently active.
     *
     * @return a list of currently active libraries.
     */
    public List<LibraryInfo> libraries()
    {
        final QueryLibraries query = new QueryLibraries();
        while (!adminCommands.offer(query))
        {
            // TODO: decide whether this and QueryLibraries#awaitResponse() should take an idle strategy
            LockSupport.parkNanos(COMMAND_QUEUE_IDLE_NS);
        }
        return query.awaitResponse();
    }

    private FixEngine(final EngineConfiguration configuration)
    {
        init(configuration);
        this.configuration = configuration;

        sequenceNumberIndex = new SequenceNumberIndex();
        initFramer(configuration, fixCounters);
        initLogger(configuration);
        initErrorPrinter(configuration);
    }

    private void initLogger(final EngineConfiguration configuration)
    {
        logger = new Logger(
            configuration, inboundLibraryStreams, outboundLibraryStreams, errorBuffer, replayPublication(),
            sequenceNumberIndex);
        logger.init();
    }

    private Publication replayPublication()
    {
        return aeron.addPublication(configuration.aeronChannel(), OUTBOUND_REPLAY_STREAM);
    }

    private void initErrorPrinter(final EngineConfiguration configuration)
    {
        if (this.configuration.printErrorMessages())
        {
            final ErrorPrinter printer = new ErrorPrinter(monitoringFile, configuration.errorSlotSize());
            errorPrinterRunner = new AgentRunner(
                configuration.errorPrinterIdleStrategy(), Throwable::printStackTrace, null, printer);
        }
    }

    private void initFramer(final EngineConfiguration configuration, final FixCounters fixCounters)
    {
        final SessionIds sessionIds = new SessionIds();

        final IdleStrategy idleStrategy = configuration.framerIdleStrategy();
        final Subscription librarySubscription = outboundLibraryStreams.subscription();
        final SessionIdStrategy sessionIdStrategy = configuration.sessionIdStrategy();

        final ConnectionHandler handler = new ConnectionHandler(
            configuration,
            sessionIdStrategy,
            sessionIds,
            inboundLibraryStreams,
            idleStrategy,
            fixCounters,
            errorBuffer);

        final Framer framer = new Framer(
            new SystemEpochClock(), configuration, handler, librarySubscription, replaySubscription(),
            inboundLibraryStreams.gatewayPublication(idleStrategy), sessionIdStrategy, sessionIds, adminCommands,
            new SequenceNumbers(sequenceNumberIndex, idleStrategy)
        );
        framerRunner = new AgentRunner(idleStrategy, errorBuffer, null, framer);
    }

    private Subscription replaySubscription()
    {
        return aeron.addSubscription(configuration.aeronChannel(), OUTBOUND_REPLAY_STREAM);
    }

    private FixEngine start()
    {
        startOnThread(framerRunner);
        logger.start();
        if (configuration.printErrorMessages())
        {
            startOnThread(errorPrinterRunner);
        }
        return this;
    }

    public synchronized void close()
    {
        quietClose(errorPrinterRunner);
        framerRunner.close();
        logger.close();
        super.close();
    }

}
