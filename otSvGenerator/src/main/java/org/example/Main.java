package org.example;

import org.example.entity.BaseDO;
import org.example.entity.TimingPtsDO;
import org.example.generator.SvPtsGenerator;
import org.example.generator.TimingPtsGenerator;
import org.example.parser.TimestampParser;
import org.example.parser.TimingPtsParser;

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
