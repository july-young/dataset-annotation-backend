package org.dubhe.admin;

import com.alibaba.fastjson.JSON;
import org.dubhe.admin.domain.dto.MenuCreateDTO;
import org.dubhe.admin.domain.dto.MenuQueryDTO;
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
public class MenuControllerTest extends BaseTest {

    @Test
    public void tree() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/menus/tree");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void page() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/menus");
        mockHttp(mockHttpServletRequestBuilder);
    }
//    @Test
//    public void create() throws Exception {
//        MenuCreateDTO dto= new MenuCreateDTO();
//
//        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/menus")
//                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
//        mockHttp(mockHttpServletRequestBuilder);
//    }


}
