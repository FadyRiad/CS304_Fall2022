package Task_6;

import com.sun.opengl.util.*;
import java.awt.*;
import javax.media.opengl.*;
import javax.swing.*;

/**
 *
 * @author Mohamed
 */
/**
 * This is a basic JOGL app. Feel free to reuse this code or modify it.
 */
public class KeyExample extends JFrame {

    static FPSAnimator animator=null;
    static GLCanvas glcanvas = null;



    public static void main(String[] args) {
        final KeyExample app = new KeyExample();

        animator = new FPSAnimator(glcanvas,30);
        animator.start();
// show what we've done
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        app.setVisible(true);
                        glcanvas.requestFocusInWindow();
                    }
                }
        );
    }

    public KeyExample() {
        super("KeyListener Example");
//set the JFrame title
//kill the process when the JFrame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//create our KeyDisplay which serves two purposes
// 1) it is our GLEventListener, and
// 2) it is our KeyListener
        KeyDisplay kd = new KeyDisplay();
//only three JOGL lines of code ... and here they are
        GLCapabilities glcaps = new GLCapabilities();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(kd);
        glcanvas.addKeyListener(kd);
//we'll want this for our repaint requests
        kd.setGLCanvas(glcanvas);
//add the GLCanvas just like we would any Component
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        setSize(500, 500);
//center the JFrame on the screen
        centerWindow(this);

    }

    public void centerWindow(Component frame) {
        Dimension screenSize
                = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();

        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        frame.setLocation(
                (screenSize.width - frameSize.width) >> 1,
                (screenSize.height - frameSize.height) >> 1
        );
    }
}