package com.tcpl.billing.repository;

import com.tcpl.billing.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Yeh import add karein

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByProjectIdAndMaterialId(Long projectId, Long materialId);
    List<Inventory> findByProjectId(Long projectId);
    List<Inventory> findByMaterialId(Long materialId);

    // Yeh naya method add karein jo bug ko fix karega
    Optional<Inventory> findByMaterialIdAndLocation(Long materialId, String location);
}