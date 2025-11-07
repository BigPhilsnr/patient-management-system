# Patient Management System

A comprehensive microservices-based patient management system built with Spring Boot, featuring REST APIs, gRPC communication, event-driven architecture with Kafka, and API Gateway routing.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technologies](#technologies)
- [Services](#services)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Contributing](#contributing)

## 🎯 Overview

This Patient Management System is a distributed microservices application that demonstrates modern cloud-native development practices. It includes patient registration, billing account management, analytics tracking, and unified API gateway access.

## 🏗️ Architecture

The system consists of four main microservices:

```
┌─────────────────┐
│   API Gateway   │ (Port 4004)
│  Spring Cloud   │
└────────┬────────┘
         │
    ┌────┴──────────────────┬──────────────┐
    │                       │              │
┌───▼────────────┐  ┌──────▼─────┐  ┌────▼──────────┐
│ Patient Service│  │  Billing   │  │   Analytics   │
│  (REST API)    │  │  Service   │  │    Service    │
│   Port 4000    │  │  Port 4001 │  │   Port 4002   │
│                │  │  gRPC 9001 │  │               │
└───┬────────────┘  └──────────┬─┘  └───────┬───────┘
    │                          │            │
    │         ┌────────────────┘            │
    │         │                             │
┌───▼─────────▼───┐            ┌───────────▼────────┐
│   PostgreSQL    │            │      Kafka         │
│   Port 5432     │            │   Port 9092        │
└─────────────────┘            └────────────────────┘
```

## 🚀 Technologies

### Core Framework
- **Spring Boot 3.5.7** - Application framework
- **Java 17** - Programming language
- **Maven** - Build tool

### Microservices Stack
- **Spring Cloud Gateway 2025.0.0** - API Gateway and routing
- **Spring Data JPA** - Data persistence
- **gRPC** - Inter-service communication
- **Apache Kafka** - Event streaming

### Database & Messaging
- **PostgreSQL 16** - Primary database
- **Apache Kafka** - Message broker
- **Zookeeper** - Kafka coordination

### DevOps
- **Docker** - Containerization
- **Docker Compose** - Multi-container orchestration

## 📦 Services

### 1. Patient Service (Port 4000)
**Technology**: Spring Boot REST API with PostgreSQL

**Features**:
- Patient CRUD operations
- Patient registration and profile management
- Event publishing to Kafka on patient creation
- PostgreSQL persistence

**Endpoints**:
- `GET /patients` - List all patients
- `POST /patients` - Create a new patient
- `PUT /patients/{id}` - Update patient information
- `GET /patients/{id}` - Get patient by ID

### 2. Billing Service (Port 4001, gRPC 9001)
**Technology**: Spring Boot with gRPC

**Features**:
- Billing account management
- gRPC API for high-performance communication
- Integration with patient service

**gRPC Methods**:
- `CreateBillingAccount` - Create billing account for a patient
- Additional billing operations

### 3. Analytics Service (Port 4002)
**Technology**: Spring Boot with Kafka Consumer

**Features**:
- Real-time patient event processing
- Kafka consumer for patient events
- Analytics and reporting capabilities

**Event Processing**:
- Listens to patient creation events
- Processes and stores analytics data

### 4. API Gateway (Port 4004)
**Technology**: Spring Cloud Gateway

**Features**:
- Unified entry point for all services
- Request routing and load balancing
- Path-based routing with prefix stripping

**Routes**:
- `/api/patients/**` → Patient Service

## 📋 Prerequisites

Before running the application, ensure you have the following installed:

- **Docker** (version 20.10+)
- **Docker Compose** (version 2.0+)
- **Java 17** or higher (for local development)
- **Maven 3.9+** (for local development)
- **Git** (for version control)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/patient-management-system.git
cd patient-management-system
```

### 2. Start All Services with Docker Compose

```bash
docker-compose up -d
```

This command will:
- Build all microservice Docker images
- Start PostgreSQL database
- Start Kafka and Zookeeper
- Start all four microservices
- Set up networking between services

### 3. Verify Services are Running

```bash
docker-compose ps
```

You should see all services in the "Up" state.

### 4. Check Service Logs

```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f patient-service
docker-compose logs -f api-gateway
docker-compose logs -f billing-service
docker-compose logs -f analytics-service
```

### 5. Stop All Services

```bash
docker-compose down
```

To remove volumes (database data):
```bash
docker-compose down -v
```

## 📚 API Documentation

### Access via API Gateway

All requests should go through the API Gateway at `http://localhost:4004`

### Patient Service API

#### Create a Patient

**Via API Gateway:**
```http
POST http://localhost:4004/api/patients
Content-Type: application/json

{
  "name": "John Doe",
  "registeredDate": "2024-06-15",
  "email": "johndoe@gmail.com",
  "address": "123 Main St, Anytown, USA",
  "dateOfBirth": "1990-05-20"
}
```

**Direct to Patient Service:**
```http
POST http://localhost:4000/patients
Content-Type: application/json

{
  "name": "John Doe",
  "registeredDate": "2024-06-15",
  "email": "johndoe@gmail.com",
  "address": "123 Main St, Anytown, USA",
  "dateOfBirth": "1990-05-20"
}
```

#### Get All Patients

**Via API Gateway:**
```http
GET http://localhost:4004/api/patients
```

**Direct to Patient Service:**
```http
GET http://localhost:4000/patients
```

#### Update a Patient

**Via API Gateway:**
```http
PUT http://localhost:4004/api/patients/{id}
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "janedoe@gmail.com",
  "address": "456 Oak Ave, Newtown, USA",
  "dateOfBirth": "1992-08-15"
}
```

### Billing Service API (gRPC)

#### Create Billing Account

```http
GRPC localhost:9001/BillingService/CreateBillingAccount

{
  "patientId": "223e4567-e89b-12d3-a456-426614174014",
  "name": "John Doe",
  "email": "philmaxsnr@gmail.com"
}
```

**Note**: Use a gRPC client like [grpcurl](https://github.com/fullstorydev/grpcurl) or the IntelliJ HTTP Client for testing gRPC endpoints.

## ⚙️ Configuration

### Service Ports

| Service | HTTP Port | gRPC Port | Description |
|---------|-----------|-----------|-------------|
| API Gateway | 4004 | - | Main entry point |
| Patient Service | 4000 | - | REST API |
| Billing Service | 4001 | 9001 | REST & gRPC |
| Analytics Service | 4002 | - | Kafka Consumer |
| PostgreSQL | 5432 | - | Database |
| Kafka | 9092 | - | Message Broker |
| Zookeeper | 2181 | - | Kafka Coordination |

### Environment Variables

#### Patient Service
- `SPRING_DATASOURCE_URL`: PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address

#### Analytics Service
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`: Kafka broker address

### Database Configuration

**PostgreSQL**:
- Database: `patientdb`
- Username: `sa`
- Password: `password`
- Host: `postgres` (in Docker network) or `localhost:5432` (external)

### Kafka Configuration

**Topics**:
- Patient events are published to Kafka when patients are created/updated
- Analytics service consumes these events in real-time

**Bootstrap Servers**: `kafka:9092` (in Docker network)

## 🛠️ Development

### Building Individual Services

Each service can be built independently:

```bash
# Patient Service
cd patient-service
mvn clean package

# Billing Service
cd billing-service
mvn clean package

# Analytics Service
cd analytics-service
mvn clean package

# API Gateway
cd api-gateway
mvn clean package
```

### Running Services Locally (without Docker)

1. **Start PostgreSQL**:
```bash
docker run -d -p 5432:5432 -e POSTGRES_DB=patientdb -e POSTGRES_USER=sa -e POSTGRES_PASSWORD=password postgres:16-alpine
```

2. **Start Kafka**:
```bash
# Start Zookeeper
docker run -d -p 2181:2181 --name zookeeper confluentinc/cp-zookeeper:7.5.0

# Start Kafka
docker run -d -p 9092:9092 --name kafka --link zookeeper confluentinc/cp-kafka:7.5.0
```

3. **Update application.properties** in each service to use `localhost` instead of Docker service names

4. **Run each service**:
```bash
cd patient-service && mvn spring-boot:run
cd billing-service && mvn spring-boot:run
cd analytics-service && mvn spring-boot:run
cd api-gateway && mvn spring-boot:run
```

## 🧪 Testing

### Using HTTP Request Files

The project includes HTTP request files in the `api-requests` directory for easy testing:

```
api-requests/
├── api-gateway/
│   ├── create-patient.http
│   ├── get-patients.http
│   └── update-patient.http
└── patient-service/
    ├── create-patient.http
    ├── get-patients.http
    └── update-patient.http

grpc-requests/
└── billing-service/
    └── create-account.http
```

Use IntelliJ IDEA's HTTP Client or VS Code REST Client extension to execute these requests.

### Testing with cURL

```bash
# Create a patient
curl -X POST http://localhost:4004/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "registeredDate": "2024-06-15",
    "email": "johndoe@gmail.com",
    "address": "123 Main St, Anytown, USA",
    "dateOfBirth": "1990-05-20"
  }'

# Get all patients
curl http://localhost:4004/api/patients
```

## 🐛 Troubleshooting

### Services Not Starting

1. **Check Docker logs**:
```bash
docker-compose logs -f [service-name]
```

2. **Verify all containers are running**:
```bash
docker-compose ps
```

3. **Check for port conflicts**:
```bash
lsof -i :4000,4001,4002,4004,5432,9092
```

### Connection Refused Errors

- Ensure all services have started completely (check logs)
- Wait for database and Kafka health checks to pass
- Verify Docker network connectivity

### Database Connection Issues

- Verify PostgreSQL container is running
- Check database credentials in environment variables
- Ensure PostgreSQL health check is passing

### Kafka Connection Issues

- Verify Kafka and Zookeeper containers are running
- Check Kafka bootstrap server configuration
- Ensure Kafka health check is passing

## 📝 Project Structure

```
patient-management-system/
├── api-gateway/              # API Gateway service
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── patient-service/          # Patient management service
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── billing-service/          # Billing service with gRPC
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── analytics-service/        # Analytics service with Kafka
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── api-requests/             # HTTP request files
├── grpc-requests/            # gRPC request files
├── docker-compose.yml        # Docker Compose configuration
└── README.md                 # This file
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- Your Name - Initial work

## 🙏 Acknowledgments

- Spring Boot team for excellent documentation
- Spring Cloud Gateway for powerful routing capabilities
- Apache Kafka for reliable event streaming
- gRPC for high-performance RPC framework

## 📞 Support

For support, email your-email@example.com or open an issue in the repository.

---

**Built with ❤️ using Spring Boot and Microservices Architecture**

