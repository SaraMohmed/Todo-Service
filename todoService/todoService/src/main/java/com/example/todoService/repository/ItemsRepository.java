package com.example.todoService.repository;

import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.response.ItemsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<ItemsDetails,Integer> {

    @Query("SELECT i FROM ItemsDetails i WHERE i.description LIKE %?1%"
            + " OR i.description LIKE %?1%")
    public List<ItemsDetails> findBydescription(String description);



}
