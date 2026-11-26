package com.smartithelpdesk.smartithelpdesk;

public class StaffUser {

    // Private attributes
    private String staffId;
    private String staffName;
    private String role;

    // Constructor
    public StaffUser(String staffId, String staffName, String role) {
        setStaffId(staffId);
        setStaffName(staffName);
        setRole(role);
    }

    // Getters
    public String getStaffId() {
        return staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public String getRole() {
        return role;
    }

    // Setters with validation
    public void setStaffId(String staffId) {
        if (staffId == null || staffId.isBlank()) {
            throw new IllegalArgumentException(
                    "Staff ID cannot be blank"
            );
        }
        this.staffId = staffId;
    }

    public void setStaffName(String staffName) {
        if (staffName == null || staffName.isBlank()) {
            throw new IllegalArgumentException(
                    "Staff name cannot be blank"
            );
        }
        this.staffName = staffName;
    }

    public void setRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Role cannot be blank"
            );
        }
        this.role = role;
    }
}
