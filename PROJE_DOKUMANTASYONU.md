# RESTORAN YÖNETİM SİSTEMİ - PROJE DOKÜMANTASYONU

## 📋 İçindekiler
1. [Proje Genel Bakış](#proje-genel-bakış)
2. [Proje Yapısı](#proje-yapısı)
3. [Package Yapısı ve Sınıflar](#package-yapısı-ve-sınıflar)
4. [Detaylı Sınıf Dokümantasyonu](#detaylı-sınıf-dokümantasyonu)
5. [Veri Yapısı](#veri-yapısı)
6. [Kullanım Kılavuzu](#kullanım-kılavuzu)

---

## 🎯 Proje Genel Bakış

Bu proje, Java konsol uygulaması olarak geliştirilmiş bir restoran yönetim sistemidir. Sistem iki ana modülden oluşur:
- **Müşteri Modülü**: Sipariş verme, rezervasyon yapma
- **İşletme Modülü**: Menü yönetimi, sipariş takibi, garson yönetimi

### Teknik Özellikler
- **Dil**: Java
- **OOP Prensipleri**: Inheritance, Encapsulation, Abstraction, Polymorphism
- **Yapılar**: Abstract Class, Interface, Inner Class
- **Hata Yönetimi**: Exception Handling
- **Veri Saklama**: .txt dosyaları
- **Toplam Sınıf Sayısı**: 37+

---

## 📁 Proje Yapısı

```
restoran app/
├── src/
│   └── com/
│       └── restoran/
│           ├── Main.java
│           ├── exception/      (4 sınıf)
│           ├── model/          (13 sınıf)
│           ├── service/        (9 sınıf)
│           ├── data/           (1 sınıf)
│           ├── util/           (4 sınıf)
│           └── ui/             (3 sınıf)
├── data/                        (Veri dosyaları)
└── README.md
```

---

## 📦 Package Yapısı ve Sınıflar

### 1. com.restoran
**Ana paket - Uygulama giriş noktası**

#### Main.java
- **Amaç**: Uygulamanın başlangıç noktası
- **Ana Fonksiyon**: `main(String[] args)`
  - Başlangıç verilerini oluşturur (DataInitializer)
  - RestaurantApp'i başlatır

---

### 2. com.restoran.exception
**Özel Exception sınıfları**

#### CustomException.java
- **Amaç**: Tüm özel exception'ların temel sınıfı
- **Özellikler**: 
  - Exception sınıfından türetilmiş
  - Mesaj parametresi alır

#### InvalidInputException.java
- **Amaç**: Geçersiz kullanıcı girişleri için
- **Kullanım Yerleri**: 
  - Boş alan kontrolü
  - Negatif sayı kontrolü
  - Format hataları

#### FileOperationException.java
- **Amaç**: Dosya işlem hataları için
- **Kullanım Yerleri**: 
  - Dosya okuma/yazma hataları
  - Dosya bulunamama

#### NotFoundException.java
- **Amaç**: Kayıt bulunamama durumları için
- **Kullanım Yerleri**: 
  - Ürün bulunamadı
  - Sipariş bulunamadı
  - Masa bulunamadı

---

### 3. com.restoran.model
**Veri model sınıfları**

#### OrderStatus.java (Enum)
- **Amaç**: Sipariş durumlarını tanımlar
- **Değerler**:
  - `BEKLEMEDE`: Sipariş alındı, bekliyor
  - `HAZIRLANIYOR`: Sipariş hazırlanıyor
  - `HAZIR`: Sipariş hazır
  - `SERVIS_EDILDI`: Sipariş servis edildi
  - `IPTAL`: Sipariş iptal edildi
- **Metodlar**:
  - `getDescription()`: Durum açıklamasını döndürür

#### User.java (Abstract)
- **Amaç**: Tüm kullanıcı tiplerinin temel sınıfı
- **Özellikler**:
  - `name`, `surname`, `username`, `password`
- **Abstract Metodlar**:
  - `getUserType()`: Kullanıcı tipini döndürür
- **Metodlar**:
  - `getFullName()`: Ad ve soyadı birleştirir

#### Customer.java
- **Amaç**: Müşteri bilgilerini tutar
- **Özellikler**:
  - `customerId`: Otomatik artan ID
- **Inheritance**: User sınıfından türetilmiş
- **Metodlar**:
  - `getUserType()`: "Müşteri" döndürür

#### Business.java
- **Amaç**: İşletme bilgilerini tutar
- **Özellikler**:
  - `businessName`: İşletme adı
  - `address`: İşletme adresi
- **Inheritance**: User sınıfından türetilmiş
- **Metodlar**:
  - `getUserType()`: "İşletme" döndürür

#### MenuItem.java (Abstract)
- **Amaç**: Menü öğelerinin temel sınıfı
- **Özellikler**:
  - `itemId`: Ürün ID
  - `name`: Ürün adı
  - `price`: Fiyat
  - `description`: Açıklama
  - `isActive`: Aktiflik durumu
- **Abstract Metodlar**:
  - `getItemType()`: Ürün tipini döndürür

#### Food.java
- **Amaç**: Yemek ürünlerini temsil eder
- **Özellikler**:
  - `category`: Kategori (Ana Yemek, Çorba, Salata vb.)
- **Inheritance**: MenuItem sınıfından türetilmiş
- **Metodlar**:
  - `getItemType()`: "Yemek" döndürür

#### Dessert.java
- **Amaç**: Tatlı ürünlerini temsil eder
- **Özellikler**:
  - `dessertType`: Tatlı tipi (Sütlü, Şerbetli, Dondurma vb.)
- **Inheritance**: MenuItem sınıfından türetilmiş
- **Metodlar**:
  - `getItemType()`: "Tatlı" döndürür

#### Menu.java (Abstract)
- **Amaç**: Menü tiplerinin temel sınıfı
- **Özellikler**:
  - `menuName`: Menü adı
  - `menuType`: Menü tipi
- **Abstract Metodlar**:
  - `displayMenu()`: Menüyü gösterir

#### FoodMenu.java
- **Amaç**: Yemek menüsünü temsil eder
- **Inheritance**: Menu sınıfından türetilmiş
- **Metodlar**:
  - `displayMenu()`: Yemek menüsünü gösterir

#### DessertMenu.java
- **Amaç**: Tatlı menüsünü temsil eder
- **Inheritance**: Menu sınıfından türetilmiş
- **Metodlar**:
  - `displayMenu()`: Tatlı menüsünü gösterir

#### Order.java
- **Amaç**: Sipariş bilgilerini tutar
- **Özellikler**:
  - `orderId`: Sipariş ID
  - `customerId`: Müşteri ID
  - `customerName`: Müşteri adı
  - `tableNumber`: Masa numarası
  - `items`: Sipariş edilen ürünler (String)
  - `totalAmount`: Toplam tutar
  - `status`: Sipariş durumu (OrderStatus)
  - `orderDate`: Sipariş tarihi
  - `waiterId`: Garson ID
  - `waiterName`: Garson adı
- **Metodlar**:
  - `getFormattedDate()`: Tarihi formatlanmış şekilde döndürür

#### Reservation.java
- **Amaç**: Rezervasyon bilgilerini tutar
- **Özellikler**:
  - `reservationId`: Rezervasyon ID
  - `customerName`: Müşteri adı
  - `customerPhone`: Müşteri telefonu
  - `tableNumber`: Masa numarası
  - `reservationDate`: Rezervasyon tarihi
  - `numberOfGuests`: Kişi sayısı
  - `isActive`: Aktiflik durumu
- **Metodlar**:
  - `getFormattedDate()`: Tarihi formatlanmış şekilde döndürür

#### Table.java
- **Amaç**: Masa bilgilerini tutar
- **Özellikler**:
  - `tableNumber`: Masa numarası
  - `capacity`: Kapasite
  - `isOccupied`: Dolu mu?
  - `isReserved`: Rezerve mi?

#### Waiter.java
- **Amaç**: Garson bilgilerini tutar
- **Özellikler**:
  - `waiterId`: Garson ID
  - `name`: Ad
  - `surname`: Soyad
  - `phoneNumber`: Telefon
  - `isAvailable`: Müsait mi?
- **Metodlar**:
  - `getFullName()`: Ad ve soyadı birleştirir

#### Payment.java
- **Amaç**: Ödeme bilgilerini tutar
- **Özellikler**:
  - `paymentId`: Ödeme ID
  - `orderId`: Sipariş ID
  - `amount`: Tutar
  - `paymentMethod`: Ödeme yöntemi
  - `paymentDate`: Ödeme tarihi
  - `isPaid`: Ödendi mi?
- **Metodlar**:
  - `getFormattedDate()`: Tarihi formatlanmış şekilde döndürür

---

### 4. com.restoran.service
**İş mantığı servisleri**

#### IMenuService.java (Interface)
- **Amaç**: Menü servisi için interface
- **Metodlar**:
  - `addFood()`: Yemek ekle
  - `addDessert()`: Tatlı ekle
  - `deleteItem()`: Ürün sil
  - `updatePrice()`: Fiyat güncelle
  - `displayMenu()`: Menüyü göster

#### IOrderService.java (Interface)
- **Amaç**: Sipariş servisi için interface
- **Metodlar**:
  - `createOrder()`: Sipariş oluştur
  - `assignWaiter()`: Garson ata
  - `getActiveOrders()`: Aktif siparişleri getir
  - `getOrderHistory()`: Geçmiş siparişleri getir

#### LoginService.java
- **Amaç**: İşletme giriş işlemlerini yönetir
- **Metodlar**:
  - `login(String username, String password)`: Giriş yapar
    - Kullanıcı adı ve şifre kontrolü yapar
    - Business nesnesi döndürür
    - Hata durumunda exception fırlatır

#### MenuManager.java
- **Amaç**: Menü yönetimi işlemlerini yapar
- **Interface**: IMenuService'i implement eder
- **Özellikler**:
  - `nextItemId`: Sonraki ürün ID'si
- **Metodlar**:
  - `addFood()`: Yemek ekler
    - Parametreler: name, price, description, category
    - Yeni Food nesnesi oluşturur ve kaydeder
  - `addDessert()`: Tatlı ekler
    - Parametreler: name, price, description, dessertType
    - Yeni Dessert nesnesi oluşturur ve kaydeder
  - `deleteItem(int itemId)`: Ürün siler
    - Menü dosyasından ilgili satırı kaldırır
  - `updatePrice(int itemId, double newPrice)`: Fiyat günceller
    - Ürünü bulur, sadece fiyatı değiştirir
    - Diğer bilgileri korur
  - `displayMenu()`: Menüyü formatlanmış şekilde gösterir
    - Aktif ürünleri listeler
    - ID, Tip, Ad, Fiyat, Açıklama bilgilerini gösterir

#### OrderManager.java
- **Amaç**: Sipariş yönetimi işlemlerini yapar
- **Interface**: IOrderService'i implement eder
- **Özellikler**:
  - `nextOrderId`: Sonraki sipariş ID'si
- **Metodlar**:
  - `createOrder()`: Yeni sipariş oluşturur
    - Parametreler: customerId, customerName, tableNumber, items, totalAmount
    - Order nesnesi oluşturur ve kaydeder
    - Order ID döndürür
  - `assignWaiter()`: Siparişe garson atar
    - Parametreler: orderId, waiterId, waiterName
    - Sipariş dosyasını günceller
  - `updateOrderStatus()`: Sipariş durumunu günceller
    - Parametreler: orderId, newStatus
    - Sipariş durumunu değiştirir
  - `getOrderTableNumber()`: Siparişin masa numarasını döndürür
    - Parametre: orderId
    - Masa numarası döndürür
  - `getActiveOrders()`: Aktif siparişleri getirir
    - SERVIS_EDILDI ve IPTAL olmayan siparişleri listeler
  - `getOrderHistory()`: Tüm siparişleri getirir
  - `getCustomerOrders(int customerId)`: Müşteri siparişlerini getirir

#### ReservationManager.java
- **Amaç**: Rezervasyon yönetimi işlemlerini yapar
- **Özellikler**:
  - `nextReservationId`: Sonraki rezervasyon ID'si
- **Metodlar**:
  - `createReservation()`: Yeni rezervasyon oluşturur
    - Parametreler: customerName, customerPhone, tableNumber, reservationDate
    - Reservation nesnesi oluşturur ve kaydeder
  - `cancelReservation(int reservationId)`: Rezervasyonu iptal eder
    - isActive = false yapar
    - Masa durumunu günceller
  - `isReservationExists()`: Aynı saatte aynı masaya rezervasyon var mı kontrol eder
    - Çakışma kontrolü yapar
  - `getAllReservations()`: Tüm rezervasyonları getirir

#### TableManager.java
- **Amaç**: Masa yönetimi işlemlerini yapar
- **Metodlar**:
  - `addTable(int tableNumber, int capacity)`: Yeni masa ekler
  - `getAllTables()`: Tüm masaları listeler
    - Durum bilgisiyle birlikte gösterir (Boş/Dolu/Rezerve)
  - `updateTableStatus()`: Masa durumunu günceller
    - Parametreler: tableNumber, isOccupied, isReserved
  - `isTableAvailable()`: Masa müsait mi kontrol eder
    - Dolu veya rezerve değilse true döndürür
  - `getNextTableNumber()`: Sonraki masa numarasını döndürür
    - Otomatik numara atama için kullanılır

#### WaiterManager.java
- **Amaç**: Garson yönetimi işlemlerini yapar
- **Özellikler**:
  - `nextWaiterId`: Sonraki garson ID'si
- **Metodlar**:
  - `addWaiter()`: Yeni garson ekler
    - Parametreler: name, surname, phoneNumber
  - `getAllWaiters()`: Tüm garsonları listeler
  - `getWaiterInfo(int waiterId)`: Garson bilgisini döndürür
    - Ad ve soyadı döndürür
  - `removeWaiter(int waiterId)`: Garsonu siler

#### PaymentService.java
- **Amaç**: Ödeme işlemlerini yönetir
- **Özellikler**:
  - `nextPaymentId`: Sonraki ödeme ID'si
- **Metodlar**:
  - `processPayment()`: Ödeme işlemini gerçekleştirir
    - Payment nesnesi oluşturur
  - `formatPaymentInfo()`: Ödeme bilgisini formatlar

#### ReportService.java
- **Amaç**: Rapor oluşturma işlemlerini yapar
- **Metodlar**:
  - `generateDailyReport()`: Günlük rapor oluşturur
  - `calculateTotalRevenue()`: Toplam geliri hesaplar

---

### 5. com.restoran.data
**Veri yönetimi**

#### DataManager.java
- **Amaç**: Tüm veri okuma/yazma işlemlerini yönetir
- **Özellikler**: Static metodlar
- **Dosya İşlemleri**:
  - `saveBusiness()`: İşletme kaydeder
  - `loadBusiness()`: İşletme yükler
  - `saveMenuItem()`: Menü öğesi kaydeder
  - `getAllMenuItems()`: Tüm menü öğelerini getirir
  - `updateMenuFile()`: Menü dosyasını günceller
  - `saveOrder()`: Sipariş kaydeder
  - `getAllOrders()`: Tüm siparişleri getirir
  - `updateOrderFile()`: Sipariş dosyasını günceller
  - `saveReservation()`: Rezervasyon kaydeder
  - `getAllReservations()`: Tüm rezervasyonları getirir
  - `updateReservationFile()`: Rezervasyon dosyasını günceller
  - `saveTable()`: Masa kaydeder
  - `getAllTables()`: Tüm masaları getirir
  - `updateTableFile()`: Masa dosyasını günceller
  - `saveWaiter()`: Garson kaydeder
  - `getAllWaiters()`: Tüm garsonları getirir
  - `updateWaiterFile()`: Garson dosyasını günceller

---

### 6. com.restoran.util
**Yardımcı sınıflar**

#### FileHandler.java
- **Amaç**: Dosya işlemlerini yönetir
- **Özellikler**:
  - `DATA_DIR`: Veri klasörü yolu ("data")
- **Metodlar**:
  - `writeToFile()`: Dosyaya ekleme yapar (append)
  - `overwriteFile()`: Dosyayı tamamen yeniden yazar
  - `readFromFile()`: Dosyadan okur
  - `fileExists()`: Dosya var mı kontrol eder

#### DataInitializer.java
- **Amaç**: Başlangıç verilerini oluşturur
- **Metodlar**:
  - `initializeData()`: İlk çalıştırmada verileri oluşturur
    - İşletme hesabı (restoran/1234)
    - 15 ürünlü menü
    - 8 masa
    - 3 garson

#### InputValidator.java
- **Amaç**: Giriş doğrulama işlemlerini yapar
- **Metodlar**:
  - `validateNotEmpty()`: Boş olmamalı kontrolü
  - `validatePositive()`: Pozitif sayı kontrolü

#### StringProcessor.java
- **Amaç**: String işlemlerini yapar
- **Metodlar**:
  - `capitalizeFirst()`: İlk harfi büyütür
  - `formatPrice()`: Fiyatı formatlar
  - `truncate()`: String'i kısaltır
  - `containsIgnoreCase()`: Büyük/küçük harf duyarsız arama

---

### 7. com.restoran.ui
**Kullanıcı arayüzleri**

#### RestaurantApp.java
- **Amaç**: Ana uygulama sınıfı
- **Inner Class**: AppConfig
  - Uygulama adı ve versiyon bilgisi
- **Metodlar**:
  - `start()`: Uygulamayı başlatır
    - Giriş menüsü gösterir
    - Müşteri veya İşletme seçimi yapar
  - `handleBusinessLogin()`: İşletme girişi yönetir
    - Kullanıcı adı ve şifre alır
    - LoginService ile giriş yapar

#### CustomerInterface.java
- **Amaç**: Müşteri arayüzü
- **Özellikler**:
  - `scanner`: Kullanıcı girişi için
  - `customer`: Müşteri bilgisi
  - `menuManager`, `orderManager`, `reservationManager`, `tableManager`: Servisler
- **Metodlar**:
  - `start()`: Müşteri panelini başlatır
    - Ad ve soyad alır
    - Customer nesnesi oluşturur
  - `showMenu()`: Müşteri menüsünü gösterir
    - 1. Sipariş Ver
    - 2. Rezervasyon Yap
    - 3. Rezervasyon İptal
    - 4. Aktif Siparişler
    - 5. Geçmiş Siparişler
    - 0. Çıkış
  - `placeOrder()`: Sipariş verme işlemi
    - Menü ve masaları gösterir
    - Ürün seçimi alır
    - Masa seçimi alır
    - Sipariş oluşturur
    - Masa durumunu günceller
    - Sipariş detaylarını dosyaya kaydeder
  - `makeReservation()`: Rezervasyon yapma
    - Telefon, masa, saat alır
    - Aynı saatte çakışma kontrolü yapar
    - Rezervasyon oluşturur
    - Masa durumunu günceller
  - `cancelReservation()`: Rezervasyon iptali
    - Rezervasyon ID alır
    - Rezervasyonu iptal eder
  - `showActiveOrders()`: Aktif siparişleri gösterir
    - Müşterinin aktif siparişlerini listeler
  - `showOrderHistory()`: Geçmiş siparişleri gösterir
    - Müşterinin geçmiş siparişlerini listeler
  - `saveOrderToFile()`: Sipariş detaylarını dosyaya kaydeder

#### BusinessInterface.java
- **Amaç**: İşletme arayüzü
- **Özellikler**:
  - `scanner`: Kullanıcı girişi için
  - `business`: İşletme bilgisi
  - `menuManager`, `orderManager`, `reservationManager`, `tableManager`, `waiterManager`: Servisler
- **Metodlar**:
  - `start()`: İşletme panelini başlatır
  - `showMenu()`: İşletme menüsünü gösterir
    - 1. Menü Yönetimi
    - 2. Ürün Ekleme
    - 3. Ürün Silme
    - 4. Fiyat Değiştirme
    - 5. Aktif Siparişler
    - 6. Geçmiş Siparişler
    - 7. Sipariş Durumu Güncelleme
    - 8. Masa Bilgileri
    - 9. Rezervasyonlar
    - 10. Rezervasyon İptal
    - 11. Garson Atama
    - 12. Garson Ekleme
    - 13. Garson Çıkarma
    - 14. Masa Ekleme
    - 0. Çıkış
  - `showMenuManagement()`: Menüyü gösterir
  - `addProduct()`: Ürün ekler (Yemek veya Tatlı)
  - `deleteProduct()`: Ürün siler
  - `updatePrice()`: Fiyat günceller
  - `showActiveOrders()`: Aktif siparişleri gösterir
  - `showOrderHistory()`: Geçmiş siparişleri gösterir
  - `updateOrderStatus()`: Sipariş durumunu günceller
    - Durum seçimi yapar
    - Sipariş durumunu günceller
    - SERVIS_EDILDI veya IPTAL ise masayı boşaltır
  - `showTableInfo()`: Masaları gösterir
  - `showReservations()`: Rezervasyonları gösterir
  - `cancelReservation()`: Rezervasyon iptal eder
  - `assignWaiter()`: Siparişe garson atar
  - `addWaiter()`: Garson ekler
  - `removeWaiter()`: Garson siler
  - `addTable()`: Masa ekler (otomatik numara atama)

---

## 💾 Veri Yapısı

### Dosya Formatları

#### business.txt
```
name|surname|username|password|businessName|address
```

#### menu.txt
```
type|id|name|price|description|isActive|category/dessertType
```
- **type**: "Yemek" veya "Tatlı"
- **category**: Yemekler için (Ana Yemek, Çorba, Salata vb.)
- **dessertType**: Tatlılar için (Sütlü, Şerbetli, Dondurma vb.)

#### orders.txt
```
orderId|customerId|customerName|tableNumber|items|totalAmount|status|orderDate|waiterId|waiterName
```

#### reservations.txt
```
reservationId|customerName|customerPhone|tableNumber|reservationDate|numberOfGuests|isActive
```

#### tables.txt
```
tableNumber|capacity|isOccupied|isReserved
```

#### waiters.txt
```
waiterId|name|surname|phoneNumber|isAvailable
```

#### order_details.txt
```
=== SİPARİŞ DETAYI ===
Sipariş ID: ...
Müşteri: ...
Masa No: ...
Tarih: ...
Ürünler:
  - ...
Toplam Tutar: ... TL
Durum: ...
==========================================
```

---

## 🚀 Kullanım Kılavuzu

### Derleme
```bash
javac -d bin -sourcepath src src/com/restoran/Main.java
```

### Çalıştırma
```bash
java -cp bin com.restoran.Main
```

### İlk Kullanım
- Uygulama ilk çalıştırıldığında otomatik olarak başlangıç verileri oluşturulur
- İşletme girişi: `restoran` / `1234`

### Müşteri İşlemleri
1. **Sipariş Verme**:
   - Menü ve masalar gösterilir
   - Ürün ID'leri virgülle ayrılarak girilir
   - Masa numarası seçilir
   - Sipariş oluşturulur

2. **Rezervasyon**:
   - Masalar gösterilir
   - Telefon, masa, saat bilgileri alınır
   - Aynı saatte çakışma kontrolü yapılır

### İşletme İşlemleri
1. **Menü Yönetimi**:
   - Ürün ekleme/silme
   - Fiyat güncelleme

2. **Sipariş Yönetimi**:
   - Aktif siparişleri görüntüleme
   - Sipariş durumu güncelleme
   - Garson atama

3. **Rezervasyon Yönetimi**:
   - Rezervasyonları görüntüleme
   - Rezervasyon iptal

---

## 🔧 Teknik Detaylar

### OOP Prensipleri Kullanımı

#### Inheritance (Kalıtım)
- `User` → `Customer`, `Business`
- `MenuItem` → `Food`, `Dessert`
- `Menu` → `FoodMenu`, `DessertMenu`

#### Encapsulation (Kapsülleme)
- Tüm model sınıflarında private alanlar
- Getter/Setter metodları

#### Abstraction (Soyutlama)
- `User`, `MenuItem`, `Menu` abstract sınıflar
- Abstract metodlar tanımlanmış

#### Polymorphism (Çok Biçimlilik)
- `MenuItem` referansı ile `Food` ve `Dessert` kullanımı
- `Menu` referansı ile `FoodMenu` ve `DessertMenu` kullanımı

#### Interface
- `IMenuService`, `IOrderService` interface'leri
- `MenuManager` ve `OrderManager` bu interface'leri implement eder

#### Inner Class
- `RestaurantApp` içinde `AppConfig` inner class

### Exception Handling
- Try-catch blokları tüm kritik işlemlerde
- Özel exception sınıfları
- Anlamlı hata mesajları

### String İşlemleri
- `StringBuilder` kullanımı (performans için)
- `StringTokenizer` ile parsing
- String metodları (contains, substring, indexOf vb.)

---

## 📝 Notlar

- Veriler `data/` klasöründe .txt dosyalarında saklanır
- Dosyalar append modunda yazılır (üzerine ekleme)
- Güncelleme işlemlerinde dosya tamamen yeniden yazılır
- Masa numaraları otomatik atanır (1'den başlayarak)
- Sipariş durumu "Servis Edildi" veya "İptal" olduğunda masa otomatik boşalır
- Rezervasyon iptal edildiğinde masa durumu güncellenir

---

## 👨‍💻 Geliştirici Notları

### Yeni Özellik Ekleme
1. Model sınıfı oluştur (gerekirse)
2. Service sınıfı oluştur veya mevcut servise metod ekle
3. DataManager'a kaydetme/okuma metodları ekle
4. UI sınıfına arayüz ekle

### Hata Ayıklama
- Tüm dosya işlemleri try-catch ile korunmuş
- Exception mesajları anlamlı
- Dosya formatları sabit (| ile ayrılmış)

### Performans
- ArrayList kullanılmamış (String/StringBuilder tercih edilmiş)
- Dosya işlemleri append modunda (hızlı yazma)
- Güncelleme işlemlerinde sadece ilgili satır değiştirilir

---

**Proje Versiyonu**: 1.0  
**Son Güncelleme**: 2025  
**Geliştirici**: Restoran Yönetim Sistemi Ekibi

