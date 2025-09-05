package com.fyaora.profilemanagement.profileservice.model.db.repository;

import com.fyaora.profilemanagement.profileservice.model.db.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, JpaSpecificationExecutor<Inquiry> {
}
