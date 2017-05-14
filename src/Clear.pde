void clearHandSmooth(int x, int y,int loc)
{
  if(x<w-(xLens-1)/2-1&&y<h-(yLens-1)/2-1) // clears handSmooth one x and y value in front of the smoothing lens
  {
    handSmooth.pixels[loc+(xLens-1)/2+(yLens-1)/2*w]=color(0);
  }
}      


void clearXProjection(int loc) // clears xProjection
{
  xProjection.pixels[loc] = color (0);
}

void clearYProjection(int loc) // clears yProjection
{
  yProjection.pixels[loc] = color (0);
}

void clearHandTrace(int loc) // clears handTrace
{
  handTrace.pixels[loc] = color (0);
}
void clearHand(int loc) // clears hand
{
  hand.pixels[loc] = color (0);
}
void clearHandFat(int loc) // clears hand
{
  handFat.pixels[loc]=color(0);
}


void hand(int loc,int y,float bl,float re) //draws hand
{
  if(bl>250&&re>1&&y<tempRelMinYOfXProj)
  {
    hand.pixels[loc]=color(0,255,0);
  }
}  
