package com.fyaora.profilemanagement.profileservice.model.db.repository;

import com.fyaora.profilemanagement.profileservice.model.db.entity.ServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesOfferedRepository extends JpaRepository<ServiceOffered, Integer> {
}
