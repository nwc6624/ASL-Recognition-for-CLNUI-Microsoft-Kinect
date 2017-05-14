import com.sun.jna.Pointer;
import processing.core.PApplet;
import processing.core.PImage;

import cl.nui.CLNUI;
import cl.nui.VKinect;


/**
 * Implements direct access to clnui and a the wrapper vkinect.
 * 
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

	
	static String WINDOW_TITLE = "/CLNUITest";

	static boolean NATIVE_MODE = false;

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
		
		size( WIDTH, HEIGHT );
		//hint( ENABLE_OPENGL_4X_SMOOTH );
		frameRate( 60 );


		if( NATIVE_MODE )
		{
			motor = CLNUI.INSTANCE.CreateNUIMotor();
			camera = CLNUI.INSTANCE.CreateNUICamera();
			CLNUI.INSTANCE.StartNUICamera( camera );
			CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)1 );
			System.out.println( "Kinect Serial: " + CLNUI.INSTANCE.GetNUIMotorSerial(motor) );		    
		}
		else
		{
			try {
				kinect = new VKinect();
				kinect.setTimeout( 300, 0 );	//2000, 1000 );
				//kinect.setColorMode( VKinect.MODE_RGB32 );
				//kinect.setDepthMode( VKinect.MODE_RGB32 );
				kinect.init();
				kinect.setNoiseRemoval( true );	// noise removal. for a static kinect, setting this to false can help on getting depth values
				kinect.setInvertDepth( true );	// invert depthmap
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		colorData = createImage( 640, 480, RGB );
		depthData = createImage( 640, 480, RGB );
	}



	public void draw()
	{
    	background( 66 );
		
		if( NATIVE_MODE )
		{
	    	//boolean res = false;
	    	CLNUI.INSTANCE.GetNUICameraColorFrameRGB32( camera, colorData.pixels, 300 );	//reinterpret_cast<DWORD*>(uiColorData) );
			colorData.updatePixels();
	    	//System.out.println( "Color result: " + res );
	    	CLNUI.INSTANCE.GetNUICameraDepthFrameRGB32( camera, depthData.pixels, 0 );	//reinterpret_cast<DWORD*>(uiDepthData) );
			depthData.updatePixels();
	    	//System.out.println( "Depth result: " + res );

			image( colorData, 0, 0, 320, 240 );
	    	image( depthData, 320, 0, 320, 240 );
		}
		else
		{
			colorData.pixels = kinect.getColor();
			colorData.updatePixels();
			//depthData.pixels = kinect.getDepth();
			//depthData.updatePixels();
			convertDepthMap();
			image( colorData, 0, 0, 320, 240 );
	    	image( depthData, 320, 0, 320, 240 );
		}
		
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
		if( NATIVE_MODE )
		{
			CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)0 );
			CLNUI.INSTANCE.DestroyNUIMotor( motor );    	
			CLNUI.INSTANCE.DestroyNUICamera( camera );
		}
		else
		{
			kinect.release();
		}

	    super.stop();
	}

	

	public static void main( String[] args ) 
	{
	    if( FULLSCREEN ) PApplet.main( new String[] { "--present", "App" } );
	    else	PApplet.main( new String[] { "--display=1", "App" } );
	    //PApplet.main( new String[] { "--present", "GPUNoise" } );
	}
} 