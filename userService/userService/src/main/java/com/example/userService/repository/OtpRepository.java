package com.example.userService.repository;

import com.example.userService.entity.Otp;
import com.example.userService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface OtpRepository extends JpaRepository<Otp,Integer> {
    @Query("select fp from Otp fp where fp.otp = ?1 and  fp.user = ?2")
    Otp findOtpAndUser(String otp, User user);
}
