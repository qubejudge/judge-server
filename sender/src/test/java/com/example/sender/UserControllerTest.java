package com.example.sender;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.aspectj.lang.annotation.Before;

import static org.mockito.Mockito.when;

import com.google.gson.Gson;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sender.controller.SubmissionController;
import com.example.sender.controller.UserController;
import com.example.sender.dto.RegisterRequest;
import com.example.sender.entity.User;
import com.example.sender.services.AuthService;
import com.example.sender.services.JwtService;
import com.example.sender.services.SubmissionService;

// @RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private AuthService authService;

    @Autowired
	private MockMvc mockMvc;

    // @Before
    // public void setup() {
    //     mvc = MockMvcBuilders
    //       .webAppContextSetup(context)
    //       .apply(springSecurity())
    //       .build();
    // }

    // @WithMockUser("spring")
    // @Test
    // public void signUp() throws Exception {
    //     RegisterRequest req = RegisterRequest.builder()     
    //                                          .name("Ishan")
    //                                           .email("email1@email.com")
    //                                           .password("password").build();
        
    //     String reqStr = new Gson().toJson(req);
        
    //     // mockMvc.perform(post("/ap1/v1/register/user"))
    //     //        .contentType(MediaType.APPLICATION_JSON)
               
    //     mockMvc.perform(post("/ap1/v1/submissions")
                
    //            .content(reqStr)
    //            .contentType(MediaType.APPLICATION_JSON))
    //            .andExpect(status().isOk()).andDo(print());
    // }
}
