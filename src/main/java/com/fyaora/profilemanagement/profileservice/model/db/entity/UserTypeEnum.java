package com.fyaora.profilemanagement.profileservice.model.db.entity;

public enum UserTypeEnum {
    CUSTOMER(1),
    SERVICE_PROVIDER(2),
    INVESTOR(3);

    private final int code;

    UserTypeEnum(int val) {
        this.code = val;
    }

    public int getCode() {
        return code;
    }

    public static UserTypeEnum fromCode(int code) {
        for (UserTypeEnum type: UserTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid UserTypeEnum code: " + code);
    }
}
