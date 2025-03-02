package com.example.ordermanagement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private WoodType woodType;
    private Integer quantity;       
}