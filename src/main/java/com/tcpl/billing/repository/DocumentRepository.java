package com.tcpl.billing.repository;

import com.tcpl.billing.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
