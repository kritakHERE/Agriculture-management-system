# FunctionDoc

This document explains the key code paths in the project using real code snippets and concrete flow descriptions. It is organized by hierarchy:

1. Entry and app startup
2. UI orchestration and tab-level functions
3. Service layer business logic
4. DAO and persistence layer
5. Utility functions
6. End-to-end interaction flows between snippets

---

## 1) Entry Layer

### 1.1 Launcher

```java
public final class SmartAgriNepalLauncher {
    private SmartAgriNepalLauncher() {
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
```

How it works:
- This is the executable entry point.
- It starts JavaFX and tells JavaFX to run App.

### 1.2 App startup

```java
@Override
public void start(Stage primaryStage) {
    this.stage = primaryStage;
    cropService.seedDefaultCrops();
    showLoginScene();
}
```

How it works:
- Saves the main Stage reference.
- Seeds default crop records only when crop storage is empty.
- Opens login screen as first UI.

---

## 2) Import and Object Wiring (App-level dependency graph)

### 2.1 Imports (functional groups)

Key imports used in App:
- DTO: CropAdvisoryDTO, DashboardDTO
- Domain models: User, Farmer, FarmPlot, CropPlan, WeatherAlert, enums
- Services: AuthService, FarmerService, FarmPlotService, CropService, CropPlanService, MarketPriceService, WeatherAlertService, DashboardService, AuditService, ExportService
- JavaFX controls/layouts: TabPane, ListView, ComboBox, TextArea, GridPane, VBox, HBox
- Utility: ValidationUtil

Why this matters:
- It shows App is the orchestrator layer that coordinates UI widgets with service calls.

### 2.2 Service objects (stateful app dependencies)

```java
private final AuthService authService = new AuthService();
private final AuditService auditService = new AuditService();
private final FarmerService farmerService = new FarmerService();
private final FarmPlotService farmPlotService = new FarmPlotService();
private final CropService cropService = new CropService();
private final CropPlanService cropPlanService = new CropPlanService();
private final MarketPriceService marketPriceService = new MarketPriceService();
private final WeatherAlertService weatherAlertService = new WeatherAlertService();
private final DashboardService dashboardService = new DashboardService();
private final ExportService exportService = new ExportService();

private Stage stage;
private User currentUser;
```

How it works:
- All tabs call these service objects directly.
- currentUser is runtime session state; role checks control which tabs/actions are enabled.

---

## 3) UI Method Chunks and Their Functionality

## 3.1 Login and dashboard routing

### Login action

```java
loginBtn.setOnAction(
    e -> authService.login(usernameField.getText(), passwordField.getText()).ifPresentOrElse(user -> {
        currentUser = user;
        auditService.log(currentUser.getUsername(), "LOGIN", "User logged in.");
        showDashboardScene();
    }, () -> showError("Invalid username or password.")));
```

What happens:
- AuthService validates credentials.
- On success, user session is set and audit event is recorded.
- App switches from login scene to tabbed dashboard.

### Role-based tab composition

```java
TabPane tabPane = new TabPane();
tabPane.getTabs().add(createDashboardTab());
if (currentUser.getRole() == UserRole.ADMIN) {
    tabPane.getTabs().add(createUsersTab());
}
if (currentUser.getRole() != UserRole.FARMER) {
    tabPane.getTabs().add(createFarmersTab());
    tabPane.getTabs().add(createCropsTab());
}
tabPane.getTabs().add(createPlotsTab());
tabPane.getTabs().add(createCropPlansTab());
tabPane.getTabs().add(createMarketPricesTab());
tabPane.getTabs().add(createWeatherTab());
if (currentUser.getRole() != UserRole.FARMER) {
    tabPane.getTabs().add(createAuditTab());
}
tabPane.getTabs().add(createExportTab());
```

What happens:
- UI permissions are enforced by conditional tab creation.
- Farmers cannot access user creation and audit trail tabs.

---

## 3.2 Farmers tab

### Data reload chunk

```java
Runnable reload = () -> {
    List<Farmer> data = farmerService.search(search.getText(), null, null, null);
    list.getItems().setAll(data.stream()
            .map(f -> f.getId() + " | " + f.getName() + " | " + f.getDistrict() + " | " + f.getPrimaryCrop()
                    + " | " + (f.isActive() ? "ACTIVE" : "INACTIVE"))
            .toList());
};
```

What it does:
- Pulls data from service search.
- Transforms model records into list lines for UI display.

### Add farmer chunk

```java
Farmer f = farmerService.create(name.getText(), district.getValue(), crop.getText(),
        "ACTIVE".equals(active.getValue()), phone.getText());
auditService.log(currentUser.getUsername(), "CREATE_FARMER", "Added farmer: " + f.getId());
reload.run();
```

