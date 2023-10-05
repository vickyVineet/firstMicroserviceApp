package com.assignment.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {

    private Long inventoryId;

    private String skuCode;

    private Integer quantity;

    private String itemName;

    private double unitPrice;

    private double salePrice;

    private Integer minimumOrder;

    private String supplier;

    private boolean discontinuedItem;

    private boolean deleted = Boolean.FALSE;
}
