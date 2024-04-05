/*
 * Copyright 2015-2023 Real Logic Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.real_logic.artio.stress;

import io.aeron.logbuffer.ControlledFragmentHandler;
import org.agrona.DirectBuffer;
import uk.co.real_logic.artio.builder.Printer;
import uk.co.real_logic.artio.decoder.PrinterImpl;
import uk.co.real_logic.artio.library.OnMessageInfo;
import uk.co.real_logic.artio.library.SessionHandler;
import uk.co.real_logic.artio.messages.DisconnectReason;
import uk.co.real_logic.artio.session.Session;
import uk.co.real_logic.artio.util.AsciiBuffer;
import uk.co.real_logic.artio.util.MutableAsciiBuffer;

import static io.aeron.logbuffer.ControlledFragmentHandler.Action.CONTINUE;

public class StressSessionHandler implements SessionHandler
{
    private final AsciiBuffer string = new MutableAsciiBuffer();
    private final Printer printer = new PrinterImpl();

    public StressSessionHandler(final Session session)
    {
    }

    public ControlledFragmentHandler.Action onMessage(
        final DirectBuffer buffer,
        final int offset,
        final int length,
        final int libraryId,
        final Session session,
        final int sequenceIndex,
        final long messageType,
        final long timestampInNs,
        final long position,
        final OnMessageInfo messageInfo)
    {
        if (StressConfiguration.PRINT_EXCHANGE)
        {
            string.wrap(buffer);
            System.out.printf("%d -> %s%n", session.id(), printer.toString(string, offset, length, messageType));
        }

        return CONTINUE;
    }

    public void onTimeout(final int libraryId, final Session session)
    {
    }

    public void onSlowStatus(final int libraryId, final Session session, final boolean hasBecomeSlow)
    {
    }

    public ControlledFragmentHandler.Action onDisconnect(
        final int libraryId, final Session session, final DisconnectReason reason)
    {
        if (StressConfiguration.PRINT_EXCHANGE)
        {
            System.out.printf("%d Disconnected: %s%n", session.id(), reason);
        }

        return CONTINUE;
    }

    public void onSessionStart(final Session session)
    {
    }
}
