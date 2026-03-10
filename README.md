🛒 Event-Driven Price Drop Tracker | Microservices Architecture
Un sistema de notificaciones de bajada de precios basado en una Arquitectura Orientada a Eventos (EDA). Este proyecto demuestra cómo construir microservicios desacoplados, resilientes y altamente escalables utilizando el ecosistema de Spring y RabbitMQ.

---

🎯 El Problema que Resuelve
En un e-commerce monolítico tradicional, si el servicio de correos electrónicos se cae, el sistema entero falla o los correos se pierden. Este proyecto soluciona ese problema mediante asincronismo y tolerancia a fallos:

El sistema que actualiza los precios no se bloquea esperando a que se envíen los correos.

Si el servicio de notificaciones está caído, RabbitMQ encola los mensajes de forma segura hasta que el servicio vuelva a estar online (Cero pérdida de datos).

---

🏗️ Arquitectura del Sistema
El ecosistema se compone de 3 piezas fundamentales:

Tracker Service (Producer / Port: 8081): Un microservicio RESTful conectado a PostgreSQL. Expone endpoints para crear productos y actualizar precios. Si detecta una bajada de precio, publica un PriceDropEvent en el Exchange de RabbitMQ.

RabbitMQ (Message Broker): Actúa como el intermediario (middleware) enrutando los mensajes desde el Exchange hacia la Queue (price.drop.queue) mediante una Routing Key.

Notification Service (Consumer / Port: 8082): Un microservicio reactivo que escucha activamente la cola de RabbitMQ. Consume los eventos en tiempo real (o procesa el trabajo atrasado si se acaba de encender) y simula el envío masivo de correos.

---

🛠️ Stack Tecnológico
Java 21

Spring Boot 3.4 (Web, Data JPA, AMQP)

RabbitMQ (Message Broker & Management UI)

PostgreSQL 16 (Persistencia Relacional)

Docker & Docker Compose (Infraestructura como Código)

Jackson (Serialización de mensajes Java a JSON)

---

🚀 Cómo Ejecutar el Proyecto Localmente
1. Requisitos Previos
   - Tener Docker Desktop instalado y corriendo.

   - Tener JDK 21 y Maven instalados.


2. Levantar la Infraestructura (Base de Datos & Broker)
   Abre una terminal en la raíz del proyecto y ejecuta:


```
Bash

docker-compose up -d
```
💡 Puedes monitorizar los mensajes en tiempo real entrando al panel de control de RabbitMQ en http://localhost:15672 (Usuario: admin, Pass: admin123).

3. Arrancar los Microservicios
   En terminales separadas, arranca ambos servicios:

```
Bash

# Terminal 1: Arrancar el Tracker Service
cd tracker-service
./mvnw spring-boot:run

# Terminal 2: Arrancar el Notification Service
cd notification-service
./mvnw spring-boot:run
```

---

🔌 API Reference & Flujo de Prueba
Para ver la magia del desacoplamiento en acción, abre tu cliente REST favorito (Postman, Insomnia, cURL) y sigue estos pasos:

1. Registrar un Producto
   POST http://localhost:8081/api/products


```
JSON

{
"name": "PlayStation 5 Pro",
"currentPrice": 800.00
}
```
2. Disparar el Evento (Bajar el precio)
   PUT http://localhost:8081/api/products/1/price?newPrice=499.99

Resultado esperado:

- La base de datos se actualizará inmediatamente.

- El tracker-service publicará el evento de forma asíncrona.

- Verás en la consola del notification-service la recepción casi instantánea del mensaje con los detalles del descuento.

---

🛡️ Características Técnicas Destacadas
- Desacoplamiento Total: Los microservicios no se conocen entre sí. No hay llamadas HTTP directas (RestTemplate o FeignClient) entre ellos.

- Tolerancia a Fallos: Las colas en RabbitMQ están configuradas como durables (durable = true).

- Auto-conversión de Mensajes: Uso de Jackson2JsonMessageConverter para la serialización automática de objetos Java a formato JSON estándar a través de la red.

---

Autor
Davide Pinna - Backend Engineer

Construyendo arquitecturas empresariales modernas y escalables.