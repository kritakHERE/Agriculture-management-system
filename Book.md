# Smart AgriNepal Cooperative Management System
## Comprehensive SDLC Book (Detailed SRS + Implementation Mapping)

Version: 1.0  
Date: 2026-03-13  
Project Type: Offline-first desktop application  
Technology Baseline: Java 17, JavaFX 21.0.2, Maven, file-based object serialization

---

## Table of Contents

1. Introduction and Document Scope
2. SDLC Phase 1 - Initiation and Project Planning
3. SDLC Phase 2 - Requirements Engineering
4. SDLC Phase 3 - System Analysis
5. SDLC Phase 4 - System Design
6. SDLC Phase 5 - Implementation and Construction
7. SDLC Phase 6 - Verification and Testing
8. SDLC Phase 7 - Deployment, Transition, and Operations
9. SDLC Phase 8 - Maintenance and Continuous Improvement
10. Future Enhancement Roadmap
11. End-to-End Requirement Traceability Matrix
12. Operational Runbook and Support Model
13. Risk Register and Governance Controls
14. Appendix (Glossary, Data Dictionary, Coding Standards)

---

## 1. Introduction and Document Scope

### 1.1 Purpose of this Book

This document is a single, end-to-end technical and functional reference for the Smart AgriNepal Cooperative Management System. It combines:

- A modest Software Requirements Specification (SRS)
- SDLC phase-by-phase explanation
- Concrete mapping from requirement to implementation
- Architecture and design decisions
- Testing and validation strategy
- Deployment and operational guidance
- Maintenance and future enhancement planning

The goal is to make the project understandable and maintainable by instructors, new developers, QA reviewers, and future contributors.

### 1.2 Product Context

Nepal agricultural cooperatives often operate in environments with:

- Limited or unreliable internet
- Frequent power interruptions
- Manual record systems vulnerable to data loss
- Inconsistent profile registration and weak traceability

The system addresses these constraints by providing a fully offline desktop platform with local file persistence and role-based operations.

### 1.3 Scope Boundary

Included in this version:

- User authentication and role-based access (Admin, Officer, Farmer)
- Farmer profile management
- Farm plot management
- Crop master management
- Crop planning with season advisory
- District-wise market price tracking and comparison
- Weather alert management
- Dashboard and analytics snapshot
- Append-only audit trail
- Text export

Excluded by design (assignment and architecture constraints):

- SQL/NoSQL database
- Cloud storage
- REST APIs/web services
- Real-time internet weather APIs
- Mobile app
- Payment and finance module

### 1.4 Audience

- Project evaluators and lecturers
- Developer team members
- QA/testing members
- Cooperative operators and technical support

### 1.5 Definitions

- DAO: Data Access Object
- DTO: Data Transfer Object
- MVC: Model-View-Controller
- FR: Functional Requirement
- NFR: Non-Functional Requirement
- RBAC: Role-Based Access Control

---

## 2. SDLC Phase 1 - Initiation and Project Planning

### 2.1 Business Problem Statement

Agricultural cooperatives in rural Nepal need a reliable operational system that does not depend on internet availability. Existing manual and spreadsheet workflows create risk in data consistency, accessibility, and accountability.

### 2.2 Vision Statement

Build a desktop-based cooperative management system that:

- Operates offline
- Stores data locally and safely
- Supports major cooperative workflows
- Enforces role-based user responsibilities
- Improves decision support with crop and market advisory features

### 2.3 Project Objectives

1. Deliver a functional Java desktop application for cooperative operations.
2. Use object-oriented design and patterns (DAO, MVC, DTO).
3. Provide complete local persistence without any database dependency.
4. Demonstrate maintainable modular architecture.
5. Support accountability through immutable audit logging.

### 2.4 Success Criteria

- Users can log in and access only permitted modules.
- Core records (farmers, plots, crops, plans, prices, alerts) persist across restarts.
- At least one advisory workflow produces meaningful guidance.
- Audit log captures key actions consistently.
- Build and run process is reproducible via Maven.

### 2.5 Stakeholder Model

Primary stakeholders:

- Admin user (agriculture officer)
- Cooperative officer
- Farmer user
- Academic evaluator

Secondary stakeholders:

- Future maintainer/developer
- Support personnel

### 2.6 Constraints and Assumptions

Constraints:

- No database allowed
- Desktop-first implementation
- Offline operation

Assumptions:

- System runs on a local machine with Java 17+
- Users are trained in basic desktop operations
- Input accuracy is partially controlled by validation and user discipline

### 2.7 Planning Deliverables by Milestones

- Milestone 1: Team organization and module decomposition
- Milestone 2: Technology and OOP justification
- Milestone 3: Analysis model and prototype progression
- Milestone 4: Finalized analysis/design package before implementation

### 2.8 Work Breakdown Structure (High Level)

1. Requirement gathering and interpretation
2. Domain model and package design
3. Core persistence framework
4. Service layer logic
5. UI assembly and role-based flow
6. Testing and validation
7. Documentation and packaging

### 2.9 Risk Identification (Planning Stage)

- Risk: Corrupted serialized data files due to abrupt shutdown
- Risk: Duplicate entries due to user input variance
- Risk: Unauthorized operation if role checks are incomplete
- Risk: Inconsistent traceability if logs are deletable

Mitigations in project:

- Consistent DAO write/read strategy
- Duplicate detection rules in service layer
- Role-based tab rendering and action restrictions
- Audit DAO delete disabled (append-only)

---

## 3. SDLC Phase 2 - Requirements Engineering

### 3.1 Requirement Elicitation Sources

- Assignment brief and module expectations
- Agricultural cooperative domain assumptions
- Rural offline-first operational requirements
- OOP and design pattern implementation requirements

### 3.2 Stakeholder Requirements Summary

Admin requires:

- Full visibility and control
- Ability to create users and view audit
- Control over crop, price, and alert workflows

Officer requires:

- Daily operational data entry
- Farmer and farm management
- Advisory and weather publishing

Farmer requires:

- Access to own operational records
- View market and active alerts
- Simpler feature surface

### 3.3 Functional Requirements (FR)

FR-01: System shall authenticate a user by username and password.  
FR-02: System shall support role values: ADMIN, OFFICER, FARMER.  
FR-03: System shall seed default users when no user data exists.  
FR-04: System shall maintain user accounts with unique usernames.  
FR-05: System shall allow Admin to create users.  
FR-06: System shall maintain farmer records with district mapping.  
FR-07: System shall prevent duplicate farmer registration by name + district.  
FR-08: System shall support farmer update operation.  
FR-09: System shall support farmer delete operation.  
FR-10: System shall support farmer search by keyword and filters.  
FR-11: System shall maintain farm plots linked to farmer ID.  
FR-12: System shall validate positive plot size.  
FR-13: System shall validate plot unit as ROPANI or BIGHA.  
FR-14: System shall support crop catalog creation and update.  
FR-15: System shall maintain crop growth duration and soil suitability.  
FR-16: System shall seed default crop catalog when empty.  
FR-17: System shall create crop plans using farmer/plot/crop/season.  
FR-18: System shall validate referenced farmer existence in crop plan creation.  
FR-19: System shall validate referenced plot existence in crop plan creation.  
FR-20: System shall validate referenced crop existence in crop plan creation.  
FR-21: System shall calculate estimated harvest date based on planting date + growth duration.  
FR-22: System shall generate advisory note for crop-season suitability.  
FR-23: System shall generate advisory DTO with suitability score and recommendations.  
FR-24: System shall maintain district-wise market price records per crop/date.  
FR-25: System shall show market price history by crop.  
FR-26: System shall compare latest district prices for selected crop.  
FR-27: System shall maintain weather alerts with severity and type.  
FR-28: System shall mark weather alerts active by date range.  
FR-29: System shall show active weather alerts for district queries.  
FR-30: System shall aggregate dashboard metrics (counts/distributions/summary).  
FR-31: System shall maintain append-only audit logs for major actions.  
FR-32: System shall not allow deletion of audit records.  
FR-33: System shall support audit log search by keyword.  
FR-34: System shall support export of farmer records to text file.  
FR-35: System shall support export of market price records to text file.  
FR-36: System shall keep persisted records across application restart.  
FR-37: System shall provide farmer-restricted view for own plots/plans.  
FR-38: System shall prevent farmers from creating market prices.  
FR-39: System shall prevent farmers from creating weather alerts.  
FR-40: System shall provide role-based module visibility in UI tabs.

