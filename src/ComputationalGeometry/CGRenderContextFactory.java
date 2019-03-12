package ComputationalGeometry;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Factory for creating new {@link CGRenderContext} instances. Since the generator methods are statically available, you
 * do not have to instantiate the factory.
 * 
 * @author edmundmaruhn
 *
 */
public final class CGRenderContextFactory {
	
	/**
	 * Creates a new render context from the given parent. This will result in delegating drawing commands to the main
	 * render surface of the application.
	 * 
	 * @param parent
	 * 
	 * @return
	 */
	public static CGRenderContext newInstance(PApplet parent) {
		return new CGRenderContextImpl(parent);
	}
	
	/**
	 * Creates a new render context from the given parent and sets the actual render target to the given graphics object.
	 * This will result in delegating drawing commands to the specified graphics target instead of the application's main
	 * surface.
	 * 
	 * @param parent
	 * @param target
	 * 
	 * @return
	 */
	public static CGRenderContext newInstance(PApplet parent, PGraphics target) {
		return new CGRenderContextImpl(parent, target);
	}
	
	
	/**
	 * @private
	 * Internal implementation of the {@link CGRenderContext} interface.
	 * 
	 * @author edmundmaruhn
	 */
	private static class CGRenderContextImpl implements CGRenderContext {

		private final PApplet parent;
		private final PGraphics target;
		
		public CGRenderContextImpl(PApplet parent) {
			this(parent, parent.g);
		}
		
		public CGRenderContextImpl(PApplet parent, PGraphics target) {
			this.parent = parent;
			this.target = target;
		}
		
		@Override
		public PApplet getParent() {
			return this.parent;
		}
		
		@Override
		public float screenX(float x, float y) {
			return this.target.screenX(x, y);
		}
		
		@Override
		public float screenX(float x, float y, float z) {
			return this.target.screenX(x, y, z);
		}
		
		@Override
		public float screenY(float x, float y) {
			return this.target.screenY(x, y);
		}
		
		@Override
		public float screenY(float x, float y, float z) {
			return this.target.screenY(x, y, z);
		}
		
		@Override
		public float screenZ(float x, float y, float z) {
			return this.target.screenZ(x, y, z);
		}
		
		@Override
		public void pushMatrix() {
			this.target.pushMatrix();
		}
		
		@Override
		public void popMatrix() {
			this.target.popMatrix();
		}
		
		@Override
		public void translate(float x, float y) {
			this.target.translate(x, y);
		}
		
		@Override
		public void translate(float x, float y, float z) {
			this.target.translate(x, y, z);
		}

		@Override
		public void beginShape() {
			this.target.beginShape();
		}

		@Override
		public void beginShape(int kind) {
			this.target.beginShape(kind);
		}
		
		@Override
		public void endShape() {
			this.target.endShape();
		}
		
		@Override
		public void endShape(int mode) {
			this.target.endShape(mode);
		}

		@Override
		public void point(float x, float y) {
			this.target.point(x, y);
		}

		@Override
		public void point(float x, float y, float z) {
			this.target.point(x, y, z);
		}

		@Override
		public void line(float x1, float y1, float x2, float y2) {
			this.target.line(x1, y1, x2, y2);
		}

		@Override
		public void line(float x1, float y1, float z1, float x2, float y2, float z2) {
			this.target.line(x1, y1, z1, x2, y2, z2);
		}

		@Override
		public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
			this.target.triangle(x1, y1, x2, y2, x3, y3);
		}

