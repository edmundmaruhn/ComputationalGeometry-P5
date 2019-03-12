/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package ComputationalGeometry;

import processing.core.*;

public class IsoSurface extends AbstractComputationalGeometry implements PConstants {
  
	PVector start, end;
	int detailx, detaily, detailz;
	public PVector[] vertices;
	public float[] values;
	float xSize, ySize, zSize;
	PFont font;
	PVector[] cubeVertexes;
	float[] cubeValues;
	
	public IsoSurface(PApplet _theParent, PVector _start, PVector _end, int _detail ){
		this(_theParent, _start, _end, _detail, _detail, _detail);
	}
  
  	public IsoSurface(PApplet _theParent, PVector _start, PVector _end, int _detailx, int _detaily, int _detailz ){
  		super(_theParent);
  		
  		start = _start;
	    end = _end;
	    detailx = _detailx;
	    detaily = _detaily;
	    detailz = _detailz;
	    this.reset();
  	}
  	
  	public IsoSurface(CGRenderContext renderContext, PVector _start, PVector _end, int _detail) {
  		this(renderContext, _start, _end, _detail, _detail, _detail);
  	}
  	
  	public IsoSurface(CGRenderContext renderContext, PVector _start, PVector _end, int _detailx, int _detaily, int _detailz ){
  		super(renderContext);
  		
	    start = _start;
	    end = _end;
	    detailx = _detailx;
	    detaily = _detaily;
	    detailz = _detailz;
	    this.reset();
  	}
	
  	public void reset(){
    
	    font = this.renderContext.createFont("Arial", 10.0f);
	    
	    cubeVertexes = new PVector[8];
		cubeValues = new float[8];
		
	    xSize = (end.x - start.x)/(detailx);
	    ySize = (end.y - start.y)/(detaily);
	    zSize = (end.z - start.z)/(detailz);   
	    
	    detailx++; detaily++; detailz++;
	    vertices = new PVector[PApplet.floor( detailx * detaily * detailz )];
	    values = new float[ vertices.length];
	    for(int i=0; i<detailx; i++){
	      for(int j=0; j<detaily; j++){
	        for(int k=0; k<detailz; k++){
	          int index = i*detaily*detailz + j*detailz + k;
	          vertices[index] = new PVector( start.x + i*xSize, start.y + j*ySize, start.z + k*zSize);
	          values[index] = 0.0f;
	        }
	      }
	    }      
  	}

public void setValues( float[] a){
	if( a.length == values.length ) {
	  for(int i=0;i<values.length; i++){
	     values[i] = a[i];
	   }
	} else {
		PApplet.print("Incorrect values variable. Must match Isosurface grid.");
	}	
}

  public void clear(){
	  for(int i=0;i<vertices.length; i++){
	      values[i] = 0.0f;
	  }
  }

  public void addPoint(PVector _pt, float weight){
	    // for each vertex, figure _pt's contribution
	    for (int i=0; i<vertices.length; i++){
	      float d = PApplet.max(1.0f,_pt.dist(vertices[i]));
	      values[i] += (1.0f /  PApplet.pow(d,2))*weight;
	    }
	  }
	  
  
  public void addPoint(PVector _pt){
	  this.addPoint(_pt, 1.0f);
  }
  
  public void plotVoxels(){
	  for(int i=0;i<vertices.length; i++){
		  int x = i / (detailz*detaily);
		  int y = i % (detailz*detaily) / detailz;
		  int z = i % detailz;
	    	
		  if(x < detailx - 1 && y < detaily - 1 && z < detailz -1 ){
			int other = i + detailz*detaily + detailz + 1;
			PVector center = PVector.add( vertices[i], vertices[other]);
		    center.mult(.5f);
		    this.renderContext.pushMatrix();
		    this.renderContext.translate(center.x, center.y, center.z);
		    this.renderContext.box( vertices[other].x - vertices[i].x,  vertices[other].y - vertices[i].y, vertices[other].z - vertices[i].z);
		    this.renderContext.popMatrix();
		 }
	  }
  }

