package com.hanghae0705.sbmoney.util;

public class MathFloor {
    public static double PercentTenths(double decimal){
        return (Math.round(decimal * 1000 ) / 1000.0) * 100;
    }
}
