package com.gtalent.inventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_transfers")
public class StockTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private String sourceWarehouse;
    private String destinationWarehouse;
    private int quantity;

    protected StockTransfer() {
    }

    public StockTransfer(
            Product product,
            String sourceWarehouse,
            String destinationWarehouse,
            int quantity) {
        this.product = product;
        this.sourceWarehouse = sourceWarehouse;
        this.destinationWarehouse = destinationWarehouse;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public String getSourceWarehouse() {
        return sourceWarehouse;
    }

    public String getDestinationWarehouse() {
        return destinationWarehouse;
    }

    public int getQuantity() {
        return quantity;
    }
}

