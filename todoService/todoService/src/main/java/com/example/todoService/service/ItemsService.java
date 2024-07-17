package com.example.todoService.service;

import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.response.ItemsResponse;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ItemsService {
    ItemsResponse save(ItemsDetails itemsDetails);
    ItemsResponse update(ItemsDetails itemsDetails);
    ItemsResponse delete(int id);
    List<ItemsDetails> search(String title);
    List<ItemsDetails> getAllItems();
}
