package com.example.userService.repository;

import com.example.userService.entity.Token;
import com.example.userService.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface TokenRepository extends JpaRepository<Token,Integer> {
    @Query("""
 select t from Token t inner join User u on t.user.id = u.id
 where t.user.id = :userId
 """)
//    @Query("select t from Token t where t.user.id=:userId")
    Optional<Token> findTokensByUser(Integer userId);

    Token findByToken(String token);

    Optional<Token> findByUser(User user);
}
