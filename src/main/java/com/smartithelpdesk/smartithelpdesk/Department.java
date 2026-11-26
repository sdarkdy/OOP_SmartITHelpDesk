package com.smartithelpdesk.smartithelpdesk;

public class Department {

    // Private attributes
    private String departmentId;
    private String departmentName;

    // Constructor
    public Department(String departmentId, String departmentName) {
        setDepartmentId(departmentId);
        setDepartmentName(departmentName);
    }

    // Getters
    public String getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    // Setters with validation
    public void setDepartmentId(String departmentId) {
        if (departmentId == null || departmentId.isBlank()) {
            throw new IllegalArgumentException(
                    "Department ID cannot be blank"
            );
        }
        this.departmentId = departmentId;
    }

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name cannot be blank"
            );
        }
        this.departmentName = departmentName;
    }
}
