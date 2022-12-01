package com.census.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingDto {
    private String sourceEHRType;
    private String destinationEHRType;
    private Map<String,String> mappingFields;
}
