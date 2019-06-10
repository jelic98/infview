package rs.raf.infview.observer.command;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.Bundle;
import rs.raf.infview.util.io.file.*;
import rs.raf.infview.util.io.orm.component.Connection;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.dialog.Question;
import rs.raf.infview.view.frame.RecordFrame;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public class SearchAction extends Command {

    private Entity entity;
    private ChangeObserver<Entity> callback;

    public SearchAction(Entity entity, ChangeObserver<Entity> callback) {
        this.entity = entity;
        this.callback = callback;
    }

    @Override
    public void execute() {
        new RecordFrame(entity, new Record(), new ChangeObserverAdapter<Record>() {
            @Override
            public void onChange(ChangeType type, Record bundle) {
                if(type != ChangeType.APPLY) {
                    return;
                }

                AbstractResource file = Context.instance().fileMatcher.get(entity);

                if(file != null) {
                    if(!(file instanceof RelationalDatabase)) {
                        Map<String, String> primaries = bundle.getPrimaries();

                        for(String key : primaries.keySet()) {
                            String value = primaries.get(key);

                            if(value == null || value.isEmpty()) {
                                DialogAdapter.error(Res.STRINGS.VALIDATOR_ATTRIBUTE_NO_VALUE);
                                return;
                            }
                        }
                    }

                    entity.getRecords().clear();

                    checkSearchType(file, bundle);
                }
            }
        }).open();
    }

    private void checkSearchType(AbstractResource file, Record record) {
        if(!(file instanceof SequentialFile)) {
            performSearch(file, record);
            return;
        }

        Object[] objOptions = EnumSet.allOf(AbstractResource.SearchType.class).toArray();
        String[] options = new String[objOptions.length];

        for(int i = 0; i < objOptions.length; i++) {
            options[i] = objOptions[i].toString();
        }

        Bundle result = new Bundle(options[0]);

        DialogAdapter.selection(new Question(Res.STRINGS.QUESTION_SEARCH)
                        .addOption(Res.STRINGS.OPTION_OK, new Command() {
                            @Override
                            public void execute() {
                                try {
                                    file.setSearchType(AbstractResource.SearchType.valueOf((String) result.getValue()));
                                }catch(ClassCastException e) {
                                    Log.e(e.getMessage());
                                }

                                performSearch(file, record);
                            }
                        })
                        .addOption(Res.STRINGS.OPTION_CANCEL),
                options, result);
    }

    private void performSearch(AbstractResource file, Record record) {
        if(record == null) {
            return;
        }

        FileOperator operator = new FileOperator(file);

        record.setAttributes(entity.getAttributes());

        List<Record> result = operator.search(record, false);

        if(result == null || result.isEmpty()) {
        	DialogAdapter.info(Res.STRINGS.RECORD_NOT_FOUND);
        	return;
        }

        if(!(file instanceof RelationalDatabase)) {
            DialogAdapter.question(new Question(Res.STRINGS.QUESTION_SINGLE)
                    .addOption(Res.STRINGS.OPTION_YES, new Command() {
                        @Override
                        public void execute() {
                            Record r = result.get(0);
                            result.clear();
                            result.add(r);
                        }
                    })
                    .addOption(Res.STRINGS.OPTION_NO));
        }

        if(file instanceof SequentialFile)
        	DialogAdapter.question(new Question(Res.STRINGS.QUESTION_FILE)
                    .addOption(Res.STRINGS.OPTION_YES, new Command() {
                        @Override
                        public void execute() {
                            SerialFile resultFile = new SerialFile();
                            String path = ((AbstractFile) file).getPath();
                            int index = path.indexOf('.');
                            path = path.substring(0, index - 1) + "-SearchResult" + path.substring(index);
                            try {
								resultFile.open(path + "", true, entity.getAttributes());
								for(Record r : result) {
						            if(r == null) {
						                continue;
						            }

						            resultFile.create(r);
						        }
							} catch (IOException e) {
								
							}finally {
								resultFile.close();
							}
                        }
                    })
                    .addOption(Res.STRINGS.OPTION_NO));
        
        for(Record r : result) {
            if(r == null) {
                continue;
            }

            r.setParent(entity);
            entity.addRecord(r);
        }

        callback.onChange(ChangeType.REFRESH, entity);
    }
}