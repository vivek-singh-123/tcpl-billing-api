package com.tcpl.billing.dto;

import lombok.Data;

@Data
public class PurchaseRequisitionRequest {
    private Long projectId;
    private String materialName;
    private Integer quantity;
    private String unit;
    private String requestedBy;
    private String remarks;
}
