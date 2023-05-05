# 2020humancomputer
# 結合人臉辨識與GPS定位點名系統

這是一個結合人臉辨識與GPS定位的點名系統，旨在提供一個安全、方便、可靠的點名解決方案。

## 專案成員

- 1061524 陳顥元
- 1061525 祁鈞輔
- 1061539 吉瀚宇
- 1061529 鄭竣尹
- 1061427 賈承霖

## 原型

- 以手畫草稿來構想原型
- 程式使用者分為教授與學生
- 採用會員登入系統
- 教授方
    - 可以查找歷史點名活動
    - 發起點名
- 學生方
    - 查找目前現有的點名活動並且進行點名
    - 抓MAC、GPS位址與進行人臉辨識

## 原型要素

- 外觀
    - 因為是Apps，要能給手機族使用，字型不能太小，且外觀簡潔才會有人想用
- 後台資訊
    - 使用者ID
    - 日期
    - 圖像、GPS位址、MAC位址、使用者課表資訊
- 功能性
    - 人臉辨識、MAC位址定位、GPS位址定位
- 互動性
    - 發起點名、進行點名、查找點名歷史、查找課表

## 設計概念

- 透過會員登入系統能保障學生與教授的隱私權且能夠得到自己想得到的資訊(點名紀錄、課表……)
- 將系統分為學生與教授能夠有效地分辨其功能與權限
- 透過查找歷史點名活動功能能讓教授更方便回顧點名結果
- 透過MAC位址能防止同一台手機登入不同帳號幫點名
- 透過GPS位址能防止在不同地點點名
- 透過人臉辨識能防止同一個人拿多隻手機點名

## 實現方法
#### MAC位址定位的方法

我們可以抓取 Android 系統底層的網卡資訊，並將其 MAC 位址擷取出來。這可以透過以下步驟實現：

1. 使用 **WifiManager** 取得 Android 裝置的 Wi-Fi 網路資訊。
2. 從 Wi-Fi 資訊中取得 MAC 位址。

#### GPS位址定位方法

我們可以透過 Google Location API 取得手機的 GPS 位置。這可以透過以下步驟實現：

1. 使用 **LocationManager** 取得 Android 裝置的位置資訊。
2. 透過 Google Play Services 的 Location API 取得裝置的 GPS 位置。

#### 設計app的方法

我們可以使用 Android Studio 來設計我們的應用程式。Android Studio 是一款由 Google 所提供的 Android 應用程式開發工具，它可以協助我們快速建立、測試和打包我們的應用程式。

#### 管理後臺方法

我們可以透過 Firebase 來進行帳戶、課程和點名的管理。Firebase 是一個由 Google 所提供的移動應用程式開發平台，它提供了一系列的工具和服務，用於加速開發移動應用程式，包括實時資料庫、認證、雲端儲存、雲端功能和分析等。

#### 人臉辨識功能怎麼設置

我們可以透過 Microsoft Azure 的 Face Recognition 服務來進行人臉辨識。具體步驟如下：

1. 將 Firebase Storage 取得的學生照片和上課時學生自拍照片上傳至 Microsoft Azure。
2. 使用 Microsoft Azure 的 Face Recognition 服務對這些照片進行辨識，並得出結果。
3. 將辨識結果回傳至我們的應用程式，判斷學生是否已經進行點名。

## 總結

我們的應用程式結合了人臉辨識和 GPS 位置定位技術，並且使用 Firebase 進行後臺管理，提供了一個方便、高效和準確的點名系統，可以幫助教授更好地管理課堂，提高教學效率。
