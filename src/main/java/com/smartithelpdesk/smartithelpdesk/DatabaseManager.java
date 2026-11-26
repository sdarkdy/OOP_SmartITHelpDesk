package com.smartithelpdesk.smartithelpdesk;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

public class DatabaseManager {

    // SQLite database file
    private static final String DB_URL
            = "jdbc:sqlite:smartithelpdesk.db";

    // Connect to SQLite
    public static Connection getConnection() throws SQLException {

        Connection connection = DriverManager.getConnection(DB_URL);

        // Enable foreign key validation
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            connection.close();
            throw e;
        }

        return connection;
    }

    // Create database tables
    public static void initializeDatabase() {

        String assetsSQL = """
                CREATE TABLE IF NOT EXISTS Assets (
                    assetId TEXT PRIMARY KEY,
                    description TEXT NOT NULL,
                    assetType TEXT NOT NULL,
                    purchaseValue REAL NOT NULL,
                    repairCost REAL NOT NULL,
                    quantity INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    massKg REAL,
                    ramGB INTEGER,
                    storageGB INTEGER,
                    material TEXT,
                    safetyPassed INTEGER
                )
                """;

        String departmentsSQL = """
                CREATE TABLE IF NOT EXISTS Departments (
                    departmentId TEXT PRIMARY KEY,
                    departmentName TEXT NOT NULL
                )
                """;

        String repairsSQL = """
                CREATE TABLE IF NOT EXISTS Repairs (
                    repairId TEXT PRIMARY KEY,
                    assetId TEXT NOT NULL,
                    repairCost REAL NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY (assetId)
                        REFERENCES Assets(assetId)
                )
                """;

        String allocationsSQL = """
                CREATE TABLE IF NOT EXISTS Allocations (
                    allocationId TEXT PRIMARY KEY,
                    assetId TEXT NOT NULL,
                    departmentId TEXT NOT NULL,
                    FOREIGN KEY (assetId)
                        REFERENCES Assets(assetId),
                    FOREIGN KEY (departmentId)
                        REFERENCES Departments(departmentId)
                )
                """;

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {

            statement.execute(assetsSQL);
            statement.execute(departmentsSQL);
            statement.execute(repairsSQL);
            statement.execute(allocationsSQL);

            // Register the default IT department
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT OR IGNORE INTO Departments "
                    + "(departmentId, departmentName) VALUES (?, ?)")) {

                ps.setString(1, "D001");
                ps.setString(2, "IT Department");
                ps.executeUpdate();
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // =====================================================
    // ASSETS - SAVE
    // =====================================================
    public static void saveAsset(Asset asset) throws SQLException {

        if (asset == null
                || asset.getAssetId() == null
                || !InventoryManager.isValidAssetId(asset.getAssetId())) {

            throw new IllegalArgumentException("Invalid Asset ID");
        }

        String sql = """
                INSERT INTO Assets (
                    assetId, description, assetType,
                    purchaseValue, repairCost, quantity, status,
                    massKg, ramGB, storageGB, material, safetyPassed
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            // Common asset fields
            ps.setString(1, asset.getAssetId());
            ps.setString(2, asset.getDescription());
            ps.setDouble(4, asset.getPurchaseValue());
            ps.setDouble(5, asset.getRepairCost());
            ps.setInt(6, asset.getQuantity());
            ps.setString(7, asset.getStatus());

            // Optional mass
            if (asset.getMassKg() == null) {
                ps.setNull(8, Types.REAL);
            } else {
                ps.setDouble(8, asset.getMassKg());
            }

            if (asset instanceof ComputerAsset computer) {

                // Computer information
                ps.setString(3, "Computer");
                ps.setInt(9, computer.getRamGB());
                ps.setInt(10, computer.getStorageGB());
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.INTEGER);

            } else if (asset instanceof FurnitureAsset furniture) {

                // Furniture information
                ps.setString(3, "Furniture");
                ps.setNull(9, Types.INTEGER);
                ps.setNull(10, Types.INTEGER);
                ps.setString(11, furniture.getMaterial());
                ps.setBoolean(12, furniture.isSafetyPassed());

            } else {
                throw new IllegalArgumentException(
                        "Unsupported asset type"
                );
            }

            ps.executeUpdate();

        }
    }

    // =====================================================
    // ASSETS - UPDATE
    // =====================================================
    public static void updateAsset(Asset asset) throws SQLException {

        if (asset == null
                || !InventoryManager.isValidAssetId(asset.getAssetId())) {
            throw new IllegalArgumentException("Invalid Asset ID");
        }

        String sql = """
                UPDATE Assets
                SET description = ?,
                    assetType = ?,
                    purchaseValue = ?,
                    repairCost = ?,
                    quantity = ?,
                    status = ?,
                    massKg = ?,
                    ramGB = ?,
                    storageGB = ?,
                    material = ?,
                    safetyPassed = ?
                WHERE assetId = ?
                """;

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, asset.getDescription());

            if (asset instanceof ComputerAsset computer) {
                ps.setString(2, "Computer");
                ps.setInt(8, computer.getRamGB());
                ps.setInt(9, computer.getStorageGB());
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.INTEGER);
            } else if (asset instanceof FurnitureAsset furniture) {
                ps.setString(2, "Furniture");
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.INTEGER);
                ps.setString(10, furniture.getMaterial());
                ps.setBoolean(11, furniture.isSafetyPassed());
            } else {
                throw new IllegalArgumentException("Unsupported asset type");
            }

            ps.setDouble(3, asset.getPurchaseValue());
            ps.setDouble(4, asset.getRepairCost());
            ps.setInt(5, asset.getQuantity());
            ps.setString(6, asset.getStatus());

            if (asset.getMassKg() == null) {
                ps.setNull(7, Types.REAL);
            } else {
                ps.setDouble(7, asset.getMassKg());
            }

            ps.setString(12, asset.getAssetId());

            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Asset not found: " + asset.getAssetId());
            }
        }
    }

    // =====================================================
    // ASSETS - DELETE
    // =====================================================
    public static void deleteAsset(String assetId)
            throws SQLException {

        if (!InventoryManager.isValidAssetId(assetId)) {
            throw new IllegalArgumentException(
                    "Invalid Asset ID: " + assetId);
        }

        try (Connection connection = getConnection()) {

            connection.setAutoCommit(false);

            try {
                // Check whether the asset exists
                String checkAsset
                        = "SELECT status FROM Assets WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(checkAsset)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Asset not found: " + assetId);
                        }

                        if ("Allocated".equalsIgnoreCase(
                                rs.getString("status"))) {
                            throw new SQLException(
                                    "Cannot delete an allocated asset.");
                        }
                    }
                }

                // Prevent deleting assets linked to repairs
                String checkRepairs
                        = "SELECT COUNT(*) FROM Repairs WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(checkRepairs)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next() && rs.getInt(1) > 0) {
                            throw new SQLException(
                                    "Cannot delete asset with repair records.");
                        }
                    }
                }

                // Prevent deleting assets linked to allocations
                String checkAllocations
                        = "SELECT COUNT(*) FROM Allocations WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(checkAllocations)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next() && rs.getInt(1) > 0) {
                            throw new SQLException(
                                    "Cannot delete asset with allocation records.");
                        }
                    }
                }

                // Delete only an unreferenced asset
                String deleteSQL
                        = "DELETE FROM Assets WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(deleteSQL)) {

                    ps.setString(1, assetId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Asset deletion failed: " + assetId);
                    }
                }

                connection.commit();

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    // =====================================================
    // ASSETS - LOAD
    // =====================================================
    public static ArrayList<Asset> loadAssets() throws SQLException {

        ArrayList<Asset> assets = new ArrayList<>();

        String sql = "SELECT * FROM Assets ORDER BY assetId";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String assetId = rs.getString("assetId");

                // Skip invalid database records
                if (assetId == null
                        || !InventoryManager.isValidAssetId(assetId)) {

                    System.out.println(
                            "Skipping invalid Asset ID: " + assetId
                    );
                    continue;
                }

                try {

                    String description = rs.getString("description");
                    String type = rs.getString("assetType");

                    double purchaseValue
                            = rs.getDouble("purchaseValue");

                    double repairCost
                            = rs.getDouble("repairCost");

                    int quantity = rs.getInt("quantity");
                    String status = rs.getString("status");

                    Asset asset;

                    if ("Computer".equals(type)) {

                        asset = new ComputerAsset(
                                assetId,
                                description,
                                purchaseValue,
                                repairCost,
                                quantity,
                                status,
                                rs.getInt("ramGB"),
                                rs.getInt("storageGB")
                        );

                    } else if ("Furniture".equals(type)) {

                        asset = new FurnitureAsset(
                                assetId,
                                description,
                                purchaseValue,
                                repairCost,
                                quantity,
                                status,
                                rs.getString("material"),
                                rs.getBoolean("safetyPassed")
                        );

                    } else {

                        System.out.println(
                                "Skipping unsupported asset: " + assetId
                        );
                        continue;
                    }

                    // Restore optional mass
                    double mass = rs.getDouble("massKg");

                    if (!rs.wasNull()) {
                        asset.setMassKg(mass);
                    }

                    assets.add(asset);

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Skipping invalid asset " + assetId
                            + ": " + e.getMessage()
                    );
                }
            }
        }

        return assets;
    }

    // =====================================================
    // REPAIRS - SAVE
    // =====================================================
    public static void saveRepair(RepairRequest repair)
            throws SQLException {

        if (repair == null) {
            throw new IllegalArgumentException(
                    "Repair request cannot be null"
            );
        }

        String sql = """
                INSERT INTO Repairs (
                    repairId, assetId, repairCost, status
                )
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, repair.getRepairId());
            ps.setString(2, repair.getAssetId());
            ps.setDouble(3, repair.getRepairCost());
            ps.setString(4, repair.getStatus());

            ps.executeUpdate();
        }
    }

    // =====================================================
    // REPAIRS - LOAD
    // =====================================================
    public static ArrayList<RepairRequest> loadRepairs()
            throws SQLException {

        ArrayList<RepairRequest> repairs = new ArrayList<>();

        String sql = "SELECT * FROM Repairs ORDER BY repairId";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                RepairRequest repair = new RepairRequest(
                        rs.getString("repairId"),
                        rs.getString("assetId"),
                        rs.getDouble("repairCost"),
                        rs.getString("status")
                );

                repairs.add(repair);
            }
        }

        return repairs;
    }

    // =====================================================
    // REPAIRS - APPROVE / REJECT / COMPLETE
    // =====================================================
    public static void updateRepairStatus(
            String repairId,
            String newStatus) throws SQLException {

        // Validate new repair status
        if (!"Open".equalsIgnoreCase(newStatus)
                && !"Approved".equalsIgnoreCase(newStatus)
                && !"Completed".equalsIgnoreCase(newStatus)
                && !"Rejected".equalsIgnoreCase(newStatus)) {

            throw new IllegalArgumentException(
                    "Invalid repair status: " + newStatus
            );
        }

        if (repairId == null || repairId.isBlank()) {
            throw new IllegalArgumentException(
                    "Repair ID is required"
            );
        }

        String sql = """
                UPDATE Repairs
                SET status = ?
                WHERE repairId = ?
                """;

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setString(2, repairId);

            int updated = ps.executeUpdate();

            if (updated == 0) {
                throw new SQLException(
                        "Repair not found: " + repairId
                );
            }
        }
    }

    // =====================================================
    // ALLOCATIONS - SAVE
    // =====================================================
    public static void saveAllocation(
            String allocationId,
            String assetId,
            String departmentId,
            String departmentName) throws SQLException {

        if (allocationId == null || allocationId.isBlank()
                || assetId == null || assetId.isBlank()
                || departmentId == null || departmentId.isBlank()
                || departmentName == null || departmentName.isBlank()) {

            throw new IllegalArgumentException(
                    "All allocation fields are required"
            );
        }

        try (Connection connection = getConnection()) {

            // Use transaction to save allocation and asset status together
            connection.setAutoCommit(false);

            try {

                // Check asset availability
                String assetSQL
                        = "SELECT status FROM Assets WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(assetSQL)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "Asset not found: " + assetId
                            );
                        }

                        if (!"Available".equalsIgnoreCase(
                                rs.getString("status"))) {

                            throw new SQLException(
                                    "Asset is not available: " + assetId
                            );
                        }
                    }
                }

                // Check active repair requests
                String repairSQL = """
                        SELECT COUNT(*) AS total
                        FROM Repairs
                        WHERE assetId = ?
                        AND UPPER(status) IN ('OPEN', 'APPROVED')
                        """;

                try (PreparedStatement ps
                        = connection.prepareStatement(repairSQL)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {

                        if (rs.next() && rs.getInt("total") > 0) {
                            throw new SQLException(
                                    "Cannot allocate asset with active repair"
                            );
                        }
                    }
                }

                // Register department if missing
                String departmentSQL = """
                        INSERT OR IGNORE INTO Departments (
                            departmentId, departmentName
                        )
                        VALUES (?, ?)
                        """;

                try (PreparedStatement ps
                        = connection.prepareStatement(departmentSQL)) {

                    ps.setString(1, departmentId);
                    ps.setString(2, departmentName);
                    ps.executeUpdate();
                }

                // Save allocation
                String allocationSQL = """
                        INSERT INTO Allocations (
                            allocationId, assetId, departmentId
                        )
                        VALUES (?, ?, ?)
                        """;

                try (PreparedStatement ps
                        = connection.prepareStatement(allocationSQL)) {

                    ps.setString(1, allocationId);
                    ps.setString(2, assetId);
                    ps.setString(3, departmentId);
                    ps.executeUpdate();
                }

                // Update asset status in the database
                String updateSQL = """
                        UPDATE Assets
                        SET status = 'Allocated'
                        WHERE assetId = ?
                        AND status = 'Available'
                        """;

                try (PreparedStatement ps
                        = connection.prepareStatement(updateSQL)) {

                    ps.setString(1, assetId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Failed to update asset allocation status"
                        );
                    }
                }

                // Confirm all database changes
                connection.commit();

            } catch (SQLException | RuntimeException e) {

                // Cancel incomplete allocation
                connection.rollback();
                throw e;

            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // =====================================================
    // ALLOCATIONS - UNALLOCATE
    // =====================================================
    public static String unallocateAsset(String allocationId)
            throws SQLException {

        if (allocationId == null || allocationId.isBlank()) {
            throw new IllegalArgumentException(
                    "Allocation ID is required");
        }

        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);

            try {
                String assetId;

                // Find the asset linked to this allocation
                String findSQL
                        = "SELECT assetId FROM Allocations "
                        + "WHERE allocationId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(findSQL)) {

                    ps.setString(1, allocationId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                    "Allocation not found: " + allocationId);
                        }

                        assetId = rs.getString("assetId");
                    }
                }

                // Remove the allocation record
                String deleteSQL
                        = "DELETE FROM Allocations WHERE allocationId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(deleteSQL)) {

                    ps.setString(1, allocationId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Could not remove allocation");
                    }
                }

                // Ensure no other allocations exist for this asset
                String countSQL
                        = "SELECT COUNT(*) FROM Allocations WHERE assetId = ?";

                try (PreparedStatement ps
                        = connection.prepareStatement(countSQL)) {

                    ps.setString(1, assetId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            throw new SQLException(
                                    "Asset has another active allocation");
                        }
                    }
                }

                // Return the asset to Available
                String updateSQL
                        = "UPDATE Assets SET status = 'Available' "
                        + "WHERE assetId = ? AND status = 'Allocated'";

                try (PreparedStatement ps
                        = connection.prepareStatement(updateSQL)) {

                    ps.setString(1, assetId);

                    if (ps.executeUpdate() != 1) {
                        throw new SQLException(
                                "Asset is not currently allocated");
                    }
                }

                connection.commit();
                return assetId;

            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    // =====================================================
    // ALLOCATIONS - LOAD
    // =====================================================
    public static ArrayList<String[]> loadAllocations()
            throws SQLException {

        ArrayList<String[]> allocations = new ArrayList<>();

        String sql = """
                SELECT allocationId, assetId, departmentId
                FROM Allocations
                ORDER BY allocationId
                """;

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                // Restore allocation data
                allocations.add(new String[]{
                    rs.getString("allocationId"),
                    rs.getString("assetId"),
                    rs.getString("departmentId")
                });
            }
        }

        return allocations;
    }

    // =====================================================
    // DATABASE INVENTORY REPORT
    // =====================================================
    public static String generateDatabaseReport()
            throws SQLException {

        ArrayList<Asset> assets = loadAssets();

        int totalAssets = assets.size();
        int allocatedAssets = 0;
        int eligibleAssets = 0;

        StringBuilder report = new StringBuilder();

        report.append("SMART IT HELP DESK - DATABASE REPORT\n");
        report.append("====================================\n\n");

        for (Asset asset : assets) {

            // Count allocated assets
            if ("Allocated".equalsIgnoreCase(asset.getStatus())) {
                allocatedAssets++;
            }

            // Count refurbishment eligible assets
            if (asset.isRefurbishmentEligible()) {
                eligibleAssets++;
            }

            report.append("Asset ID: ")
                    .append(asset.getAssetId()).append("\n");

            report.append("Description: ")
                    .append(asset.getDescription()).append("\n");

            report.append("Status: ")
                    .append(asset.getStatus()).append("\n");

            report.append("Refurbishment Eligible: ")
                    .append(asset.isRefurbishmentEligible())
                    .append("\n");

            if (asset instanceof ReuseReportable reuse) {

                report.append("Reuse Summary: ")
                        .append(reuse.getReuseSummary())
                        .append("\n");
            }

            report.append("------------------------------------\n");
        }

        report.append("\nTOTAL REGISTERED ASSETS: ")
                .append(totalAssets);

        report.append("\nALLOCATED ASSETS: ")
                .append(allocatedAssets);

        report.append("\nREFURBISHMENT ELIGIBLE: ")
                .append(eligibleAssets);

        return report.toString();
    }

    // =====================================================
    // DATABASE STATISTICS
    // =====================================================
    public static int countRepairs() throws SQLException {

        String sql = "SELECT COUNT(*) FROM Repairs";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public static int countAllocations() throws SQLException {

        String sql = "SELECT COUNT(*) FROM Allocations";

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // Test database initialization
    public static void main(String[] args) {
        initializeDatabase();
    }
}
