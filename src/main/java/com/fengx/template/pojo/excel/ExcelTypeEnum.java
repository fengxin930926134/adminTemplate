package com.fengx.template.pojo.excel;

import lombok.Getter;

/**
 * excel表格类型枚举
 */
@Getter
public enum ExcelTypeEnum {

    EXCEL_TYPE_XLSX("xlsx","EXCEL2007以上版本"),
    EXCEL_TYPE_XLS("xls","EXCEL2003版本");

    private final String code;
    private final String message;

    ExcelTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}