package com.qg.taxi.callable;

import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.model.action.TakeTaxiCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create by ming on 18-8-3 上午10:40
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
public class MeterDataService implements Callable<List<TakeTaxiCount>> {

    private final
    GpsMeterDataHisDao gpsMeterDataHisDaoSql;

    @Autowired
    public MeterDataService(GpsMeterDataHisDao gpsMeterDataHisDaoSql) {
        this.gpsMeterDataHisDaoSql = gpsMeterDataHisDaoSql;
    }

    private static AtomicInteger integer = new AtomicInteger(1);

    public static void setInteger(AtomicInteger integer) {
        MeterDataService.integer = integer;
    }

    @Override
    public List<TakeTaxiCount> call() {

        String newTable = "meter_data_his" + integer.getAndIncrement();
        System.out.println(newTable);
        return gpsMeterDataHisDaoSql.totalCount(newTable);
    }
}

