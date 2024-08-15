package org.otsvgenerator;

import org.otsvgenerator.entity.BaseDO;

import java.util.List;

public interface ObjectParser<T extends BaseDO> {
    List<T> deserialize(String input);
    String serialize(List<T> input);
    List<T> moveAllObjectsBy(List<T> input, int offset);
}
