/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BookFlightsPage extends JFrame {
    public static final List<String> flights = FlightManagementPage.flights;
private List<String> availableTimes  = new ArrayList<>();
private List<String> availableNumbers = new ArrayList<>();


    private String username;
    private boolean returning;
    private String nextBookingId;


    private JPanel leftPanel, rightPanel;
    private JRadioButton localButton, internationalButton;
    private JRadioButton firstClassButton, economyButton;
    private ButtonGroup flightTypeGroup, classTypeGroup;
    private JComboBox<String> sourceComboBox, destinationComboBox, timeComboBox;
    private JTextField passportField, dateTextField;
    private JButton searchButton, bookButton, backButton;
    private JTextArea resultArea;

    private final String[] localCities = {
        "JED","RUH","NUM","MED","BHH","DMM","AQI","AHB","TUU","AJF"
    };
    private final String[] internationalCities = {
        "UAE","France","Germany","Egypt","USA","UK",
        "Australia","Saudi Arabia","Turkey","Switzerland"
    };
    private final String[] dummyLocalTimes         = {"8:00 AM","2:30 PM","9:00 PM"};
    private final String[] dummyInternationalTimes = {"3:30 PM","5:30 PM","9:00 PM"};

    private static final String DB_URL = "jdbc:derby://localhost:1527/flightmanagementsystem;create=true";

    public BookFlightsPage() {
        this.username = null;
        this.returning = false;
        initUI();
    }

 public BookFlightsPage(String username, boolean returning) {
    this.username  = username;
    this.returning = returning;
    initUI();
            setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

    }

    private void initUI() {
        setTitle("Booking Flights Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        ImageIcon icon = new ImageIcon(
    getClass().getResource("/flightmanagementsystem/emp1.jpg")
);        Image img = icon.getImage().getScaledInstance(1000, 1000, Image.SCALE_SMOOTH);
        rightPanel.add(new JLabel(new ImageIcon(img)));

        
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));
        leftPanel.setBackground(new Color(245, 245, 245));

        
        JLabel titleLabel = new JLabel("Book Flight");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel flightTypeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        flightTypeRow.setBackground(leftPanel.getBackground());
        JLabel flightTypeLabel = new JLabel("Flight Type:");
        flightTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        flightTypeLabel.setForeground(new Color(0, 153, 204));
        flightTypeRow.add(flightTypeLabel);
        localButton = new JRadioButton("Local");
        internationalButton = new JRadioButton("International");
        flightTypeGroup = new ButtonGroup();
        flightTypeGroup.add(localButton);
        flightTypeGroup.add(internationalButton);
        localButton.setSelected(true);
        flightTypeRow.add(localButton);
        flightTypeRow.add(internationalButton);
        leftPanel.add(flightTypeRow);

        JPanel classTypeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        classTypeRow.setBackground(leftPanel.getBackground());
        JLabel classTypeLabel = new JLabel("Class Type:");
        classTypeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        classTypeLabel.setForeground(new Color(0, 153, 204));
        classTypeRow.add(classTypeLabel);
        firstClassButton = new JRadioButton("First Class");
        economyButton    = new JRadioButton("Economy");
        classTypeGroup   = new ButtonGroup();
        classTypeGroup.add(firstClassButton);
        classTypeGroup.add(economyButton);
        firstClassButton.setSelected(true);
        classTypeRow.add(firstClassButton);
        classTypeRow.add(economyButton);
        leftPanel.add(classTypeRow);

       
        JPanel fromToRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        fromToRow.setBackground(leftPanel.getBackground());
        JLabel fromLabel = new JLabel("From:");
        fromLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        fromLabel.setForeground(new Color(0, 153, 204));
        fromToRow.add(fromLabel);
        sourceComboBox      = new JComboBox<>(localCities);
        fromToRow.add(sourceComboBox);
        JLabel toLabel = new JLabel("To:");
        toLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        toLabel.setForeground(new Color(0, 153, 204));
        fromToRow.add(toLabel);
        destinationComboBox = new JComboBox<>(localCities);
        fromToRow.add(destinationComboBox);
        leftPanel.add(fromToRow);

        
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        dateRow.setBackground(leftPanel.getBackground());
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        dateLabel.setForeground(new Color(0, 153, 204));
        dateRow.add(dateLabel);
        dateTextField = new JTextField(10);
        dateRow.add(dateTextField);
        leftPanel.add(dateRow);

        JPanel timeRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        timeRow.setBackground(leftPanel.getBackground());
        JLabel timeLabel = new JLabel("Available Times:");
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        timeLabel.setForeground(new Color(0, 153, 204));
        timeRow.add(timeLabel);
        timeComboBox = new JComboBox<>(dummyLocalTimes);
        timeComboBox.setEnabled(false);
        timeRow.add(timeComboBox);
        leftPanel.add(timeRow);

        JPanel passportRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        passportRow.setBackground(leftPanel.getBackground());
        JLabel passportLabel = new JLabel("Passport ID:");
        passportLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        passportLabel.setForeground(new Color(0, 153, 204));
        passportRow.add(passportLabel);
        passportField = new JTextField(15);
        passportRow.add(passportField);
        leftPanel.add(passportRow);

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchButton.setBackground(new Color(133, 230, 255));
        searchButton.setOpaque(true);
        searchButton.setFocusPainted(false);
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        searchRow.setBackground(leftPanel.getBackground());
        searchRow.add(searchButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(searchRow);

        resultArea = new JTextArea(15, 50);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(resultScroll);

        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.setBackground(new Color(133, 230, 255));
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Arial", Font.PLAIN, 16));
        bookButton.setBackground(new Color(133, 230, 255));
        bookButton.setOpaque(true);
        bookButton.setFocusPainted(false);
        bookButton.setEnabled(false);
        JPanel bookRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bookRow.setBackground(leftPanel.getBackground());
        bookRow.add(backButton);
        bookRow.add(bookButton);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(bookRow);

        add(leftPanel);
        add(rightPanel);

        localButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateCityLists(); }
        });
        internationalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { updateCityLists(); }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doSearch(); }
        });
        timeComboBox.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        showFlightDetails();
    }
});
        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { doBook(); }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new CustomerHomePage(username, returning);
            }
        });

        setVisible(true);
    }

    private String currentFlightNumber;

