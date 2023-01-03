package com.census.migration.service;

import com.census.migration.model.BatchData;
import com.census.migration.repository.AddBatchDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddBatchServiceImpl implements AddBatchService{

    @Autowired
    private AddBatchDataRepository addBatchDataRepository;

    @Override
    public BatchData addBatchData(BatchData batchDataRequest) {
        return addBatchDataRepository.save(batchDataRequest);
    }

    @Override
    public List<BatchData> getBatchDataList(int clientId) {
        return addBatchDataRepository.getByClientId(clientId);
    }
}
