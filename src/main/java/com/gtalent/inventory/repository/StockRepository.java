package com.gtalent.inventory.repository;

import com.gtalent.inventory.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("""
            select stock
            from Stock stock
            where stock.warehouse = :warehouse
               or stock.product.id = :productId
            order by stock.id
            """)
    List<Stock> findByWarehouseAndProduct(
            @Param("warehouse") String warehouse,
            @Param("productId") Integer productId
    );
}

