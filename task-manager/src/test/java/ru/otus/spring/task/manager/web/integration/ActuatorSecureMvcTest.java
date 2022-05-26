package ru.otus.spring.task.manager.web.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.liquibase.enabled=false"
)
class ActuatorSecureMvcTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @WithMockUser(
            username = "actuator", authorities = "ACTUATOR"
    )
    void testHealth_Available() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void testHealth_notAuthorize() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().is(401))
                .andReturn();
    }

    @Test
    @WithMockUser(
            username = "user"
    )
    void testHealth_Deni() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().is(403))
                .andReturn();
    }
}
