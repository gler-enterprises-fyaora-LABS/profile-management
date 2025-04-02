package com.fyaora.typeprovider.typeprovider.model.db.repository;

import com.fyaora.typeprovider.typeprovider.model.db.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

    Optional<UserType> findByType(Integer type);
}
