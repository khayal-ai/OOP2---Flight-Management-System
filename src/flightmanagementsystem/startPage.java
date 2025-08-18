/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class startPage extends JFrame {

    public JButton start;

    public startPage() {
        setTitle("AirGo");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

ImageIcon icon = new ImageIcon(
    getClass().getResource("/flightmanagementsystem/start.jpg")
);
        Image scaled = icon.getImage().getScaledInstance(
            Toolkit.getDefaultToolkit().getScreenSize().width,
            Toolkit.getDefaultToolkit().getScreenSize().height,
            Image.SCALE_SMOOTH
        );
        JLabel background = new JLabel(new ImageIcon(scaled));
        background.setLayout(null);
        setContentPane(background);

        start = new JButton("Start");
        start.setFont(new Font("Arial", Font.PLAIN, 24));
        start.setBackground(new Color(135, 206, 235));
        start.setForeground(Color.BLACK);
        start.setBounds(100, 500, 100, 50);
        start.setFocusPainted(false);

        background.add(start);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginPage();
                dispose();
            }
        });

        setVisible(true);
    }
}
