package ComputationalGeometry;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * An interface that (partially) reflects the API of {@link processing.core.PGraphics} and {@link processing.core.PApplet}
 * It is used to wrap a PApplet and an optional PGraphics as the render context for the Computational Geometry library
 * in order to delegate all operations to a drawing surface other than the main one.
 * 
 * @author edmundmaruhn
 */
public interface CGRenderContext {

	/**
	 * A reference on the currently running PApplet instance. It's there to still have access to the main render surface
	 * if needed, while it is not guaranteed that methods of this interface will forward rendering commands to that
	 * applet.
	 * 
	 * @return
	 */
	public PApplet getParent();

	public float screenX(float x, float y);

	public float screenX(float x, float y, float z);

	public float screenY(float x, float y);

	public float screenY(float x, float y, float z);

	public float screenZ(float x, float y, float z);

	public void pushMatrix();

	public void popMatrix();

	public void translate(float x, float y);

	public void translate(float x, float y, float z);

	public void beginShape();

	public void beginShape(int kind);

	public void endShape();

	public void endShape(int mode);

	public void point(float x, float y);

	public void point(float x, float y, float z);

	public void line(float x1, float y1, float x2, float y2);

	public void line(float x1, float y1, float z1, float x2, float y2, float z2);

	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3);

	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4);

	public void vertex(float x, float y);

	public void vertex(float x, float y, float z);

	public void vertex(float[] v);

	public void vertex(float x, float y, float u, float v);

	public void vertex(float x, float y, float z, float u, float v);

	public void bezierVertex(float x2, float y2, float x3, float y3, float x4, float y4);

	public void bezierVertex(float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4);

	public void quadraticVertex(float cx, float cy, float x3, float y3);

	public void quadraticVertex(float cx, float cy, float cz, float x3, float y3, float z3);

	public void curveVertex(float x, float y);

	public void curveVertex(float x, float y, float z);

	public void box(float size);

	public void box(float w, float h, float d);

	public PFont createFont(String name, float size);

	public PFont createFont(String name, float size, boolean smooth);

	public PFont createFont(String name, float size, boolean smooth, char[] charset);
	
	
	public void textAlign(int alignX);
	
	public void textAlign(int alignX, int alignY);
	
	public float textAscent();
	
	public float textDescent();
	
	public void textFont(PFont which);
	
	public void textFont(PFont which, float size);
	
	public void textLeading(float leading);
	
	public void textMode(int mode);
	
	public void textSize(float size);
	
	public float textWidth(char c);
	
	public float textWidth(String str);
	
	public float textWidth(char[] chars, int start, int length);
	
	public void text(char c, float x, float y);
	
	public void text(char c, float x, float y, float z);
	
	public void text(String str, float x, float y);
	
	public void text(char[] chars, int start, int stop, float x, float y);
	
	public void text(String str, float x, float y, float z);
	
	public void text(char[] chars, int start, int stop, float x, float y, float z);
	
	public void text(String str, float x1, float y1, float x2, float y2);
	
	public void text(int num, float x, float y);
	
	public void text(int num, float x, float y, float z);
	
	public void text(float num, float x, float y);
	
	public void text(float num, float x, float y, float z);

}
