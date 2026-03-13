# Smart AgriNepal Cooperative Management System

## Version History

| Version | Date       | Description                                      |
|---------|------------|--------------------------------------------------|
| 1.0.0   | 2025-03-12 | Initial release - Full system implementation     |

---

## Project Overview

**System Name:** Smart AgriNepal Cooperative Management System  
**Cooperative:** Janasewa Krishi Sahakari  
**Districts Covered:** Chitwan, Dang, Rupandehi  
**Technology:** Java 17+ with JavaFX (Desktop Application)  
**Persistence:** File-based (Object Serialization) — No Database  
**Architecture:** MVC (Model-View-Controller) with DAO and DTO patterns  

---

## In-Scope Features

### Core Features
1. **User Authentication & Role-Based Access**
   - Login system with Admin, Cooperative Officer, and Farmer roles
   - Password-based authentication with role-specific dashboards
   - Session management

2. **Farmer Profile Management (CRUD)**
   - Register, update, view, delete farmer profiles
   - Duplicate detection by name + district
   - Search and filter by district, crop type, status

3. **Farm Plot Management**
   - Add, edit, remove farm plots linked to farmers
   - Track plot size (in Ropani/Bigha), soil type, irrigation status

4. **Crop Management**
   - Maintain catalog of crops (Rice, Maize, Wheat, Mustard, Vegetables)
   - Crop-season compatibility mapping
   - Crop details (growth duration, water needs, recommended soil)

5. **Crop Planning with Season Advisory**
   - Create crop plans linking farmer → plot → crop → season
   - Nepali season-based validation (6 seasons: Basanta, Grishma, Barsha, Sharad, Hemanta, Shishir)
   - Crop-season mismatch warnings
   - Planting and harvesting date tracking

6. **District-wise Market Prices**
   - Track daily market prices per crop per district
   - Price comparison across districts
   - Historical price viewing

7. **Audit Trail**
   - Log all CRUD operations with timestamp, user, action
   - View audit history with filters
   - Non-deletable audit records

8. **Dashboard & Analytics**
   - Total farmers, plots, active crop plans
   - Crop distribution charts
   - District-wise farmer distribution
   - Season advisory summary

### Extra Features (Beyond Requirements)
9. **Weather Alert System**
   - Create and broadcast weather alerts per district
   - Severity levels (INFO, WARNING, SEVERE, CRITICAL)
   - Alert type categorization (Flood, Drought, Frost, Storm, Heatwave, General)
   - Active/expired alert management
   - Farmer notification on dashboard

10. **Advanced Crop Analytics**
    - Crop-season suitability scoring
    - Recommended crops per season display
    - Yield estimation based on plot size and crop type

11. **Data Export**
    - Export farmer data to text files
    - Export market prices to text files

---

## Out-of-Scope

| Feature                        | Reason                                              |
|--------------------------------|-----------------------------------------------------|
| Database Storage (SQL/NoSQL)   | Assignment constraint — file I/O only               |
| Cloud/Network Storage          | Assignment constraint — offline-first, local only    |
| Web Service / REST API         | Assignment constraint — desktop app, method calls    |
| Internet Connectivity          | System designed for offline rural areas              |
| Real-time Weather Data (API)   | Offline system — weather alerts are manually entered |
| Mobile Application             | Desktop-only requirement                             |
| Multi-language / Localization  | English-only for this version                        |
| Payment / Financial Module     | Not part of cooperative management scope             |
| GPS / Map Integration          | Requires internet; simplified to text-based location |
| Image Upload for Farms         | Complexity beyond assignment scope                   |
| Email/SMS Notifications        | Requires internet connectivity                       |
| Report Generation (PDF)        | Beyond assignment scope; text export provided        |

---

## Architecture & Design Patterns

### MVC Pattern
```
Model       → Data entities (Farmer, Crop, FarmPlot, etc.)
View        → JavaFX UI components (programmatic, no FXML)
Controller  → Business logic coordination between View and Service
```

