# Smart AgriNepal Cooperative Management System

This implementation is built from [documentation.md](documentation.md) and the assignment brief in [ap_question.txt](ap_question.txt). The workspace did not include `question.pdf`, so the app was aligned with the available brief content.

## What This Program Implements

This Java 17 + JavaFX desktop app implements all in-scope features listed in the project documentation:

1. User authentication and role-based access (Admin, Officer, Farmer)
2. Farmer profile CRUD with duplicate detection
3. Farm plot management linked to farmers
4. Crop catalog management with crop-season compatibility fields
5. Crop planning with season advisory and mismatch warning
6. District-wise market prices with history and cross-district comparison
7. Append-only audit trail for system operations
8. Dashboard analytics (counts + distributions + season summary)
9. Weather alert system (manual offline alerts by district/severity/type)
10. Advanced crop analytics (suitability scoring + estimated yield)
11. Data export to text files

Out-of-scope items are not implemented (database, web APIs, cloud/network storage, external weather APIs, mobile app, GPS/maps, SMS/email, PDF reporting, payment module, etc.).

## Tech Stack and Why

- Java 17: Required modern OOP support and stable LTS runtime.
- JavaFX: Desktop UI toolkit for offline-first GUI.
- Maven: Dependency/build management and reproducible runs.
- Java Object Serialization (`.dat` files): Satisfies assignment requirement for local file persistence without SQL.
- MVC + DAO + DTO: Matches assignment design expectations and keeps business logic separated from UI.

## Project Structure

- [src/main/java/com/agrinepal/SmartAgriNepalLauncher.java](src/main/java/com/agrinepal/SmartAgriNepalLauncher.java): Dedicated launcher class. Use this as the direct IDE entry point when you want to avoid JavaFX runtime bootstrap issues from running the JavaFX Application subclass directly.
- [src/main/java/com/agrinepal/App.java](src/main/java/com/agrinepal/App.java): Main JavaFX application. Contains login flow, role-aware tabs, and UI event wiring.
- [src/main/java/com/agrinepal/model](src/main/java/com/agrinepal/model): Domain models/enums (Farmer, Crop, CropPlan, MarketPrice, WeatherAlert, AuditLog, User hierarchy, and shared enums).
- [src/main/java/com/agrinepal/dao](src/main/java/com/agrinepal/dao): Generic DAO API + file-based DAO implementation + entity DAOs mapped to `.dat` files.
- [src/main/java/com/agrinepal/service](src/main/java/com/agrinepal/service): Business rules and use-case logic.
- [src/main/java/com/agrinepal/dto](src/main/java/com/agrinepal/dto): Aggregated transfer objects for dashboard and advisory results.
- [src/main/java/com/agrinepal/util](src/main/java/com/agrinepal/util): Utility classes for IDs, validation, file IO, season mapping.
- [src/main/java/com/agrinepal/controller](src/main/java/com/agrinepal/controller): Thin controller layer used to reflect MVC design intent.
- [data](data): Auto-populated serialized storage files.

## Deep Functional Walkthrough

### 1) Authentication + Session + Roles

Core classes:
- [src/main/java/com/agrinepal/service/AuthService.java](src/main/java/com/agrinepal/service/AuthService.java)
- [src/main/java/com/agrinepal/model/User.java](src/main/java/com/agrinepal/model/User.java)
- [src/main/java/com/agrinepal/model/AdminUser.java](src/main/java/com/agrinepal/model/AdminUser.java)
- [src/main/java/com/agrinepal/model/CooperativeOfficerUser.java](src/main/java/com/agrinepal/model/CooperativeOfficerUser.java)
- [src/main/java/com/agrinepal/model/FarmerUser.java](src/main/java/com/agrinepal/model/FarmerUser.java)

How it works:
- Users are persisted in `data/users.dat` via [src/main/java/com/agrinepal/dao/UserDAO.java](src/main/java/com/agrinepal/dao/UserDAO.java).
- On first run, `AuthService` seeds default credentials:
  - `admin` / `admin123`
  - `officer1` / `officer123`
- Login checks username/password and stores session in `currentUser` in [src/main/java/com/agrinepal/App.java](src/main/java/com/agrinepal/App.java).
- UI tabs are role-gated:
  - Admin: full access + user management
  - Officer: operational modules
  - Farmer: limited to own plot/plan actions, price viewing, dashboard, alert viewing, export

### 2) Farmer Profile Management (CRUD + Duplicate Prevention)

Core classes:
- [src/main/java/com/agrinepal/service/FarmerService.java](src/main/java/com/agrinepal/service/FarmerService.java)
- [src/main/java/com/agrinepal/dao/FarmerDAO.java](src/main/java/com/agrinepal/dao/FarmerDAO.java)
- [src/main/java/com/agrinepal/model/Farmer.java](src/main/java/com/agrinepal/model/Farmer.java)

How it works:
- Create: validates required name and blocks duplicates using `(name + district)` matching.
- Read/Search: supports keyword filtering through `search(...)`.
- Update: updates selected farmer.
- Delete: deletes selected farmer profile.

### 3) Farm Plot Management

Core classes:
- [src/main/java/com/agrinepal/service/FarmPlotService.java](src/main/java/com/agrinepal/service/FarmPlotService.java)
- [src/main/java/com/agrinepal/model/FarmPlot.java](src/main/java/com/agrinepal/model/FarmPlot.java)

How it works:
- Plots store `farmerId`, location, size, unit (`ROPANI`/`BIGHA`), soil type, irrigation flag.
- Validation enforces size > 0 and valid land unit.
- Farmer users can only view/create for their own `farmerId` when that account is linked.

