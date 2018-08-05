package com.qg.taxi.web;

import com.qg.taxi.service.DiagramService;
import com.qg.taxi.service.OperateHisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/8/5
 * description：可视化和预测相关的接口
 * motto: All efforts are not in vain
 */
@RestController
@CrossOrigin
@RequestMapping("/predict")
@Slf4j
public class PredictController {

    @Autowired
    private DiagramService diagramService;
    @Autowired
    private OperateHisService operateHisService;

    /**
     *
     * @param map 选定一个区域返回对应的 Map
     * @return
     * @throws ParseException
     * @throws IOException
     */

    @PostMapping("/area")
    public Map<Integer, List> diagramPredict(@RequestBody Map<String, Object> map) throws Exception {
        log.info("区域预测...");

        String dateString = (String) map.get("date");
        double latmin = (double) map.get("latmin");
        double lomin = (double) map.get("lomin");
        double latmax = (double) map.get("latmax");
        double lomax = (double) map.get("lomax");
        String model = (String) map.get("model");

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString+" 00:00:00");
        //得到预测的内容
        Map<Integer, List> diagramResult = diagramService.clientDataMining(latmin, lomin, latmax, lomin, date, model);
        //得到真实的数值
        Map<Integer, List> selectResult = operateHisService.getAreaGpsMapByDay(latmin,
                lomin, latmax, lomax, date);
        if (diagramResult.size() == 0){
            throw new Exception("预测出现错误");
        } else {
            selectResult.putAll(diagramResult);
            return selectResult;
        }
    }

    /**
     * 得到一个点对应这个geoHash区域的真实值和预测值
     */
    @PostMapping(value = "/point")
    @ResponseBody
    public Map<Integer, List> getPointPredict(@RequestBody Map<String, Object> map) throws Exception {
        String dateString = (String) map.get("date");
        //获取经纬度
        double lat = (double) map.get("lat");
        double log = (double) map.get("log");
        String model = (String) map.get("model");

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString+" 00:00:00");
        Map<Integer, List> diagramResult = diagramService.clientDataMining(lat, log, lat, log, date, model);
        return diagramResult;
    }
}
