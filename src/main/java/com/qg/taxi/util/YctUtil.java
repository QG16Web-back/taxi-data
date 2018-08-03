package com.qg.taxi.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.taxi.dao.mysql.YctDao;
import com.qg.taxi.model.gps.GpsDouble;
import com.qg.taxi.model.gps.OnOffGps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by ming on 18-8-3 上午11:52
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Component
@Slf4j
public class YctUtil {

    private static Gson gson = new Gson();

    @Autowired
    private YctDao yctDao;

    private static YctUtil yctUtil;

    @PostConstruct
    private void init() {
        yctUtil = this;
    }

    public static void getRoadInfo() throws ParseException {
        long start = System.currentTimeMillis();
        log.info("获取路径信息");
        File file = new File("C:\\Users\\Administrator\\Desktop\\TaxiDemo\\test.json");
        ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>> map = gson.fromJson(FileUtil.getContent(file), new TypeToken<ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>>>() {
        }.getType());
        List<ConcurrentHashMap<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, List<ConcurrentHashMap<String, Object>>> entry : map.entrySet()) {
            list.addAll(map.get(entry.getKey()));
        }
        for (ConcurrentHashMap<String, Object> hashMap : list) {
            String startTime = hashMap.get("WORK_BEGIN_TIME").toString();
            String endTime = hashMap.get("WORK_END_TIME").toString();
            List<GpsDouble> dataList = new ArrayList<>();
            if (ConcurrentDateUtil.getDate(ConcurrentDateUtil.parse(startTime), ConcurrentDateUtil.parse(endTime)) > 0) {

            } else {
                int day = ConcurrentDateUtil.getDate(ConcurrentDateUtil.parse(startTime), ConcurrentDateUtil.parse("2017-02-01 00:00:00"));
                List<GpsDouble> gpsList = yctUtil.yctDao.getGps(hashMap.get("PLATENO").toString(), ConcurrentDateUtil.parse(hashMap.get("WORK_BEGIN_TIME").toString()),
                        ConcurrentDateUtil.parse(hashMap.get("WORK_END_TIME").toString()), "gpsdata_copy" + (day + 1));
                dataList.addAll(gpsList);
            }
            hashMap.put("GPS", dataList);
            System.out.println("TEST");
        }
        try {
            File file1 = new File("C:\\Users\\Administrator\\Desktop\\hbase\\road1.json");
            PrintStream printStream = new PrintStream(new FileOutputStream(file1));
            printStream.write(gson.toJson(map).getBytes());
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis() - start) / 1000 + "秒");
    }

    public static void getCarOnOff() throws ParseException {
        File file = new File("C:\\Users\\Administrator\\Desktop\\TaxiDemo\\test.json");
        ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>> map = gson.fromJson(FileUtil.getContent(file), new TypeToken<ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>>>() {
        }.getType());
        List<ConcurrentHashMap<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, List<ConcurrentHashMap<String, Object>>> entry : map.entrySet()) {
            list.addAll(map.get(entry.getKey()));
        }
        for (ConcurrentHashMap<String, Object> hashMap : list) {
            String startTime = hashMap.get("WORK_BEGIN_TIME").toString();
            int day = ConcurrentDateUtil.getDate(ConcurrentDateUtil.parse(startTime), ConcurrentDateUtil.parse("2017-02-01 00:00:00"));
            OnOffGps onOffGps;
            onOffGps = yctUtil.yctDao.getOnOffGps(hashMap.get("PLATENO").toString(), ConcurrentDateUtil.parse(startTime), "operate_his" + (day + 1));
            if (onOffGps == null) {
                System.out.println("TEST");
            } else {
                hashMap.put("GET_ON_LONGITUDE", onOffGps.getGET_ON_LONGITUDE());
                hashMap.put("GET_OFF_LONGITUDE", onOffGps.getGET_OFF_LONGITUDE());
                hashMap.put("GET_ON_LATITUDE", onOffGps.getGET_ON_LATITUDE());
                hashMap.put("GET_OFF_LATITUDE", onOffGps.getGET_OFF_LATITUDE());
            }
        }
        try {
            File file1 = new File("C:\\Users\\Administrator\\Desktop\\TaxiDemo\\test.json");
            PrintStream printStream = new PrintStream(new FileOutputStream(file1));
            printStream.write(gson.toJson(map).getBytes());
            printStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
