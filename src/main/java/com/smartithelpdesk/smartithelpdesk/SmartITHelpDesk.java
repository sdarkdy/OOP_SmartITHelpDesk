package com.smartithelpdesk.smartithelpdesk;

public class SmartITHelpDesk {

    public static void main(String[] args) {

        // Create a laptop asset
        ComputerAsset laptop = new ComputerAsset(
                "IT001",
                "Dell Laptop",
                3500.00,
                500.00,
                1,
                "Available",
                16,
                512
        );

        // Display the asset information
        System.out.println("Asset ID: " + laptop.getAssetId());
        System.out.println("Description: " + laptop.getDescription());
        System.out.println("Purchase Value: " + laptop.getPurchaseValue());
        System.out.println("Repair Cost: " + laptop.getRepairCost());
        System.out.println("Quantity: " + laptop.getQuantity());
        System.out.println("Status: " + laptop.getStatus());

        System.out.println("-------------------");

        Department department1 = new Department(
                "D001",
                "IT Department"
        );

        System.out.println("Department ID: "
                + department1.getDepartmentId());

        System.out.println("Department Name: "
                + department1.getDepartmentName());

        System.out.println("-------------------");

        SupportTicket ticket1 = new SupportTicket(
                "T001",
                "IT001",
                "Laptop cannot connect to Wi-Fi",
                "Open"
        );

        System.out.println("Ticket ID: " + ticket1.getTicketId());
        System.out.println("Asset ID: " + ticket1.getAssetId());
        System.out.println("Issue: " + ticket1.getIssue());
        System.out.println("Status: " + ticket1.getStatus());

        System.out.println("-------------------");

        RepairRequest repair1 = new RepairRequest(
                "R001",
                "IT001",
                500.00,
                "Open"
        );

        System.out.println("Repair ID: " + repair1.getRepairId());
        System.out.println("Asset ID: " + repair1.getAssetId());
        System.out.println("Repair Cost: " + repair1.getRepairCost());
        System.out.println("Status: " + repair1.getStatus());

        System.out.println("-------------------");

        AllocationManager allocationManager = new AllocationManager();

        allocationManager.addRepairRequest(repair1);

        try {
            allocationManager.allocateAsset(
                    "A001",
                    laptop,
                    department1
            );
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");

        StaffUser staff1 = new StaffUser(
                "S001",
                "Yousef",
                "IT Technician"
        );

        System.out.println("Staff ID: " + staff1.getStaffId());
        System.out.println("Staff Name: " + staff1.getStaffName());
        System.out.println("Role: " + staff1.getRole());

        System.out.println("-------------------");

        InventoryManager inventory = new InventoryManager();

        // Register our existing laptop
        inventory.addAsset(laptop);

        // Create another asset with the same ID
        ComputerAsset duplicateLaptop = new ComputerAsset(
                "IT001",
                "HP Laptop",
                4000.00,
                300.00,
                1,
                "Available",
                16,
                512
        );

        // Attempt duplicate registration
        try {
            inventory.addAsset(duplicateLaptop);
        } catch (IllegalArgumentException e) {
            System.out.println("Registration rejected: " + e.getMessage());
        }

        System.out.println("-------------------");

        System.out.println("Total Registered Assets: "
                + InventoryManager.getAssetCounter());

        System.out.println("-------------------");

        // Create an asset with an invalid ID format
        ComputerAsset invalidAsset = new ComputerAsset(
                "ABC001",
                "HP Printer",
                1200.00,
                200.00,
                1,
                "Available",
                4,
                128
        );

        try {
            inventory.addAsset(invalidAsset);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Registration rejected: " + e.getMessage()
            );
        }

        System.out.println("Total Registered Assets: "
                + InventoryManager.getAssetCounter());

        ComputerAsset laptop2 = new ComputerAsset(
                "IT002",
                "HP Laptop",
                3000.00,
                200.00,
                1,
                "Under Repair",
                8,
                256
        );

        inventory.addAsset(laptop2);

        System.out.println("-------------------");
        System.out.println("Search by keyword:");

        // Search using the abstract Asset class
        for (Asset asset : inventory.searchAsset("Dell")) {
            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription());
        }

