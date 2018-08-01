package com.qg.taxi.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：打车数量
 * motto: All efforts are not in vain
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ExcelPropertyIndexModel extends BaseRowModel {
    @ExcelProperty(value = "打车数量", index = 1)
    private String count;

    @ExcelProperty(value = "日期", index = 0)
    private String time;
}
