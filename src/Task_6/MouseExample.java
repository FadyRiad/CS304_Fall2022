package Task_6;
import com.sun.opengl.util.FPSAnimator;
import java.awt.*;
import javax.swing.*;
import javax.media.opengl.GLCanvas;

public class MouseExample extends JFrame {

    static FPSAnimator animator=null;
    static GLCanvas glcanvas = null;

    public static void main(String[] args) {
        final MouseExample app = new MouseExample();
        animator = new FPSAnimator(glcanvas,30);
        animator.start();
// show what we've done
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        app.setVisible(true);
                    }
                }
        );
    }

    public MouseExample() {
//set the JFrame title
        super("KeyListener Example");
//kill the process when the JFrame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//create our KeyDisplay which serves two purposes
// 1) it is our GLEventListener, and
// 2) it is our KeyListener
        MouseDisplay md = new MouseDisplay();
//only three JOGL lines of code ... and here they are
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(md);
        glcanvas.addMouseListener(md);
//we'll want this for our repaint requests
        md.setGLCanvas(glcanvas);
//add the GLCanvas just like we would any Component
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        setSize(500, 300);
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

