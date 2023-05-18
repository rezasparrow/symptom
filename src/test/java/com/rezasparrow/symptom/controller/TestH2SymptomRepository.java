package com.rezasparrow.symptom.controller;

import com.rezasparrow.symptom.model.Symptom;
import org.springframework.data.repository.CrudRepository;

public interface TestH2SymptomRepository extends CrudRepository<Symptom , Long> {
}
