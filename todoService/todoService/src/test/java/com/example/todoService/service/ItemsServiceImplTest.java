package com.example.todoService.service;

import com.example.todoService.TodoServiceApplication;
import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.entity.Priority;
import com.example.todoService.response.ItemsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TodoServiceApplication.class)
public class ItemsServiceImplTest {

    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void addItemTest(){
        ItemsDetails item = ItemsDetails.builder()
                .description("item 1")
                .status(false)
                .priority(Priority.valueOf("low"))
                .build();
        String url = "http://localhost:8080/service/todo/addItem";
        ResponseEntity<ItemsResponse> response = restTemplate.postForEntity(url,item,ItemsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("item added succssfully", response.getBody().getMessage());
    }

    @Test
    public void updateItemTest(){
        ItemsDetails item = ItemsDetails.builder()
                .id(2)
                .description("item 2")
                .status(false)
                .priority(Priority.valueOf("high"))
                .build();
        String url = "http://localhost:8080/service/todo/updateItem";
        HttpEntity<ItemsDetails> requestEntity = new HttpEntity<>(item);
        ResponseEntity<ItemsResponse> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, ItemsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("item updated succssfully", response.getBody().getMessage());
    }

    @Test
    public void deleteItemTest() {
        String url = "http://localhost:8080/service/todo/deleteItem?id=3";

        ResponseEntity<ItemsResponse> response = restTemplate.exchange(url, HttpMethod.DELETE, null, ItemsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals("Item deleted successfully", response.getBody().getMessage());

    }

    @Test
    public void getAllItemsTest() {
        String url = "http://localhost:8080/service/todo/getAllItems";

        ResponseEntity <List<ItemsResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ItemsResponse>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        

    }

}