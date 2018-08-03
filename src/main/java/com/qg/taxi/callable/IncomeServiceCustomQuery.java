package com.qg.taxi.callable;

import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.model.action.DriverInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create by ming on 18-8-3 上午10:33
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
@Scope("prototype")
public class IncomeServiceCustomQuery implements Callable<List<DriverInfo>> {
    private final GpsMeterDataHisDao gpsMeterDataHisDaoSql;

    private int startTime;
    private int endTime;

    @Autowired
    public IncomeServiceCustomQuery(GpsMeterDataHisDao gpsMeterDataHisDaoSql) {
        this.gpsMeterDataHisDaoSql = gpsMeterDataHisDaoSql;
    }

    private static AtomicInteger integer = new AtomicInteger(1);

    public static void setInteger(AtomicInteger integer) {
        IncomeServiceCustomQuery.integer = integer;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public List<DriverInfo> call() {
        String newTable = "meter_data_his" + integer.getAndIncrement();
        List<DriverInfo> infoList = gpsMeterDataHisDaoSql.getIncomeByCustomQuery(newTable, startTime, endTime);

        for (DriverInfo info : infoList) {
            if (Integer.parseInt(newTable.substring(14)) <= 9) {
                info.setDay("2017-02-0" + newTable.substring(14));
            } else if (Integer.parseInt(newTable.substring(14)) <= 28 && Integer.parseInt(newTable.substring(14)) >= 10) {
                info.setDay("2017-02-" + newTable.substring(14));
            } else if (Integer.parseInt(newTable.substring(14)) <= 37 && Integer.parseInt(newTable.substring(14)) >= 29) {
                info.setDay("2017-03-0" + (Integer.parseInt(newTable.substring(14)) - 28));
            } else {
                info.setDay("2017-03-" + (Integer.parseInt(newTable.substring(14)) - 28));
            }
        }
        System.out.println(newTable + gpsMeterDataHisDaoSql);
        return infoList;
    }
}