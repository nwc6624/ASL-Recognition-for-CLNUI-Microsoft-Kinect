package cl.nui;

import com.sun.jna.Pointer;


/**
 * Kinect class for java on top of CLNUI from Code Laboratories.
 * Windows only and it's having problems with running on Processing IDE.
 * 
 * How to get started:
 * 1) Create kinect object  2) Setup any flags if needed  3) Call init()  4) Access data. simple   5) Release
 * 
 * 
 * @author Victor Martins
 *
 */
public class VKinect extends Thread
{
	public static boolean USE_THREAD = true;
	
	public static final int LED_OFF = 0;
	public static final int LED_GREEN = 1;
	public static final int LED_RED = 2;
	public static final int LED_ORANGE = 3;
	public static final int LED_BLINK_GREEN = 4;
	//public static final int LED_BLINK_GREEN = 5;
	public static final int LED_BLINK_RED_ORANGE = 6;
	//public static final int LED_BLINK_ORANGE = 7;
	

	public static final int MODE_RAW = 0;
	//public static int RGB24 = 1;
	public static final int MODE_RGB32 = 2;


	private final int _width = 640;
	private final int _height = 480;
	//private final int _halfWidth = _width / 2;
	//private final int _halfHeight = _height / 2;
	//private final float _oneOverWidth = 1.0f / (float)_width;
	//private final float _oneOverHeight = 1.0f / (float)_height;
	
	private boolean _isRunning = false;
	private boolean _invertDepth = false;
	private boolean _doNoiseRemoval = true;
	
	Pointer motor, camera;
    //PImage _colorData, _depthData;
    //PImage _prevColorData, _prevDepthData;
	
	int _yTable[];
    
    int[] _colorDataArray, _depthDataArray;
    int[] _backColorDataArray, _backDepthDataArray;

    //byte[] 	_colorDataArrayRAW, _backColorDataArrayRAW;
    short[] _backDepthDataArray16;
    short[] _depthDataArray16;
    int[] _depthDataArray8;
    //byte[]	_depthByteDataArrayRAW;	//, _backDepthByteDataArrayRAW;
    float[] _backDistancePixels;
    float[] _backDistancePixelsNorm;
    float[] _distancePixels;
    float[] _distancePixelsNorm;

    float[] _accelerometer;
    
    int _colorAccessMode;
    int _depthAccessMode;
    
	int _colorTimeout;
	int _depthTimeout;
    
    boolean isMotorCreated;
    boolean isCameraCreated;
    
    float _minDistance, _maxDistance;
    

    public long _longerTime = 0;
    public long _elapsedTime = 0;
    

    int gray( int gray )
    {
    	if (gray > 255) gray = 255; else if (gray < 0) gray = 0;
    	return 0xff000000 | (gray << 16) | (gray << 8) | gray;
    }
    
    
	public VKinect()
	{
		//_app = null;
	    isMotorCreated = false;
	    isCameraCreated = false;
	    
	    _colorAccessMode = MODE_RGB32;
	    _depthAccessMode = MODE_RAW;
	}
	

	
	/**
	 * Allocate memory for stuff we need
	 */
	private void allocate()
	{
		_yTable = new int[_height];
		for( int i=0; i<_height; i++ )
			_yTable[i] = i * _width;

		_colorDataArray = new int[_width*_height];
		_depthDataArray = new int[_width*_height];		
		//_colorDataArrayRAW = new byte[_width*_height];
		_backDepthDataArray16 = new short[_width*_height];
		_depthDataArray16 = new short[_width*_height];
		_depthDataArray8 = new int[_width*_height];
		_backColorDataArray = new int[_width*_height];
		_backDepthDataArray = new int[_width*_height];
		//_backColorDataArrayRAW = new byte[_width*_height];

		//_depthByteDataArrayRAW = new byte[_width*_height];
		//_backDepthByteDataArrayRAW = new byte[_width*_height];
		
		_distancePixels = new float[_width*_height];
		_distancePixelsNorm = new float[_width*_height];
		_backDistancePixels = new float[_width*_height];
		_backDistancePixelsNorm = new float[_width*_height];

		_accelerometer = new float[3];

		_colorTimeout = 300;
		_depthTimeout = 0;
	}
	

