package com.example.tracker_service.services;

import com.example.tracker_service.config.RabbitMQConfig;
import com.example.tracker_service.dtos.PriceDropEvent;
import com.example.tracker_service.entities.Product;
import com.example.tracker_service.repositories.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Product updateProductPrice(Long productId, BigDecimal newPrice) {
        // 1. Buscamos el producto en la BD
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        BigDecimal oldPrice = product.getCurrentPrice();

        // 2. Actualizamos y guardamos el nuevo precio
        product.setCurrentPrice(newPrice);
        Product savedProduct = productRepository.save(product);

        // 3. LA MAGIA: Si el precio nuevo es menor al viejo, publicamos el evento
        if (newPrice.compareTo(oldPrice) < 0) {
            PriceDropEvent event = new PriceDropEvent(product.getName(), oldPrice, newPrice);

            // Enviamos al Exchange con nuestra Routing Key
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    event
            );
            System.out.println("📉 ¡Evento de bajada de precio publicado para: " + product.getName() + "!");
        }

        return savedProduct;
    }
}
