package com.census.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ehr_mapping")
public class EHRMapping {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int mappingId;
    private String sourceEHR;
    private String targetEHR;
    private String sourceFileName;
    private String sourceSheetName;
    private String sourceFieldName;
    private String mapping;
    private String sourceFieldType;
    private String sourceFieldFormat;
    private String targetFileName;
    private String targetSheetName;
    private String targetFieldName;
    private String targetFieldMandatory;
    private String targetFieldType;
    private String targetFieldFormat;
}