### 3.4 Non-Functional Requirements (NFR)

NFR-01 (Offline Reliability): System shall run without internet connectivity.  
NFR-02 (Local Persistence): System shall use local file-based storage only.  
NFR-03 (Usability): UI shall support straightforward form-based interaction.  
NFR-04 (Performance): Typical CRUD actions should complete within practical desktop latency.  
NFR-05 (Maintainability): Project shall separate concerns using model/dao/service/controller/ui structure.  
NFR-06 (Traceability): Critical actions shall be auditable by username, action, and time.  
NFR-07 (Portability): Application shall build/run on systems with Java 17 and Maven.  
NFR-08 (Integrity): Invalid references and empty required fields shall be validated before persistence.  
NFR-09 (Security Baseline): Role-based access shall restrict unavailable features per user role.  
NFR-10 (Recoverability): Export feature shall provide simple data extraction support.

### 3.5 Business Rules

BR-01: Farmer uniqueness by (name, district).  
BR-02: Plot must belong to an existing farmer ID.  
BR-03: Crop plan references must exist.  
BR-04: Advisory score increases by season and soil compatibility rules.  
BR-05: Market comparison uses latest entry per district.  
BR-06: Weather active status determined by start/end date window.  
BR-07: Audit records are immutable through system features.

### 3.6 Use Case List

UC-01 Login
UC-02 Logout
UC-03 Create user
UC-04 Register farmer
UC-05 Update farmer
UC-06 Delete farmer
UC-07 Register farm plot
UC-08 Delete farm plot
UC-09 Add crop
UC-10 Delete crop
UC-11 Create crop plan
UC-12 Generate crop advisory
UC-13 Add market price
UC-14 View market history
UC-15 Compare district prices
UC-16 Create weather alert
UC-17 View weather alerts
UC-18 View dashboard
UC-19 View/search audit trail
UC-20 Export farmers
UC-21 Export market prices

### 3.7 Acceptance Criteria Summary

- All FRs can be demonstrated through UI operations.
- Role restrictions are observable at tab/action level.
- File outputs are generated for export actions.
- Restarting app preserves records previously created.

---

## 4. SDLC Phase 3 - System Analysis

### 4.1 Analysis Goals

This phase translates requirements into:

- Domain entities and relationships
- Operation flows and actor interactions
- Data lifecycle and state transitions
- Rule validation points

### 4.2 Domain Analysis Model

Core entities:

- User (abstract), AdminUser, CooperativeOfficerUser, FarmerUser
- Farmer
- FarmPlot
- Crop
- CropPlan
- MarketPrice
- WeatherAlert
- AuditLog

Supporting enums:

- UserRole
- District
- Season
- SoilType
- AlertSeverity
- AlertType

DTO entities:

- DashboardDTO
- CropAdvisoryDTO

### 4.3 Key Relationships

- Farmer 1..* FarmPlot
- Farmer 1..* CropPlan
- Crop 1..* CropPlan
- District 1..* Farmer
- District 1..* MarketPrice
- District 1..* WeatherAlert

### 4.4 Process Analysis by Module

#### 4.4.1 Authentication Process

Input: username, password  
Decision: credential match?  
Output: user session object or error

#### 4.4.2 Farmer Registration Process

Input: name, district, crop, status, phone  
Rule check: mandatory name, duplicate check  
Output: persisted farmer ID

#### 4.4.3 Crop Plan Process

Input: farmerID, plotID, cropID, season, plantingDate  
Rule check: foreign references + suitability mapping  
System action: compute harvest date and advisory note  
Output: persisted plan + analytics available

#### 4.4.4 Market Analysis Process

Input: crop selection  
System action: filter history, compute latest by district  
Output: comparative textual result

#### 4.4.5 Alert Process

Input: district, severity, type, message, duration  
System action: date range active state  
Output: visible active alerts

### 4.5 State and Lifecycle Analysis

- Farmer status: ACTIVE/INACTIVE
- CropPlan status: active flag (current implementation stores as active true on creation)
- WeatherAlert status: dynamic active/inactive by date range
- Session state: null user vs authenticated user

### 4.6 Data Flow Perspective

User interaction -> App UI event -> Service method -> DAO -> File write/read -> UI refresh

This linear flow simplifies debugging and traceability in offline desktop context.

### 4.7 Gap and Limitation Analysis

Observed intentional limitations in current release:

- No encrypted password storage
- No transactional rollback across multi-step operations
- No built-in backup scheduler
- No conflict resolution for manual file edits

These are tracked under future enhancements.

---

## 5. SDLC Phase 4 - System Design

### 5.1 Design Principles Applied

- Separation of concerns
- Layered architecture
- Reusable generic persistence base class
- Minimal coupling between UI forms and persistence
- Rule validation centralized in services

### 5.2 Architecture Overview

Presentation Layer:

- JavaFX scene and tab components in App class

Application/Service Layer:

- AuthService
- FarmerService
- FarmPlotService
- CropService
- CropPlanService
- MarketPriceService
- WeatherAlertService
- DashboardService
- AuditService
- ExportService

Persistence Layer:

- GenericDAO interface
- FileBasedDAO abstract implementation
- Entity-specific DAO classes

Domain Layer:

- Model classes and enums

Transfer Layer:

- DTOs for aggregated and advisory responses

### 5.3 Design Pattern Justification

#### DAO Pattern

Why used:

- Abstracts storage mechanism from business logic
- Allows uniform CRUD contract
- Supports assignment requirement for local file persistence

How used:

- GenericDAO defines operations
- FileBasedDAO provides common implementation
- Entity DAOs bind to file paths

#### MVC Pattern (Pragmatic Desktop Variant)

Why used:

- Supports clear role split between UI composition and business logic

How used:

- Model: domain classes
- View+controller orchestration: JavaFX UI in App and thin controller classes
- Service: business logic mediator

#### DTO Pattern

Why used:

- Aggregates rich data for UI presentation without exposing internal DAO logic

How used:

- DashboardDTO for aggregated metrics
- CropAdvisoryDTO for scoring and recommendation output

### 5.4 Package Design

- com.agrinepal.model
- com.agrinepal.dao
- com.agrinepal.service
- com.agrinepal.dto
- com.agrinepal.controller
- com.agrinepal.util

### 5.5 Data Design and File Schema

Data files:

- data/users.dat
- data/farmers.dat
- data/farmplots.dat
- data/crops.dat
- data/cropplans.dat
- data/marketprices.dat
- data/weatheralerts.dat
- data/auditlog.dat

Objects are serialized lists by type. The DAO reads full list, mutates in memory, then writes full list.

### 5.6 Security and Access Design

Role access design:

- ADMIN: all modules
- OFFICER: all operational modules except user creation
- FARMER: restricted modules and restricted action buttons

UI-level enforcement:

- Tabs conditionally created by role
- Certain buttons disabled for farmer role
- Farmer-specific data filtering using mapped farmerId

### 5.7 Error Handling Design

- Service and util methods throw IllegalArgumentException/runtime exceptions on invalid state
- App.showError displays operation-level failure feedback
- Form handlers catch exceptions and keep UI responsive

### 5.8 Advisory Scoring Design

Base score = 50  
+35 if season suitability rule passes  
+15 if plot soil matches crop recommended soil  
Capped at 100

Estimated yield formula:

EstimatedYield = PlotSize * UnitMultiplier * (Score/100) * 100

Where:

- UnitMultiplier = 13.5 for BIGHA
- UnitMultiplier = 1.0 for ROPANI

