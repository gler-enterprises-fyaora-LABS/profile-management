package com.fyaora.profilemanagement.profileservice.model.db.repository;

import com.fyaora.profilemanagement.profileservice.model.enums.UserTypeEnum;
import com.fyaora.profilemanagement.profileservice.model.db.entity.Waitlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitlistRepository extends JpaRepository<Waitlist, Long> {

    Page<Waitlist> findByUserType(UserTypeEnum userType, Pageable pageable);
    Page<Waitlist> findByUserTypeAndEmailOrTelnum(UserTypeEnum userType, String email, String telnum, Pageable pageable);

    @EntityGraph(attributePaths = "waitlistServiceOffered")
    @Query("SELECT w FROM Waitlist w WHERE w.userType = :userType AND (w.email = :email OR w.telnum = :telnum)")
    List<Waitlist> findForServiceProviderByUserTypeAndEmailOrTelnum(
            @Param("userType") UserTypeEnum userType,
            @Param("email") String email,
            @Param("telnum") String telnum
    );

    @Query("SELECT w FROM Waitlist w WHERE w.userType = :userType AND (w.email = :email OR w.telnum = :telnum)")
    List<Waitlist> findByUserTypeAndEmailOrTelnum(
            @Param("userType") UserTypeEnum userType,
            @Param("email") String email,
            @Param("telnum") String telnum
    );
}
