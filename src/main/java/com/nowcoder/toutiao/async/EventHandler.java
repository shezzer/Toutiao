package com.nowcoder.toutiao.async;

import java.util.List;

/**
 * Created by Sherl on 2017/8/1.
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}
