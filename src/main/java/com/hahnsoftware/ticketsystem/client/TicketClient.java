package com.hahnsoftware.ticketsystem.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketClient extends JFrame {
    private JTextField titleField, descriptionField, ticketIdField;
    private JComboBox<String> priorityCombo, categoryCombo;
    private JButton submitButton, viewTicketsButton, updateStatusButton, addCommentButton;
    private JTextArea ticketList;
    private JTextField statusField, commentField;
    private String username;

    public TicketClient(String username) {
        this.username = username;
        setTitle("IT Support Ticket System - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Ticket Creation Components
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(20);
        add(titleField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1;
        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        add(priorityCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(new String[]{"Network", "Hardware", "Software", "Other"});
        add(categoryCombo, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        submitButton = new JButton("Submit Ticket");
        add(submitButton, gbc);

        // Ticket List
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        ticketList = new JTextArea(10, 30);
        ticketList.setEditable(false);
        add(new JScrollPane(ticketList), gbc);

        // Ticket Management Components
        gbc.gridwidth = 1; // Reset gridwidth
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Ticket ID:"), gbc);
        gbc.gridx = 1;
        ticketIdField = new JTextField(10);
        add(ticketIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("New Status:"), gbc);
        gbc.gridx = 1;
        statusField = new JTextField(10);
        add(statusField, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Comment:"), gbc);
        gbc.gridx = 1;
        commentField = new JTextField(20);
        add(commentField, gbc);

        gbc.gridx = 1; gbc.gridy = 9;
        viewTicketsButton = new JButton("View Tickets");
        add(viewTicketsButton, gbc);

        gbc.gridy = 10;
        updateStatusButton = new JButton("Update Status");
        add(updateStatusButton, gbc);

        gbc.gridy = 11;
        addCommentButton = new JButton("Add Comment");
        add(addCommentButton, gbc);

        // Action Listeners
        submitButton.addActionListener(e -> submitTicket());
        viewTicketsButton.addActionListener(e -> viewTickets(username));
        updateStatusButton.addActionListener(e -> updateStatus(username));
        addCommentButton.addActionListener(e -> addComment(username));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void submitTicket() {
        try {
            String urlString = "http://localhost:8080/api/tickets?username=" + username;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format(
                    "{\"title\":\"%s\",\"description\":\"%s\",\"priority\":\"%s\",\"category\":\"%s\"}",
                    titleField.getText(), descriptionField.getText(), priorityCombo.getSelectedItem(), categoryCombo.getSelectedItem()
            );
            conn.getOutputStream().write(jsonInput.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            String ticketId = response.contains("\"id\"") ? response.split("\"id\":")[1].split(",")[0] : "Unknown";
            ticketList.append(String.format("Ticket Created - ID: %s, Username: %s\n", ticketId, username));

            in.close();
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting ticket: " + ex.getMessage());
        }
    }

    private void viewTickets(String username) {
        try {
            String urlString = "http://localhost:8080/api/tickets?username=" + username;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            ticketList.setText("Tickets:\n" + response + "\n");

            in.close();
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching tickets: " + ex.getMessage());
        }
    }

    private void updateStatus(String username) {
        try {
            String ticketId = ticketIdField.getText();
            if (ticketId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Ticket ID");
                return;
            }
            String urlString = "http://localhost:8080/api/tickets/" + ticketId + "/status?username=" + username + "&newStatus=" + statusField.getText();
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            ticketList.append("Status Updated for Ticket ID " + ticketId + ": " + response + "\n");

            in.close();
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating status: " + ex.getMessage());
        }
    }

    private void addComment(String username) {
        try {
            String ticketId = ticketIdField.getText();
            if (ticketId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Ticket ID");
                return;
            }
            String urlString = "http://localhost:8080/api/tickets/" + ticketId + "/comments?username=" + username;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = String.format("{\"content\":\"%s\"}", commentField.getText());
            conn.getOutputStream().write(jsonInput.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            ticketList.append("Comment Added to Ticket ID " + ticketId + ": " + response + "\n");

            in.close();
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding comment: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String username = JOptionPane.showInputDialog("Enter your username:");
            if (username != null && !username.trim().isEmpty()) {
                new TicketClient(username);
            } else {
                System.exit(0);
            }
        });
    }
}