package org.dubhe.auth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = CustomerOauthExceptionSerializer.class)
public class CustomerOauthException extends OAuth2Exception {
    public CustomerOauthException(String msg, Throwable t) {
        super(msg, t);
    }

    public CustomerOauthException(String msg) {
        super(msg);
    }

}
