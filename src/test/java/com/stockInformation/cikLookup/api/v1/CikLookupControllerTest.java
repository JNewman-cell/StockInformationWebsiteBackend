package com.stockInformation.cikLookup.api.v1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.stockInformation.cikLookup.entity.CikLookup;
import com.stockInformation.cikLookup.service.CikLookupService;

import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CikLookupController.class)
@Import({
    com.stockInformation.cikLookup.api.v1.CikLookupMapperImpl.class,
    com.stockInformation.config.SecurityConfig.class
})
class CikLookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CikLookupService cikLookupService;

    @Test
    @WithMockUser
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
    @WithMockUser
    void testGetCikLookupByCikNotFound() throws Exception {
        when(cikLookupService.findByCik(999999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/cik-lookup/999999"))
                .andExpect(status().isNotFound());

        verify(cikLookupService).findByCik(999999);
    }
}
