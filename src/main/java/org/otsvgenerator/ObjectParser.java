package org.otsvgenerator;

import org.otsvgenerator.entity.BaseDO;

import java.util.List;

public interface ObjectParser<T extends BaseDO> {
    /**
     * Deserialize content in osz file
     * @param input input as String
     * @return list of objects parsed
     */
    List<T> deserialize(String input);

    /**
     * Serialize objects to be recognizable in osz
     * @param input list of objects generated
     * @return content string
     */
    String serialize(List<T> input);

    /**
     * n
     * @param input
     * @param offset
     * @return
     */
    List<T> moveAllObjectsBy(List<T> input, int offset);
}
