package com.example.todoService.service;

import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.repository.ItemsRepository;
import com.example.todoService.response.ItemsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Override
    public ItemsResponse save (ItemsDetails itemsDetails){
        return new ItemsResponse("item added succssfully", itemsRepository.save(itemsDetails));
    }

    @Override
    public ItemsResponse update(ItemsDetails itemsDetails) {
        Optional<ItemsDetails> itemsDetails1 = itemsRepository.findById(itemsDetails.getId());
        if(itemsDetails1.isEmpty()){
            return new ItemsResponse("Item not found",null);
        }
        else {
            return new ItemsResponse("Item updated succssfully", itemsRepository.save(itemsDetails));
        }
    }

    @Override
    public ItemsResponse delete(int id) {
        itemsRepository.deleteById(id);
        return new ItemsResponse("Item deleted successfully",null);
    }

    @Override
    public List<ItemsDetails> search(String description) {
        return itemsRepository.findBydescription(description);
    }

    @Override
    public List<ItemsDetails> getAllItems() {
      return  itemsRepository.findAll();
    }


}
