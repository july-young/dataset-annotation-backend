package org.dubhe.auth.exception;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description 序列化异常处理类

 */
public class CustomerOauthExceptionSerializer extends StdSerializer<CustomerOauthException> {

    protected CustomerOauthExceptionSerializer() {
        super(CustomerOauthException.class);
    }

    @Override
    public void serialize(CustomerOauthException e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", String.valueOf(e.getHttpErrorCode()));
        jsonGenerator.writeStringField("msg", e.getMessage());
        jsonGenerator.writeStringField("data",null);
        jsonGenerator.writeEndObject();
    }
}