	/**
	 * Start Kinect's motor and camera.
	 * This should be used if you're not running processing
	 */
	public boolean init() throws Exception
	{
		try {
			motor = CLNUI.INSTANCE.CreateNUIMotor();
			if( motor == null ) {
				throw new Exception( "Failed to init kinect's motor" );
			}
			camera = CLNUI.INSTANCE.CreateNUICamera();
			if( camera == null ) {
				throw new Exception( "Failed to init kinect camera" );
			}
		} finally {
		}
		CLNUI.INSTANCE.StartNUICamera( camera );
		setLedColor( LED_GREEN );
		System.out.println( "+ Kinect serial number: " + CLNUI.INSTANCE.GetNUIMotorSerial(motor) );
		allocate();			
		
		if( USE_THREAD ) super.start();
		_isRunning = true;
		return true;
	}



	/**
	 * 
	 */
	public void run() 
	{
		while( USE_THREAD && _isRunning )
		{
			long before = System.currentTimeMillis();
			update();
			
			try {
				Thread.sleep( 1 );	// Sleep
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			

			long after = System.currentTimeMillis();
			_elapsedTime = after - before;
			if( _elapsedTime > _longerTime ) _longerTime = _elapsedTime;
			//System.out.println("Elapsed time: " + Long.toString(_elapsedTime) + " milliseconds. Max time was: " + _longerTime );

			//Thread.yield();
		}
	}




	/**
	 * Update data
	 */
	public void update()
	{
		//
		// Capture color array
		//
		switch( _colorAccessMode )
		{
		case MODE_RAW:
			//CLNUI.INSTANCE.GetNUICameraColorFrameRAW( camera, _backColorDataArrayRAW, _depthTimeout );
			//System.arraycopy( _backColorDataArrayRAW, 0, _colorDataArrayRAW, 0, _width*_height );
			// Runs in 32bit anyway
			CLNUI.INSTANCE.GetNUICameraColorFrameRGB32( camera, _backColorDataArray, _colorTimeout );
			System.arraycopy( _backColorDataArray, 0, _colorDataArray, 0, _width*_height );
			break;
		case MODE_RGB32:
			CLNUI.INSTANCE.GetNUICameraColorFrameRGB32( camera, _backColorDataArray, _colorTimeout );
			System.arraycopy( _backColorDataArray, 0, _colorDataArray, 0, _width*_height );
	    	break;
		}


		//
		// Capture depth array
		//
		switch( _depthAccessMode )
		{
		case MODE_RAW:
			CLNUI.INSTANCE.GetNUICameraDepthFrameRAW( camera, _backDepthDataArray16, _depthTimeout );

			_minDistance = Float.MAX_VALUE;
			_maxDistance = Float.MIN_VALUE;
			for( int i=0; i<_width*_height; i++ )
			{
				if( _backDepthDataArray16[i] < 1024 )
				{
					// As specified in: http://openkinect.org/wiki/Imaging_Information
					_backDistancePixels[i] = 100.f / (-0.00307f * (_backDepthDataArray16[i]) + 3.33f);
					
					// From Cinder-kinect. Convert to 16bit
					int v = _backDepthDataArray16[i];
					if( _invertDepth ) _depthDataArray[i] = (65535 - (65535 - ((v*v) >> 4)));
					else _depthDataArray[i] = (65535 - ((v*v) >> 4));
					_depthDataArray16[i] = (short)_depthDataArray[i];	//(65535 - ((v*v) >> 4));
					_depthDataArray8[i] = (int)( (2048 * 256) / (float)(2048-v) );
					//_depthDataArray8[i] = gray( 256 - ((v*v)>>12) );	//(int)( ( (2048 * 256) / (float)(2048-v) ) ) );
					//System.out.println( "depth: " + _depthDataArray8[i] );
				}
				else 
				{
					if( getNoiseRemoval() )
					{
						_depthDataArray[i] = 0;
						_depthDataArray16[i] = 0;
						_depthDataArray8[i] = 0;						
					}
				}

/***
 				float t = (float)_backDepthDataArray16[i];
				if( t < 0 ) t = 0;
				if( _doNoiseRemoval && t > 1023 ) t = 0;

				// Using equation from http://openkinect.org/wiki/Imaging_Information
				_backDistancePixels[i] = 100.f / (-0.00307f * (t) + 3.33f);
				//_backDistancePixels[i] = 100.f / (-0.00307f * ((float)_backDepthDataArray16[i]) + 3.33f);
//				float t = 2047.0f - (float)_backDepthDataArray16[i];
//				if( t > 2047-0 ) t = 2047-0;
//				if( t < 2047-1023 ) t = 2047-1023;
				if( _invertDepth ) _depthDataArray[i] = gray( (int)( (Math.floor( (2048.0f * 256.0f) / (2048-t) )) - 256 ) ); 
				else _depthDataArray[i] = gray( (int)( (Math.floor( (2048.0f * 256.0f) / (t) )) - 256 ) );
				//_depthByteDataArrayRAW[i] = (byte)(_depthDataArray16[i] - 256);	//( Math.floor((2048.0f * 256.0f) / t) - 256.0f );
				//System.out.println( i + " - " + _depthDataArray16[i] );
****/

/***
				// Remove far plane values
				if( _backDepthDataArray16[i] > 2048-1 ) 
				{
					_backDistancePixels[i] = 0;
					_depthDataArray[i] = 0;
				}
				else 
				{
					if( !_invertDepth )
					{
						// Using equation from http://openkinect.org/wiki/Imaging_Information
						_backDistancePixels[i] = 100.f / (-0.00307f * (_backDepthDataArray16[i]) + 3.33f);
						// Eliminate background noise or not. Is said that removing raw disparity above 1023
						// works effectively.
						if( _doNoiseRemoval )
						{
							// To convert the 11-bit disparity value to an 8-bit grayscale value that is fairly linear with respect to distance: (2048 * 256) / (2048 - rawDisparity)
							if( _backDepthDataArray16[i] < 1024 ) 
							{
								float t = _backDepthDataArray16[i];
								if( t < 0 ) t = 0;
								_depthDataArray[i] = (int)( ((float)(2048 * 256) / t)-256.0f );
								//_depthDataArray16[i] = (short)( (float)(2048 * 256) / (float)((_backDepthDataArray16[i])));
								//_depthByteDataArrayRAW[i] = (byte)_depthDataArray[i];	//( (float)(2048 * 256) / (float)((_backDepthDataArray16[i])));
							}
							else 
							{
								_depthDataArray[i] = 0;
								//_depthByteDataArrayRAW[i] = 0;
							}
						}
						else
						{
							float t = _backDepthDataArray16[i];
							if( t < 0 ) t = 0;
							_depthDataArray[i] = (int)( ((float)(2048 * 256) / t)-256.0f );
							//_depthDataArray16[i] = (short)( (2048 * 256) / (float)(_backDepthDataArray16[i]));
							//_depthByteDataArrayRAW[i] = (byte)_depthDataArray16[i];	//( (2048 * 256) / (float)(_backDepthDataArray16[i]));
						}
					}
					else
					{
						// Using equation from http://openkinect.org/wiki/Imaging_Information
						_backDistancePixels[i] = 100.f / (-0.00307f * (_backDepthDataArray16[i]) + 3.33f);
						
						// Eliminate background noise or not. Is said that removing raw disparity above 1023
						// works effectively.
						if( _doNoiseRemoval )
						{
							// To convert the 11-bit disparity value to an 8-bit grayscale value that is fairly linear with respect to distance: (2048 * 256) / (2048 - rawDisparity)
							if( _backDepthDataArray16[i] < 1024 ) 
							{
								float t = 2048.0f - _backDepthDataArray16[i];
								if( t < 0 ) t = 0;
								//if( t > 1023 ) t = 1023;
								_depthDataArray[i] = (int)( ((float)(2048 * 256) / t)-256.0f );
								//_depthDataArray16[i] = (short)( (float)(2048 * 256) / (float)(2048 - _backDepthDataArray16[i]));
								//_depthByteDataArrayRAW[i] = (byte)_depthDataArray[i];	//( (float)(2048 * 256) / (float)(2048 - _backDepthDataArray16[i]) );
							}
							else 
							{
								_depthDataArray[i] = 0;
								//_depthByteDataArrayRAW[i] = 0;
							}
						}
						else
						{
							float t = 2048 - _backDepthDataArray16[i];
							_depthDataArray[i] = (int)( ((float)(2048 * 256) / t)-256.0f );
							//_depthDataArray16[i] = (short)( (2048 * 256) / (float)(2048 - _backDepthDataArray16[i]));
							//_depthByteDataArrayRAW[i] = (byte)_depthDataArray16[i];	//( (2048 * 256) / (float)(2048 - _backDepthDataArray16[i]));
						}
					}
				}
****/
				if( _backDistancePixels[i] < _minDistance ) _minDistance = _backDistancePixels[i]; 
				if( _backDistancePixels[i] > _maxDistance ) _maxDistance = _backDistancePixels[i]; 				
			}

			// Compute normalized distances array
			for( int i=0; i<_width*_height; i++ )
			{
				_backDistancePixelsNorm[i] = (_backDistancePixels[i] * 0.0025f);	// divide by max acceptable distance, 400 centimeters.
			}
			System.arraycopy( _backDistancePixels, 0, _distancePixels, 0, _width*_height );
			System.arraycopy( _backDistancePixelsNorm, 0, _distancePixelsNorm, 0, _width*_height );
			break;
			
		case MODE_RGB32:
	    	CLNUI.INSTANCE.GetNUICameraDepthFrameRGB32( camera, _backDepthDataArray, _depthTimeout );
			System.arraycopy( _backDepthDataArray, 0, _depthDataArray, 0, _width*_height );
/*	    	
			_minDistance = Float.MAX_VALUE;
			_maxDistance = Float.MIN_VALUE;
			for( int i=0; i<_width*_height; i++ )
			{
				
				int v = (_backDepthDataArray[i]>>13);
				System.out.println( "depth: " + v + " -- " + (65535 - ((v*v) >> 4)) );
				
				if( v < 1024 )
				{
					// Using equation from http://openkinect.org/wiki/Imaging_Information
					_backDistancePixels[i] = 100.f / (-0.00307f * (v) + 3.33f);
					
					// From cinder-kinect.
					// Convert from 11bit to 16bit
					//int v = _backDepthDataArray[i];
					if( _invertDepth ) _depthDataArray[i] = (65535 - (65535 - ((v*v) >> 4)));
					else _depthDataArray[i] = (65535 - ((v*v) >> 4));
					_depthDataArray16[i] = (short)_depthDataArray[i];	//(65535 - ((v*v) >> 4));
					//_depthDataArray8[i] = gray( (int)( (2048 * 256) / (float)(v) ) );
					_depthDataArray8[i] = gray( _depthDataArray16[i]>>8 );
				}
				else 
				{
					if( getNoiseRemoval() )
					{
						_depthDataArray[i] = 0;
						_depthDataArray16[i] = 0;
						_depthDataArray8[i] = 0;						
					}
				}
			}*/  	
	    	break;
		}	
    }

	
	
	
	/**
	 * Return a 12-digit string with the motor serial number
	 * 
	 * @return
	 */
    public String getSerial()
    {
    	if( motor != null ) {
    		return CLNUI.INSTANCE.GetNUIMotorSerial(motor);
    	}
    	
    	System.err.println( "(CLNUI)  Failed to get serial" );
    	return null;
    }

    /**
     * Prepare and setup kinect's motor
     * 
     * @return
     */
    public boolean createMotor()
    {
		motor = CLNUI.INSTANCE.CreateNUIMotor();
    	if( motor != null ) {
    		isMotorCreated = true;
    	}
    	return isMotorCreated;
    }

    
    /**
     * Prepare and setup kinect's camera
     * 
     * @return
     */
    public boolean createCamera()
    {
    	//_camera = null;
		camera = CLNUI.INSTANCE.CreateNUICamera();
    	if( camera != null ) 
    	{
    		isCameraCreated = true;
    		allocate();
    	}
    	return isCameraCreated;
    }

    /**
     * Start camera. Make sure you have created it before calling this method
     * 
     * @return
     */
    public boolean startCamera()
    {
    	if( camera != null )
    		CLNUI.INSTANCE.StartNUICamera( camera );
    	else 
    		System.err.println( "(CLNUI)  Couldn't start camera." );
    	return isCameraCreated;
    }


    /**
     * Stop camera. Call this before destroying it
     * 
     * @return
     */
    public boolean stopCamera()
    {
    	if( camera != null ) 
    	{
    		CLNUI.INSTANCE.StopNUICamera( camera );
    		CLNUI.INSTANCE.DestroyNUICamera( camera );
    	}
    	else System.err.println( "(CLNUI)  Couldn't stop camera." );
    	return isCameraCreated;
    }

    /**
     * Stop motor
     * 
     * @return
     */
    public boolean stopMotor()
    {
    	if( motor != null ) 
    	{
    		CLNUI.INSTANCE.DestroyNUIMotor( motor );
    	}
    	else System.err.println( "(CLNUI)  Couldn't destroy motor." );
    	return isMotorCreated;
    }
    
    
    /**
     * Set kinect's LED.
     * 
     * index:
     * 0 LED off
     * 1 LED Green
     * 2 LED Red
     * 3 LED Orange
     * 4/5 LED Blink Green
     * 6/7 LED Blink Red/Orange
     * 
     * @param mode
     * @return
     */
    public boolean setLedColor( int mode )
    {
    	if( motor != null ) {
    		CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)mode );
    	}
    	else System.err.println( "(CLNUI)  Couldn't destroy motor." );
    	return isMotorCreated;
    }

    
    /**
     * Set motor's head tilt position
     * 
     * @param position
     * @return
     */
    public boolean setMotorTilt( int position )
    {
    	return CLNUI.INSTANCE.NUIMotorMove( motor, (short)position );
    }

    
    /**
     * Get motor's accelerometer's values for XYZ
     * 
     * @return array
     */
    public float[] getMotorAccelerometer()
    {
    	if( motor != null ) 
    	{
    		short x=0, y=0, z=0;
    		CLNUI.INSTANCE.GetNUIMotorAccelerometer( motor, x, y, z );
//    		CLNUI.INSTANCE.GetNUIMotorAccelerometer( motor, (short)_accelerometer[0], (short)_accelerometer[1], (short)_accelerometer[2] );
    		_accelerometer[0] = x;
    		_accelerometer[1] = y;
    		_accelerometer[2] = z;
    		return _accelerometer;
    	}
    	return null;
    }

    
