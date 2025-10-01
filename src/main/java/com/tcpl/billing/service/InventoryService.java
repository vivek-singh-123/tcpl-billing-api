package com.tcpl.billing.service;

import com.tcpl.billing.dto.InventoryMoveRequest;
import com.tcpl.billing.model.Inventory;
import com.tcpl.billing.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Yeh import add karein

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepo;

    // --- GET inventory with optional filters ---
    // Is method mein koi change nahi hai
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
    // Yeh poora method update kiya gaya hai
    @Transactional
    public Inventory moveInventory(InventoryMoveRequest req) {
        // --- SOURCE INVENTORY ---
        // Puraane, buggy code ko is naye code se replace kiya gaya hai
        Inventory fromInv = inventoryRepo.findByMaterialIdAndLocation(req.getMaterialId(), req.getFrom())
                .orElseThrow(() -> new RuntimeException(
                        "No inventory at source location for materialId: " + req.getMaterialId() + " and location: " + req.getFrom()
                ));

        if (fromInv.getQuantity() < req.getQty()) {
            throw new RuntimeException("Insufficient quantity at source location: " + req.getFrom());
        }
        fromInv.setQuantity(fromInv.getQuantity() - req.getQty());

        // --- DESTINATION INVENTORY ---
        // Puraane, buggy code ko is naye code se replace kiya gaya hai
        // Agar destination par stock nahi hai, toh ek naya record banega
        Inventory toInv = inventoryRepo.findByMaterialIdAndLocation(req.getMaterialId(), req.getTo())
                .orElse(new Inventory(null, null, req.getMaterialId(), 0.0, req.getTo()));

        toInv.setQuantity(toInv.getQuantity() + req.getQty());

        // Dono changes ko transaction mein save karein
        inventoryRepo.save(fromInv);
        return inventoryRepo.save(toInv);
    }
}