        System.out.println("-------------------");
        System.out.println("Search by keyword and status:");

        // Search using the overloaded method
        for (Asset asset : inventory.searchAsset(
                "Laptop", "Available")) {

            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription() + " - "
                    + asset.getStatus());
        }

        System.out.println("-------------------");
        System.out.println("All laptops:");

        for (Asset asset : inventory.searchAsset("Laptop")) {
            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription());
        }

        System.out.println("-------------------");
        System.out.println("Available laptops:");

        for (Asset asset : inventory.searchAsset(
                "Laptop", "Available")) {

            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription());
        }

        System.out.println("-------------------");
        System.out.println("Testing successful allocation:");

        // Create an available laptop
        ComputerAsset laptop3 = new ComputerAsset(
                "IT003",
                "Lenovo ThinkPad",
                4200.00,
                0.00,
                1,
                "Available",
                16,
                512
        );

        // Register the new laptop
        inventory.addAsset(laptop3);

        // Allocate the laptop to IT Department
        try {
            allocationManager.allocateAsset(
                    "A002",
                    laptop3,
                    department1
            );

            System.out.println("New Asset Status: "
                    + laptop3.getStatus());

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing duplicate allocation:");

        try {
            allocationManager.allocateAsset(
                    "A003",
                    laptop3,
                    department1
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing allocation after repair completion:");

        // Mark the existing repair as completed
        repair1.setStatus("Completed");

        // Attempt to allocate IT001 again
        try {
            allocationManager.allocateAsset(
                    "A004",
                    laptop,
                    department1
            );

            System.out.println("New Asset Status: "
                    + laptop.getStatus());

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing approved repair restriction:");

        // Create another available laptop
        ComputerAsset laptop4 = new ComputerAsset(
                "IT004",
                "Dell Latitude",
                3200.00,
                400.00,
                1,
                "Available",
                16,
                512
        );

        inventory.addAsset(laptop4);

        // Create an approved repair request
        RepairRequest repair2 = new RepairRequest(
                "R002",
                "IT004",
                400.00,
                "Approved"
        );

        // Register the repair
        allocationManager.addRepairRequest(repair2);

        // Attempt allocation
        try {
            allocationManager.allocateAsset(
                    "A005",
                    laptop4,
                    department1
            );

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing blank asset ID:");

        try {
            ComputerAsset blankAsset = new ComputerAsset(
                    "",
                    "Test Laptop",
                    2500.00,
                    100.00,
                    1,
                    "Available",
                    8,
                    256
            );

            inventory.addAsset(blankAsset);

        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Registration rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing duplicate allocation ID:");

        // Create a fifth laptop
        ComputerAsset laptop5 = new ComputerAsset(
                "IT005",
                "HP EliteBook",
                3800.00,
                0.00,
                1,
                "Available",
                16,
                512
        );

        // Register the fifth laptop
        inventory.addAsset(laptop5);

        // Attempt to reuse allocation ID A002
        try {
            allocationManager.allocateAsset(
                    "A002",
                    laptop5,
                    department1
            );
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Allocation rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing duplicate repair ID:");

        RepairRequest duplicateRepair = new RepairRequest(
                "R001",
                "IT002",
                250.00,
                "Open"
        );

        try {
            allocationManager.addRepairRequest(duplicateRepair);
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "Repair rejected: " + e.getMessage()
            );
        }

        System.out.println("-------------------");
        System.out.println("Testing ComputerAsset inheritance:");

        // Create a computer asset
        ComputerAsset computer1 = new ComputerAsset(
                "IT006",
                "Dell Workstation",
                5000.00,
                800.00,
                1,
                "Available",
                32,
                1000
        );

        // Display inherited computer attributes
        System.out.println("Asset ID: " + computer1.getAssetId());
        System.out.println("Description: " + computer1.getDescription());
        System.out.println("Purchase Value: " + computer1.getPurchaseValue());
        System.out.println("Repair Cost: " + computer1.getRepairCost());
        System.out.println("Quantity: " + computer1.getQuantity());
        System.out.println("Status: " + computer1.getStatus());

        // Display computer-specific attributes
        System.out.println("RAM: " + computer1.getRamGB() + " GB");
        System.out.println("Storage: " + computer1.getStorageGB() + " GB");

        System.out.println("-------------------");
        System.out.println("Testing FurnitureAsset inheritance:");

        // Create a furniture asset
        FurnitureAsset furniture1 = new FurnitureAsset(
                "IT007",
                "Office Desk",
                1500.00,
                200.00,
                1,
                "Available",
                "Wood",
                true
        );

        // Display inherited furniture attributes
        System.out.println("Asset ID: " + furniture1.getAssetId());
        System.out.println("Description: " + furniture1.getDescription());
        System.out.println("Purchase Value: " + furniture1.getPurchaseValue());
        System.out.println("Repair Cost: " + furniture1.getRepairCost());
        System.out.println("Quantity: " + furniture1.getQuantity());
        System.out.println("Status: " + furniture1.getStatus());

        // Display furniture-specific attributes
        System.out.println("Material: " + furniture1.getMaterial());
        System.out.println("Safety Passed: " + furniture1.isSafetyPassed());

        System.out.println("-------------------");
        System.out.println("Testing refurbishment eligibility:");

        // Check computer refurbishment eligibility
        System.out.println("Computer Eligible: "
                + computer1.isRefurbishmentEligible());

        // Check furniture refurbishment eligibility
        System.out.println("Furniture Eligible: "
                + furniture1.isRefurbishmentEligible());

        System.out.println("-------------------");
        System.out.println("Testing polymorphism:");

        // Create a mixed collection of assets
        java.util.ArrayList<Asset> assets = new java.util.ArrayList<>();

        // Add computer and furniture objects
        assets.add(computer1);
        assets.add(furniture1);

        // Process different assets using the same method
        for (Asset asset : assets) {

            System.out.println("Asset: " + asset.getDescription());

            System.out.println("Refurbishment Eligible: "
                    + asset.isRefurbishmentEligible());
        }

        System.out.println("-------------------");
        System.out.println("Testing refurbishment thresholds:");

        // Test computer exactly at the 40% limit
        computer1.setRepairCost(2000.00);
        System.out.println("Computer at 40%: "
                + computer1.isRefurbishmentEligible());

        // Test computer above the 40% limit
        computer1.setRepairCost(2001.00);
        System.out.println("Computer above 40%: "
                + computer1.isRefurbishmentEligible());

        // Test furniture exactly at the 25% limit
        furniture1.setRepairCost(375.00);
        System.out.println("Furniture at 25%: "
                + furniture1.isRefurbishmentEligible());

        // Test furniture above the 25% limit
        furniture1.setRepairCost(376.00);
        System.out.println("Furniture above 25%: "
                + furniture1.isRefurbishmentEligible());

        // Test furniture with failed safety inspection
        furniture1.setRepairCost(200.00);
        furniture1.setSafetyPassed(false);
        System.out.println("Furniture safety failed: "
                + furniture1.isRefurbishmentEligible());

        System.out.println("-------------------");
        System.out.println("Testing ReuseReportable interface:");

        // Create interface references
        ReuseReportable computerReport = computer1;
        ReuseReportable furnitureReport = furniture1;

        // Display computer reuse summary
        System.out.println(computerReport.getReuseSummary());

        // Display furniture reuse summary
        System.out.println(furnitureReport.getReuseSummary());

        System.out.println("-------------------");
        System.out.println("Testing mixed asset inventory:");

        // Register computer and furniture assets
        inventory.addAsset(computer1);
        inventory.addAsset(furniture1);

        // Search for computer assets
        System.out.println("Search for Dell Workstation:");
        for (Asset asset : inventory.searchAsset("Workstation")) {
            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription());
        }

        // Search for furniture assets
        System.out.println("Search for Office Desk:");
        for (Asset asset : inventory.searchAsset("Desk")) {
            System.out.println(asset.getAssetId() + " - "
                    + asset.getDescription());
        }

        // Display the total registered assets
        System.out.println("Total Registered Assets: "
                + InventoryManager.getAssetCounter());

    }
}
