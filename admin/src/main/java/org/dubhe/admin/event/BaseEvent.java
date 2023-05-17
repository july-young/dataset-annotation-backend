

package org.dubhe.admin.event;

import org.springframework.context.ApplicationEvent;


/**
 * @description 超类 事件
 * @date 2020-06-01
 */
public abstract class BaseEvent<T> extends ApplicationEvent {

    private static final long serialVersionUID = 895628808370649881L;

    protected T eventData;

    public BaseEvent(Object source, T eventData) {
        super(source);
        this.eventData = eventData;
    }

    public BaseEvent(T eventData) {
        super(eventData);
    }

    public T getEventData() {
        return eventData;
    }

    public void setEventData(T eventData) {
        this.eventData = eventData;
    }
}
