package com.assignment.inventoryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "Inventory_Details")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE Inventory_Details SET deleted = true WHERE inventory_id=?")
@FilterDef(name = "deletedInventoryFilter", parameters = @ParamDef(name = "isDeleted", type = org.hibernate.type.descriptor.java.BooleanJavaType.class
))
@Filter(name = "deletedInventoryFilter", condition = "deleted = :isDeleted")
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @Column(name = "sku_code", nullable = false)
    private String skuCode;

    private Integer quantity;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "sale_price")
    private double salePrice;

    @Column(name = "minimum_order")
    private Integer minimumOrder;

    private String supplier;

    @Column(name = "discontinued_item")
    private boolean discontinuedItem;

    private boolean deleted = Boolean.FALSE;

}
