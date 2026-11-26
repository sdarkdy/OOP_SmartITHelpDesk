package com.smartithelpdesk.smartithelpdesk;

public class AssetRecord {

    private final String assetId;
    private String description;
    private double purchaseValue;
    private double repairCost;
    private int quantity;
    private String status;

    // Constructor to initialize an asset
    public AssetRecord(String assetId, String description,
            double purchaseValue, double repairCost,
            int quantity, String status) {

        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException("Asset ID cannot be blank");
        }

        this.assetId = assetId;
        setDescription(description);
        setPurchaseValue(purchaseValue);
        setRepairCost(repairCost);
        setQuantity(quantity);
        setStatus(status);
    }

    // Getters
    public String getAssetId() {
        return assetId;
    }

    public String getDescription() {
        return description;
    }

    public double getPurchaseValue() {
        return purchaseValue;
    }

    public double getRepairCost() {
        return repairCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    // Setters with validation
    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
        this.description = description;
    }

    public void setPurchaseValue(double purchaseValue) {
        if (!Double.isFinite(purchaseValue) || purchaseValue < 0) {
            throw new IllegalArgumentException("Invalid purchase value");
        }
        this.purchaseValue = purchaseValue;
    }

    public void setRepairCost(double repairCost) {
        if (!Double.isFinite(repairCost) || repairCost < 0) {
            throw new IllegalArgumentException("Invalid repair cost");
        }
        this.repairCost = repairCost;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status cannot be blank");
        }
        this.status = status;
    }
}
