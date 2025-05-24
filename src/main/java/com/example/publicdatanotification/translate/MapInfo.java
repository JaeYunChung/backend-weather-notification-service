package com.example.publicdatanotification.translate;

import org.springframework.stereotype.Component;

@Component
public class MapInfo {

    final static double PI = Math.asin(1.0f)*2.0;
    final static double DEGRAD = PI/180.0;
    final static double RADDEG = 180.0/PI;

    public int[] transSlatToGrid(float longitude, float latitude){
        EarthParam earth = new EarthParam();
        float[] result = mapConv(longitude, latitude, earth);
        return new int[]{(int) (result[0] + 1.5), (int) (result[1] + 1.5)};
    }
    public float[] mapConv(float longitude, float latitude, EarthParam earth){

        double re = earth.Re/earth.grid;
        double slat1 = earth.slat1*DEGRAD;
        double slat2 = earth.slat2*DEGRAD;
        double olon = earth.olon*DEGRAD;
        double olat = earth.olat*DEGRAD;

        double sn = Math.tan(PI*0.25 + slat2*0.5)/Math.tan(PI*0.25 + slat1*0.5);
        sn = Math.log(Math.cos(slat1)/ Math.cos(slat2))/ Math.log(sn);
        double sf = Math.tan(PI*0.25 + slat1*0.5);
        sf = Math.pow(sf, sn)*Math.cos(slat1)/sn;
        double ro = Math.tan(PI*0.25 + olat*0.5);
        ro = re*sf/Math.pow(ro, sn);

        double ra = Math.tan(PI*0.25 + latitude*DEGRAD*0.5);
        ra = re*sf/ Math.pow(ra, sn);
        double theta = longitude * DEGRAD - olon;
        if (theta > PI) theta -= 2.0*PI;
        if (theta < -PI) theta += 2.0*PI;
        theta *= sn;
        return new float[]{(float)(ra*Math.sin(theta)) + earth.x0,
                (float)(ro - ra*Math.cos(theta)) + earth.y0};
    }

}
