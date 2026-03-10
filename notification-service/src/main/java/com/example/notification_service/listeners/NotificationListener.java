package com.example.notification_service.listeners;

import com.example.notification_service.dtos.PriceDropEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @RabbitListener(queues = "price.drop.queue")
    public void receiveMessage(PriceDropEvent event) {
        System.out.println("\n========================================");
        System.out.println("🔔 ¡NUEVA NOTIFICACIÓN RECIBIDA!");
        System.out.println("📧 Simulando envío de correo masivo a usuarios...");
        System.out.println("🎮 Producto: " + event.productName());
        System.out.println("💸 Bajó de: $" + event.oldPrice() + " a $" + event.newPrice());
        System.out.println("========================================\n");
    }
}
