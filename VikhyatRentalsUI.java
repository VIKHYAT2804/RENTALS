import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Property {
    String name, type, area, availability, tenant, furnishing;
    double rent;

    public Property(String name, String type, String area, double rent,
                    String availability, String tenant, String furnishing) {
        this.name = name;
        this.type = type;
        this.area = area;
        this.rent = rent;
        this.availability = availability;
        this.tenant = tenant;
        this.furnishing = furnishing;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-5s | %-20s | ₹%-7.0f | %-12s | %-10s | %-15s",
                name, type, area, rent, availability, tenant, furnishing);
    }
}

public class VikhyatRentalsUI extends JFrame implements ActionListener {
    private JTextField nameField, rentField;
    private JComboBox<String> typeBox, areaBox, tenantBox, furnishingBox, availabilityBox;
    private JTextArea outputArea;
    private List<Property> properties;

    public VikhyatRentalsUI() {
        super("VIKHYAT RENTALS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null); // center the window
        setLayout(new BorderLayout(10, 10));

        properties = loadProperties("properties.csv");

        // --- Input Panel ---
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Property Filters"));

        nameField = new JTextField();
        rentField = new JTextField();

        typeBox = new JComboBox<>(new String[]{"Any", "1 BHK", "2 BHK", "3 BHK", "4 BHK"});
        areaBox = new JComboBox<>(new String[]{"Any", "BANGALORE SOUTH", "BANGALORE NORTH", "BANGALORE CENTRAL"});
        tenantBox = new JComboBox<>(new String[]{"Any", "Family", "Bachelor", "Company"});
        furnishingBox = new JComboBox<>(new String[]{"Any", "Fully Furnished", "Half Furnished", "Not Furnished"});
        availabilityBox = new JComboBox<>(new String[]{"Any", "Immediate", "After 10 days", "After 15 days", "After 30 days"});

        JPanel row1 = new JPanel(new GridLayout(1, 4, 10, 10));
        row1.add(new JLabel("Property Name:")); row1.add(nameField);
        row1.add(new JLabel("Max Rent (₹):")); row1.add(rentField);

        JPanel row2 = new JPanel(new GridLayout(1, 4, 10, 10));
        row2.add(new JLabel("Property Type:")); row2.add(typeBox);
        row2.add(new JLabel("Area:")); row2.add(areaBox);

        JPanel row3 = new JPanel(new GridLayout(1, 4, 10, 10));
        row3.add(new JLabel("Tenant:")); row3.add(tenantBox);
        row3.add(new JLabel("Furnishing:")); row3.add(furnishingBox);
        row3.add(new JLabel("Availability:")); row3.add(availabilityBox);

        inputPanel.setLayout(new GridLayout(3, 1, 10, 10));
        inputPanel.add(row1);
        inputPanel.add(row2);
        inputPanel.add(row3);

        add(inputPanel, BorderLayout.NORTH);

        // --- Output Area ---
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Matching Properties"));
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel buttonPanel = new JPanel();
        JButton submitButton = new JButton("Find Properties");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.addActionListener(this);
        buttonPanel.add(submitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        outputArea.setText(""); 
        String name = nameField.getText().trim();
        String type = (String) typeBox.getSelectedItem();
        String area = (String) areaBox.getSelectedItem();
        String tenant = (String) tenantBox.getSelectedItem();
        String furnishing = (String) furnishingBox.getSelectedItem();
        String availability = (String) availabilityBox.getSelectedItem();
        double maxRent = Double.MAX_VALUE;

        try { maxRent = Double.parseDouble(rentField.getText().trim()); } catch (Exception ex) {}

        boolean found = false;
        for (Property p : properties) {
            if (!name.isEmpty() && !p.name.equalsIgnoreCase(name)) continue;
            if (!type.equals("Any") && !p.type.equalsIgnoreCase(type)) continue;
            if (!area.equals("Any") && !p.area.equalsIgnoreCase(area)) continue;
            if (!tenant.equals("Any") && !p.tenant.equalsIgnoreCase(tenant)) continue;
            if (!furnishing.equals("Any") && !p.furnishing.equalsIgnoreCase(furnishing)) continue;
            if (!availability.equals("Any") && !p.availability.equalsIgnoreCase(availability)) continue;
            if (p.rent > maxRent) continue;

            outputArea.append(p.toString() + "\n");
            found = true;
        }

        if (!found) outputArea.setText("No matching properties found.");
    }

    private List<Property> loadProperties(String fileName) {
        List<Property> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    list.add(new Property(parts[0], parts[1], parts[2],
                            Double.parseDouble(parts[3]), parts[4], parts[5], parts[6]));
                }
            }
        } catch (Exception ex) {
            System.out.println("Error loading CSV: " + ex.getMessage());
        }
        return list;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(VikhyatRentalsUI::new);
    }
}
