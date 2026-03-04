package com.atguigu.gmall.common.enums;

public enum JmeterOperationType {
	API("api"), DATA_PRODUCE("dataProduce"), PERFORMANCE("performance");

	private final String string;

	JmeterOperationType(String string) {
		this.string = string;
	}

	@Override
	public String toString() {
		return this.string;
	}
}
