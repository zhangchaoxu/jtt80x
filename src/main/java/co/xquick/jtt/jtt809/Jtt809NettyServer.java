package co.xquick.jtt.jtt809;

import co.xquick.jtt.entity.Message;
import co.xquick.jtt.netty.NettyAccountConfig;
import com.google.common.collect.Maps;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Netty 服务器端 主链路
 *
 * @author Charles zhangchaoxu@gmail.com
 */
@Data
@Component
public class Jtt809NettyServer {

    public static Logger LOGGER = LoggerFactory.getLogger(Jtt809NettyServer.class);

    /**
     * 主链路登陆状态
     */
    private LoginStatusEnum loginStatus = LoginStatusEnum.init;

    /**
     * 对应的从链路,key为msgGesscenterId
     */
    private HashMap<Integer, Jtt809NettyClient> clientMap = Maps.newHashMap();

    @Autowired
    NettyAccountConfig nettyAccountConfig;

    public void start() {
        final Jtt809NettyServer server = this;
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(new ReadTimeoutHandler(300));//5分钟没有消息断开
                            //ch.pipeline().addLast(new IdleStateHandler(300,300,300));//5分钟没有消息断开
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1000, Unpooled.buffer(1).writeByte(Message.MSG_TALL))); // 粘包分隔符
                            ch.pipeline().addLast(new Jtt809Decoder());//该处理器将信息转换成message对象
                            ch.pipeline().addLast(new Jtt809Handler(server, nettyAccountConfig));
                        }
                    });
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(nettyAccountConfig.getPort()).sync();
            LOGGER.info("Server start listen at " + nettyAccountConfig.getPort());
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            LOGGER.info("Server start failed");
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
