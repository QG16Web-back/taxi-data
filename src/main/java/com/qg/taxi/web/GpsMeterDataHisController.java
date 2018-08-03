package com.qg.taxi.web;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qg.taxi.callable.*;
import com.qg.taxi.model.action.DriverInfo;
import com.qg.taxi.model.action.TakeTaxiCount;
import com.qg.taxi.model.excel.ExcelPropertyIndexModel;
import com.qg.taxi.service.GpsMeterDataHisService;
import com.qg.taxi.util.ConcurrentDateUtil;
import com.qg.taxi.util.ExcelUtil;
import com.qg.taxi.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create by ming on 18-8-3 上午9:47
 *
 * @author ming
 * I'm the one to ignite the darkened skies.
 */
@RestController
@CrossOrigin
@Slf4j
public class GpsMeterDataHisController {

    @Autowired
    private GpsMeterDataHisService gpsMeterDataHisService;

    private final String[] title = {
            "车牌号", "2017-02-01", "2017-02-02", "2017-02-03",
            "2017-02-04", "2017-02-05", "2017-02-06", "2017-02-07",
            "2017-02-08", "2017-02-09", "2017-02-10", "2017-02-11",
            "2017-02-12", "2017-02-13", "2017-02-14", "2017-02-15",
            "2017-02-16", "2017-02-17", "2017-02-18", "2017-02-19",
            "2017-02-20", "2017-02-21", "2017-02-22", "2017-02-23",
            "2017-02-24", "2017-02-25", "2017-02-26", "2017-02-27",
            "2017-02-28", "2017-03-01", "2017-03-02", "2017-03-03",
            "2017-03-04", "2017-03-05", "2017-03-06", "2017-03-07",
            "2017-03-08", "2017-03-09", "2017-03-10", "2017-03-11",
            "2017-03-12", "2017-03-13", "2017-03-14", "2017-03-15",
            "2017-03-16", "2017-03-17", "2017-03-18", "2017-03-19",
            "2017-03-20", "2017-03-21", "2017-03-22", "2017-03-23",
            "2017-03-24", "2017-03-25", "2017-03-26", "2017-03-27",
            "2017-03-28", "2017-03-29", "2017-03-30", "2017-03-31"
    };

    private final String[] hourTitle = {
            "车牌号", "00:00:00-01:00:00", "01:00:00-02:00:00", "02:00:00-03:00:00",
            "03:00:00-04:00:00", "04:00:00-05:00:00", "05:00:00-06:00:00", "06:00:00-07:00:00",
            "07:00:00-08:00:00", "08:00:00-09:00:00", "09:00:00-10:00:00", "10:00:00-11:00:00",
            "11:00:00-12:00:00", "12:00:00-13:00:00", "13:00:00-14:00:00", "14:00:00-15:00:00",
            "15:00:00-16:00:00", "16:00:00-17:00:00", "17:00:00-18:00:00", "18:00:00-19:00:00", "19:00:00-20:00:00",
            "20:00:00-21:00:00", "21:00:00-22:00:00", "22:00:00-23:00:00", "23:00:00-24:00:00",
    };

    @RequestMapping(
            value = "empty-custom-query",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getEmptyByCustomQuery(HttpServletRequest request, @RequestBody Map<String, String> map) throws Exception {
        File file = new File(request.getServletContext().getRealPath("/take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-空载量(单位：公里).xls"));
        Map<String, String> result = new HashMap<>(1);

        if (file.exists()) {
            result.put("link", "take-taxi/按天划分-自定义（" + map.get("startTime") +
                    "-" + map.get("endTime") + "）-空载量(单位：公里).xls");
            return result;
        }

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();
        Map<String, double[]> dataMap = new HashMap<>();
        long start = System.currentTimeMillis();
        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<EmptyMileageCustomQueryService> list = new ArrayList<>();
        EmptyMileageCustomQueryService.setInteger(new AtomicInteger(1));
        for (int i = 0; i < 59; i++) {
            EmptyMileageCustomQueryService service = SpringUtil.getBean(EmptyMileageCustomQueryService.class);
            service.setEndTime(Integer.parseInt(map.get("endTime")));
            service.setStartTime(Integer.parseInt(map.get("startTime")));
            list.add(service);
        }
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(list);
            for (Future<List<DriverInfo>> future : futures) {
                if (future.isDone()) {
                    List<DriverInfo> infoList = future.get();
                    data.addAll(infoList);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Date date = ConcurrentDateUtil.ymdParse("2017-02-01");
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0
                });
            }
            Date date1 = ConcurrentDateUtil.ymdParse(info.getDay());
            dataMap.get(info.getNumber())[(int) ((date1.getTime() - date.getTime()) / (1000 * 3600 * 24))] = info.getEmptyMileage();
        }
        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("按天划分查询时间（空载量）:" + (end - start));
        if (!file.exists()) {
            boolean flag = file.createNewFile();
            if (!flag) {
                throw new Exception("create file fail");
            }
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-空载量(单位：公里).xls"), title, dataMap, "sheet");
        result.put("link", "take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-空载量(单位：公里).xls");
        return result;
    }

