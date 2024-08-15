package org.example.parser;

import org.example.ObjectParser;
import org.example.entity.BaseDO;
import org.example.entity.HitObjectsDO;
import org.example.entity.TimingPtsDO;

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
            HitObjectsDO hitObjectsDO = new HitObjectsDO();
            hitObjectsDO.setX(Integer.parseInt(timingAttrs[0]));
            hitObjectsDO.setY(Integer.parseInt(timingAttrs[1]));
            hitObjectsDO.setTimestamp(Integer.parseInt(timingAttrs[2]));
            hitObjectsDO.setType(Integer.parseInt(timingAttrs[3]));
            hitObjectsDO.setObjectParams(timingAttrs[4]);
            hitObjectsDO.setHitSample(timingAttrs[5]);
            hitObjectsDOList.add(hitObjectsDO);
        }
        return hitObjectsDOList;
    }

    @Override
    public List<HitObjectsDO> moveAllObjectsBy(List<HitObjectsDO> timingPts, int offset) {
        for (BaseDO pt : timingPts) {
            pt.setTimestamp(pt.getTimestamp() + offset);
        }
        return timingPts;
    }

    @Override
    public String serialize (List<HitObjectsDO> timingPtsDOList) {
        StringBuilder sbd = new StringBuilder();
        Field[] fields = TimingPtsDO.class.getDeclaredFields();

        for (BaseDO pt : timingPtsDOList) {
            try {
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = field.get(pt);
                    // 将所有字段用","拼接
                    if (value != null) {
                        sbd.append(value).append(",");
                    }
                }
                sbd.deleteCharAt(sbd.length() - 1);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sbd.append("\n");
        }
        return sbd.toString();
    }
}
