package net.bukkitlabs.utils.event;

import net.bukkitlabs.utils.event.exception.EventCannotBeProcessedException;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {

    private final Map<Method, Listener> methodeList = new HashMap<>();

    public void registerListener(@NotNull final Listener listener) {
        for (final Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventCatch.class) ||
                    method.getParameterTypes().length != 1) continue;
            methodeList.put(method, listener);
        }
    }

    public void call(@NotNull final Event event) throws EventCannotBeProcessedException {
        final Method[] methods = this.methodeList.keySet().stream()
                .filter(method -> method.getParameterTypes()[0].equals(event.getClass()))
                .sorted((methode1, methode2) -> methode2.getAnnotation(EventCatch.class).priority().getSlot() -
                        methode1.getAnnotation(EventCatch.class).priority().getSlot())
                .toArray(Method[]::new);
        if (event instanceof Cancelable cancelable) {
            this.cancelableCall(cancelable, methods);
            return;
        }
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(EventCatch.class) ||
                    method.getParameterTypes().length != 1 ||
                    !method.getParameterTypes()[0].equals(event.getClass())) continue;
            try {
                final Listener listener = this.methodeList.get(method);
                if (!method.canAccess(listener)) method.setAccessible(true);
                method.invoke(listener, event);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                throw new EventCannotBeProcessedException(event);
            }
        }
    }

    private void cancelableCall(final @NotNull Cancelable event, final @NotNull Method[] methods) throws EventCannotBeProcessedException {
        boolean canceled = false;
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(EventCatch.class) ||
                    method.getParameterTypes().length != 1 ||
                    !method.getParameterTypes()[0].equals(event.getClass()) ||
                    (canceled && !method.getAnnotation(EventCatch.class).ignoreCancelled())) continue;
            try {
                final Listener listener = this.methodeList.get(method);
                if (!method.canAccess(listener)) method.setAccessible(true);
                method.invoke(listener, event);
                if (event.isCanceled()) canceled = true;
            } catch (IllegalAccessException | InvocationTargetException exception) {
                throw new EventCannotBeProcessedException((Event) event);
            }
        }
    }
}
