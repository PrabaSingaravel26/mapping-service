package com.census.migration.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class TargetDataId implements Serializable {
    private static final long serialVersionUID = 2702030623316532366L;

    private Integer targetId;

    private String sheetName;

}