    @RequestMapping(
            value = "income-custom-query",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getIncomeByCustomQuery(HttpServletRequest request, @RequestBody Map<String, String> map) throws IOException,
            ParseException {

        File file = new File(request.getServletContext().getRealPath("/take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-收入(单位：元).xls"));
        Map<String, String> result = new HashMap<>(1);
        if (file.exists()) {
            result.put("link", "take-taxi/按天划分-自定义（" + map.get("startTime") +
                    "-" + map.get("endTime") + "）-收入(单位：元).xls");
            return result;
        }

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();
        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<IncomeServiceCustomQuery> list = new ArrayList<>();
        IncomeServiceCustomQuery.setInteger(new AtomicInteger(1));
        for (int i = 0; i < 59; i++) {
            IncomeServiceCustomQuery service = SpringUtil.getBean(IncomeServiceCustomQuery.class);
            service.setEndTime(Integer.parseInt(map.get("endTime")));
            service.setStartTime(Integer.parseInt(map.get("startTime")));
            list.add(service);
        }
        long start = System.currentTimeMillis();
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(list);
            for (Future<List<DriverInfo>> future : futures) {
                data.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Date date = ConcurrentDateUtil.ymdParse("2017-02-01");
        Map<String, double[]> dataMap = new HashMap<>();
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0
                });
            }
            Date date1 = ConcurrentDateUtil.ymdParse(info.getDay());
            dataMap.get(info.getNumber())[(int) ((date1.getTime() - date.getTime()) / (1000 * 3600 * 24))] = info.getIncome();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-收入(单位：元).xls"), title, dataMap, "sheet");
        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("按天划分自查询时间（收入）" + (end - start));

