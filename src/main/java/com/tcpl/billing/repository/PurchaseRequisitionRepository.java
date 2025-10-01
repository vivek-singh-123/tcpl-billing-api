package com.tcpl.billing.repository;

import com.tcpl.billing.model.PurchaseRequisition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRequisitionRepository extends JpaRepository<PurchaseRequisition, Long> {
}
