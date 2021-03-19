package com.incaze.springsecuritysample;

import com.incaze.springsecuritysample.controller.AuthController;
import com.incaze.springsecuritysample.controller.SecurityController;
import com.incaze.springsecuritysample.repository.UserModelRepository;
import com.incaze.springsecuritysample.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringSecuritySampleApplication.class)
@AutoConfigureMockMvc
class SecurityControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = standaloneSetup(new SecurityController()).build();
    }

    @Test
    public void testInit() throws Exception {
        Assert.assertNotNull(mockMvc);
    }

    @Test
    public void givenNoToken_whenGetAdmin_thenForbidden() throws Exception {
        mockMvc.perform(get("/admin/get"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenNoToken_whenGetUser_thenForbidden() throws Exception {
        mockMvc.perform(get("/user/get"))
                .andExpect(status().isForbidden());
    }
}