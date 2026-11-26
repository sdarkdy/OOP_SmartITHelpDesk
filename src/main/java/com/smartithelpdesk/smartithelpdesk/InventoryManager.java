package com.smartithelpdesk.smartithelpdesk;

import java.util.ArrayList;

public class InventoryManager {

    // Store all registered assets using the abstract Asset class
    private ArrayList<Asset> assets = new ArrayList<>();

    // Static variable shared by all InventoryManager objects
    private static int assetCounter = 0;

    // Static method to return the total registered assets
    public static int getAssetCounter() {
        return assetCounter;
    }

    // Static method to validate asset ID
    public static boolean isValidAssetId(String assetId) {

        if (assetId == null) {
            return false;
        }

        return assetId.matches("IT[0-9]{3}");
    }

    // Register a new asset
    public void addAsset(Asset newAsset) {

        if (newAsset == null) {
            throw new IllegalArgumentException(
                    "Asset cannot be null"
            );
        }

        // Validate the asset ID format
        if (!isValidAssetId(newAsset.getAssetId())) {
            throw new IllegalArgumentException(
                    "Invalid Asset ID: " + newAsset.getAssetId()
            );
        }

        // Check for duplicate asset IDs
        for (Asset asset : assets) {

            if (asset.getAssetId().equalsIgnoreCase(
                    newAsset.getAssetId())) {

                throw new IllegalArgumentException(
                        "Duplicate Asset ID: " + newAsset.getAssetId()
                );
            }
        }

        // Add asset when ID is unique
        assets.add(newAsset);

        // Increase the counter after successful registration
        assetCounter++;

        System.out.println(
                "Asset registered successfully: " + newAsset.getAssetId()
        );
    }

    // Search assets by keyword
    public ArrayList<Asset> searchAsset(String keyword) {

        // Validate keyword FIRST
        if (keyword == null) {
            throw new IllegalArgumentException(
                    "Search keyword cannot be null"
            );
        }

        ArrayList<Asset> results = new ArrayList<>();

        for (Asset asset : assets) {
            if (asset.getAssetId().toLowerCase().contains(keyword.toLowerCase())
                    || asset.getDescription().toLowerCase().contains(keyword.toLowerCase())) {

                results.add(asset);
            }
        }

        return results;
    }

    // Overloaded method: search by keyword and status
    public ArrayList<Asset> searchAsset(String keyword, String status) {

        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException(
                    "Search status cannot be blank"
            );
        }

        ArrayList<Asset> results = new ArrayList<>();

        for (Asset asset : searchAsset(keyword)) {
            if (asset.getStatus().equalsIgnoreCase(status)) {
                results.add(asset);
            }
        }

        return results;
    }

    // Remove an asset from the in-memory inventory
    public boolean removeAsset(String assetId) {

        if (!isValidAssetId(assetId)) {
            throw new IllegalArgumentException(
                    "Invalid Asset ID: " + assetId);
        }

        for (int i = 0; i < assets.size(); i++) {

            if (assets.get(i).getAssetId().equalsIgnoreCase(assetId)) {

                assets.remove(i);
                assetCounter--;

                return true;
            }
        }

        return false;
    }

    // Return all registered assets
    public ArrayList<Asset> getAllAssets() {

        return new ArrayList<>(assets);
    }

}
