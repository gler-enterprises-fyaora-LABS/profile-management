package com.fyaora.profilemanagement.profileservice.model.db.entity;

public enum VendorTypeEnum {
    INDEPENDENT(1),
    COMPANY(2);

    private final int code;

    VendorTypeEnum(int val) {
        this.code = val;
    }

    public int getCode() {
        return code;
    }

    public static VendorTypeEnum fromCode(int code) {
        for (VendorTypeEnum type: VendorTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid VendorTypeEnums code: " + code);
    }
}
