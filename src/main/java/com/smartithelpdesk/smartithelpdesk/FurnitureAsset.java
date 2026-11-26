package com.smartithelpdesk.smartithelpdesk;

public class FurnitureAsset extends Asset implements ReuseReportable {

    private String material;
    private boolean safetyPassed;

    // Constructor
    public FurnitureAsset(String assetId, String description,
            double purchaseValue, double repairCost,
            int quantity, String status,
            String material, boolean safetyPassed) {

        super(assetId, description, purchaseValue,
                repairCost, quantity, status);

        setMaterial(material);
        this.safetyPassed = safetyPassed;
    }

    // Getters
    public String getMaterial() {
        return material;
    }

    public boolean isSafetyPassed() {
        return safetyPassed;
    }

    // Setters
    public void setMaterial(String material) {
        if (material == null || material.isBlank()) {
            throw new IllegalArgumentException(
                    "Material cannot be blank"
            );
        }
        this.material = material;
    }

    public void setSafetyPassed(boolean safetyPassed) {
        this.safetyPassed = safetyPassed;
    }

    // Override refurbishment eligibility for furniture
    @Override
    public boolean isRefurbishmentEligible() {

        return safetyPassed
                && getRepairCost() <= getPurchaseValue() * 0.25;
    }

    // Override reuse reporting method
    @Override
    public String getReuseSummary() {

        return "Asset ID: " + getAssetId()
                + " | Description: " + getDescription()
                + " | Material: " + getMaterial()
                + " | Safety Passed: " + isSafetyPassed()
                + " | Refurbishment Eligible: "
                + isRefurbishmentEligible();
    }

}