### 5.9 UX Design Strategy

- Form-first interaction model for low training overhead
- Readable list views for immediate feedback
- Dedicated help labels in each module
- Consistent control naming and operation buttons

### 5.10 Design Decisions and Trade-offs

Decision: single App UI orchestrator file  
Trade-off: fast delivery and clarity for assignment vs limited UI modularity

Decision: object serialization for all entities  
Trade-off: simple local persistence vs schema migration complexity

Decision: append-only audit DAO  
Trade-off: accountability gains vs occasional cleanup challenge

---

## 6. SDLC Phase 5 - Implementation and Construction

### 6.1 Technology Stack Implementation

- Language: Java 17
- UI: JavaFX controls and layouts
- Build: Maven
- Annotation helper: org.jetbrains annotations
- Runtime launcher: SmartAgriNepalLauncher

### 6.2 Source Structure to Requirement Mapping

Entry and UI orchestration:

- App.java
- SmartAgriNepalLauncher.java

Business services:

- AuthService
- FarmerService
- FarmPlotService
- CropService
- CropPlanService
- MarketPriceService
- WeatherAlertService
- DashboardService
- AuditService
- ExportService

Persistence:

- GenericDAO
- FileBasedDAO
- entity DAOs

### 6.3 Requirement Implementation Mapping (Narrative)

#### 6.3.1 FR-01, FR-02, FR-03, FR-04, FR-05 (Authentication and User Management)

Implemented by:

- AuthService.login
- AuthService.createUser
- AuthService.seedDefaultUsers
- App.createUsersTab

Evidence of behavior:

- Credential matching scans persisted users and returns Optional<User>
- User creation validates required fields and username uniqueness
- Default users created only when users storage is empty
- User role determines concrete subclass creation

#### 6.3.2 FR-06 to FR-10 (Farmer Management)

Implemented by:

- FarmerService.create, update, delete, search, all
- App.createFarmersTab

Evidence of behavior:

- Duplicate check based on same name + district
- CRUD actions update list view immediately after operation
- Search supports keyword and optional filter pipeline

#### 6.3.3 FR-11 to FR-13 (Farm Plot Management)

Implemented by:

- FarmPlotService.create with validations
- FarmPlotService.byFarmer
- App.createPlotsTab

Evidence of behavior:

- Plot creation rejects non-positive size
- Unit validated to ROPANI/BIGHA
- Farmer role auto-forces own farmerId in plot creation

#### 6.3.4 FR-14 to FR-16 (Crop Master)

Implemented by:

- CropService.create, update, delete, all, seedDefaultCrops
- App.createCropsTab

Evidence of behavior:

- Crop records include growth duration, water need, soil, seasons
- Seed routine creates baseline crops when storage is empty

#### 6.3.5 FR-17 to FR-23 (Crop Planning and Advisory)

Implemented by:

- CropPlanService.create
- CropPlanService.advisory
- NepaliSeasonUtil.isCropSuitable and recommendedCrops
- App.createCropPlansTab

Evidence of behavior:

- Foreign references validated before creation
- Harvest date computed from planting date + duration
- Advisory score computes suitability and estimated yield
- UI supports both plan creation and advisory generation

#### 6.3.6 FR-24 to FR-26 (Market Price Module)

Implemented by:

- MarketPriceService.create
- MarketPriceService.historyByCrop
- MarketPriceService.compareDistricts
- App.createMarketPricesTab

Evidence of behavior:

- Daily prices stored with district and crop relation
- History sorted by recent date first
- Latest price grouped by district for comparison output

#### 6.3.7 FR-27 to FR-29 (Weather Alerts)

Implemented by:

- WeatherAlertService.create
- WeatherAlertService.activeByDistrict
- WeatherAlert.isActive
- App.createWeatherTab

Evidence of behavior:

- Alert validity uses start/end date range
- Farmer role has view-only access
- Officer receives confirmation dialog before creating alert

#### 6.3.8 FR-30 (Dashboard)

Implemented by:

- DashboardService.getDashboard
- DashboardDTO
- App.createDashboardTab

Evidence of behavior:

- Counts, crop distribution, district distribution, and season summary aggregated
- Dashboard refresh action re-computes current metrics

#### 6.3.9 FR-31 to FR-33 (Audit)

Implemented by:

- AuditService.log
- AuditService.search
- AuditLogDAO.delete override throwing UnsupportedOperationException
- App.createAuditTab

Evidence of behavior:

- Major operations call log() with username/action/details
- Audit search allows text filter by user/action/details
- Delete operation blocked by DAO design

#### 6.3.10 FR-34, FR-35 (Exports)

Implemented by:

- ExportService.exportFarmers
- ExportService.exportMarketPrices
- App.createExportTab

Evidence of behavior:

- Text files created under exports directory
- Export action also logged in audit trail

#### 6.3.11 FR-36 to FR-40 (Persistence and RBAC)

Implemented by:

- FileBasedDAO read/write behavior
- App role-driven tab creation and action disabling
- Farmer-specific filtering logic in plans and plots

Evidence of behavior:

- Records remain available after restart due to serialized file storage
- Farmers cannot access admin-only tab content

### 6.4 Build and Execution Implementation

Build and run workflow:

1. mvn clean javafx:run
2. or run SmartAgriNepalLauncher as IDE main class

Maven config includes:

- maven-compiler-plugin release 17
- javafx-maven-plugin mainClass binding

### 6.5 Coding Conventions and Quality Practices

- Packages grouped by responsibility
- Service-level validation before persistence mutation
- Reusable list refresh lambdas in UI
- Consistent ID generation by prefix

### 6.6 Current Technical Debt (Known)

- Password stored in plaintext
- Single large App class for all tabs
- Full-list rewrite persistence model for every mutation
- No schema version migration layer for serialized objects

---

## 7. SDLC Phase 6 - Verification and Testing

### 7.1 Test Strategy

Testing scope includes:

- Functional behavior tests
- Role-based access tests
- Data persistence tests
- Validation/error handling tests
- Regression checks across core workflows

### 7.2 Test Levels

1. Unit-level (service behavior conceptual checks)
2. Integration-level (UI -> service -> DAO -> file)
3. System-level (end-to-end user scenarios)
4. User acceptance simulation (role workflows)

### 7.3 Functional Test Case Catalog

TC-01 Login with valid admin credentials  
Expected: dashboard shown with admin tabs

TC-02 Login with invalid password  
Expected: error dialog shown

TC-03 Create duplicate username  
Expected: operation blocked with validation message

TC-04 Add farmer with blank name  
Expected: validation failure

TC-05 Add farmer duplicate name+district  
Expected: duplicate rejection

TC-06 Search farmer by partial name  
Expected: filtered list

TC-07 Add plot size = 0  
Expected: validation failure

TC-08 Add plot with invalid unit  
Expected: validation failure

TC-09 Add crop with negative duration  
Expected: validation failure

TC-10 Create crop plan with unknown crop ID  
Expected: crop not found error

TC-11 Generate advisory for suitable season  
Expected: high suitability score and recommended message

TC-12 Generate advisory for mismatch  
Expected: lower score and risk message

TC-13 Add market price as officer  
Expected: record saved and listed

TC-14 Attempt add market price as farmer  
Expected: add action disabled

TC-15 Show history for selected crop  
Expected: reverse chronological listing

TC-16 Compare district prices with no data  
Expected: no data message

TC-17 Create weather alert as officer with confirmation cancel  
Expected: no alert persisted

TC-18 Create weather alert as officer confirm OK  
Expected: alert persisted and listed

TC-19 Weather active status after end date  
Expected: inactive

TC-20 Farmer view weather tab  
Expected: only active alerts visible

TC-21 View audit tab as admin/officer  
Expected: searchable log list

TC-22 Farmer role audit tab visibility  
Expected: audit tab not shown

TC-23 Export farmers  
Expected: exports/farmers.txt created

TC-24 Export market prices  
Expected: exports/market_prices.txt created

TC-25 Restart app after CRUD  
Expected: persisted records still present

