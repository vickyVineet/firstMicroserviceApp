package com.assignment.inventoryservice.service;

import com.assignment.inventoryservice.dto.InventoryRequest;
import com.assignment.inventoryservice.dto.InventoryResponse;
import com.assignment.inventoryservice.dto.StockResponse;
import com.assignment.inventoryservice.entity.InventoryEntity;
import com.assignment.inventoryservice.repository.InventoryRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public List<StockResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory -> StockResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity()>0)
                        .build()).toList();
    }

    public void createInventory(InventoryRequest inventoryRequest){
        InventoryEntity inventory = InventoryEntity.builder()
                .quantity(inventoryRequest.getQuantity())
                .skuCode(inventoryRequest.getSkuCode())
                .discontinuedItem(inventoryRequest.isDiscontinuedItem())
                .itemName(inventoryRequest.getItemName())
                .minimumOrder(inventoryRequest.getMinimumOrder())
                .salePrice(inventoryRequest.getSalePrice())
                .supplier(inventoryRequest.getSupplier())
                .unitPrice(inventoryRequest.getUnitPrice())
                .build();

        InventoryEntity savedInventory = inventoryRepository.save(inventory);

        log.info("Inventory {} is saved",savedInventory.getInventoryId());
    }

    public List<InventoryResponse> getAllCustomers() {
//        Session session = entityManager.unwrap(Session.class);
//        Filter filter = session.enableFilter("deletedInventoryFilter");
//        filter.setParameter("isDeleted", isDeleted);
        List<InventoryEntity> inventories =  inventoryRepository.findAll();
//        session.disableFilter("deletedInventoryFilter");
        return inventories.stream().map(this::entityToResponse).toList();
    }

    private InventoryResponse entityToResponse(InventoryEntity inventoryEntity) {
        return InventoryResponse.builder()
                .inventoryId(inventoryEntity.getInventoryId())
                .skuCode(inventoryEntity.getSkuCode())
                .salePrice(inventoryEntity.getSalePrice())
                .supplier(inventoryEntity.getSupplier())
                .unitPrice(inventoryEntity.getUnitPrice())
                .discontinuedItem(inventoryEntity.isDiscontinuedItem())
                .itemName(inventoryEntity.getItemName())
                .minimumOrder(inventoryEntity.getMinimumOrder())
                .quantity(inventoryEntity.getQuantity())
                .deleted(inventoryEntity.isDeleted())
                .build();
    }

    public InventoryEntity updateInventory(long id, InventoryEntity inventoryReqObj){
        Optional<InventoryEntity> inventoryDataFromRepo = inventoryRepository.findById(id);
        InventoryEntity savedInventory= null;

        if (inventoryDataFromRepo.isPresent()){
            InventoryEntity inventory = inventoryDataFromRepo.get();
            inventory.setSkuCode(inventoryReqObj.getSkuCode());
            inventory.setQuantity(inventoryReqObj.getQuantity());
            inventory.setDeleted(inventoryReqObj.isDeleted());
            inventory.setItemName(inventoryReqObj.getItemName());
            inventory.setSupplier(inventoryReqObj.getSupplier());
            inventory.setDiscontinuedItem(inventoryReqObj.isDiscontinuedItem());
            inventory.setMinimumOrder(inventoryReqObj.getMinimumOrder());
            inventory.setSalePrice(inventoryReqObj.getSalePrice());
            inventory.setUnitPrice(inventoryReqObj.getUnitPrice());
//            inventory.setInventoryId(inventoryReqObj.getInventoryId());
            savedInventory = inventoryRepository.save(inventory);
        }

        return savedInventory;
    }

    public void remove(Long id){
        inventoryRepository.deleteById(id);
    }


    public List<InventoryEntity> findAll(Specification<InventoryEntity> spec) {
        return inventoryRepository.findAll(spec);
    }

    public InventoryEntity getInventoryById(long id) {
        InventoryEntity inventory = new InventoryEntity();
        Optional<InventoryEntity> inventoryOpt = inventoryRepository.findById(id);
        if (inventoryOpt.isPresent())    inventory = inventoryOpt.get();
        return inventory;
    }

    public List<InventoryEntity> searchFilter(Map<String,String> filter){
        List<InventoryEntity> inventoryEntityList = new ArrayList<>();

        filter.forEach((k,v) -> {
            switch (k){
                case "skuCode" -> inventoryEntityList.addAll(inventoryRepository.findBySkuCode(v));
                case "quantity" -> inventoryEntityList.addAll(inventoryRepository.findByQuantity(Integer.valueOf(v)));
                case "itemName" -> inventoryEntityList.addAll(inventoryRepository.findByItemName(v));
                case "unitPrice" -> inventoryEntityList.addAll(inventoryRepository.findByUnitPrice(Double.parseDouble(v)));
                case "salePrice" -> inventoryEntityList.addAll(inventoryRepository.findBySalePrice(Double.parseDouble(v)));
                case "minimumOrder" -> inventoryEntityList.addAll(inventoryRepository.findByMinimumOrder(Integer.valueOf(v)));
                case "supplier" -> inventoryEntityList.addAll(inventoryRepository.findBySupplier(v));
                case "discontinuedItem" -> inventoryEntityList.addAll(inventoryRepository.findByDiscontinuedItem(Boolean.parseBoolean(v)));
                case "deleted" -> inventoryEntityList.addAll(inventoryRepository.findByDeleted(Boolean.parseBoolean(v)));
                default -> throw new IllegalArgumentException("Unknown operator filter");
            }
        });

        return inventoryEntityList;
    }

//    public List<InventoryEntity> updateInventory(List<InventoryEntity> inventoryListReq){
//
//        InventoryEntity savedInventory= null;
//        List<InventoryEntity> updatedInventoryResponse = new ArrayList<>();
//
//        for (InventoryEntity inv : inventoryListReq) {
//            Optional<InventoryEntity> inventoryDataFromRepo = inventoryRepository.findById(inv.getInventoryId());
//            if (inventoryDataFromRepo.isPresent()){
//                InventoryEntity inventory = inventoryDataFromRepo.get();
//                inventory.setQuantity(inv.getQuantity());
////            inventory.setSkuCode(inventoryListReq.getSkuCode()); //should not update skuCode
//                savedInventory = inventoryRepository.save(inventory);
//
//            }
//
//            updatedInventoryResponse.add(savedInventory);
//        }
//
//        return updatedInventoryResponse;
//
//    }
}
