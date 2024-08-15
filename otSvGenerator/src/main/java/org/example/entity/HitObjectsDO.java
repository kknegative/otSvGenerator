package org.example.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HitObjectsDO extends BaseDO {
    private int x;
    private int y;
    private int type;
    // 0 -> don, 2 -> whistle, 8 -> kat, 10 -> whistle + kat
    private int hitSound;
    // seaprated by comma
    private String objectParams;
    // separated by colon
    private String hitSample;
}