### 7.4 Non-Functional Test Focus

- Startup and response behavior under realistic data volume
- File write/read consistency across repeated operations
- Usability test with first-time user task completion

### 7.5 Defect Categories to Track

- Validation defects
- Access control defects
- Persistence defects
- UI state defects
- Reporting/export defects

### 7.6 Example Bug Handling Workflow

1. Reproduce in deterministic steps
2. Identify failing layer (UI/service/DAO)
3. Patch with minimal impact
4. Re-run relevant test cases
5. Update documentation and regression notes

### 7.7 Coverage Notes

The architecture is testable at service boundaries. A future iteration can add JUnit-based automated tests with mock DAOs and temporary file fixtures.

---

## 8. SDLC Phase 7 - Deployment, Transition, and Operations

### 8.1 Deployment Model

Deployment type: local desktop installation/run environment.

Requirements:

- Java 17 runtime
- Maven (for build/run) or packaged build output
- Local write permissions for data and exports directories

### 8.2 Run Configuration

Preferred run:

- mvn clean javafx:run

IDE run:

- Main class: SmartAgriNepalLauncher

### 8.3 Environment Initialization

On first successful startup:

- crop defaults seeded if crop file empty
- admin/officer users seeded if user file empty

### 8.4 Data Directory Behavior

- data directory stores .dat serialized files
- each module writes to dedicated file
- missing files are created on first write

### 8.5 Operational Procedures

Daily startup:

1. Start application
2. Login with role account
3. Execute operational module workflows
4. Export reports if needed
5. Logout

Daily shutdown:

1. Ensure no modal errors pending
2. Logout and close app normally
3. Optional copy of data and exports for backup

### 8.6 Backup and Restore Procedure (Operational)

Backup:

- copy data directory to timestamped backup location
- copy exports directory if reports needed

Restore:

- stop app
- replace data directory with selected backup snapshot
- restart app and verify key records

### 8.7 Support Escalation Model

Level 1: User operation issue (input and usage)  
Level 2: Data or module behavior issue  
Level 3: Build/runtime/environment issue

### 8.8 Release Packaging Recommendations

For academic deployment:

- Include README with run instructions
- Include known default credentials
- Include sample data notes

For production-like extension:

- Add installer and JRE bundle
- Add configuration and log directory controls

---

## 9. SDLC Phase 8 - Maintenance and Continuous Improvement

### 9.1 Maintenance Objectives

- Keep system stable and usable for cooperative staff
- Fix defects with minimal disruption
- Improve reliability without violating offline constraints
- Introduce enhancements incrementally

### 9.2 Maintenance Types

Corrective maintenance:

- Fix bugs and broken workflows

Adaptive maintenance:

- Update to newer JavaFX/JDK versions
- Adjust logic to policy changes

Perfective maintenance:

- Improve UI clarity and speed
- Add stronger validations and filtering

Preventive maintenance:

- Refactor large UI class into modular views
- Add automated tests to reduce regression risk

### 9.3 Maintenance Workflow

1. Capture issue report with reproducible steps
2. Classify severity and impact
3. Analyze root cause
4. Patch implementation
5. Execute regression tests
6. Deploy patch and update documentation

### 9.4 Change Impact Zones

High impact modules:

- App (UI orchestration)
- FileBasedDAO (all persistence)
- AuthService (security and access)

Moderate impact modules:

- CropPlanService (advisory logic)
- DashboardService (aggregations)

### 9.5 Data Compatibility Management

Because object serialization is used, model changes must be controlled. Safe evolution practices:

- Add fields with defaults carefully
- Track serialVersionUID where needed
- Verify old data loading in migration testing

### 9.6 Operational Monitoring Suggestions

Current state has no external monitoring service. Practical local monitoring can include:

- audit log review schedule
- periodic export verification
- weekly backup snapshot checks

### 9.7 Documentation Maintenance Policy

Whenever a feature changes:

- update requirement mapping section
- update test cases impacted
- update deployment/ops notes if behavior changed

---

## 10. Future Enhancement Roadmap

### 10.1 Short-Term Enhancements (1-2 iterations)

1. Password hashing and stronger auth validation
2. Additional field validation (phone format, constraints)
3. Advanced dashboard filtering controls
4. Import functionality from CSV/text
5. Better error categorization and user guidance

### 10.2 Mid-Term Enhancements (3-5 iterations)

1. Modular UI decomposition (separate view classes)
2. Service-level transaction simulation for multi-step workflows
3. Automated test suite with JUnit and test fixtures
4. Report templates for weekly/monthly summaries
5. Backup manager module with one-click restore points

### 10.3 Long-Term Enhancements

1. Optional encrypted local data storage
2. Sync-capable architecture for future connectivity mode
3. GIS/location integration if connectivity constraints relax
4. Multi-language UI support
5. Analytics forecasting models for yield and pricing

### 10.4 Future Database Migration Path (If policy changes)

Current DAO abstraction can support gradual migration:

- keep service contracts unchanged
- swap file-based DAO implementation with DB-backed DAO
- run coexistence mode for data migration period

### 10.5 Future API Readiness Path

If web endpoints are required later:

- expose service methods through REST controller layer
- keep core business logic unchanged
- retain desktop as offline-capable client

---

## 11. End-to-End Requirement Traceability Matrix

| Requirement | Description | Implementation Artifacts | Verification Approach |
|---|---|---|---|
| FR-01 | Authenticate user | AuthService.login, App login action | TC-01, TC-02 |
| FR-02 | Role support | UserRole enum, User subclasses | Role-based UI checks |
| FR-03 | Seed default users | AuthService.seedDefaultUsers | First-run test |
| FR-04 | Unique usernames | AuthService.createUser validation | TC-03 |
| FR-05 | Admin creates users | App users tab + AuthService | Admin scenario test |
| FR-06 | Farmer profile creation | FarmerService.create | TC-04 |
| FR-07 | Duplicate farmer prevention | FarmerService duplicate rule | TC-05 |
| FR-08 | Farmer update | FarmerService.update + App handler | CRUD regression |
| FR-09 | Farmer delete | FarmerService.delete + App helper | CRUD regression |
| FR-10 | Farmer search | FarmerService.search | TC-06 |
| FR-11 | Farm plot linked to farmer | FarmPlot model, FarmPlotService.create | Data linkage checks |
| FR-12 | Positive plot size | FarmPlotService validation | TC-07 |
| FR-13 | Unit validation | FarmPlotService validation | TC-08 |
| FR-14 | Crop CRUD | CropService + crops tab | CRUD tests |
| FR-15 | Crop suitability fields | Crop model fields | Data integrity checks |
| FR-16 | Default crop seed | CropService.seedDefaultCrops | First-run test |
| FR-17 | Create crop plan | CropPlanService.create | TC-10 and success path |
| FR-18 | Validate farmer ref | CropPlanService.create | Invalid farmer test |
| FR-19 | Validate plot ref | CropPlanService.create | Invalid plot test |
| FR-20 | Validate crop ref | CropPlanService.create | TC-10 |
| FR-21 | Compute harvest date | CropPlanService.create | Date output verification |
| FR-22 | Advisory note | CropPlanService.create note | Plan output check |
| FR-23 | Advisory DTO analytics | CropPlanService.advisory | TC-11, TC-12 |
| FR-24 | Market price create | MarketPriceService.create | TC-13 |
| FR-25 | Price history | MarketPriceService.historyByCrop | TC-15 |
| FR-26 | District comparison | MarketPriceService.compareDistricts | TC-16 |
| FR-27 | Alert create | WeatherAlertService.create | TC-18 |
| FR-28 | Alert active window | WeatherAlert.isActive | TC-19 |
| FR-29 | Active alerts by district | WeatherAlertService.activeByDistrict | Dashboard/weather tests |
| FR-30 | Dashboard aggregate | DashboardService.getDashboard | TC-18 equivalent refresh test |
| FR-31 | Audit logging | AuditService.log | Audit validation |
| FR-32 | Non-deletable audit | AuditLogDAO.delete override | Negative delete test |
| FR-33 | Audit search | AuditService.search | TC-21 |
| FR-34 | Export farmers | ExportService.exportFarmers | TC-23 |
| FR-35 | Export prices | ExportService.exportMarketPrices | TC-24 |
| FR-36 | Persist across restart | FileBasedDAO + file storage | TC-25 |
| FR-37 | Farmer self-scope data | App filters for farmer role | Role scenario test |
| FR-38 | Farmer cannot add price | App button disable | TC-14 |
| FR-39 | Farmer cannot create alert | App button disable | Role scenario test |
| FR-40 | Role-based module visibility | App tab composition logic | Role scenario test |

