package org.otsvgenerator;

import org.otsvgenerator.entity.TimingPtsDO;
import org.otsvgenerator.entity.request.BaseBatchGenerateRequest;

import java.util.List;

public interface ObjectGenerator<Q extends BaseBatchGenerateRequest> {
    /**
     * batch generate objects based on request
     * @param req request
     * @return list of objects generated
     */
    List<TimingPtsDO> batchGenerate(Q req);
}
