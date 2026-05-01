package com.ticketrush.boundedcontext.user.out;

import com.ticketrush.boundedcontext.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
