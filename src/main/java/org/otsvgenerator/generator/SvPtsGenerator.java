package org.otsvgenerator.generator;

import org.otsvgenerator.ObjectGenerator;
import org.otsvgenerator.calculator.TimestampCalculator;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.SvBatchGenerateRequest;
import org.otsvgenerator.parser.TimingPtsParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SvPtsGenerator implements ObjectGenerator<SvBatchGenerateRequest> {
    @Override
    public List<TimingPtsDO> batchGenerate(SvBatchGenerateRequest svReq) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = TimestampCalculator.getBeatLength(svReq.getBpm());
        // 区间取整使用HALF_EVEN
        double currSV = svReq.getSvStart();
        double curr = svReq.getStart();
        while (curr <= svReq.getEnd()) {
            TimingPtsDO timingPtsDO = svReq.convertTimingPtsDO(currSV, curr);
            timingPtsDOList.add(timingPtsDO);
            BigDecimal interval = TimestampCalculator.getIntervalFromSnap(beatLength, svReq.getSnap());
            // TODO: 可能会有精度问题, 暂时不想动脑了
            curr += interval.doubleValue();
            currSV += svReq.getStep();
        }
        return timingPtsDOList;
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
//        TimestampParser timestampParser = new TimestampParser();
        List<SvBatchGenerateRequest> reqList = Arrays.stream("4785,8594,16690,20023,23832,91924,92401,107639,108115,108591,109067,110020,110972,111924,112877,113829,114782,149542,153352"
                    .split(","))
                .map(Integer::parseInt)
                .map(ts -> {
                    SvBatchGenerateRequest req = new SvBatchGenerateRequest();
                    req.setStart(ts);
                    req.setEnd(ts);
                    req.setBpm(new BigDecimal(126));
                    req.setSnap(1);
                    req.setVolume(70);
                    req.setBeats(4);
                    req.setUseSoft(true);
                    req.setInKiai(false);
                    req.setSvStart(1.25);
                    req.setStep(-0.02);
                    return req;
                })
                .collect(Collectors.toList());
        List<TimingPtsDO> total = new ArrayList<>();
        for (SvBatchGenerateRequest req : reqList) {
            List<TimingPtsDO> timingPtsDOS = svPtsGenerator.batchGenerate(req);
            total.addAll(timingPtsDOS);
        }

        System.out.println(parser.serialize(total));
    }
}
