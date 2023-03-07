package com.tco.city.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = {"/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
public class CityControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;


    @WithMockUser(username="user", password = "user", roles = {"ALLOW_VIEW"})
    @Test
    public void getDataShouldReturnFilteredData() throws Exception {
        ResultActions result = mvc.perform(get("/api/getData")
                .param("limit", "5")
                .param("page", "0")
                .param("filter", "M")
            );

        result.andExpect(status().isOk());
        String data = result.andReturn().getResponse().getContentAsString();
        JSONObject jo = new JSONObject(data);
        JSONArray array = jo.getJSONArray("content");
        assertEquals(array.length(), 2);
        assertEquals(array.getJSONObject(0).get("name"), ("Minsk"));
        assertEquals(array.getJSONObject(1).get("name"), ("Madrid"));
    }

    @WithMockUser(username="test", password = "test", roles = {"NOT_ALLOW_VIEW"})
    @Test
    public void getDataShouldReturnForbiddenStatusWithotRole() throws Exception {
        //when(service.getByPage(any(), any(), any())).thenReturn(response);
        this.mvc.perform(get("/api/getData")
                .param("limit", "1")
                .param("page", "1")
                .param("filter", "q")
            )
            .andExpect(status().isForbidden());
    }


    @WithMockUser(username="user", password = "user", roles = {"ALLOW_VIEW"})
    @Test
    public void updateNotAllowedForAllowViewRole() throws Exception {
        this.mvc.perform(post("/api/city/1")
                .param("city", "{id:1,name:name:url:url")
            )
            .andExpect(status().isForbidden());
    }

    @WithMockUser(username="user", password = "user", roles = {"ALLOW_EDIT"})
    @Test
    public void updateAllowedForAllowEditRole() throws Exception {
        //when(service.get(1L)).thenReturn(new City(1L, "oldName"));
        this.mvc.perform(post("/api/city/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"1\",\"name\":\"name\",\"url\":\"url\"}")
            )
            .andExpect(status().isOk());
    }
}
