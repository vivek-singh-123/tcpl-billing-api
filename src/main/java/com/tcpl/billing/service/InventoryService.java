package com.tcpl.billing.service;

import com.tcpl.billing.dto.InventoryMoveRequest;
import com.tcpl.billing.model.Inventory;
import com.tcpl.billing.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;

    // --- GET inventory with optional filters ---
    public List<Inventory> listInventory(Long projectId, Long materialId) {
        if (projectId != null && materialId != null) {
            return inventoryRepo.findByProjectIdAndMaterialId(projectId, materialId);
        } else if (projectId != null) {
            return inventoryRepo.findByProjectId(projectId);
        } else if (materialId != null) {
            return inventoryRepo.findByMaterialId(materialId);
        } else {
            return inventoryRepo.findAll();
        }
    }

    // --- POST inventory move ---
    public Inventory moveInventory(InventoryMoveRequest req) {
        // Reduce quantity from source location
        Inventory fromInv = inventoryRepo.findByProjectIdAndMaterialId(null, req.getMaterialId())
                .stream()
                .filter(inv -> inv.getLocation().equals(req.getFrom()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No inventory at source location"));

        if (fromInv.getQuantity() < req.getQty()) {
            throw new RuntimeException("Insufficient quantity at source location");
        }
        fromInv.setQuantity(fromInv.getQuantity() - req.getQty());
        inventoryRepo.save(fromInv);

        // Add quantity to destination location
        Inventory toInv = inventoryRepo.findByProjectIdAndMaterialId(null, req.getMaterialId())
                .stream()
                .filter(inv -> inv.getLocation().equals(req.getTo()))
                .findFirst()
                .orElse(new Inventory(null, null, req.getMaterialId(), 0, req.getTo()));

        toInv.setQuantity(toInv.getQuantity() + req.getQty());
        return inventoryRepo.save(toInv);
    }
}
