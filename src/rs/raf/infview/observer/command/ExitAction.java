package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.model.Resource;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.dialog.Question;
import rs.raf.infview.view.component.Node;

public class ExitAction extends Command {

    @Override
    public void execute() {
        DialogAdapter.question(new Question(Res.STRINGS.QUESTION_UNSAVED)
                .addOption(Res.STRINGS.OPTION_SAVE_EXIT, new Command() {
                    @Override
                    public void execute() {
                        Node root = Context.instance().tree.getRoot();

                        for(Node child : root.getChildren()) {
                            Resource resource = (Resource) child.getDelegateModel();

                            CommandQueue.push(new SaveAction(resource).skipDialog(), ThreadOptions.SINGLE_THREAD);
                        }

                        Context.instance().fileMatcher.closeAll();

                        System.exit(0);
                    }
                })
                .addOption(Res.STRINGS.OPTION_YES, new Command() {
                    @Override
                    public void execute() {
                        System.exit(0);
                    }
                })
                .addOption(Res.STRINGS.OPTION_NO));
    }
}