package CLNUIJavaTest;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.jna.Pointer;

import cl.nui.CLNUI;


/**
 *
 *	Java example showing color and depth data from the Kinect.
 * 	
 * This example was kindly made by Volker Gollücke
 */
public class App2 {


    static JFrame f = new JFrame();
    static JPanel p = new JPanel();
    static JLabel l1 = new JLabel();
    static JLabel l2 = new JLabel();
    private static Pointer motor;
    private static Pointer camera;
    static BufferedImage colorData;
    static BufferedImage depthData;
    static int[] rgb = new int[640 * 480];
    static int[] depth = new int[640 * 480];


    public static void main(String args[]) {
        setup();
        while (true) {
            draw();
            l1.setIcon(new ImageIcon(depthData));
            l2.setIcon(new ImageIcon(colorData));
            l1.repaint();
            l2.repaint();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void setup() {
        p.setLayout(new BorderLayout());
        p.add(l1, BorderLayout.WEST);
        p.add(l2, BorderLayout.EAST);
        p.setSize(1200, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(p);
        f.setSize(1200, 500);
        f.setVisible(true);

        motor = CLNUI.INSTANCE.CreateNUIMotor();
        camera = CLNUI.INSTANCE.CreateNUICamera();
        CLNUI.INSTANCE.StartNUICamera(camera);
        CLNUI.INSTANCE.SetNUIMotorLED(motor, (byte) 7);
        System.out.println("Kinect Serial: "
                + CLNUI.INSTANCE.GetNUIMotorSerial(motor));

        colorData = new BufferedImage(640, 480, BufferedImage.TYPE_INT_BGR);
        depthData = new BufferedImage(640, 480, BufferedImage.TYPE_INT_BGR);
    }

    public static void draw() {
        CLNUI.INSTANCE.GetNUICameraColorFrameRGB32(camera, rgb, 0); 
        colorData.setRGB(0, 0, 640, 480, rgb, 0, 640);
        CLNUI.INSTANCE.GetNUICameraDepthFrameRGB32(camera, depth, 0); 
        depthData.setRGB(0, 0, 640, 480, depth, 0, 640);
    }

}
