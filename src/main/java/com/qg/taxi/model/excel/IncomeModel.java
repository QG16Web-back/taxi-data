package com.qg.taxi.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeModel {
    @ExcelProperty(value = "车牌号", index = 0)
    private String plateNo;

    @ExcelProperty(value = "日期", index = 1)
    private String day;

    @ExcelProperty(value = "收入", index = 2)
    private String income;
}