### DAO Pattern (File-Based)
```
GenericDAO<T>       → Interface defining CRUD operations
FileBasedDAO<T>     → Abstract class implementing file I/O via Object Serialization
FarmerDAO, etc.     → Concrete implementations for each entity
```

### DTO Pattern
```
DashboardDTO        → Aggregated dashboard statistics
CropAdvisoryDTO     → Crop-season recommendation data
```

### OOP Concepts Used
- **Encapsulation:** All model fields are private with getters/setters
- **Inheritance:** User → Farmer, Admin, CooperativeOfficer hierarchy
- **Abstraction:** Abstract User class, GenericDAO interface
- **Polymorphism:** Method overriding in DAO implementations, User.getRole()
- **Interface:** GenericDAO interface, Serializable
- **Instantiation:** Factory-style object creation in services

---

## Data Storage

All data stored via Java Object Serialization in the `data/` directory:

| File               | Content                  | Format              |
|--------------------|--------------------------|---------------------|
| `users.dat`        | User accounts            | Object Serialization|
| `farmers.dat`      | Farmer profiles          | Object Serialization|
| `farmplots.dat`    | Farm plot records         | Object Serialization|
| `crops.dat`        | Crop catalog             | Object Serialization|
| `cropplans.dat`    | Crop planning records    | Object Serialization|
| `marketprices.dat` | Market price entries     | Object Serialization|
| `weatheralerts.dat`| Weather alert records    | Object Serialization|
| `auditlog.dat`     | Audit trail entries      | Object Serialization|

---

## Project Structure

```
SmartAgriNepal/
├── src/com/agrinepal/
│   ├── SmartAgriNepalLauncher.java       # Plain launcher entry point for IDE/Maven execution
│   ├── App.java                          # JavaFX application and UI composition
│   ├── model/
│   │   ├── District.java                 # Enum: Chitwan, Dang, Rupandehi
│   │   ├── Season.java                   # Enum: 6 Nepali seasons
│   │   ├── UserRole.java                 # Enum: ADMIN, OFFICER, FARMER
│   │   ├── AlertSeverity.java            # Enum: INFO, WARNING, SEVERE, CRITICAL
│   │   ├── AlertType.java                # Enum: Flood, Drought, etc.
│   │   ├── SoilType.java                 # Enum: Clay, Sandy, Loamy, etc.
│   │   ├── User.java                     # Abstract base user class
│   │   ├── Farmer.java                   # Farmer entity
│   │   ├── FarmPlot.java                 # Farm plot entity
│   │   ├── Crop.java                     # Crop entity
│   │   ├── CropPlan.java                 # Crop plan entity
│   │   ├── MarketPrice.java              # Market price entity
│   │   ├── WeatherAlert.java             # Weather alert entity
│   │   └── AuditLog.java                 # Audit log entity
│   ├── dao/
│   │   ├── GenericDAO.java               # DAO interface
│   │   ├── FileBasedDAO.java             # Abstract file-based DAO
│   │   ├── UserDAO.java                  # User data access
│   │   ├── FarmerDAO.java                # Farmer data access
│   │   ├── FarmPlotDAO.java              # Farm plot data access
│   │   ├── CropDAO.java                  # Crop data access
│   │   ├── CropPlanDAO.java              # Crop plan data access
│   │   ├── MarketPriceDAO.java           # Market price data access
│   │   ├── WeatherAlertDAO.java          # Weather alert data access
│   │   └── AuditLogDAO.java              # Audit log data access
│   ├── dto/
│   │   ├── DashboardDTO.java             # Dashboard statistics
│   │   └── CropAdvisoryDTO.java          # Crop advisory data
│   ├── service/
│   │   ├── AuthService.java              # Authentication logic
│   │   ├── FarmerService.java            # Farmer business logic
│   │   ├── CropPlanService.java          # Crop plan + advisory logic
│   │   ├── MarketPriceService.java       # Market price logic
│   │   ├── WeatherAlertService.java      # Weather alert logic
│   │   ├── AuditService.java             # Audit logging
│   │   └── DashboardService.java         # Dashboard aggregation
│   ├── util/
│   │   ├── FileUtil.java                 # File I/O utilities
│   │   ├── ValidationUtil.java           # Input validation
│   │   ├── NepaliSeasonUtil.java         # Season-crop mapping
│   │   └── IdGenerator.java              # Unique ID generation
│   ├── view/
│   │   ├── LoginView.java                # Login screen
│   │   ├── DashboardView.java            # Main dashboard
│   │   ├── FarmerManagementView.java     # Farmer CRUD UI
│   │   ├── FarmPlotView.java             # Farm plot management UI
│   │   ├── CropManagementView.java       # Crop catalog UI
│   │   ├── CropPlanView.java             # Crop planning UI
│   │   ├── MarketPriceView.java          # Market price UI
│   │   ├── WeatherAlertView.java         # Weather alerts UI
│   │   └── AuditLogView.java             # Audit log UI
│   └── controller/
│       ├── LoginController.java          # Login logic
│       ├── DashboardController.java      # Dashboard logic
│       ├── FarmerController.java         # Farmer management logic
│       ├── CropPlanController.java       # Crop plan logic
│       ├── MarketPriceController.java    # Market price logic
│       └── WeatherAlertController.java   # Weather alert logic
├── data/                                 # Auto-created data directory
├── documentation.md                      # This file
└── README.md                             # Build & run instructions
```

