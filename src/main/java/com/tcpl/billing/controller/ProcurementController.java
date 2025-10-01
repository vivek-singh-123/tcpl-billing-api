package com.tcpl.billing.controller;

import com.tcpl.billing.model.PurchaseOrder;
import com.tcpl.billing.model.PurchaseRequisition;
import com.tcpl.billing.service.ProcurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProcurementController {

    private final ProcurementService procurementService;

    @PostMapping("/purchase-requisitions")
    public PurchaseRequisition createRequisition(@RequestBody PurchaseRequisition requisition) {
        return procurementService.createRequisition(requisition);
    }

    @GetMapping("/purchase-orders")
    public List<PurchaseOrder> getOrders(@RequestParam Long projectId) {
        return procurementService.getOrders(projectId);
    }

    @PostMapping("/purchase-orders/{id}/approve")
    public PurchaseOrder approveOrder(@PathVariable Long id) {
        return procurementService.approveOrder(id);
    }
}
