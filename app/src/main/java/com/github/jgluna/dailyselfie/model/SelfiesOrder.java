package com.github.jgluna.dailyselfie.model;

public enum SelfiesOrder {

    DATE_DESC("creation_date desc"),
    DATE_ASC("creation_date asc"),
    MODIFIED_ASC("modification_date asc"),
    MODIFIED_DESC("modification_date desc");

    private final String description;

    SelfiesOrder(String description) {
        this.description = description;
    }

    public static SelfiesOrder getByString(String value) {
        for (SelfiesOrder order : SelfiesOrder.values()) {
            if (order.getDescription().equals(value)) {
                return order;
            }
        }
        return null;
    }

    public String getDescription() {
        return this.description;
    }

}
