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
import javax.persistence.Table;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "ehr_data")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "sourceId"
)
@TypeDefs({@TypeDef(
        name = "json",
        typeClass = JsonBinaryType.class
)})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EHRData {
    @Id
    @Column(
            name = "source_id"
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private UUID sourceId;
    @Type(
            type = "json"
    )
    @Column(
            name = "source_data",
            columnDefinition = "json"
    )
    private Map<String,Object> data;
}