What it does:
- Service validates and persists farmer.
- Audit entry is created.
- List is refreshed immediately.

---

## 3.3 Farm plots tab (ID+Name farmer selection)

### Farmer dropdown population chunk

```java
Runnable reloadFarmers = () -> farmer.getItems().setAll(farmerService.all().stream()
        .map(f -> f.getId() + " | " + f.getName())
        .toList());
```

What it does:
- UI selection uses formatted visible text but still carries ID in the left part.

### Add plot chunk

```java
String effectiveFarmerId = parseSelectedId(farmer.getValue(), "Farmer");
if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
    effectiveFarmerId = fu.getFarmerId();
}
FarmPlot p = farmPlotService.create(effectiveFarmerId, location.getText(),
        Double.parseDouble(size.getText()),
        unit.getValue(), soilType.getValue(), "YES".equals(irrigated.getValue()));
```

What it does:
- Converts selected combo text to actual ID using parseSelectedId.
- Forces self-ownership when logged in user is a farmer.
- Persists plot through service.

---

## 3.4 Crop plans tab (farmer -> plot dependency)

### Dependent dropdown chunk

```java
Runnable reloadPlotsForFarmer = () -> {
    String selectedFarmerId = null;
    if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
        selectedFarmerId = fu.getFarmerId();
    } else if (farmer.getValue() != null) {
        selectedFarmerId = parseSelectedId(farmer.getValue(), "Farmer");
    }

    if (selectedFarmerId == null) {
        plot.getItems().clear();
        return;
    }

    plot.getItems().setAll(farmPlotService.byFarmer(selectedFarmerId).stream()
            .map(p -> p.getId() + " | " + p.getLocationNote() + " | " + p.getSize() + " " + p.getUnit())
            .toList());
};
```

What it does:
- Reads farmer selection.
- Filters plots to only that farmer.
- Rebuilds plot combo list.

### Create plan chunk

```java
CropPlan plan = cropPlanService.create(effectiveFarmerId,
        parseSelectedId(plot.getValue(), "Plot"),
        parseSelectedId(crop.getValue(), "Crop"),
        season.getValue(), LocalDate.now());
```

What it does:
- Pulls selected IDs for plot/crop.
- Calls service that validates IDs and computes harvest/advisory note.

### Advisory generation chunk

```java
CropAdvisoryDTO dto = cropPlanService.advisory(season.getValue(),
        parseSelectedId(crop.getValue(), "Crop"),
        parseSelectedId(plot.getValue(), "Plot"));
```

What it does:
- Runs score-based advisory logic using selected crop + plot + season.
- Writes structured message to advisory text area.

---

## 3.5 Market prices tab

### Create market price chunk

```java
MarketPrice p = marketPriceService.create(parseSelectedId(crop.getValue(), "Crop"), district.getValue(),
        LocalDate.now(),
        Double.parseDouble(price.getText()));
```

What it does:
- Creates new market price for selected crop and district at current date.

### Compare districts chunk

```java
compare.setOnAction(e -> analysis
        .setText(marketPriceService.compareDistricts(parseSelectedId(crop.getValue(), "Crop"))));
```

What it does:
- Service extracts latest entry per district for that crop and formats report text.

---

## 3.6 Weather alerts tab (with officer confirmation)

### Alert create protection chunk

```java
if (currentUser.getRole() == UserRole.OFFICER) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setHeaderText("Confirm weather alert");
    confirm.setContentText("Create this alert for " + district.getValue() + " with "
            + severity.getValue() + " severity?");
    if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
        return;
    }
}
```

What it does:
- Adds a hard user-confirm step before persisting officer-created weather alerts.

### Persist weather alert chunk

```java
LocalDate start = LocalDate.now();
LocalDate end = start.plusDays(Integer.parseInt(days.getText()));
WeatherAlert alert = weatherAlertService.create(district.getValue(), severity.getValue(),
        type.getValue(), message.getText(), start, end);
```

What it does:
- Derives active date window from days input.
- Writes alert through service and DAO.

---

## 3.7 Shared UI helper methods

```java
private Label sectionHelp(String message) {
    Label help = new Label(message);
    help.setWrapText(true);
    help.setStyle("-fx-padding: 8; -fx-background-color: #F5F7FA; -fx-border-color: #D9DEE7;");
    return help;
}

private String parseSelectedId(String selectedValue, String fieldName) {
    ValidationUtil.require(!ValidationUtil.isBlank(selectedValue), fieldName + " is required.");
    return selectedValue.split("\\|")[0].trim();
}
```

