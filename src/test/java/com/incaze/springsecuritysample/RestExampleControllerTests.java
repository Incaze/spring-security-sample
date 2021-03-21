package com.incaze.springsecuritysample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SpringSecuritySampleApplication.class)
@AutoConfigureMockMvc
public class RestExampleControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void auth_whenInvalidData_thenNotFound() throws Exception {
        String content = getAuthData("alex", "wanrltw1");

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void auth_whenValidData_thenOk() throws Exception {
        String content = getAuthData("alex", "wanrltw");

        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void register_whenInvalidData_thenConflict() throws Exception {
        String content = getAuthData("alex", "wanrltw1");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void register_whenValidData_thenOk() throws Exception {
        String content = getAuthData("mihail", "mihailov");

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username="alexis")
    public void delete_whenInvalidRole_thenForbidden() throws Exception {
        String content = getDeleteData("mihail");

        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void delete_whenValidRoleRequestUnkownUser_thenOk() throws Exception {
        String content = getDeleteData("xxx");

        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void delete_whenValidRole_thenOk() throws Exception {
        String content = getDeleteData("mihail");

        mockMvc.perform(post("/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
    }

    @Test
    public void givenUnknownUser_whenGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenUnknownUser_whenGetUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void givenInvalidRole_whenGetUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis")
    public void givenInvalidRole_whenGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void givenValidRole_whenGetAdmin_thenOk() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alexis")
    public void givenValidRole_whenGetUser_thenOk() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isOk());
    }

    private String getDeleteData(String login) throws JsonProcessingException {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(body);
    }

    private String getAuthData(String login, String password) throws JsonProcessingException {
        Map<String, String> body = new HashMap<>();
        body.put("login", login);
        body.put("password", password);

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(body);
    }

    private String obtainAccessToken(String login, String password) throws Exception {
        String content = getAuthData(login, password);

        ResultActions result
                = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("token").toString();
    }
}
