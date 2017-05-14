import com.sun.jna.Pointer;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import cl.nui.VKinect;


/**
 * Implements a simple point cloud based on depth buffer.
 * This is rendering quite alot of points in an unoptimized way. Speed was not important for this project
 * 
 * @author Victor Martins
 *
 */
public class App extends PApplet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	static String WINDOW_TITLE = "/CLNUIPointCloud";
	
	static boolean FULLSCREEN = false;
	
	static int WIDTH = 640;
	static int HEIGHT = 480;
	static int HALFWIDTH = WIDTH / 2;
	static int HALFHEIGHT = HEIGHT / 2;
	static float ONEOVERWIDTH = 1.0f / (float)WIDTH;
	static float ONEOVERHEIGHT = 1.0f / (float)HEIGHT;
	static float aspectRatio = WIDTH / (float)HEIGHT;

	static float clickTime;
	static boolean isKeyPressed;
	

	Pointer motor, camera;
    PImage colorData, depthData;

    VKinect kinect;
    
    
	
	public void setup()
	{
		this.frame.setTitle( WINDOW_TITLE );
		
		size( WIDTH, HEIGHT, OPENGL );
		//hint( ENABLE_OPENGL_4X_SMOOTH );
		frameRate( 60 );


		try {
			kinect = new VKinect();
			kinect.setTimeout( 300, 0 );	//2000, 1000 );
			//kinect.setColorMode( VKinect.MODE_RGB32 );
			//kinect.setDepthMode( VKinect.MODE_RGB32 );
			kinect.init();
			kinect.setNoiseRemoval( true );
			kinect.setInvertDepth( true );
		} catch (Exception e) {
			e.printStackTrace();
		}


		colorData = createImage( 640, 480, RGB );
		depthData = createImage( 640, 480, RGB );
	}



	public void draw()
	{
    	background( 66 );    	
    	float time = millis() * 0.001f;
    	
    	
    	
    	perspective( PI*0.25f, 1, 1, 6000 );
    	camera( cos(time*0.4f)*400, cos(time*0.45f)*100, sin(time*0.4f)*400, 0, 0, 0, 0, 1, 0 );
    	
		PVector p = new PVector();
    	int step = 4;
    	stroke( 204, 102, 0 );
    	beginShape( POINTS );
    	for( int j=0; j<kinect.getHeight(); j+=step )
    	{
        	for( int i=0; i<kinect.getWidth(); i+=step )
        	{
        		p.z = kinect.getDepthAt( i, j )>>8;
        		if( p.z > 1 )
        		{
	        		p.x = (i-kinect.getWidth()*0.5f) * 0.25f;
	        		p.y = (j-kinect.getHeight()*0.5f) * 0.25f;
	        		//p.z = kinect.getDistanceAt( i, j );
	        		//p.z = kinect.getNormDistanceAt( i, j ) * 400;
	        		//point( p.x, p.y, p.z );
	        		vertex( p.x, p.y, p.z );
	        		//vertex( p.x, p.y+1, p.z );
        		}
        	}
		}
    	endShape();
    		
    	
    	//
    	// Debug screens
    	//
    	perspective();
    	camera();
		colorData.pixels = kinect.getColor();
		colorData.updatePixels();
		//depthData.pixels = kinect.getDepth();
		//depthData.updatePixels();
		convertDepthMap();
		image( colorData, width-160, 0, 160, 120 );
    	image( depthData, width-160, 120, 160, 120 );
    	
	}


	public void convertDepthMap()
	{
		// Convert depth to 8bit grayscale just for display
		short[] buffer = kinect.getRawDepth();
		//depthData.loadPixels();
		for( int i=0; i<640*480; i++ )
			depthData.pixels[i] = color( (buffer[i]>>8)&0xff );
		depthData.updatePixels();
	}
	

	public void keyReleased()
	{
	}
	
	public void keyPressed()
	{
	}



	public void stop()
	{
		if( kinect != null ) kinect.release();

	    super.stop();
	}

	

	public static void main( String[] args ) 
	{
	    if( FULLSCREEN ) PApplet.main( new String[] { "--present", "App" } );
	    else	PApplet.main( new String[] { "--display=1", "App" } );
	    //PApplet.main( new String[] { "--present", "GPUNoise" } );
	}
} 