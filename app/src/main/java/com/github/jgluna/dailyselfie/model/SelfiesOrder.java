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

    public String getDescription() {
        return this.description;
    }

}
