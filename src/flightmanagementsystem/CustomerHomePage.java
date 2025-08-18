/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerHomePage extends JFrame {
    private JPanel contentPanel;
    private JButton bookButton;
    private JButton myProfileButton;
    private JButton logoutButton;
    private String username;
    private boolean isReturning;

    public CustomerHomePage(String username, boolean isReturning) {
        this.username = username;
        this.isReturning = isReturning;
        initUI();
        setIconImage(new ImageIcon(
            getClass().getResource("/airportmanagementsystem/airgo.png")
        ).getImage());
    }

    private void initUI() {
        setTitle("Passenger Home Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon(getClass().getResource(
            "/airportmanagementsystem/pass1.jpg"
        ));
        Image bgImg = icon.getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setLayout(new GridBagLayout());
        setContentPane(background);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(255, 255, 255, 200));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel logoLabel = new JLabel("Welcome to AirGo!", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 30));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(logoLabel);
        contentPanel.add(Box.createVerticalStrut(30));

        Dimension buttonSize = new Dimension(350, 40);
        Color btnColor = new Color(133, 230, 255);

        bookButton = new JButton("Book Flights");
        myProfileButton = new JButton("My Profile");
        logoutButton = new JButton("Logout");

        for (JButton b : new JButton[]{bookButton, myProfileButton, logoutButton}) {
            b.setPreferredSize(buttonSize);
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setBackground(b == logoutButton ? new Color(255, 99, 71) : btnColor);
            b.setForeground(b == logoutButton ? Color.WHITE : Color.BLACK);
            b.setFont(new Font("Arial", Font.PLAIN, 18));
            contentPanel.add(b);
            contentPanel.add(Box.createVerticalStrut(20));
        }

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new BookFlightsPage(username, isReturning);
            }
        });

        myProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AccountSettingsPage(username, isReturning);
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginPage();
            }
        });

        background.add(contentPanel);
        setVisible(true);
    }
}