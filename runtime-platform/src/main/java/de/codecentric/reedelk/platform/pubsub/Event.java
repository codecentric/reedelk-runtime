package de.codecentric.reedelk.platform.pubsub;

import de.codecentric.reedelk.platform.commons.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static de.codecentric.reedelk.platform.pubsub.Action.Module.UN_INSTALLED;

public class Event {

    private static final Logger logger = LoggerFactory.getLogger(Event.class);

    public static final Event operation;
    static {
        channels = new ConcurrentHashMap<>();
        operation = new Event();
    }

    private Event() {
    }

    private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, WeakReference<Object>>> channels;

    public static void fireModuleUninstalled(long moduleId) {
        Action.Module.ActionModuleUninstalled message = new Action.Module.ActionModuleUninstalled(moduleId);
        Event.operation.publish(UN_INSTALLED, message);
    }

    public void subscribe(String channelName, Object subscriber) {
        if (!channels.containsKey(channelName)) {
            channels.put(channelName, new ConcurrentHashMap<>());
        }

        channels.get(channelName).put(subscriber.hashCode(), new WeakReference<>(subscriber));
    }

    private void publish(String channelName, Post message) {
        for(Map.Entry<Integer, WeakReference<Object>> subs : channels.get(channelName).entrySet()) {
            WeakReference<Object> subscriberRef = subs.getValue();
            Optional.ofNullable(subscriberRef.get()).ifPresent(subscriberObj -> {
                for (final Method method : subscriberObj.getClass().getDeclaredMethods()) {
                    Annotation annotation = method.getAnnotation(OnMessage.class);
                    if (annotation != null) {
                        deliverMessage(subscriberObj, method, message);
                    }
                }
            });
        }
    }

    private <T, P extends Post> void deliverMessage(T subscriber, Method method, P message) {
        try {
            boolean methodFound = false;
            for (final Class paramClass : method.getParameterTypes()) {
                if (paramClass.equals(message.getClass())) {
                    methodFound = true;
                    break;
                }
            }
            if (methodFound) {
                method.setAccessible(true);
                method.invoke(subscriber, message);
            }
        } catch (Exception e) {
            logger.error(Messages.PubSub.ERROR_DELIVERING_MESSAGE.format(), e);
        }
    }
}
