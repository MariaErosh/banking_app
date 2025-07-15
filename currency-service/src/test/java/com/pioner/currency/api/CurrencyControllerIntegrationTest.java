package com.pioner.currency.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pioner.currency.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.time.LocalDate;

@WebMvcTest(CurrencyController.class)
public class CurrencyControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CurrencyService currencyService;

    @TestConfiguration
    static class MockBeans {
        @Bean
        public CurrencyService currencyService () {
            return Mockito.mock(CurrencyService.class);
        }
    }

    @Test
    void  shouldReturnRateDividedByBaseCurrencyWhenBaseNotEur() throws Exception {
        // given
        CurrencyRequest request = new CurrencyRequest();
        request.setDate(LocalDate.of(2025, 7, 6));
        request.setIsoCurrency("USD");
        request.setIsoBasedCurrency("RUB");

        when(currencyService.getRateByDate("USD", request.getDate()))
                .thenReturn(BigDecimal.valueOf(0.8500));
        when(currencyService.getRateByDate("RUB", request.getDate()))
                .thenReturn(BigDecimal.valueOf(0.0110));

        // when
        mockMvc.perform(post("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("77.272727"));


        // then
        verify(currencyService, times(1)).getRateByDate("USD", request.getDate());
        verify(currencyService, times(1)).getRateByDate("RUB", request.getDate());
        verify(currencyService, never()).getRateByDate("EUR", request.getDate());
    }

    @Test
    void shouldReturnRateWhenBaseIsEmpty() throws Exception {
        CurrencyRequest request = new CurrencyRequest();
        request.setDate(LocalDate.of(2025, 7, 6));
        request.setIsoCurrency("GBP");

        when (currencyService.getRateByDate("GBP", request.getDate()))
                .thenReturn(BigDecimal.valueOf(1.1572));

        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().string("1.157200"));

        verify(currencyService, times(1)).getRateByDate("GBP", request.getDate());
        verify(currencyService, never()).getRateByDate("EUR", request.getDate());
    }

    void ShouldReturnBadRequestWhenCurrencyIsMissing () throws Exception {
        CurrencyRequest request = new CurrencyRequest();
        request.setDate(LocalDate.of(2025, 6, 7));

        mockMvc.perform(post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
           .andExpect(status().isBadRequest());

    }


}
