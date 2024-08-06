package com.example.invoiceclientapp.controller;

import com.example.invoiceclientapp.model.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.invoiceclientapp.model.Invoice;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

public class HelloController {

    @FXML
    private ListView<String> lvInvoiceCollection;

    @FXML
    private TextField lblRecipientId;

    @FXML
    private TextField lblDate;

    @FXML
    private TextField lblDescription;

    @FXML
    private TextField lblAmount;

    @FXML
    private Button btnCreateInvoice;

    @FXML
    private Button btnRefreshInvoiceList;

    @FXML
    public void initialize() {
        // Initialization code, if needed
    }

    @FXML
    private void createInvoice() {
        try {
            // Create a new Invoice object with form data
            Invoice invoice = new Invoice(
                    null, // ID will be set by the server
                    lblRecipientId.getText(),
                    lblDate.getText(),
                    lblDescription.getText(),
                    lblAmount.getText(),
                    null // initial encoded data is null
            );

            // Convert the Invoice object to JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String invoiceJson = objectMapper.writeValueAsString(invoice);

            // Encode the JSON string in Base64
            String encodedInvoice = Base64.getEncoder().encodeToString(invoiceJson.getBytes(StandardCharsets.UTF_8));

            // Prepare the final payload
            String jsonPayload = "{\"encodedInvoice\":\"" + encodedInvoice + "\"}";
            //String jsonPayload = "{\"invoiceData\":\"" + encodedInvoice + "\"}"; // Example of a different field name


            // Send the HTTP POST request
            URL url = new URL("http://localhost:8080/api/invoices");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setRequestProperty("Authorization", "Bearer YOUR_ACCESS_TOKEN"); // Add if server requires authentication
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response (optional)
            int responseCode = connection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

            // You can handle the response here if necessary
            /*BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print or process the response
            System.out.println("Response: " + response.toString());*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshInvoiceList() {
        // Assume `currentUserId` is obtained when the user logs in or is set when the application starts
        String currentUserId = "1234"; // Replace with actual user ID retrieval mechanism

        try {
            // Send GET request to retrieve invoices addressed to the current user's ID
            URL url = new URL("http://localhost:8080/api/invoices?recipientId=" + currentUserId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            //connection.setRequestProperty("Authorization", "Bearer YOUR_ACCESS_TOKEN"); // If needed

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Process the response
            ObjectMapper objectMapper = new ObjectMapper();
            // Assuming the server returns a list of invoices in JSON format
            List<Invoice> invoices = objectMapper.readValue(response.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));

            // Update the ListView
            //lvInvoiceCollection.getItems().clear();
            for (Invoice invoice : invoices) {
                // Decode the Base64 encoded invoice data
                /*if (invoice.getEncodedData() != null) {
                    String invoiceJson = new String(Base64.getDecoder().decode(invoice.getEncodedData()), StandardCharsets.UTF_8);
                    Invoice decodedInvoice = objectMapper.readValue(invoiceJson, Invoice.class);
                    // Add the invoice to the ListView
                    lvInvoiceCollection.getItems().add(decodedInvoice.toString());
                } else {
                    System.out.println("Encoded data is null for invoice with ID " + invoice.getId());
                }*/
                lvInvoiceCollection.getItems().add(invoice.toString());
            }

            // Send DELETE requests to remove the processed invoices
            for (Invoice invoice : invoices) {
                URL deleteUrl = new URL("http://localhost:8080/api/invoices/" + invoice.getId());
                HttpURLConnection deleteConnection = (HttpURLConnection) deleteUrl.openConnection();
                deleteConnection.setRequestMethod("DELETE");
                deleteConnection.setRequestProperty("Authorization", "Bearer YOUR_ACCESS_TOKEN"); // If needed
                int deleteResponseCode = deleteConnection.getResponseCode();
                System.out.println("DELETE Response Code :: " + deleteResponseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
