package org.otsvgenerator.parser;

import org.otsvgenerator.ObjectParser;
import org.otsvgenerator.entity.BaseDO;
import org.otsvgenerator.entity.HitObjectsDO;
import org.otsvgenerator.entity.TimingPtsDO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HitObjectsParser implements ObjectParser<HitObjectsDO> {

    @Override
    public List<HitObjectsDO> deserialize(String timingPtsStr) {
        String[] timingPts = timingPtsStr.split("\n");
        List<HitObjectsDO> hitObjectsDOList = new ArrayList<>();
        for (String pt : timingPts) {
            String[] timingAttrs = pt.split(",");
            if (timingAttrs.length != 6) {
                // not a hitcircle
                continue;
            }
            HitObjectsDO hitObjectsDO = new HitObjectsDO();
            hitObjectsDO.setX(Integer.parseInt(timingAttrs[0]));
            hitObjectsDO.setY(Integer.parseInt(timingAttrs[1]));
            hitObjectsDO.setTimestamp(Integer.parseInt(timingAttrs[2]));
            hitObjectsDO.setType(Integer.parseInt(timingAttrs[3]));
            hitObjectsDO.setHitSound(Integer.parseInt(timingAttrs[4]));
            // hitclrcles do not have objectParams
            hitObjectsDO.setHitSample(timingAttrs[5]);
            hitObjectsDOList.add(hitObjectsDO);
        }
        return hitObjectsDOList;
    }

    @Override
    public List<HitObjectsDO> moveAllObjectsBy(List<HitObjectsDO> hitObjects, int offset) {
        for (BaseDO pt : hitObjects) {
            pt.setTimestamp(pt.getTimestamp() + offset);
        }
        return hitObjects;
    }

    @Override
    public String serialize (List<HitObjectsDO> hitObjectsDOList) {
        StringBuilder sbd = new StringBuilder();
        for (HitObjectsDO pt : hitObjectsDOList) {
            sbd.append(pt.getX()).append(",");
            sbd.append(pt.getY()).append(",");
            sbd.append(pt.getTimestamp()).append(",");
            sbd.append(pt.getType()).append(",");
            sbd.append(pt.getHitSound()).append(",");
            sbd.append(pt.getHitSample());
            sbd.append("\n");
        }
        return sbd.toString();
    }
}
