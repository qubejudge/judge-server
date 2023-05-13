package com.example.sender;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.sender.controller.SubmissionController;
import com.example.sender.dto.RegisterRequest;
import com.example.sender.entity.User;
import com.example.sender.services.JwtService;
import com.example.sender.services.SubmissionService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// @WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc
public class SubmissionControllerTest {
    
    @Autowired
    private SubmissionController submissionController;

    @Autowired
    private JwtService jwtService;

    // @Value(value="${local.server.port}")
	// private String port;

	// @Autowired
	// private TestRestTemplate restTemplate;

    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private SubmissionService service;

    // @Test
	// public void greetingShouldReturnDefaultMessage() throws Exception {
    //     String endpoint = "http://localhost:" + port + "/hi";
    //     System.out.println(endpoint);
	// 	assertThat(this.restTemplate.getForObject(endpoint,
	// 			String.class)).contains("Hello, World!");
	// }

    @Test
	public void shouldReturnDefaultMessage() throws Exception {
        //when(service.getSubmissionsByUserID());
		// this.mockMvc.perform(get("/api/v1/hii")).andDo(print()).andExpect(status().isOk())
		// 		.andExpect(content().string(containsString("Hello, World")));
        mockMvc.perform(get("/api/v1/hii"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Hello, World")));
	}

    

    @Test
    public void getEmptyListOfSubmissions() throws Exception {
        mockMvc.perform(get("/api/v1/submissions"))
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("")));
    }

    // @Test
    // public void signUp() throws Exception {
    //     RegisterRequest req = RegisterRequest.builder()     
    //                                          .name("Ishan")
    //                                           .email("email1@email.com")
    //                                           .password("password").build();
        
    //     String reqStr = new Gson().toJson(req);
        
    //     // mockMvc.perform(post("/ap1/v1/register/user"))
    //     //        .contentType(MediaType.APPLICATION_JSON)
               
    //     mockMvc.perform(post("/ap1/v1/register/user")
    //            .content(reqStr)
    //            .contentType(MediaType.APPLICATION_JSON))
    //            .andExpect(status().isOk()).andDo(print());
    // }


}
