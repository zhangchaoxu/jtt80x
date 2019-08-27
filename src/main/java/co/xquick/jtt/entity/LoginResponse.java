package co.xquick.jtt.entity;

import lombok.Data;

/**
 * 登录结果
 */
@Data
public class LoginResponse {

    private int result;
    private int verifyCode;

}

