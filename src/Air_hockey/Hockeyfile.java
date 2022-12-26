package Air_hockey;

import Air_hockey.AnimListener;
import Air_hockey.TextureReader;
import java.awt.event.MouseListener;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.media.opengl.*;
import java.util.BitSet;
import javax.media.opengl.glu.GLU;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Hockeyfile extends AnimListener implements MouseMotionListener, MouseListener, KeyListener {

    AudioInputStream audioStream;
    Clip clip;
    GL gl;
    int maxWidth = 400, maxHeight = 600;
    static boolean sound = true, start = true, bot;
    static int pos_x1 = 180, pos_y1 = 60, pos_x2 = 180, pos_y2 = 490; // postion of player 1, 2
    static int pos_bx = 180, pos_by = 270; // postion of ball
    static int speedX, speedY, bot_speed, speed = 15, mins_speed = 3, win = 10, direction, score1, score2, i, j, k;
    static int page;

    TextRenderer ren = new TextRenderer(new Font("sanaSerif", Font.BOLD, 10));
    rectangle ball;

    String textureNames[] = {"home1.png", "home2.png", "game.png", "player1.png", "player2.png", "ball.png", "pause1.png", "pause2.png", "instructions.png", "LEVELS.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    @Override
    public void init(GLAutoDrawable gld) {

        try {

            audioStream = AudioSystem.getAudioInputStream(new File("Assets//song.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }

    }

    @Override
    public void display(GLAutoDrawable gld) {

        gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        switch (page) {
            case 0:
                DrawBackground(0); // home1
                break;
            case 1:
                DrawBackground(1); // home2 mute
                break;
            case 2:
                DrawBackground(8);//Help
                break;
            case 3:
                DrawBackground(9);
                break;//Difficulty
            case 4: // play
                DrawBackground(2);
                Draw(pos_x1, pos_y1, 0.15f, 0.1f, 3);// x , y , scaleX , scaleY , index
                Draw(pos_x2, pos_y2, 0.15f, 0.1f, 4);
                Draw(pos_bx, pos_by, 0.1f, 0.06f, 5);
                drawscore();

                start();
                wins();
                finish();
                mins_speed();

                if (!start) {
                    pos_bx += speedX;
                    pos_by += speedY;
                }
                ball = new rectangle(pos_bx - 5, pos_by + 15, 30, 30);

                if(bot && !start){
                    bot();
                }

                wall();
                hit();
                break;
            case 5:
                DrawBackground(6);//pause
                break;
            case 6:
                DrawBackground(7);//pause if mute
                break;

        }

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {

    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {

    }

    public void DrawBackground(int i) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_POLYGON);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void Draw(int x, int y, float scaleX, float scaleY, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On
        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(scaleX, scaleY, 1);

        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(e.getX() + " " + (550-e.getY()));
        if (!start) {
            if (e.getX() >= 20 && e.getX() <= 330) {
                pos_x1 = e.getX();

            }
            if (e.getY() >= 280 && e.getY() <= 535) {
                pos_y1 = 550 - e.getY();

            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (page) {
            case 0:
            case 1:
                // System.out.println(e.getX() + " " + (e.getY()));
                if (e.getX() > 90 && e.getX() < 290 && e.getY() > 290 && e.getY() < 315) { //x and y of 2 players
                    bot = false;
                    page = 4; // game
                } else if (e.getX() > 135 && e.getX() < 240 && e.getY() > 335 && e.getY() < 380) {
                    page = 2; // help
                } else if (e.getX() > 145 && e.getX() < 235 && e.getY() > 400 && e.getY() < 430) {
                    System.exit(0);
                } else if (e.getX() > 305 && e.getX() < 340 && e.getY() > 50 && e.getY() < 85) {
                    sound = !sound;
                    sound();
                } else if (e.getX() > 100 && e.getX() < 280 && e.getY() > 240 && e.getY() < 270) {
                    page = 3;
                    bot = true;//difficulty page
                }
                break;
            case 2:
                if (e.getX() > 20 && e.getX() < 110 && e.getY() > 30 && e.getY() < 60) {//back in help
                    page = sound ? 0 : 1;
                }
                break;
            case 3:
                if (e.getX() > 270 && e.getX() < 350 && e.getY() > 60 && e.getY() < 100) {
                    page = sound ? 0 : 1;//back to menu
                } else if (e.getX() > 65 && e.getX() < 310 && e.getY() > 265 && e.getY() < 315) {
                    bot_speed = 3;//easy
                    page = 4;
                } else if (e.getX() > 65 && e.getX() < 310 && e.getY() > 330 && e.getY() < 375) {
                    bot_speed = 5;//medium
                    page = 4;
                } else if (e.getX() > 65 && e.getX() < 310 && e.getY() > 400 && e.getY() < 445) {
                    bot_speed = 7;//hard
                    page = 4;
                }
                break;
            case 5:
            case 6:
                if (e.getX() > 75 && e.getX() < 310 && e.getY() > 235 && e.getY() < 300) {
                    page = 4; // game

                } else if (e.getX() > 80 && e.getX() < 315 && e.getY() > 370 && e.getY() < 435) {
                    page = sound ? 0 : 1;
                    score1 = 0;
                    score2 = 0;
                    defult();//main menu
                }

                if (e.getX() > 300 && e.getX() < 345 && e.getY() > 20 && e.getY() < 60) {
                    sound = !sound;
                    sound();//mute button

                }
                break;

        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    BitSet keybits = new BitSet(256);

    @Override
    public void keyPressed(KeyEvent e) {
        keybits.set(e.getKeyCode());

        if (!start && !bot) {//player 2
            if ((keybits.get(KeyEvent.VK_RIGHT) || keybits.get(KeyEvent.VK_D)) && pos_x2 <= 310) {
                pos_x2 += 30;
            }
            if ((keybits.get(KeyEvent.VK_LEFT) || keybits.get(KeyEvent.VK_A)) && pos_x2 >= 40) {
                pos_x2 -= 30;
            }
            if ((keybits.get(KeyEvent.VK_UP) || keybits.get(KeyEvent.VK_W)) && pos_y2 <= 500) {
                pos_y2 += 30;
            }
            if ((keybits.get(KeyEvent.VK_DOWN) || keybits.get(KeyEvent.VK_S)) && pos_y2 >= 310) {
                pos_y2 -= 30;
            }
        }
        if (keybits.get(KeyEvent.VK_Q)) {
            pos_bx = 180;
            pos_by = 270;
            speedX = 0;
            speedY = 0;
        }
        if ((keybits.get(KeyEvent.VK_P) || keybits.get(KeyEvent.VK_ESCAPE)) && page == 4) {
            sound();
            start = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        keybits.clear(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public boolean collision_left() {
        return (ball.intersect(new rectangle(pos_x1 - 20, pos_y1 + 15, 15, 15)) // (shifting the rec then gives it the width and lenght it needs)
                || ball.intersect(new rectangle(pos_x2 - 20, pos_y2 + 15, 15, 15)));
    }

    public boolean collision_up_left() {
        return ball.intersect(new rectangle(pos_x1 - 20, pos_y1 + 40, 20, 20))
                || ball.intersect(new rectangle(pos_x2 - 20, pos_y2 + 40, 20, 20));
    }

    public boolean collision_up() {
        return ball.intersect(new rectangle(pos_x1 + 5 , pos_y1 + 40, 15, 15))
                || ball.intersect(new rectangle(pos_x2 + 5 , pos_y2 + 40, 15, 15));
    }

    public boolean collision_up_right() {
        return ball.intersect(new rectangle(pos_x1 + 15, pos_y1 + 40, 20, 20))
                || ball.intersect(new rectangle(pos_x2 + 15, pos_y2 + 40, 20, 20));
    }

    public boolean collision_right() {
        return ball.intersect(new rectangle(pos_x1 + 20, pos_y1 + 15, 15, 15))
                || ball.intersect(new rectangle(pos_x2 + 20, pos_y2 + 15, 15, 15));
    }

    public boolean collision_down_right() {
        return ball.intersect(new rectangle(pos_x1 + 15, pos_y1 , 20, 20))
                || ball.intersect(new rectangle(pos_x2 + 15, pos_y2 , 20, 20));
    }

    public boolean collision_down() {
        return ball.intersect(new rectangle(pos_x1 + 5 , pos_y1 , 15, 15))
                || ball.intersect(new rectangle(pos_x2 + 5 , pos_y2 , 15, 15));
    }

    public boolean collision_down_left() {
        return ball.intersect(new rectangle(pos_x1 - 20, pos_y1 , 20, 20))
                || ball.intersect(new rectangle(pos_x2 - 20, pos_y2 , 20, 20));

    }

    public void hit() {
        if (collision_left()) {
            speedX = -speed;
            speedY = 0;
            direction = 1;
        }

        if (collision_up_left()) {
            speedX = -speed;
            speedY = speed;
            direction = 2;
        }
        if (collision_up()) {
            speedX = 0;
            speedY = speed;
            direction = 3;
        }
        if (collision_up_right()) {
            speedX = speed;
            speedY = speed;
            direction = 4;
        }

        if (collision_right()) {
            speedX = speed;
            speedY = 0;
            direction = 5;
        }
        if (collision_down_right()) {
            speedX = speed;
            speedY = -speed;
            direction = 6;
        }
        if (collision_down()) {
            speedX = 0;
            speedY = -speed;
            direction = 7;
        }
        if (collision_down_left()) {
            speedX = -speed;
            speedY = -speed;
            direction = 8;
        }

    }

    public void wall() {

        switch (direction) {
            case 1: // left
                if (pos_bx <= 20) {
                    speedX *= -1;
                    speedX -= mins_speed;
                    direction = 5; // right
                    if (bot && pos_by > 270) {
                        speedY = mins_speed - speedY;
                        direction = 6;
                    }  //if the bot stuck with the ball
                }
                break;
            case 2:  // up-left
                if (pos_bx <= 20) {
                    speedX *= -1;
                    speedX -= mins_speed;

                    speedY -= mins_speed;
                    direction = 4; // up-right
                }
                if (pos_by >= 535) {
                    speedY *= -1;
                    speedX += mins_speed;
                    speedY += mins_speed;
                    direction = 8; // down-left
                }
                break;
            case 3: //up
                if (pos_by >= 535) {
                    speedY *= -1;
                    speedY += mins_speed;
                    direction = 7; // down
                    if (bot && pos_by > 270) {
                        speedY = mins_speed - speedY;
                        direction = 6;
                    }
                }
                break;
            case 4: // up-right
                if (pos_by >= 535) {
                    speedY *= -1;
                    speedX -= mins_speed;
                    speedY += mins_speed;
                    direction = 6; // down-right
                }
                if (pos_bx >= 350) {
                    speedX *= -1;
                    speedX += mins_speed;
                    speedY -= mins_speed;
                    direction = 2; // up-left
                }
                break;
            case 5: // right
                if (pos_bx >= 350) {
                    speedX *= -1;
                    speedX += mins_speed;
                    direction = 1; // left
                    if (bot && pos_by > 270) {
                        speedY = mins_speed - speedY;
                        direction = 8;
                    }
                }
                break;
            case 6: // down-right
                if (pos_bx >= 350) {
                    speedX *= -1;
                    speedX += mins_speed;
                    speedY += mins_speed;
                    direction = 8; // down-left
                }
                if (pos_by <= 15) {
                    speedY *= -1;
                    speedX -= mins_speed;
                    speedY -= mins_speed;
                    direction = 4; // up-right
                }
                break;
            case 7: // down
                if (pos_by <= 15) {
                    speedY *= -1;
                    speedY -= mins_speed;
                    direction = 3; // up
                }
                break;
            case 8: // down-left
                if (pos_by <= 15) {
                    speedY *= -1;
                    speedX += mins_speed;
                    speedY -= mins_speed;
                    direction = 2; // up-left
                }
                if (pos_bx <= 20) {
                    speedX *= -1;
                    speedX -= mins_speed;
                    speedY += mins_speed;
                    direction = 6; // down-right
                }
                break;

        }

    }

    public void wins() {
        boolean f = false;
        if (pos_bx > 120 && pos_bx < 250) {
            if (pos_by >= 535 || pos_by <= 15) {

                if (pos_by >= 535) {
                    f = true;
                }
                j++;
                ren.beginRendering(100, 100);
                if (f) {
                    ren.setColor(Color.RED);
                } else {
                    ren.setColor(Color.BLUE);
                }
                ren.draw("GOAL!", 33, 48);
                ren.setColor(Color.WHITE);
                ren.endRendering();
                speedX = 0;
                speedY = 0;
                if (j > 60) {
                    if (f) {
                        score2++;
                    } else {
                        score1++;
                    }

                    defult();
                    j = 0;
                }

            }
        }
    }

    public void drawscore() {
        ren.beginRendering(120, 120);
        ren.setColor(Color.BLUE);
        ren.draw(score1 + "", 100, 65);
        ren.setColor(Color.RED);
        ren.draw(score2 + "", 100, 50);
        ren.setColor(Color.WHITE);
        ren.endRendering();
    }

    public void drawready() {
        ren.beginRendering(100, 100);
        ren.setColor(Color.RED);
        ren.draw("READY?", 30, 48);

        ren.setColor(Color.WHITE);
        ren.endRendering();
    }

    public void drawstart() {
        ren.beginRendering(100, 100);
        ren.setColor(Color.BLUE);
        ren.draw("GOOOO!", 30, 48);

        ren.setColor(Color.WHITE);
        ren.endRendering();
    }

    public void sound() {
        if (sound) {
            page = page == 1 ? 0 : 5;//main menu and pause with sound
            clip.start();
        } else {
            page = page == 0 ? 1 : 6;//main menu and pasue without sound
            clip.stop();
        }
    }

    public void start() {
        if (start) {

            if (i < 30) {
                drawready();
            } else {
                drawstart();
            }
            i++;
            if (i > 60) {
                start = false;
                i = 0;
            }

        }
    }

    public void finish() {
        if (score1 == win || score2 == win) {
            k++;

            if (k < 120) {
                drawfinish();
            } else {
                k = 0;
                page = sound ? 0 : 1;
                start = true;
                score1 = 0;
                score2 = 0;
                defult();

            }
        }
    }

    public void drawfinish() {

        ren.beginRendering(100, 100);
        if (score1 == win) {
            ren.setColor(Color.BLUE);
            ren.draw("BLUE WINS!", 24, 48);

        } else {
            ren.setColor(Color.RED);
            ren.draw("RED WINS!", 25, 48);
        }
        ren.setColor(Color.WHITE);
        ren.endRendering();

    }

    public void bot() {
        double c = 0;

        if (pos_bx < 335 && pos_bx > 35) {
            c = 1.5; //bot movement with the ball direction
        } else {
            if (pos_x2 > 20 && pos_x2 < 350) {
                c = -1.5;//condition if the bot reached the wall return in the opposite direction
            }
        }
        if (pos_bx > pos_x2) {
            pos_x2 += c * bot_speed; //if the ball direction was his right
        }
        if (pos_bx < pos_x2) {
            pos_x2 -= c * bot_speed;//if the ball direction was his left
        }

        if (pos_by > 270 && pos_by < pos_y2) {
            pos_y2 -= bot_speed;//to play only in his side
        } else if (pos_y2 < 490) {
            pos_y2 += bot_speed;//to return to his original position if the ball was not in his side
        }

    }
    public void mins_speed(){  //function to decrease the speed of the ball
        if (speedX == mins_speed || speedY == mins_speed
                || speedX == -mins_speed || speedY == -mins_speed) {
            i++;
            if (i > 30) {
                speedX = 0;
                speedY = 0;
                i = 0;
            }
        }
    }
    public void defult() {
        pos_x1 = 180;
        pos_y1 = 60;
        pos_x2 = 180;
        pos_y2 = 490;
        pos_bx = 180;
        pos_by = 270;
        speedX = 0;
        speedY = 0;

    }
}

class rectangle {

    int lx, ly, rx, ry;

    rectangle(int x, int y, int width, int height) {
        lx = x;
        ly = y;
        rx = width + x;
        ry = y - height;
    }

    boolean intersect(rectangle r) {

        if (lx > r.rx || r.lx > rx) {   //check if the rectangle is on the left side or the right side of the other rectangle
            return false;    //if not then go to the next condition
        }
        if (ry > r.ly || r.ry > ly) {  //check if the rectangle is above or under the other rectangle
            return false;   //if false then they are intersected
        }

        return true;
    }

}
