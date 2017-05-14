void boundaryNeighbours (int loc) // test nnumber of neighbours that are boundary pixels
{
  boundaryNeighbours = 0;
  boundaryBin(loc-1);
  if(boundaryBin==true)
  {
    boundaryNeighbours++;
  }
  boundaryBin(loc-w);
  if(boundaryBin==true)
  {
    boundaryNeighbours++;
  } 
  boundaryBin(loc+1);
  if(boundaryBin==true)
  {
    boundaryNeighbours++;
  }
  boundaryBin(loc+w);
  if(boundaryBin==true)
  {
    boundaryNeighbours++;
  }
}  
