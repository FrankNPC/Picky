package picky.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import picky.Configuration;

public class PickyServer {
	
	private static PickyServer pickyServer = new PickyServer();
	public static PickyServer getInstance() {
		return pickyServer;
	}
	
	private EventLoopGroup boosGroup = null;
	private EventLoopGroup workGroup = null;
	private ServerBootstrap bootstrap = null;
	
	private PickyServer() {
		boosGroup = new NioEventLoopGroup(1);
		workGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
	}
	
	public PickyServer start() throws InterruptedException {
		bootstrap.group(boosGroup, workGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG,1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) throws Exception {
						socketChannel.pipeline().addLast(new MessageHandler());
					}
				});
		ChannelFuture channelFuture = bootstrap.bind(Configuration.getConfiguration().getPort()).sync();
		channelFuture.channel().closeFuture().sync();
		return this;
	}

	public void shutdown() {
		boosGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
		pickyServer=null;
	}
}
