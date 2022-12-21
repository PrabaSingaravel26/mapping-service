package com.census.migration.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "target_data")
@TypeDefs({@TypeDef(
        name = "json",
        typeClass = JsonBinaryType.class
)})
@IdClass(TargetDataId.class)
public class TargetData {
    @Id
    @Column(
            name = "target_id"
    )
    private Integer targetId;

    @Id
    private String sheetName;

    @Type(
            type = "json"
    )
    @Column(
            name = "target_data",
            columnDefinition = "json"
    )
    private Map<String,Object> data;
}
