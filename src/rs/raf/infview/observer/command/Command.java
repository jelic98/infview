package rs.raf.infview.observer.command;

import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;

public abstract class Command implements ChangeObserver<Object> {

    private String status;
    private boolean skipHistory;

    public Command() {
        skipHistory = false;
    }

    public abstract void execute();

    public String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    public boolean hasStatus() {
        return status != null && !status.isEmpty();
    }

    public boolean isSkipHistory() {
        return skipHistory;
    }

    Command skipHistory() {
        this.skipHistory = true;

        return this;
    }

    Command skipHistory(boolean skipHistory) {
        this.skipHistory = skipHistory;

        return this;
    }

    public boolean changesHistory() {
        return false;
    }

    public boolean canUndo() {
        return false;
    }

    @Override
    public void onChange() {
        CommandQueue.push(this);
    }

    @Override
    public void onChange(ChangeType type) {
        onChange();
    }

    @Override
    public void onChange(ChangeType type, Object bundle) {
        onChange(type);
    }
}