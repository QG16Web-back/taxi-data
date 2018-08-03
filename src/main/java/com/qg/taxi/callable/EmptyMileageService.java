package com.qg.taxi.callable;

import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.model.action.DriverInfo;
import com.qg.taxi.utils.SpringUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create by ming on 18-8-3 上午10:37
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
@Scope("prototype")
public class EmptyMileageService implements Callable<List<DriverInfo>> {

    private static AtomicInteger integer = new AtomicInteger(1);

    public static void setInteger(AtomicInteger integer) {
        EmptyMileageService.integer = integer;
    }

    @Override
    public List<DriverInfo> call() {
        String newTable = "meter_data_his" + integer.getAndIncrement();
        GpsMeterDataHisDao mySql = SpringUtil.getBean(GpsMeterDataHisDao.class);
        List<DriverInfo> infoList = mySql.getEmptyMileage(newTable);
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
        return infoList;
    }
}