private void doSearch() {
        String src  = (String) sourceComboBox.getSelectedItem();
        String dst  = (String) destinationComboBox.getSelectedItem();
        String date = dateTextField.getText().trim();

        if (src.equals(dst)) {
            JOptionPane.showMessageDialog(
                this,
                "Source and destination cannot be the same.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            if (!parsed.after(new Date())) throw new Exception();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid future date.",
                "Invalid Date",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        availableTimes.clear();
        availableNumbers.clear();
        String sql =
            "SELECT flight_time, flight_number " +
            "FROM APP.FLIGHT " +
            "WHERE flight_type = ? AND class_type = ? " +
              "AND source = ? AND destination = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String flightType = localButton.isSelected() ? "Local" : "International";
            String classType  = firstClassButton.isSelected() ? "First Class" : "Economy";
            ps.setString(1, flightType);
            ps.setString(2, classType);
            ps.setString(3, src);
            ps.setString(4, dst);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String t = rs.getString("flight_time");
                    String f = rs.getString("flight_number");
                    if (!availableTimes.contains(t)) {
                        availableTimes.add(t);
                        availableNumbers.add(f);
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "DB error loading available flights:\n" + ex.getMessage(),
                "DB Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("VALUES NEXT VALUE FOR APP.BOOKING_SEQ")) {
            rs.next();
            nextBookingId = "TIC-" + rs.getInt(1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not reserve booking ID:\n" + ex.getMessage(),
                "DB Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (availableTimes.isEmpty()) {
            timeComboBox.setModel(new DefaultComboBoxModel<>(new String[0]));
            timeComboBox.setEnabled(false);
            bookButton.setEnabled(false);
            resultArea.setText(
                "No flights available for:\n" +
                (localButton.isSelected() ? "Local" : "International") + " | " +
                (firstClassButton.isSelected() ? "First Class" : "Economy") + "\n" +
                src + " → " + dst
            );
            return;
        }

        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        for (String t : availableTimes) m.addElement(t);
        timeComboBox.setModel(m);
        timeComboBox.setEnabled(true);
        bookButton.setEnabled(true);

        timeComboBox.setSelectedIndex(0);
        showFlightDetails();
    }

    private void showFlightDetails() {
        int idx = timeComboBox.getSelectedIndex();
        if (idx < 0) return;

        String src        = (String) sourceComboBox.getSelectedItem();
        String dst        = (String) destinationComboBox.getSelectedItem();
        String date       = dateTextField.getText().trim();
        String chosenTime = availableTimes.get(idx);
        currentFlightNumber = availableNumbers.get(idx);

        String flightType = localButton.isSelected() ? "Local" : "International";
        String classType  = firstClassButton.isSelected() ? "First Class" : "Economy";
        int price         = flightType.equals("Local") ? 250 : 400;

        String details = String.format(
            "Available Flights:%n%n" +
            "Flight Type: %s | Class: %s%n" +
            "Source: %s | Destination: %s%n" +
            "Date: %s%n" +
            "Booking ID: %s%n" +
            "Flight Number: %s | Time: %s | Price: $%d",
            flightType, classType, src, dst, date,
            nextBookingId, currentFlightNumber, chosenTime, price
        );
        resultArea.setText(details);
    }

    private void doBook() {
 String passport = passportField.getText().trim().toUpperCase();
    if (!passport.matches("[A-Z]{2}\\d{6}")) {
        JOptionPane.showMessageDialog(
            this,
            "Invalid Passport ID!\n" +
            "It must be exactly 8 characters : first two uppercase letters, then 6 digits.",
            "Input Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

       try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement chk = conn.prepareStatement(
             "SELECT COUNT(*) FROM APP.BOOKINGS WHERE passenger_passport = ?"
         )) {
        chk.setString(1, passport);
        try (ResultSet rs = chk.executeQuery()) {
            rs.next();
            if (rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog( this,
                "There is already an existing booking with passport ID: " + passport,
                "Booking Error",
                JOptionPane.WARNING_MESSAGE
            );
                passportField.setText("");
                passportField.requestFocus();
                return;
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(
            this,
            "DB error checking existing booking:\n" + ex.getMessage(),
            "DB Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

        String dst        = (String) destinationComboBox.getSelectedItem();
        String chosenTime = (String) timeComboBox.getSelectedItem();
        String flightType = localButton.isSelected() ? "Local" : "International";
        String classType  = firstClassButton.isSelected() ? "First Class" : "Economy";
        int price         = flightType.equals("Local") ? 250 : 400;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement bk = conn.prepareStatement(
               "INSERT INTO APP.BOOKINGS " +
               "(booking_id, passenger_passport, flight_number, destination, flight_time, flight_type, class_type, price, booking_date) " +
               "VALUES(?,?,?,?,?,?,?,? ,CURRENT_DATE)"
             )) {
            bk.setString(1, nextBookingId);
            bk.setString(2, passport);
            bk.setString(3, currentFlightNumber);
            bk.setString(4, dst);
            bk.setString(5, chosenTime);
            bk.setString(6, flightType);
            bk.setString(7, classType);
            bk.setInt   (8, price);
            bk.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Booking DB error: " + ex.getMessage(),
                "DB Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        new PaymentPage(currentFlightNumber).setVisible(true);
        dispose();
    }
    private void updateCityLists() {
        if (localButton.isSelected()) {
            sourceComboBox.setModel(new DefaultComboBoxModel<>(localCities));
            destinationComboBox.setModel(new DefaultComboBoxModel<>(localCities));
            timeComboBox.setModel(new DefaultComboBoxModel<>(dummyLocalTimes));
        } else {
            sourceComboBox.setModel(new DefaultComboBoxModel<>(internationalCities));
            destinationComboBox.setModel(new DefaultComboBoxModel<>(internationalCities));
            timeComboBox.setModel(new DefaultComboBoxModel<>(dummyInternationalTimes));
        }
        timeComboBox.setEnabled(false);
        bookButton.setEnabled(false);
    }}