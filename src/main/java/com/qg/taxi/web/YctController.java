package com.qg.taxi.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.taxi.cache.YctCache;
import com.qg.taxi.dto.RequestResult;
import com.qg.taxi.enumeration.StateEnum;
import com.qg.taxi.model.inform.Yct;
import com.qg.taxi.service.YctService;
import com.qg.taxi.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by ming on 18-8-3 上午11:35
 * <p>
 * 羊城通相关
 * </p>
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@RestController
@RequestMapping("/yct")
@CrossOrigin
public class YctController {
    private Gson gson = new Gson();

    @Autowired
    private YctService yctService;

    @GetMapping("/list")
    public RequestResult getYctList() {
        return new RequestResult<>(StateEnum.OK, YctCache.yctList);
    }

    @PostMapping("/onoff")
    public RequestResult getYctOnOff(HttpServletRequest request, @RequestBody Map<String, String> map) {
        File file = new File(request.getServletContext().getRealPath("/take-taxi/yct.json"));
        Map<String, Object> result = new HashMap<>(3);
        if (!file.exists()) {
            return new RequestResult(StateEnum.NO_SUCH_FILE);
        }
        ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>> data = gson.fromJson(FileUtil.getContent(file), new TypeToken<ConcurrentHashMap<String, List<ConcurrentHashMap<String, Object>>>>() {
        }.getType());
        return new RequestResult<>(StateEnum.OK, data.get(map.get("YCT_LOGIC_CARD_NO")));
    }

    @PutMapping("/file/create")
    public RequestResult createYctFile(HttpServletRequest request) throws ParseException, IOException, URISyntaxException {
        return yctService.getOnOff(request);
    }
}
