package com.qg.taxi.service.impl;

import ch.hsr.geohash.GeoHash;
import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.dao.oracle.OracleGpsMeterDataHisDao;
import com.qg.taxi.model.excel.CountModel;
import com.qg.taxi.model.gps.GpsMeterDataHis;
import com.qg.taxi.service.GpsMeterDataHisService;
import com.qg.taxi.util.DateUtil;
import com.qg.taxi.util.ExcelUtil;
import com.qg.taxi.util.MyGeoHashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
public class GpsMeterDataHisServiceImpl implements GpsMeterDataHisService {
    private static final String GRAPH_NAME = "meter_data_his";
    private static final String GRAPH_OPERATE_HIS = "operate_his";

    @Autowired
    private OracleGpsMeterDataHisDao oracleDao;

    @Autowired
    private GpsMeterDataHisDao mysqlDao;

    @Override
    public void insertMeterHisToMysql(int start, int end, int k, int size) throws ParseException {
        int times = 130;
        int dayNum = 60;
        while(start <= size*k && end <= size*k) {
            List<List<GpsMeterDataHis>> sortGpsByDayList = new ArrayList<>(60);

            //先创建出大小为60的集合，存放60天的数量
            int iterator = 0;
            while (iterator < dayNum) {
                List<GpsMeterDataHis> gpsList = new ArrayList<>();
                sortGpsByDayList.add(iterator, gpsList);
                iterator++;
            }

            List<GpsMeterDataHis> dataHisList = oracleDao.selectMeterDataHisByNum(start, end);

            //将得到的数据进行分类
            log.info("=========正在整理数据=========");
            for (GpsMeterDataHis gpsMeterDataHis : dataHisList) {
                if (null == gpsMeterDataHis.getPlateNo()) {
                    continue;
                }
                //得到这一条数据所对应的日期
                Map dayAndHourMap = DateUtil.getDayAndHour(gpsMeterDataHis.getWorkBeginTime());

                //获得日期，这样对应插入的数据库表
                int dayOfGps = (int) dayAndHourMap.get("day");
                if (dayOfGps > 60){ continue;}
                gpsMeterDataHis.setTimeRepre((Integer) dayAndHourMap.get("hour"));
                //将数据插入到对应的天数上
                sortGpsByDayList.get(dayOfGps - 1).add(gpsMeterDataHis);
            }

            log.info("==========整理结束==========");

            for (int i = 1; i <= sortGpsByDayList.size(); i++) {
                String graphNameAdd = GRAPH_NAME + i;
                if (sortGpsByDayList.get(i - 1).size() == 0){ continue;}
                mysqlDao.addGpsMeterHisList(graphNameAdd, sortGpsByDayList.get(i - 1));
            }

            start += size;
            end += size;

            //成功一次之后将集合中所有集合都清空
            dataHisList.clear();
            for (List<GpsMeterDataHis> gpsMeterDataHises : sortGpsByDayList) {
                gpsMeterDataHises.clear();
            }

            sortGpsByDayList.clear();

            log.info("<<<<<<<<导入第"+times+"次成功>>>>>>>>>>>");
            times++;
        }
    }


    @Override
    public String countGeoHashByTime(double latmin, double lonmin, double latmax, double lonmax, int precis, Date date) throws IOException {
        //获得这个区域的geoHash分布
        List<GeoHash> geoHashList = MyGeoHashUtils.getArea(latmin, lonmin, latmax, lonmax, precis);

        //没有这个区域的情况
        if (null == geoHashList || geoHashList.size() == 0 || null == date) {
            return null;
        }else {
            Map<String, List<CountModel>> countMap = new HashMap<>(6);
            //获得表名
            String tableName = GRAPH_OPERATE_HIS + DateUtil.getDayAndHour(date).get("day");
            for (GeoHash geoHash : geoHashList) {
                List<CountModel> countModels = mysqlDao.countByTimeGeoHash(tableName, geoHash.toBase32());
                if (null != countModels){
                    countMap.put(geoHash.toBase32(), countModels);
                }
            }
            return ExcelUtil.createExcel(countMap);
        }
    }
}