---

## 12. Operational Runbook and Support Model

### 12.1 Start-of-Day Checklist

1. Verify Java runtime availability.
2. Launch application.
3. Confirm successful login.
4. Validate dashboard summary renders.

### 12.2 During-Operation Checklist

1. Use correct role account for tasks.
2. Avoid duplicate manual entries.
3. Review warning dialogs before confirmation.
4. Check audit tab (admin/officer) for suspicious operations.

### 12.3 End-of-Day Checklist

1. Export required files if reporting needed.
2. Logout all users.
3. Copy data directory backup snapshot.

### 12.4 Incident Playbooks

Incident: Cannot login  
Action: verify credentials, check users data file integrity, attempt default account on clean setup.

Incident: Module action fails with validation message  
Action: verify required fields and data references.

Incident: Missing data after crash  
Action: restore latest backup snapshot and verify key records.

Incident: Unexpected role access  
Action: verify account role assignment and recreate user if needed.

---

## 13. Risk Register and Governance Controls

### 13.1 Risk Register

| ID | Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|---|
| R-01 | Plaintext passwords | Medium | High | implement hash+salt in next release |
| R-02 | Serialized schema break | Medium | High | controlled model evolution and migration test |
| R-03 | Human input errors | High | Medium | stronger validation and training |
| R-04 | File corruption on hard shutdown | Low-Med | High | backup policy and restore runbook |
| R-05 | Single-class UI complexity | Medium | Medium | refactor into modular views |
| R-06 | No automated tests | Medium | High | add JUnit pipeline |

### 13.2 Governance Controls

- Code review before merge
- Requirement-to-code traceability update on feature change
- Regression test checklist for each release
- Versioned release notes and migration notes

### 13.3 Compliance to Assignment Constraints

- OOP implementation: satisfied
- DAO pattern with local files: satisfied
- Desktop application: satisfied
- No database, no web API dependency: satisfied

---

## 14. Appendix

### 14.1 Glossary

Admin: highest-privilege cooperative operator account.  
Officer: operational staff account.  
Farmer account: restricted account linked to farmer record.  
Crop plan: planned cultivation record connecting farmer, plot, crop, season, timeline.  
Audit log: immutable operation trace record.

### 14.2 Data Dictionary (Conceptual)

User:

- id
- username
- password
- fullName
- role

Farmer:

- id
- name
- district
- primaryCrop
- active
- phone
- registeredDate

FarmPlot:

- id
- farmerId
- locationNote
- size
- unit
- soilType
- irrigated

Crop:

- id
- name
- growthDurationDays
- waterNeed
- recommendedSoil
- suitableSeasons

CropPlan:

- id
- farmerId
- plotId
- cropId
- season
- plantingDate
- estimatedHarvestDate
- active
- advisoryNote

MarketPrice:

- id
- cropId
- district
- priceDate
- pricePerKg

WeatherAlert:

- id
- district
- severity
- type
- message
- startDate
- endDate

AuditLog:

- id
- timestamp
- username
- action
- details

### 14.3 Coding Standards Snapshot

- Use meaningful package boundaries.
- Validate user input before persistence mutation.
- Keep business logic in service classes, not in DAO.
- Log sensitive operations to audit trail.
- Keep utility methods stateless and reusable.

### 14.4 Maintenance Change Log Template

Use this template for each maintenance release:

Release ID:  
Date:  
Change Type (corrective/adaptive/perfective/preventive):  
Modules affected:  
Requirements impacted:  
Backward compatibility notes:  
Test cases executed:  
Deployment notes:  

### 14.5 Suggested Next Documentation Artifacts

1. UML package, class, and sequence diagrams synchronized with current code.
2. Formal test report with evidence screenshots per test case.
3. User manual with step-by-step operating scenarios.
4. Release engineering notes for packaging and distribution.

---

## Conclusion

This SDLC Book provides a full lifecycle view of the Smart AgriNepal system, from requirement definition through maintenance planning, and it maps each key requirement to concrete implementation structures. The architecture fulfills offline-first cooperative constraints, demonstrates object-oriented and pattern-based design, and offers a practical foundation for continuous improvement.

For academic and practical evolution, the immediate priorities are security hardening, automated test coverage, modular UI refactoring, and controlled model evolution for long-term data compatibility.

---

## 15. Detailed Use Case Specifications

This section expands each major use case into operational detail to support implementation review, QA preparation, and user training.

### 15.1 UC-01 Login

Primary actor: Admin, Officer, Farmer  
Goal: Access authorized system functions.

Preconditions:

- User account exists in local users storage.
- Application has started and login screen is visible.

Trigger:

- User enters username and password and clicks Login.

Main flow:

1. System collects username and password inputs.
2. System calls authentication service.
3. Service scans existing users and compares credentials.
4. If matched, service returns user object.
5. UI stores authenticated user as session.
6. Audit log records login event.
7. Dashboard scene is displayed with role-specific tabs.

Alternative flow A1 (invalid credential):

1. No matching record is found.
2. UI shows error alert with invalid credential message.
3. Login screen remains active.

Postconditions:

- Successful: authenticated session exists.
- Failed: no session created.

### 15.2 UC-02 Logout

Primary actor: Any authenticated user  
Goal: End current session safely.

Main flow:

1. User clicks Logout from dashboard header.
2. System logs logout action with user identity.
3. Current session user is cleared.
4. System navigates back to login scene.

Postconditions:

- Session is null.
- Next operation requires authentication.

### 15.3 UC-03 Create User

Primary actor: Admin  
Goal: Create system accounts for officer or farmer users.

Preconditions:

- Admin is logged in.
- Users tab is visible.

Main flow:

1. Admin enters username, full name, password, role.
2. For farmer role, admin enters linked farmer ID.
3. Admin clicks Create User.
4. Service validates required fields and username uniqueness.
5. Service generates user ID and concrete role subtype object.
6. User is persisted to users storage.
7. Audit log records create user action.
8. Users list reloads with newly created account.

Alternative flow A1 (duplicate username):

1. Validation detects existing username.
2. System shows validation failure.
3. No user is persisted.

### 15.4 UC-04 Register Farmer

Primary actor: Admin or Officer  
Goal: Create a unique farmer profile.

Main flow:

1. Actor fills farmer profile fields.
2. System validates farmer name is not blank.
3. System checks duplicate by name and district.
4. ID is generated and farmer persisted.
5. Audit event is appended.
6. Farmer list refreshes.

Alternative flow A1 (duplicate farmer):

1. Duplicate rule triggered.
2. System shows duplicate error.
3. Action aborted.

### 15.5 UC-05 Update Farmer

Primary actor: Admin or Officer  
Goal: Correct or revise farmer details.

Main flow:

1. Actor selects farmer entry from list.
2. Actor updates fields in form.
3. Actor clicks update.
4. Service persists updated farmer by matching ID.
5. Audit event appended.
6. List refresh shows revised values.

### 15.6 UC-06 Delete Farmer

Primary actor: Admin or Officer  
Goal: Remove farmer record when required.

Main flow:

1. Actor selects farmer item.
2. System extracts selected ID.
3. Service deletes by ID.
4. Audit event appended.
5. List refreshes.

Note:

- Current implementation does not enforce dependent-entity cascade warnings. This should be considered in future enhancement.

