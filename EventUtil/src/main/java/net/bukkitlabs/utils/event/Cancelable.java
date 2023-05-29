package net.bukkitlabs.utils.event;

public interface Cancelable {
    boolean isCanceled();

    void setCanceled(final boolean canceled);
}
