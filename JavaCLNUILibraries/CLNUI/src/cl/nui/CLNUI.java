package cl.nui;

import com.sun.jna.Library;
import com.sun.jna.Native;
//import com.sun.jna.Platform;
import com.sun.jna.Pointer;
 

public interface CLNUI extends Library
{
    //private PApplet parent;
    static boolean libraryLoaded = false;
    public static String libraryFilename = "CLNUIDevice.dll";    
    static String dllpathx32 = "C://Program Files (x86)//Code Laboratories//CL NUI Platform//SDK//Bin//CLNUIDevice.dll";
    static String dllpathx64 = "C://Program Files//Code Laboratories//CL NUI Platform//SDK//Bin//CLNUIDevice.dll";

	CLNUI INSTANCE = (CLNUI)Native.loadLibrary( libraryFilename, CLNUI.class );
	//CLNUI INSTANCE = (CLNUI)Native.loadLibrary( dllpathx32, CLNUI.class );
	//CLNUI INSTANCE = (CLNUI)Native.loadLibrary((Platform.isWindows() ? dllpathx32 : "c"), CLNUI.class );
	
	void loadLibrary( String libName ); 
    
	// Library initialization
	Pointer CreateNUIMotor();
	boolean DestroyNUIMotor( Pointer mot );
	
	// Get device serial number
	String GetNUIMotorSerial(Pointer mot );

	// Motor control
	boolean NUIMotorMove(Pointer mot, short position);

	// Get accelerometer data
	boolean GetNUIMotorAccelerometer(Pointer mot, short x, short y, short z);

	boolean SetNUIMotorLED(Pointer mot, byte value);
	
	
	// Library initialization
	Pointer CreateNUICamera();
	boolean DestroyNUICamera(Pointer cam);

	// Camera capture control
	boolean StartNUICamera(Pointer cam);
	boolean StopNUICamera(Pointer cam);

	// Camera video frame image data retrieval
	boolean GetNUICameraColorFrameRAW(Pointer cam, byte[] pData, int waitTimeout );
	boolean GetNUICameraColorFrameRGB24(Pointer cam, byte[] pData, int waitTimeout );
	boolean GetNUICameraColorFrameRGB32(Pointer cam, int[] pData, int waitTimeout );

	// Camera depth frame image data retrieval
	boolean GetNUICameraDepthFrameRAW(Pointer cam, short[] pData, int waitTimeout );
	boolean GetNUICameraDepthFrameRGB32(Pointer cam, int[] pData, int waitTimeout );	
}
