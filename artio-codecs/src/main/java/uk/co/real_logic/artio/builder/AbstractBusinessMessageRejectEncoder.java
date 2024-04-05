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
package uk.co.real_logic.artio.builder;

import org.agrona.DirectBuffer;

public interface AbstractBusinessMessageRejectEncoder extends Encoder
{
    AbstractBusinessMessageRejectEncoder refSeqNum(int value);

    AbstractBusinessMessageRejectEncoder refMsgType(byte[] value, int offset, int length);

    AbstractBusinessMessageRejectEncoder text(CharSequence value);

    AbstractBusinessMessageRejectEncoder businessRejectRefID(DirectBuffer value, int offset, int length);

    AbstractBusinessMessageRejectEncoder businessRejectReason(int value);
}
