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
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mapping_table")
@TypeDefs({@TypeDef(
        name = "json",
        typeClass = JsonBinaryType.class
)})
public class MappingTable {
    @Id
    private String sourceEHR;
    private String destinationEHR;
    @Type(
            type = "json"
    )
    @Column(
            name = "source_target_column_map",
            columnDefinition = "json"
    )
    private Map<String, String> sourceTargetColumnMap;
}
