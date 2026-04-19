project-root/
│
├── .docker/                        # Arquivos de suporte Docker
├── src/
│   ├── main/
│   │   ├── java/com/company/project/
│   │   │   ├── application/                # Camada de Aplicação (Ports)
│   │   │   │   ├── dto/                    # Requests/Responses (LoginRequest, TokenResponse)
│   │   │   │   ├── ports/
│   │   │   │   │   ├── in/                 # Use Cases (Input) - Interfaces de entrada
│   │   │   │   │   └── out/                # Persistence Ports (Output) - Interfaces para o banco
│   │   │   │   └── service/                # Implementação dos Use Cases (Regras de Negócio)
│   │   │   │
│   │   │   ├── domain/                     # Camada de Domínio (O Coração - Sem dependências Spring)
│   │   │   │   ├── model/                  # Entidades de Negócio (User, Role, Permission)
│   │   │   │   └── exception/              # Exceções de negócio (UserInactiveException)
│   │   │   │
│   │   │   └── infrastructure/             # Camada de Infraestrutura (Adapters & Config)
│   │   │       ├── adapters/
│   │   │       │   ├── in/web/             # Controllers REST (AuthController, UserController)
│   │   │       │   └── out/persistence/    # Entidades JPA, Repositories e Mappers (MapStruct)
│   │   │       └── config/                 # Configurações do Framework
│   │   │           ├── audit/              # JPA Auditing & AuditorAware
│   │   │           ├── security/           # Spring Security, JWT Filter, BCrypt, GoogleAuthService
│   │   │           └── swagger/            # OpenAPI Config com suporte a Bearer Token
│   │   │
│   │   └── resources/
│   │       ├── db/migration/               # Scripts SQL do Flyway (V1..V4)
│   │       ├── application.yml             # Configs (DB, JWT, Google Client ID, Virtual Threads)
│   │       └── logback-spring.xml          # Configurações de log
│   │
│   └── test/                               # Testes unitários e de integração
│
├── docker-compose.yml                      # Postgres + pgAdmin
├── pom.xml                                 # Dependências (Java 21, Spring Boot 3.2+)
└── README.md                               # Documentação de como rodar o esqueleto