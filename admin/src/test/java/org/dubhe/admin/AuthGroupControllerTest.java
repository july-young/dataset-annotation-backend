package org.dubhe.admin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.dubhe.admin.domain.dto.AuthCodeCreateDTO;
import org.dubhe.admin.domain.dto.AuthCodeUpdateDTO;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.cloud.unittest.base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthGroupControllerTest extends BaseTest {
    @Test
    public void queryAll() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/authCode");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void create() throws Exception {
        AuthCodeCreateDTO authCodeCreateDTO = new AuthCodeCreateDTO();
        authCodeCreateDTO.setAuthCode("aaaa");
        Set<Long> permissions = new HashSet<>();
        permissions.add(5L);
        permissions.add(6L);
        authCodeCreateDTO.setPermissions(permissions);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/authCode")
                .content(JSON.toJSONString(authCodeCreateDTO)).contentType(MediaType.APPLICATION_JSON);
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void update() throws Exception {
        AuthCodeUpdateDTO dto = new AuthCodeUpdateDTO();
        dto.setId(4L);
        dto.setAuthCode("bbbb");
        Set<Long> permissions = new HashSet<>();
        permissions.add(5L);
        permissions.add(6L);
        dto.setPermissions(permissions);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/authCode")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void delete() throws Exception {
        DeleteDTO deleteDTO = new DeleteDTO();
        deleteDTO.setIds(Sets.newHashSet(4L));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete("/authCode")
                .content(JSON.toJSONString(deleteDTO)).contentType(MediaType.APPLICATION_JSON);
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void list() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/authCode/list");
        mockHttp(mockHttpServletRequestBuilder);
    }
}
