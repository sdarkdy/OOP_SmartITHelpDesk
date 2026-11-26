package com.smartithelpdesk.smartithelpdesk;

import java.util.ArrayList;

public class AllocationManager {

    private ArrayList<Allocation> allocations = new ArrayList<>();

    // Store repair requests
    private ArrayList<RepairRequest> repairs = new ArrayList<>();

    // Register a repair request
    public void addRepairRequest(RepairRequest repair) {

        if (repair == null) {
            throw new IllegalArgumentException(
                    "Repair request cannot be null"
            );
        }

        // Prevent duplicate repair request IDs
        for (RepairRequest existingRepair : repairs) {

            if (existingRepair.getRepairId().equalsIgnoreCase(
                    repair.getRepairId())) {

                throw new IllegalArgumentException(
                        "Duplicate Repair ID: " + repair.getRepairId()
                );
            }
        }

        repairs.add(repair);
    }

    public void allocateAsset(
            String allocationId,
            Asset asset,
            Department department) {

        if (asset == null || department == null) {
            throw new IllegalArgumentException(
                    "Asset and department are required"
            );
        }

        // Check all registered repair requests
        for (RepairRequest currentRepair : repairs) {

            if (currentRepair.getAssetId().equalsIgnoreCase(
                    asset.getAssetId())
                    && (currentRepair.getStatus().equalsIgnoreCase("Open")
                    || currentRepair.getStatus().equalsIgnoreCase("Approved"))) {

                throw new IllegalArgumentException(
                        "Asset cannot be allocated during an active repair"
                );
            }
        }

        // Only available equipment can be allocated
        if (!asset.getStatus().equalsIgnoreCase("Available")) {
            throw new IllegalArgumentException(
                    "Asset is not available"
            );
        }

        // Prevent duplicate allocation IDs and repeated allocations
        for (Allocation allocation : allocations) {

            if (allocation.getAllocationId().equalsIgnoreCase(allocationId)) {
                throw new IllegalArgumentException(
                        "Duplicate Allocation ID: " + allocationId
                );
            }

            if (allocation.getAssetId().equalsIgnoreCase(asset.getAssetId())) {
                throw new IllegalArgumentException(
                        "Asset is already allocated"
                );
            }
        }

        // Create a new allocation record
        Allocation allocation = new Allocation(
                allocationId,
                asset.getAssetId(),
                department.getDepartmentId()
        );

        // Save allocation and update asset status
        allocations.add(allocation);
        asset.setStatus("Allocated");

        System.out.println(
                "Allocation successful: " + allocationId
        );
    }

    // Remove an allocation from memory
    public void removeAllocation(String allocationId) {

        if (allocationId == null || allocationId.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation ID is required");
        }

        allocations.removeIf(
                allocation -> allocation.getAllocationId()
                        .equalsIgnoreCase(allocationId)
        );
    }

    // Update the status of an existing repair request
    public void updateRepairStatus(String repairId, String newStatus) {

        // Validate repair ID
        if (repairId == null || repairId.isBlank()) {
            throw new IllegalArgumentException(
                    "Repair ID cannot be blank"
            );
        }

        // Validate repair status
        if (newStatus == null
                || (!newStatus.equals("Open")
                && !newStatus.equals("Approved")
                && !newStatus.equals("Completed")
                && !newStatus.equals("Rejected"))) {

            throw new IllegalArgumentException(
                    "Invalid repair status"
            );
        }

        // Search for the registered repair
        for (RepairRequest repair : repairs) {

            if (repair.getRepairId().equalsIgnoreCase(repairId)) {

                // Update the existing repair
                repair.setStatus(newStatus);
                return;
            }
        }

        // Reject unknown repair IDs
        throw new IllegalArgumentException(
                "Repair not found: " + repairId
        );
    }

}
