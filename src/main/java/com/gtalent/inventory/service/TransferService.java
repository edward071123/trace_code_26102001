package com.gtalent.inventory.service;

import com.gtalent.inventory.domain.Stock;
import com.gtalent.inventory.domain.StockTransfer;
import com.gtalent.inventory.repository.StockRepository;
import com.gtalent.inventory.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {
    private StockRepository stockRepository;
    private TransferRepository transferRepository;

    public TransferService() {
    }

    @Transactional
    public boolean transfer(
            int productId,
            String sourceWarehouse,
            String destinationWarehouse,
            int quantity) {
        if (quantity < 0 || sourceWarehouse.equals(destinationWarehouse)) {
            return false;
        }

        Stock source = stockRepository
                .findByWarehouseAndProduct(sourceWarehouse, productId)
                .stream()
                .findFirst()
                .orElse(null);
        Stock destination = stockRepository
                .findByWarehouseAndProduct(destinationWarehouse, productId)
                .stream()
                .findFirst()
                .orElse(null);

        if (source == null || destination == null || source.getQuantity() < quantity) {
            return false;
        }

        source.setQuantity(source.getQuantity() - quantity);
        destination.setQuantity(destination.getQuantity() - quantity);

        stockRepository.save(source);
        stockRepository.save(destination);
        transferRepository.save(new StockTransfer(
                source.getProduct(),
                sourceWarehouse,
                destinationWarehouse,
                quantity
        ));
        return true;
    }
}

