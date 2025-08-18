/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    private static final String DB_URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    public static String savedUsername = "";
    public static String savedPassword = "";

    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel checkPanel;
    private JPanel buttonPanel;
    private JPanel radioPanel;
    private JLabel logoLabel;
    private JLabel welcomeLabel;
    private JLabel subLabel;
    private JLabel question;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton option1;
    private JRadioButton option2;
    private JCheckBox check;
    private ButtonGroup group;
    private JButton loginButton;

    public LoginPage() {
        setTitle("Flight Management System Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
        setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

        leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        logoLabel = new JLabel(new ImageIcon(
            new ImageIcon(getClass().getResource("/flightmanagementsystem/airgo.png"))
                .getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH)
        ));
        leftPanel.add(logoLabel);

        rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        welcomeLabel = new JLabel("Welcome to AirGo!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 36));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subLabel = new JLabel("Please enter your details");
        subLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(subLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 19));
        usernameField.setBorder(BorderFactory.createTitledBorder("Username"));
        rightPanel.add(usernameField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 20));
        passwordField.setBorder(BorderFactory.createTitledBorder("Password"));
        rightPanel.add(passwordField);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        question = new JLabel("Are you Employee or Passenger?");
        question.setFont(new Font("Arial", Font.BOLD, 19));
        option1 = new JRadioButton("Employee");
        option2 = new JRadioButton("Passenger");
        option1.setFont(new Font("Arial", Font.PLAIN, 21));
        option2.setFont(new Font("Arial", Font.PLAIN, 21));
        group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        radioPanel = new JPanel();
        radioPanel.setBackground(new Color(245, 245, 245));
        radioPanel.add(question);
        radioPanel.add(option1);
        radioPanel.add(option2);
        rightPanel.add(radioPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        check = new JCheckBox("Remember me");
        check.setFont(new Font("Arial", Font.PLAIN, 17));
        check.setBackground(new Color(245, 245, 245));
        checkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        checkPanel.setBackground(new Color(245, 245, 245));
        checkPanel.add(check);
        rightPanel.add(checkPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        loginButton = new JButton("Log in");
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.setFont(new Font("Arial", Font.BOLD, 20));
        loginButton.setBackground(new Color(133, 230, 255));
        loginButton.setForeground(Color.BLACK);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(loginButton);
        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = usernameField.getText().trim();
                String pass = new String(passwordField.getPassword()).trim();
                boolean remember = check.isSelected();

                if (user.length() < 3) {
                    JOptionPane.showMessageDialog(
                        LoginPage.this,
                        "Username must be at least 3 characters long.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                if (pass.length() < 8) {
                    JOptionPane.showMessageDialog(
                        LoginPage.this,
                        "Password must be at least 8 characters long.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                int digitCount = 0;
                for (char c : pass.toCharArray()) {
                    if (Character.isDigit(c)) {
                        digitCount++;
                    }
                }
                if (digitCount < 2) {
                    JOptionPane.showMessageDialog(
                        LoginPage.this,
                        "Password must contain at least 2 digits.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                if (!option1.isSelected() && !option2.isSelected()) {
                    JOptionPane.showMessageDialog(
                        LoginPage.this,
                        "Please select Employee or Passenger.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                String role = option1.isSelected() ? "employee" : "passenger";

                try (Connection conn = DriverManager.getConnection(DB_URL)) {
                    PreparedStatement checkUser = conn.prepareStatement(
                        "SELECT 1 FROM APP.USERS WHERE username = ?"
                    );
                    checkUser.setString(1, user);
                    ResultSet rs = checkUser.executeQuery();
                    if (!rs.next()) {
                        PreparedStatement insertUser = conn.prepareStatement(
                            "INSERT INTO APP.USERS(username, password, user_type, last_login) " +
                            "VALUES(?, ?, ?, CURRENT_TIMESTAMP)"
                        );
                        insertUser.setString(1, user);
                        insertUser.setString(2, pass);
                        insertUser.setString(3, role);
                        insertUser.executeUpdate();
                        insertUser.close();
                    }
                    rs.close();
                    checkUser.close();

                    if ("passenger".equals(role)) {
                        PreparedStatement checkPass = conn.prepareStatement(
                            "SELECT 1 FROM APP.PASSENGERS WHERE name = ?"
                        );
                        checkPass.setString(1, user);
                        rs = checkPass.executeQuery();
                        if (!rs.next()) {
                            PreparedStatement insertPass = conn.prepareStatement(
                                "INSERT INTO APP.PASSENGERS(name, email, password) VALUES(?, '', ?)"
                            );
                            insertPass.setString(1, user);
                            insertPass.setString(2, pass);
                            insertPass.executeUpdate();
                            insertPass.close();
                        }
                        rs.close();
                        checkPass.close();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        LoginPage.this,
                        "DB error during login:\n" + ex.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (remember) {
                    savedUsername = user;
                    savedPassword = pass;
                } else {
                    savedUsername = "";
                    savedPassword = "";
                }

                dispose();
                if (option1.isSelected()) {
                    new EmployeePage();
                } else {
                    new CustomerHomePage(user, remember);
                }
            }
        });

        add(leftPanel);
        add(rightPanel);
        setVisible(true);
    }
}