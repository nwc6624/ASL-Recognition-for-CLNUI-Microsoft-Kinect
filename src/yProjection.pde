 void yProjection (int x,int y,float bl,float re)
    {
     
      if (y==tempRelMinYOfXProj-1&&x==w-1) // clears array caontiang y values of projection 
      {
        for (int i=0; i<yPos.length;i++)
        {
          yPos[i]=0;
        }
      }    
      int sumYPosArray=0;//sum of values in array


      if(bl>250&&re>1&&y<tempRelMinYOfXProj)                                                                                                                                      
      {

        yOfYProj++;
      } 


      if(tempRelMinYOfXProj>(xPos.length-1)/2&&tempRelMinYOfXProj<h-(xPos.length-1)/2&&y==tempRelMinYOfXProj-1)
      {
        
        for (int i=0; i<yPos.length-1;i++) // stores new y value in array and shift previous y values down one place
        {
          yPos[i] = yPos[i+1];
        }

        yPos[yPos.length-1] = yOfYProj;
        println(yOfYProj);

        for (int i=0; i<yPos.length;i++) // sum array
        {
          sumYPosArray= sumYPosArray+ yPos[i];
        }  




        yProjYAve=sumYPosArray/yPos.length; //mean of y values in projection over yPos.length

          if (maxYOfYProj<yProjYAve)
        {
          maxYOfYProj=yProjYAve;
          maxXOfYProj=xOfYProj-(yPos.length-1)/2;
        }  




        if(x>(yPos.length-1)/2) //keeps array in bounds
        {
          
          yProjection.pixels[xOfYProj+yProjYAve*w-(yPos.length-1)/2] = color(0,255,0); //displays average of y positions of pixels contained in array
        }


        yOfYProj=0;//reset x value
        xOfYProj=x; //next y position
      }
     
    }

 void yProjectionMax()
    {

      fill(255,0,0);
      ellipse(maxXOfYProj*400/w,maxYOfYProj*300/h,10,10);
      tempMaxYOfYProj=maxYOfYProj; //stores previus y value of maximum for use in midPoint of hand finder
      maxYOfYProj=0; //clears y coord of maximum Y
      tempMaxXOfYProj=maxXOfYProj; //stores previus x value of maximum for use in midPoint of hand finder
      maxXOfYProj=0; //clears X coord of maximum Y
    }

