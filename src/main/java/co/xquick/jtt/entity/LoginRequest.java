package co.xquick.jtt.entity;

import lombok.Data;

/**
 * 登陆请求对象
 */
@Data
public class LoginRequest {
    // 用户名
    private int userId;
    // 密码
    private String password;
    // 从链路IP
    private String downLinkIp;
    // 从链路端口
    private int downLinkPort;
}
