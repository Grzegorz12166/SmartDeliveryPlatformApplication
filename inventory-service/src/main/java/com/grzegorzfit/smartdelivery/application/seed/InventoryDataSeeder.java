package com.grzegorzfit.smartdelivery.application.seed;


import com.grzegorzfit.smartdelivery.domain.entity.Product;
import com.grzegorzfit.smartdelivery.domain.entity.StockItem;
import com.grzegorzfit.smartdelivery.domain.repository.ProductRepository;
import com.grzegorzfit.smartdelivery.domain.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import model.Currency;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class InventoryDataSeeder implements CommandLineRunner{
    private final ProductRepository productRepository;
    private final StockItemRepository stockItemRepository;

    @Override
    public void run(String... args) {
        if(productRepository.count()>0){
            return;
        }
        seedProductsFromCsv();
    }

    private void seedProductsFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("products.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());

            for (CSVRecord csvRecord : csvParser) {
                String sku = csvRecord.get("sku");
                String name = csvRecord.get("name");
                BigDecimal price = new BigDecimal(csvRecord.get("price"));
                Currency currency = Currency.valueOf(csvRecord.get("currency"));
                boolean active = Boolean.parseBoolean(csvRecord.get("active"));
                int quantity = Integer.parseInt(csvRecord.get("quantity"));
                int reserved = Integer.parseInt(csvRecord.get("reserved"));

                Product product = new Product(sku, name, price, currency, active);
                productRepository.save(product);

                StockItem stockItem = new StockItem(product, quantity, reserved);
                stockItemRepository.save(stockItem);
            }

            csvParser.close();
        } catch (Exception e) {
            throw new RuntimeException("Error loading products from CSV file", e);
        }
    }

}
