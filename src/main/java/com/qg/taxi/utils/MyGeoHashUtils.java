package com.qg.taxi.utils;

import ch.hsr.geohash.GeoHash;
import com.qg.taxi.model.gps.Gps;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: Wilder Gao
 * Date: 2018/3/27
 * Time: 10:38
 * Description: GeoHash 处理工具类
 */
public class MyGeoHashUtils {
    /**
     * 通过左下角右上角两个经纬度坐标来得到整个矩形范围的GeoHash集合
     *
     * @param latmin 最小纬度
     * @param lonmin 最小经度
     * @param latmax 最大纬度
     * @param lonmax 最大经度
     * @param precis 想要得到的字符串长度，长度越长代表划分的格子越小
     * @return 整个矩形范围的GeoHash集合
     */
    public static LinkedList<GeoHash> getArea(double latmin, double lonmin, double latmax, double lonmax, int precis) {
        if (latmin > latmax || lonmin > lonmax) {
            return null;
        }
        if (latmin > 90 || latmin < -90 || lonmin > 180 ||
                lonmin < -180 || latmax > 90 || latmax < -90 || lonmax > 180 || lonmax < -180) {
            return null;
        }
        LinkedList<GeoHash> geoList = new LinkedList<>();
        GeoHash ld = GeoHash.withCharacterPrecision(latmin, lonmin, precis);
        GeoHash rd = GeoHash.withCharacterPrecision(latmin, lonmax, precis);
        GeoHash lu = GeoHash.withCharacterPrecision(latmax, lonmin, precis);

        int goflat = 1;
        int goup = 1;
        GeoHash tmp = ld;
        while (!tmp.equals(rd)) {
            tmp = tmp.getEasternNeighbour();
            goflat++;
        }

        tmp = lu;
        while (!tmp.equals(ld)) {
            tmp = tmp.getSouthernNeighbour();
            goup++;
        }
        tmp = lu;
        for (int i = 0; i < goflat; i++) {
            GeoHash tmp2 = tmp;
            geoList.add(tmp2);
            tmp = tmp.getEasternNeighbour();
            for (int j = goup- 2  ; j >= 0; j--) {
                tmp2 = tmp2.getSouthernNeighbour();
                geoList.add(tmp2);
            }
        }

        return geoList;
    }


    /**
     * 通过geoHash字符串来返回对应的经纬度坐标
     *
     * @param geoHashString geoHash字符串
     * @return 经纬度
     */
    public static Gps getGpsByGeoHash(String geoHashString) {
        //设置精度
        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
        //得到GeoHash对象
        GeoHash geoHash = GeoHash.fromGeohashString(geoHashString);
        StringBuilder geoHashPoint = new StringBuilder(geoHash.getPoint().toString());
        Gps gps = new Gps();
        //切掉两个括号
        geoHashPoint.delete(0, 1);
        geoHashPoint.delete(geoHashPoint.length() - 1, geoHashPoint.length());
        String[] gpsPointArray = geoHashPoint.toString().split(",");
        double x = Double.parseDouble(decimalFormat.format(Double.parseDouble(gpsPointArray[0])));
        double y = Double.parseDouble(decimalFormat.format(Double.parseDouble(gpsPointArray[1])));

        x = x  - (0.001 / 85 * 38) + (0.001 /85 * 76 * Math.random());
        y = y - (0.001 / 111 * 38 ) + (0.001 / 111 * 76 * Math.random());

        gps.setLat(x);
        gps.setLng(y);

        return gps;
    }

    public static List<String> getGeoHashString(List<GeoHash> geoHashes){
        List<String> geoHashString = new LinkedList<>();
        for (GeoHash geoHash : geoHashes) {
            geoHashString.add(geoHash.toBase32());
        }
        return geoHashString;
    }

}