//    public int[] getColorArray( int timeOut )
//    {
//    	if( _camera == null ) {
//    		System.err.println( "(CLNUI)  Can't access color data" );
//    		return null;
//    	}
//    	GetNUICameraColorFrameRGB32( _camera, _colorData, timeOut );
//    	return _colorData;
//    }
//    public IntBuffer getColorData( int timeOut )
//    {
//    	if( _camera == null ) {
//    		System.err.println( "(CLNUI)  Can't access color data" );
//    		return null;
//    	}
//    	GetNUICameraColorFrameRGB32( _camera, _colorData, timeOut );
//    	return IntBuffer.wrap( _colorData );
//    }
//    
//    public int[] getDepthArray( int timeOut )
//    {
//    	if( _camera == null ) {
//    		System.err.println( "(CLNUI)  Can't access depth data" );
//    		return null;
//    	}
//    	GetNUICameraDepthFrameRGB32( _camera, _depthData, timeOut );
//    	return _colorData;
//    }
//    public IntBuffer getDepthData( int timeOut )
//    {
//    	if( _camera == null ) {
//    		System.err.println( "(CLNUI)  Can't access depth data" );
//    		return null;
//    	}
//    	GetNUICameraDepthFrameRGB32( _camera, _depthData, timeOut );
//    	return IntBuffer.wrap( _colorData );
//    }

    
    /**
     * Check if camera is available.
     * I prefer to use this to check if object were created, instead of checking on every method.
     */
    public boolean isCameraValid()
    {
    	return (camera != null ) ? true : false; 
    }

    /**
     * Check if motor is available.
     * I prefer to use this to check if object were created, instead of checking on every method.
     */
    public boolean isMotorValid()
    {
    	return (motor != null ) ? true : false; 
    }
	
	
    /**
     * Get color data for current frame
     * 
     * @param timeOut
     * @return array
     */
	/*public int[] getColorArray( int timeOut )
	{
    	CLNUI.INSTANCE.GetNUICameraColorFrameRGB32( camera, _colorDataArray, timeOut );
		return _colorDataArray; 
	}*/
	
	/**
     * Get depth data for current frame
	 * 
	 * @param timeOut
	 * @return
	 */
	/*public int[] getDepthArray( int timeOut )
	{
    	CLNUI.INSTANCE.GetNUICameraDepthFrameRGB32( camera, _depthDataArray, 0 );
		return _depthDataArray; 
	}*/

	
	/**
	 * 
	 * @param timeOut
	 * @return
	 */
	/*public int[] getColorArrayRaw( int timeOut )
	{
		try 
		{
			CLNUI.INSTANCE.GetNUICameraColorFrameRAW( camera, _colorDataArrayRAW, 0 );
			System.arraycopy( _colorDataArrayRAW, 0, _colorDataArray, 0, _width*_height );
		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return _depthDataArray; 
	}*/
	
	
	/**
	 * Return raw depth map (11 bit values) 
	 * @param timeOut
	 * @return
	 */
	/*public short[] getDepthArrayRaw( int timeOut )
	{
		try 
		{
			CLNUI.INSTANCE.GetNUICameraDepthFrameRAW( camera, _backDepthDataArray16, 0 );
			System.arraycopy( _backDepthDataArray16, 0, _depthDataArray16, 0, _width*_height );			
		} catch( Exception e )
		{
			e.printStackTrace();
		}
		return _depthDataArray16; 
	}*/
	
	
	
	public int getColorMode()
	{
		return _colorAccessMode;
	}
	
	/**
	 * Set color capture mode. Available modes are RAW and RGB32
	 * 
	 * @param mode
	 */
	public void setColorMode( int mode )
	{
		_colorAccessMode = mode;
	}

	public int getDepthMode()
	{
		return _depthAccessMode;
	}
	
	/**
	 * Set depth capture mode. Available modes are RAW and RGB32
	 * 
	 * @param mode
	 */
	public void setDepthMode( int mode )
	{
		_depthAccessMode = mode;
	}


	/**
	 * Set timeout values for color and depth data
	 * Time is in milliseconds
	 * 
	 * @param colorTimeout
	 * @param depthTimeout
	 */
	public void setTimeout( int colorTimeout, int depthTimeout )
	{
		_colorTimeout = colorTimeout;
		_depthTimeout = depthTimeout;
	}

	
	public int[] getColor()
	{
		return _colorDataArray;
	}
	public int[] getDepth()
	{
		return _depthDataArray;
	}
