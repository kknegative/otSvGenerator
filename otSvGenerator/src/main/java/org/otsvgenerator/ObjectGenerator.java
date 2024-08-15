package org.otsvgenerator;

import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;

import java.util.List;

public interface ObjectGenerator<Q extends BaseBatchGenerateRequest> {
    List<TimingPtsDO> batchGenerate(Q req);
}
