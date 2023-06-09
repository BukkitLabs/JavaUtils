package net.bukkitlabs.utils.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventCatch {

    Priority priority() default Priority.NORMAL;

    boolean ignoreCancelled() default false;

    enum Priority {
        LOWEST(0),
        LOW(1),
        NORMAL(2),
        HIGH(3),
        HIGHEST(4),
        MONITOR(5);

        private final int slot;

        Priority(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return this.slot;
        }
    }
}