### 4) Crop Management

Core classes:
- [src/main/java/com/agrinepal/service/CropService.java](src/main/java/com/agrinepal/service/CropService.java)
- [src/main/java/com/agrinepal/model/Crop.java](src/main/java/com/agrinepal/model/Crop.java)

How it works:
- Maintains crop catalog with growth days, water need, recommended soil, and suitable seasons.
- Seeds default major crops (Rice, Maize, Wheat, Mustard, Vegetables).

### 5) Crop Planning + Season Advisory

Core classes:
- [src/main/java/com/agrinepal/service/CropPlanService.java](src/main/java/com/agrinepal/service/CropPlanService.java)
- [src/main/java/com/agrinepal/util/NepaliSeasonUtil.java](src/main/java/com/agrinepal/util/NepaliSeasonUtil.java)
- [src/main/java/com/agrinepal/dto/CropAdvisoryDTO.java](src/main/java/com/agrinepal/dto/CropAdvisoryDTO.java)

How it works:
- Crop plan links farmer, plot, crop, season, planting date, computed harvest date.
- Advisory note is generated automatically:
  - suitable season -> positive note
  - mismatch -> warning note
- Advanced analytics are generated via `advisory(...)`:
  - suitability score (0-100)
  - recommended crops for the selected season
  - estimated yield based on plot size and suitability

### 6) Market Price Management

Core classes:
- [src/main/java/com/agrinepal/service/MarketPriceService.java](src/main/java/com/agrinepal/service/MarketPriceService.java)
- [src/main/java/com/agrinepal/model/MarketPrice.java](src/main/java/com/agrinepal/model/MarketPrice.java)

How it works:
- Stores per-crop, per-district daily prices.
- History view returns latest-first timeline for selected crop.
- Comparison output shows most recent known price by district.

### 7) Audit Trail (Append-Only)

Core classes:
- [src/main/java/com/agrinepal/service/AuditService.java](src/main/java/com/agrinepal/service/AuditService.java)
- [src/main/java/com/agrinepal/dao/AuditLogDAO.java](src/main/java/com/agrinepal/dao/AuditLogDAO.java)

How it works:
- Every important action logs timestamp, user, action, and details.
- Audit DAO overrides delete and throws exception, enforcing non-deletable records.
- UI provides keyword filtering for operational review.

### 8) Dashboard and Analytics

Core classes:
- [src/main/java/com/agrinepal/service/DashboardService.java](src/main/java/com/agrinepal/service/DashboardService.java)
- [src/main/java/com/agrinepal/dto/DashboardDTO.java](src/main/java/com/agrinepal/dto/DashboardDTO.java)

How it works:
- Aggregates:
  - total farmers
  - total plots
  - active plans
  - crop distribution
  - district-wise farmer distribution
  - season recommendation summary
- Dashboard tab can refresh this snapshot on demand.

### 9) Weather Alert System

Core classes:
- [src/main/java/com/agrinepal/service/WeatherAlertService.java](src/main/java/com/agrinepal/service/WeatherAlertService.java)
- [src/main/java/com/agrinepal/model/WeatherAlert.java](src/main/java/com/agrinepal/model/WeatherAlert.java)

How it works:
- Manual offline alert creation by district, severity, type, message, active date range.
- `isActive()` checks current date against start/end bounds.
- Farmer view shows active alerts.

### 10) Data Export (Text Files)

Core classes:
- [src/main/java/com/agrinepal/service/ExportService.java](src/main/java/com/agrinepal/service/ExportService.java)

How it works:
- Exports farmers to `exports/farmers.txt`
- Exports market prices to `exports/market_prices.txt`
- Output format is CSV-like text for easy offline sharing.

## Persistence Details

All persistence uses object serialization and local files only:

- `data/users.dat`
- `data/farmers.dat`
- `data/farmplots.dat`
- `data/crops.dat`
- `data/cropplans.dat`
- `data/marketprices.dat`
- `data/weatheralerts.dat`
- `data/auditlog.dat`

`FileBasedDAO<T>` in [src/main/java/com/agrinepal/dao/FileBasedDAO.java](src/main/java/com/agrinepal/dao/FileBasedDAO.java) handles CRUD generically:
- read all
- find by id
- append/save
- replace/update by ID
- delete by ID (except audit)

## Build and Run

Prerequisites:
- JDK 17+
- Maven 3.9+

Run with Maven:

```bash
mvn clean javafx:run
```

Run from an IDE:

- Run [src/main/java/com/agrinepal/SmartAgriNepalLauncher.java](src/main/java/com/agrinepal/SmartAgriNepalLauncher.java), not [src/main/java/com/agrinepal/App.java](src/main/java/com/agrinepal/App.java).
- `SmartAgriNepalLauncher` is a plain Java entry point that calls `Application.launch(App.class, args)` and avoids the JavaFX bootstrap error commonly triggered by direct IDE execution of the JavaFX `Application` subclass.
- If your IDE still fails, use the Maven goal `javafx:run` instead of a plain Application run configuration.

Package JAR (without native bundling):

```bash
mvn clean package
```

## Notes for SRS Maintenance

Since you mentioned SRS maintenance:
- This codebase already maps requirements directly into modules/services.
- You can now maintain a separate `SRS.md` or update [documentation.md](documentation.md) with:
  - use case IDs that map to service methods
  - non-functional constraints (offline-first, serialization-only)
  - traceability matrix from requirement -> class/method

If you want, the next step can be generating a complete SRS file template aligned to this implementation.