        result.put("link", "take-taxi/按天划分-自定义（" + map.get("startTime") +
                "-" + map.get("endTime") + "）-收入(单位：元).xls");
        return result;
    }

    @RequestMapping(
            value = "/empty-hour",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getEmptyMileageByHour(HttpServletRequest request, @RequestBody Map<String, String> map) throws IOException, ParseException {
        Map<String, String> result = new HashMap<>(1);
        File file = new File(request.getServletContext().getRealPath("/take-taxi/" + map.get("day") + "-空载量（单位：公里）.xls"));
        if (file.exists()) {
            result.put("link", "take-taxi/" + map.get("day") + "-空载量（单位：公里）.xls");
            return result;
        }

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();
        Date startDay = ConcurrentDateUtil.ymdParse("2017-02-01");
        Date date = ConcurrentDateUtil.ymdParse(map.get("day"));
        String table = "meter_data_his" + (int) ((date.getTime() - startDay.getTime()) / (1000 * 3600 * 24) + 1);
        long start = System.currentTimeMillis();
        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<HourEmptyMileageService> services = new ArrayList<>();
        HourEmptyMileageService service = SpringUtil.getBean(HourEmptyMileageService.class);
        service.setTable(table);
        services.add(service);
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(services);
            for (Future<List<DriverInfo>> future : futures) {
                data.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Map<String, double[]> dataMap = new HashMap<>();
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0
                });
            }
            dataMap.get(info.getNumber())[info.getHour()] = info.getEmptyMileage();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/" + map.get("day") + "-空载量（单位：公里）.xls"), hourTitle, dataMap, "sheet");
        long end = System.currentTimeMillis();
        System.out.println("按小时划分查询时间（空载量）：" + (end - start));
        pool.shutdown();
        result.put("link", "take-taxi/" + map.get("day") + "-空载量（单位：公里）.xls");
        return result;
    }

    @RequestMapping(
            value = "/income-hour",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getIncomeByHour(HttpServletRequest request, @RequestBody Map<String, String> map) throws ParseException, IOException {

        File file = new File(request.getServletContext().getRealPath("/take-taxi/" + map.get("day") + "-收入（单位：元）.xls"));
        Map<String, String> result = new HashMap<>(1);
        if (file.exists()) {
            result.put("link", "take-taxi/" + map.get("day") + "-收入（单位：元）.xls");
            return result;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();
        Date startDay = ConcurrentDateUtil.ymdParse("2017-02-01");
        Date date = ConcurrentDateUtil.ymdParse(map.get("day"));
        String table;
        long start = System.currentTimeMillis();
        table = "meter_data_his" + (int) ((date.getTime() - startDay.getTime()) / (1000 * 3600 * 24) + 1);
        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<HourIncomeService> services = new ArrayList<>();
        HourIncomeService service = SpringUtil.getBean(HourIncomeService.class);
        service.setTable(table);
        services.add(service);
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(services);
            for (Future<List<DriverInfo>> future : futures) {
                data.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Map<String, double[]> dataMap = new HashMap<>();
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0
                });
            }
            dataMap.get(info.getNumber())[info.getHour()] = info.getIncome();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/" + map.get("day") + "-收入（单位：元）.xls"), hourTitle, dataMap, "sheet");
        long end = System.currentTimeMillis();
        System.out.println("按小时划分查询时间（收入）：" + (end - start));
        pool.shutdown();
        result.put("link", "take-taxi/" + map.get("day") + "-收入（单位：元）.xls");
        return result;
    }

    @RequestMapping(
            value = "/empty",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getEmptyMileage(HttpServletRequest request) throws IOException, ParseException {

        File file = new File(request.getServletContext().getRealPath("/take-taxi/按天划分-空载量（单位：公里）.xls"));
        Map<String, String> map = new HashMap<>(1);
        if (file.exists()) {
            map.put("link", "take-taxi/按天划分-空载量（单位：公里）.xls");
            return map;
        }

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();

        Map<String, double[]> dataMap = new HashMap<>();
        long start = System.currentTimeMillis();
        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<EmptyMileageService> list = new ArrayList<>();
        EmptyMileageService.setInteger(new AtomicInteger(1));
        for (int i = 0; i < 59; i++) {
            EmptyMileageService service = SpringUtil.getBean(EmptyMileageService.class);
            list.add(service);
        }
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(list);
            for (Future<List<DriverInfo>> future : futures) {
                if (future.isDone()) {
                    List<DriverInfo> infoList = future.get();
                    data.addAll(infoList);
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Date date = ConcurrentDateUtil.ymdParse("2017-02-01");
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0
                });
            }
            Date date1 = ConcurrentDateUtil.ymdParse(info.getDay());
            dataMap.get(info.getNumber())[(int) ((date1.getTime() - date.getTime()) / (1000 * 3600 * 24))] = info.getEmptyMileage();
        }
        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("按天划分查询时间（空载量）:" + (end - start));
        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/按天划分-空载量（单位：公里）.xls"), title, dataMap, "sheet");
        map.put("link", "take-taxi/按天划分-空载量（单位：公里）.xls");
        return map;
    }

    @RequestMapping(
            value = "/income",
            method = RequestMethod.POST,
            produces = "application/json"
    )
    public Map<String, String> getIncomeByDay(HttpServletRequest request) throws IOException, ParseException {
        File file = new File(request.getServletContext().getRealPath("/take-taxi/按天划分-收入（单位：元）.xls"));
        Map<String, String> map = new HashMap<>(1);
        if (file.exists()) {
            map.put("link", "take-taxi/按天划分-收入（单位：元）.xls");
            return map;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();

        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                60,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        List<IncomeService> list = new ArrayList<>();
        IncomeService.setInteger(new AtomicInteger(1));
        for (int i = 0; i < 59; i++) {
            IncomeService service = SpringUtil.getBean(IncomeService.class);
            list.add(service);
        }
        long start = System.currentTimeMillis();
        List<DriverInfo> data = new ArrayList<>();
        try {
            List<Future<List<DriverInfo>>> futures = pool.invokeAll(list);
            for (Future<List<DriverInfo>> future : futures) {
                data.addAll(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        Date date = ConcurrentDateUtil.ymdParse("2017-02-01");
        Map<String, double[]> dataMap = new HashMap<>();
        for (DriverInfo info : data) {
            if (!dataMap.containsKey(info.getNumber())) {
                dataMap.put(info.getNumber(), new double[]{
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0
                });
            }
            Date date1 = ConcurrentDateUtil.ymdParse(info.getDay());
            dataMap.get(info.getNumber())[(int) ((date1.getTime() - date.getTime()) / (1000 * 3600 * 24))] = info.getIncome();
        }

        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeData(request.getServletContext().getRealPath("/take-taxi/按天划分-收入（单位：元）.xls"), title, dataMap, "sheet");
        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("按天划分查询时间（收入）" + (end - start));
        map.put("link", "take-taxi/按天划分-收入（单位：元）.xls");
        return map;
    }

    @RequestMapping(
            value = "/total",
            method = RequestMethod.POST
    )
    public Map<String, String> getTakeTaxiTotalCount(HttpServletRequest request) throws IOException {
        File file = new File(request.getServletContext().getRealPath("/take-taxi/打车总量（单位：次数）.xlsx"));
        Map<String, String> map = new HashMap<>(1);
        if (file.exists()) {
            map.put("link", "take-taxi/打车总量（单位：次数）.xlsx");
            return map;
        }
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("查询-%d").build();

        // Common Thread Pool
        ExecutorService pool = new ThreadPoolExecutor(
                4,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        long startTime = System.currentTimeMillis();
        List<MeterDataService> list = new ArrayList<>();
        MeterDataService.setInteger(new AtomicInteger(1));
        for (int i = 0; i < 59; i++) {
            MeterDataService service = SpringUtil.getBean(MeterDataService.class);
            list.add(service);
        }
        List<ExcelPropertyIndexModel> data = new ArrayList<>();
        try {
            List<Future<List<TakeTaxiCount>>> futures = pool.invokeAll(list);
            int index = 1;
            for (Future<List<TakeTaxiCount>> future : futures) {
                for (TakeTaxiCount taxiCount : future.get()) {
                    ExcelPropertyIndexModel model;
                    String time;
                    if (taxiCount.getTime() >= 0 && taxiCount.getTime() <= 9) {
                        time = "0" + String.valueOf(taxiCount.getTime());
                    } else {
                        time = String.valueOf(taxiCount.getTime());
                    }
                    if (index >= 1 && index <= 28) {
                        if (index <= 9) {
                            model = new ExcelPropertyIndexModel(String.valueOf(taxiCount.getCount()),
                                    "2017-02-0" + String.valueOf(index) + " " + time + ":00:00");
                        } else {
                            model = new ExcelPropertyIndexModel(String.valueOf(taxiCount.getCount()),
                                    "2017-02-" + String.valueOf(index) + " " + time + ":00:00");
                        }
                    } else {
                        if (index <= 37) {
                            model = new ExcelPropertyIndexModel(String.valueOf(taxiCount.getCount()),
                                    "2017-03-0" + String.valueOf(index - 28) + " " + time + ":00:00");
                        } else {
                            model = new ExcelPropertyIndexModel(String.valueOf(taxiCount.getCount()),
                                    "2017-03-" + String.valueOf(index - 28) + " " + time + ":00:00");
                        }
                    }
                    data.add(model);
                }
                index++;
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        ExcelUtil.writeExcel(request.getServletContext().getRealPath("/take-taxi/打车总量（单位：次数）.xlsx"), data);
        long endTime = System.currentTimeMillis();
        log.info("查询用时： " + String.valueOf(endTime - startTime));
        pool.shutdown();
        map.put("link", "take-taxi/打车总量（单位：次数）.xlsx");
        return map;
    }
}
