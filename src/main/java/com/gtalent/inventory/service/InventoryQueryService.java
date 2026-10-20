package com.gtalent.inventory.service;

import com.gtalent.inventory.domain.Stock;
import com.gtalent.inventory.domain.StockTransfer;
import com.gtalent.inventory.repository.StockRepository;
import com.gtalent.inventory.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class InventoryQueryService {
    private final StockRepository stockRepository;
    private final TransferRepository transferRepository;

    public InventoryQueryService(
            StockRepository stockRepository,
            TransferRepository transferRepository) {
        this.stockRepository = stockRepository;
        this.transferRepository = transferRepository;
    }

    public List<Stock> findAllStocks() {
        List<Stock> stocks = stockRepository.findAll();
        stocks.sort(Comparator.comparing(Stock::getWarehouse)
                .thenComparing(stock -> stock.getProduct().getId()));
        return stocks;
    }

    public Optional<Stock> findStock(String warehouse, int productId) {
        return stockRepository.findByWarehouseAndProduct(warehouse, productId)
                .stream()
                .findFirst();
    }

    public List<StockTransfer> findAllTransfers() {
        return transferRepository.findAll();
    }
}