### 15.7 UC-07 Register Farm Plot

Primary actor: Admin, Officer, Farmer (restricted self scope)

Main flow:

1. Actor selects farmer reference (or auto-bound farmer for farmer role).
2. Actor enters plot location, size, unit, soil type, irrigation status.
3. System validates size > 0 and accepted unit.
4. System persists plot with generated ID.
5. Audit event appended.
6. Plot list refreshes.

Alternative flow A1 (invalid size):

1. Size <= 0.
2. Validation error shown.

### 15.8 UC-08 Delete Farm Plot

Primary actor: Admin, Officer, Farmer (own scope)

Main flow:

1. Actor selects plot.
2. System deletes by selected ID.
3. Audit event appended.
4. List refreshes.

### 15.9 UC-09 Add Crop

Primary actor: Admin or Officer

Main flow:

1. Actor enters crop master details.
2. Actor selects one to three seasons.
3. System validates name and duration.
4. Crop is created and persisted.
5. Audit entry added.

### 15.10 UC-10 Delete Crop

Primary actor: Admin or Officer

Main flow:

1. Actor selects crop list row.
2. System deletes by extracted crop ID.
3. Audit event logged.

### 15.11 UC-11 Create Crop Plan

Primary actor: Admin, Officer, Farmer (own scope)

Main flow:

1. Actor selects farmer.
2. System filters available plots by selected farmer.
3. Actor selects plot and crop.
4. Actor selects season and submits.
5. Service verifies farmer, plot, and crop references.
6. Service computes harvest date from planting date and growth duration.
7. Service evaluates season suitability and writes advisory note.
8. Plan persisted and audit logged.

### 15.12 UC-12 Generate Crop Advisory

Primary actor: Admin, Officer, Farmer

Main flow:

1. Actor selects season, crop, and plot.
2. System computes suitability score.
3. System computes estimated yield.
4. System returns recommended crops for selected season.
5. UI prints advisory report text.

### 15.13 UC-13 Add Market Price

Primary actor: Admin or Officer  
Restricted actor: Farmer cannot add.

Main flow:

1. Actor selects crop and district.
2. Actor enters price per kg.
3. System persists dated price record.
4. Audit event appended.

### 15.14 UC-14 View Market History

Primary actor: All roles

Main flow:

1. Actor selects crop.
2. System fetches all crop prices.
3. System sorts by descending date.
4. UI renders chronological history lines.

### 15.15 UC-15 Compare District Prices

Primary actor: All roles

Main flow:

1. Actor selects crop.
2. System groups prices by district.
3. System picks latest record in each district.
4. UI shows district to latest price mapping.

### 15.16 UC-16 Create Weather Alert

Primary actor: Admin or Officer  
Restricted actor: Farmer cannot create.

Main flow:

1. Actor sets district, severity, type, message, active duration.
2. If actor is officer, confirmation dialog is shown.
3. On confirmation, system creates date range and persists alert.
4. Audit event appended.

### 15.17 UC-17 View Weather Alerts

Primary actor: All roles

Main flow:

1. Actor opens weather tab.
2. System loads all alerts for admin/officer.
3. For farmer role, system filters only active alerts.

### 15.18 UC-18 View Dashboard

Primary actor: All roles

Main flow:

1. Actor opens dashboard tab.
2. System computes totals and distributions.
3. System generates season summary recommendation.
4. System displays active alerts for configured district sample.

### 15.19 UC-19 View/Search Audit Trail

Primary actor: Admin or Officer

Main flow:

1. Actor enters filter keyword.
2. System applies contains search on user/action/details.
3. Filtered entries shown in list.

### 15.20 UC-20 Export Farmers

Primary actor: All roles (subject to operational policy)

Main flow:

1. Actor clicks export farmers.
2. System prepares CSV-like header and rows.
3. System writes exports/farmers.txt.
4. Audit event appended and path shown.

### 15.21 UC-21 Export Market Prices

Primary actor: All roles (subject to operational policy)

Main flow:

1. Actor clicks export market prices.
2. System resolves crop names for crop IDs.
3. System writes exports/market_prices.txt.
4. Audit event appended and path shown.

---

## 16. Deep Module Engineering Notes

### 16.1 Authentication Module Engineering

Responsibilities:

- Identity verification
- User creation and role subtype construction
- First-run baseline account seeding

Input validation logic:

- Username required
- Password required
- Username uniqueness enforced in persisted dataset

Implementation strengths:

- Simple deterministic flow suitable for offline desktop
- Uses polymorphism for role type object creation

Improvement opportunities:

- Store password hash rather than plaintext
- Add lockout or retry throttling

### 16.2 Farmer Module Engineering

Responsibilities:

- Farmer lifecycle management
- Search and filter pipeline support

Validation strategy:

- Hard requirement on non-empty name
- Business uniqueness rule to reduce duplicates

Data quality impact:

- Duplicate check significantly improves profile integrity
- Search helps data retrieval in larger cooperative datasets

### 16.3 Farm Plot Module Engineering

Responsibilities:

- Represent physical farm units linked to farmers

Key validations:

- Positive size
- Unit domain enforcement

Access control behavior:

- Farmer role is auto-scoped to linked farmer ID

### 16.4 Crop Module Engineering

Responsibilities:

- Maintain stable crop master reference set
- Capture agronomic attributes for advisory logic

Default seeding behavior:

- Ensures system usability on clean setup
- Avoids first-run empty catalog blocker

### 16.5 Crop Plan Module Engineering

Responsibilities:

- Construct farmer cultivation plans
- Enforce reference validity
- Deliver advisory insights

Computational logic:

- Harvest date based on deterministic duration addition
- Suitability from season mapping and soil compatibility
- Yield estimation from size, unit multiplier, and score

Engineering caution:

- Domain formula is simplified; future agronomy refinement recommended

### 16.6 Market Price Module Engineering

Responsibilities:

- Store daily district prices
- Provide historical and comparative views

Algorithm choices:

- History uses descending date sort
- Comparison groups by district and selects max date

Benefits:

- Supports farmer decision-making on where/when to sell produce

### 16.7 Weather Alert Module Engineering

Responsibilities:

- Capture and distribute weather risk alerts
- Filter active alerts by district/date

Operational safeguards:

- Officer confirmation before alert publication
- Farmer receives view-only alert access

### 16.8 Dashboard Module Engineering

Responsibilities:

- Aggregate cross-module operational picture
- Present easy-to-read status snapshot

Computed data elements:

- total farmers
- total plots
- active plans
- crop frequency
- district distribution
- season recommendation summary

### 16.9 Audit Module Engineering

Responsibilities:

- Immutable chronological event trail
- Operational accountability and transparency

Key policy implementation:

- Delete operation blocked at DAO level

Benefits:

- Ensures action history remains available for review

### 16.10 Export Module Engineering

Responsibilities:

- Produce portable text outputs for sharing/reporting

Output model:

- Header line + joined row lines
- Target directory auto-created as needed

Robustness:

- Wraps IO exceptions with meaningful runtime message

---

## 17. Extended Testing Blueprint

### 17.1 Role-Based Scenario Matrix

Admin scenarios:

1. create all entity types
2. view audit and dashboard
3. export all reports

Officer scenarios:

1. manage farmers/plots/plans/prices/alerts
2. cannot create users
3. can view audit

Farmer scenarios:

1. view dashboard and market data
2. manage only self-scope plots and plans
3. cannot add market prices
4. cannot publish alerts
5. no audit tab access

### 17.2 Data Integrity Regression Set

RG-01: Add 100 farmers with varied districts and verify persisted count.  
RG-02: Add mixed plots and verify byFarmer filter correctness.  
RG-03: Create plans for multiple crops and seasons and validate advisory note consistency.  
RG-04: Enter multi-date prices and verify latest-by-district output stability.  
RG-05: Enter short and long duration weather alerts and verify active transitions by date.

### 17.3 Negative Testing Set

