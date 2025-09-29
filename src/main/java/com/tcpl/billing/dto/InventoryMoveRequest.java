package com.tcpl.billing.dto;

import lombok.Data;

@Data
public class InventoryMoveRequest {
    private Long materialId;
    private double qty;
    private String from;
    private String to;
    private String refType; // e.g., "invoice", "transfer"
    private Long refId;
}
