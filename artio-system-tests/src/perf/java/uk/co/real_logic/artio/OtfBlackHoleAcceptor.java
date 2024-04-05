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
package uk.co.real_logic.artio;

import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.infra.Blackhole;
import uk.co.real_logic.artio.fields.AsciiFieldFlyweight;
import uk.co.real_logic.artio.otf.MessageControl;
import uk.co.real_logic.artio.otf.OtfMessageAcceptor;
import uk.co.real_logic.artio.util.AsciiBuffer;

import static org.openjdk.jmh.annotations.CompilerControl.Mode.DONT_INLINE;

public final class OtfBlackHoleAcceptor implements OtfMessageAcceptor
{
    private final Blackhole bh;

    public OtfBlackHoleAcceptor(final Blackhole bh)
    {
        this.bh = bh;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onNext()
    {
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onField(final int tag, final AsciiBuffer buffer, final int offset, final int length)
    {
        bh.consume(tag);
        bh.consume(buffer);
        bh.consume(offset);
        bh.consume(length);
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onGroupHeader(final int tag, final int numInGroup)
    {
        bh.consume(tag);
        bh.consume(numInGroup);
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onGroupBegin(final int tag, final int numInGroup, final int index)
    {
        bh.consume(tag);
        bh.consume(numInGroup);
        bh.consume(index);
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onGroupEnd(final int tag, final int numInGroup, final int index)
    {
        bh.consume(tag);
        bh.consume(numInGroup);
        bh.consume(index);
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public MessageControl onComplete()
    {
        return MessageControl.CONTINUE;
    }

    @CompilerControl(DONT_INLINE)
    public boolean onError(
        final ValidationError error,
        final long messageType,
        final int tagNumber,
        final AsciiFieldFlyweight value)
    {
        bh.consume(error);
        bh.consume(messageType);
        bh.consume(tagNumber);
        bh.consume(value);
        return false;
    }
}
