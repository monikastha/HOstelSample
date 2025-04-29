package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;

public class AdminUpdateFee extends JFrame {
    private int feeId;
    private JTextField txtCredit, txtRemaining, txtDateMonth;
    private JButton btnUpdate, btnCancel;
    private AdminFeeManager parentPanel; // Reference to the parent panel to refresh data

    public AdminUpdateFee(int feeId, AdminFeeManager parentPanel) {
        this.feeId = feeId;
        this.parentPanel = parentPanel;

        setTitle("Update Fee Payment");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCredit = new JLabel("Paid Amount:");
        lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblCredit, gbc);

        txtCredit = new JTextField();
        txtCredit.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 0;
        add(txtCredit, gbc);

        JLabel lblRemaining = new JLabel("Remaining Amount:");
        lblRemaining.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 1;
        add(lblRemaining, gbc);

        txtRemaining = new JTextField();
        txtRemaining.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 1;
        add(txtRemaining, gbc);

        JLabel lblDateMonth = new JLabel("Date/Month:");
        lblDateMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2;
        add(lblDateMonth, gbc);

        txtDateMonth = new JTextField();
        txtDateMonth.setFont(new Font("Tahoma", Font.PLAIN, 16));
        gbc.gridx = 1; gbc.gridy = 2;
        add(txtDateMonth, gbc);

        btnUpdate = new JButton("Update Payment");
        btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnUpdate.setBackground(new Color(30, 144, 255));
        btnUpdate.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(btnUpdate, gbc);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Tahoma", Font.BOLD, 16));
        btnCancel.setBackground(new Color(220, 20, 60));
        btnCancel.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnCancel, gbc);

        // Load current data of fee record
        loadFeeData();

        // Button Actions
        btnUpdate.addActionListener(e -> updateFee());
        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void loadFeeData() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
             PreparedStatement pst = con.prepareStatement("SELECT f_credit, f_remaining, f_date_month FROM fee WHERE f_id = ?")) {

            pst.setInt(1, feeId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                txtCredit.setText(rs.getBigDecimal("f_credit").toString());
                txtRemaining.setText(rs.getBigDecimal("f_remaining").toString());
                txtDateMonth.setText(rs.getString("f_date_month"));
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading fee data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFee() {
        String credit = txtCredit.getText();
        String remaining = txtRemaining.getText();
        String dateMonth = txtDateMonth.getText();

        // Validation
        if (credit.isEmpty() || remaining.isEmpty() || dateMonth.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try {
            //  Use BigDecimal for precision
            BigDecimal creditDecimal = new BigDecimal(credit);
            BigDecimal remainingDecimal = new BigDecimal(remaining);
            String statusUpdate = remainingDecimal.compareTo(BigDecimal.ZERO) > 0 ? "Pending" : "Paid";

            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3307/hostel", "root", "");
                 PreparedStatement pst = con.prepareStatement("UPDATE fee SET f_credit = ?, f_remaining = ?, f_date_month = ?, f_status = ? WHERE f_id = ?")) {

                pst.setBigDecimal(1, creditDecimal);
                pst.setBigDecimal(2, remainingDecimal);
                pst.setString(3, dateMonth);
                pst.setString(4, statusUpdate); // Set the updated status
                pst.setInt(5, feeId);

                System.out.println("Remaining Decimal: " + remainingDecimal);
                System.out.println("Status Update: " + statusUpdate);

                int rowsUpdated = pst.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Payment Updated Successfully!");
                    if (parentPanel != null) {
                        parentPanel.viewPayments(false); // Refresh the table in the parent panel
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating payment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number format. Please enter numeric values for Credit and Remaining amounts.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Example: Open Update Fee window with a specific fee ID (e.g., 1) and a null parent panel for standalone testing
        new AdminUpdateFee(1, null);
    }
}