---

## Nepali Seasons & Crop Mapping

| Season   | Nepali Name | Months   | Suitable Crops                    |
|----------|-------------|----------|-----------------------------------|
| Basanta  | बसन्त      | Feb-Apr  | Rice seedlings, Vegetables        |
| Grishma  | ग्रीष्म     | May-Jun  | Maize, Vegetables                 |
| Barsha   | बर्षा       | Jul-Aug  | Rice (main planting)              |
| Sharad   | शरद        | Sep-Oct  | Wheat prep, Mustard               |
| Hemanta  | हेमन्त      | Nov-Dec  | Wheat, Mustard                    |
| Shishir  | शिशिर      | Jan-Feb  | Vegetables, Wheat harvest         |

---

## User Roles & Permissions

| Feature              | Admin | Officer | Farmer |
|----------------------|-------|---------|--------|
| Manage Users         | ✅    | ❌      | ❌     |
| Manage Farmers       | ✅    | ✅      | ❌     |
| Manage Farm Plots    | ✅    | ✅      | ✅*    |
| Manage Crops         | ✅    | ✅      | ❌     |
| Create Crop Plans    | ✅    | ✅      | ✅*    |
| View Market Prices   | ✅    | ✅      | ✅     |
| Update Market Prices | ✅    | ✅      | ❌     |
| Weather Alerts       | ✅    | ✅      | View   |
| View Audit Logs      | ✅    | ✅      | ❌     |
| Dashboard            | ✅    | ✅      | ✅     |

*Farmers can only manage their own plots and crop plans.

---

## How to Build & Run

Use `README.md` for the full build and run instructions.

Runtime note:
- Run `SmartAgriNepalLauncher` as the direct IDE entry point.
- `App` remains the JavaFX `Application` subclass, but the dedicated launcher avoids JavaFX runtime bootstrap errors in plain IDE application runs.

---

## Team Contribution Areas

| Module                  | Key Classes                                    |
|-------------------------|------------------------------------------------|
| Authentication & Users  | User, AuthService, LoginView, LoginController  |
| Farmer Management       | Farmer, FarmerDAO, FarmerService, FarmerView    |
| Farm Plot & Crop Plan   | FarmPlot, CropPlan, CropPlanService, Views     |
| Market Prices           | MarketPrice, MarketPriceService, Views         |
| Weather Alerts          | WeatherAlert, WeatherAlertService, Views       |
| Dashboard & Analytics   | DashboardService, DashboardView, DTOs          |
