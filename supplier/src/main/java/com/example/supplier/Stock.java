package com.example.supplier;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model representing wood stock data sent from supplier.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    private WoodType woodType;
    private Integer quantity;
}