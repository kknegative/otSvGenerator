package org.otsvgenerator.generator;

import org.otsvgenerator.ObjectGenerator;
import org.otsvgenerator.calculator.TimestampCalculator;
import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TimingPtsGenerator implements ObjectGenerator<BaseBatchGenerateRequest> {
    @Override
    public List<TimingPtsDO> batchGenerate(BaseBatchGenerateRequest req) {
        List<TimingPtsDO> timingPtsDOList = new ArrayList<>();
        // 这里本来就需要取整, 精度不需要太关心
        // beatLength保留16位
        BigDecimal beatLength = TimestampCalculator.getBeatLength(req.getBpm());
        // 区间取整使用HALF_EVEN
        double curr = req.getStart();
        while (curr <= req.getEnd()) {
            TimingPtsDO timingPtsDO = req.convertTimingPtsDO(beatLength, curr);
            timingPtsDOList.add(timingPtsDO);
            BigDecimal interval = TimestampCalculator.getIntervalFromSnap(beatLength, req.getSnap());
            curr += interval.doubleValue();
        }
        return timingPtsDOList;
    }
}
