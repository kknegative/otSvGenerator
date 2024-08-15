package org.otsvgenerator.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.otsvgenerator.calculator.TimestampCalculator;
import org.otsvgenerator.entity.TimingPtsDO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SvBatchGenerateRequest extends BaseBatchGenerateRequest {
    // 起始sv
    private double svStart;
    // 每条线之间的间距
    private double step;

    public TimingPtsDO convertTimingPtsDO(double currSV, double curr) {
        TimingPtsDO timingPtsDO = new TimingPtsDO();
        timingPtsDO.setBeatLength(TimestampCalculator.getSVBeatLength(currSV));
        // 4/4 -> beats=4
        timingPtsDO.setMeter(getBeats());
        // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
        timingPtsDO.setSampleSet(isUseSoft() ? 2 : 1);
        // default hitsound
        timingPtsDO.setSampleIndex(0);
        timingPtsDO.setVolume(getVolume());
        timingPtsDO.setInherited(0);
        // kiai: 1, nokiai: 0
        timingPtsDO.setEffects(isInKiai() ? 1 : 0);
        timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.HALF_DOWN)).intValue());
        return timingPtsDO;
    }
}
