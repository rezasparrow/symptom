package com.rezasparrow.symptom.repository;

import com.rezasparrow.symptom.model.Symptom;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SymptomRepository extends CrudRepository<Symptom , Long> {
    Optional<Symptom> findByCode(String code);
}
