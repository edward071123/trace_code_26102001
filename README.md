# Spring Boot Trace Code Debug Lab

這是一個練習 Spring Boot、Spring Data JPA、repository 與 dependency injection 的除錯專案。主題是「倉庫庫存調撥系統」。

專案使用 MySQL、Spring Data JPA 與 Hibernate。程式故意留下 **5 個錯誤**；完成修正後，學生應能理解 Spring Bean 如何注入、service 如何呼叫 repository，以及 transaction 中的 entity 如何寫回資料庫。

## 技術與呼叫架構

```text
InventoryApplication
        │
        ▼
InventoryDemoRunner (@Component / CommandLineRunner)
        │ constructor injection
        ├───────────────┐
        ▼               ▼
InventoryQueryService  TransferService (@Service / @Transactional)
        │               │
        └───────┬───────┘
                ▼
Spring Data JPA Repository
        │
        ▼
MySQL Database ← data.sql
```

## 專案結構

```text
.
├── pom.xml
├── README.md
├── docs/
│   └── teacher-notes.md
└── src/main/
    ├── java/com/gtalent/inventory/
    │   ├── InventoryApplication.java
    │   ├── domain/
    │   │   ├── Product.java
    │   │   ├── Stock.java
    │   │   └── StockTransfer.java
    │   ├── repository/
    │   │   ├── ProductRepository.java
    │   │   ├── StockRepository.java
    │   │   └── TransferRepository.java
    │   ├── runner/
    │   │   └── InventoryDemoRunner.java
    │   └── service/
    │       ├── InventoryQueryService.java
    │       └── TransferService.java
    └── resources/
        ├── application.properties
        └── data.sql
```

## 執行方式

請先準備可連線的 MySQL，預設連線資訊如下：

```text
URL:      jdbc:mysql://localhost:3306/inventorydb
Username: root
Password: root
```

使用者必須有建立 `inventorydb` 的權限。也可以用環境變數覆寫連線資訊：

```bash
export DB_URL='jdbc:mysql://localhost:3306/inventorydb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Taipei'
export DB_USERNAME='root'
export DB_PASSWORD='your-password'
```

使用 IntelliJ IDEA 或 VS Code 開啟 Maven 專案，執行 `InventoryApplication`；也可以直接使用專案內附的 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

第一次執行會在 `TransferService.transfer` 發生錯誤。請從 stack trace 找到自己的程式碼位置，依序完成任務，不要一次查看教師答案。

## 正確需求

初始庫存：

| Warehouse | Product | Quantity |
|---|---|---:|
| Taipei | Keyboard | 8 |
| Taichung | Keyboard | 3 |
| Taipei | Mouse | 5 |
| Taichung | Mouse | 6 |
| Taipei | Webcam | 4 |
| Taichung | Webcam | 2 |

程式應將 4 個 Keyboard 從 Taipei 調撥到 Taichung：

- Taipei Keyboard：`8 -> 4`
- Taichung Keyboard：`3 -> 7`
- 新增一筆 quantity 為 4 的 `StockTransfer`

數量為 0 的調撥必須回傳 `false`，不能改庫存，也不能新增紀錄。

## 學生任務

### 任務 1：Repository 沒有被注入

Spring Boot 可以建立 `TransferService` Bean，但執行調撥時發生 `NullPointerException`。

請追蹤：

- stack trace 第一個屬於本專案的行號
- `InventoryDemoRunner` 的 service 如何被注入
- `InventoryQueryService` 的 repository 如何被注入
- `TransferService` 是否也應使用相同的 constructor injection

限制：不要在 service 裡使用 `new` 建立 repository，也不要改用 static 欄位。

### 任務 2：MySQL 初始資料錯誤

修正注入後，比對需求表與啟動時印出的庫存，其中一筆數量不一致。

請追蹤：

- `application.properties` 如何設定 datasource
- Hibernate 建表後，哪個檔案負責插入資料
- `data.sql` 的 column 與 value 對應

### 任務 3：Repository 複合查詢錯誤

查詢 Taichung 的 Keyboard，卻取得 Taipei 的庫存。

請追蹤：

- `InventoryQueryService.findStock`
- `StockRepository.findByWarehouseAndProduct`
- JPQL 的兩個條件應是任一成立，還是同時成立

### 任務 4：目的倉庫的庫存方向錯誤

查詢修正後，來源庫存應扣除 4，但目的庫存應增加 4。

請在 `TransferService.transfer` 觀察兩個 managed entity 的 quantity，確認各自應使用加法或減法。

### 任務 5：0 數量調撥被接受

`transfer(..., 0)` 應回傳 `false`，而且 transaction 不應寫入任何資料。檢查輸入驗證的邊界條件，以及驗證發生在 repository 操作之前或之後。

## 完成條件

全部修正後應同時符合：

1. 啟動過程不出現 exception。
2. Taichung Webcam 初始庫存是 2。
3. 查詢 Taichung Keyboard 得到 Taichung、quantity 3。
4. 4 個 Keyboard 調撥回傳 `true`。
5. 0 個 Mouse 調撥回傳 `false`。
6. 調撥後 Taipei Keyboard 是 4，Taichung Keyboard 是 7。
7. `History count` 是 1。

