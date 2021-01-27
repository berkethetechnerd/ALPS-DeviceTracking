package com.berkethetechnerd.devicetrackerapi.repository;

import com.berkethetechnerd.devicetrackerapi.entity.SensorData;
import org.springframework.data.repository.CrudRepository;

public interface SensorRepository extends CrudRepository<SensorData, Integer> { }
