package com.hahnsoftware.ticketsystem.controller;

import com.hahnsoftware.ticketsystem.model.Comment;
import com.hahnsoftware.ticketsystem.model.Ticket;
import com.hahnsoftware.ticketsystem.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket, @RequestParam String username) {
        return ResponseEntity.ok(ticketService.createTicket(ticket, username));
    }

    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<Comment> addComment(
            @PathVariable Long ticketId,
            @RequestBody Map<String, String> body,
            @RequestParam String username) {
        return ResponseEntity.ok(ticketService.addComment(ticketId, body.get("content"), username));
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getTickets(@RequestParam String username) {
        return ResponseEntity.ok(ticketService.getTickets(username));
    }

    @PutMapping("/{ticketId}/status")
    public ResponseEntity<Ticket> updateTicketStatus(
            @PathVariable Long ticketId,
            @RequestParam String newStatus,
            @RequestParam String username) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(ticketId, newStatus, username));
    }
}