NG-01: Null/blank critical fields.  
NG-02: Invalid IDs passed through UI selection edge cases.  
NG-03: Numeric parse failures for size/price/days.  
NG-04: Repeated delete attempts on already removed entities.  
NG-05: Corrupt data file simulation and fallback strategy verification.

### 17.4 Usability Validation Script

Task set for non-technical user:

1. Login as officer.
2. Register a farmer.
3. Register a plot for that farmer.
4. Add market price.
5. Create crop plan.
6. Generate advisory.
7. Export farmers.

Metrics:

- completion time
- number of operation errors
- user confidence score

### 17.5 Suggested Automated Test Introduction Plan

Phase A:

- unit tests for services with in-memory stubs

Phase B:

- integration tests against temporary file paths

Phase C:

- smoke test for startup and key workflows

---

## 18. Maintenance Backlog and Refactor Strategy

### 18.1 Priority P1 (Security and Data Safety)

1. Replace plaintext password with PBKDF2/BCrypt hashing.
2. Add file integrity checksum metadata.
3. Add secure credential reset flow for admin.

### 18.2 Priority P2 (Architecture Quality)

1. Split App tabs into dedicated view/controller classes.
2. Introduce application service facade for UI decoupling.
3. Add repository abstraction layer for easier testing.

### 18.3 Priority P3 (Operations and Analytics)

1. Add backup/restore tab with snapshot management.
2. Add trend analysis charting for price history.
3. Add crop plan completion and yield realization tracking.

### 18.4 Serialization Evolution Strategy

Recommended steps for model evolution:

1. Introduce explicit serialVersionUID for persisted classes.
2. Add compatibility tests with old data samples.
3. Provide migration utility if structural changes are breaking.

### 18.5 Documentation Evolution Strategy

For each release:

1. update requirement matrix
2. update test matrix
3. update known limitations
4. update runbook and deployment notes

---

## 19. Expanded Governance Model

### 19.1 Release Governance

Release gates:

1. Functional regression pass
2. Role access review pass
3. Export and backup sanity pass
4. Documentation completeness pass

### 19.2 Quality Metrics (Recommended)

- Defect escape rate
- Mean time to resolve defects
- User-reported error frequency per module
- Data recovery success rate from backup

### 19.3 Team Workflow Recommendations

1. Maintain module ownership but enforce cross-review.
2. Use short release cycles for controlled enhancement.
3. Keep architecture decisions documented with rationale.

---

## 20. Final SDLC Summary by Phase

Phase 1 (Initiation): Problem, scope, objectives, and success criteria were established for offline cooperative operations.  
Phase 2 (Requirements): Functional and non-functional requirements were specified with business rules and acceptance criteria.  
Phase 3 (Analysis): Domain entities, process flows, and relationship models were defined to bridge business to technical design.  
Phase 4 (Design): Layered architecture, DAO/MVC/DTO patterns, access control strategy, and persistence model were designed.  
Phase 5 (Implementation): JavaFX application with modular services and file-based DAOs was developed and integrated.  
Phase 6 (Testing): Test strategy and scenario catalog validate behavior, reliability, and role restrictions.  
Phase 7 (Deployment): Local run/deployment and operational procedures support practical usage in constrained environments.  
Phase 8 (Maintenance): Corrective, adaptive, perfective, and preventive roadmap ensures long-term viability and growth.

This completes an SDLC-aligned, implementation-backed, maintainable project documentation baseline for Smart AgriNepal.

---

## 21. Detailed Functional Requirement Specifications (Extended)

This section provides per-requirement detail in a formal mini-spec format to support auditability and implementation reviews.

### FR-01 Authentication

Statement: System shall authenticate users using username/password pair.

Rationale:

- Establish controlled access.

Inputs:

- username string
- password string

Processing:

- Case-insensitive username match
- Exact password match

Outputs:

- Optional user object or authentication failure

Failure conditions:

- unknown username
- wrong password

### FR-02 Role Modeling

Statement: System shall model three user roles.

Role values:

- ADMIN
- OFFICER
- FARMER

Rationale:

- Enforce permission boundaries and reduce accidental misuse.

### FR-03 First-run User Seeding

Statement: System shall create baseline admin and officer accounts when no user records exist.

Operational value:

- Guarantees bootstrap usability without manual file setup.

### FR-04 Username Uniqueness

Statement: System shall reject duplicate usernames.

Validation rule:

- No existing username equalsIgnoreCase(newUsername)

### FR-05 User Creation by Admin

Statement: Admin shall create user accounts from UI.

Expected behavior:

- Create role-specific subtype object
- Persist and reflect in user list

### FR-06 Farmer Record Creation

Statement: System shall store farmer identity and profile details.

Key fields:

- id, name, district, primaryCrop, active, phone, registeredDate

### FR-07 Duplicate Farmer Prevention

Statement: System shall prevent duplicate farmer entries using name+district criterion.

Rationale:

- Minimize duplicate registration caused by naming variation errors.

### FR-08 Farmer Update

Statement: System shall allow editing existing farmer records.

Outcome:

- Existing record replaced by ID in storage.

### FR-09 Farmer Delete

Statement: System shall support farmer deletion by selected ID.

Note:

- Dependent data handling is currently basic and can be strengthened later.

### FR-10 Farmer Search

Statement: System shall allow keyword-based farmer search.

Search target fields:

- farmer name
- farmer ID
- optional crop text and district filtering

### FR-11 Farm Plot Management

Statement: System shall maintain farm plots linked to farmer IDs.

Core fields:

- plot id
- farmer id
- location note
- size and unit
- soil type
- irrigation flag

### FR-12 Plot Size Validation

Statement: Plot size must be strictly greater than zero.

### FR-13 Plot Unit Validation

Statement: Plot unit must be either ROPANI or BIGHA.

### FR-14 Crop Catalog Management

Statement: System shall maintain crop reference records.

### FR-15 Crop Agronomy Attributes

Statement: Crop shall include growth days, water need, recommended soil, suitable seasons.

### FR-16 Crop Seeding

Statement: System shall seed default crops on first run if catalog empty.

### FR-17 Crop Plan Creation

Statement: System shall create a crop plan linking farmer, plot, crop, season.

### FR-18 Farmer Reference Validation in Plan

Statement: Crop plan creation shall fail if farmer ID does not exist.

### FR-19 Plot Reference Validation in Plan

Statement: Crop plan creation shall fail if plot ID does not exist.

### FR-20 Crop Reference Validation in Plan

Statement: Crop plan creation shall fail if crop ID does not exist.

### FR-21 Harvest Date Computation

Statement: System shall compute estimated harvest date as plantingDate + growthDurationDays.

### FR-22 Advisory Note in Plan

Statement: System shall set note indicating suitability or mismatch.

### FR-23 Advanced Advisory DTO

Statement: System shall return suitability score, recommended crops, and yield estimate.

### FR-24 Market Price Capture

Statement: System shall store market price entries by crop, district, date, and price per kg.

### FR-25 Price History Query

Statement: System shall list historical price entries for a crop in reverse date order.

### FR-26 District Price Comparison

Statement: System shall compare latest prices per district for selected crop.

### FR-27 Weather Alert Creation

Statement: System shall create weather alert with district, severity, type, message, and date window.

### FR-28 Alert Activity Rule

Statement: Alert active status shall be true when current date is within [startDate, endDate].

### FR-29 District Alert Query

Statement: System shall provide active alerts by district.

### FR-30 Dashboard Aggregation

Statement: System shall produce aggregated operational metrics and summaries.

### FR-31 Audit Write

Statement: System shall write audit entry for major actions.

Audit fields:

- id
- timestamp
- username
- action
- details

### FR-32 Audit Immutability

Statement: System shall not allow audit deletion.

### FR-33 Audit Search

Statement: System shall filter audit logs by keyword across action, details, username.

### FR-34 Farmer Export

Statement: System shall export farmer dataset to text file.

### FR-35 Market Price Export

Statement: System shall export market price dataset to text file.

### FR-36 Persistence Across Restart

Statement: Created records shall remain available after app restart.

