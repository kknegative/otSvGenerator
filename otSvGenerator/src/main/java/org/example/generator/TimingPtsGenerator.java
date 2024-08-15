package org.example.generator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.example.ObjectGenerator;
import org.example.entity.TimingPtsDO;
import org.example.parser.TimingPtsParser;


public class TimingPtsGenerator implements ObjectGenerator {
    @Override
    public List<TimingPtsDO> batchGenerate(Integer start, Integer end,
                                int bpm, int snap, int volume,
                                int beats, boolean useSoft, boolean inKiai) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = new BigDecimal(60000).divide(new BigDecimal(bpm), new MathContext(15, RoundingMode.HALF_EVEN));
        // 区间取整使用HALF_EVEN
        int count = 0;
        double curr = start;
        while (curr <= end) {
            // 针对0.5做的处理
            TimingPtsDO timingPtsDO = new TimingPtsDO();
            // duration per beat in ms
            timingPtsDO.setBeatLength(beatLength);
            // 4/4 -> beats=4
            timingPtsDO.setMeter(beats);
            // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
            timingPtsDO.setSampleSet(useSoft ? 2 : 1);
            // default hitsound
            timingPtsDO.setSampleIndex(0);
            timingPtsDO.setVolume(volume);
            timingPtsDO.setInherited(1);
            // kiai: 0, nokiai: 1
            timingPtsDO.setEffects(0);
            timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.HALF_DOWN)).intValue());
            timingPtsDOList.add(timingPtsDO);
            double interval = calculateTimestampFromSnap(beatLength, snap, count);
            curr += interval;
            count++;
        }
        return timingPtsDOList;
    }


    private double calculateTimestampFromSnap(BigDecimal beatLength, int snap, int count) {
        BigDecimal result = beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
        return result.doubleValue();
    }
}
