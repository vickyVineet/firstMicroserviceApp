package com.assignment.inventoryservice.controller;

import com.assignment.inventoryservice.dto.InventoryRequest;
import com.assignment.inventoryservice.dto.InventoryResponse;
import com.assignment.inventoryservice.dto.StockResponse;
import com.assignment.inventoryservice.entity.InventoryEntity;
import com.assignment.inventoryservice.service.InventoryService;
import com.assignment.inventoryservice.specfication.InventorySpecificationsBuilder;
import com.assignment.inventoryservice.specfication.SearchOperation;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StockResponse> isInStock(@RequestParam List<String> skuCode){

        return inventoryService.isInStock(skuCode);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.createInventory(inventoryRequest);
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("status", 201);
        map.put("message", "Inventory created successfully!");
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllInventories(){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<InventoryResponse> inventories  = inventoryService.getAllCustomers();
        if (!inventories.isEmpty()){
            map.put("status", 200);
            map.put("inventories", inventories);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 0);
            map.put("message", "inventories not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable("id") long id, @RequestBody InventoryEntity inventory){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            InventoryEntity savedInventory = inventoryService.updateInventory(id, inventory);
            map.put("status", 204);
            map.put("savedInventory", savedInventory);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }catch (Exception exception){
            map.clear();
            map.put("status", 400);
            map.put("message", "Unable to update");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable("id") long id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            inventoryService.remove(id);
            map.put("status", 200);
            map.put("message", "Record is deleted successfully!");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            map.clear();
            map.put("status", 400);
            map.put("message", "Data is not deleted");
            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/searchInventory/spec")
    @ResponseBody
    public List<InventoryEntity> findAllBySpecification(@RequestParam(value = "search") String search) {
        InventorySpecificationsBuilder builder = new InventorySpecificationsBuilder();
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(4), matcher.group(3), matcher.group(5));
        }

        Specification<InventoryEntity> spec = builder.build();
        return inventoryService.findAll(spec);

    }

    @GetMapping("/filterSearch")
    public ResponseEntity<?> searchFilter(@RequestBody Map<String,String> body){
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        List<InventoryEntity> inventories = inventoryService.searchFilter(body);
        if (!inventories.isEmpty()){
            map.put("status", 200);
            map.put("inventories", inventories);
            return new ResponseEntity<>(map, HttpStatus.OK);
        }else{
            map.clear();
            map.put("status", 400);
            map.put("message", "inventories is not found");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/update")
//    public ResponseEntity<?> updateItems(@RequestBody List<InventoryEntity> inventoryReqList){
//        Map<String, Object> map = new LinkedHashMap<String, Object>();
//        try {
//            List<InventoryEntity> savedInventories = inventoryService.updateInventory(inventoryReqList);
//            map.put("status", 1);
//            map.put("savedInventories", savedInventories);
//            return new ResponseEntity<>(map, HttpStatus.OK);
//        }catch (Exception exception){
//            map.clear();
//            map.put("status", 0);
//            map.put("message", "Unable to update");
//            return new ResponseEntity<>(map, HttpStatus.NOT_MODIFIED);
//        }
//
//    }


}
