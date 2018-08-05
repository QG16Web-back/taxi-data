package com.qg.taxi.web;

import com.qg.taxi.service.OperateHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Wilder Gao
 * time:2018/8/5
 * description：geohash区域显示
 * motto: All efforts are not in vain
 */
@RestController
@RequestMapping("/geohash")
@CrossOrigin
public class GeoHashController {
    @Autowired
    private OperateHisService operateHisService;

    @PostMapping(value = "/rectangle")
    @ResponseBody
    public List<String[]> retangle(@RequestBody Map<String, String> map) throws ParseException {
        String dateString = map.get("date");
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString+" 00:00:00");
        List<String[]> result = operateHisService.getGuangzhouRectangle(date);
        return result;
    }
}
