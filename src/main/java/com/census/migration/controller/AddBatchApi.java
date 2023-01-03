package com.census.migration.controller;

import com.census.migration.model.BatchData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/migration")
public interface AddBatchApi {
    @PostMapping("/add_batch")
    BatchData saveBatchData(@RequestBody BatchData batchRequest);
    
    @GetMapping("/get_batch_data")
    List<BatchData> getBatchDataList(@RequestParam("clientId") int clientId);
}
