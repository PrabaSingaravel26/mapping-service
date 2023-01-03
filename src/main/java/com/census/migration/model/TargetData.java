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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Map;

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
            name = "patient_id"
    )
    private Integer patientId;

    @Id
    private String targetSheetName;

    private String targetFileName;

    @Type(
            type = "json"
    )
    @Column(
            name = "target_data",
            columnDefinition = "json"
    )
    private Map<String,Object> targetData;
}
