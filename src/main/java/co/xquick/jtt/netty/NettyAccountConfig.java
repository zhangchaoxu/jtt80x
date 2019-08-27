package co.xquick.jtt.netty;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * netty config
 */
@Component
@Data
@ConfigurationProperties(prefix = "netty")
public class NettyAccountConfig {

    private String  url;

    private int port;

    private int bossThread;

    private int workerThread;

    private boolean keepalive;

    private int backlog;

    public String orgKey;

    public String msgGesscenterId;

    public int userId;

    public String password;
}
