package rs.raf.infview.model;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import java.util.Set;

public class Root extends AbstractModel {

    public Root() {
        this(null);
    }

    public Root(AbstractModel model) {
        super(Res.STRINGS.INFO_ADD_NODE, null);

        if(model != null) {
            addChild(model);
        }
    }

    @Override
    public AbstractModel getClone() {
        return null;
    }

    @Override
    public AbstractModel getChild(String name) {
        return new Resource(name);
    }

    @Override
    public String getChildType() {
        return Resource.class.getSimpleName();
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public boolean hasValidateables() {
        return false;
    }

    @Override
    public Set<? extends Validateable> getValidateables() {
        return null;
    }

    @Override
    public void validate(Validator validator) throws ValidationException {
        throw new ValidationException(Res.STRINGS.ERROR_CANNOT_VALIDATE_ROOT);
    }

    @Override
    public String getUid() {
        return toString();
    }
}