package com.smartithelpdesk.smartithelpdesk;

public class ComputerAsset extends Asset implements ReuseReportable {

    private int ramGB;
    private int storageGB;

    public ComputerAsset(String assetId, String description,
            double purchaseValue, double repairCost,
            int quantity, String status,
            int ramGB, int storageGB) {

        super(assetId, description, purchaseValue,
                repairCost, quantity, status);

        setRamGB(ramGB);
        setStorageGB(storageGB);
    }

    public int getRamGB() {
        return ramGB;
    }

    public int getStorageGB() {
        return storageGB;
    }

    public void setRamGB(int ramGB) {
        if (ramGB <= 0) {
            throw new IllegalArgumentException(
                    "RAM must be greater than zero"
            );
        }
        this.ramGB = ramGB;
    }

    public void setStorageGB(int storageGB) {
        if (storageGB <= 0) {
            throw new IllegalArgumentException(
                    "Storage must be greater than zero"
            );
        }
        this.storageGB = storageGB;
    }

    // Override refurbishment eligibility for computers
    @Override
    public boolean isRefurbishmentEligible() {

        return getRepairCost() <= getPurchaseValue() * 0.40;
    }

    // Override reuse reporting method
    @Override
    public String getReuseSummary() {

        return "Asset ID: " + getAssetId()
                + " | Description: " + getDescription()
                + " | RAM: " + getRamGB() + " GB"
                + " | Storage: " + getStorageGB() + " GB"
                + " | Refurbishment Eligible: "
                + isRefurbishmentEligible();
    }

}
