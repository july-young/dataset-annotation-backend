

package org.dubhe.cloud.authconfig.factory;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @description 加密工具工厂类

 */
public class PasswordEncoderFactory {

    private PasswordEncoderFactory(){

    }


    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    /**
     * 获取加密器
     * @return
     */
    public static PasswordEncoder getPasswordEncoder(){
        return PASSWORD_ENCODER;
    }

}
