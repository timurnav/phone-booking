package com.yourcompany.phonebooking;

import com.yourcompany.phonebooking.entity.Phone;
import com.yourcompany.phonebooking.entity.PhoneDetails;
import com.yourcompany.phonebooking.web.ResponseError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PhonesTest extends BaseTest {

    @Test
    void listPhones() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/phones")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Phone[] phones = fromResponse(mvcResult, Phone[].class);
        Assertions.assertThat(phones)
                .hasSize(10);
        Phone phone = phones[0];

        Assertions.assertThat(phone.getId()).isEqualTo(12);
        Assertions.assertThat(phone.getBrand()).isEqualTo("Samsung");
        Assertions.assertThat(phone.getDevice()).isEqualTo("Galaxy S9");
        Assertions.assertThat(phone.getBookedAt()).isNull();
        Assertions.assertThat(phone.getBookedBy()).isNull();
    }

    @Test
    void bookPhone() throws Exception {
        MvcResult mvcResultBefore = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phoneBefore = fromResponse(mvcResultBefore, PhoneDetails.class);

        Assertions.assertThat(phoneBefore.getId()).isEqualTo(12);
        Assertions.assertThat(phoneBefore.getBookedAt()).isNull();
        Assertions.assertThat(phoneBefore.getBookedBy()).isNull();

        Instant beforeBooking = Instant.now();
        MvcResult mvcResult = mockMvc.perform(put("/phones/12/book")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Phone bookedPhone = fromResponse(mvcResult, Phone.class);

        Assertions.assertThat(bookedPhone.getId()).isEqualTo(12);
        Assertions.assertThat(bookedPhone.getBookedAt()).isBetween(beforeBooking, Instant.now());
        Assertions.assertThat(bookedPhone.getBookedBy()).isNotNull();
        Assertions.assertThat(bookedPhone.getBookedBy().getEmail()).isEqualTo(ADMIN_EMAIL);

        MvcResult mvcResultAfter = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phoneAfter = fromResponse(mvcResultAfter, PhoneDetails.class);

        Assertions.assertThat(phoneAfter.getId()).isEqualTo(bookedPhone.getId());
        Assertions.assertThat(phoneAfter.getBrand()).isEqualTo(bookedPhone.getBrand());
        Assertions.assertThat(phoneAfter.getDevice()).isEqualTo(bookedPhone.getDevice());
        Assertions.assertThat(phoneAfter.getBookedBy())
                .usingRecursiveComparison()
                .isEqualTo(bookedPhone.getBookedBy());
        Assertions.assertThat(phoneAfter.getBookedAt()).isEqualTo(bookedPhone.getBookedAt());
    }

    @Test
    void returnPhone() throws Exception {
        bookPhone();

        MvcResult mvcResult = mockMvc.perform(put("/phones/12/return")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Phone returnedPhone = fromResponse(mvcResult, Phone.class);

        Assertions.assertThat(returnedPhone.getId()).isEqualTo(12);
        Assertions.assertThat(returnedPhone.getBookedAt()).isNull();
        Assertions.assertThat(returnedPhone.getBookedBy()).isNull();

        MvcResult mvcResultAfter = mockMvc.perform(get("/phones/12/details")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        PhoneDetails phoneAfter = fromResponse(mvcResultAfter, PhoneDetails.class);

        Assertions.assertThat(phoneAfter.getId()).isEqualTo(12);
        Assertions.assertThat(phoneAfter.getBookedBy()).isNull();
        Assertions.assertThat(phoneAfter.getBookedAt()).isNull();
    }

    @Test
    void unableToReturnNotBookedPhone() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/phones/12/return")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        ResponseError returnedPhone = fromResponse(mvcResult, ResponseError.class);
        Assertions.assertThat(returnedPhone.getMessage())
                .isEqualTo("Illegal booking user");
    }

    @ParameterizedTest
    @MethodSource("notFoundRequests")
    void phoneNotFound(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        MvcResult mvcResult = mockMvc.perform(requestBuilder
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic(ADMIN_EMAIL, PASSWORD))
                        .accept(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        ResponseError returnedPhone = fromResponse(mvcResult, ResponseError.class);
        Assertions.assertThat(returnedPhone.getMessage())
                .isEqualTo("Unable to find phone by id 67");
    }

    public static Stream<Arguments> notFoundRequests() {
        return Stream.of(
                Arguments.of(get("/phones/67/details")),
                Arguments.of(put("/phones/67/book")),
                Arguments.of(put("/phones/67/return"))
        );
    }
}
