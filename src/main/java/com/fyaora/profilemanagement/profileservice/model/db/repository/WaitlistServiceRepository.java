package com.fyaora.profilemanagement.profileservice.model.db.repository;

import com.fyaora.profilemanagement.profileservice.model.db.entity.WaitlistServiceOffered;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitlistServiceRepository extends JpaRepository<WaitlistServiceOffered, Long> {
}
