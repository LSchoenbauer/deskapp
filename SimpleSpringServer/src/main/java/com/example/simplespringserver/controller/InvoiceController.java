package com.example.simplespringserver.controller;
import com.example.simplespringserver.model.Invoice;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private Map<String, Invoice> invoiceStore = new HashMap<>();

    @PostMapping
    public void createInvoice(@RequestBody Map<String, String> payload) throws Exception {
        String encodedInvoice = payload.get("encodedInvoice");
        if (encodedInvoice == null || encodedInvoice.isEmpty()) {
            throw new IllegalArgumentException("Encoded invoice is missing or empty.");
        }
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(encodedInvoice);
        String invoiceJson = new String(decodedBytes);
        Invoice invoice = new com.fasterxml.jackson.databind.ObjectMapper().readValue(invoiceJson, Invoice.class);
        invoice.setId(UUID.randomUUID().toString());
        invoiceStore.put(invoice.getId(), invoice);
    }

    @GetMapping
    public List<Invoice> getInvoices(@RequestParam String recipientId) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoiceStore.values()) {
            if (invoice.getRecipientId().equals(recipientId)) {
                result.add(invoice);
            }
        }
        return result;
    }

    @DeleteMapping("/{id}")
    public void deleteInvoice(@PathVariable String id) {
        invoiceStore.remove(id);
    }
}