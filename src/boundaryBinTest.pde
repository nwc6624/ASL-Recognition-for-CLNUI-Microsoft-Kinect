void boundaryBin(int loc)
{
  if (loc%w>0&&loc/w>0&&loc%w<w-1&&loc/w<h-1)
  {
    int neighbours =0; //clears value of number of pixels in a 3*3 square around the pixel at loc
    int xLens=3;
    int yLens=3;
    int  x3=loc%w;
    int y3=loc/w;
    float bl=blue (handFat.pixels[loc]);
    float re=red (handFat.pixels[loc]);

    if (x3>(xLens-1)/2&&y3>(yLens-1)/2&&x3<w-(xLens-1)/2-1&&y3<h-(yLens-1)/2-1)//ensures averaging lense stays within boundary of image
    {

      for (int x=0;x<xLens;x++)
      { 
        for (int y=0;y<yLens;y++)
        {
          int locTemp = loc+x+y*w-(xLens-1)/2-((yLens-1)/2)*w; //temp position of pixel in square to be tested
          float blTemp=blue (handFat.pixels[locTemp]);
          float reTemp=red (handFat.pixels[locTemp]); 
          if (blTemp>250&&reTemp>1)
          {
            neighbours++;
          }
        }
      }
      if(neighbours<xLens*yLens&&bl>250&&re>1&&loc/w<h-3)
      {
        boundaryBin=true;
         //handFat.pixels[loc]=color(255,0,0);
         
      }

      else 
      {
        boundaryBin=false;

      }
    }
  }
  else
  {
    boundaryBin=false;

  }
}

