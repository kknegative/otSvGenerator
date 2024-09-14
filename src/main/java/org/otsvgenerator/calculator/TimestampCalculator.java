package org.otsvgenerator.calculator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class TimestampCalculator {
    /**
     * 60000ms (1 min) / bpm to calculate the time elapsed between two bar lines
     * @param bpm bpm of song
     * @return time elapsed between two bar lines
     */
    public static BigDecimal getBeatLength(BigDecimal bpm) {
        return new BigDecimal(60000).divide(bpm, new MathContext(15, RoundingMode.HALF_EVEN));
    }

    /**
     * calculate the interval between two snapped objects
     * @param beatLength time elapsed between two bar lines
     * @param snap snap in editor (e.g 8 for 1/8)
     * @return time elapsed between two snapped objects
     */
    public static BigDecimal getIntervalFromSnap(BigDecimal beatLength, int snap) {
        return beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
    }

    /**
     * calculate slider velocity (-100/sv value in editor)
     * @param currSV desired sv value in editor
     * @return corresponding sv value in .osu
     */
    public static BigDecimal getSVBeatLength(double currSV) {
        return new BigDecimal(-100).divide(new BigDecimal(currSV), 12, RoundingMode.HALF_UP);
    }
}
