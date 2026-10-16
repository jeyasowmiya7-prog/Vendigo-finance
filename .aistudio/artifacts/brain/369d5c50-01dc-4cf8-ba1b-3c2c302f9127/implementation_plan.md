# Vendigo Finance – Daily Income & Expense Tracker for Street Vendors

Vendigo Finance is a fast, reliable, offline-first daily income and expense tracking app tailored specifically for Indian street vendors (chai stalls, tender coconut sellers, fruit/vegetable carts, and small roadside eateries). It cleanly separates physical cash from digital UPI payments, automatically calculates real daily profit, and reconciles closing cash balances without complex accounting jargon.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> The following decisions were confirmed through Phase 1 user interview questions:

- **Confirmed Runtime & Architecture**: Built as a native Android application using **Kotlin**, **Jetpack Compose (Material 3)**, and an offline-first **Room SQLite Database** for zero-latency, local reliability, and streaming emulator preview.
- **Confirmed Language Support**: Built-in dual language support with an immediate in-app toggle between **English** and **Tamil (தமிழ்)**, ensuring effortless accessibility for regional vendors.
- **Confirmed Initial Data State**: Starts with a clean, pristine database for real use, while providing a 1-tap **"Load Sample Street Vendor Data"** option in Settings so the app can also be tested instantly with realistic tea stall & snack cart numbers.

---

## 1. Overview & Core Concept

- **What It Does**: Empowers micro-merchants and street vendors to answer three fundamental questions at the end of every business day:
  1. *How much did I sell today?* (Cash Sales + UPI Sales)
  2. *How much did I spend on business supplies?* (Stock, ingredients, ice, packaging, stall rent, fuel/electricity)
  3. *What is my actual net profit today?* (Total Sales − Total Expenses)
- **Target Audience**: Street food carts, fruit and tender coconut vendors, tea stalls, and small kirana/kiosk owners who handle dozens of quick daily micro-transactions in cash and UPI.
- **Key Value**: Eliminates lost cash and confusion between bank UPI balances and physical pocket money by tracking physical cash drawers separately from digital UPI receipts.

---

## 2. User Experience & Visual Design

### Key User Flows

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Morning Routine: Open Cash Drawer                        │
│    Vendor opens app -> Inputs Opening Cash (e.g. ₹500)      │
├─────────────────────────────────────────────────────────────┤
│ 2. During the Day: Rapid 2-Tap Transaction Logging          │
│    Tap "Add Cash Sale" / "Add UPI Sale" / "Add Expense"     │
│    Enter amount on large numpad -> Select Category -> Save  │
├─────────────────────────────────────────────────────────────┤
│ 3. Evening Wrap-Up: Cash Count & Reconciliation             │
│    App computes: Closing Cash = Opening + Cash In - Cash Out│
│    Vendor enters counted physical cash -> Checks discrepancy│
├─────────────────────────────────────────────────────────────┤
│ 4. Business Review & Reports                                │
│    View today's profit card (green if profit, red if loss), │
│    weekly trends, category breakdown, and export summary    │
└─────────────────────────────────────────────────────────────┘
```

### Visual Identity & Theme
- **Theme & Atmosphere**: Professional merchant emerald green (`#1B5E20`, `#2E7D32`) paired with crisp warm cream backgrounds (`#F8FAF6`, `#FFFFFF`) and dark forest accents.
- **Outdoor Sunlight Readability**: High-contrast bold typography, extra-large numeric figures for currency values (₹), minimum touch target of 48–56dp, and clear icon badges.
- **Color Coding**:
  - **Income / Sales**: Vibrant Forest Green (`#2E7D32`)
  - **Expenses**: Brick Terracotta / Crimson (`#C62828`)
  - **Cash Drawer**: Warm Amber / Gold Badge (`#F57F17` / `#FFF8E1`)
  - **UPI Payments**: Deep Royal Blue Badge (`#1565C0` / `#E3F2FD`)
- **Navigation**: Standard Material 3 Bottom Navigation Bar with 4 clear tabs:
  1. **Home**: Dashboard with daily sales, profit, cash & UPI balance cards, quick-action buttons, and recent transactions.
  2. **Transactions**: Complete filterable history (Today, Yesterday, This Week, Custom; Cash vs UPI; Sales vs Expenses) with search, edit, and delete.
  3. **Reports**: Daily, weekly, and monthly summaries, visual expense category breakdown bars, profit comparison, and report sharing.
  4. **Settings**: Business & vendor name, English/Tamil toggle, Opening cash manager, Load Demo Data, and Data Export.

---

## 3. Key Product Decisions & Trade-Offs

- **Decision 1: Integer Paise Storage (`Long`)**:
  - *Chosen Approach*: All currency amounts are stored in database records as integer paise (e.g., ₹150.50 is stored as `15050` paise) and formatted for display.
  - *Why*: Eliminates floating-point calculation errors (`0.1 + 0.2 != 0.3`) when computing cumulative daily totals.
