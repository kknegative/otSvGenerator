package org.otsvgenerator;

import org.otsvgenerator.entity.BaseDO;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;
import org.otsvgenerator.entity.request.SvBatchGenerateRequest;
import org.otsvgenerator.generator.SvPtsGenerator;
import org.otsvgenerator.generator.TimingPtsGenerator;
import org.otsvgenerator.parser.TimestampParser;
import org.otsvgenerator.parser.TimingPtsParser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TimingPtsGenerator timingPtsGenerator = new TimingPtsGenerator();
        TimestampParser timestampParser = new TimestampParser();
        int start = timestampParser.convertToMs("01:35:734");
        int end = timestampParser.convertToMs("01:36:448");
        BaseBatchGenerateRequest req = new BaseBatchGenerateRequest();
        req.setStart(start);
        req.setEnd(end);
        req.setBpm(new BigDecimal(126));
        req.setSnap(16);
        req.setVolume(70);
        req.setBeats(4);
        req.setUseSoft(true);
        req.setInKiai(true);
        List<TimingPtsDO> redLines = timingPtsGenerator.batchGenerate(req);

        SvPtsGenerator svPtsGenerator = new SvPtsGenerator();
        SvBatchGenerateRequest svReq = new SvBatchGenerateRequest();
        svReq.setStart(start);
        svReq.setEnd(end);
        svReq.setBpm(new BigDecimal(126));
        svReq.setSnap(16);
        svReq.setVolume(70);
        svReq.setBeats(4);
        svReq.setUseSoft(true);
        svReq.setInKiai(true);
        svReq.setSvStart(1.25);
        svReq.setStep(-0.02);
        List<TimingPtsDO> greenLines = svPtsGenerator.batchGenerate(svReq);

        List<TimingPtsDO> allObjects = new ArrayList<>();
        allObjects.addAll(redLines);
        allObjects.addAll(greenLines);
        allObjects.sort(Comparator.comparing(BaseDO::getTimestamp));

        TimingPtsParser timingPtsParser = new TimingPtsParser();
        String s = timingPtsParser.serialize(allObjects);
        System.out.println(s);
    }
}
