package org.otsvgenerator;

import org.otsvgenerator.entity.TimingPtsDO;

import java.util.List;

public interface ObjectGenerator {
    List<TimingPtsDO> batchGenerate(Integer start, Integer end,
                                    int bpm, int snap, int volume,
                                    int beats, boolean useSoft, boolean inKiai);
}