- **Decision 2: Strict Cash vs. UPI Separation**:
  - *Chosen Approach*: Physical Closing Cash calculation strictly follows:
    $$\text{Closing Cash} = \text{Opening Cash} + \text{Cash Sales} - \text{Cash Expenses}$$
    UPI income is never mixed into the vendor's physical pocket cash drawer.
    Daily Profit combines all sales and expenses:
    $$\text{Daily Profit} = (\text{Cash Sales} + \text{UPI Sales}) - (\text{Cash Expenses} + \text{UPI Expenses})$$
  - *Why*: Solves the real-world problem where vendors count their pocket money at night and wonder why it doesn't match their total business profit.
- **Decision 3: Offline-First Local Room SQLite Database**:
  - *Chosen Approach*: 100% on-device storage using Android Room with Kotlin Coroutines and reactive `Flow`.
  - *Why*: Zero network latency, full offline capability during roadside network drops, and absolute privacy for vendor earnings.

---

## 4. Technical Architecture & Data Strategy

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       UI Layer                              │
│  [HomeScreen]  [TransactionsScreen]  [ReportsScreen] [Settings]│
│        ▲                 ▲                 ▲            ▲   │
│        └─────────────────┼─────────────────┼────────────┘   │
│                          │                                  │
│                 VendigoViewModel                            │
│    (Exposes StateFlow<VendorDashboardState>, handles UI events)│
├─────────────────────────────────────────────────────────────┤
│                    Domain & Calculation                     │
│               FinanceCalculationService                     │
│    - Daily Sales, Expenses, Profit/Loss, Cash Balance       │
│    - Date formatting (DD/MM/YYYY IST), Paise to Rupee       │
│    - English & Tamil localized string mappings              │
├─────────────────────────────────────────────────────────────┤
│                     Repository Layer                        │
│                   VendigoRepository                         │
│    - Flow<List<TransactionRecord>>, CRUD, Day Aggregations  │
│    - Flow<DailyCashRecord?>, Opening & Closing Cash CRUD    │
├─────────────────────────────────────────────────────────────┤
│                   Room Database Layer                       │
│    VendigoDatabase (SQLite)                                 │
│    ├── TransactionDao  ──> `transactions` table             │
│    └── DailyCashDao    ──> `daily_cash_records` table       │
└─────────────────────────────────────────────────────────────┘
```

### Data Entities & Schema

```kotlin
// 1. Transaction Entity
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String, // "SALE" or "EXPENSE"
    val amountPaise: Long, // Stored in paise (₹100 = 10000)
    val paymentMethod: String, // "CASH" or "UPI"
    val category: String, // "STOCK", "INGREDIENTS", "TRANSPORT", "PACKAGING", "RENT", "ELECTRICITY", "OTHER", "GENERAL_SALE"
    val description: String = "",
    val dateEpochDay: Long, // Epoch day for clean date queries
    val timestamp: Long = System.currentTimeMillis()
)

// 2. Daily Cash Record Entity
@Entity(tableName = "daily_cash_records")
data class DailyCashRecordEntity(
    @PrimaryKey val dateEpochDay: Long,
    val openingCashPaise: Long = 0L,
    val countedClosingCashPaise: Long? = null,
    val notes: String = ""
)
```

### Key UI Components to Implement
1. **FinancialSummaryBanner**: High-impact cards displaying Today's Sales, Total Expenses, and Net Profit (with positive green / break-even gray / loss red).
2. **CashVsUpiCards**: Side-by-side cards comparing physical cash in hand vs. net recorded UPI amount.
3. **QuickActionTray**: Large 1-tap buttons for rapid transaction entry.
4. **TransactionBottomSheet / Dialog**: Large numeric input pad, quick category pills (Stock, Ingredients, Transport, etc.), and payment toggle.
5. **ReconciliationCard**: Shows opening cash + cash sales - cash expenses = expected closing cash, with input for actual counted cash to highlight discrepancies.
6. **ReportsDashboard**: Time range selector (Today, Week, Month), category expense distribution bar charts, and CSV/Text export.
7. **SettingsView**: Business details, language toggle (English / தமிழ்), and demo data loader.

---

## 5. Execution Plan & Next Steps

Upon your approval:
1. Update `metadata.json` with the official app name ("Vendigo Finance") and description.
2. Configure Room entities, DAOs, and database migrations.
3. Implement `FinanceCalculationService` and bilingual localization strings for English and Tamil.
4. Implement `VendigoRepository` and `VendigoViewModel`.
5. Build UI composables for Home, Transactions, Reports, Settings, and Transaction Modal.
6. Verify and compile the Android app to ensure 100% build success and full interactivity.