  public void plotCases(float threshold){
	
	  for(int i=0; i<vertices.length; i++){
	    	
		  int x = i / (detailz*detaily);
		  int y = i % (detailz*detaily) / detailz;
		  int z = i % detailz;
	    	
	    	if(x < detailx - 1 && y < detaily - 1 && z < detailz -1 ){
	    	
		    	cubeValues[0] = values[i];
		    	cubeValues[1] = values[i + detaily*detailz];
		    	cubeValues[2] = values[i + detaily*detailz + 1];
		    	cubeValues[3] = values[i + 1];
		    	cubeValues[4] = values[i + detailz];
		    	cubeValues[5] = values[i + detaily*detailz + detailz];
		    	cubeValues[6] = values[i + detaily*detailz + detailz + 1];
		    	cubeValues[7] = values[i + detailz + 1];
		    	
		    	PVector corner = vertices[i];
		    			    	
		    	String myString = "";
			    for(int j=0; j<8; j++){
			      if(cubeValues[j] > threshold){
			        myString = "1" + myString; 
			      } 
			      else{
			        myString = "0" + myString; 
			      }
			    }
			    int lookUp = PApplet.unbinary(myString);
			    float sx = this.renderContext.screenX(corner.x + xSize/2.0f, corner.y+ ySize/2.0f, corner.z + zSize/2.0f );
			    float sy = this.renderContext.screenY(corner.x + xSize/2.0f, corner.y+ ySize/2.0f, corner.z + zSize/2.0f );
			    this.renderContext.textMode(SCREEN);
			    this.renderContext.textAlign(CENTER, CENTER);
			    this.renderContext.textFont(font);
			    this.renderContext.text(lookUp, sx, sy);
	    	}
	    }
	  
  }
  
  public void plot(float threshold){
	  
	
    for(int i=0; i<vertices.length; i++){
    	
    	int x = i / (detailz*detaily);
		int y = i % (detailz*detaily) / detailz;
		int z = i % detailz;
    	
    	if(x < detailx - 1 && y < detaily - 1 && z < detailz -1 ){
    	
    		cubeValues[0] = values[i];
	    	cubeValues[1] = values[i + detaily*detailz];
	    	cubeValues[2] = values[i + detaily*detailz + 1];
	    	cubeValues[3] = values[i + 1];
	    	cubeValues[4] = values[i + detailz];
	    	cubeValues[5] = values[i + detaily*detailz + detailz];
	    	cubeValues[6] = values[i + detaily*detailz + detailz + 1];
	    	cubeValues[7] = values[i + detailz + 1];
	    	
	    	cubeVertexes[0] = vertices[i];
	    	cubeVertexes[1] = vertices[i + detaily*detailz];
	    	cubeVertexes[2] = vertices[i + detaily*detailz + 1];
	    	cubeVertexes[3] = vertices[i + 1];
	    	cubeVertexes[4] = vertices[i + detailz];
	    	cubeVertexes[5] = vertices[i + detaily*detailz + detailz];
	    	cubeVertexes[6] = vertices[i + detaily*detailz + detailz + 1];
	    	cubeVertexes[7] = vertices[i + detailz + 1];
	    	
	    	this.lookUp(threshold, cubeValues, cubeVertexes);
    	}
    }
  } 
  
