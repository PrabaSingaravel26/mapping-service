package com.census.migration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "batch_data")
public class BatchData implements Serializable {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private int batchId;
    private int clientId;
    private String serviceLine;
    private String sourceEHR;
    private String destinationEHR;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date goLiveDate;
    @ElementCollection(targetClass = String.class)
    private List<String> processes;
}
