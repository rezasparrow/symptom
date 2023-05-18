package com.rezasparrow.symptom.controller;

import com.rezasparrow.symptom.dto.SymptomDto;
import com.rezasparrow.symptom.service.symptom.SymptomService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/symptoms")
public class SymptomController {
    private final SymptomService service;

    @Autowired
    public SymptomController(SymptomService service) {
        this.service = service;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all symptoms")
    public List<SymptomDto> getAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get symptom by code")
    @ResponseStatus(HttpStatus.OK)
    public SymptomDto getAll(@PathVariable("code") String code) {
        return service.findByCode(code);
    }

    @DeleteMapping(value = "/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Delete all symptoms")
    public void deleteAll() {
        service.deleteAll();
    }
    @PostMapping(value = "/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        List<SymptomDto> dtos = SymptomCsvHelper.readData(file.getInputStream());
        service.addAll(dtos);
    }
}
