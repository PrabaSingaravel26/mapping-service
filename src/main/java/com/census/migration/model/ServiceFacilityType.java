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
@Table(name = "service_facility_type")
public class ServiceFacilityType {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int facilityTypeId;
    private String sourceEHR;
    private String targetEHR;
    private String datasetValue;
    private String hchbValue;
}
