package Task_6;
import com.sun.opengl.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.opengl.*;
import javax.swing.*;


public class KeyDisplay implements GLEventListener, KeyListener {

    int xPosition = 0;
    int yPosition = 0;
    float red = 0.0f;
    float green = 0.0f;
    float blue = 1.0f;
    GLCanvas glc;

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }

    /**
     * Take care of initialization here.
     */
    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        red = 0.0f;
        green = 0.0f;
        blue = 1.0f;
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glViewport(0, 0, 50, 50);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-100.0, 100.0, -100.0, 100.0, -1, 1);
    }
    int xr=0,yd=0,yu=0,xl=0,i=1;
    int r=0,step=0;
    double sides=0,StartAngle=0;
    boolean isDecreasing=false,isDecreasing2=false,isDecreasingD=false,isDecreasingU=false,isDecreasingR=false,isDecreasingL=false;
    /**
     * Take care of drawing here.
     */
    public void display(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
//Remember point size refers
//to pixels, not the coordinate
//system we've set up in the
//GLCanvas


        //gl.glPointSize(6.0f);  حجم النقطة
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        drawPolyLinesFilled(gl,10,new Color(1f,1f,0f,1f),360,StartAngle,1,xPosition,yPosition);
        if(xr==1){
            xPosition=(int) (isDecreasingR?xPosition-1:xPosition+1);
            if(xPosition<=0){
                isDecreasingR=false;
                xr=0;
            }
            if(xPosition>=70){
                isDecreasingR=true;
            }
        }
        if(xl==1){
            xPosition=(int) (isDecreasingL?xPosition-1:xPosition+1);
            if(xPosition<=-70){
                isDecreasingL=false;
            }
            if(xPosition>=0){
                isDecreasingL=true;
                xl=0;
            }
        }
        if(yu==1){
            yPosition=(int) (isDecreasingU?yPosition-1:yPosition+1);
            if(yPosition<=0){
                isDecreasingU=false;
                yu=0;
            }
            if(yPosition>=70){
                isDecreasingU=true;
            }
        }
        if(yd==1){
            yPosition=(int) (isDecreasingD?yPosition-1:yPosition+1);
            if(yPosition<=-70){
                isDecreasingD=false;
            }
            if(yPosition>=0){
                isDecreasingD=true;
                yd=0;
            }
        }

        //الخط
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex2i(-100, -10);
        gl.glVertex2i(350, -10);
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
    )
    {

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
////////////////////////////////////
// KeyListener implementation below

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar()+"\t"+e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            yu=1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            yd=1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xl= 1;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xr=1;
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_8) {
            yu=1;
        } else if (e.getKeyChar() == KeyEvent.VK_2) {
            yd=1;
        } else if (e.getKeyChar() == KeyEvent.VK_4) {
            xl= 1;
        } else if (e.getKeyChar() == KeyEvent.VK_6) {
            xr=1;
        }
    }
    void drawPolyLinesFilled(GL gl,int r,Color c,double sides,double StartAngle,int step,int x,int y){
        gl.glColor3fv(c.getColorComponents(null), 0);
        gl.glBegin(GL.GL_POLYGON);
        for (double i = StartAngle; i < 360*step+StartAngle; i+=step*360.0/sides) {
            gl.glVertex2d(x+(r * (Math.cos(Math.toRadians( i)))), y+(r * (Math.sin(Math.toRadians(i)))));
        }
        gl.glEnd();
    }
}