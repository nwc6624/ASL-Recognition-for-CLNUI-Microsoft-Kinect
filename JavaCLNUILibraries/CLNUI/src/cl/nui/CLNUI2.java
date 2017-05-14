package cl.nui;

import java.nio.IntBuffer;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
 

public class CLNUI2
{
	public static native Pointer CreateNUIMotor();
	public static native boolean DestroyNUIMotor( Pointer mot );

    // Get device serial number
    native static String GetNUIMotorSerial( Pointer mot);

    // Motor control
    native static boolean NUIMotorMove( Pointer mot, short position );

    // Get accelerometer data
    native static boolean GetNUIMotorAccelerometer( Pointer mot, short x, short y, short z );

    native static boolean SetNUIMotorLED( Pointer mot, byte value );

    // Library initialization
    public static native Pointer CreateNUICamera();
    public static native boolean DestroyNUICamera( Pointer cam );

    // Camera capture control
    native static boolean StartNUICamera( Pointer cam );
    native static boolean StopNUICamera( Pointer cam );
    
    // Camera video frame image data retrieval
    native static boolean GetNUICameraColorFrameRAW( Pointer cam, byte[] pData, int waitTimeout);
    native static boolean GetNUICameraColorFrameRGB24( Pointer cam, byte[] pData, int waitTimeout);
    native static boolean GetNUICameraColorFrameRGB32( Pointer cam, int[] pData, int waitTimeout);
 // Camera depth frame image data retrieval
    native static boolean GetNUICameraDepthFrameRAW( Pointer cam, short[] pData, int waitTimeout );
    native static boolean GetNUICameraDepthFrameRGB32( Pointer cam, int[] pData, int waitTimeout );

    
    
    private static boolean USE_NATIVE = true;
    
    //private PApplet parent;
    private static boolean isMotorCreated = false;
    private static boolean isCameraCreated = false;
    private static boolean libraryLoaded = false;
    private static String libraryFilename = "CLNUIDevice.dll";
    private static String dllpathx32 = "C://Program Files (x86)//Code Laboratories//CL NUI Platform//SDK//Bin//CLNUIDevice.dll";
    private static String dllpathx64 = "C://Program Files//Code Laboratories//CL NUI Platform//SDK//Bin//CLNUIDevice.dll";
    private Pointer _motor, _camera;

    private static int[] _colorData, _depthData;

    
    //
    // Static methods
    //
    static
    {
        try
        {
        	if( USE_NATIVE ) Native.register( dllpathx32 );
        	else System.load(dllpathx32);
            libraryLoaded = true;
            System.out.println( "'" + dllpathx32 + " was loaded" );
        }
        catch(UnsatisfiedLinkError e1)
        {
            System.out.println("(1) Could not find the CLNUIDevice.dll");
            try
            {
            	if( USE_NATIVE ) Native.register( dllpathx64 );
            	else System.load(dllpathx64);
                libraryLoaded = true;
                System.out.println( "'" + dllpathx64 + "' was loaded");
            }
            catch(UnsatisfiedLinkError e2)
            {
                System.out.println("(2) Could not find the CLNUIDevice.dll");
            }
        }
    }
    

    /**
     * Load library with custom path
     * 
     * @param libraryPath
     */
    public static void loadLibrary( String libraryPath )
    {
        if( libraryLoaded ) return;
        try
        {
        	if( USE_NATIVE )  Native.register( libraryPath );
        	else System.load(libraryPath);
            System.out.println("(Custom) '" + libraryPath + "' was loaded");
        }
        catch(UnsatisfiedLinkError e1)
        {
            System.out.println("(3) Could not find the CLNUIDevice.dll (Custom Path)");
        }
    }

    public static boolean isLibraryLoaded()
    {
        return libraryLoaded;
    }

    
    private void allocate()
    {
		_colorData = new int[ 640*480 ];
		_depthData = new int[ 640*480 ];
    }
    
    public String getSerial()
    {
    	if( _motor != null ) {
    		return GetNUIMotorSerial( _motor );
    	}
    	
    	System.err.println( "(CLNUI)  Failed to get serial" );
    	return null;
    }

    public boolean createMotor()
    {
        if( !libraryLoaded ) {
        	System.err.println( "CLNUIDevice.dll is not loaded" );
        	return false;
    	}
    	
    	//_motor = null;
    	_motor = CreateNUIMotor();
    	if( _motor != null ) {
    		isMotorCreated = true;
    	} else System.err.println( "(CLNUI)  Couldn't create motor." );
    	return isMotorCreated;
    }
    
    public boolean stopMotor()
    {
    	if( _motor != null ) {
    		DestroyNUIMotor( _motor );
    	}
    	else System.err.println( "(CLNUI)  Couldn't destroy motor." );
    	return isMotorCreated;
    }
    
    public boolean setMotorLed( int index )
    {
        if( !libraryLoaded ) {
        	System.err.println( "CLNUIDevice.dll is not loaded" );
        	return false;
    	}
    	
    	if( _motor != null ) {
    		SetNUIMotorLED( _motor, (byte)index );
    	}
    	else System.err.println( "(CLNUI)  Couldn't set motor LED." );
    	return isMotorCreated;
    }
    

    public boolean createCamera()
    {
        if( !libraryLoaded ) {
        	System.err.println( "CLNUIDevice.dll is not loaded" );
        	return false;
    	}
    	
    	//_camera = null;
    	_camera = CreateNUICamera();
    	if( _camera != null ) 
    	{
    		isCameraCreated = true;
    		allocate();
    	}
    	else System.err.println( "(CLNUI)  Couldn't create camera." );
    	return isCameraCreated;
    }

    public boolean startCamera()
    {
        if( !libraryLoaded ) {
        	System.err.println( "CLNUIDevice.dll is not loaded" );
        	return false;
    	}
    	
    	if( _camera != null )
    		StartNUICamera( _camera );
    	else System.err.println( "(CLNUI)  Couldn't start camera." );
    	return isCameraCreated;
    }


    public boolean stopCamera()
    {
    	if( _camera != null ) 
    	{
    		StopNUICamera( _camera );
    		DestroyNUICamera( _camera );
    	}
    	else System.err.println( "(CLNUI)  Couldn't stop camera." );
    	return isCameraCreated;
    }

    public int[] getColorArray( int timeOut )
    {
    	/*if( _camera == null ) {
    		System.err.println( "(CLNUI)  Can't access color data" );
    		return null;
    	}*/
    	GetNUICameraColorFrameRGB32( _camera, _colorData, timeOut );
    	return _colorData;
    }
    public IntBuffer getColorData( int timeOut )
    {
    	/*if( _camera == null ) {
    		System.err.println( "(CLNUI)  Can't access color data" );
    		return null;
    	}*/
    	GetNUICameraColorFrameRGB32( _camera, _colorData, timeOut );
    	return IntBuffer.wrap( _colorData );
    }
    
    public int[] getDepthArray( int timeOut )
    {
    	/*if( _camera == null ) {
    		System.err.println( "(CLNUI)  Can't access depth data" );
    		return null;
    	}*/
    	GetNUICameraDepthFrameRGB32( _camera, _depthData, timeOut );
    	return _depthData;
    }
    public IntBuffer getDepthData( int timeOut )
    {
    	/*if( _camera == null ) {
    		System.err.println( "(CLNUI)  Can't access depth data" );
    		return null;
    	}*/
    	GetNUICameraDepthFrameRGB32( _camera, _depthData, timeOut );
    	return IntBuffer.wrap( _depthData );
    }
}
