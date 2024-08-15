package org.otSvGen;

import org.otSvGen.entity.BaseDO;
import org.otSvGen.entity.TimingPtsDO;
import org.otSvGen.generator.SvPtsGenerator;
import org.otSvGen.generator.TimingPtsGenerator;
import org.otSvGen.parser.TimestampParser;
import org.otSvGen.parser.TimingPtsParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TimingPtsGenerator timingPtsGenerator = new TimingPtsGenerator();
        TimestampParser timestampParser = new TimestampParser();
        int start = timestampParser.convertToMs("01:35:734");
        int end = timestampParser.convertToMs("01:36:448");
        List<TimingPtsDO> redLines = timingPtsGenerator.batchGenerate(start, end,
                126, 16, 70, 4, true, true);
        SvPtsGenerator svPtsGenerator = new SvPtsGenerator();
        List<TimingPtsDO> greenLines = svPtsGenerator.batchGenerate(start, end,
                126, 16, 70, 4, true, 1.25, -0.02, true);

        TimingPtsParser timingPtsParser = new TimingPtsParser();
        List<TimingPtsDO> allObjects = new ArrayList<>();
        allObjects.addAll(redLines);
        allObjects.addAll(greenLines);
        allObjects.sort(Comparator.comparing(BaseDO::getTimestamp));
        String s = timingPtsParser.serialize(allObjects);
        System.out.println(s);
    }
}
