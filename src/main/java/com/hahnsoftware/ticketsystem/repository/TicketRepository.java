package com.hahnsoftware.ticketsystem.repository;

import com.hahnsoftware.ticketsystem.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
}