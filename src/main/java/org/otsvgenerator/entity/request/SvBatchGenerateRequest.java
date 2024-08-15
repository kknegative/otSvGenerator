package org.otsvgenerator.entity.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SvBatchGenerateRequest extends BaseBatchGenerateRequest {
    // 起始sv
    private double svStart;
    // 每条线之间的间距
    private double step;
}
