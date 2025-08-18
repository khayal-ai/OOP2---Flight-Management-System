/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package flightmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PaymentPage extends JFrame {
    private JTextField flightNumberField, cardHolderField, cardNumberField, expiryField;
    private JPasswordField cvvField;
    private JComboBox<String> paymentTypeCombo;
    private JButton confirmButton, cancelButton;

    public PaymentPage() {
        setTitle("Payment Page");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(
            getClass().getResource("/flightmanagementsystem/airgo.png")
        ).getImage());

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/flightmanagementsystem/payment.png")
        );
        Image bg = icon.getImage().getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
        JLabel backgroundLabel = new JLabel(new ImageIcon(bg));
        backgroundLabel.setLayout(new GridBagLayout());
        setContentPane(backgroundLabel);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setPreferredSize(new Dimension(500, 600));
        content.setBackground(new Color(135, 206, 235, 220));
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel welcome = new JLabel("Complete Flight Reservation");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(welcome);
        content.add(Box.createVerticalStrut(20));

        content.add(makeRow("Flight Number:", flightNumberField = new JTextField(20)));
        content.add(makeRow("Payment Type:", paymentTypeCombo =
            new JComboBox<>(new String[]{"Credit Card","Debit Card","PayPal"})));
        content.add(makeRow("Cardholder Name:", cardHolderField = new JTextField(20)));
        content.add(makeRow("Card Number:", cardNumberField = new JTextField(20)));
        content.add(makeRow("Expiry Date (MM/YY):", expiryField = new JTextField(10)));
        content.add(makeRow("CVV:", cvvField = new JPasswordField(5)));

        
        JPanel btns = new JPanel();
        confirmButton = new JButton("Confirm");
        cancelButton  = new JButton("Cancel");
        for (JButton b : new JButton[]{confirmButton, cancelButton}) {
            b.setPreferredSize(new Dimension(150, 40));
            b.setFont(new Font("Arial", Font.PLAIN, 16));
            b.setBackground(Color.WHITE);
            b.setForeground(Color.BLACK);
            btns.add(b);
            if (b == confirmButton) {
                btns.add(Box.createRigidArea(new Dimension(20, 0)));
            }
        }
        content.add(Box.createVerticalStrut(20));
        content.add(btns);

        backgroundLabel.add(content);

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (flightNumberField.getText().trim().isEmpty() ||
                        cardHolderField.getText().trim().isEmpty() ||
                        cardNumberField.getText().trim().isEmpty() ||
                        expiryField.getText().trim().isEmpty() ||
                        new String(cvvField.getPassword()).trim().isEmpty()) {
                        throw new Exception("Please fill in all payment fields.");
                    }

                    String name = cardHolderField.getText().trim();
                    if (!name.matches("[A-Za-z ]+")) {
                        throw new Exception("Cardholder name must contain only letters and spaces.");
                    }

                    String raw = cardNumberField.getText().trim().replaceAll("\\s+","");
                    if (!raw.matches("\\d{16}")) {
                        throw new Exception("Card number must be exactly 16 digits.");
                    }

                    String exp = expiryField.getText().trim();
                    SimpleDateFormat fmt = new SimpleDateFormat("MM/yy");
                    fmt.setLenient(false);
                    Date expDate = fmt.parse(exp);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(expDate);
                    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                    if (cal.getTime().before(new Date())) {
                        throw new Exception("The card is expired.");
                    }

                    String cvv = new String(cvvField.getPassword()).trim();
                    if (!cvv.matches("\\d{3}")) {
                        throw new Exception("CVV must be exactly 3 digits.");
                    }

                    JOptionPane.showMessageDialog(
                        PaymentPage.this,
                        "Your booking is complete!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    dispose();
                    new BookFlightsPage();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        PaymentPage.this,
                        ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new BookFlightsPage();
            }
        });

        setVisible(true);
    }

    public PaymentPage(String flightNumber) {
        this();
        flightNumberField.setText(flightNumber);
        flightNumberField.setEditable(false);
    }

    private JPanel makeRow(String label, JComponent field) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setBackground(new Color(135, 206, 235, 0));
        field.setPreferredSize(new Dimension(250, 30));
        p.add(new JLabel(label));
        p.add(field);
        return p;
    }
}