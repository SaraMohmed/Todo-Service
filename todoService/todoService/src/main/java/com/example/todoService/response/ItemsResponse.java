package com.example.todoService.response;

import com.example.todoService.entity.ItemsDetails;
import jakarta.persistence.Table;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ItemsResponse {

    private String message;
    private ItemsDetails data;
}
