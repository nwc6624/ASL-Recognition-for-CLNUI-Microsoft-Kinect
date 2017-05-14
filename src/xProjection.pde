void xProjection (int x,int y,float bl,float re)
{
  if (y==h-1&&x==w-1) // clears array caontiang x values of projection 
  {
    for (int i=0; i<xPos.length;i++)
    {
      xPos[i]=0;
    }
  }    
  int sumXPosArray=0;//sum of values in array


  if(bl>250&&re>1)
  {

    xOfXProj++;
  } 


  if(x==w-1)
  {
    for (int i=0; i<xPos.length-1;i++) // stores new x value in array and shift previous x values down one place
    {
      xPos[i] = xPos[i+1];
    }

    xPos[xPos.length-1] = xOfXProj;

    for (int i=0; i<xPos.length;i++) // sum array
    {
      sumXPosArray+=xPos[i];
    }  
    xProjXAve=sumXPosArray/xPos.length; //mean of x values in projection over xPos.length

      if (maxXOfXProj<xProjXAve) // finds maximum x value of x projection
    {
      maxXOfXProj=xProjXAve;
      maxYOfXProj=yOfXProj-(xPos.length-1)/2;
    }  
    if (relMinXOfXProj>xProjXAve&&y>tempMaxYOfXProj&&y<h-(xPos.length-1)/2)//finds relative minimum x value of x projection to isolate hand from arm
    {
      relMinXOfXProj=xProjXAve;
      relMinYOfXProj=yOfXProj-(xPos.length-1)/2;
    }  




    if(y>(xPos.length-1)/2) //keeps array in bounds
    {
      xProjection.pixels[xProjXAve+yOfXProj*w-(xPos.length-1)/2*w] = color(0,255,0); //displays average of x positions of pixels contained in array
    }

    xOfXProj=0;//reset x value
    yOfXProj=y; //next y position
  }
}


void xProjectionMax()
{

  fill(255,0,0);
  ellipse(maxXOfXProj*400/w,maxYOfXProj*300/h+300,10,10);
  tempMaxXOfXProj=maxXOfXProj; //stores previus x value of maximum for use in midPoint of hand finder
  maxXOfXProj=0; //clears y coord of maximum x
  tempMaxYOfXProj=maxYOfXProj; //stores previus x value of maximum for use in midPoint of hand finder
  maxYOfXProj=0; //clears y coord of maximum x
}

void  xProjectionRelMin()
{
  fill (0,0,255);
  ellipse(relMinXOfXProj*400/w,relMinYOfXProj*300/h+300,10,10);
  tempRelMinYOfXProj=relMinYOfXProj;
  relMinXOfXProj=w; //clears y coord of maximum x
  relMinYOfXProj=h; //clears y coord of maximum x
} 
