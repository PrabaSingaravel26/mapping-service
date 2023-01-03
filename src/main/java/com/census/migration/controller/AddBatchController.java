package com.census.migration.controller;

import com.census.migration.model.BatchData;
import com.census.migration.service.AddBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddBatchController implements AddBatchApi {

    @Autowired
    private AddBatchService addBatchService;

    @Override
    public BatchData saveBatchData(BatchData batchRequest) {
        return addBatchService.addBatchData(batchRequest);
    }

    @Override
    public List<BatchData> getBatchDataList(int clientId) {
        return addBatchService.getBatchDataList(clientId);
    }
}
