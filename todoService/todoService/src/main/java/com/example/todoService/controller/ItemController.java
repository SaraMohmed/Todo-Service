package com.example.todoService.controller;

import com.example.todoService.entity.ItemsDetails;
import com.example.todoService.repository.ItemsRepository;
import com.example.todoService.response.ItemsResponse;
import com.example.todoService.service.ItemsService;

import com.example.todoService.service.UserServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("service/todo")
public class ItemController {

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private ItemsRepository itemsRepository;

    @Autowired
    private UserServiceClient userServiceClient;



    @PostMapping("/createItem")
    public ItemsDetails createItem(@RequestBody ItemsDetails item) {
        return userServiceClient.saveItems(item);

    }


    @Operation(summary = "add new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "item added succssfully",
                    content = @Content(schema = @Schema(implementation = ItemsService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addItem")
    public ItemsResponse save(@RequestBody ItemsDetails itemsDetails){
        return itemsService.save(itemsDetails);
    }




    @Operation(summary = "update item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated succssfully",
                    content = @Content(schema = @Schema(implementation = ItemsService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/updateItem")
    public ItemsResponse update(@RequestBody ItemsDetails itemsDetails){
        return itemsService.update(itemsDetails);
    }




    @Operation(summary = "delete item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully",
                    content = @Content(schema = @Schema(implementation = ItemsService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/deleteItem")
    public ItemsResponse delete(@RequestParam("id") int id){
        return itemsService.delete(id);
    }





    @Operation(summary = "search any item by description")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " ",
                    content = @Content(schema = @Schema(implementation = ItemsService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/searchItem")
    public List<ItemsDetails> search(@RequestParam("des") String description){
        return itemsService.search(description);
    }




    @Operation(summary = "get all the items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = " ",
                    content = @Content(schema = @Schema(implementation = ItemsService.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllItems")
    public List<ItemsDetails> getAllItems() {
        return  itemsService.getAllItems();
    }
}
