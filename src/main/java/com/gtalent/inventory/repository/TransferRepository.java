package com.gtalent.inventory.repository;

import com.gtalent.inventory.domain.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<StockTransfer, Long> {
}

