package com.atguigu.gmall.common.enums;

public enum Gender {
    MALE("男"),
    FEMALE("女");

    private final String string;

    private Gender(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
