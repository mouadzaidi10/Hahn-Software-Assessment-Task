package com.hahnsoftware.ticketsystem.service;

import com.hahnsoftware.ticketsystem.model.AuditLog;
import com.hahnsoftware.ticketsystem.model.Comment;
import com.hahnsoftware.ticketsystem.model.Ticket;
import com.hahnsoftware.ticketsystem.model.User;
import com.hahnsoftware.ticketsystem.repository.AuditLogRepository;
import com.hahnsoftware.ticketsystem.repository.CommentRepository;
import com.hahnsoftware.ticketsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AuditLogRepository auditLogRepository;
    @Autowired
    private UserService userService;

    public Ticket createTicket(Ticket ticket, String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword("defaultPass");
            user.setRole("EMPLOYEE"); // Default to EMPLOYEE for now
            user = userService.saveUser(user);
        }
        ticket.setUser(user);
        ticket.setCreationDate(LocalDateTime.now());
        ticket.setStatus("New");
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTickets(String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if ("IT_SUPPORT".equals(user.getRole())) {
            return ticketRepository.findAll(); // IT Support sees all tickets
        } else {
            return ticketRepository.findByUserId(user.getId()); // Employees see only their own
        }
    }


    public Comment addComment(Long ticketId, String content, String username) {
        User user = userService.findByUsername(username);
        if (user == null || !"IT_SUPPORT".equals(user.getRole())) {
            throw new RuntimeException("Only IT Support can add comments");
        }
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        Comment comment = new Comment(content, ticket, user);
        return commentRepository.save(comment);
    }


    public Ticket updateTicketStatus(Long ticketId, String newStatus, String username) {
        User user = userService.findByUsername(username);
        if (user == null || !"IT_SUPPORT".equals(user.getRole())) {
            throw new RuntimeException("Only IT Support can update ticket status");
        }
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        String oldStatus = ticket.getStatus();
        ticket.setStatus(newStatus);
        ticket = ticketRepository.save(ticket);

        AuditLog auditLog = new AuditLog();
        auditLog.setTicketId(ticketId);
        auditLog.setOldStatus(oldStatus);
        auditLog.setNewStatus(newStatus);
        auditLog.setChangeDate(LocalDateTime.now());
        auditLog.setUser(user);
        auditLogRepository.save(auditLog);

        return ticket;
    }
}