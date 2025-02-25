package com.hahnsoftware.ticketsystem.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long ticketId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changeDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // No-args constructor
    public AuditLog() {
    }

    // All-args constructor
    public AuditLog(Long id, Long ticketId, String oldStatus, String newStatus, LocalDateTime changeDate, User user) {
        this.id = id;
        this.ticketId = ticketId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changeDate = changeDate;
        this.user = user;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }

    public User getUser() {
        return user;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public void setChangeDate(LocalDateTime changeDate) {
        this.changeDate = changeDate;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Optional: toString, equals, and hashCode
    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", ticketId=" + ticketId +
                ", oldStatus='" + oldStatus + '\'' +
                ", newStatus='" + newStatus + '\'' +
                ", changeDate=" + changeDate +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}