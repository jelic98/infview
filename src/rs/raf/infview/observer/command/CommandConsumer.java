package rs.raf.infview.observer.command;

import rs.raf.infview.core.App;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class CommandConsumer extends Thread {

    private final BlockingQueue<Command> queue;
    private boolean running;

    CommandConsumer() {
        queue = new ArrayBlockingQueue<>(App.COMMAND_QUEUE_SIZE);
    }

    void consume(Command command, ThreadOptions threadOptions) {
        if(!isRunning()) {
            start();
        }

        if(threadOptions == ThreadOptions.SINGLE_THREAD) {
            executeCommand(command);
            return;
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    if(!isRunning()) {
                        throw new InterruptedException();
                    }

                    queue.put(command);
                }catch(InterruptedException e) {
                    executeCommand(command);
                    interrupt();
                }
            }
        }.start();
    }

    @Override
    public void run() {
        setRunning(true);

        while(isRunning()) {
            try {
                executeCommand(queue.take());
            }catch(InterruptedException e) {
                setRunning(false);
                interrupt();
            }
        }
    }

    private void executeCommand(Command command) {
        if(command.canUndo()) {
            UndoableCommand undoable = (UndoableCommand) command;

            if(undoable.getState() == UndoableCommand.ExecutionState.UNDO) {
                undoable.revert();
                return;
            }
        }

        new Thread() {
            @Override
            public void run() {
                command.execute();
            }
        }.start();
    }

    private boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }
}