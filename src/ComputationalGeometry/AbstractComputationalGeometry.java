package ComputationalGeometry;

import processing.core.PApplet;

public abstract class AbstractComputationalGeometry implements ComputationalGeometry {

	
	public AbstractComputationalGeometry(PApplet parent) {
		this(CGRenderContextFactory.newInstance(parent, parent.g));
	}
	
	public AbstractComputationalGeometry(CGRenderContext renderContext) {
		this.renderContext = renderContext;
	}
	
	
	protected final CGRenderContext renderContext;
	
	public final CGRenderContext getRenderContext() {
		return this.renderContext;
	}
}
