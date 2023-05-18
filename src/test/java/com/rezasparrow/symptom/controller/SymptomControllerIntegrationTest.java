package com.rezasparrow.symptom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rezasparrow.symptom.DataGenerator;
import com.rezasparrow.symptom.SymptomApplication;
import com.rezasparrow.symptom.TestUtils;
import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.exceptions.ErrorMessage;
import com.rezasparrow.symptom.model.Symptom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.rezasparrow.symptom.TestUtils.assertSymptomMap;
import static org.assertj.core.api.Assertions.assertThat;
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
                        get(baseUrl + "/")
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
                        get(baseUrl + "/" + code)
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
        mockMvc.perform(delete(baseUrl + "/"))
                .andExpect(status().isAccepted())
                .andReturn();

        var dbSymptoms = testRepository.findAll();
        assertThat(dbSymptoms).isEmpty();
    }

    @Test
    void uploadFile_shouldUploadFile() throws Exception {
        testRepository.deleteAll();
        var csvData = getClass().getResourceAsStream("/exercise.csv");
        MockMultipartFile file = new MockMultipartFile("file", csvData);

        mockMvc.perform(multipart(baseUrl + "/").file(file))
                .andExpect(status().isAccepted());

        Iterable<Symptom> symptoms = testRepository.findAll();

        assertThat(symptoms).hasSize(18);
        var symptom = StreamSupport.stream(symptoms.spliterator(), false)
                .filter(x -> x.getCode().equals("271636001"))
                .findFirst()
                .orElseThrow();

        assertThat(symptom.getSource()).isEqualTo("ZIB");
        assertThat(symptom.getCodeListCode()).isEqualTo("ZIB001");
        assertThat(symptom.getDisplayValue()).isEqualTo("Polsslag regelmatig");
        assertThat(symptom.getSource()).isEqualTo("ZIB");
        assertThat(symptom.getLongDescription()).isEqualTo("The long description is necessary");
        assertThat(TestUtils.dateFormat.format(symptom.getFromDate())).isEqualTo("01-01-2019");
        assertThat(symptom.getToDate()).isNull();
        assertThat(symptom.getSortingPriority()).isEqualTo("1");
    }

    @Test
    void uploadFile_duplicateCode_shouldUploadFile() throws Exception {
        testRepository.deleteAll();
        var csvData = getClass().getResourceAsStream("/duplicate_code.csv");
        MockMultipartFile file = new MockMultipartFile("file", csvData);

        var resultContent = mockMvc.perform(multipart(baseUrl + "/").file(file))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();


        var mapper = new ObjectMapper();
        var message = mapper.readValue(resultContent, ErrorMessage.class);

        assertThat(message.getMessage()).isEqualTo("Duplicate symptom with code = 271636001");
    }

    @Test
    void findByCode_ItemNotExist_ReturnStatusCodeNotFound() throws Exception {
        var code = "NotExistCode";
        mockMvc.perform(get(baseUrl + "/" + code))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @AfterEach
    public void cleanTest() {
        testRepository.deleteAll();
    }
}