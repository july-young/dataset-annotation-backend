package org.dubhe.admin;

import com.alibaba.fastjson.JSON;
import org.dubhe.admin.domain.dto.UserRegisterDTO;
import org.dubhe.admin.domain.dto.UserRegisterMailDTO;
import org.dubhe.admin.domain.dto.UserResetPasswordDTO;
import org.dubhe.admin.enums.UserMailCodeEnum;
import org.dubhe.cloud.unittest.base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginControllerTest extends BaseTest {

    @Test
    public void code() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/auth/code");
        mockHttpWithoutLogin(mockHttpServletRequestBuilder);
    }
    @Test
    public void logout() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete("/auth/logout");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void info() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/auth/info");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void register() throws Exception {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setCode("1234");
        userRegisterDTO.setEmail("1234@qq.com");
        userRegisterDTO.setNickName("abc");
        userRegisterDTO.setPassword("123456");
        userRegisterDTO.setPhone("12345678901");
        userRegisterDTO.setSex(1);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/userRegister")
                .content(JSON.toJSONString(userRegisterDTO)).contentType(MediaType.APPLICATION_JSON)
                ;
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void resetPassword() throws Exception {
        UserResetPasswordDTO dto = new UserResetPasswordDTO();
        dto.setCode("1234");
        dto.setEmail("1234@qq.com");
        dto.setPassword("123456");

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/resetPassword")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON)
                ;
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void getCodeBySentEmail() throws Exception {
        UserRegisterMailDTO dto = new UserRegisterMailDTO();
        dto.setEmail("1234@qq.com");
        dto.setType(UserMailCodeEnum.REGISTER_CODE.getValue());

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/auth/getCodeBySentEmail")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON)
                ;
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void getPublicKey() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/auth/getPublicKey");
        mockHttp(mockHttpServletRequestBuilder);
    }


}
