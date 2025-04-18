# API 電文代號檢查器插件說明

## 概述

本插件是一個 IntelliJ IDEA 的程式碼檢查工具 (Inspection)，旨在協助開發團隊確保 Java 專案中的 API Controller 方法和 Service 類別遵循特定的 Javadoc 註解規範，特別是關於「電文代號」(ApiMsgId) 的標註。

此插件的主要目標是：

1.  **強制規範**：確保關鍵的 API 入口點和業務邏輯單元都有明確的、符合格式的電文代號標識。
2.  **提高效率**：自動檢測缺失或格式不符的電文代號，並提供智能的快速修復 (Quick Fix) 建議，減少人工檢查和添加註解的負擔。
3.  **增進可維護性**：透過標準化的電文代號，讓開發者更容易理解 API 功能、追蹤業務流程，並關聯相關文件或系統。

## 主要功能

*   **檢查 API 方法**：自動識別使用 Spring Web Mapping 註解（如 `@RequestMapping`, `@GetMapping`, `@PostMapping` 等）標記的方法，並檢查其 Javadoc 是否包含有效的電文代號。
*   **檢查 Service 類別**：檢查被判定為 Service 的接口 (`*Service`, `*Svc`) 或實現類 (`*Impl`, `@Service` 註解) 的 Javadoc 是否包含電文代號。
*   **驗證電文代號格式**：根據預設的正則表達式 `([A-Za-z0-9]+-[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*\\s+.+)` 驗證 Javadoc 中的電文代號格式（例如：`SYS-T-USER_LOGIN 使用者登入`）。
*   **提供智能快速修復**：
    *   當 API 方法缺少電文代號時，提供選項：
        *   生成一個基於 Controller 類名和方法名的預設模板（如 `API-USER_LOGIN [請填寫API描述]`）。
        *   如果該方法內部使用了某個 Service，且該 Service 或其關聯類 (Controller/接口/實現類) 已經有電文代號，則提供選項從該 Service 引用電文代號，並在提示中標明來源 Service 類名。
    *   當 Service 類缺少電文代號時，如果能找到使用此 Service 的 Controller 方法、或其接口/實現類的電文代號，則提供選項添加來自這些來源的電文代號，並在提示中標明來源（如 `Controller 方法 login()` 或 `UserServiceImpl`）。

## 檢查規則詳情

1.  **API 方法 (`ApiMsgIdInspection.visitMethod`)**
    *   觸發條件：方法被 `@*Mapping` 註解標記。
    *   檢查點：方法的 Javadoc (`PsiDocComment`) 是否存在，且其內容是否匹配 `ApiMsgIdUtil.API_ID_PATTERN`。
    *   問題報告：若缺少或格式不符，則報告問題。
2.  **Service 類別 (`ApiMsgIdInspection.visitClass`)**
    *   觸發條件：類別被 `ApiMsgIdUtil.isServiceClass()` 判定為 Service (接口或實現類)。
    *   檢查點：類別的 Javadoc 是否存在且匹配 `ApiMsgIdUtil.API_ID_PATTERN`。
    *   問題報告：若缺少，則嘗試查找關聯的 Controller/Service 電文代號，如果找到則報告問題並提供快速修復建議。

## 工作原理簡介

*   **PSI (Program Structure Interface)**：插件利用 IntelliJ Platform 提供的 PSI 來分析 Java 程式碼的語法結構樹，識別方法、類別、註解、Javadoc 等元素。
*   **References Search**：透過 `ReferencesSearch.search()` 查找特定類別 (Service) 在專案中的所有被引用之處，以嘗試建立 Controller 與 Service 之間的關聯。
*   **命名慣例與註解**：依賴普遍的命名慣例（如 `*Controller`, `*Service`, `*Impl`）和標準註解（如 `@Service`, `@*Mapping`）來識別元件類型。
*   **正則表達式**：使用 `ApiMsgIdUtil.API_ID_PATTERN` 來匹配和驗證 Javadoc 中的電文代號格式。
*   **Quick Fixes**：實現 `LocalQuickFix` 接口，提供在檢查到問題時可以執行的自動化程式碼修改操作（如添加或替換 Javadoc）。

## 使用效益

*   **規範一致性**：確保團隊成員遵循統一的 API 文件標註標準。
*   **文件可讀性**：電文代號提供了一個快速理解介面功能的入口。
*   **可追溯性**：方便將程式碼中的 API 與外部系統、文件或監控指標進行關聯。
*   **開發效率**：減少因忘記添加或格式錯誤導致的溝通成本和後續修改。

## 已知限制與潛在改進點

