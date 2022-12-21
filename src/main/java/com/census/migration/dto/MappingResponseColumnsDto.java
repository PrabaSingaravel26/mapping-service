package com.census.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingResponseColumnsDto {

    private String sourceSheetName;

    private String sourceEHRColumn;

    private String destinationSheetName;

    private String destinationEHRColumn;

    private String requiredField;
}
