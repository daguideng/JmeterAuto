package com.atguigu.gmall.common.enums;

/**
 * 组织机构类型
 */
public enum OrgType {

    ORG("组织机构"),
    DEP(" 部门"),
    GROUP("小组");

    private final String string;

    private OrgType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }
}
