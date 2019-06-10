package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.util.Condition;
import rs.raf.infview.view.component.Frame;

public abstract class FrameTemplate {

    public enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    Frame frame;
    private String title;
    private Condition openCondition;
    private Orientation orientation = Orientation.HORIZONTAL;
    private int width, height;
    private boolean lazy;

    FrameTemplate(String title) {
        this(title, false);
    }

	FrameTemplate(String title, boolean lazy) {
        this.title = title;
        this.lazy = lazy;

        openCondition = new Condition(false);

        if(!lazy) {
            setup();
        }
	}

	private void setup() {
        frame = App.UI.getFrame();
        frame.setTitle(title);
        frame.setIcon(Res.ICONS.APP);
        frame.setResizable(false);

        width = frame.getInitialWidth();
        height = frame.getInitialHeight();

        configure();
        populate();
    }
	
	protected abstract void configure();
	protected abstract void populate();

    final void setOrientation(Orientation orientation) {
        this.orientation = orientation;

        checkOrientation();
    }

	final void setSizeRatio(float ratio) {
        width *= ratio;
        height *= ratio;

        checkOrientation();
	}

    private void checkOrientation() {
        if(shouldResize()) {
            width = width + height;
            height = width - height;
            width = width - height;
        }

        frame.setSize(width, height);
    }

    private boolean shouldResize() {
        boolean horizontal = orientation == Orientation.HORIZONTAL && width < height;
        boolean vertical = orientation == Orientation.VERTICAL && width > height;

        return horizontal || vertical;
    }

    public void open() {
        if(!shoudldOpen()) {
            close();
            return;
        }

        if(lazy) {
            setup();
        }

        frame.setVisible(true);
        frame.setFocused(true);

        openCondition.setValue(true);
    }

    public void close() {
        frame.setVisible(false);
        frame.setFocused(false);
        frame.close();

        openCondition.setValue(false);
    }

    private boolean shoudldOpen() {
        return openCondition.isFalse();
    }
}