		@Override
		public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
			this.target.quad(x1, y1, x2, y2, x3, y3, x4, y4);
		}

		@Override
		public void vertex(float x, float y) {
			this.target.vertex(x, y);
		}

		@Override
		public void vertex(float x, float y, float z) {
			this.target.vertex(x, y, z);
		}

		@Override
		public void vertex(float[] v) {
			this.target.vertex(v);
		}

		@Override
		public void vertex(float x, float y, float u, float v) {
			this.target.vertex(x, y, u, v);
		}

		@Override
		public void vertex(float x, float y, float z, float u, float v) {
			this.target.vertex(x, y, z, u, v);
		}

		@Override
		public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4) {
			this.target.bezierVertex(x2, y2, x3, y3, x4, y4);
		}

		@Override
		public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
			this.target.bezierVertex(x2, y2, z2, x3, y3, z3, x4, y4, z4);
		}

		@Override
		public void quadraticVertex(float cx, float cy, float x3, float y3) {
			this.target.quadraticVertex(cx, cy, x3, y3);
		}

		@Override
		public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3) {
			this.target.quadraticVertex(cx, cy, cz, x3, y3, z3);
		}

		@Override
		public void curveVertex(float x, float y) {
			this.target.curveVertex(x, y);
		}

		@Override
		public void curveVertex(float x, float y, float z) {
			this.target.curveVertex(x, y, z);
		}
		
		@Override
		public void box(float size) {
			this.target.box(size);
		}
		
		@Override
		public void box(float w, float h, float d) {
			this.target.box(w, h, d);
		}
		
		
		@Override
		public PFont createFont(String name, float size) {
			this.saveParentRenderTarget();
			final PFont font = this.parent.createFont(name, size);
			this.restoreParentRenderTarget();
			
			return font;
		}
		
		@Override
		public PFont createFont(String name, float size, boolean smooth) {
			this.saveParentRenderTarget();
			final PFont font = this.parent.createFont(name, size, smooth);
			this.restoreParentRenderTarget();
			
			return font;
		}
		
		@Override
		public PFont createFont(String name, float size, boolean smooth, char[] charset) {
			this.saveParentRenderTarget();
			final PFont font = this.parent.createFont(name, size, smooth, charset);
			this.restoreParentRenderTarget();
			
			return font;
		}

		@Override
		public void textAlign(int alignX) {
			this.target.textAlign(alignX);
		}

		@Override
		public void textAlign(int alignX, int alignY) {
			this.target.textAlign(alignX, alignY);
		}

		@Override
		public float textAscent() {
			return this.target.textAscent();
		}

		@Override
		public float textDescent() {
			return this.target.textDescent();
		}

		@Override
		public void textFont(PFont which) {
			this.target.textFont(which);
		}

		@Override
		public void textFont(PFont which, float size) {
			this.target.textFont(which, size);
		}

		@Override
		public void textLeading(float leading) {
			this.target.textLeading(leading);
		}

		@Override
		public void textMode(int mode) {
			this.target.textMode(mode);
		}

		@Override
		public void textSize(float size) {
			this.target.textSize(size);
		}

		@Override
		public float textWidth(char c) {
			return this.target.textWidth(c);
		}

		@Override
		public float textWidth(String str) {
			return this.target.textWidth(str);
		}

		@Override
		public float textWidth(char[] chars, int start, int length) {
			return this.target.textWidth(chars, start, length);
		}

		@Override
		public void text(char c, float x, float y) {
			this.target.text(c, x, y);
		}

		@Override
		public void text(char c, float x, float y, float z) {
			this.target.text(c, x, y, z);
		}

		@Override
		public void text(String str, float x, float y) {
			this.target.text(str, x, y);
		}

		@Override
		public void text(char[] chars, int start, int stop, float x, float y) {
			this.target.text(chars, start, stop, x, y);
		}

		@Override
		public void text(String str, float x, float y, float z) {
			this.target.text(str, x, y, z);
		}

		@Override
		public void text(char[] chars, int start, int stop, float x, float y, float z) {
			this.target.text(chars, start, stop, x, y, z);
		}

		@Override
		public void text(String str, float x1, float y1, float x2, float y2) {
			this.target.text(str, x1, y1, x2, y2);
		}

		@Override
		public void text(int num, float x, float y) {
			this.target.text(num, x, y);
		}

		@Override
		public void text(int num, float x, float y, float z) {
			this.target.text(num, x, y, z);
		}

		@Override
		public void text(float num, float x, float y) {
			this.target.text(num, x, y);
		}
		
		@Override
		public void text(float num, float x, float y, float z) {
			this.target.text(num, x, y, z);
		}
		
 
		/**
		 * @private
		 * Temporarily stores the render target of the PApplet in order to get Processing internal API features delegated to
		 * the actual render target. This way we do not have to pollute Processing's packages with custom components.
		 */
		private PGraphics parentRenderTarget;
		
		private void saveParentRenderTarget() {
			this.parentRenderTarget = this.parent.g;
		}
		
		private void restoreParentRenderTarget() {
			if (this.parentRenderTarget != null) {
				this.parent.g = this.parentRenderTarget;
				this.parentRenderTarget = null;
			}
		}
	}

}
