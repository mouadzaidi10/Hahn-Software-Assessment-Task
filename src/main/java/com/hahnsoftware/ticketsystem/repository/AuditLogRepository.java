package com.hahnsoftware.ticketsystem.repository;

import com.hahnsoftware.ticketsystem.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}