//	public byte[] getColorRAW()
//	{
//		return _colorDataArrayRAW;
//	}
	public short[] getRawDepth()
	{
		return _depthDataArray16;
	}
	public int[] getDepth8()
	{
		return _depthDataArray8;
	}

	public float[] getDistanceData()
	{
		return _distancePixels;
	}

	public float[] getDistanceDataNorm()
	{
		return _distancePixelsNorm;
	}

	public float getMinDistance()
	{
		return _minDistance;
	}
	public float getMaxDistance()
	{
		return _maxDistance;
	}
	
	public int getWidth()
	{
		return _width;
	}
	public int getHeight()
	{
		return _height;
	}

	public int getColorAt( int x, int y )
	{
		return _colorDataArray[ x+_yTable[y] ];
//		if( getDepthMode() == MODE_RAW ) return _colorDataArrayRAW[ x+_yTable[y] ];
//		return _colorDataArray[ x+_yTable[y] ];
	}

	public int getDepthAt( int x, int y )
	{
		return _depthDataArray[ x+_yTable[y] ];
//		if( getDepthMode() == MODE_RAW ) return _depthDataArray16[ x+_yTable[y] ];
//		return _depthDataArray[ x+_yTable[y] ];
	}

	public float getDistanceAt( int x, int y )
	{
		return _distancePixels[ x+_yTable[y] ];
	}
	public float getNormDistanceAt( int x, int y )
	{
		return _distancePixelsNorm[ x+_yTable[y] ];
	}

	
	/*public float[] getPosition( int x, int y, float minDistance, float scaleFactor )
	{
		// Once you have the distance using the measurement above 
		// A good approximation for converting (i, j, z) to (x,y,z) is: 
		// x = (i - w / 2) * (z + minDistance) * scaleFactor 
		// y = (j - h / 2) * (z + minDistance) * scaleFactor 
		// z = z
		float z = getDepthAt( x, y ) >> 16;
		float[] pos = new float[3];
		pos[0] = (x - (_halfWidth)) * (z+minDistance) * scaleFactor;
		pos[1] = (y - (_halfHeight)) * (z+minDistance) * scaleFactor;
		pos[2] = getDepthAt( x, y );
		return pos;
	}*/
	

	public boolean getInvertDepth()
	{
		return _invertDepth;
	}
	
	public void setInvertDepth( boolean flag )
	{
		_invertDepth = flag;
	}

	public boolean getNoiseRemoval()
	{
		return _doNoiseRemoval;
	}
	
	public void setNoiseRemoval( boolean flag )
	{
		_doNoiseRemoval = flag;
	}
	
	

	/**
	 * Release all.
	 */
	public void release()
	{
//		if( USE_THREAD )
//		{
//			try {
//				// Wait some time while thread stops.
//				Thread.sleep( 500 );
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		
		_isRunning = false;		

		if( motor != null ) 
		{
			CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)0 );
			CLNUI.INSTANCE.DestroyNUIMotor( motor );
		}
		if( camera != null ) 
		{
			CLNUI.INSTANCE.StopNUICamera( camera );    	
			CLNUI.INSTANCE.DestroyNUICamera( camera );
		}
	}
}
