package com.tcpl.billing.controller;

import com.tcpl.billing.dto.InventoryMoveRequest;
import com.tcpl.billing.model.Inventory;
import com.tcpl.billing.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // --- GET /api/inventory?projectId=&materialId= ---
    @GetMapping
    public ResponseEntity<List<Inventory>> listInventory(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long materialId
    ) {
        return ResponseEntity.ok(inventoryService.listInventory(projectId, materialId));
    }

    // --- POST /api/inventory/move ---
    @PostMapping("/move")
    public ResponseEntity<Inventory> moveInventory(@RequestBody InventoryMoveRequest req) {
        Inventory moved = inventoryService.moveInventory(req);
        return ResponseEntity.ok(moved);
    }
}
