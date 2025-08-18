/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlightManagementPage extends JFrame {
    public static final List<String> flights = new ArrayList<>();

    private static final String DB_URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    private JPanel leftPanel, rightPanel;
    private JLabel imageLabel, titleLabel;
    private JRadioButton localButton, internationalButton;
    private ButtonGroup flightTypeGroup;
    private JRadioButton firstClassButton, economyButton;
    private ButtonGroup classTypeGroup;
    private JComboBox<String> sourceComboBox, destinationComboBox, timeComboBox;
    private JButton addButton, cancelButton, backButton;

    private final String[] localCities             = {"JED","RUH","NUM","MED","BHH","DMM","AQI","AHB","TUU","AJF"};
    private final String[] internationalCities     = {"UAE","France","Germany","Egypt","USA","UK","Australia","Saudi Arabia","Turkey","Switzerland"};
    private final String[] localFlightTime         = {"8:00 AM","2:30 PM","9:00 PM"};
    private final String[] internationalFlightTime = {"3:30 PM","5:30 PM","9:00 PM"};

    public FlightManagementPage() {
        setTitle("Flights Management");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1,2));

        setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

        ImageIcon icon = new ImageIcon(getClass().getResource("/flightmanagementsystem/emp1.jpg"));
        Image scaled  = icon.getImage().getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
        imageLabel    = new JLabel(new ImageIcon(scaled));
        rightPanel    = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(imageLabel);

        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(60,60,60,60));
        leftPanel.setBackground(new Color(245,245,245));

        titleLabel = new JLabel("Flights Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD,30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0,20)));

        localButton        = new JRadioButton("Local");
        internationalButton= new JRadioButton("International");
        flightTypeGroup    = new ButtonGroup();
        flightTypeGroup.add(localButton);
        flightTypeGroup.add(internationalButton);
        localButton.setSelected(true);
        JPanel ftRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ftRow.setBackground(leftPanel.getBackground());
        ftRow.add(new JLabel("Flight Type:"));
        ftRow.add(localButton);
        ftRow.add(internationalButton);
        leftPanel.add(ftRow);

        firstClassButton   = new JRadioButton("First Class");
        economyButton      = new JRadioButton("Economy");
        classTypeGroup     = new ButtonGroup();
        classTypeGroup.add(firstClassButton);
        classTypeGroup.add(economyButton);
        firstClassButton.setSelected(true);
        JPanel ctRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ctRow.setBackground(leftPanel.getBackground());
        ctRow.add(new JLabel("Class Type:"));
        ctRow.add(firstClassButton);
        ctRow.add(economyButton);
        leftPanel.add(ctRow);

        sourceComboBox = new JComboBox<>(localCities);
        JPanel srcRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        srcRow.setBackground(leftPanel.getBackground());
        srcRow.add(new JLabel("From:"));
        srcRow.add(sourceComboBox);
        leftPanel.add(srcRow);

        destinationComboBox = new JComboBox<>(localCities);
        JPanel dstRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dstRow.setBackground(leftPanel.getBackground());
        dstRow.add(new JLabel("To:"));
        dstRow.add(destinationComboBox);
        leftPanel.add(dstRow);

        timeComboBox = new JComboBox<>(localFlightTime);
        JPanel tRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tRow.setBackground(leftPanel.getBackground());
        tRow.add(new JLabel("Time:"));
        tRow.add(timeComboBox);
        leftPanel.add(tRow);

        addButton    = new JButton("Add Flight");
        cancelButton = new JButton("Cancel Flight");
        backButton   = new JButton("Back");
        Dimension bSize = new Dimension(160,50);
        Font      bFont = new Font("Arial", Font.PLAIN,16);
        Color     bColor= new Color(133,230,255);
        for (JButton b : new JButton[]{backButton, addButton, cancelButton}) {
            b.setPreferredSize(bSize);
            b.setFont(bFont);
            b.setBackground(bColor);
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER,20,10));
        btnRow.setBackground(leftPanel.getBackground());
        btnRow.add(backButton);
        btnRow.add(addButton);
        btnRow.add(cancelButton);
        leftPanel.add(btnRow);

        localButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLists();
            }
        });
        internationalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLists();
            }
        });
        addButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String type = localButton.isSelected() ? "Local" : "International";
        String cls  = firstClassButton.isSelected() ? "First Class" : "Economy";
        String src  = (String) sourceComboBox.getSelectedItem();
        String dst  = (String) destinationComboBox.getSelectedItem();
        String tm   = (String) timeComboBox.getSelectedItem();

        if (src.equals(dst)) {
            JOptionPane.showMessageDialog(
                FlightManagementPage.this,
                "Source and destination cannot be the same.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement check = conn.prepareStatement(
                 "SELECT 1 FROM APP.FLIGHT " +
                 "WHERE flight_type=? AND class_type=? AND source=? AND destination=? AND flight_time=?"
             )) {
            check.setString(1, type);
            check.setString(2, cls);
            check.setString(3, src);
            check.setString(4, dst);
            check.setString(5, tm);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(
                        FlightManagementPage.this,
                        "That flight already exists.",
                        "Duplicate",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                FlightManagementPage.this,
                "DB Error checking flight: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int code = new Random().nextInt(900) + 100;
        String flightNumber = dst + "-" + code;
        String compositeKey = type + "|" + cls + "|" + src + "→" + dst + "@" + tm;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(
               "INSERT INTO APP.FLIGHT " +
               "(flight_number, flight_type, class_type, source, destination, flight_time) " +
               "VALUES (?, ?, ?, ?, ?, ?)"
             )) {
            ps.setString(1, flightNumber);
            ps.setString(2, type);
            ps.setString(3, cls);
            ps.setString(4, src);
            ps.setString(5, dst);
            ps.setString(6, tm);
            ps.executeUpdate();
            flights.add(compositeKey);
            JOptionPane.showMessageDialog(
                FlightManagementPage.this,
                "Flight added successfully! Flight# " + flightNumber,
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                FlightManagementPage.this,
                "DB Error adding flight: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
});
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String type = localButton.isSelected() ? "Local" : "International";
                String cls  = firstClassButton.isSelected() ? "First Class" : "Economy";
                String src  = (String) sourceComboBox.getSelectedItem();
                String dst  = (String) destinationComboBox.getSelectedItem();
                String tm   = (String) timeComboBox.getSelectedItem();
                String compositeKey = type + "|" + cls + "|" + src + "→" + dst + "@" + tm;

                Connection conn = null;
                try {
                    conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement find = conn.prepareStatement(
                        "SELECT flight_number FROM APP.FLIGHT " +
                        "WHERE flight_type=? AND class_type=? AND source=? AND destination=? AND flight_time=?"
                    );
                    find.setString(1, type);
                    find.setString(2, cls);
                    find.setString(3, src);
                    find.setString(4, dst);
                    find.setString(5, tm);
                    ResultSet rs = find.executeQuery();
                    if (!rs.next()) {
                        JOptionPane.showMessageDialog(
                            FlightManagementPage.this,
                            "No such flight to cancel.",
                            "Not Found",
                            JOptionPane.ERROR_MESSAGE
                        );
                        rs.close();
                        find.close();
                        return;
                    }
                    String flightNumber = rs.getString("flight_number");
                    rs.close();
                    find.close();
                    conn.setAutoCommit(false);

                    PreparedStatement delB = conn.prepareStatement(
                        "DELETE FROM APP.BOOKINGS WHERE flight_number = ?"
                    );
                    delB.setString(1, flightNumber);
                    delB.executeUpdate();
                    delB.close();

                    PreparedStatement delF = conn.prepareStatement(
                        "DELETE FROM APP.FLIGHT WHERE flight_number = ?"
                    );
                    delF.setString(1, flightNumber);
                    int removed = delF.executeUpdate();
                    delF.close();

                    if (removed > 0) {
                        conn.commit();
                        flights.remove(compositeKey);
                        JOptionPane.showMessageDialog(
                            FlightManagementPage.this,
                            "Flight and its bookings cancelled successfully.",
                            "Removed",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    } else {
                        conn.rollback();
                        JOptionPane.showMessageDialog(
                            FlightManagementPage.this,
                            "No such flight to cancel.",
                            "Not Found",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                        FlightManagementPage.this,
                        "DB Error removing flight: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    if (conn != null) try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException ignore) {}
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new EmployeePage();
            }
        });

        add(leftPanel);
        add(rightPanel);
        setVisible(true);

        loadFlightsFromDb();
    }

    private void updateLists() {
        if (localButton.isSelected()) {
            sourceComboBox.setModel(new DefaultComboBoxModel<>(localCities));
            destinationComboBox.setModel(new DefaultComboBoxModel<>(localCities));
            timeComboBox.setModel(new DefaultComboBoxModel<>(localFlightTime));
        } else {
            sourceComboBox.setModel(new DefaultComboBoxModel<>(internationalCities));
            destinationComboBox.setModel(new DefaultComboBoxModel<>(internationalCities));
            timeComboBox.setModel(new DefaultComboBoxModel<>(internationalFlightTime));
        }
    }

    public static void loadFlightsFromDb() {
        flights.clear();
        String sql = "SELECT flight_type, class_type, source, destination, flight_time FROM APP.FLIGHT";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String type = rs.getString("flight_type");
                String cls  = rs.getString("class_type");
                String src  = rs.getString("source");
                String dst  = rs.getString("destination");
                String tm   = rs.getString("flight_time");
                flights.add(type + "|" + cls + "|" + src + "→" + dst + "@" + tm);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Error loading flights from DB:\n" + ex.getMessage(),
                "DB Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
