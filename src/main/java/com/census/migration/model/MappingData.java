package com.census.migration.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mapping_data")
public class MappingData {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int id;
    private String sourceEHR;
    private String targetEHR;
    private String sourceSheetName;
    private String sourceColumnName;
    private String targetSheetName;
    private String targetColumnName;
    private String requiredField;
}
