package com.smartithelpdesk.smartithelpdesk;

public class Allocation {

    // Private attributes
    private String allocationId;
    private String assetId;
    private String departmentId;

    // Constructor
    public Allocation(String allocationId, String assetId,
            String departmentId) {

        setAllocationId(allocationId);
        setAssetId(assetId);
        setDepartmentId(departmentId);
    }

    // Getters
    public String getAllocationId() {
        return allocationId;
    }

    public String getAssetId() {
        return assetId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    // Setters with validation
    public void setAllocationId(String allocationId) {
        if (allocationId == null || allocationId.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation ID cannot be blank"
            );
        }
        this.allocationId = allocationId;
    }

    public void setAssetId(String assetId) {
        if (assetId == null || assetId.isBlank()) {
            throw new IllegalArgumentException(
                    "Asset ID cannot be blank"
            );
        }
        this.assetId = assetId;
    }

    public void setDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Department ID cannot be blank"
            );
        }
        this.departmentId = departmentId;
    }
}
