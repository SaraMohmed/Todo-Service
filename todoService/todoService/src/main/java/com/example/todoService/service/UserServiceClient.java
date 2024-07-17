package com.example.todoService.service;


import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserServiceClient {

    @Autowired
    private RestTemplate restTemplate ;

    @Autowired
    private ItemsRepository itemsRepository;



//    public ItemsDetails saveItems(ItemsDetails itemsDetails,String token ) {
//        String url = "http://localhost:8081/service/token/getToken/";
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer "+token);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//
//        ItemsDetails itemDetails = ItemsDetails.builder()
//
//                .userId(itemsDetails.getUserId())
//                .description(itemsDetails.getDescription())
//                .createdAt(itemsDetails.getCreatedAt())
//                .priority(itemsDetails.getPriority())
//                .status(itemsDetails.isStatus())
//                .build();
//
//        return itemsRepository.save(itemDetails);
//    }

    public ItemsDetails saveItems(ItemsDetails itemsDetails) {
        String tokenUrl = "http://localhost:8081/service/token/getToken/";

        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.GET, null, String.class);

        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
            String token = tokenResponse.getBody();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ItemsDetails itemDetails = ItemsDetails.builder()
                    .userId(itemsDetails.getUserId())
                    .description(itemsDetails.getDescription())
                    .createdAt(itemsDetails.getCreatedAt())
                    .priority(itemsDetails.getPriority())
                    .status(itemsDetails.isStatus())
                    .build();

            return itemsRepository.save(itemDetails);
        } else {
            throw new RuntimeException("Failed to retrieve token, status code: " + tokenResponse.getStatusCode());
        }

    }


//    private static final Logger logger = LoggerFactory.getLogger(UserServiceClient.class);
//
//    public ItemsDetails saveItems(ItemsDetails itemsDetails) {
//        String tokenUrl = "http://localhost:8081/service/token/getToken/";
//
//        logger.info("Requesting token from {}", tokenUrl);
//        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.GET, null, String.class);
//        logger.info("Received response: {}", tokenResponse);
//
//        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
//            String token = tokenResponse.getBody();
//            logger.info("Retrieved token: {}", token);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("Authorization", "Bearer " + token);
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            ItemsDetails itemDetails = ItemsDetails.builder()
//                    .userId(itemsDetails.getUserId())
//                    .description(itemsDetails.getDescription())
//                    .createdAt(itemsDetails.getCreatedAt())
//                    .priority(itemsDetails.getPriority())
//                    .status(itemsDetails.isStatus())
//                    .build();
//
//            logger.info("Saving item details: {}", itemDetails);
//            return itemsRepository.save(itemDetails);
//        } else {
//            logger.error("Failed to retrieve token, status code: {}", tokenResponse.getStatusCode());
//            throw new RuntimeException("Failed to retrieve token, status code: " + tokenResponse.getStatusCode());
//        }
//    }
}
