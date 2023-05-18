package com.rezasparrow.symptom;

import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.model.Symptom;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {

    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
