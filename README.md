# ASL-Recognition-for-CLNUI-Microsoft-Kinect
Most sensors of the 21st century use either the Charged Coupled Device (CCD) or the Complementary Metal-oxide-semiconductor (CMOS) technology due to the vivacity and color clarity offered. There are several monocular and binocular hues for depth sensory along with infrared technologies in common practice, yet there currently stands no universally precise method for depth interpretation visually for machine vision/learning.  The Kinect, with its infrared sensor, delivers a 640*480 depth-map static image via live feed that produces not only a relatively accurate depth sensor, but also background detection and extraction, and even blob detection for moving aspects, more specifically the user. 

This project is an attempt to utilize the Processing language along with the CLNUI libraries for kinect to recognize ASL gestures within the Kinect's input. 

Since we are dealing with specifically the hands of the user, hand extraction had to be implemented, and the following concept was used due to having previous success shown within other builds:

•	Hand and wrist detection and extraction

•	Hand isolation from wrist via X and Y axis from creating projections

•	Filtering to remove extra pixels, blank areas/distortions, and perimeter

•	When completed, image was smoothed to perform trace search with an averaging lenses, and removing layer due to enlargement

•	Search algorithm used for redefinition of perimeter

•	Features of hand defined by “Seeing” the angle between three points on the smoothed trace to determine if a threshold requirement was met

•	Neural network designed to take and classify inputs as static ASL hand gesture library
