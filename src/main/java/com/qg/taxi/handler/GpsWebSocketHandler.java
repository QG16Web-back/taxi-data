package com.qg.taxi.handler;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.taxi.config.websocket.HandShake;
import com.qg.taxi.hbase.HBaseSample;
import com.qg.taxi.model.gps.GPS;
import com.qg.taxi.service.HttpApiService;
import com.qg.taxi.util.CoordinateTransformUtil;
import com.qg.taxi.util.ExcelUtil;
import com.qg.taxi.util.MyGeoHashUtils;
import com.sun.istack.internal.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Wilder Gao
 * time:2018/7/30
 * description：
 * motto: All efforts are not in vain
 */
@Component
@Slf4j
public class GpsWebSocketHandler implements WebSocketHandler  {

    @Autowired
    private HttpApiService apiService;

    @Override
    public void afterConnectionEstablished(@Nullable WebSocketSession webSocketSession) {
        Gson gson = new Gson();
        try {
            TextMessage response = new TextMessage(gson.toJson(ExcelUtil.getPlate()));
            webSocketSession.sendMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("ConnectionEstablished: " + Objects.requireNonNull(Objects.requireNonNull(webSocketSession).getRemoteAddress()).toString());
    }

    @Override
    public void handleMessage(@Nullable WebSocketSession webSocketSession, @Nullable WebSocketMessage<?> webSocketMessage) throws Exception {
        System.out.println("收到信息：" + Objects.requireNonNull(webSocketMessage).getPayload().toString());
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(webSocketMessage.getPayload().toString(), new TypeToken<Map<String, String>>() {
        }.getType());
        if ("1".equals(map.get("index"))) {
            getThermalMap(webSocketSession, map);
        } else if ("2".equals(map.get("index"))) {
            getGpsRoad(webSocketSession, map);
        }
    }

    private void getGpsRoad(WebSocketSession webSocketSession, Map<String, String> map) throws IOException, InterruptedException {
        HBaseSample hBaseSample = new HBaseSample(HandShake.getConf(), "gpsdata_copy");
        System.out.println("准备进行HBase数据测试！");
        Table table = null;
        ResultScanner rScanner = null;
        Gson gson = new Gson();
        try {
            long start = System.currentTimeMillis();
            table = hBaseSample.getConn().getTable(hBaseSample.getTableName());
            Scan scan = new Scan();
            scan.addFamily(Bytes.toBytes("gps"));
            scan.setStartRow(Bytes.toBytes(map.get("startTime")));
            scan.setStopRow(Bytes.toBytes(map.get("endTime")));
            scan.setCaching(2000);
            Filter filter = new RowFilter(CompareFilter.CompareOp.EQUAL,
                    new SubstringComparator(map.get("plate").substring(1, 7)));
            scan.setFilter(filter);
            rScanner = table.getScanner(scan);
            for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
                double lng = 0;
                double lat = 0;
                for (Cell cell : r.rawCells()) {
                    if ("LATITUDE".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                        lat = Double.parseDouble(Bytes.toString(CellUtil.cloneValue(cell)));
                    } else {
                        lng = Double.parseDouble(Bytes.toString(CellUtil.cloneValue(cell)));
                    }
                }

                double[] result = CoordinateTransformUtil.wgs84tobd09(lng, lat);
                Map<String, Double> resultMap = new HashMap<>(2);
                resultMap.put("lng", result[0]);
                resultMap.put("lat", result[1]);
                TextMessage response = new TextMessage(gson.toJson(resultMap));
                webSocketSession.sendMessage(response);
                Thread.sleep(1000);
            }
            long end = System.currentTimeMillis();
            System.out.println("时间：" + (end - start));
        } catch (IOException e) {
            log.error("Scan data failed ", e);
        } finally {
            if (rScanner != null) {
                // Close the scanner object.
                rScanner.close();
            }
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    log.error("Close table failed ", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void changeGpsToBaidu(Map<String, Double> map) throws IOException, URISyntaxException {
        Map<String, Object> requestMap = new HashMap<>();
        String coords = map.get("lng")+","+map.get("lat");
        requestMap.put("coords", coords);
        requestMap.put("ak", "FU11miq91ajwbHbpWYv1oXmg3eGgugfB");
        requestMap.put("from", 1);
        requestMap.put("output", "json");
        Map<String, Object> resultMap = apiService.doGet("http://api.map.baidu.com/geoconv/v1/", requestMap);
        List<Map<String, Double>> locations = (List<Map<String, Double>>) resultMap.get("result");
        map.put("lng", locations.get(0).get("x"));
        map.put("lat", locations.get(0).get("y"));
        System.out.println(resultMap);
    }

    private void getThermalMap(WebSocketSession webSocketSession, Map<String, String> map) throws InterruptedException, IOException {
        HBaseSample hBaseSample = new HBaseSample(HandShake.getConf(), "operate_his");

        System.out.println("准备进行HBase数据测试！");
        Table table = null;
        ResultScanner rScanner = null;
        Gson gson = new Gson();
        try {
            long start = System.currentTimeMillis();
            table = hBaseSample.getConn().getTable(hBaseSample.getTableName());
            Scan scan = new Scan();
            scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("GEOHASH7"));
            // 注意：hbase
            scan.setStartRow(Bytes.toBytes(map.get("startTime")));
            scan.setStopRow(Bytes.toBytes(map.get("endTime")));
            scan.setCaching(2000);

            rScanner = table.getScanner(scan);
            int i = 0;
            List<GPS> list = new ArrayList<>();

            Result[] r;
            while ((r = rScanner.next(300)) != null) {
                Map<String, Object> resultMap = new HashMap<>(2);
                String time = null;
                String resultTime;
                if (r.length == 0) {
                    break;
                }
                i += r.length;
                for (Cell cell : r[0].rawCells()) {
                    time = Bytes.toString(CellUtil.cloneRow(cell)).substring(0, 8);
                }
                assert time != null;
                resultTime = time.substring(0, 2) + "月" + time.substring(2, 4) + "日" + time.substring(4, 6) + "时" + time.substring(6, 8) + "分";
                resultMap.put("time", resultTime);
                for (Result result : r) {
                    for (Cell cell : result.rawCells()) {
                        GPS gps = MyGeoHashUtils.getGpsByGeoHash(Bytes.toString(CellUtil.cloneValue(cell)));
                        gps.setCount(50);
                        list.add(gps);
                    }
                }
                resultMap.put("list", list);
                TextMessage response = new TextMessage(gson.toJson(resultMap));
                Objects.requireNonNull(webSocketSession).sendMessage(response);
                list.clear();
                Thread.sleep(1000);
            }
            System.out.println("条数：" + i);
            long end = System.currentTimeMillis();
            System.out.println("时间：" + (end - start));
        } catch (IOException e) {
            log.error("Scan data failed ", e);
        } finally {
            if (rScanner != null) {
                // Close the scanner object.
                rScanner.close();
            }
            if (table != null) {
                try {
                    // Close the HTable object.
                    table.close();
                } catch (IOException e) {
                    log.error("Close table failed ", e);
                }
            }
        }
    }

    @Override
    public void handleTransportError(@Nullable WebSocketSession webSocketSession, @Nullable Throwable throwable) throws Exception {
        if (Objects.requireNonNull(webSocketSession).isOpen()) {
            webSocketSession.close();
        }
        log.info("WebSocket connection close");
    }

    @Override
    public void afterConnectionClosed(@Nullable WebSocketSession webSocketSession, @Nullable CloseStatus closeStatus) {
        log.info("Connection closed..." + (webSocketSession != null ? Objects.requireNonNull(webSocketSession.getRemoteAddress()).toString() : null));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
