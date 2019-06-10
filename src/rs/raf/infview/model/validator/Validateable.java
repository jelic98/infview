package rs.raf.infview.model.validator;

import java.util.Set;

public interface Validateable {

	Validateable getParent();
	boolean hasValidateables();
	Set<? extends Validateable> getValidateables();
	void validate(Validator validator) throws ValidationException;
}