package tth_group.payment_listener.enums;

public enum ProjectCardType {
    PROJECT_REQUIREMENT("Project Requirement"),
    EDIT_INFORMATION("Edit Information"),
    PROJECT_EDIT_REQUIREMENT("Project Edit Requirement"),
    UPDATE_STATUS("Update Status"),
    CUSTOMER_REQUIREMENT("Customer Requirement"),
    SALER_REQUIREMENT("Saler Requirement");

    private final String displayName;

    ProjectCardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

