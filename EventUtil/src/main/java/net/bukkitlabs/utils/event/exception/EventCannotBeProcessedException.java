package net.bukkitlabs.utils.event.exception;

import net.bukkitlabs.utils.event.Event;
import org.jetbrains.annotations.NotNull;

public class EventCannotBeProcessedException extends Exception{
    private final Event event;

    public EventCannotBeProcessedException(@NotNull Event event) {
        super("Event " + event.getEventName() + " can't be processed!");
        this.event = event;
    }

    @NotNull
    public Event getEvent() {
        return event;
    }
}
