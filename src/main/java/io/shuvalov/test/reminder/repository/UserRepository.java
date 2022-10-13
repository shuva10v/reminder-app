package io.shuvalov.test.reminder.repository;

import io.shuvalov.test.reminder.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String username);
}