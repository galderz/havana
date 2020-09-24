//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.netty:netty-common:4.1.51.Final
//DEPS io.netty:netty-buffer:4.1.51.Final
//DEPS io.netty:netty-transport:4.1.51.Final
//DEPS io.netty:netty-handler:4.1.51.Final

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.*;

public class client
{
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String... args) throws InterruptedException, IOException
    {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        //p.addLast(new LoggingHandler(LogLevel.INFO));
                        p.addLast(new EchoClientHandler());
                    }
                });

            // Start the client.
            ChannelFuture f = b.connect(HOST, PORT).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully();
        }


//        EventLoopGroup group = new NioEventLoopGroup();
//        try
//        {
//            Bootstrap b = new Bootstrap();
//            b.group(group)
//                .channel(NioSocketChannel.class)
//                .option(ChannelOption.TCP_NODELAY, true)
//                .handler(new ChannelInitializer<SocketChannel>()
//                {
//                    @Override
//                    public void initChannel(SocketChannel ch) throws Exception
//                    {
//                        ChannelPipeline p = ch.pipeline();
//                        p.addLast(new LoggingHandler(LogLevel.INFO));
//                        p.addLast(new LocalEchoClientHandler());
//                    }
//                });
//
//            // Start the client.
//            Channel ch = b.connect(HOST, PORT).sync().channel();
//
//            // Read commands from the stdin.
//            System.out.println("Enter text (quit to end)");
//            ChannelFuture lastWriteFuture = null;
//            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
//            for (;;) {
//                String line = in.readLine();
//                if (line == null || "quit".equalsIgnoreCase(line)) {
//                    break;
//                }
//
//                // Sends the received line to the server.
//                lastWriteFuture = ch.writeAndFlush(line);
//                out.println("Written and flushed, now wait");
//                lastWriteFuture.awaitUninterruptibly();
//                out.println("Write complete");
//            }
//
//            // Wait until all messages are flushed before closing the channel.
//            if (lastWriteFuture != null) {
//                lastWriteFuture.awaitUninterruptibly();
//            }
//
////            // Wait until the connection is closed.
////            f.channel().closeFuture().sync();
//        }
//        finally
//        {
//            // Shut down the event loop to terminate all threads.
//            group.shutdownGracefully();
//        }
    }

//    static class LocalEchoClientHandler extends SimpleChannelInboundHandler<Object>
//    {
//        @Override
//        public void channelRead0(ChannelHandlerContext ctx, Object msg)
//        {
//            // Print as received
//            System.out.println(msg);
//        }
//
//        @Override
//        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
//        {
//            cause.printStackTrace();
//            ctx.close();
//        }
//    }

    static class EchoClientHandler extends ChannelInboundHandlerAdapter
    {
        private final ByteBuf firstMessage;

        /**
         * Creates a client-side handler.
         */
        public EchoClientHandler() {
            firstMessage = Unpooled.buffer(16);
            for (int i = 0; i < firstMessage.capacity(); i ++) {
                firstMessage.writeByte((byte) i);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            out.println("Send first ping...");
            ctx.writeAndFlush(firstMessage);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            out.println("Next ping...");
            ctx.write(msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            // Close the connection when an exception is raised.
            cause.printStackTrace();
            ctx.close();
        }
    }
}
