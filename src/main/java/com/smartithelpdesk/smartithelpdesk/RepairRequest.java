package com.smartithelpdesk.smartithelpdesk;

public class RepairRequest {

    // Private attributes
    private String repairId;
    private String assetId;
    private double repairCost;
    private String status;

    // Constructor
    public RepairRequest(String repairId, String assetId,
            double repairCost, String status) {

        setRepairId(repairId);
        setAssetId(assetId);
        setRepairCost(repairCost);
        setStatus(status);
    }

    // Getters
    public String getRepairId() {
        return repairId;
    }

    public String getAssetId() {
        return assetId;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public String getStatus() {
        return status;
    }

    // Setters with validation
    public void setRepairId(String repairId) {
        if (repairId == null || repairId.isBlank()) {
            throw new IllegalArgumentException(
                    "Repair ID cannot be blank"
            );
        }
        this.repairId = repairId;
    }

    public void setAssetId(String assetId) {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException(
                    "Asset ID cannot be blank"
            );
        }
        this.assetId = assetId;
    }

    public void setRepairCost(double repairCost) {
        if (!Double.isFinite(repairCost) || repairCost < 0) {
            throw new IllegalArgumentException(
                    "Repair cost cannot be negative or invalid"
            );
        }
        this.repairCost = repairCost;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Status cannot be blank"
            );
        }
        this.status = status;
    }
}
