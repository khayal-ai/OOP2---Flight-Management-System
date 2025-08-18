/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EmployeePage extends JFrame {
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JLabel label;
    private JButton manageFlights;
    private JButton manageInfo;
    private JButton logout;

    public EmployeePage() {
        setTitle("Employee Home Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setIconImage(new ImageIcon(
        getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

ImageIcon icon = new ImageIcon(
  getClass().getResource(
    "/flightmanagementsystem/emp4.jpg"
  )
);        Image bgImg = icon.getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setPreferredSize(new Dimension(530, 600));
        contentPanel.setBackground(new Color(255, 255, 255, 230));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        welcomeLabel = new JLabel("Welcome AirGo Employee");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        label = new JLabel("Please choose an action to perform");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(0, 153, 204));

        manageFlights = new JButton("Manage Flights");
        manageInfo = new JButton("Manage Employee Information");
        logout = new JButton("Logout");
        Dimension btnSize = new Dimension(270, 40);
        Font btnFont = new Font("Arial", Font.PLAIN, 16);
        Color btnColor = new Color(133, 230, 255);
        for (JButton b : new JButton[]{manageFlights, manageInfo, logout}) {
            b.setBackground(btnColor);
            b.setForeground(Color.BLACK);
            b.setFont(btnFont);
            b.setMaximumSize(btnSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setFocusPainted(false);
        }

        manageFlights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new FlightManagementPage();
            }
        });
        manageInfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Employeesmanagmentpage();
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        contentPanel.add(welcomeLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(label);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(manageFlights);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(manageInfo);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        contentPanel.add(logout);

        background.add(contentPanel);

        setVisible(true);
    }
}