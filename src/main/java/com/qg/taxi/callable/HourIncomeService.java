package com.qg.taxi.callable;

import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.model.action.DriverInfo;
import com.qg.taxi.util.SpringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Create by ming on 18-8-3 上午10:36
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
@Scope("prototype")
public class HourIncomeService implements Callable<List<DriverInfo>> {

    private String table;

    public void setTable(String table) {
        this.table = table;
    }

    @Override
    public List<DriverInfo> call() {
        GpsMeterDataHisDao mySql = SpringUtil.getBean(GpsMeterDataHisDao.class);
        System.out.println(table);
        return mySql.getIncomeByHour(table);
    }
}
