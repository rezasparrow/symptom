package com.rezasparrow.symptom.service.symptom;

import com.rezasparrow.symptom.dto.SymptomDto;

import java.util.List;

public interface SymptomService {
    void addAll(List<SymptomDto> symptoms);
}
