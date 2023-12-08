package com.example.clean.Repository;

import com.example.clean.Entity.ProductEntity;
import com.example.clean.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmailAndOauthType(String email, String oauthType);
    Optional<UserEntity> findByEmail(String email);

}
