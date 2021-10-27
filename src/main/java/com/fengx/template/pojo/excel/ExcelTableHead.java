package com.fengx.template.pojo.excel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * excel表头对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelTableHead {

    /**
     * 一级表头
     */
    private String firstHead;

    /**
     * 二级表头
     */
    private List<String> secondHead = new ArrayList<>();

    /**
     * 表头所占列数
     */
    private int colNumber = 1;

    public void setSecondHead(List<String> secondHead) {
        this.secondHead = secondHead;
        if (secondHead.size() > 1) {
            this.colNumber = secondHead.size();
        }
    }

    public ExcelTableHead(String firstHead) {
        this.firstHead = firstHead;
    }
}