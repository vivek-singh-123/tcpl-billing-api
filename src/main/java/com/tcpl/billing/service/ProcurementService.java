package com.tcpl.billing.service;

import com.tcpl.billing.model.PurchaseOrder;
import com.tcpl.billing.model.PurchaseRequisition;
import com.tcpl.billing.repository.PurchaseOrderRepository;
import com.tcpl.billing.repository.PurchaseRequisitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcurementService {

    private final PurchaseRequisitionRepository requisitionRepo;
    private final PurchaseOrderRepository orderRepo;

    public PurchaseRequisition createRequisition(PurchaseRequisition req) {
        return requisitionRepo.save(req);
    }

    public List<PurchaseOrder> getOrders(Long projectId) {
        return orderRepo.findByProjectId(projectId);
    }

    public PurchaseOrder approveOrder(Long id) {
        PurchaseOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus("approved");
        return orderRepo.save(order);
    }
}
