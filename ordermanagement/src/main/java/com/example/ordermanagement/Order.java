package com.example.ordermanagement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private Long id;
    private WoodType woodType;
    private Integer quantity;
    private String status;
        
}