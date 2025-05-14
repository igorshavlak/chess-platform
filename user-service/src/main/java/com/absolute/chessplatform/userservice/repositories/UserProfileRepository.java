package com.absolute.chessplatform.userservice.repositories;

import com.absolute.chessplatform.userservice.entities.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    List<UserProfile> findByUsernameContainingIgnoreCase(String username);

    List<UserProfile> findByUsernameContainingIgnoreCaseAndUserIdNot(String username, UUID userId);
}
