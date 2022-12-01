package com.census.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "source_data")
public class SourceData {
    @Id
    private int patientId;
    private String name;
    private String address;
    private String state;
    private int zipCode;
    private String county;
    private Long mobileNumber;
    private String gender;
}
