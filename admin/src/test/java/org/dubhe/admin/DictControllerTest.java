

package org.dubhe.admin;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.dubhe.biz.base.dto.DeleteDTO;
import org.dubhe.admin.domain.dto.DictCreateDTO;
import org.dubhe.admin.domain.dto.DictUpdateDTO;
import org.dubhe.admin.domain.entity.DictDetail;
import org.dubhe.cloud.unittest.base.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @description 实体类
 * @date 2020-03-25
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DictControllerTest extends BaseTest {

    @Test
    public void list() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/dict/all");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void page() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.get("/dict");
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void create() throws Exception {
        DictCreateDTO createDTO = new DictCreateDTO();
        createDTO.setName("aaa");
        createDTO.setRemark("aaa");
        DictDetail detail = new DictDetail();
        detail.setDictId(1L);
        detail.setLabel("bbbb");
        detail.setSort("1");
        detail.setValue("cccc");
        createDTO.setDictDetails(Lists.newArrayList(detail));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post("/dict")
                .content(JSON.toJSONString(createDTO)).contentType(MediaType.APPLICATION_JSON);
                ;
        mockHttp(mockHttpServletRequestBuilder);
    }

    @Test
    public void update() throws Exception {
        DictUpdateDTO dto = new DictUpdateDTO();
        dto.setId(41L);
        dto.setName("eee");
        dto.setRemark("eee");
        DictDetail detail = new DictDetail();
        detail.setDictId(1L);
        detail.setLabel("fff");
        detail.setSort("1");
        detail.setValue("ggg");
        dto.setDictDetails(Lists.newArrayList(detail));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.put("/dict")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        ;
        mockHttp(mockHttpServletRequestBuilder);
    }


    @Test
    public void delete() throws Exception {
        DeleteDTO dto = new DeleteDTO();
        dto.setIds(Sets.newHashSet(41L));
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.delete("/dict")
                .content(JSON.toJSONString(dto)).contentType(MediaType.APPLICATION_JSON);
        ;
        mockHttp(mockHttpServletRequestBuilder);
    }


}
