package com.yourcompany.phonebooking;

import com.yourcompany.phonebooking.entity.PhoneDetails;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PhoneDetailsTest extends BaseTest {

    @Override
    protected void setUpMockServer() {
        // no_op
    }

    @Test
    void workingFonoapiTest() throws Exception {
        super.setUpMockServer();

        MvcResult mvcResultAfter = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phone = fromResponse(mvcResultAfter, PhoneDetails.class);

        Assertions.assertThat(phone.getId()).isEqualTo(12);
        Assertions.assertThat(phone.getBookedBy()).isNull();
        Assertions.assertThat(phone.getBookedAt()).isNull();

        Assertions.assertThat(phone.getTechnology()).isEqualTo("TECH");
        Assertions.assertThat(phone.getBands2g()).isEqualTo("2g");
        Assertions.assertThat(phone.getBands3g()).isEqualTo("3g");
        Assertions.assertThat(phone.getBands4g()).isEqualTo("4g");
    }

    @Test
    void fonoapiNotWorking() throws Exception {
        mockServer.expect(ExpectedCount.manyTimes(), requestTo(tokenUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        MvcResult mvcResultAfter = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phone = fromResponse(mvcResultAfter, PhoneDetails.class);

        Assertions.assertThat(phone.getId()).isEqualTo(12);
        Assertions.assertThat(phone.getBookedBy()).isNull();
        Assertions.assertThat(phone.getBookedAt()).isNull();

        Assertions.assertThat(phone.getTechnology()).isNull();
        Assertions.assertThat(phone.getBands2g()).isNull();
        Assertions.assertThat(phone.getBands3g()).isNull();
        Assertions.assertThat(phone.getBands4g()).isNull();
    }

    @Test
    void fonoapiReturnedEmptyResult() throws Exception {
        mockServer.expect(ExpectedCount.manyTimes(), requestTo(tokenUrl))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("TOKEN")
                );
        mockServer.expect(ExpectedCount.manyTimes(), requestTo(deviceUrl))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("[]")
                );

        MvcResult mvcResultAfter = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phone = fromResponse(mvcResultAfter, PhoneDetails.class);

        Assertions.assertThat(phone.getId()).isEqualTo(12);
        Assertions.assertThat(phone.getBookedBy()).isNull();
        Assertions.assertThat(phone.getBookedAt()).isNull();

        Assertions.assertThat(phone.getTechnology()).isNull();
        Assertions.assertThat(phone.getBands2g()).isNull();
        Assertions.assertThat(phone.getBands3g()).isNull();
        Assertions.assertThat(phone.getBands4g()).isNull();
    }
}