How they work:
- sectionHelp creates reusable instruction banners for each tab.
- parseSelectedId converts combo display text like FAR-1234abcd | Ram into FAR-1234abcd.

---

## 4) Service Layer Chunks (business logic)

## 4.1 AuthService

### Login

```java
public Optional<User> login(String username, String password) {
    return userDAO.findAll().stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password))
            .findFirst();
}
```

Behavior:
- In-memory stream filter on persisted users.
- Case-insensitive username, exact password match.

### User creation by role

```java
if (role == UserRole.ADMIN) {
    user = new AdminUser(id, username, password, fullName);
} else if (role == UserRole.OFFICER) {
    user = new CooperativeOfficerUser(id, username, password, fullName);
} else {
    user = new FarmerUser(id, username, password, fullName, farmerId);
}
userDAO.save(user);
```

Behavior:
- Builds specific subclass based on role.
- Persists via UserDAO.

## 4.2 CropPlanService

### Create plan logic

```java
ValidationUtil.require(farmerDAO.findById(farmerId).isPresent(), "Farmer not found.");
ValidationUtil.require(farmPlotDAO.findById(plotId).isPresent(), "Plot not found.");
Crop crop = cropDAO.findById(cropId).orElseThrow(() -> new IllegalArgumentException("Crop not found."));

boolean suitable = crop.getSuitableSeasons().contains(season)
        || NepaliSeasonUtil.isCropSuitable(season, crop.getName());
String note = suitable ? "Season-crop combination is suitable." : "Warning: crop-season mismatch detected.";

CropPlan plan = new CropPlan(
        IdGenerator.generate("PLN"),
        farmerId,
        plotId,
        cropId,
        season,
        plantingDate,
        plantingDate.plusDays(crop.getGrowthDurationDays()),
        true,
        note);
cropPlanDAO.save(plan);
```

Behavior:
- Validates foreign keys farmer/plot/crop.
- Computes suitability note.
- Computes harvest date from crop duration.

### Advisory scoring logic

```java
int score = 50;
if (crop.getSuitableSeasons().contains(season) || NepaliSeasonUtil.isCropSuitable(season, crop.getName())) {
    score += 35;
}
if (crop.getRecommendedSoil() == plot.getSoilType()) {
    score += 15;
}
score = Math.min(score, 100);

double unitMultiplier = "BIGHA".equalsIgnoreCase(plot.getUnit()) ? 13.5 : 1.0;
double estimatedYield = plot.getSize() * unitMultiplier * (score / 100.0) * 100.0;
```

Behavior:
- Score starts from baseline 50.
- Adds season fitness and soil match bonus.
- Estimated yield scales by score and unit conversion.

## 4.3 FarmerService

### Duplicate guard on create

```java
boolean duplicate = farmerDAO.findAll().stream()
        .anyMatch(f -> f.getName().equalsIgnoreCase(name.trim()) && f.getDistrict() == district);
ValidationUtil.require(!duplicate, "Duplicate farmer found with same name and district.");
```

Behavior:
- Prevents near-duplicate records by name + district.

## 4.4 FarmPlotService

### Unit and size validation

```java
ValidationUtil.require(size > 0, "Plot size must be greater than zero.");
ValidationUtil.require("ROPANI".equalsIgnoreCase(unit) || "BIGHA".equalsIgnoreCase(unit),
        "Unit must be Ropani or Bigha.");
```

Behavior:
- Standardizes and validates core physical plot fields.

## 4.5 MarketPriceService

### Latest-by-district compare

```java
List<MarketPrice> latest = marketPriceDAO.findAll().stream()
        .filter(p -> p.getCropId().equals(cropId))
        .collect(Collectors.groupingBy(MarketPrice::getDistrict,
                Collectors.maxBy(Comparator.comparing(MarketPrice::getPriceDate))))
        .values().stream()
        .flatMap(Optional -> Optional.stream())
        .toList();
```

Behavior:
- Groups prices by district and takes max date entry for each district.

## 4.6 WeatherAlertService

```java
public List<WeatherAlert> activeByDistrict(District district) {
    return weatherAlertDAO.findAll().stream()
            .filter(a -> a.getDistrict() == district && a.isActive())
            .collect(Collectors.toList());
}
```

Behavior:
- Returns only active alerts in target district.

## 4.7 DashboardService

### Crop distribution by crop name

```java
Map<String, Long> cropDistribution = cropPlanDAO.findAll().stream()
        .collect(Collectors.groupingBy(p -> {
            Crop c = cropMap.get(p.getCropId());
            return c == null ? "Unknown" : c.getName();
        }, Collectors.counting()));
```

