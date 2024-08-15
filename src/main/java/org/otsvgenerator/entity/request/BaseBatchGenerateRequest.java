package org.otsvgenerator.entity.request;

import lombok.Data;
import org.otsvgenerator.entity.TimingPtsDO;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
public class BaseBatchGenerateRequest {
    private int start;
    private int end;
    private BigDecimal bpm;
    private int snap;
    private int volume;
    private int beats;
    private boolean useSoft;
    private boolean inKiai;

    public TimingPtsDO convertTimingPtsDO(BigDecimal beatLength, double curr) {
        TimingPtsDO timingPtsDO = new TimingPtsDO();
        timingPtsDO.setBeatLength(beatLength);
        // 4/4 -> beats=4
        timingPtsDO.setMeter(beats);
        // 0 = beatmap default, 1 = normal, 2 = soft, 3 = drum
        timingPtsDO.setSampleSet(useSoft ? 2 : 1);
        // default hitsound
        timingPtsDO.setSampleIndex(0);
        timingPtsDO.setVolume(volume);
        timingPtsDO.setInherited(1);
        timingPtsDO.setEffects(0);
        timingPtsDO.setTimestamp(new BigDecimal(curr, new MathContext(0, RoundingMode.HALF_DOWN)).intValue());
        return timingPtsDO;
    }
}
