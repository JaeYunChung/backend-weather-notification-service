package com.example.publicdatanotification.translate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EarthParam {
    float Re = 6371.00877f;
    float grid = 5.0f;
    float slat1 = 30.0f;
    float slat2 = 60.0f;
    float olon = 126.0f;
    float olat = 38.0f;
    float x0 = 210/grid;
    float y0 = 675/grid;
    int first = 0;
}
