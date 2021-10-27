package com.fengx.template.config.encode;

import com.fengx.template.utils.MD5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 加密配置
 */
public class PasswordEncoderConfig implements PasswordEncoder {

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Utils.encode(rawPassword.toString()));
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Utils.encode(rawPassword.toString());
    }
}