  public void lookUp(float threshold, float[] myValues, PVector[] vertexes){
	    String myString = "";
	    for(int i=0; i<8; i++){
	      if(myValues[i] > threshold){
	        myString = "1" + myString; 
	      } 
	      else{
	        myString = "0" + myString; 
	      }
	    }
	    int lookUp = PApplet.unbinary(myString);
	    int[] triangles = new int[15];
	    for(int j=0;j<15;j++){
	      triangles[j]=faces[lookUp*15+j];
	    }

	    for(int i=0;i<5;i++){
	      if(triangles[i*3] != -1){
	      	this.renderContext.beginShape(TRIANGLES);
	        for(int v=0;v<3;v++){
	          int num1 = edgeToVertexA[triangles[i*3+v]];
	          int num2 = edgeToVertexB[triangles[i*3+v]];
	          float vtx1P, vtx2P;
	          float spread = PApplet.abs(myValues[num1]-myValues[num2]);
	          if (myValues[num1] < myValues[num2]){
	            vtx1P = 1- PApplet.abs((threshold-myValues[num1])/spread);
	            vtx2P = 1- PApplet.abs((myValues[num2]-threshold)/spread);
	          }
	          else{
	            vtx1P = 1- PApplet.abs((myValues[num1]-threshold)/spread);
	            vtx2P = 1- PApplet.abs((threshold-myValues[num2])/spread);
	          }
	          //vtx1P = vtx2P = .5f;
	          this.renderContext.vertex(vertexes[num1].x*vtx1P+vertexes[num2].x*vtx2P,vertexes[num1].y*vtx1P+vertexes[num2].y*vtx2P,vertexes[num1].z*vtx1P+vertexes[num2].z*vtx2P);
	        }
	        this.renderContext.endShape();
	      }
	    }
	  }

  static int edgeToVertexA[] = {
    0,1,2,3,
    4,5,6,7,
    0,1,3,2  };
  static int edgeToVertexB[] = {
    1,2,3,0,
    5,6,7,4,
    4,5,7,6  };  

