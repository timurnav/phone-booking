package com.yourcompany.phonebooking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.yourcompany.phonebooking.service.fonoapi.DeviceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = Postgres.Initializer.class)
@Transactional
public class BaseTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new JavaTimeModule());

    public static final String ADMIN_EMAIL = "admin@admin.com";
    public static final String PASSWORD = "password";

    @Value("${fonoapi.token-url}")
    protected String tokenUrl;
    @Value("${fonoapi.device-url}")
    protected String deviceUrl;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private RestTemplate restTemplate;

    protected MockRestServiceServer mockServer;

    @BeforeEach
    public void init() throws Exception {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        setUpMockServer();
    }

    protected void setUpMockServer() throws Exception {
        mockServer.expect(ExpectedCount.manyTimes(), requestTo(tokenUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("TOKEN")
                );
        DeviceEntity entity = new DeviceEntity();
        entity.setBrand("BRAND");
        entity.setDeviceName("DEVICE_NAME");
        entity.setTechnology("TECH");
        entity.set_2g_bands("2g");
        entity.set_3g_bands("3g");
        entity.set_4g_bands("4g");
        mockServer.expect(ExpectedCount.manyTimes(), requestTo(deviceUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(OBJECT_MAPPER.writeValueAsString(List.of(entity)))
                );
    }

    public <T> T fromResponse(MvcResult result, Class<T> type) throws Exception {
        return OBJECT_MAPPER.readerFor(type).readValue(result.getResponse().getContentAsString());
    }
}
