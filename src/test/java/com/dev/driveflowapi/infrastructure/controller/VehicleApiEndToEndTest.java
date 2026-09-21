package com.dev.driveflowapi.infrastructure.controller;

import com.dev.driveflowapi.application.dto.output.address.AddressOutput;
import com.dev.driveflowapi.application.port.out.ZipCodeGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:e2e-test;MODE=PostgreSQL;NON_KEYWORDS=YEAR;DB_CLOSE_DELAY=-1;DATABASE_TO_LOWER=TRUE",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=validate"
})
@AutoConfigureMockMvc
class VehicleApiEndToEndTest {

    @Autowired private MockMvc mockMvc;

    @MockitoBean private ZipCodeGateway zipCodeGateway;

    @Test
    void createsDealerThenVehicleAndListsItByDealer() throws Exception {
        when(zipCodeGateway.findByZipCode("01001000"))
                .thenReturn(new AddressOutput("01001-000", "Praça da Sé, Sé, São Paulo - SP"));

        String dealerBody = """
                {"corporateName":"Driveflow Motors","cnpj":"12345678000195","zipCode":"01001000","number":"10"}
                """;
        String dealerResponse = mockMvc.perform(post("/dealer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dealerBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address").value("Praça da Sé, Sé, São Paulo - SP"))
                .andReturn().getResponse().getContentAsString();
        String dealerId = com.jayway.jsonpath.JsonPath.read(dealerResponse, "$.id");

        String vehicleBody = """
                {"brand":"Honda","model":"Civic","fuelTypes":["FLEX"],"color":"White","year":2024,"price":120000.00,"dealerId":"%s"}
                """.formatted(dealerId);

        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vehicleBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dealerId").value(dealerId))
                .andExpect(jsonPath("$.dealerName").value("Driveflow Motors"));

        mockMvc.perform(get("/vehicles").param("dealerId", dealerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].model").value("Civic"));
    }

    @Test
    void rejectsVehicleWithoutRequiredFields() throws Exception {
        mockMvc.perform(post("/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."));
    }
}
