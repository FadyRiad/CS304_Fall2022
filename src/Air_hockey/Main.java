package Air_hockey;

import com.sun.opengl.util.*;
import java.awt.*;
import javax.media.opengl.*;
import javax.swing.*;

 class Air_hockey extends JFrame {

    static GLCanvas glcanvas = null;
    public static Animator animator;

    public static void main(String[] args) {
        new Air_hockey();

    }

    public Air_hockey() {
        Hockeyfile listener = new Hockeyfile();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);

        glcanvas.addMouseMotionListener(listener);
        glcanvas.addMouseListener(listener);
        glcanvas.addKeyListener(listener);

        add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(50);
        animator.add(glcanvas);
        animator.start();

        setTitle("AirHockey");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}
