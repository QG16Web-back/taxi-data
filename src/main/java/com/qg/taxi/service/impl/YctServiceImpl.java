package com.qg.taxi.service.impl;

import com.google.gson.Gson;
import com.qg.taxi.dao.mysql.YctDao;
import com.qg.taxi.dao.oracle.OracleYctDao;
import com.qg.taxi.dto.RequestResult;
import com.qg.taxi.enumeration.StateEnum;
import com.qg.taxi.exception.TaxiException;
import com.qg.taxi.model.gps.GpsString;
import com.qg.taxi.model.gps.OnOffGps;
import com.qg.taxi.model.inform.Yct;
import com.qg.taxi.service.HttpApiService;
import com.qg.taxi.service.YctService;
import com.qg.taxi.util.ConcurrentDateUtil;
import com.qg.taxi.util.CoordinateTransformUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by ming on 18-8-3 下午1:53
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@Service
@Slf4j
public class YctServiceImpl implements YctService {

    @Autowired
    private YctDao yctDao;

    @Autowired
    OracleYctDao oracleYctDao;

    private Gson gson = new Gson();

    @Autowired
    private HttpApiService httpAPIService;

    @Override
    public RequestResult getOnOff(HttpServletRequest request) throws ParseException, IOException, URISyntaxException {
        File file = new File(request.getServletContext().getRealPath("/take-taxi/yct.json"));
        if (file.exists()) {
            throw new TaxiException(StateEnum.FILE_IS_EXIST);
        }


        List<Yct> yctList = yctDao.getYctInfo();
        ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>> resultMap = new ConcurrentHashMap<>();
        for (Yct yct : yctList) {
            double GET_ON_LONGITUDE;
            double GET_OFF_LONGITUDE;
            double GET_ON_LATITUDE;
            double GET_OFF_LATITUDE;
            OnOffGps onOffGps = oracleYctDao.getOnOffGpsOracle(yct.getPlateNo(), ConcurrentDateUtil.format(yct.getWorkBeginTime()));
            if (onOffGps == null) {
                System.out.println("1");
                String[] strings = getStartAndEndTime(ConcurrentDateUtil.format(yct.getWorkBeginTime()));
                GpsString getOn;
                try {
                    getOn = oracleYctDao.getOnOffGpsOracle2(yct.getPlateNo(), strings[0], strings[1]);
                } catch (Exception e) {
                    System.out.println("异常1");
                    continue;
                }
                if (getOn == null) {
                    System.out.println("2");
                    continue;
                }
                GET_ON_LONGITUDE = Double.parseDouble(getOn.getLONGITUDE().replace("+", ""));
                GET_ON_LATITUDE = Double.parseDouble(getOn.getLATITUDE().replace("+", ""));
                String[] endStrings = getStartAndEndTime(ConcurrentDateUtil.format(yct.getWorkEndTime()));
                GpsString getOff;
                try {
                    getOff = oracleYctDao.getOnOffGpsOracle2(yct.getPlateNo(), endStrings[0], endStrings[1]);
                } catch (Exception e) {
                    System.out.println("异常2");
                    continue;
                }
                if (getOff == null) {
                    System.out.println("3");
                    continue;
                }
                GET_OFF_LATITUDE = Double.parseDouble(getOff.getLATITUDE().replace("+", ""));
                GET_OFF_LONGITUDE = Double.parseDouble(getOff.getLONGITUDE().replace("+", ""));
            } else {
                double[] getOnBd09 = CoordinateTransformUtil.wgs84tobd09(onOffGps.getGET_ON_LONGITUDE(), onOffGps.getGET_ON_LATITUDE());
                double[] getOffBd09 = CoordinateTransformUtil.wgs84tobd09(onOffGps.getGET_OFF_LONGITUDE(), onOffGps.getGET_OFF_LATITUDE());
                GET_ON_LONGITUDE = getOnBd09[0];
                GET_ON_LATITUDE = getOnBd09[1];
                GET_OFF_LONGITUDE = getOffBd09[0];
                GET_OFF_LATITUDE = getOffBd09[1];
            }
            log.info("获取街道信息");
            String GET_ON_STREET = getStreet(String.valueOf(GET_ON_LATITUDE), String.valueOf(GET_ON_LONGITUDE));
            String GET_OFF_STREET = getStreet(String.valueOf(GET_OFF_LATITUDE), String.valueOf(GET_OFF_LONGITUDE));

            if (resultMap.containsKey(yct.getYctLogicCardNo())) {
                ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>(9);
                hashMap.put("WORK_BEGIN_TIME", yct.getWorkBeginTime());
                hashMap.put("WORK_END_TIME", yct.getWorkEndTime());
                hashMap.put("PLATENO", yct.getPlateNo());
                hashMap.put("GET_ON_LONGITUDE", GET_ON_LONGITUDE);
                hashMap.put("GET_OFF_LONGITUDE", GET_OFF_LONGITUDE);
                hashMap.put("GET_ON_LATITUDE", GET_ON_LATITUDE);
                hashMap.put("GET_OFF_LATITUDE", GET_OFF_LATITUDE);
                hashMap.put("GET_ON_STREET", GET_ON_STREET);
                hashMap.put("GET_OFF_STREET", GET_OFF_STREET);
                resultMap.get(yct.getYctLogicCardNo()).add(hashMap);
            } else {
                List<ConcurrentHashMap<String, Object>> list = new ArrayList<>();
                ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<>(9);
                hashMap.put("WORK_BEGIN_TIME", yct.getWorkBeginTime());
                hashMap.put("WORK_END_TIME", yct.getWorkEndTime());
                hashMap.put("PLATENO", yct.getPlateNo());
                hashMap.put("GET_ON_LONGITUDE", GET_ON_LONGITUDE);
                hashMap.put("GET_OFF_LONGITUDE", GET_OFF_LONGITUDE);
                hashMap.put("GET_ON_LATITUDE", GET_ON_LATITUDE);
                hashMap.put("GET_OFF_LATITUDE", GET_OFF_LATITUDE);
                hashMap.put("GET_ON_STREET", GET_ON_STREET);
                hashMap.put("GET_OFF_STREET", GET_OFF_STREET);
                list.add(hashMap);
                resultMap.put(yct.getYctLogicCardNo(), list);
            }
        }
        boolean flag = file.createNewFile();
        if (!flag) {
            throw new IOException("创建文件失败");
        }
        log.info("写文件");
        PrintStream printStream = new PrintStream(new FileOutputStream(file));
        printStream.print(gson.toJson(resultMap));
        printStream.close();
        return new RequestResult(StateEnum.OK);
    }

    private String[] getStartAndEndTime(String time) throws ParseException {
        Date date = ConcurrentDateUtil.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, 7);
        String endTime = ConcurrentDateUtil.format(cal.getTime());
        cal.add(Calendar.SECOND, -14);
        String startTime = ConcurrentDateUtil.format(cal.getTime());
        return new String[]{startTime, endTime};
    }

    @SuppressWarnings("unchecked")
    private String getStreet(String lat, String lon) throws IOException, URISyntaxException {
        Map<String, Object> map = new HashMap<>(4);
        map.put("location", lat + "," + lon);
        map.put("output", "json");
        map.put("pois", 0);
        map.put("ak", "FU11miq91ajwbHbpWYv1oXmg3eGgugfB");
        Map<String, Object> result = httpAPIService.doGet("http://api.map.baidu.com/geocoder/v2/", map);
        Map<String, Object> map1 = (Map<String, Object>) result.get("result");
        String name = map1.get("formatted_address").toString();
        if ("".equals(name)) {
            name = "没有定位到街道";
        }
        return name;
    }
}
