package picky.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Each of the byte array contains one more command.
 * 
 * @author FrankNPC
 *
 */
public class MessageHandler extends ChannelInboundHandlerAdapter {

	private ByteBuf buffer = Unpooled.EMPTY_BUFFER;
	
	@Override
	public void channelRead(ChannelHandlerContext channelContext, Object msg) throws Exception {
		buffer.writeBytes((ByteBuf) msg);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext channelContext) throws Exception {
		ChannelManager.getInstance().receive(channelContext, buffer.clear().array());
	}

	@Override
	public void channelInactive(ChannelHandlerContext channelContext) throws Exception {
		ChannelManager.getInstance().invalid(channelContext);
	}

}