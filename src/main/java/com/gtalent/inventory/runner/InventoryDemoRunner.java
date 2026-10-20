package com.gtalent.inventory.runner;

import com.gtalent.inventory.domain.Stock;
import com.gtalent.inventory.domain.StockTransfer;
import com.gtalent.inventory.service.InventoryQueryService;
import com.gtalent.inventory.service.TransferService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InventoryDemoRunner implements CommandLineRunner {
    private final InventoryQueryService inventoryQueryService;
    private final TransferService transferService;

    public InventoryDemoRunner(
            InventoryQueryService inventoryQueryService,
            TransferService transferService) {
        this.inventoryQueryService = inventoryQueryService;
        this.transferService = transferService;
    }

    @Override
    public void run(String... args) {
        printInventory("Inventory Before Transfer");
        printLookup("Taichung", 101);

        boolean transferred = transferService.transfer(101, "Taipei", "Taichung", 4);
        System.out.println("Transfer 4 keyboards succeeded: " + transferred);

        boolean zeroTransferred = transferService.transfer(102, "Taipei", "Taichung", 0);
        System.out.println("Transfer 0 mice succeeded: " + zeroTransferred);
        System.out.println();

        printInventory("Inventory After Transfer");
        printTransferHistory();
    }

    private void printInventory(String title) {
        System.out.println("===== " + title + " =====");
        for (Stock stock : inventoryQueryService.findAllStocks()) {
            System.out.printf(
                    "%-8s %-8s quantity: %d%n",
                    stock.getWarehouse(),
                    stock.getProduct().getName(),
                    stock.getQuantity()
            );
        }
        System.out.println();
    }

    private void printLookup(String warehouse, int productId) {
        Stock stock = inventoryQueryService.findStock(warehouse, productId).orElse(null);
        if (stock == null) {
            System.out.println("Lookup result: not found");
            return;
        }

        System.out.printf(
                "Lookup %s/product %d -> %s, quantity: %d%n%n",
                warehouse,
                productId,
                stock.getWarehouse(),
                stock.getQuantity()
        );
    }

    private void printTransferHistory() {
        System.out.println("===== Transfer History =====");
        for (StockTransfer transfer : inventoryQueryService.findAllTransfers()) {
            System.out.printf(
                    "#%d %s: %s -> %s, quantity: %d%n",
                    transfer.getId(),
                    transfer.getProduct().getName(),
                    transfer.getSourceWarehouse(),
                    transfer.getDestinationWarehouse(),
                    transfer.getQuantity()
            );
        }
        System.out.println("History count: " + inventoryQueryService.findAllTransfers().size());
    }
}

