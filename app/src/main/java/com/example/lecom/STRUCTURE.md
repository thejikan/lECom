# Project Structure

This document describes the folder structure of the lECom Android application.

## Directory Structure

```
com/example/lecom/
├── data/                    # Data layer - handles data operations
│   ├── repository/          # Repository implementations (data sources coordination)
│   ├── local/               # Local data sources
│   │   ├── dao/             # Data Access Objects (Room DAOs)
│   │   └── db/              # Database classes (Room database)
│   └── remote/              # Remote data sources
│       ├── api/             # API service interfaces and implementations
│       └── dto/             # Data Transfer Objects (API response models)
│
├── domain/                  # Domain layer - business logic
│   ├── model/               # Domain models (business entities)
│   └── usecase/             # Use cases (business logic operations)
│
├── ui/                      # UI layer - presentation
│   ├── screen/              # Screen components
│   │   ├── activity/        # Activity classes
│   │   └── fragment/        # Fragment classes
│   ├── viewmodel/           # ViewModel classes (MVVM pattern)
│   ├── adapter/             # RecyclerView/ListView adapters
│   └── component/           # Reusable UI components (custom views, widgets)
│
├── service/                 # Services layer
│   ├── api/                 # API service classes
│   └── background/          # Background services (WorkManager, JobScheduler)
│
├── utils/                   # Utility classes and helper functions
│
├── provider/                # Provider classes (dependency providers)
│
└── di/                      # Dependency Injection modules (Koin, Hilt, Dagger)

```

## Usage Guidelines

### Data Layer (`data/`)
- **repository/**: Implement repository pattern to coordinate between local and remote data sources
- **local/dao/**: Room DAO interfaces for database operations
- **local/db/**: Room database class and entities
- **remote/api/**: Retrofit interfaces and API service implementations
- **remote/dto/**: Data classes for API request/response

### Domain Layer (`domain/`)
- **model/**: Pure Kotlin data classes representing business entities
- **usecase/**: Single responsibility use cases that encapsulate business logic

### UI Layer (`ui/`)
- **screen/activity/**: Activity classes
- **screen/fragment/**: Fragment classes
- **viewmodel/**: ViewModel classes that manage UI-related data
- **adapter/**: RecyclerView adapters and ViewHolders
- **component/**: Reusable UI components (custom views, dialogs, etc.)

### Service Layer (`service/`)
- **api/**: API service implementations
- **background/**: Background services, workers, and scheduled tasks

### Utils (`utils/`)
- Helper classes, extension functions, constants, etc.

### Provider (`provider/`)
- Dependency providers and factory classes

### DI (`di/`)
- Dependency injection modules (Koin modules, Hilt modules, Dagger modules)

## Architecture Pattern

This structure follows **Clean Architecture** principles with clear separation of concerns:
- **Presentation Layer**: `ui/`
- **Domain Layer**: `domain/`
- **Data Layer**: `data/`

