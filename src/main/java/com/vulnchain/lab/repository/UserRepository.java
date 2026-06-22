package com.vulnchain.lab.repository;

import com.vulnchain.lab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername( String username);
    Optional<User> findByEmail( String email);
    boolean existsByUsername( String username);
}
