
void handFat(int loc)
{
  boundary(loc);
  if(boundary==true)
  {
    handFat.pixels[loc]=color(0);
  }  
    
  
   int expansion =7 ;
  if(boundary==false&&loc%w>(expansion-1)/2&&loc/w>(expansion-1)/2) //fattens hand
  {
    for (int x=-1*(expansion-1)/2;x<(expansion+1)/2;x++)
    { 
      for (int y=-1*(expansion-1)/2;y<(expansion+1)/2;y++)
      {

        handFat.pixels[loc+x+y*w]=color(255,255,255);
        maxPoint=loc; //this should be loc+x+y*w not sure why it works like this
      }
    }
  }
}

