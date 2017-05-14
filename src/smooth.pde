

void smoothPixels(int x3, int y3,int loc,float bl, float re)
{

  if (x3>(xLens-1)/2&&y3>(yLens-1)/2&&x3<w-(xLens-1)/2-1&&y3<h-(yLens-1)/2-1&&bl>250&&re>1)//ensures averaging lense stays within boundary of image
  {


    //clears value for active neighbours 
    int pixelQuant=0; // number of active pixels to use as divisor

    //test for active pixels in a 5*5 squaree
    int totX = 0;
    int totY =0;
    int smoothedLoc=0;
    int xAvg=0;
    int yAvg=0;


    for (int x=0;x<xLens;x++)
    { 
      for (int y=0;y<yLens;y++)
      {

        int locTemp = loc+x+y*w-(xLens-1)/2-((yLens-1)/2)*w; //temp position of pixel in square to be tested

        float slTemp=blue (depthData.pixels[locTemp]);
        float reTemp=red (depthData.pixels[locTemp]);
        if (slTemp>250&&reTemp>1&&(abs ((loc%w)-(locTemp%w))+ abs((loc/w)-(locTemp/w))   )<(xLens-1)/2)//sets lens shape

        {

          totX=totX+x;
          totY=totY+y;
          pixelQuant++;
          //          if (loc==w/2+h/2*w)
          //          {
          //            handSmooth.pixels[locTemp]=color(0,255,0);//shows lens shape
          //          }
        }
      }
    }

    xAvg=totX/pixelQuant;
    yAvg=totY/pixelQuant;
    smoothedLoc = loc+xAvg+yAvg*w-(xLens-1)/2-((yLens-1)/2)*w;
    if(pixelQuant<=proxFilter)
    {
      handSmooth.pixels[smoothedLoc]=color(0); // clears pixels with fewer than 36 neighbours
    }
    else
    {
      handSmooth.pixels[smoothedLoc]=color(0,255,0);//draws smoothed hand
      // handSmooth.pixels[smoothedLoc-w]=color(0,255,0);
      //handSmooth.pixels[smoothedLoc+w]=color(0,255,0);
      //handSmooth.pixels[smoothedLoc-1]=color(0,255,0);
      //handSmooth.pixels[smoothedLoc+1]=color(0,255,0);
      //  handSmooth.pixels[smoothedLoc-w-1]=color(0,255,0);
      //  handSmooth.pixels[smoothedLoc+w+1]=color(0,255,0);
      //  handSmooth.pixels[smoothedLoc+w-1]=color(0,255,0);
      //  handSmooth.pixels[smoothedLoc-w+1]=color(0,255,0);
    }
  }
}

