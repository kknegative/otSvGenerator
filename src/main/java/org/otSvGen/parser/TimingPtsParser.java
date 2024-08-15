package org.otSvGen.parser;

import org.otSvGen.ObjectParser;
import org.otSvGen.entity.TimingPtsDO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TimingPtsParser implements ObjectParser<TimingPtsDO> {
    @Override
    public List<TimingPtsDO> deserialize(String timingPtsStr) {
        String[] timingPts = timingPtsStr.split("\n");
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        for (String pt : timingPts) {
            String[] timingAttrs = pt.split(",");
            TimingPtsDO timingPtsDO = new TimingPtsDO();
            timingPtsDO.setTimestamp(Integer.parseInt(timingAttrs[0]));
            timingPtsDO.setBeatLength(new BigDecimal(timingAttrs[1]));
            timingPtsDO.setMeter(Integer.parseInt(timingAttrs[2]));
            timingPtsDO.setSampleSet(Integer.parseInt(timingAttrs[3]));
            timingPtsDO.setSampleIndex(Integer.parseInt(timingAttrs[4]));
            timingPtsDO.setVolume(Integer.parseInt(timingAttrs[5]));
            timingPtsDO.setInherited(Integer.parseInt(timingAttrs[6]));
            timingPtsDO.setEffects(Integer.parseInt(timingAttrs[7]));
            timingPtsDOList.add(timingPtsDO);
        }
        return timingPtsDOList;
    }

    @Override
    public String serialize(List<TimingPtsDO> timingPtsDOList) {
        StringBuilder sbd = new StringBuilder();
        for (TimingPtsDO pt : timingPtsDOList) {
            sbd.append(pt.getTimestamp()).append(",");
            sbd.append(pt.getBeatLength()).append(",");
            sbd.append(pt.getMeter()).append(",");
            sbd.append(pt.getSampleSet()).append(",");
            sbd.append(pt.getSampleIndex()).append(",");
            sbd.append(pt.getVolume()).append(",");
            sbd.append(pt.getInherited()).append(",");
            sbd.append(pt.getEffects());
            sbd.append("\n");
        }
        return sbd.toString();
    }

    @Override
    public List<TimingPtsDO> moveAllObjectsBy(List<TimingPtsDO> timingPts, int offset) {
        for (TimingPtsDO pt : timingPts) {
            pt.setTimestamp(pt.getTimestamp() + offset);
        }
        return timingPts;
    }
}