### FR-37 Farmer Data Scope

Statement: Farmer role shall see and act only on linked farmer records for plans and plots.

### FR-38 Farmer Price Restriction

Statement: Farmer role shall not create market price records.

### FR-39 Farmer Alert Restriction

Statement: Farmer role shall not create weather alerts.

### FR-40 Role-based Visibility

Statement: Tabs and actions shall be displayed according to role permissions.

---

## 22. Extended Non-Functional Specification

### 22.1 Availability and Reliability

The application prioritizes practical reliability in offline environments rather than distributed high-availability design. Reliability objectives include:

- deterministic startup
- stable CRUD execution in local process
- understandable error messaging
- recoverability via manual backup

### 22.2 Performance Considerations

Given file-based storage and moderate data volume, expected performance is suitable for assignment scale. Potential bottlenecks:

- full-list serialization writes per mutation
- repeated scans for search and grouping

Optimization path (future):

- in-memory cache with periodic flush
- index maps for large datasets

### 22.3 Scalability Boundaries

Current implementation scales for small-to-medium cooperative records. Very large data growth may require:

- segmented files
- database migration
- archival mechanisms

### 22.4 Security Profile

Current state:

- role-based UI restrictions
- basic credential checks

Gaps:

- plaintext password storage
- no at-rest encryption

Security hardening roadmap:

1. hash passwords
2. add password policy enforcement
3. add optional encrypted file storage

### 22.5 Maintainability Attributes

Strengths:

- clear package layering
- service encapsulation of business logic
- utility centralization

Weaknesses:

- monolithic App UI file

Mitigation:

- split module tabs into individual view classes

### 22.6 Testability Attributes

Strengths:

- service methods are mostly deterministic
- validation logic explicit

Improvement:

- add automated tests and file fixture framework

### 22.7 Usability Attributes

Strengths:

- consistent form/list pattern
- clear action buttons
- section help messages in tabs

Improvements:

- inline field-level validation messages
- keyboard shortcuts and accessibility labels

### 22.8 Portability

Runs where Java 17 and JavaFX dependencies are available. Maven plugin simplifies runtime bootstrapping.

### 22.9 Compliance and Constraints

Design stays within assignment boundaries:

- no external DB
- no network API dependencies
- desktop UI and method-call business execution

---

## 23. Extended Data Architecture and Persistence Analysis

### 23.1 Persistence Pattern

FileBasedDAO follows a list-oriented persistence strategy:

1. Read full list
2. Apply mutation in memory
3. Write full list

Advantages:

- straightforward logic
- low implementation complexity

Trade-offs:

- write amplification for single record changes
- potential contention if future multi-user process writes are introduced

### 23.2 Entity Identity Strategy

IDs are generated with prefix-based ID generator. Examples:

- USR-*
- FAR-*
- PLT-*
- CRP-*
- PLN-*
- MKT-*
- WTH-*
- AUD-*

Benefits:

- human-readable category identification
- easier debugging and support communication

### 23.3 Serialization Considerations

Object serialization is sufficient for assignment constraints. To improve long-term resilience:

1. stabilize serialized model classes
2. add version compatibility tests
3. guard against incompatible class changes

### 23.4 Data Integrity Controls

Integrity is mainly protected through service-layer validation:

- required fields
- reference existence
- business uniqueness
- bounded values

### 23.5 Data Recovery Model

Recovery currently depends on operational backup discipline. Recommended future work:

- built-in snapshot manager
- restore preview and validation

---

## 24. Extended User and Role Operation Manual

### 24.1 Admin Operating Guide

Typical admin day flow:

1. Login with admin credentials.
2. Verify dashboard snapshot for system health.
3. Create officer/farmer-linked users as needed.
4. Review farmers and crops for consistency.
5. Review audit logs for suspicious or incorrect actions.
6. Export reports on demand.
7. Logout and archive backups.

Admin cautions:

- Ensure farmer-linked user includes valid farmer ID.
- Avoid deleting records without operational confirmation.

### 24.2 Officer Operating Guide

Typical officer flow:

1. Login as officer.
2. Register and update farmers.
3. Register farm plots.
4. Create crop plans and review advisory messages.
5. Enter market prices daily.
6. Publish weather alerts with confirmation.
7. Monitor dashboard and audit.

Officer cautions:

- Ensure selected farmer and plot alignment before plan creation.
- Confirm weather alerts carefully before publishing.

### 24.3 Farmer Operating Guide

Typical farmer flow:

1. Login as farmer account.
2. Review dashboard and active alerts.
3. Check district market prices and history.
4. Manage own plots and plans only.
5. Generate advisory for selected crop/season.
6. Export data if required.

Farmer constraints:

- Cannot create market prices.
- Cannot create weather alerts.
- No access to audit trail.

### 24.4 Data Entry Standards

Recommended entry standards:

- Use consistent spelling for names and crop references.
- Use numeric values only in size/price/days fields.
- Avoid trailing spaces in identifiers.

### 24.5 Error Message Handling Guide

When error dialog appears:

1. Read full message.
2. Check required fields.
3. Re-check selected IDs in combo boxes.
4. Retry after correction.

Escalate to support if issue repeats with same valid inputs.

---

## 25. Extended Quality Assurance Artefacts

### 25.1 Requirement Coverage Checklist

Checklist categories:

- authentication and session
- CRUD integrity
- role restrictions
- advisory logic correctness
- export and audit completeness

### 25.2 Regression Checklist (Release Gate)

Before release, verify:

1. Login and logout for all roles
2. Create, update, delete farmer
3. Add plot and create crop plan
4. Add market price and compare districts
5. Create weather alert and verify active visibility
6. Export files generated successfully
7. Audit logs reflect performed actions
8. App restart preserves new records

### 25.3 Sample Defect Report Template

Defect ID:  
Module:  
Severity:  
Preconditions:  
Steps to reproduce:  
Expected result:  
Actual result:  
Evidence:  
Proposed fix notes:  

### 25.4 Release Readiness Template

Build version:  
Date:  
Changed modules:  
Risk assessment:  
Executed test cases:  
Known limitations:  
Rollback plan:  

---

## 26. Long-Form Future Engineering Blueprint

### 26.1 Security Upgrade Blueprint

Step 1: Password hashing implementation

- Introduce secure hashing library
- Migrate existing users through controlled reset process

Step 2: Credential policy

- minimum length
- complexity guidance
- optional expiry policy

Step 3: Storage hardening

- optional encrypted data files
- secure key management for local deployment

### 26.2 UI Refactor Blueprint

Goal:

- Split large App class into modular view controllers.

Plan:

1. extract each tab into separate class
2. define shared context and service provider
3. keep event handling localized
4. centralize common UI utilities

### 26.3 Testing Automation Blueprint

Goal:

- Build confidence and reduce regression defects.

Plan:

1. service-level unit tests
2. DAO integration tests using temp directories
3. smoke tests for role workflows
4. CI integration with Maven test lifecycle

### 26.4 Data Migration Blueprint

Goal:

- Prepare for safe evolution of serialized model schema.

Plan:

1. define model version metadata
2. create migration utility for old snapshots
3. run compatibility test matrix

### 26.5 Analytics Evolution Blueprint

Goal:

- Move from descriptive dashboard to predictive advisories.

Plan:

1. seasonal trend accumulation
2. moving-average price trend analysis
3. crop yield recommendation refinement
4. risk scoring from weather and market history

---

## 27. Comprehensive SDLC Closure Notes

The project currently demonstrates a complete educational and practical SDLC loop:

1. Planning aligned to domain problem and constraints
2. Requirements converted into explicit module-level functionality
3. Analysis translated into coherent domain model
4. Design implemented with OOP and recognized patterns
5. Implementation delivered as runnable desktop software
6. Validation structured with broad test coverage guidance
7. Deployment and operation instructions documented
8. Maintenance and enhancement roadmaps clearly defined

This documentation baseline is intentionally implementation-aware, so future contributors can trace what exists, why it exists, and how to extend it safely.
