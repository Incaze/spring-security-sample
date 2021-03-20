package com.incaze.springsecuritysample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incaze.springsecuritysample.controller.AuthController;
import org.junit.Before;
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
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(classes = SpringSecuritySampleApplication.class)
@AutoConfigureMockMvc
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new AuthController()).build();
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
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        String accessToken = obtainAccessToken("alex", "wanrltw");
        mockMvc.perform(get("/get/user")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenValidRole_whenGetSecureRequest_thenOk() throws Exception {
        String accessToken = obtainAccessToken("adminchik", "adminchik");
        mockMvc.perform(get("/get/user")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void unknownUser_whenGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void unknownUser_whenGetUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void mockUser_whenAdminGetAdmin_thenOk() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="alexis",roles={"ADMIN"})
    public void mockUser_whenAdminGetUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis")
    public void mockUser_whenUserGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username="alexis")
    public void mockUser_whenUserGetUser_thenOk() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isOk());
    }
}
