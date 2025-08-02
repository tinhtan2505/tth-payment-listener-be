package tth_group.payment_listener.enums;

public enum CategoryType {
    PROJECT_PRIORITY(1, "Project Priority"),
    PROJECT_TYPE(2, "Project Type"),
    FILE_TYPE(3, "File Type"),
    PROJECT_STATUS(4, "Project Status"),
    EDITOR_STATUS(5, "Editor Status"),
    CUSTOMER_STATUS(6, "Customer Status"),
    CUSTOMER_SERVICE(7, "Customer Service"),
    CUSTOMER_SOURCE(8, "Customer Source"),
    PAYMENT_METHOD(9, "Payment Method");

    private final int value;
    private final String displayName;

    CategoryType(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }
}

