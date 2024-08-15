package org.otsvgenerator.generator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.otsvgenerator.ObjectGenerator;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;
import org.otsvgenerator.entity.request.SvBatchGenerateRequest;
import org.otsvgenerator.parser.TimingPtsParser;


public class SvPtsGenerator implements ObjectGenerator<SvBatchGenerateRequest> {
    @Override
    public List<TimingPtsDO> batchGenerate(SvBatchGenerateRequest svReq) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = new BigDecimal(60000).divide(new BigDecimal(svReq.getBpm()), new MathContext(15, RoundingMode.HALF_EVEN));
        // 区间取整使用HALF_EVEN
        double currSV = svReq.getSvStart();
        double curr = svReq.getStart();
        while (curr <= svReq.getEnd()) {
            TimingPtsDO timingPtsDO = new TimingPtsDO();
            // 绿线的beatLength为 -(100/sv)
            timingPtsDO.setBeatLength(new BigDecimal(-100).divide(new BigDecimal(currSV), 12, RoundingMode.HALF_UP));
            // 4/4 -> beats=4
            timingPtsDO.setMeter(svReq.getEnd());
            // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
            timingPtsDO.setSampleSet(svReq.isUseSoft() ? 2 : 1);
            // default hitsound
            timingPtsDO.setSampleIndex(0);
            timingPtsDO.setVolume(svReq.getVolume());
            timingPtsDO.setInherited(0);
            timingPtsDO.setEffects(svReq.isInKiai() ? 1 : 0);
            // 小数向下取整
            timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.DOWN)).intValue());
            timingPtsDOList.add(timingPtsDO);
            double interval = calculateTimestampFromSnap(beatLength, svReq.getSnap());
            curr += interval;
            currSV += svReq.getStep();
        }
        return timingPtsDOList;
    }

    private double calculateTimestampFromSnap(BigDecimal beatLength, int snap) {
        // 取一位小数, 四舍五入
        BigDecimal result = beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    public List<TimingPtsDO> flyingBarlineGenerator(List<SvBatchGenerateRequest> reqList) {
        ArrayList<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        for (SvBatchGenerateRequest req : reqList) {
            List<TimingPtsDO> flyingBarlines = batchGenerate(req);
            List<TimingPtsDO> initialBarlines = batchGenerate(req);
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
        List<BaseBatchGenerateRequest> reqList = Arrays.stream("4785,8594,16690,20023,23832,91924,92401,107639,108115,108591,109067,110020,110972,111924,112877,113829,114782,149542,153352"
                    .split(","))
                .map(Integer::parseInt)
                .map(ts -> {
                    BaseBatchGenerateRequest req = new BaseBatchGenerateRequest();
                    req.setStart(ts);
                    req.setEnd(ts);
                    req.setBpm(126);
                    req.setSnap(1);
                    req.setVolume(70);
                    req.setBeats(4);
                    req.setUseSoft(true);
                    req.setInKiai(false);
                    return req;
                })
                .collect(Collectors.toList());
        List<TimingPtsDO> total = new ArrayList<>();
        for (BaseBatchGenerateRequest req : reqList) {
            List<TimingPtsDO> timingPtsDOS = timingPtsGenerator.batchGenerate(req);
            total.addAll(timingPtsDOS);
        }

        System.out.println(parser.serialize(total));
    }
}
