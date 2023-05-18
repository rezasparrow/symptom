package com.rezasparrow.symptom.model;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Symptom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String source;
    private String codeListCode;
    private String code;
    private String displayValue;
    private String longDescription;
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Temporal(TemporalType.DATE)
    private Date toDate;
    private String sortingPriority;
}