*   **性能**：在大型專案中，`ReferencesSearch` 可能會消耗較多時間，尤其對於被廣泛使用的基礎 Service。
*   **準確性**：
    *   關聯查找主要依賴直接引用和簡單的文本匹配，對於複雜的依賴注入場景可能無法完全準確。
    *   基於命名慣例的判斷可能不適用於所有專案。
*   **快速修復選擇**：當找到多個可能的電文代號來源時，目前只取第一個，未來可改進為提供選項讓使用者選擇。
*   **可配置性**：電文代號的正則表達式目前是硬編碼，未來可考慮做成可配置選項。

## 如何使用

1.  將此插件打包並安裝到 IntelliJ IDEA 中。
2.  啟用插件後，它會在背景自動分析您開啟的 Java 檔案。
3.  如果檢測到 API 方法或 Service 類別缺少或格式錯誤的電文代號 Javadoc，對應的程式碼行（通常是方法名或類名）旁邊會出現警告標記（例如黃色波浪線）。
4.  將滑鼠懸停在警告標記上，或將游標放在該處並按下快速修復快捷鍵（預設通常是 `Alt+Enter` 或 `Option+Return`），即可看到問題描述和可用的快速修復選項（如 "添加電文代號註解"）。
5.  選擇合適的快速修復選項即可自動修改程式碼。

```mermaid
graph TD
    subgraph "入口：IntelliJ 觸發檢查"
        Start((開始分析 Java 檔案)) --> BuildVisitor{建立 Visitor};
    end

    BuildVisitor --> MethodCheck{檢查方法?};
    BuildVisitor --> ClassCheck{檢查類別?};

    subgraph "分支一：檢查方法 (visitMethod)"
        MethodCheck --> IsApiMethod{是 API 方法嗎};
        IsApiMethod -- 否 --> StopMethod[(結束此方法檢查)];
        IsApiMethod -- 是 --> HasMethodApiId{Javadoc 是否有<br>有效的電文代號?};
        HasMethodApiId -- 是 --> StopMethod;
        HasMethodApiId -- 否 --> FindServiceInMethod[查找方法內使用的 Service<br>及其關聯電文代號];
        FindServiceInMethod --> FoundServiceApiId{從 Service 找到<br>電文代號嗎?};
        FoundServiceApiId -- 是 --> RegisterProblemMethodFromService[註冊問題建議從 Service 添加];
        FoundServiceApiId -- 否 --> RegisterProblemMethodDefault[註冊問題(建議添加模板)];
        RegisterProblemMethodFromService --> StopMethod;
        RegisterProblemMethodDefault --> StopMethod;
    end

    subgraph "分支二：檢查類別 (visitClass)"
        ClassCheck --> IsServiceClass{是 Service 類別嗎?<br>(接口或實現類)};
        IsServiceClass -- 否 --> StopClass[(結束此類別檢查)];
        IsServiceClass -- 是 --> HasClassApiId{Javadoc 是否有<br>有效的電文代號?};
        HasClassApiId -- 是 --> StopClass;
        HasClassApiId -- 否 --> FindRelatedApiId[查找關聯來源的電文代號<br>(Controller/接口/實現類)];

        subgraph "查找關聯來源的詳細邏輯"
            FindRelatedApiId --> IfInterface{是 Service 接口?};
            IfInterface -- 是 --> SearchControllersUsingInterface[查使用此接口的 Controller ID];
            SearchControllersUsingInterface --> FoundCtrlId1{直接找到?};
            FoundCtrlId1 -- 是 --> AddToResultMap(添加到結果);
            FoundCtrlId1 -- 否 --> SearchImplJavadoc[查實現類的 Javadoc ID];
            SearchImplJavadoc --> AddToResultMap;

            IfInterface -- 否 --> SearchControllersUsingImpl[查使用此實現類的 Controller ID];
            SearchControllersUsingImpl --> FoundCtrlId2{直接找到?};
            FoundCtrlId2 -- 是 --> AddToResultMap;
            FoundCtrlId2 -- 否 --> SearchInterfaceJavadoc[查接口的 Javadoc ID<br>或使用接口的 Controller ID];
            SearchInterfaceJavadoc --> AddToResultMap;
        end

        AddToResultMap --> FoundAnyRelatedId{結果 Map<br>是否為空?};
        FoundAnyRelatedId -- 否 (不為空) --> RegisterProblemClass[註冊問題<br>(建議從找到的來源添加)];
        FoundAnyRelatedId -- 是 (為空) --> StopClass;
        RegisterProblemClass --> StopClass;
    end

    style StopMethod fill:#f9f,stroke:#333,stroke-width:2px
    style StopClass fill:#f9f,stroke:#333,stroke-width:2px
```

