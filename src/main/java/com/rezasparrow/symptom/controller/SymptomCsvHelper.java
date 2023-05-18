package com.rezasparrow.symptom.controller;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.rezasparrow.symptom.dto.SymptomDto;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

class SymptomCsvHelper {
    private final static DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
    public static List<SymptomDto> readData(InputStream stream) throws IOException {
        var mapper = new CsvMapper();
        mapper.setDateFormat(format);

        return mapper.readerFor(SymptomDto.class)
                .with(CsvSchema.emptySchema().withHeader())
                .readValues(stream)
                .readAll()
                .stream()
                .map(x -> (SymptomDto) x)
                .toList();
    }
}
