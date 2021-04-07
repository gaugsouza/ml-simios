package com.ml.simios.repository;

import com.ml.simios.domain.Being;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeingRepository extends JpaRepository<Being, Integer> {
}
