package com.assignment.inventoryservice.repository;

import com.assignment.inventoryservice.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long>, JpaSpecificationExecutor
        <InventoryEntity> {

    List<InventoryEntity> findBySkuCodeIn(List<String> skuCode);


    @Query("SELECT e FROM InventoryEntity e WHERE CONCAT(e.skuCode, ' ', e.quantity, ' ', e.itemName, ' ', e.unitPrice" +
            ", ' ', e.salePrice, ' ', e.minimumOrder, ' ', e.supplier, ' ', e.discontinuedItem, ' ', e.deleted) LIKE %?1%")
    public List<InventoryEntity> search(String keyword);

    Collection<? extends InventoryEntity> findBySkuCode(String skuCode);

    Collection<? extends InventoryEntity> findByQuantity(Integer quantity);

    Collection<? extends InventoryEntity> findByItemName(String itemName);

    Collection<? extends InventoryEntity> findByUnitPrice(double unitPrice);

    Collection<? extends InventoryEntity> findBySalePrice(double salePrice);

    Collection<? extends InventoryEntity> findByMinimumOrder(Integer minimumOrder);

    Collection<? extends InventoryEntity> findBySupplier(String supplier);

    Collection<? extends InventoryEntity> findByDiscontinuedItem(boolean discontinuedItem);

    Collection<? extends InventoryEntity> findByDeleted(boolean deleted);
}
