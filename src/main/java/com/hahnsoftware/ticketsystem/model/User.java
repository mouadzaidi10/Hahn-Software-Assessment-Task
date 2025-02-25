package com.hahnsoftware.ticketsystem.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference // This will be serialized
    private List<Ticket> tickets;

    // Constructors, Getters, Setters (unchanged)
    public User() {}
    public User(Long id, String username, String password, String role, List<Ticket> tickets) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.tickets = tickets;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<Ticket> getTickets() { return tickets; }
    public void setTickets(List<Ticket> tickets) { this.tickets = tickets; }
}