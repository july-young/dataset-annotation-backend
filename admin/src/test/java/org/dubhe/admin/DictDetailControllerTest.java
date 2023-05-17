package org.dubhe.admin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.admin.domain.dto.DictDetailCreateDTO;
import org.dubhe.admin.domain.dto.DictDetailUpdateDTO;
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
public class DictDetailControllerTest extends BaseTest {



    @Test
    public void page() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/dictDetail");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void create() throws Exception {
        DictDetailCreateDTO dto = new DictDetailCreateDTO();
        dto.setDictId(1L);
        dto.setLabel("aaa");
        dto.setValue("aaa");
        dto.setSort("100");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/dictDetail")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void update() throws Exception {
        DictDetailUpdateDTO dto = new DictDetailUpdateDTO();
        dto.setId(41L);
        dto.setLabel("bbb");
        dto.setValue("bbb");
        dto.setSort("101");
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/dictDetail")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        ;
        mockHttp(mockHttpServletRequestBuilder);
    }


    @Test
    public void delete() throws Exception {
        DeleteDTO dto = new DeleteDTO();
        dto.setIds(Sets.newHashSet(41L));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete("/dictDetail")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        ;
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void
    getDictDetails() throws Exception {
        DeleteDTO dto = new DeleteDTO();
        dto.setIds(Sets.newHashSet(41L));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/dictDetail/getDictDetails")
                .param("name","a");

        mockHttp(mockHttpServletRequestBuilder);
    }

}
