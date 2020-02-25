package cooking.data;

import cooking.ZeroxCooking;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JComboBox cookComboBox;
    private JButton start;

    public GUI() {
        super("Pick food to cook.");

        setLayout(new FlowLayout());

        start = new JButton("Start");

        cookComboBox = new JComboBox(Resources.values());

        add(cookComboBox);
        add(start);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ZeroxCooking.res = (Resources) cookComboBox.getSelectedItem();
                setVisible(false);
            }
        });
    }
}
