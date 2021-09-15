package picky.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class PickyClient {
	
	private static PickyClient pickyClient = new PickyClient();
	public static PickyClient getInstance() {
		return pickyClient;
	}

	private PickyClient() {
		workGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
	}
	
	private EventLoopGroup workGroup = null;
	private Bootstrap bootstrap = null;
	public PickyClient connect(String hostUrl) {
		String[] array = hostUrl.split(":");
		if (array.length>2) {return this;}
		return connect(array[0], array.length==1?80:Integer.parseInt(array[1]));
	}
	public PickyClient connect(String host, int port) { 
		try{
			bootstrap.group(workGroup)
					.channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel socketChannel) throws Exception {
							socketChannel.pipeline().addLast(new MessageHandler());
						}
					});
			ChannelFuture future = bootstrap.connect(host,port).sync();
			future.channel().closeFuture().sync();
		}catch (Exception e){
		}
		return this;
	}

	public void shutdown() {
		workGroup.shutdownGracefully();
		pickyClient=null;
	}
}
