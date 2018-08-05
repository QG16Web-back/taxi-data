package com.qg.taxi.service.impl;

import com.google.gson.Gson;
import com.qg.taxi.dao.mysql.GpsMeterDataHisDao;
import com.qg.taxi.dao.mysql.OperateHisDao;
import com.qg.taxi.model.inform.DiagramModel;
import com.qg.taxi.service.DiagramService;
import com.qg.taxi.service.HttpApiService;
import com.qg.taxi.util.DateUtil;
import com.qg.taxi.util.MyGeoHashUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Wilder Gao
 * time:2018/8/5
 * description：
 * motto: All efforts are not in vain
 */
@Service
@Slf4j
public class DiagramServiceImpl implements DiagramService {

    @Autowired
    private OperateHisDao operateHisDao;
    @Autowired
    private HttpApiService httpApiService;

    private final String URL = "http://192.168.1.112:10086/predict/carnum";
    private final String TABLE_NAME = "operate_his";

    @Override
    public Map<Integer, List> clientDataMining(double latmin, double lonmin,
                                               double latmax, double lonmax, Date date, String model) throws Exception {
        if (latmin > latmax || lonmin > lonmax) {
            log.error("请求格式有误");
            return null;
        } else if (latmin > 90 || latmin < -90 || lonmin > 180 || lonmin < -180 ||
                latmax > 90 || latmax < -90 || lonmax > 180 || lonmax < -180) {
            log.error("请求经纬度大小有误");
            return null;
        } else {
            Gson gson = new Gson();
            List<String> geoHashList = MyGeoHashUtils.getGeoHashString(
                    MyGeoHashUtils.getArea(latmin, lonmin, latmax, lonmax, 5));
            List<Integer> clientDataMiningList = new ArrayList<>();
            int[] days = DateUtil.getDayBeforeFive(date);
            if (days.length == 0 || days[0] == 0){
                log.error("=======  请求的时间在２月５号之前  =======");
            }else {
                log.info(" ===== 循环查找当天前五天的数据 ===== ");
                for (int i = 0 ; i < days.length ; i++){
                    List<Integer> dayAreaList = operateHisDao.diagramSelect(TABLE_NAME+days[i], geoHashList);
                    if (dayAreaList.size() != 24){
                        log.error("======= 数据库查询长度有误 =======");
                        throw new Exception("数据库查询长度有误");
                    }
                    clientDataMiningList.addAll(dayAreaList);
                }
                DiagramModel diagramModel = new DiagramModel(model, clientDataMiningList);
                List result = httpApiService.doPostJsonMap(URL, gson.toJson(diagramModel));
                if (result.size() != 0) {
                    Map<Integer, List> resultMap = new HashMap<>(23);
                    //查找对应的当天的数据
                    int day = DateUtil.getDayAndHour(date).get("day");
                    log.info(" ==== 第 "+day +" 天的数据 ====");
                    List<Integer> currentDayList = operateHisDao.diagramSelect(TABLE_NAME+day, geoHashList);
                    resultMap.put(24, currentDayList);
                    resultMap.put(25, result);
                    return resultMap;
                }
            }
        }
        return null;
    }
}
