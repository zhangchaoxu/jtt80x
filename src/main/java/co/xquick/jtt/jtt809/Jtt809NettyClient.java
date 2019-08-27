package co.xquick.jtt.jtt809;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty 客户端 从链路
 *
 * @author Charles zhangchaoxu@gmail.com
 */
@Data
public class Jtt809NettyClient {

    public static Logger LOGGER = LoggerFactory.getLogger(Jtt809NettyClient.class);

    private String ip;
    private int port;

    public Jtt809NettyClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    private Channel channel;

    public void init() {
        final ByteBuf delimiter = Unpooled.buffer(1).writeByte(0x5d);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline().addLast(new ReadTimeoutHandler(300));//5分钟没有消息断开
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(5000, delimiter));
                            ch.pipeline().addLast(new Jtt809Decoder());//该处理器将信息转换成message对象
                            ch.pipeline().addLast(new Jtt809Handler());
                        }
                    });
            channel = bootstrap.connect(ip, port).channel();
            LOGGER.info("Client init success at [" + ip + ":" + port + "]");
        } catch (Exception e) {
            LOGGER.info("Client init failed");
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }
    }

}
