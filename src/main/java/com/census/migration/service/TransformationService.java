package com.census.migration.service;

import java.util.List;

public interface TransformationService {
    String transformSourceToTargetFile(List<Integer> patient_id);
}
