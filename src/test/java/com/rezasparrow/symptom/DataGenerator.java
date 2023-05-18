package com.rezasparrow.symptom;

import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.model.Symptom;
import net.datafaker.Faker;
import net.datafaker.sequence.FakeSequence;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

public class DataGenerator {
    public static List<SymptomDto> symptomDtos(int count) {
        var faker = new Faker();

        FakeSequence<SymptomDto> fakerBuilder = faker.collection(() -> generateSymptomDto(faker))
                .len(count)
                .build();

        var symptoms = StreamSupport.stream(fakerBuilder.spliterator(), false)
                .toList();

        makeCodesUniqueForDtos(symptoms);
        return symptoms;
    }

    public static List<Symptom> symptoms(int count) {
        var faker = new Faker();

        FakeSequence<Symptom> fakerBuilder = faker.collection(() -> generateSymptom(faker))
                .len(count)
                .build();

        var symptoms = StreamSupport.stream(fakerBuilder.spliterator(), false)
                .toList();

        makeCodesUnique(symptoms);
        return symptoms;
    }

    public static Symptom symptom() {
        return symptoms(1).get(0);
    }

    private static Symptom generateSymptom(Faker faker) {
        var from = faker.date().past(1, TimeUnit.DAYS);
       return Symptom
                .builder()
                .code(faker.code().asin())
                .fromDate(from)
                .toDate(faker.date().past(1, TimeUnit.DAYS, from))
                .displayValue(faker.expression("#{regexify '[a-z]{20,30}'}"))
                .longDescription(faker.expression("#{regexify '[a-z]{40,50}'}"))
                .source(faker.expression("#{regexify '[a-z]{40,50}'}"))
                .codeListCode(faker.expression("#{regexify '[a-z]{5,15}'}"))
                .sortingPriority(faker.expression("#{regexify '[1-9]{1,2}'}"))
                .build();
    }


    private static SymptomDto generateSymptomDto(Faker faker) {
        var from = faker.date().past(faker.number().positive(), TimeUnit.DAYS);
        return SymptomDto
                .builder()
                .code(faker.code().asin())
                .fromDate(from)
                .toDate(faker.date().past(faker.number().positive(), TimeUnit.DAYS, from))
                .displayValue(faker.expression("#{regexify '[a-z]{0,30}'}"))
                .longDescription(faker.expression("#{regexify '[a-z]{40,50}'}"))
                .source(faker.expression("#{regexify '[a-z]{1,5}'}"))
                .codeListCode(faker.expression("#{regexify '[a-z]{5,15}'}"))
                .sortingPriority(faker.expression("#{regexify '[1-9]{1,2}'}"))
                .build();
    }


    private static void makeCodesUniqueForDtos(List<SymptomDto> symptoms) {
        for (int i = 0; i < symptoms.size(); i++) {
            var symptom = symptoms.get(i);
            symptom.setCode(symptom.getCode() + i);
        }
    }

    private static void makeCodesUnique(List<Symptom> symptoms) {
        for (int i = 0; i < symptoms.size(); i++) {
            var symptom = symptoms.get(i);
            symptom.setCode(symptom.getCode() + i);
        }
    }
}
