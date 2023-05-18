package com.rezasparrow.symptom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rezasparrow.symptom.DataGenerator;
import com.rezasparrow.symptom.SymptomApplication;
import com.rezasparrow.symptom.TestUtils;
import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.model.Symptom;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.rezasparrow.symptom.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(value = SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = SymptomApplication.class
)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties"
)
class SymptomControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestH2SymptomRepository testRepository;
    private List<Symptom> symptoms;
    private final String baseUrl = "/api/v1/symptoms";
    @BeforeEach
    public void beforeEach() {
        symptoms = DataGenerator.symptoms(10);
        testRepository.saveAll(symptoms);
    }

    @Test
    void getRequest_ShouldReturnAllSymptoms() throws Exception {
        MvcResult result = mockMvc.perform(
                        get(baseUrl+"/")
                )
                .andExpect(status().isOk())
                .andReturn();

        var resultContent = result.getResponse()
                .getContentAsString();
        var mapper = new ObjectMapper();
        var dtos = Arrays.stream(mapper.readValue(resultContent, SymptomDto[].class))
                .toList();

        assertSymptomMap(symptoms, dtos);
    }

    @Test
    void getByCode_ShouldItemWithOkStatus() throws Exception {
        var symptom = symptoms.get(0);
        var code = symptom.getCode();
        MvcResult result = mockMvc.perform(
                        get(baseUrl+"/" + code)
                )
                .andExpect(status().isOk())
                .andReturn();

        var resultContent = result.getResponse()
                .getContentAsString();
        var mapper = new ObjectMapper();
        var dto = mapper.readValue(resultContent, SymptomDto.class);

        TestUtils.assertSymptomMap(symptom, dto);
    }

    @Test
    void shouldDeleteAllData() throws Exception {
        mockMvc.perform(delete(baseUrl+"/"))
                .andExpect(status().isAccepted())
                .andReturn();

        var dbSymptoms = testRepository.findAll();
        assertThat(dbSymptoms).isEmpty();
    }

    @AfterEach
    public void cleanTest(){
        testRepository.deleteAll();
    }

}