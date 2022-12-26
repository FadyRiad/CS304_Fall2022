package Task_6;


import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.swing.*;

 class MouseDisplay
        implements GLEventListener, MouseListener {

    int xPosition = 50;
    int yPosition = 50;
    float red = 0.0f;
    float green = 0.5f;
    float blue = 0.5f;
    GLCanvas glc;

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }

    /**
     * Take care of initialization here.
     */
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        red = 0.1f;
        green = 0.1f;
        blue = 0.4f;
        gl.glClearColor(red, green, blue, 1.0f);
        gl.glViewport(0, 0, 100, 100);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(0.0, 100, 0, 100, -1, 1);
    }

    /**
     * Take care of drawing here.
     */
    int i=0;
    int[][] arr =new int[1000][2];
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//Remember point size refers
//to pixels, not the coordinate
//system we've set up in the
//GLCanvas
        gl.glPointSize(6.0f);
        red = 1.0f;
        green = 1.0f;
        blue = 1.0f;
        gl.glColor3f(red, green, blue);
        gl.glBegin(GL.GL_POINTS);
        for(int j=0;j<=i;j++){
            gl.glVertex2i(arr[j][0], arr[j][1]);
        }
        gl.glEnd();
        gl.glColor3f(1.0f, green, blue);
        gl.glBegin(GL.GL_LINES);
        if(i%2==1){
            for(int j=0;j<=i-1;j++){
                gl.glVertex2i(arr[j][0], arr[j][1]);
            }
        }
        else {
            for(int j=0;j<=i;j++){
                gl.glVertex2i(arr[j][0], arr[j][1]);
            }
        }
        gl.glEnd();
    }

    /**
     * Called when the GLDrawable (GLCanvas or GLJPanel) has changed in size. We
     * won't need this, but you may eventually need it -- just not yet.
     */
    public void reshape(
            GLAutoDrawable drawable,
            int x,
            int y,
            int width,
            int height
    ) {
    }

    /**
     * If the display depth is changed while the program is running this method
     * is called. Nowadays this doesn't happen much, unless a programmer has his
     * program do it.
     */
    public void displayChanged(
            GLAutoDrawable drawable,
            boolean modeChanged,
            boolean deviceChanged
    ) {
    }
////////////////////////////////////////////
// MouseListener implementation below

    public void mouseClicked(MouseEvent e) {

        double x = e.getX();
        double y = e.getY();

        System.out.println(x+" "+y);

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        System.out.println(width+" "+height);
//get percent of GLCanvas instead of
//points and then converting it to our
//'100' based coordinate system.
        arr[i][0]=(int) ((x / width) * 100);
        arr[i][1]=100-((int) ((y / height) * 100));
        i++;


//        xPosition = (int) ((x / width) * 100);
//        yPosition = ((int) ((y / height) * 100));
//reversing direction of y axis
//        yPosition = 100 - yPosition;
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}