

package org.dubhe.data.util;

import lombok.Data;
import org.dubhe.data.domain.entity.DataSequence;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @description 表ID值存储
 * @date 2020-10-16
 */
@Data
public class IdAlloc {

    private Queue<Long> ids;

    private Long unUsed;

    public IdAlloc() {
        ids = new LinkedList<>();
        unUsed = 0L;
    }

    /**
     * 补充ID
     *
     * @param dataSequence
     */
    public void add(DataSequence dataSequence) {
        for (Long i = dataSequence.getStart(); i < dataSequence.getStart() + dataSequence.getStep(); i++) {
            ids.add(i);
            unUsed++;
        }
    }

    public Queue<Long> poll(int number) {
        Queue<Long> result = new LinkedList<>();
        for (int i = 0; i < number; i++) {
            result.add(ids.poll());
            unUsed--;
        }
        return result;
    }

}
