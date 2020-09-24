//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.netty:netty-common:4.1.51.Final
//DEPS io.netty:netty-buffer:4.1.51.Final
//DEPS io.netty:netty-transport:4.1.51.Final
//DEPS io.netty:netty-handler:4.1.51.Final

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static java.lang.System.out;

public class client
{
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String... args) throws InterruptedException, IOException
    {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try
        {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>()
                {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception
                    {
                        ChannelPipeline p = ch.pipeline();
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(new EchoClientHandler());
                    }
                });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            final var ch = f.channel();

            // Read commands from the stdin.
            System.out.println("Enter text (quit to end)");
            ChannelFuture lastWriteFuture = null;

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            for (;;) {
                String line = in.readLine();
                if (line == null || "quit".equalsIgnoreCase(line)) {
                    break;
                }

                // Sends the received line to the server.
                final var buf = Unpooled.buffer(16);
                buf.writeCharSequence(line, StandardCharsets.UTF_8);
                lastWriteFuture = ch.writeAndFlush(buf);
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.awaitUninterruptibly();
            }

            // Wait until the connection is closed.
            ch.closeFuture().sync();
        }
        finally
        {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }
    }

    static class EchoClientHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
        {
            // Print as received
            ByteBuf buf = (ByteBuf) msg;
            out.println(buf.toString(StandardCharsets.UTF_8));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx)
        {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
        {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }
    }
}
