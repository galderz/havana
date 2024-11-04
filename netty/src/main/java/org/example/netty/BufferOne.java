package org.example.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

public class BufferOne
{
    public static void main(String[] args)
    {
        final ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(8, 8);
        System.out.println(buffer.toString());
    }
}
