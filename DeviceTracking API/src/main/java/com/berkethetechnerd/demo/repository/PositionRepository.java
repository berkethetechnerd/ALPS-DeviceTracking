package com.berkethetechnerd.demo.repository;

import com.berkethetechnerd.demo.entity.Position;
import org.springframework.data.repository.CrudRepository;

public interface PositionRepository extends CrudRepository<Position, Integer> { }
