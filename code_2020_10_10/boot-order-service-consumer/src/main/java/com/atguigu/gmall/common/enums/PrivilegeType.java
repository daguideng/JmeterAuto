package com.atguigu.gmall.common.enums;

import java.util.ArrayList;
import java.util.List;


public enum PrivilegeType {

    MENU_LEFT_FOLDER("菜单目录"),
    MENU_LEFT_BRANCH("菜单"),
    ELEMENT("页面元素"),
    URL("对外接口"),
    JAVA("java方法名");

    private final String string;

    PrivilegeType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public static List<PrivilegeType> getMenuLeft() {
        List<PrivilegeType> types = new ArrayList<>();
        types.add(PrivilegeType.MENU_LEFT_FOLDER);
        types.add(PrivilegeType.MENU_LEFT_BRANCH);
        return types;
    }

    public static List<PrivilegeType> getPrivilegeType() {
        List<PrivilegeType> types = new ArrayList<>();
        types.add(PrivilegeType.MENU_LEFT_BRANCH);
        types.add(PrivilegeType.MENU_LEFT_FOLDER);
        types.add(PrivilegeType.ELEMENT);
        types.add(PrivilegeType.URL);
        types.add(PrivilegeType.JAVA);
        return types;
    }

}
