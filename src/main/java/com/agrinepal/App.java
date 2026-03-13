package com.agrinepal;

import com.agrinepal.dto.CropAdvisoryDTO;
import com.agrinepal.dto.DashboardDTO;
import com.agrinepal.model.AlertSeverity;
import com.agrinepal.model.AlertType;
import com.agrinepal.model.CropPlan;
import com.agrinepal.model.District;
import com.agrinepal.model.FarmPlot;
import com.agrinepal.model.Farmer;
import com.agrinepal.model.FarmerUser;
import com.agrinepal.model.MarketPrice;
import com.agrinepal.model.Season;
import com.agrinepal.model.SoilType;
import com.agrinepal.model.User;
import com.agrinepal.model.UserRole;
import com.agrinepal.model.WeatherAlert;
import com.agrinepal.service.AuditService;
import com.agrinepal.service.AuthService;
import com.agrinepal.service.CropPlanService;
import com.agrinepal.service.CropService;
import com.agrinepal.service.DashboardService;
import com.agrinepal.service.ExportService;
import com.agrinepal.service.FarmPlotService;
import com.agrinepal.service.FarmerService;
import com.agrinepal.service.MarketPriceService;
import com.agrinepal.service.WeatherAlertService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App extends Application {
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        cropService.seedDefaultCrops();
        showLoginScene();
    }

    private void showLoginScene() {
        Label title = new Label("Smart AgriNepal Cooperative Management System");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        VBox root = getVBox(usernameField, title);
        root.setPadding(new Insets(24));
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 680, 420);
        stage.setTitle("Smart AgriNepal");
        stage.setScene(scene);
        stage.show();
    }

    private @NotNull VBox getVBox(TextField usernameField, Label title) {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        Label hint = new Label("Default login: admin/admin123 or officer1/officer123");

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(
                e -> authService.login(usernameField.getText(), passwordField.getText()).ifPresentOrElse(user -> {
                    currentUser = user;
                    auditService.log(currentUser.getUsername(), "LOGIN", "User logged in.");
                    showDashboardScene();
                }, () -> showError("Invalid username or password.")));

        return new VBox(12, title, usernameField, passwordField, loginBtn, hint);
    }

    private void showDashboardScene() {
        BorderPane root = new BorderPane();
        Label header = new Label("Logged in as: " + currentUser.getFullName() + " [" + currentUser.getRole() + "]");
        header.setPadding(new Insets(10));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> {
            auditService.log(currentUser.getUsername(), "LOGOUT", "User logged out.");
            currentUser = null;
            showLoginScene();
        });

        HBox top = new HBox(12, header, logoutBtn);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(8));
        root.setTop(top);

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

        root.setCenter(tabPane);
        Scene scene = new Scene(root, 1200, 760);
        stage.setScene(scene);
        stage.show();
    }

    private Tab createDashboardTab() {
        TextArea summary = new TextArea();
        summary.setEditable(false);

        Button refresh = new Button("Refresh Dashboard");
        refresh.setOnAction(e -> {
            DashboardDTO dto = dashboardService.getDashboard();
            String text = "Total Farmers: " + dto.getTotalFarmers() + "\n"
                    + "Total Plots: " + dto.getTotalPlots() + "\n"
                    + "Active Crop Plans: " + dto.getActiveCropPlans() + "\n\n"
                    + "Crop Distribution:\n" + dto.getCropDistribution() + "\n\n"
                    + "District Distribution:\n" + dto.getDistrictDistribution() + "\n\n"
                    + "Season Advisory:\n" + dto.getSeasonSummary() + "\n\n"
                    + "Active Alerts in Chitwan:\n"
                    + weatherAlertService.activeByDistrict(District.CHITWAN).stream().map(WeatherAlert::getMessage)
                            .collect(Collectors.joining("\n"));
            summary.setText(text);
        });
        refresh.fire();

        VBox box = new VBox(10, refresh, summary);
        box.setPadding(new Insets(10));
        VBox.setVgrow(summary, Priority.ALWAYS);

        Tab tab = new Tab("Dashboard", box);
        tab.setClosable(false);
        return tab;
    }

    private Tab createUsersTab() {
        TextField username = new TextField();
        TextField fullName = new TextField();
        PasswordField password = new PasswordField();
        ComboBox<UserRole> role = new ComboBox<>();
        role.getItems().addAll(UserRole.values());
        role.setValue(UserRole.OFFICER);
        TextField farmerId = new TextField();

        ListView<String> users = new ListView<>();
        Runnable reload = () -> users.getItems().setAll(authService.allUsers().stream()
                .map(u -> u.getId() + " | " + u.getUsername() + " | " + u.getRole())
                .toList());

        Button add = new Button("Create User");
        add.setOnAction(e -> {
            try {
                authService.createUser(username.getText(), password.getText(), fullName.getText(), role.getValue(),
                        farmerId.getText());
                auditService.log(currentUser.getUsername(), "CREATE_USER", "Created user: " + username.getText());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        reload.run();
        GridPane form = basicForm();
        addRow(form, 0, "Username", username);
        addRow(form, 1, "Full Name", fullName);
        addRow(form, 2, "Password", password);
        addRow(form, 3, "Role", role);
        addRow(form, 4, "Farmer ID (for farmer role)", farmerId);

        VBox left = new VBox(10, form, add);
        HBox body = new HBox(10, left, users);
        VBox.setVgrow(users, Priority.ALWAYS);
        HBox.setHgrow(users, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Users", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createFarmersTab() {
        TextField name = new TextField();
        ComboBox<District> district = new ComboBox<>();
        district.getItems().addAll(District.values());
        district.setValue(District.CHITWAN);
        TextField crop = new TextField();
        TextField phone = new TextField();
        ComboBox<String> active = new ComboBox<>();
        active.getItems().addAll("ACTIVE", "INACTIVE");
        active.setValue("ACTIVE");

        TextField search = new TextField();
        search.setPromptText("Search by ID/Name/Crop");
        ListView<String> list = new ListView<>();

        Runnable reload = () -> {
            List<Farmer> data = farmerService.search(search.getText(), null, null, null);
            list.getItems().setAll(data.stream()
                    .map(f -> f.getId() + " | " + f.getName() + " | " + f.getDistrict() + " | " + f.getPrimaryCrop()
                            + " | " + (f.isActive() ? "ACTIVE" : "INACTIVE"))
                    .toList());
        };

        Button add = new Button("Add Farmer");
        add.setOnAction(e -> {
            try {
                Farmer f = farmerService.create(name.getText(), district.getValue(), crop.getText(),
                        "ACTIVE".equals(active.getValue()), phone.getText());
                auditService.log(currentUser.getUsername(), "CREATE_FARMER", "Added farmer: " + f.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button update = new Button("Update by Selected ID");
        update.setOnAction(e -> {
            String selected = list.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select a farmer first.");
                return;
            }
            try {
                String id = selected.split("\\|")[0].trim();
                Farmer farmer = farmerService.all().stream().filter(f -> f.getId().equals(id)).findFirst()
                        .orElseThrow();
                farmer.setName(name.getText());
                farmer.setDistrict(district.getValue());
                farmer.setPrimaryCrop(crop.getText());
                farmer.setPhone(phone.getText());
                farmer.setActive("ACTIVE".equals(active.getValue()));
                farmerService.update(farmer);
                auditService.log(currentUser.getUsername(), "UPDATE_FARMER", "Updated farmer: " + farmer.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button delete = new Button("Delete Selected");
        delete.setOnAction(e -> deleteSelected(
                list,
                "farmer",
                farmerService::delete,
                "DELETE_FARMER",
                "Deleted farmer: ",
                reload));

        search.textProperty().addListener((obs, oldV, newV) -> reload.run());
        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "Name", name);
        addRow(form, 1, "District", district);
        addRow(form, 2, "Primary Crop", crop);
        addRow(form, 3, "Phone", phone);
        addRow(form, 4, "Status", active);

        VBox left = new VBox(10, form, add, update, delete, search);
        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Farmers", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createPlotsTab() {
        TextField farmerId = new TextField();
        TextField location = new TextField();
        TextField size = new TextField();
        ComboBox<String> unit = new ComboBox<>();
        unit.getItems().addAll("ROPANI", "BIGHA");
        unit.setValue("ROPANI");
        ComboBox<SoilType> soilType = new ComboBox<>();
        soilType.getItems().addAll(SoilType.values());
        soilType.setValue(SoilType.LOAMY);
        ComboBox<String> irrigated = new ComboBox<>();
        irrigated.getItems().addAll("YES", "NO");
        irrigated.setValue("YES");

        ListView<String> list = new ListView<>();
        Runnable reload = () -> {
            List<FarmPlot> plots = farmPlotService.all();
            if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
                plots = plots.stream().filter(p -> p.getFarmerId().equals(fu.getFarmerId())).toList();
            }
            list.getItems().setAll(plots.stream()
                    .map(p -> p.getId() + " | Farmer=" + p.getFarmerId() + " | " + p.getSize() + " " + p.getUnit()
                            + " | " + p.getSoilType() + " | Irrigated=" + p.isIrrigated())
                    .toList());
        };

        Button add = new Button("Add Plot");
        add.setOnAction(e -> {
            try {
                String effectiveFarmerId = farmerId.getText();
                if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
                    effectiveFarmerId = fu.getFarmerId();
                }
                FarmPlot p = farmPlotService.create(effectiveFarmerId, location.getText(),
                        Double.parseDouble(size.getText()),
                        unit.getValue(), soilType.getValue(), "YES".equals(irrigated.getValue()));
                auditService.log(currentUser.getUsername(), "CREATE_PLOT", "Added plot: " + p.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button delete = new Button("Delete Selected");
        delete.setOnAction(e -> deleteSelected(
                list,
                "plot",
                farmPlotService::delete,
                "DELETE_PLOT",
                "Deleted plot: ",
                reload));

        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "Farmer ID", farmerId);
        addRow(form, 1, "Location", location);
        addRow(form, 2, "Size", size);
        addRow(form, 3, "Unit", unit);
        addRow(form, 4, "Soil", soilType);
        addRow(form, 5, "Irrigated", irrigated);

        VBox left = new VBox(10, form, add, delete);
        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Farm Plots", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createCropsTab() {
        TextField name = new TextField();
        TextField duration = new TextField();
        TextField water = new TextField();
        ComboBox<SoilType> soil = new ComboBox<>();
        soil.getItems().addAll(SoilType.values());
        soil.setValue(SoilType.LOAMY);

        ComboBox<Season> s1 = new ComboBox<>();
        ComboBox<Season> s2 = new ComboBox<>();
        ComboBox<Season> s3 = new ComboBox<>();
        s1.getItems().addAll(Season.values());
        s2.getItems().addAll(Season.values());
        s3.getItems().addAll(Season.values());
        s1.setValue(Season.BASANTA);

        ListView<String> list = new ListView<>();
        Runnable reload = () -> list.getItems().setAll(cropService.all().stream()
                .map(c -> c.getId() + " | " + c.getName() + " | " + c.getGrowthDurationDays() + " days | Soil="
                        + c.getRecommendedSoil() + " | Seasons=" + c.getSuitableSeasons())
                .toList());

        Button add = new Button("Add Crop");
        add.setOnAction(e -> {
            try {
                cropService.create(name.getText(), Integer.parseInt(duration.getText()), water.getText(),
                        soil.getValue(),
                        Stream.of(s1.getValue(), s2.getValue(), s3.getValue()).filter(Objects::nonNull).distinct()
                                .toList());
                auditService.log(currentUser.getUsername(), "CREATE_CROP", "Added crop: " + name.getText());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button delete = new Button("Delete Selected");
        delete.setOnAction(e -> deleteSelected(
                list,
                "crop",
                cropService::delete,
                "DELETE_CROP",
                "Deleted crop: ",
                reload));

        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "Crop Name", name);
        addRow(form, 1, "Duration (days)", duration);
        addRow(form, 2, "Water Need", water);
        addRow(form, 3, "Recommended Soil", soil);
        addRow(form, 4, "Season 1", s1);
        addRow(form, 5, "Season 2", s2);
        addRow(form, 6, "Season 3", s3);

        VBox left = new VBox(10, form, add, delete);
        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Crops", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createCropPlansTab() {
        TextField farmerId = new TextField();
        TextField plotId = new TextField();
        TextField cropId = new TextField();
        ComboBox<Season> season = new ComboBox<>();
        season.getItems().addAll(Season.values());
        season.setValue(Season.BASANTA);

        ListView<String> list = new ListView<>();
        TextArea advisoryArea = new TextArea();
        advisoryArea.setEditable(false);

        Runnable reload = () -> {
            List<CropPlan> plans = cropPlanService.all();
            if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
                plans = plans.stream().filter(p -> p.getFarmerId().equals(fu.getFarmerId())).toList();
            }
            list.getItems().setAll(plans.stream()
                    .map(p -> p.getId() + " | Farmer=" + p.getFarmerId() + " | Plot=" + p.getPlotId() + " | Crop="
                            + p.getCropId() + " | " + p.getSeason() + " | " + p.getAdvisoryNote())
                    .toList());
        };

        Button add = new Button("Create Plan");
        add.setOnAction(e -> {
            try {
                String effectiveFarmerId = farmerId.getText();
                if (currentUser.getRole() == UserRole.FARMER && currentUser instanceof FarmerUser fu) {
                    effectiveFarmerId = fu.getFarmerId();
                }
                CropPlan plan = cropPlanService.create(effectiveFarmerId, plotId.getText(), cropId.getText(),
                        season.getValue(), LocalDate.now());
                auditService.log(currentUser.getUsername(), "CREATE_PLAN", "Created crop plan: " + plan.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button delete = new Button("Delete Selected");
        delete.setOnAction(e -> deleteSelected(
                list,
                "crop plan",
                cropPlanService::delete,
                "DELETE_PLAN",
                "Deleted crop plan: ",
                reload));

        Button advisory = new Button("Generate Advisory");
        advisory.setOnAction(e -> {
            try {
                CropAdvisoryDTO dto = cropPlanService.advisory(season.getValue(), cropId.getText(), plotId.getText());
                advisoryArea.setText("Season: " + dto.getSeason()
                        + "\nCrop: " + dto.getCropName()
                        + "\nSuitability Score: " + dto.getSuitabilityScore() + "/100"
                        + "\nEstimated Yield: " + String.format("%.2f", dto.getEstimatedYield())
                        + "\nRecommended Crops: " + String.join(", ", dto.getRecommendedCrops())
                        + "\nMessage: " + dto.getAdvisoryMessage());
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "Farmer ID", farmerId);
        addRow(form, 1, "Plot ID", plotId);
        addRow(form, 2, "Crop ID", cropId);
        addRow(form, 3, "Season", season);

        VBox left = new VBox(10, form, add, advisory, delete, advisoryArea);
        VBox.setVgrow(advisoryArea, Priority.ALWAYS);

        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Crop Plans", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createMarketPricesTab() {
        TextField cropId = new TextField();
        ComboBox<District> district = new ComboBox<>();
        district.getItems().addAll(District.values());
        district.setValue(District.CHITWAN);
        TextField price = new TextField();
        ListView<String> list = new ListView<>();
        TextArea analysis = new TextArea();
        analysis.setEditable(false);

        Runnable reload = () -> list.getItems().setAll(marketPriceService.all().stream()
                .map(p -> p.getId() + " | Crop=" + p.getCropId() + " | " + p.getDistrict() + " | " + p.getPriceDate()
                        + " | " + p.getPricePerKg() + "/kg")
                .toList());

        Button add = new Button("Add Price");
        add.setDisable(currentUser.getRole() == UserRole.FARMER);
        add.setOnAction(e -> {
            try {
                MarketPrice p = marketPriceService.create(cropId.getText(), district.getValue(), LocalDate.now(),
                        Double.parseDouble(price.getText()));
                auditService.log(currentUser.getUsername(), "CREATE_PRICE", "Added market price: " + p.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        Button history = new Button("Show Crop History");
        history.setOnAction(e -> {
            List<MarketPrice> historyList = marketPriceService.historyByCrop(cropId.getText());
            analysis.setText(historyList.stream()
                    .map(h -> h.getPriceDate() + " | " + h.getDistrict() + " | " + h.getPricePerKg())
                    .collect(Collectors.joining("\n")));
        });

        Button compare = new Button("Compare District Prices");
        compare.setOnAction(e -> analysis.setText(marketPriceService.compareDistricts(cropId.getText())));

        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "Crop ID", cropId);
        addRow(form, 1, "District", district);
        addRow(form, 2, "Price / KG", price);

        VBox left = new VBox(10, form, add, history, compare, analysis);
        VBox.setVgrow(analysis, Priority.ALWAYS);

        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Market Prices", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createWeatherTab() {
        ComboBox<District> district = new ComboBox<>();
        district.getItems().addAll(District.values());
        district.setValue(District.CHITWAN);
        ComboBox<AlertSeverity> severity = new ComboBox<>();
        severity.getItems().addAll(AlertSeverity.values());
        severity.setValue(AlertSeverity.INFO);
        ComboBox<AlertType> type = new ComboBox<>();
        type.getItems().addAll(AlertType.values());
        type.setValue(AlertType.GENERAL);
        TextField message = new TextField();
        TextField days = new TextField("3");

        ListView<String> list = new ListView<>();
        Runnable reload = () -> {
            List<WeatherAlert> alerts = weatherAlertService.all();
            if (currentUser.getRole() == UserRole.FARMER) {
                alerts = alerts.stream().filter(WeatherAlert::isActive).toList();
            }
            list.getItems().setAll(alerts.stream()
                    .map(a -> a.getId() + " | " + a.getDistrict() + " | " + a.getSeverity() + " | " + a.getType()
                            + " | Active=" + a.isActive() + " | " + a.getMessage())
                    .toList());
        };

        Button add = new Button("Create Alert");
        add.setDisable(currentUser.getRole() == UserRole.FARMER);
        add.setOnAction(e -> {
            try {
                LocalDate start = LocalDate.now();
                LocalDate end = start.plusDays(Integer.parseInt(days.getText()));
                WeatherAlert alert = weatherAlertService.create(district.getValue(), severity.getValue(),
                        type.getValue(), message.getText(), start, end);
                auditService.log(currentUser.getUsername(), "CREATE_WEATHER_ALERT",
                        "Created weather alert: " + alert.getId());
                reload.run();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        reload.run();

        GridPane form = basicForm();
        addRow(form, 0, "District", district);
        addRow(form, 1, "Severity", severity);
        addRow(form, 2, "Type", type);
        addRow(form, 3, "Message", message);
        addRow(form, 4, "Active Days", days);

        VBox left = new VBox(10, form, add);
        HBox body = new HBox(10, left, list);
        HBox.setHgrow(list, Priority.ALWAYS);
        body.setPadding(new Insets(10));

        Tab tab = new Tab("Weather Alerts", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createAuditTab() {
        TextField search = new TextField();
        search.setPromptText("Filter by action/user/details");
        ListView<String> list = new ListView<>();

        Runnable reload = () -> list.getItems().setAll(auditService.search(search.getText()).stream()
                .map(a -> a.getTimestamp() + " | " + a.getUsername() + " | " + a.getAction() + " | " + a.getDetails())
                .toList());

        search.textProperty().addListener((obs, oldV, newV) -> reload.run());
        reload.run();

        VBox body = new VBox(10, search, list);
        body.setPadding(new Insets(10));
        VBox.setVgrow(list, Priority.ALWAYS);

        Tab tab = new Tab("Audit Trail", body);
        tab.setClosable(false);
        return tab;
    }

    private Tab createExportTab() {
        TextArea result = new TextArea();
        result.setEditable(false);

        Button exportFarmer = new Button("Export Farmers to exports/farmers.txt");
        exportFarmer.setOnAction(e -> {
            Path file = exportService.exportFarmers(Path.of("exports/farmers.txt"));
            auditService.log(currentUser.getUsername(), "EXPORT_FARMERS", "Exported farmers to: " + file);
            result.appendText("Exported farmers: " + file + "\n");
        });

        Button exportPrices = new Button("Export Market Prices to exports/market_prices.txt");
        exportPrices.setOnAction(e -> {
            Path file = exportService.exportMarketPrices(Path.of("exports/market_prices.txt"));
            auditService.log(currentUser.getUsername(), "EXPORT_PRICES", "Exported market prices to: " + file);
            result.appendText("Exported market prices: " + file + "\n");
        });

        VBox body = new VBox(10, exportFarmer, exportPrices, result);
        body.setPadding(new Insets(10));
        VBox.setVgrow(result, Priority.ALWAYS);

        Tab tab = new Tab("Export", body);
        tab.setClosable(false);
        return tab;
    }

    private GridPane basicForm() {
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        return grid;
    }

    private void addRow(GridPane form, int row, String label, javafx.scene.Node node) {
        form.add(new Label(label + ":"), 0, row);
        form.add(node, 1, row);
    }

    private void deleteSelected(ListView<String> list,
            String entityName,
            Consumer<String> deleteAction,
            String auditAction,
            String detailsPrefix,
            Runnable reload) {
        String selected = list.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Select a " + entityName + " first.");
            return;
        }
        String id = selected.split("\\|")[0].trim();
        deleteAction.accept(id);
        auditService.log(currentUser.getUsername(), auditAction, detailsPrefix + id);
        reload.run();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Action failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
