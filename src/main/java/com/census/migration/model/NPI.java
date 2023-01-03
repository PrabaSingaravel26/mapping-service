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
@Table(name = "npi")
public class NPI {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int npiId;
    private String sourceEHR;
    private String targetEHR;
    private String lastName;
    private String firstName;
    private String npi;
}