Behavior:
- Joins plan cropId to crop name map.
- Produces dashboard summary counts per crop.

## 4.8 AuditService

```java
public void log(String username, String action, String details) {
    AuditLog log = new AuditLog(
            IdGenerator.generate("AUD"),
            LocalDateTime.now(),
            username,
            action,
            details);
    auditLogDAO.save(log);
}
```

Behavior:
- Centralized audit event writer used from nearly every UI action.

## 4.9 ExportService

```java
private Path writeText(Path path, String content) {
    try {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.writeString(path, content);
        return path;
    } catch (IOException e) {
        throw new RuntimeException("Failed to export file: " + path, e);
    }
}
```

Behavior:
- Shared write helper ensures output directory exists before writing.

---

## 5) DAO and Persistence Hierarchy

## 5.1 Generic contract

```java
public interface GenericDAO<T> {
    List<T> findAll();
    Optional<T> findById(String id);
    void save(T entity);
    void update(T entity);
    void delete(String id);
}
```

Meaning:
- Every DAO in the project follows the same CRUD shape.

## 5.2 File-based generic implementation

```java
public abstract class FileBasedDAO<T extends Identifiable & Serializable> implements GenericDAO<T> {
    private final String path;

    @Override
    public List<T> findAll() {
        return new ArrayList<>(FileUtil.readList(path));
    }

    @Override
    public void save(T entity) {
        List<T> all = findAll();
        all.add(entity);
        FileUtil.writeList(path, all);
    }
}
```

Meaning:
- DAOs persist to .dat files through Java object serialization.
- Concrete DAOs only define file path and entity type.

## 5.3 File utility serialization

```java
public static <T extends Serializable> List<T> readList(String path) {
    File file = new File(path);
    if (!file.exists() || file.length() == 0) {
        return new ArrayList<>();
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
        Object data = ois.readObject();
        if (data instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<T> list = (List<T>) data;
            return list;
        }
        return new ArrayList<>();
    } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException("Failed to read file: " + path, e);
    }
}
```

Meaning:
- Storage format is serialized Java List objects.
- Missing/empty files safely return empty lists.

---

## 6) Utility Chunks

## 6.1 ID generation

```java
public static String generate(String prefix) {
    return prefix + "-" + UUID.randomUUID().toString().substring(0, 8);
}
```

Use:
- Prefix-based IDs across entities:
  - USR, FAR, PLT, CRP, PLN, MKT, WTH, AUD

## 6.2 Validation guard

```java
public static void require(boolean condition, String message) {
    if (!condition) {
        throw new IllegalArgumentException(message);
    }
}
```

Use:
- Uniform validation failure behavior in services and UI helper parsing.

---

## 7) Interaction Between Code Snippets (Concrete End-to-End Flows)

## Flow A: Login

1. Login button handler in App reads username/password.
2. App calls AuthService.login.
3. AuthService scans UserDAO data.
4. On success App sets currentUser and logs via AuditService.log.
5. App builds role-based tabs in showDashboardScene.

## Flow B: Create Crop Plan

1. User selects farmer, plot, crop, season in createCropPlansTab.
2. parseSelectedId converts display strings into IDs.
3. App calls CropPlanService.create.
4. Service validates farmer/plot/crop existence via DAO findById.
5. Service computes advisory note + harvest date and saves CropPlan.
6. App logs action and reloads plan list.

## Flow C: Generate Advisory

1. App reads selected crop, plot, season.
2. App calls CropPlanService.advisory.
3. Service computes suitability score and estimated yield.
4. DTO is returned and displayed in advisoryArea.

## Flow D: Create Weather Alert

1. Officer clicks Create Alert.
2. App shows confirmation dialog (officer only).
3. If confirmed, dates are computed from days field.
4. App calls WeatherAlertService.create and logs audit event.
5. Alerts list reloads.

## Flow E: Data Persistence Everywhere

1. Service method calls DAO save/update/delete.
2. DAO implementation comes from FileBasedDAO.
3. FileBasedDAO uses FileUtil.readList/writeList.
4. Changes are serialized into data/*.dat files.

---

## 8) Practical Hierarchy Summary

- Presentation/UI: App (tab methods, event handlers, role-based visibility)
- Coordination/Business rules: service classes
- Data access abstraction: GenericDAO
- Data access implementation: FileBasedDAO + concrete DAOs
- Storage mechanism: FileUtil Java serialization to .dat files
- Shared cross-cutting helpers: ValidationUtil, IdGenerator

This hierarchy is consistent across major features: Users, Farmers, Plots, Crops, Crop Plans, Market Prices, Weather Alerts, Audit, and Export.
