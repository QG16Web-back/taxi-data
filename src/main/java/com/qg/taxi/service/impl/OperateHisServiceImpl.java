package com.qg.taxi.service.impl;

import com.qg.taxi.dao.oracle.OracleOperateHisDao;
import com.qg.taxi.model.gps.Gps;
import com.qg.taxi.model.gps.GpsOperateHis;
import com.qg.taxi.service.OperateHisService;
import com.qg.taxi.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/7/23
 * description：
 * motto: All efforts are not in vain
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class OperateHisServiceImpl implements OperateHisService {

    private static final String graphName = "meter_data_his";
    private static final String graphOperateName = "operate_his";

    @Autowired
    private OracleOperateHisDao oracleDao;

    @Autowired
    private com.qg.taxi.dao.mysql.OperateHisDao mysqlDao;


    @Override
    public void insertOperateHisToMysql(int start, int end, int k, int size) throws ParseException {
        int times = 1;
        while(start <= size*k && end <= size*k) {
            List<List<GpsOperateHis>> sortGpsOpsByDayList = new ArrayList<>(60);
            int iterator = 0;
            int dayNum = 60;

            while (iterator < dayNum) {
                List<GpsOperateHis> gpsList = new ArrayList<>();
                sortGpsOpsByDayList.add(iterator, gpsList);
                iterator++;
            }
            List<GpsOperateHis> operateHisList = oracleDao.selectOperateHisByNum(start, end);

            //将得到的数据进行分类
            log.info("==================================================正在整OpeHis表数据==================================================");
            for (GpsOperateHis operateHis : operateHisList) {
                if (null == operateHis.getPlateNo()) {
                    continue;
                }
                //得到这一条数据所对应的日期
                Map dayAndHourMap = DateUtil.getDayAndHour(operateHis.getWorkBeginTime());
                int dayOfGps = (int) dayAndHourMap.get("day");
                int hour = (int) dayAndHourMap.get("hour");
                int year = (int) dayAndHourMap.get("year");
                int month = (int) dayAndHourMap.get("month");

                //清理垃圾数据
                if (dayOfGps > 60 || year != 2017){ continue;}
                if (month != 2 && month != 3){ continue;}

                operateHis.setTimeRepre(hour);
                sortGpsOpsByDayList.get(dayOfGps - 1).add(operateHis);
            }
            log.info("==================================================整理OpeHis结束====================================================");

            for (int i = 1; i <= sortGpsOpsByDayList.size(); i++) {
                String graphNameAdd = graphOperateName + i;
                if (sortGpsOpsByDayList.get(i - 1).size() == 0){ continue;}
                mysqlDao.addGpsOperateHisList(graphNameAdd, sortGpsOpsByDayList.get(i-1));
            }

            start += size;
            end += size;
            operateHisList.clear();
            //清理集合信息
            for (List<GpsOperateHis> gpsMeterDataHis : sortGpsOpsByDayList) {
                gpsMeterDataHis.clear();
            }
            sortGpsOpsByDayList.clear();
            System.out.println("<<<<<<<<导入第"+times+"次成功>>>>>>>>>>>");
            times++;
        }
    }



    @Override
    public Map<Integer, List<Gps>> getAreaGpsMapByDay(double latmin, double lonmin, double latmax, double lonmax, Date date) {
        return null;
    }
}
