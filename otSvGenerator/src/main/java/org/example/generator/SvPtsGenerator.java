package org.example.generator;

import com.google.common.collect.ImmutableList;
import org.example.ObjectGenerator;
import org.example.entity.TimingPtsDO;
import org.example.parser.TimestampParser;
import org.example.parser.TimingPtsParser;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SvPtsGenerator implements ObjectGenerator {
    @Override
    public List<TimingPtsDO> batchGenerate(Integer start, Integer end,
                                int bpm, int snap, int volume,
                                int beats, boolean useSoft, boolean inKiai) {
        // TODO: 摸了
        return new ArrayList<>();
    }

    public List<TimingPtsDO> batchGenerate(Integer start, Integer end,
                                int bpm, int snap, int volume,
                                int beats, boolean useSoft,
                                double svStart, double step, boolean inKiai) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = new BigDecimal(60000).divide(new BigDecimal(bpm), new MathContext(15, RoundingMode.HALF_EVEN));
        // 区间取整使用HALF_EVEN
        double currSV = svStart;
        double curr = start;
        while (curr <= end) {
            TimingPtsDO timingPtsDO = new TimingPtsDO();
            // 绿线的beatLength为 -(100/sv)
            timingPtsDO.setBeatLength(new BigDecimal(-100).divide(new BigDecimal(currSV), 12, RoundingMode.HALF_UP));
            // 4/4 -> beats=4
            timingPtsDO.setMeter(beats);
            // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
            timingPtsDO.setSampleSet(useSoft ? 2 : 1);
            // default hitsound
            timingPtsDO.setSampleIndex(0);
            timingPtsDO.setVolume(volume);
            timingPtsDO.setInherited(0);
            timingPtsDO.setEffects(inKiai ? 1 : 0);
            // 小数向下取整
            timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.DOWN)).intValue());
            timingPtsDOList.add(timingPtsDO);
            double interval = calculateTimestampFromSnap(beatLength, snap);
            curr += interval;
            currSV += step;
        }
        return timingPtsDOList;
    }

    private double calculateTimestampFromSnap(BigDecimal beatLength, int snap) {
        // 取一位小数, 四舍五入
        BigDecimal result = beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public List<TimingPtsDO> flyingBarlineGenerator(List<Integer> timestamp,
                                int bpm, int snap, int volume,
                                int beats, boolean useSoft,
                                double flySV, double originSV, boolean inKiai) {
        ArrayList<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        for (Integer time : timestamp) {
            List<TimingPtsDO> flyingBarlines = batchGenerate(time, time,
                    bpm, snap, volume, beats, useSoft, flySV, 0, inKiai);
            List<TimingPtsDO> initialBarlines = batchGenerate(time + 1, time + 1,
                    bpm, snap, volume, beats, useSoft, originSV, 0, inKiai);
            timingPtsDOList.addAll(flyingBarlines);
            timingPtsDOList.addAll(initialBarlines);
        }
        return timingPtsDOList;
    }

    public static void main(String[] args) {
        TimingPtsParser parser = new TimingPtsParser();
        SvPtsGenerator svPtsGenerator = new SvPtsGenerator();
        TimingPtsGenerator timingPtsGenerator = new TimingPtsGenerator();
//        TimestampParser timestampParser = new TimestampParser();
        List<Integer> bookMarks = Arrays.stream("4785,8594,16690,20023,23832,91924,92401,107639,108115,108591,109067,110020,110972,111924,112877,113829,114782,149542,153352"
                    .split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        List<TimingPtsDO> total = new ArrayList<>();
        for (Integer bookMark : bookMarks) {
            List<TimingPtsDO> timingPtsDOS = timingPtsGenerator.batchGenerate(bookMark, bookMark, 126,
                    1, 70, 4, true, false);
            total.addAll(timingPtsDOS);
        }
//        List<TimingPtsDO> timingPtsDOS = svPtsGenerator.flyingBarlineGenerator(bookMarks, 126,
//                1, 70, 4, true, 1.5, 0.75, false);

        System.out.println(parser.serialize(total));
    }
}
