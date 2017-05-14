void edge(int maxPoint)
{

  boolean change=false;
  boolean complete=false;
  boolean firstSearch=true;
  int loc=maxPoint; //location
  int up=-w;
  int down=w;
  int left=-1;
  int right=1;
  int r = (int) random (0,255); //red
  int g = (int) random (0,255); //green
  int b = (int) random (0,255); //blue
  int locP1 =0;// 1st previous loc positon
  int locP2 =0;// 2nd previous loc 
  int locP3 =0;// 3rd previous loc 
  int locP4 =0;// 4th previous loc 
  boolean junction =false; // true if there is a junction
  int junction1=1; //1st previous junction positon
  int junction1Prev=2;//position before junction position
  int junction1Post=3;//position after most recent junction

  int  junction2=3;// 2nd previous junciton posiiton
  int  junction2Prev=4; //2dn previous position before junction position
  int  junction2Post=5; //position after 2nd last junction junction position

  int  junction3=6;// 3rd previous junciton posiiton
  int  junction3Prev=7; //3rd previous position before junction position
  int  junction3Post=8; //position after 3rd last junction position

  int k=0; //edge array index
  int xPoint=0; //x point of vertex on smoothed hand
  int yPoint=0; //y point of vertex on smoothed hand
  int counter =1; //counts numberof indexes in edgeSmooth
  int   centroidX=0;
  int  centroidY=0;

  int aveX=0;
  int aveY=0;

  //to calculate angles between points

  int pass =0; //corresponds to previous m for identified point/valley
  int xPointP4=0;  //2 previous x and y positions to find anles
  int   yPointP4=0;

  int  xPointP3=0;
  int  yPointP3=0;   
  int xPointP2=0;  //2 previous x and y positions to find anles
  int   yPointP2=0;

  int  xPointP1=0;
  int  yPointP1=0;
  float angleB=0; //angle abc between last three points 

  int [] fingertip = new int  [7]; // array and indexes to store fingertip and valley points
  int fin =0;
  int [] valley = new int  [7];
  int val =0;
  int [] fingerDir=new int [7];
  
  float [] valleyNN = new float  [7]; //distances for neural network
  float [] fingertipNN=new float [7];

  int flagTip=0; //test if a fingertip has been detected

  int xTipAve=0; //average of previous x fingertip coords for points in a small space
  int tipX =0; //last x coord of fingertip 
  int yTipAve=0;
  int tipY =0;

  int    xPointP12=0;   //previous tip coordiantes for proximate tips
  int    xPointP11=0;
  int  yPointP12=0; 
  int  yPointP11=0;
  int avePass=1;
  
  
  int testXAve=0; //average loc of test for tips
  int testYAve=0;
  int testY1=0; //test coordiantes stored in the first instance before averaging
  int testX1=0;
 int   testXP2=0; //previous tip test for proximate tips
 int        testXP1=0;
   int      testYP2=0;
   int      testYP1=0;





  for(int i=1;complete==false&&i<w*3;i++)
  {



    //boundary neighbour check
    boundaryNeighbours(loc);// checks how many neighbour pixels are also edge pixels above,below left and right (max4)
    if (boundaryNeighbours>2)
    {
      junction3=junction1; //3rd previous junction
      junction3Prev=junction1Prev;//pixel before 3rd previous junction
      junction2=junction1; //2nd previous junction
      junction2Prev=junction1Prev;//pixel before 2nd previous junction
      junction1=loc; //pixel is a juncton
      junction1Prev=locP1; //edge pixel before first junction pixels
      //        r= (int) random(0,255);
      //        g= (int) random(0,255);
      //        b= (int) random(0,255);
      junction = true;
    }
    //left
    boundaryNeighbours(loc+left);//tests for dead end (if<2)
    boundaryBin(loc+left); //check if left pixel is part of edge
    if (boundaryBin==true&&boundaryNeighbours>1&&loc+left!=locP1&&loc+left!=junction1Post) // if so  colour it
    {
      locP4=locP3;
      locP3=locP2;
      locP2=locP1;
      locP1=loc;
      loc=loc+left;
      // handTrace.pixels[loc]=color(r,g,b);
      edge[k] = loc;
      k++;
      change=true;
      prnt(loc,i);
      if (junction==true) //test if previos position was junction and remembers position after last 3 junctions
      {
        junction3Post=junction2Post;
        junction2Post=junction1Post;
        junction1Post=loc;
        junction=false;
      }
    }
    if (firstSearch==false&&loc==maxPoint)
    {
      complete=true;
    }


    // up

    //boundary neighbour check
    boundaryNeighbours(loc);// checks how many neighbour pixels are also edge pixels above,below left and right (max4)
    if (boundaryNeighbours>2)
    {
      junction3=junction1; //3rd previous junction
      junction3Prev=junction1Prev;//pixel before 3rd previous junction
      junction2=junction1; //2nd previous junction
      junction2Prev=junction1Prev;//pixel before 2nd previous junction
      junction1=loc; //pixel is a juncton
      junction1Prev=locP1; //edge pixel before first junction pixels
      //        r= (int) random(0,255);
      //        g= (int) random(0,255);
      //        b= (int) random(0,255);
      junction = true;
    }
    //up
    boundaryNeighbours(loc+up); //tests for dead end (if<2)
    boundaryBin(loc+up); //check if up pixel is part of edge
    if (boundaryBin==true&&boundaryNeighbours>1&&loc+up!=locP1&&loc+up!=junction1Post) // if so  colour it
    {
      locP4=locP3;
      locP3=locP2;
      locP2=locP1;
      locP1=loc;
      loc=loc+up;
      // handTrace.pixels[loc]=color(r,g,b);
      edge[k] = loc;
      k++;

      if (junction==true) //test if previos position was junction and remembers position after last 3 junctions
      {
        junction3Post=junction2Post;
        junction2Post=junction1Post;
        junction1Post=loc;
        junction=false;
      }
    }
    if (firstSearch==false&&loc==maxPoint)
    {
      complete=true;
    }


    //right

    //boundary neighbour check
    boundaryNeighbours(loc);// checks how many neighbour pixels are also edge pixels above,below left and right (max4)
    if (boundaryNeighbours>2)
    {
      junction3=junction1; //3rd previous junction
      junction3Prev=junction1Prev;//pixel before 3rd previous junction
      junction2=junction1; //2nd previous junction
      junction2Prev=junction1Prev;//pixel before 2nd previous junction
      junction1=loc; //pixel is a juncton
      junction1Prev=locP1; //edge pixel before first junction pixels
      //        r= (int) random(0,255);
      //        g= (int) random(0,255);
      //        b= (int) random(0,255);
      junction = true;
    }
    //right
    boundaryNeighbours(loc+right);//tests for dead end (if<2)
    boundaryBin(loc+right); //check if right pixel is part of edge
    if (boundaryBin==true&&boundaryNeighbours>1&&loc+right!=locP1&&loc+right!=junction1Post) // if so  colour it
    {
      locP4=locP3;
      locP3=locP2;
      locP2=locP1;
      locP1=loc;
      loc=loc+right;
      // handTrace.pixels[loc]=color(r,g,b);
      edge[k] = loc;
      k++;
      change=true;
      prnt(loc,i);
      if (junction==true) //test if previos position was junction and remembers position after last 3 junctions
      {
        junction3Post=junction2Post;
        junction2Post=junction1Post;
        junction1Post=loc;
        junction=false;
      }
    }
    if (firstSearch==false&&loc==maxPoint)
    {
      complete=true;
    }

    //down

    // boundary neighbour check
    boundaryNeighbours(loc);// checks how many neighbour pixels are also edge pixels above,below left and right (max4)
    if (boundaryNeighbours>2)
    {
      junction3=junction1; //3rd previous junction
      junction3Prev=junction1Prev;//pixel before 3rd previous junction
      junction2=junction1; //2nd previous junction
      junction2Prev=junction1Prev;//pixel before 2nd previous junction
      junction1=loc; //pixel is a juncton
      junction1Prev=locP1; //edge pixel before first junction pixels
      //        r= (int) random(0,255);
      //        g= (int) random(0,255);
      //        b= (int) random(0,255);
      junction = true;
    }


    //down
    boundaryNeighbours(loc+down);//tests for dead end (if<2)
    boundaryBin(loc+down); //check if down pixel is part of edge
    if (boundaryBin==true&&boundaryNeighbours>1&&loc+down!=locP1&&loc+down!=junction1Post) // if so  colour it
    {
      locP4=locP3;
      locP3=locP2;
      locP2=locP1;
      locP1=loc;
      loc=loc+down;
      //handTrace.pixels[loc]=color(r,g,b);
      edge[k] = loc;
      k++;
      change=true;
      prnt(loc,1);
      if (junction==true) //test if previos position was junction and remembers position after last 3 junctions
      {
        junction3Post=junction2Post;
        junction2Post=junction1Post;
        junction1Post=loc;
        junction=false;
      }
    }
    if (loc==maxPoint)//search is complete
    {
      complete=true;
    }

    if (loc==locP4||loc==junction1||loc==junction2) //search is stuck so start again
    {
      complete=true;
    }
  }

  //displays averaged trace array 
  int f=25; //filter size
  int freq=15; //frequency of samples from fitlered array (edgeSmooth)


  beginShape();

  for (int m=(f-1)/2;m<k-(f+1)/2;m=m+freq)
  {
    aveX=0;
    aveY=0;

    for (int n=-1*(f-1)/2;n<(f+1)/2;n++)
    {
      aveX=edge[m+n]%w+aveX;
      aveY=edge[m+n]/w+aveY;
    }
    aveX=aveX/f;
    aveY=aveY/f;
    edgeSmooth[m]=aveX+aveY*w;


    //prevSmooth=edgeSmooth[m]
    stroke (255);
    fill (255);




    xPointP2=xPointP1;   //2 previous x and y positions to find angles
    yPointP2=yPointP1;
    xPointP1=xPoint;
    yPointP1=yPoint;
    xPoint=edgeSmooth[m]%w;
    yPoint=edgeSmooth[m]/w;

    float  ab = dist (xPoint,yPoint,xPointP1,yPointP1);   //finds angle between three previous points
    float  bc = dist (xPointP1,yPointP1,xPointP2,yPointP2);    
    float ac = dist (xPoint,yPoint,xPointP2,yPointP2);
    angleB = acos((ab*ab+bc*bc-ac*ac)/(2*ab*bc));
  

    fill(10);
    curveVertex (xPoint*800/w,yPoint*600/h); //draws line between every x points in array




    centroidX=centroidX+xPoint;   //finds average x value of vertices for centroid
    centroidY=centroidY+yPoint;  // finds ave y"""" 
    counter++;

    //finds fingertips
    if(angleB<2.2&&yPointP1<maxPoint/w-20)    
    {

      int acAveX=(xPoint+xPointP2)/2;   // average of x coords of point next to identified angle point
      int acAveY= (yPoint+yPointP2)/2;// average of y coords of point next to identified angle point


      int P1DiffX = 5*(xPointP1-acAveX)/2; //find the difference between the middle point and the points on either side
      int P1DiffY = 5*(yPointP1-acAveY)/2;

      int testX=xPointP1+P1DiffX; //find pixel to test for fingertip or valley
      int testY=yPointP1+P1DiffY;

      float bl = blue (handFat.pixels[testX+testY*w]);// test the pixel a differences length past the P1 ie further in to the hand for valleys and further out from the finger for fingertips


      if (m<pass+40&&flagTip==1&&fin>0) // remembers history of fingertip points that are close together
      {

        xPointP12=xPointP11; 
        xPointP11=xPointP1;
        yPointP12=yPointP11; 
        yPointP11=yPointP1;
        
        testXP2=testXP1;
        testXP1=testX;
        testYP2=testYP1;
        testYP1=testY;
        
        avePass++;
      }  



      if (m>pass+40) //prevents more than one point being identified every x coordinates along edge
      {   
        if(avePass>1) //find average of fingertip pixels that are close together
        {
          xTipAve= (tipX+xPointP11+xPointP12)/avePass;
          yTipAve= (tipY+yPointP11+yPointP12)/avePass;
          testXAve= (testX1+testXP1+testXP2)/avePass;
          testYAve= (testY1+testYP1+testYP2)/avePass;
          
          fingertip[fin-1]=xTipAve+yTipAve*w;
          fingerDir[fin-1]=testXAve+testYAve*w;
          avePass=1;
          xPointP12=0; 
          xPointP11=0;
          yPointP12=0; 
          yPointP11=0;
           testXP2=0;
         testXP1=0;
        testYP2=0;
        testYP1=0;
       
          
          
        }

        if (bl==0&&fin<5) //finds fingertips
        {
          tipX=xPointP1;
          tipY= yPointP1;
          fingertip[fin] = xPointP1+yPointP1*w;
          testX1=testX;
          testY1=testY;
          fingerDir[fin]= testX+testY*w;
          fin++;
          flagTip=1;
        }


        if(bl==255&&val<4&&flagTip==1) //finds valleys
        {
          valley[val] = xPointP1+yPointP1*w;
          val++;
          flagTip=0;
        }
        pass=m;
      }
    }
  }

  curveVertex(edgeSmooth[(f-1)/2]%w*800/640,edgeSmooth[(f-1)/2]/w*600/480); //draws line between first and last point
  endShape(CLOSE);
  centroidX=centroidX/counter*800/640;   
  centroidY=centroidY/counter*600/480;
  fill(255,0,0);
  ellipse(centroidX,centroidY,10,10);

  for(int i=0; i<fin+1;i++) //displays fingertips and direction
  {
    noStroke();
    fill(255,0,0);
    ellipse (fingertip[i]%w*800/w,fingertip[i]/w*600/h,10,10);
   fill(0);
    ellipse (fingertip[i]%w*800/w,fingertip[i]/w*600/h,6,6);
    
    if(fingertip[i]>0)
    {
     stroke(170,10,200); 
    line(fingertip[i]%w*800/w,fingertip[i]/w*600/h,fingerDir[i]%w*800/w,fingerDir[i]/w*600/h);
    float distanceF = (dist(centroidX,centroidY,fingertip[i]%w*800/w,fingertip[i]/w*600/h)/230)*0.8; //normalised between 0.1 and 0.9 for NN
    fingertipNN[i]=distanceF;
   
    stroke(0,255,255);
    line(centroidX,centroidY,fingertip[i]%w*800/w,fingertip[i]/w*600/h);
    }
    fill(0,255,255);
     textFont(ft,18);
    text (i+1,(fingerDir[i]%w+10)*800/w,(fingerDir[i]/w)*600/h);
    fill (100,100,180);
    ellipse (fingerDir[i]%w*800/w,fingerDir[i]/w*600/h,2,2);
  }
  
  for(int i=0; i<val+1;i++) //displays valleys
  {
    if(valley[i]>0)
    {
    fill(230,180,40);
    noStroke();
    ellipse (valley[i]%w*800/w,valley[i]/w*600/h,5,5);
    fill(0);
    ellipse (valley[i]%w*800/w,valley[i]/w*600/h,2,2);
    fill(0,255,0);
    text (i+1,(valley[i]%w+10)*800/w,(valley[i]/w)*600/h);
    stroke(0,255,0);
    line(centroidX,centroidY,valley[i]%w*800/w,valley[i]/w*600/h);
    float distanceV = (dist(centroidX,centroidY,valley[i]%w*800/w,valley[i]/w*600/h)/230)*0.8; //normalised between 0.1 and 0.9 for NN
    valleyNN[i]=distanceV;
    }
  } 
  
  if(analyse==true) //runs nerual network once
  {
  neuralNetwork(fingertipNN[0],fingertipNN[1],fingertipNN[2],fingertipNN[3],fingertipNN[4],valleyNN[0],valleyNN[1],valleyNN[2],valleyNN[3]); 
  analyse =false;
  }
  //clears arrays storing tips and valleys
   for(int i=0; i<fin+1;i++)
  {
    
    fingertip[i]=0;
    fingertipNN[i]=0;
  }  
  for(int i=0; i<val+1;i++)
  {
    valley[i]=0;
    valleyNN[i]=0;
  }
}

