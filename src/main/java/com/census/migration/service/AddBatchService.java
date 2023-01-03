package com.census.migration.service;

import com.census.migration.model.BatchData;

import java.util.List;

public interface AddBatchService {

    BatchData addBatchData(BatchData batchDataRequest);

    List<BatchData> getBatchDataList(int clientId);
}
