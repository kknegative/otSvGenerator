package org.otsvgenerator.generator;

import org.otsvgenerator.ObjectGenerator;
import org.otsvgenerator.calculator.TimestampCalculator;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.SvBatchGenerateRequest;
import org.otsvgenerator.parser.TimingPtsParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class SvPtsGenerator implements ObjectGenerator<SvBatchGenerateRequest> {
    @Override
    public List<TimingPtsDO> batchGenerate(SvBatchGenerateRequest svReq) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // beatLength has precision of 16 digits, rounded using HALF_EVEN
        BigDecimal beatLength = TimestampCalculator.getBeatLength(svReq.getBpm());
        double currSV = svReq.getSvStart();
        double curr = svReq.getStart();
        while (curr <= svReq.getEnd()) {
            TimingPtsDO timingPtsDO = svReq.convertTimingPtsDO(currSV, curr);
            timingPtsDOList.add(timingPtsDO);
            BigDecimal interval = TimestampCalculator.getIntervalFromSnap(beatLength, svReq.getSnap());
            // TODO: to confirm if there would be precision issues
            curr += interval.doubleValue();
            currSV += svReq.getStep();
        }
        return timingPtsDOList;
    }

    public List<TimingPtsDO> generateFlyingTimingPts(List<SvBatchGenerateRequest> reqList,
                                                   double flyingSV, double initialSV,
                                                   int flyingOffset, int initialOffset) {
        ArrayList<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        for (SvBatchGenerateRequest req : reqList) {
            SvBatchGenerateRequest flyingReq = req.clone();
            flyingReq.setSvStart(flyingSV);
            flyingReq.setStart(req.getStart() + flyingOffset);
            flyingReq.setEnd(req.getStart() + flyingOffset);
            SvBatchGenerateRequest initialReq = req.clone();
            initialReq.setSvStart(initialSV);
            initialReq.setStart(req.getStart() + initialOffset);
            initialReq.setEnd(req.getStart() + initialOffset);
            List<TimingPtsDO> flyingBarlines = batchGenerate(flyingReq);
            List<TimingPtsDO> initialBarlines = batchGenerate(initialReq);
            timingPtsDOList.addAll(flyingBarlines);
            timingPtsDOList.addAll(initialBarlines);
        }
        timingPtsDOList.sort(Comparator.comparing(TimingPtsDO::getTimestamp));
        return timingPtsDOList;
    }

    public static void main(String[] args) {
        TimingPtsParser parser = new TimingPtsParser();
        SvPtsGenerator svPtsGenerator = new SvPtsGenerator();
//        TimestampParser timestampParser = new TimestampParser();
        List<SvBatchGenerateRequest> reqList = Arrays.stream("39030,40230,41430,42630,43830,45030,46230,47430,48630,49830,51030,52230,53430,54630,55830,57030,58230,59430,60630,61830,63030,64230,65430,66630,67830,69030,70230,71430,72630,73830,75030,76230,77430,78630,79830,81030,82230,83430,84630,85830,87030,88230,89430,90630,91830,93030,94230,95430,96630,97830,99030,100230,101430,102630,103830,105030,106230,107430,108630,109830,111030,112230,113430,114630,153630,163230"
                    .split(","))
                .map(Integer::parseInt)
                .map(ts -> {
                    SvBatchGenerateRequest req = new SvBatchGenerateRequest();
                    req.setStart(ts);
                    req.setEnd(ts);
                    req.setBpm(new BigDecimal(200));
                    req.setSnap(1);
                    req.setVolume(85);
                    req.setBeats(4);
                    req.setUseSoft(false);
                    req.setInKiai(false);
                    req.setSvStart(1.3);
                    req.setStep(0);
                    return req;
                })
                .collect(Collectors.toList());
        List<TimingPtsDO> total = svPtsGenerator.generateFlyingTimingPts(reqList, 1.5, 0.75, -3,3);

        System.out.println(parser.serialize(total));
    }
}
