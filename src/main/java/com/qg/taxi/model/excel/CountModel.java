package com.qg.taxi.model.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CountModel extends BaseRowModel {
    /**
     * 查询的表名
     */
    private String tableName;

    /**
     * geoHash字符串
     */
    @ExcelProperty(value = "geoHash", index = 3)
    private String geoHash;
    /**
     * 区域车总量
     */
    @ExcelProperty(value = {"count"}, index = 1)
    private int count;
    /**
     * 时间标记
     */
    private int timeRepre;

    /**
     * 分钟时间戳
     */
    private int minRepre;

    /**
     * 时间字符串表达式，格式为 timeRepre:minRepre
     */
    @ExcelProperty(value = {"time"},index = 2)
    private String time;
}
