package org.otsvgenerator.entity.request;

import lombok.Data;

@Data
public class BaseBatchGenerateRequest {
    private int start;
    private int end;
    private int bpm;
    private int snap;
    private int volume;
    private int beats;
    private boolean useSoft;
    private boolean inKiai;
}
