import SimpleOpenNI.*;

import com.sun.jna.Pointer;
import cl.nui.CLNUI;
import processing.opengl.*;


Pointer motor, camera;
PImage depthData,handSmooth,xProjection,yProjection, handTrace, hand,handFat,traceSmooth;

//height and width
int w=640;
int h=480;

//lens for average filter size
int xLens=3;
int yLens=3;

//outlier filter sensitivity ie removes taret pixel if fewer than proxFilter pixels in lens area.
int proxFilter=20;



PFont ft;
PFont ft2;
// x projection variables
int xOfXProj=0;//x position of x projection pixels
int yOfXProj=0;//y position of x projection pixels
int smoothYAmount = 30;
int [] xPos = new int [smoothYAmount];//array to hold 10 values x values of x projecton
int maxXOfXProj; //maximum x value in x projection
int maxYOfXProj; //max y valu in x projeciton
int xProjXAve=0;//mean of values in array
int prevXProjXAve=0;//previous mean of values in array
int relMinXOfXProj=w;//x coord of relative minimum of x projection
int relMinYOfXProj=h;//y coord of relative minimum of x projection
int tempRelMinYOfXProj=h-1;


// y projection variables
int xOfYProj=0;//x position of y projection pixels
int yOfYProj=0;//y position of y projection pixels
int smoothXAmount = 30;
int [] yPos = new int [smoothXAmount];//array to hold 10 values x values of x projecton
int maxYOfYProj; //maximum x value in x projection
int maxXOfYProj; //max y valu in x projeciton
int yProjYAve=0;//mean of values in array
int prevYProjYAve=0;//previous mean of values in array

//finds mid point on hand y coord

int tempMaxXOfXProj=0; // necessary becasue MaxXOfXProj is cleared and need this value to identify midpoint of hands
int tempMaxYOfXProj=0; // necessary becasue MaxXOfXProj is cleared and need this value to identify midpoint of hands

//finds mid point on hand x coord

int tempMaxXOfYProj=0; // necessary becasue MaxXOfXProj is cleared and need this value to identify midpoint of hands
int tempMaxYOfYProj=0; // necessary becasue MaxXOfXProj is cleared and need this value to identify midpoint of hands

//trace and edge detection
boolean boundary=false; // boundary test flag boundary = 0 means not boudanry
boolean boundaryBin=false; // boundary test flag boundary = 0 means not boudanry
int boundaryNeighbours; //test number of nieghbours that are also boundary n
int maxPoint=0; // fist point of edge array. 
int edge [] = new int [w*4]; //edge array
int edgeSmooth [] = new int [w*4]; //edge array

//if true activates NN once
boolean analyse = false;






void setup()
{
  smooth();
  size( 800, 600);
  frameRate( 100 );

  motor = CLNUI.INSTANCE.CreateNUIMotor();
  camera = CLNUI.INSTANCE.CreateNUICamera();
  CLNUI.INSTANCE.StartNUICamera( camera );
  CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)7 );
  System.out.println( "Kinect Serial: " + CLNUI.INSTANCE.GetNUIMotorSerial(motor) );


  depthData = createImage( 640, 480, RGB );
//  handSmooth = createImage (640, 480, RGB);
  handTrace = createImage (640, 480, RGB);
//  xProjection = createImage (640, 480, RGB);
//  yProjection = createImage (640, 480, RGB);
//  hand = createImage (640, 480, RGB);
  handFat = createImage (640, 480, RGB);
  traceSmooth = createImage (640, 480, RGB);
  ft = loadFont("IrisUPCBold-28.vlw");
  ft2=loadFont("DialogInput.bolditalic-150.vlw");
}




public void draw()
{

  smooth();




  CLNUI.INSTANCE.GetNUICameraDepthFrameRGB32( camera, depthData.pixels, 0 );	//reinterpret_cast<DWORD*>(uiDepthData) );
  //System.out.println( "Depth result: " + res );

  background( 0 );
  // image( handSmooth, 0, 0,400,300);
  // image( handTrace,0,0,800,600);
  //    image( hand, 400,0,400,300);
//  image( xProjection, 0,300,400,300);
//  image( yProjection, 0,0,400,300);



  for (int y = 0; y < h; y++)
  { 
    for (int x = 0; x < w; x++)
    {

      int loc = x+y*w;
      float bl = blue (depthData.pixels[loc]);
      float re = red (depthData.pixels[loc]);

      //clearXProjection (loc); //clears x projection
      // clearHandSmooth  (x,y,loc); //clears handsmooth
      clearHandTrace (loc);//clears handTrace
      clearHandFat(loc);

      if(bl>250&&re>1&&y<h-10)
      {
        // maxPoint=loc-1-1*w;
        handFat(loc);
      }  




     // xProjection(x,y,bl,re); //draws x projection
      //  hand(loc,y,bl,re);//draws hand
    }
  }

  if (maxPoint>10+10*w)
  {
    edge(maxPoint);
  }

//  for (int x = 0; x < w; x++)
//  {
//    for (int y = 0; y < h; y++)
//    { 
//      int loc = x+y*w;
//      float bl = blue (depthData.pixels[loc]);
//      float re = red (depthData.pixels[loc]);
//      clearYProjection (loc); //clears y projection
//      yProjection(x,y,bl,re);
//      
//    }
//  }




 // println(frameRate);



//      xProjectionMax();
//      xProjectionRelMin();
//      yProjectionMax();
//        midPoint(); //find mid point of hand
  depthData.updatePixels();
  //  handSmooth.updatePixels();
//  handTrace.updatePixels();
//  handFat.updatePixels();
  traceSmooth.updatePixels();
  //  hand.updatePixels();
//  xProjection.updatePixels();
//  yProjection.updatePixels();
}






void midPoint () //find mid poit on hand
{

  fill(0,0,255);
  ellipse(tempMaxXOfYProj*800/w,tempMaxYOfXProj*600/h,10,10);
}  




void keyPressed()
{
  analyse = true;
}









void prnt (int loc,int i)
{

  //println("x"+loc%w+"y"+loc/w);
}



public void stop()
{
  CLNUI.INSTANCE.SetNUIMotorLED( motor, (byte)0 );
  CLNUI.INSTANCE.DestroyNUIMotor( motor );    	
  CLNUI.INSTANCE.DestroyNUICamera( camera );    	

  super.stop();
}


