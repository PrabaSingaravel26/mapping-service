package com.census.migration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MappingResponseColumnsDto {

    private String sourceEHRColumn;

    private String destinationEHRColumn;
}