  static int faces[] =    {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 1, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 8, 3, 9, 8, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 2, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 8, 3, 1, 2, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 2, 11, 0, 2, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    2, 8, 3, 2, 11, 8, 11, 9, 8, -1, -1, -1, -1, -1, -1,
    3, 10, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 10, 2, 8, 10, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 9, 0, 2, 3, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 10, 2, 1, 9, 10, 9, 8, 10, -1, -1, -1, -1, -1, -1,
    3, 11, 1, 10, 11, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 11, 1, 0, 8, 11, 8, 10, 11, -1, -1, -1, -1, -1, -1,
    3, 9, 0, 3, 10, 9, 10, 11, 9, -1, -1, -1, -1, -1, -1,
    9, 8, 11, 11, 8, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 3, 0, 7, 3, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 1, 9, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 1, 9, 4, 7, 1, 7, 3, 1, -1, -1, -1, -1, -1, -1,
    1, 2, 11, 8, 4, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 4, 7, 3, 0, 4, 1, 2, 11, -1, -1, -1, -1, -1, -1,
    9, 2, 11, 9, 0, 2, 8, 4, 7, -1, -1, -1, -1, -1, -1,
    2, 11, 9, 2, 9, 7, 2, 7, 3, 7, 9, 4, -1, -1, -1,
    8, 4, 7, 3, 10, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    10, 4, 7, 10, 2, 4, 2, 0, 4, -1, -1, -1, -1, -1, -1,
    9, 0, 1, 8, 4, 7, 2, 3, 10, -1, -1, -1, -1, -1, -1,
    4, 7, 10, 9, 4, 10, 9, 10, 2, 9, 2, 1, -1, -1, -1,
    3, 11, 1, 3, 10, 11, 7, 8, 4, -1, -1, -1, -1, -1, -1,
    1, 10, 11, 1, 4, 10, 1, 0, 4, 7, 10, 4, -1, -1, -1,
    4, 7, 8, 9, 0, 10, 9, 10, 11, 10, 0, 3, -1, -1, -1,
    4, 7, 10, 4, 10, 9, 9, 10, 11, -1, -1, -1, -1, -1, -1,
    9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 5, 4, 0, 8, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 5, 4, 1, 5, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    8, 5, 4, 8, 3, 5, 3, 1, 5, -1, -1, -1, -1, -1, -1,
    1, 2, 11, 9, 5, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 0, 8, 1, 2, 11, 4, 9, 5, -1, -1, -1, -1, -1, -1,
    5, 2, 11, 5, 4, 2, 4, 0, 2, -1, -1, -1, -1, -1, -1,
    2, 11, 5, 3, 2, 5, 3, 5, 4, 3, 4, 8, -1, -1, -1,
    9, 5, 4, 2, 3, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 10, 2, 0, 8, 10, 4, 9, 5, -1, -1, -1, -1, -1, -1,
    0, 5, 4, 0, 1, 5, 2, 3, 10, -1, -1, -1, -1, -1, -1,
    2, 1, 5, 2, 5, 8, 2, 8, 10, 4, 8, 5, -1, -1, -1,
    11, 3, 10, 11, 1, 3, 9, 5, 4, -1, -1, -1, -1, -1, -1,
    4, 9, 5, 0, 8, 1, 8, 11, 1, 8, 10, 11, -1, -1, -1,
    5, 4, 0, 5, 0, 10, 5, 10, 11, 10, 0, 3, -1, -1, -1,
    5, 4, 8, 5, 8, 11, 11, 8, 10, -1, -1, -1, -1, -1, -1,
    9, 7, 8, 5, 7, 9, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 3, 0, 9, 5, 3, 5, 7, 3, -1, -1, -1, -1, -1, -1,
    0, 7, 8, 0, 1, 7, 1, 5, 7, -1, -1, -1, -1, -1, -1,
    1, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 7, 8, 9, 5, 7, 11, 1, 2, -1, -1, -1, -1, -1, -1,
    11, 1, 2, 9, 5, 0, 5, 3, 0, 5, 7, 3, -1, -1, -1,
    8, 0, 2, 8, 2, 5, 8, 5, 7, 11, 5, 2, -1, -1, -1,
    2, 11, 5, 2, 5, 3, 3, 5, 7, -1, -1, -1, -1, -1, -1,
    7, 9, 5, 7, 8, 9, 3, 10, 2, -1, -1, -1, -1, -1, -1,
    9, 5, 7, 9, 7, 2, 9, 2, 0, 2, 7, 10, -1, -1, -1,
    2, 3, 10, 0, 1, 8, 1, 7, 8, 1, 5, 7, -1, -1, -1,
    10, 2, 1, 10, 1, 7, 7, 1, 5, -1, -1, -1, -1, -1, -1,
    9, 5, 8, 8, 5, 7, 11, 1, 3, 11, 3, 10, -1, -1, -1,
    5, 7, 10, 5, 10, 11, 1, 0, 9, -1, -1, -1, -1, -1, -1,
    10, 11, 5, 10, 5, 7, 8, 0, 3, -1, -1, -1, -1, -1, -1,
    10, 11, 5, 7, 10, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    11, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 8, 3, 5, 11, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 0, 1, 5, 11, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 8, 3, 1, 9, 8, 5, 11, 6, -1, -1, -1, -1, -1, -1,
    1, 6, 5, 2, 6, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 6, 5, 1, 2, 6, 3, 0, 8, -1, -1, -1, -1, -1, -1,
    9, 6, 5, 9, 0, 6, 0, 2, 6, -1, -1, -1, -1, -1, -1,
    5, 9, 8, 5, 8, 2, 5, 2, 6, 3, 2, 8, -1, -1, -1,
    2, 3, 10, 11, 6, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    10, 0, 8, 10, 2, 0, 11, 6, 5, -1, -1, -1, -1, -1, -1,
    0, 1, 9, 2, 3, 10, 5, 11, 6, -1, -1, -1, -1, -1, -1,
    5, 11, 6, 1, 9, 2, 9, 10, 2, 9, 8, 10, -1, -1, -1,
    6, 3, 10, 6, 5, 3, 5, 1, 3, -1, -1, -1, -1, -1, -1,
    0, 8, 10, 0, 10, 5, 0, 5, 1, 5, 10, 6, -1, -1, -1,
    3, 10, 6, 0, 3, 6, 0, 6, 5, 0, 5, 9, -1, -1, -1,
    6, 5, 9, 6, 9, 10, 10, 9, 8, -1, -1, -1, -1, -1, -1,
    5, 11, 6, 4, 7, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 3, 0, 4, 7, 3, 6, 5, 11, -1, -1, -1, -1, -1, -1,
    1, 9, 0, 5, 11, 6, 8, 4, 7, -1, -1, -1, -1, -1, -1,
    11, 6, 5, 1, 9, 7, 1, 7, 3, 7, 9, 4, -1, -1, -1,
    6, 1, 2, 6, 5, 1, 4, 7, 8, -1, -1, -1, -1, -1, -1,
    1, 2, 5, 5, 2, 6, 3, 0, 4, 3, 4, 7, -1, -1, -1,
    8, 4, 7, 9, 0, 5, 0, 6, 5, 0, 2, 6, -1, -1, -1,
    7, 3, 2, 7, 2, 6, 5, 9, 4, -1, -1, -1, -1, -1, -1,
    3, 10, 2, 7, 8, 4, 11, 6, 5, -1, -1, -1, -1, -1, -1,
    5, 11, 6, 4, 7, 2, 4, 2, 0, 2, 7, 10, -1, -1, -1,
    0, 1, 9, 4, 7, 8, 2, 3, 10, 5, 11, 6, -1, -1, -1,
    9, 4, 5, 11, 2, 1, 7, 10, 6, -1, -1, -1, -1, -1, -1,
    8, 4, 7, 3, 10, 5, 3, 5, 1, 5, 10, 6, -1, -1, -1,
    5, 1, 0, 5, 0, 4, 7, 10, 6, -1, -1, -1, -1, -1, -1,
    0, 3, 8, 4, 5, 9, 10, 6, 7, -1, -1, -1, -1, -1, -1,
    4, 5, 9, 7, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    11, 4, 9, 6, 4, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 11, 6, 4, 9, 11, 0, 8, 3, -1, -1, -1, -1, -1, -1,
    11, 0, 1, 11, 6, 0, 6, 4, 0, -1, -1, -1, -1, -1, -1,
    8, 3, 1, 8, 1, 6, 8, 6, 4, 6, 1, 11, -1, -1, -1,
    1, 4, 9, 1, 2, 4, 2, 6, 4, -1, -1, -1, -1, -1, -1,
    3, 0, 8, 1, 2, 9, 2, 4, 9, 2, 6, 4, -1, -1, -1,
    0, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    8, 3, 2, 8, 2, 4, 4, 2, 6, -1, -1, -1, -1, -1, -1,
    11, 4, 9, 11, 6, 4, 10, 2, 3, -1, -1, -1, -1, -1, -1,
    0, 8, 2, 2, 8, 10, 4, 9, 11, 4, 11, 6, -1, -1, -1,
    3, 10, 2, 0, 1, 6, 0, 6, 4, 6, 1, 11, -1, -1, -1,
    6, 4, 8, 6, 8, 10, 2, 1, 11, -1, -1, -1, -1, -1, -1,
    9, 6, 4, 9, 3, 6, 9, 1, 3, 10, 6, 3, -1, -1, -1,
    8, 10, 6, 8, 6, 4, 9, 1, 0, -1, -1, -1, -1, -1, -1,
    3, 10, 6, 3, 6, 0, 0, 6, 4, -1, -1, -1, -1, -1, -1,
    6, 4, 8, 10, 6, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    7, 11, 6, 7, 8, 11, 8, 9, 11, -1, -1, -1, -1, -1, -1,
    0, 7, 3, 0, 11, 7, 0, 9, 11, 6, 7, 11, -1, -1, -1,
    11, 6, 7, 1, 11, 7, 1, 7, 8, 1, 8, 0, -1, -1, -1,
    11, 6, 7, 11, 7, 1, 1, 7, 3, -1, -1, -1, -1, -1, -1,
    1, 2, 6, 1, 6, 8, 1, 8, 9, 8, 6, 7, -1, -1, -1,
    2, 6, 7, 2, 7, 3, 0, 9, 1, -1, -1, -1, -1, -1, -1,
    7, 8, 0, 7, 0, 6, 6, 0, 2, -1, -1, -1, -1, -1, -1,
    7, 3, 2, 6, 7, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    2, 3, 10, 11, 6, 8, 11, 8, 9, 8, 6, 7, -1, -1, -1,
    2, 0, 9, 2, 9, 11, 6, 7, 10, -1, -1, -1, -1, -1, -1,
    1, 11, 2, 3, 8, 0, 6, 7, 10, -1, -1, -1, -1, -1, -1,
    11, 2, 1, 6, 7, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    8, 9, 1, 8, 1, 3, 10, 6, 7, -1, -1, -1, -1, -1, -1,
    0, 9, 1, 10, 6, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 8, 0, 10, 6, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    7, 10, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    7, 6, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 0, 8, 10, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 1, 9, 10, 7, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    8, 1, 9, 8, 3, 1, 10, 7, 6, -1, -1, -1, -1, -1, -1,
    11, 1, 2, 6, 10, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 2, 11, 3, 0, 8, 6, 10, 7, -1, -1, -1, -1, -1, -1,
    2, 9, 0, 2, 11, 9, 6, 10, 7, -1, -1, -1, -1, -1, -1,
    2, 10, 3, 11, 8, 6, 11, 9, 8, 8, 7, 6, -1, -1, -1,
    7, 2, 3, 6, 2, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    7, 0, 8, 7, 6, 0, 6, 2, 0, -1, -1, -1, -1, -1, -1,
    2, 7, 6, 2, 3, 7, 0, 1, 9, -1, -1, -1, -1, -1, -1,
    1, 6, 2, 1, 8, 6, 1, 9, 8, 8, 7, 6, -1, -1, -1,
    11, 7, 6, 11, 1, 7, 1, 3, 7, -1, -1, -1, -1, -1, -1,
    11, 7, 6, 1, 7, 11, 1, 8, 7, 1, 0, 8, -1, -1, -1,
    0, 3, 7, 0, 7, 11, 0, 11, 9, 6, 11, 7, -1, -1, -1,
    7, 6, 11, 7, 11, 8, 8, 11, 9, -1, -1, -1, -1, -1, -1,
    6, 8, 4, 10, 8, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 6, 10, 3, 0, 6, 0, 4, 6, -1, -1, -1, -1, -1, -1,
    8, 6, 10, 8, 4, 6, 9, 0, 1, -1, -1, -1, -1, -1, -1,
    9, 4, 6, 9, 6, 3, 9, 3, 1, 10, 3, 6, -1, -1, -1,
    6, 8, 4, 6, 10, 8, 2, 11, 1, -1, -1, -1, -1, -1, -1,
    3, 2, 10, 0, 6, 1, 0, 4, 6, 6, 11, 1, -1, -1, -1,
    0, 2, 8, 2, 10, 8, 4, 11, 9, 4, 6, 11, -1, -1, -1,
    11, 9, 4, 11, 4, 6, 10, 3, 2, -1, -1, -1, -1, -1, -1,
    8, 2, 3, 8, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1,
    0, 4, 2, 4, 6, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 8, 0, 1, 9, 2, 2, 9, 4, 2, 4, 6, -1, -1, -1,
    1, 9, 4, 1, 4, 2, 2, 4, 6, -1, -1, -1, -1, -1, -1,
    8, 1, 3, 8, 6, 1, 8, 4, 6, 6, 11, 1, -1, -1, -1,
    11, 1, 0, 11, 0, 6, 6, 0, 4, -1, -1, -1, -1, -1, -1,
    4, 6, 11, 4, 11, 9, 0, 3, 8, -1, -1, -1, -1, -1, -1,
    11, 9, 4, 6, 11, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 9, 5, 7, 6, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 8, 3, 4, 9, 5, 10, 7, 6, -1, -1, -1, -1, -1, -1,
    5, 0, 1, 5, 4, 0, 7, 6, 10, -1, -1, -1, -1, -1, -1,
    8, 7, 4, 3, 5, 10, 3, 1, 5, 5, 6, 10, -1, -1, -1,
    9, 5, 4, 11, 1, 2, 7, 6, 10, -1, -1, -1, -1, -1, -1,
    0, 9, 1, 4, 8, 7, 2, 10, 3, 5, 6, 11, -1, -1, -1,
    5, 6, 11, 4, 2, 7, 4, 0, 2, 2, 10, 7, -1, -1, -1,
    3, 2, 10, 7, 4, 8, 11, 5, 6, -1, -1, -1, -1, -1, -1,
    7, 2, 3, 7, 6, 2, 5, 4, 9, -1, -1, -1, -1, -1, -1,
    8, 7, 4, 9, 5, 0, 0, 5, 6, 0, 6, 2, -1, -1, -1,
    1, 5, 2, 5, 6, 2, 3, 4, 0, 3, 7, 4, -1, -1, -1,
    6, 2, 1, 6, 1, 5, 4, 8, 7, -1, -1, -1, -1, -1, -1,
    11, 5, 6, 1, 7, 9, 1, 3, 7, 7, 4, 9, -1, -1, -1,
    1, 0, 9, 5, 6, 11, 8, 7, 4, -1, -1, -1, -1, -1, -1,
    4, 0, 3, 4, 3, 7, 6, 11, 5, -1, -1, -1, -1, -1, -1,
    5, 6, 11, 4, 8, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    6, 9, 5, 6, 10, 9, 10, 8, 9, -1, -1, -1, -1, -1, -1,
    3, 6, 10, 0, 6, 3, 0, 5, 6, 0, 9, 5, -1, -1, -1,
    0, 10, 8, 0, 5, 10, 0, 1, 5, 5, 6, 10, -1, -1, -1,
    6, 10, 3, 6, 3, 5, 5, 3, 1, -1, -1, -1, -1, -1, -1,
    5, 6, 11, 1, 2, 9, 9, 2, 10, 9, 10, 8, -1, -1, -1,
    0, 9, 1, 2, 10, 3, 5, 6, 11, -1, -1, -1, -1, -1, -1,
    10, 8, 0, 10, 0, 2, 11, 5, 6, -1, -1, -1, -1, -1, -1,
    2, 10, 3, 11, 5, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    5, 8, 9, 5, 2, 8, 5, 6, 2, 3, 8, 2, -1, -1, -1,
    9, 5, 6, 9, 6, 0, 0, 6, 2, -1, -1, -1, -1, -1, -1,
    1, 5, 6, 1, 6, 2, 3, 8, 0, -1, -1, -1, -1, -1, -1,
    1, 5, 6, 2, 1, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 3, 8, 1, 8, 9, 5, 6, 11, -1, -1, -1, -1, -1, -1,
    9, 1, 0, 5, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 3, 8, 5, 6, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    11, 5, 6, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    10, 5, 11, 7, 5, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    10, 5, 11, 10, 7, 5, 8, 3, 0, -1, -1, -1, -1, -1, -1,
    5, 10, 7, 5, 11, 10, 1, 9, 0, -1, -1, -1, -1, -1, -1,
    9, 8, 5, 8, 7, 5, 11, 3, 1, 11, 10, 3, -1, -1, -1,
    10, 1, 2, 10, 7, 1, 7, 5, 1, -1, -1, -1, -1, -1, -1,
    2, 10, 3, 0, 8, 1, 1, 8, 7, 1, 7, 5, -1, -1, -1,
    9, 7, 5, 9, 2, 7, 9, 0, 2, 2, 10, 7, -1, -1, -1,
    7, 5, 9, 7, 9, 8, 3, 2, 10, -1, -1, -1, -1, -1, -1,
    2, 5, 11, 2, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1,
    8, 2, 0, 8, 5, 2, 8, 7, 5, 11, 2, 5, -1, -1, -1,
    11, 2, 1, 9, 0, 5, 5, 0, 3, 5, 3, 7, -1, -1, -1,
    9, 8, 7, 9, 7, 5, 11, 2, 1, -1, -1, -1, -1, -1, -1,
    1, 3, 5, 3, 7, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 8, 7, 0, 7, 1, 1, 7, 5, -1, -1, -1, -1, -1, -1,
    9, 0, 3, 9, 3, 5, 5, 3, 7, -1, -1, -1, -1, -1, -1,
    9, 8, 7, 5, 9, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    5, 8, 4, 5, 11, 8, 11, 10, 8, -1, -1, -1, -1, -1, -1,
    5, 0, 4, 5, 10, 0, 5, 11, 10, 10, 3, 0, -1, -1, -1,
    4, 5, 9, 0, 1, 8, 8, 1, 11, 8, 11, 10, -1, -1, -1,
    11, 10, 3, 11, 3, 1, 9, 4, 5, -1, -1, -1, -1, -1, -1,
    2, 5, 1, 2, 8, 5, 2, 10, 8, 4, 5, 8, -1, -1, -1,
    0, 4, 5, 0, 5, 1, 2, 10, 3, -1, -1, -1, -1, -1, -1,
    0, 2, 10, 0, 10, 8, 4, 5, 9, -1, -1, -1, -1, -1, -1,
    9, 4, 5, 2, 10, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    2, 5, 11, 3, 5, 2, 3, 4, 5, 3, 8, 4, -1, -1, -1,
    5, 11, 2, 5, 2, 4, 4, 2, 0, -1, -1, -1, -1, -1, -1,
    3, 8, 0, 1, 11, 2, 4, 5, 9, -1, -1, -1, -1, -1, -1,
    1, 11, 2, 9, 4, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    8, 4, 5, 8, 5, 3, 3, 5, 1, -1, -1, -1, -1, -1, -1,
    0, 4, 5, 1, 0, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 4, 5, 0, 3, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 4, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 10, 7, 4, 9, 10, 9, 11, 10, -1, -1, -1, -1, -1, -1,
    4, 8, 7, 9, 10, 0, 9, 11, 10, 10, 3, 0, -1, -1, -1,
    1, 11, 10, 1, 10, 4, 1, 4, 0, 7, 4, 10, -1, -1, -1,
    3, 1, 11, 3, 11, 10, 7, 4, 8, -1, -1, -1, -1, -1, -1,
    4, 10, 7, 9, 10, 4, 9, 2, 10, 9, 1, 2, -1, -1, -1,
    9, 1, 0, 8, 7, 4, 2, 10, 3, -1, -1, -1, -1, -1, -1,
    10, 7, 4, 10, 4, 2, 2, 4, 0, -1, -1, -1, -1, -1, -1,
    8, 7, 4, 3, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    2, 9, 11, 2, 7, 9, 2, 3, 7, 7, 4, 9, -1, -1, -1,
    9, 11, 2, 9, 2, 0, 8, 7, 4, -1, -1, -1, -1, -1, -1,
    3, 7, 4, 3, 4, 0, 1, 11, 2, -1, -1, -1, -1, -1, -1,
    1, 11, 2, 8, 7, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 9, 1, 4, 1, 7, 7, 1, 3, -1, -1, -1, -1, -1, -1,
    0, 9, 1, 8, 7, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 0, 3, 7, 4, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    4, 8, 7, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    9, 11, 8, 11, 10, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 0, 9, 3, 9, 10, 10, 9, 11, -1, -1, -1, -1, -1, -1,
    0, 1, 11, 0, 11, 8, 8, 11, 10, -1, -1, -1, -1, -1, -1,
    3, 1, 11, 10, 3, 11, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 2, 10, 1, 10, 9, 9, 10, 8, -1, -1, -1, -1, -1, -1,
    1, 0, 9, 2, 10, 3, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 2, 10, 8, 0, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    3, 2, 10, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    2, 3, 8, 2, 8, 11, 11, 8, 9, -1, -1, -1, -1, -1, -1,
    9, 11, 2, 0, 9, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 3, 8, 1, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 11, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    1, 3, 8, 9, 1, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 9, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    0, 3, 8, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
  };
}