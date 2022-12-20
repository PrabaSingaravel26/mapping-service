package com.census.migration.service;

import java.util.UUID;

public interface TransformationService {
    String transformSourceToTargetFile(int patient_id);
}
