package rs.raf.infview.model.options;

public class NoResourceOptions implements ResourceOptions{

	@Override
	public String getUid() {
		return toString();
	}

	@Override
	public ResourceOptions getClone() {
		return new NoResourceOptions();
	}

	@Override
	public String getExtension() {
		return null;
	}

	@Override
	public void setExtension(String extension) {

	}
}
