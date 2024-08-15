package org.otsvgenerator.generator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.otsvgenerator.ObjectGenerator;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;


public class TimingPtsGenerator implements ObjectGenerator<BaseBatchGenerateRequest> {
    @Override
    public List<TimingPtsDO> batchGenerate(BaseBatchGenerateRequest req) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = new BigDecimal(60000).divide(new BigDecimal(req.getBpm()), new MathContext(15, RoundingMode.HALF_EVEN));
        // 区间取整使用HALF_EVEN
        int count = 0;
        double curr = req.getStart();
        while (curr <= req.getEnd()) {
            // 针对0.5做的处理
            TimingPtsDO timingPtsDO = new TimingPtsDO();
            // duration per beat in ms
            timingPtsDO.setBeatLength(beatLength);
            // 4/4 -> beats=4
            timingPtsDO.setMeter(req.getBeats());
            // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
            timingPtsDO.setSampleSet(req.isUseSoft() ? 2 : 1);
            // default hitsound
            timingPtsDO.setSampleIndex(0);
            timingPtsDO.setVolume(req.getVolume());
            timingPtsDO.setInherited(1);
            // kiai: 0, nokiai: 1
            timingPtsDO.setEffects(0);
            timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.HALF_DOWN)).intValue());
            timingPtsDOList.add(timingPtsDO);
            double interval = calculateTimestampFromSnap(beatLength, req.getSnap(), count);
            curr += interval;
            count++;
        }
        return timingPtsDOList;
    }


    private double calculateTimestampFromSnap(BigDecimal beatLength, int snap, int count) {
        BigDecimal result = beatLength.divide(new BigDecimal(snap), 1, RoundingMode.HALF_UP);
        return result.doubleValue();
    }
}
