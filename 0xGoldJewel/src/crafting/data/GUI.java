package crafting.data;

import crafting.ZeroxGoldJewel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JComboBox jewelleryComboBox;
    private JButton start;

    public GUI() {

        super("Pick jewellery.");

        setLayout(new FlowLayout());

        start = new JButton("Start");

        jewelleryComboBox = new JComboBox(Jewellery.values());

        add(jewelleryComboBox);
        add(start);

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                ZeroxGoldJewel.toCraft = (Jewellery) jewelleryComboBox.getSelectedItem();
                setVisible(false);
            }
        });
    }
}

