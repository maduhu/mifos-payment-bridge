package com.omexit.util;

/**
 * Created by aomeri on 11/21/2015.
 */
public enum IdType {

    INVALID_ID(0, "idType.invalid"),
    NATIONAL_ID(1, "idType.nationalId"),
    PASSPORT_NUMBER(2, "idType.passportNumber"),
    ALIEN_ID(3, "idType.alienId"),
    MILITARY_ID(4, "idType.militaryId"),
    BIRTH_CERTIFICATE(5, "idType.birthCertificate"),
    BUSINESS_REGISTRATION_NUMBER(6, "idType.businessRegistrationNumber");

    private final Integer value;
    private final String code;

    private IdType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public static IdType fromInt(final Integer typeValue) {
        IdType enumeration = IdType.INVALID_ID;
        switch (typeValue) {
            case 1:
                enumeration = IdType.NATIONAL_ID;
                break;
            case 2:
                enumeration = IdType.PASSPORT_NUMBER;
            case 3:
                enumeration = IdType.ALIEN_ID;
            case 4:
                enumeration = IdType.MILITARY_ID;
            case 5:
                enumeration = IdType.BIRTH_CERTIFICATE;
            case 6:
                enumeration = IdType.BUSINESS_REGISTRATION_NUMBER;
                break;
        }

        return enumeration;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public boolean hasTypeOf(final IdType type) {
        return this.value.equals(type.getValue());
    }

    public boolean isInvalId() {
        return this.value.equals(IdType.INVALID_ID.getValue());
    }

    public boolean isNationalId() {
        return this.value.equals(IdType.NATIONAL_ID.getValue());
    }

    public boolean isPassportNumber() {
        return this.value.equals(IdType.PASSPORT_NUMBER.getValue());
    }

    public boolean isAlienId() {
        return this.value.equals(IdType.ALIEN_ID.getValue());
    }

    public boolean isMilitaryId() {
        return this.value.equals(IdType.MILITARY_ID.getValue());
    }

    public boolean isBirthCertificate() {
        return this.value.equals(IdType.BIRTH_CERTIFICATE.getValue());
    }

    public boolean isBusinessRegistrationNumber() {
        return this.value.equals(IdType.BUSINESS_REGISTRATION_NUMBER.getValue());
    }
}