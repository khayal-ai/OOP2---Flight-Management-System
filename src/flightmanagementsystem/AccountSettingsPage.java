/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;



public class AccountSettingsPage extends JFrame {
    private static final String DB_URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    private final boolean isReturning;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton updateButton, backButton;

    public AccountSettingsPage(String originalName, boolean isReturning) {
        this.isReturning = isReturning;

        setTitle("My Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
        setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        );
        leftPanel.add(new JLabel(new ImageIcon(
            icon.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH)
        )));
        add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        add(rightPanel);

        JLabel titleLabel = new JLabel("Account Settings", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(titleLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel subLabel = new JLabel("Update your profile information", SwingConstants.CENTER);
        subLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(subLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        nameField = new JTextField(
            isReturning && !LoginPage.savedUsername.isEmpty()
                ? LoginPage.savedUsername
                : ""
        );
        nameField.setBorder(BorderFactory.createTitledBorder("Name"));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rightPanel.add(nameField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        emailField = new JTextField();
        emailField.setBorder(BorderFactory.createTitledBorder("Email"));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rightPanel.add(emailField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        passwordField = new JPasswordField(
            isReturning && !LoginPage.savedPassword.isEmpty()
                ? LoginPage.savedPassword
                : ""
        );
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttons.setBackground(new Color(245, 245, 245));
        updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(150, 45));
        updateButton.setFont(new Font("Arial", Font.BOLD, 18));
        updateButton.setBackground(new Color(0, 153, 204));
        updateButton.setForeground(Color.WHITE);
        buttons.add(updateButton);

        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(150, 45));
        backButton.setFont(new Font("Arial", Font.BOLD, 18));
        backButton.setBackground(new Color(133, 230, 255));
        backButton.setForeground(Color.BLACK);
        buttons.add(backButton);

        rightPanel.add(buttons);

        loadProfile(originalName);

updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!validateInputs()) {
                    return;
                }
                saveProfile(originalName);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerHomePage(nameField.getText().trim(), isReturning);
            }
        });

        setVisible(true);
    }

    private void loadProfile(String nameKey) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT email FROM APP.PASSENGERS WHERE name = ?"
             )) {
            ps.setString(1, nameKey);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    emailField.setText(rs.getString("email"));
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "DB error loading profile:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInputs() {
        String newName = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String pwd     = new String(passwordField.getPassword()).trim();

        int letters = 0;
        for (char c : newName.toCharArray()) if (Character.isLetter(c)) letters++;
        if (letters < 3) {
            JOptionPane.showMessageDialog(this,
                "Name must contain at least 3 letters.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this,
                "Email must end with @gmail.com.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (pwd.length() < 8) {
            JOptionPane.showMessageDialog(this,
                "Password must be at least 8 characters long.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        int digitCount = 0;
        for (char c : pwd.toCharArray()) if (Character.isDigit(c)) digitCount++;
        if (digitCount < 2) {
            JOptionPane.showMessageDialog(this,
                "Password must contain at least 2 digits.",
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveProfile(String originalName) {
        String newName = nameField.getText().trim();
        String email   = emailField.getText().trim();
        String pwd     = new String(passwordField.getPassword()).trim();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement upd = conn.prepareStatement(
                "UPDATE APP.PASSENGERS SET name = ?, email = ?, password = ? WHERE name = ?"
            );
            upd.setString(1, newName);
            upd.setString(2, email);
            upd.setString(3, pwd);
            upd.setString(4, originalName);
            int rows = upd.executeUpdate();
            upd.close();

            if (rows == 0) {
                PreparedStatement ins = conn.prepareStatement(
                    "INSERT INTO APP.PASSENGERS(name, email, password) VALUES(?,?,?)"
                );
                ins.setString(1, newName);
                ins.setString(2, email);
                ins.setString(3, pwd);
                ins.executeUpdate();
                ins.close();
            }

            if (isReturning) {
                LoginPage.savedUsername = newName;
                LoginPage.savedPassword = pwd;
            }

            JOptionPane.showMessageDialog(this,
                "Your profile has been saved!",
                "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            new CustomerHomePage(newName, isReturning);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "DB error saving profile:\n" + ex.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
