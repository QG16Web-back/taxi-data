package com.qg.taxi.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmptyMileageModel extends BaseRowModel {
    @ExcelProperty(value = "车牌号", index = 0)
    private String plateno;

    @ExcelProperty(value = "日期", index = 1)
    private String day;

    @ExcelProperty(value = "空载里程", index = 2)
    private String emptyMileage;
}
