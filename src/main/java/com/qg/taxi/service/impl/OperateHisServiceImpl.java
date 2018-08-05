package com.qg.taxi.service.impl;

import ch.hsr.geohash.GeoHash;
import com.qg.taxi.dao.mysql.OperateHisDao;
import com.qg.taxi.dao.oracle.OracleOperateHisDao;
import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.model.gps.GpsOperateHis;
import com.qg.taxi.model.inform.CalculateGeoHash;
import com.qg.taxi.service.OperateHisService;
import com.qg.taxi.util.DateUtil;
import com.qg.taxi.util.MyGeoHashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

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
    /**
     * 广州经纬度分布，分别是最小纬度、最小经度、最大纬度、最大经度
     */
    private double[] guangzhouRectangle = {22.7179, 113.1674, 23.5187, 113.8142};

    @Autowired
    private OracleOperateHisDao oracleDao;

    @Autowired
    private OperateHisDao mysqlDao;


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
            log.info("正在整OpeHis表数据");
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
            log.info("整理OpeHis结束");

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
    public Map<Integer, List> getAreaGpsMapByDay(double latmin, double lonmin, double latmax, double lonmax, Date date) {
        if (latmin > latmax || lonmin > lonmax) {
            return null;
        }else if (latmin > 90 || latmin < -90 || lonmin > 180 ||
                lonmin < -180 || latmax > 90 || latmax < -90 || lonmax > 180 || lonmax < -180) {
            return null;
        }else {
            String tableName = graphOperateName + DateUtil.getDayAndHour(date).get("day");
            List<GPS> gpsList = mysqlDao.selectGeoHashByTime(tableName, lonmin, lonmax, latmin, latmax);
            if (gpsList.size() == 0){
                return null;
            }else {
                Map<Integer, List> countMap = new HashMap<>();
                for (int i = 0 ; i <= 23 ; i++){
                    countMap.put(i, new ArrayList<>());
                }
                for (GPS gps : gpsList) {
                    gps.setCount(10);
                    countMap.get(gps.getTimeRepre()).add(gps);
                }
                return countMap;
            }
        }
    }

    @Override
    public List<String[]> getGuangzhouRectangle(Date date) {
        List<GeoHash> list = MyGeoHashUtils.getArea(guangzhouRectangle[0], guangzhouRectangle[1],
                guangzhouRectangle[2], guangzhouRectangle[3], 5);
        String tableName = graphOperateName + DateUtil.getDayAndHour(date).get("day");
        int x = 0 , y =0 ;
        List<CalculateGeoHash> resultCal = new ArrayList<>();
        for (GeoHash geoHash : list) {
            List<CalculateGeoHash> geoHash1 = mysqlDao.selectGuangZhou(geoHash.toBase32(), tableName);
            if (geoHash1.size() == 0){
                continue;
            }
            CalculateGeoHash calculateGeoHash = geoHash1.get(0);
            calculateGeoHash.setX(x);
            calculateGeoHash.setY(y++);
            calculateGeoHash.setGeoHash(geoHash.toBase32());

            //这里是x y 并不是经纬度，而是显示在前端可视化中的坐标系，一列有15个方块这样子排列
            if (y >= 15){
                x++;
                y = 0;

            }
            resultCal.add(calculateGeoHash);
        }

        List<String[]> result = new ArrayList<>();
        for (CalculateGeoHash calculateGeoHash : resultCal) {
            String[] calString = new String[4];
            calString[0] = Integer.toString(calculateGeoHash.getX());
            calString[1] = Integer.toString(calculateGeoHash.getY());

            if (calculateGeoHash.getCount() == 0){
                calString[2] = Integer.toString(0);
            }else {
                int color = calculateGeoHash.getCount() / 300;
                if (color >= 4) {
                    calString[2] = Integer.toString(4);
                }else {
                    calString[2] = Integer.toString(color);
                }
            }
            calString[3] = calculateGeoHash.getGeoHash();

            result.add(calString);

        }

        return result;
    }
}
