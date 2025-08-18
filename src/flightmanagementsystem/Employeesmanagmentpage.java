/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
public class Employeesmanagmentpage extends JFrame {
    private static final String DB_URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    
    private JPanel leftPanel, rightPanel;
    private JLabel titleLabel, nameLabel, idlabel, passwordLabel, logoLabel;
    private JTextField name, id;
    private JPasswordField password;
    private JButton addEmployee, viewEmployee, backButton;
    private JTextArea textarea;

    public Employeesmanagmentpage() {
        setTitle("Employees Management Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

                setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/flightmanagementsystem/emp3.jpg")
        );
        Image scaled = icon.getImage()
                           .getScaledInstance(600, 600, Image.SCALE_SMOOTH);
        logoLabel = new JLabel(new ImageIcon(scaled));

        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(logoLabel);

        leftPanel = new JPanel();
        leftPanel.setBackground(new Color(245, 245, 245));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        titleLabel = new JLabel("Employees Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nameLabel     = new JLabel("Employee Name:");
        idlabel       = new JLabel("Employee ID:");
        passwordLabel = new JLabel("Password:");

        for (JLabel lbl : new JLabel[]{nameLabel, idlabel, passwordLabel}) {
            lbl.setFont(new Font("Arial", Font.PLAIN, 16));
            lbl.setForeground(new Color(0, 153, 204));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl.setPreferredSize(new Dimension(150, 25));
        }

        name     = new JTextField(15);
        id       = new JTextField(15);
        password = new JPasswordField(15);

        textarea = new JTextArea(8, 30);
        textarea.setLineWrap(true);
        textarea.setWrapStyleWord(true);
        textarea.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        backButton   = new JButton("Back");
        addEmployee  = new JButton("Add Employee");
        viewEmployee = new JButton("View Employees");

        Dimension buttonSize = new Dimension(150, 100);
        Font buttonFont      = new Font("Arial", Font.PLAIN, 16);
        Color buttonColor    = new Color(133, 230, 255);

        for (JButton b : new JButton[]{backButton, addEmployee, viewEmployee}) {
            b.setBackground(buttonColor);
            b.setForeground(Color.BLACK);
            b.setFont(buttonFont);
            b.setMaximumSize(buttonSize);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        leftPanel.add(makeRow(nameLabel, name));
        leftPanel.add(makeRow(idlabel, id));
        leftPanel.add(makeRow(passwordLabel, password));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        actionRow.setBackground(leftPanel.getBackground());
        actionRow.add(backButton);
        actionRow.add(addEmployee);
        leftPanel.add(actionRow);

        leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        JPanel viewRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        viewRow.setBackground(leftPanel.getBackground());
        viewRow.add(viewEmployee);
        leftPanel.add(viewRow);
        leftPanel.add(textarea);

        add(leftPanel);
        add(rightPanel);

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EmployeePage();
            }
        });

        addEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String n = name.getText().trim();
                String i = id.getText().trim();
                String p = new String(password.getPassword()).trim();

                if (n.isEmpty() || i.isEmpty() || p.isEmpty()) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "All fields are required.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (!n.matches("[A-Za-z ]+")) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Name must contain letters and spaces only.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                int letterCount = 0;
                for (char c : n.toCharArray()) {
                    if (Character.isLetter(c)) letterCount++;
                }
                if (letterCount < 3) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Name must contain at least 3 letters.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (!i.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Employee ID must be exactly 10 digits.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                if (p.length() < 8) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Password must be at least 8 characters long.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                int digitCount = 0;
                for (char c : p.toCharArray()) {
                    if (Character.isDigit(c)) digitCount++;
                }
                if (digitCount < 2) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Password must contain at least 2 digits.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = conn.prepareStatement(
                       "INSERT INTO APP.EMPLOYEES(employee_id, name, password) VALUES (?, ?, ?)"
                     )) {
                    ps.setString(1, i);
                    ps.setString(2, n);
                    ps.setString(3, p);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "Employee added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } catch (SQLException ex) {
                    if ("23505".equals(ex.getSQLState())) {
                        JOptionPane.showMessageDialog(
                            Employeesmanagmentpage.this,
                            "Employee ID already exists!",
                            "Duplicate",
                            JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                            Employeesmanagmentpage.this,
                            "DB Error: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });

        viewEmployee.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textarea.setText("");
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery(
                       "SELECT employee_id, name FROM APP.EMPLOYEES"
                     )) {
                    while (rs.next()) {
                        textarea.append(
                          rs.getString("employee_id") +
                          "  |  " +
                          rs.getString("name") +
                          "\n"
                        );
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        Employeesmanagmentpage.this,
                        "DB Error: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        setVisible(true);
    }

    private JPanel makeRow(JComponent lbl, JComponent fld) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(245, 245, 245));
        row.add(lbl);
        row.add(fld);
        return row;
    }
}

