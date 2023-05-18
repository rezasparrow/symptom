package com.rezasparrow.symptom.service.symptom;

import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.exceptions.DuplicateSymptomException;
import com.rezasparrow.symptom.model.Symptom;
import com.rezasparrow.symptom.repository.SymptomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.*;

@Service
public class SymptomServiceImpl implements SymptomService{
    private final SymptomRepository repository;

    public SymptomServiceImpl(SymptomRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addAll(List<SymptomDto> symptoms) {
        validateDuplicateCodeInInput(symptoms);
        var entities = symptoms.stream()
                .map(SymptomServiceImpl::mapToEntity)
                .toList();
        for(var symptom : entities){
            var oldSymptom = repository.findByCode(symptom.getCode());
            if(oldSymptom.isPresent())
                throw new DuplicateSymptomException(symptom.getCode());
        }

        repository.saveAll(entities);
    }

    private void validateDuplicateCodeInInput(List<SymptomDto> dtos) {
        var exist = dtos.stream()
                .collect(groupingBy(SymptomDto::getCode, counting()))
                .entrySet()
                .stream()
                .filter(x -> x.getValue() > 1)
                .findFirst();

        if (exist.isPresent()) {
            var code = exist.get().getKey();
            throw new DuplicateSymptomException(code);
        }
    }

    private static Symptom mapToEntity(SymptomDto dto){
        return Symptom.builder()
                .displayValue(dto.getDisplayValue())
                .fromDate(dto.getFromDate())
                .toDate(dto.getToDate())
                .code(dto.getCode())
                .longDescription(dto.getLongDescription())
                .codeListCode(dto.getCodeListCode())
                .source(dto.getSource())
                .sortingPriority(dto.getSortingPriority())
                .build();
    }
}
