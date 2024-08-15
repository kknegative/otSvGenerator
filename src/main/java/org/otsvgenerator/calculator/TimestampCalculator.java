package org.otsvgenerator.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TimestampCalculator {
    // 1分钟/bpm计算每一个小节的距离(in ms)
    public static BigDecimal getBeatLength(BigDecimal bpm) {
        return new BigDecimal(60000).divide(bpm, new MathContext(15, RoundingMode.HALF_EVEN));
    }

    // 根据snap(eg. 1/8), 计算物件之间的距离
    public static BigDecimal getIntervalFromSnap(BigDecimal beatLength, int snap) {
        return beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
    }

    // 计算绿线的速度 (-100/sv)
    public static BigDecimal getSVBeatLength(double currSV) {
        return new BigDecimal(-100).divide(new BigDecimal(currSV), 12, RoundingMode.HALF_UP);
    }
}
