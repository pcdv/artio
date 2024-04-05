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
package uk.co.real_logic.artio.engine;

import java.nio.ByteBuffer;

/**
 * Modify a ByteBuffer whilst providing better illegal argument exceptions.
 */
public final class ByteBufferUtil
{
    public static void position(final ByteBuffer byteBuffer, final int newPosition)
    {
        try
        {
            byteBuffer.position(newPosition);
        }
        catch (final IllegalArgumentException ex)
        {
            throw new IllegalArgumentException(
                "limit = " + byteBuffer.limit() + ", position = " + newPosition, ex);
        }
    }

    public static void limit(final ByteBuffer byteBuffer, final int newLimit)
    {
        try
        {
            byteBuffer.limit(newLimit);
        }
        catch (final IllegalArgumentException ex)
        {
            throw new IllegalArgumentException("newLimit = " + newLimit + " capacity = " + byteBuffer.capacity(), ex);
        }
    }
}
