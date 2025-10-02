package com.fyaora.profilemanagement.profileservice.model.db.repository;

import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long>, JpaSpecificationExecutor<Waitlist> {

    Optional<Waitlist> findByEmailAndEnabled(String email, Boolean status);
}
