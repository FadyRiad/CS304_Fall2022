package Tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Tap extends JFrame implements ActionListener , MouseMotionListener {
    private final JButton b1;
    private final JButton b2;
    private final JButton b3;
    private final JButton b4;
    private final JLabel l1;
    private final Checkbox c1;
    private final JTextArea t1;
    private final JTextField t2;




    public Tap(){
        b1 = new JButton("b1");
        b2 = new JButton("b2");
        b3 = new JButton("b3");
        b4 = new JButton("b4");
        l1 = new JLabel("l1");
        c1 = new Checkbox("c1");
        t1 = new JTextArea(" ");
        t2 = new JTextField(" ");

        b1.addActionListener(this);


        setLayout(new GridLayout(4,4,50,50));
        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(l1);
        add(c1);
        add(t1);
        add(t2);

        setSize(600,600);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

    }

    public static void main(String[] args) {
        new Tap();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(b1)){
            t1.setText(t1.getText()+" \n Button is Pressred" + t2.getText());
            System.out.println("Jbutton id pressed ");
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
