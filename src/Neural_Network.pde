
void neuralNetwork(float f1,float f2,float f3,float f4,float f5,float v1,float v2,float v3,float v4)
{
  //inputs
  float x1A = f1+0.1;  
  float x2A = f2+0.1;
  float x3A = f3+0.1;
  float x4A = f4+0.1;
  float x5A = f5+0.1;
  float x6A = v1+0.1;
  float x7A = v2+0.1;
  float x8A = v3+0.1;
  float x9A = v4+0.1;



  //initial weights
  //input layer weights
  float w1A = -0.7730226;
  float w1B = 0.7183541;
  float w1C = -0.6928675;
  float w1D = 0.5296034;
  float w1E = 0.8375236;
  float w2A = -0.51678956;
  float w2B = -0.31572336;
  float w2C = -0.8320275;
  float w2D = -0.24141584;
  float w2E = -0.95364016;
  float w3A = -0.5566789;
  float w3B = 0.94730735;
  float w3C = 0.26510268;
  float w3D = 0.8865323;
  float w3E = -0.15867522;
  float w4A = -0.49440315;
  float w4B = 0.7721455;
  float w4C = -0.262894;
  float w4D = 0.94908196;
  float w4E = -0.39958116;
  float w5A = 0.08508696;
  float w5B = 0.27202654;
  float w5C = -1.0197405;
  float w5D = -0.008199202;
  float w5E = -1.0913138;
  float w6A = -0.49093857;
  float w6B = 0.8819983;
  float w6C = 0.10008627;
  float w6D = 1.0063748;
  float w6E = -0.21145786;
  float w7A = 0.44217157;
  float w7B = 0.0989018;
  float w7C = 0.3772578;
  float w7D = 0.3345932;
  float w7E = 0.987472;
  float w8A = 0.89786;
  float w8B = 1.0219779;
  float w8C = -0.3381234;
  float w8D = -0.8775543;
  float w8E = 0.581145;
  float w9A = -0.62561995;
  float w9B = 0.002124509;
  float w9C = -0.54920745;
  float w9D = 0.5327601;
  float w9E = -0.86570156;

  //output layer weights
  float w1 = -0.1945392;
  float w2 = 0.79378915;
  float w3 = -0.32651442;
  float w4 = 0.55492806;
  float w5 = -0.9274519;

  //node inputs and outputs
  float y;
  float y1_in;
  float y1_out;
  float y2_in;
  float y2_out;
  float y3_in;
  float y3_out;
  float y4_in;
  float y4_out;
  float y5_in;
  float y5_out;

String result;

  //euler's number
  float e = (float) Math.E;










  //forward pass and y output 
 
    //calculate hidden layer inputs 
    //calculate hidden layer inputs 
    y1_in = x1A*w1A + x2A*w2A + x3A*w3A + x4A*w4A + x5A*w5A + x6A*w6A + x7A*w7A + x8A*w8A + x9A*w9A;
    y2_in = x1A*w1B + x2A*w2B + x3A*w3B + x4A*w4B + x5A*w5B + x6A*w6B + x7A*w7B + x8A*w8B + x9A*w9B;
    y3_in = x1A*w1C + x2A*w2C + x3A*w3C + x4A*w4C + x5A*w5C + x6A*w6C + x7A*w7C + x8A*w8C + x9A*w9C;
    y4_in = x1A*w1D + x2A*w2D + x3A*w3D + x4A*w4D + x5A*w5D + x6A*w6D + x7A*w7D + x8A*w8D + x9A*w9D;
    y5_in = x1A*w1E + x2A*w2E + x3A*w3E + x4A*w4E + x5A*w5E + x6A*w6E + x7A*w7E + x8A*w8E + x9A*w9E;

    //calclute hidden layer outputs rounded to 4 decs
    y1_out = 1/(1+pow(e,-y1_in));
    y2_out = 1/(1+pow(e,-y2_in));
    y3_out = 1/(1+pow(e,-y3_in));
    y4_out = 1/(1+pow(e,-y4_in));
    y5_out = 1/(1+pow(e,-y5_in));


    // calculate y output to 4 decs
    y = y1_out*w1 + y2_out*w2 + y3_out*w3 + y4_out*w4 +y5_out*w5;
 
//displays result
fill(255);
textFont(ft2,100);
if(y<0.3)
{
 text ("U",w-40,h-50);
}
else if (y>=0.3&&y<0.5)
{
  text ("I",w-40,h-50);
}  
if(y>=0.5&&y<0.7)
{
  text ("4",w-40,h-50);
}
else if (y>=0.7)
{
  text ("5",w-40,h-50);
} 



  //prints the input values, outputs value, output target, pass number and learning rate
 
    println();
    print("_INPUTS");
    println();
    print("x1A = " + x1A);
    println();
    print("x2A = " + x2A);
    println();
    print("x3A = " + x3A);
    println();
    print("x4A = " + x4A);
    println();
    print("x5A = " + x5A);
    println();
    print("x6A = " + x6A);
    println();
    print("x7A = " + x7A);
    println();
    print("x8A = " + x8A);
    println();
    print("x9A = " + x9A);
    println();


    print("Output");
    println();
    print("y_out = " + y);
    println();
    
    
  
}



