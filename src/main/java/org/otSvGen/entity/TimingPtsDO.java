package org.otSvGen.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class TimingPtsDO extends BaseDO {
    private BigDecimal beatLength;
    private int meter;
    private int sampleSet;
    private int sampleIndex;
    private int volume;
    // 1 -> red line, 0 -> green line
    private int inherited;
    private int effects;
}
