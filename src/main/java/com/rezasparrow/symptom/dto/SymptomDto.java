package com.rezasparrow.symptom.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SymptomDto {
    private String source;
    private String codeListCode;
    private String code;
    private String displayValue;
    private String longDescription;
    private Date fromDate;
    private Date toDate;
    private String sortingPriority;
}