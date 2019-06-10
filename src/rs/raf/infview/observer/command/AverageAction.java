package rs.raf.infview.observer.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.Resource;
import rs.raf.infview.model.type.ResourceType;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.util.io.file.RecordAverager;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.frame.QueryRecordFrame;
import rs.raf.infview.view.frame.ResultFrame;
import rs.raf.infview.view.frame.SortRecordFrame;

public class AverageAction extends Command{
	private Entity entity;

    public AverageAction(Entity entity) {
    	this.entity = entity;
    }

    @Override
    public void execute() {
    	if(((Resource) entity.getParent()).getType() != ResourceType.RELATIONAL_DATABASE) {
    		DialogAdapter.error(Res.STRINGS.ERROR_NOT_DATABASE);
    		return;
    	}
    	
    	Map<Integer, Attribute> map = new HashMap<>();
    	
    	new QueryRecordFrame(entity, new Record(), new ChangeObserverAdapter<Record>() {
            @Override
            public void onChange(ChangeType type, Record bundle) {
                if(type != ChangeType.APPLY) {
                    return;
                }
                map.putAll(QueryRecordFrame.avgInfo);

                Entity clone = (Entity) entity.getClone();
                clone.getAttributes().clear();
                Attribute attr = new Attribute("AVERAGE");
                attr.setAncestor(clone);
                clone.getAttributes().add(attr);
                for(Integer key : map.keySet()) {
                	if(key == 0)
                		continue;
                	Attribute attr1 = new Attribute(map.get(key).getName());
                	attr1.setAncestor(clone);
                	clone.getAttributes().add(attr1);
                }

                new ResultFrame(clone, ((RecordAverager) Context.instance()
            			.fileMatcher.get(entity))
            			.average(map)).open();
            }
        }).open();	
    }
}
