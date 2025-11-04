package com.stockinfo.backend.api.v1.cik;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockinfo.backend.entity.CikLookup;
import com.stockinfo.backend.service.CikLookupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CikLookupController.class)
@Import(com.stockinfo.backend.api.v1.cik.CikLookupMapperImpl.class)
class CikLookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CikLookupService cikLookupService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetCikLookupByCik() throws Exception {
        CikLookup cikLookup = new CikLookup(320193, "Apple Inc.");

        when(cikLookupService.findByCik(320193)).thenReturn(Optional.of(cikLookup));

        mockMvc.perform(get("/api/v1/cik-lookup/320193"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cik").value(320193))
                .andExpect(jsonPath("$.companyName").value("Apple Inc."));

        verify(cikLookupService).findByCik(320193);
    }

    @Test
    void testGetCikLookupByCikNotFound() throws Exception {
        when(cikLookupService.findByCik(999999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/cik-lookup/999999"))
                .andExpect(status().isNotFound());

        verify(cikLookupService).findByCik(999999);
    }
}
