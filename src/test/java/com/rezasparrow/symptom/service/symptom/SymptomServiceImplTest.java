package com.rezasparrow.symptom.service.symptom;

import com.rezasparrow.symptom.DataGenerator;
import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.exceptions.DuplicateSymptomException;
import com.rezasparrow.symptom.model.Symptom;
import com.rezasparrow.symptom.repository.SymptomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class SymptomServiceImplTest {
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    @Mock
    private SymptomRepository repository;
    private SymptomServiceImpl service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        service = new SymptomServiceImpl(repository);
    }

    @Test
    void addAll_duplicateCodeInInput_ThrowDuplicateSymptomException() {
        var symptoms = DataGenerator.symptomDtos(10);
        var code = symptoms.get(1).getCode();
        symptoms.get(0).setCode(code);

        assertThatThrownBy(() -> service.addAll(symptoms))
                .isInstanceOf(DuplicateSymptomException.class);

    }

    @Test
    void addAll_DuplicateCodeInDatabase_ThrowDuplicateSymptomException() {
        var symptoms = DataGenerator.symptomDtos(10);
        var code = symptoms.get(1).getCode();
        given(repository.findByCode(code))
                .willReturn(Optional.of(DataGenerator.symptom()));

        assertThatThrownBy(() -> service.addAll(symptoms))
                .isInstanceOf(DuplicateSymptomException.class);
    }

    @Test
    void addAll_shouldConvertDtoToEntityAndSaveThem() {
        var dtos = DataGenerator.symptomDtos(1);
        ArgumentCaptor<Iterable<Symptom>> captor = ArgumentCaptor.forClass(Iterable.class);

        service.addAll(dtos);
        verify(repository, VerificationModeFactory.times(1))
                .saveAll(captor.capture());

        var symptoms = StreamSupport.stream(captor.getValue().spliterator(), false)
                .toList();
        assertThat(symptoms).hasSize(1);
        var symptom = symptoms.get(0);
        var dto = dtos.get(0);
        assertSymptomMap(symptom, dto);
    }

    @Test
    void findAll_shouldReturnAllSymptoms() {
        int symptomsCount = 10;
        var dbSymptoms = DataGenerator.symptoms(symptomsCount);
        given(repository.findAll())
                .willReturn(dbSymptoms);
        var symptoms = service.findAll();
        verify(repository, VerificationModeFactory.only())
                .findAll();
        assertThat(symptoms).hasSize(symptomsCount);
        assertSymptomMap(dbSymptoms, symptoms);
    }

    @Test
    void findByCode_ItemWithCodeNotExist_ThrowNoSuchElementException() {
        given(repository.findByCode(any(String.class)))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByCode("Code"))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void deleteAll_ShouldCallDeleteAll() {
        service.deleteAll();
        verify(repository, VerificationModeFactory.only())
                .deleteAll();
    }

    public static void assertSymptomMap(Iterable<Symptom> symptoms, Iterable<SymptomDto> dtos) {
        var sortedDbSymptoms = StreamSupport.stream(symptoms.spliterator(), false)
                .sorted(Comparator.comparing(Symptom::getCode))
                .toList();
        var sortedRetrievedSymptoms = StreamSupport.stream(dtos.spliterator(), false)
                .sorted(Comparator.comparing(SymptomDto::getCode))
                .toList();
        assertThat(sortedDbSymptoms.size()).isEqualTo(sortedRetrievedSymptoms.size());
        for (int i = 0; i < sortedDbSymptoms.size(); i++) {
            var dto = sortedRetrievedSymptoms.get(i);
            var symptom = sortedDbSymptoms.get(i);
            assertSymptomMap(symptom, dto);
        }
    }

    public static void assertSymptomMap(Symptom symptom, SymptomDto dto) {
        assertField("Code", symptom.getCode(), dto.getCode());
        assertField("CodeListCode", symptom.getCodeListCode(), dto.getCodeListCode());
        assertField("LongDescription", symptom.getLongDescription(), dto.getLongDescription());
        assertField("Source", symptom.getSource(), dto.getSource());
        assertField("DisplayValue", symptom.getDisplayValue(), dto.getDisplayValue());
        assertField("SortingPriority", symptom.getSortingPriority(), dto.getSortingPriority());

        assertThat(dateFormat.format(symptom.getToDate()))
                .isEqualTo(dateFormat.format(dto.getToDate()));

        assertThat(dateFormat.format(symptom.getFromDate()))
                .isEqualTo(dateFormat.format(dto.getFromDate()));
    }


    private static void assertField(String fieldName, String actual, String expected) {

        assertThat(actual)
                .as("%s should be %s but is %s", fieldName, expected, actual)
                .isEqualTo(expected);
    }
}
