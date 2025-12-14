# Restoran Yönetim Sistemi

Java konsol uygulaması - Restoran işletmesi ve müşteri yönetimi için geliştirilmiş bir sistem.

## Özellikler

### Müşteri Paneli
- Sipariş menüsü görüntüleme
- Masa seçimi
- Yemek/Tatlı seçimi ve sipariş verme
- Rezervasyon yapma
- Aktif siparişleri görüntüleme
- Geçmiş siparişleri görüntüleme

### İşletme Paneli
- Menü yönetimi
- Ürün ekleme/silme
- Fiyat değiştirme
- Aktif siparişleri görüntüleme
- Geçmiş siparişleri görüntüleme
- Masa bilgileri
- Rezervasyon yönetimi
- Garson atama ve yönetimi

## Kullanım

### Derleme
```bash
javac -d bin -sourcepath src src/com/restoran/Main.java
```

### Çalıştırma
```bash
java -cp bin com.restoran.Main
```

## İlk Kurulum

Uygulama ilk çalıştırıldığında otomatik olarak başlangıç verileri oluşturulur:

**İşletme Hesabı:**
- Kullanıcı adı: `restoran`
- Şifre: `1234`
- İşletme adı: Lezzet Restoran

**Menü:**
- 10 yemek (Adana Kebap, Urfa Kebap, Döner, Lahmacun, Pide, Çorbalar, Salatalar, vb.)
- 5 tatlı (Baklava, Sütlaç, Künefe, Dondurma, Kazandibi)

**Masalar:**
- 8 masa (kapasiteleri 2-8 kişi arası)

**Garsonlar:**
- 3 garson (Mehmet Demir, Ayşe Kaya, Ali Çelik)

Tüm veriler `data/` klasöründe .txt dosyalarında saklanır.

## Teknik Özellikler

- **OOP Prensipleri**: Inheritance, Encapsulation, Abstraction, Polymorphism
- **Yapılar**: Abstract class ve Interface kullanımı
- **Hata Yönetimi**: Exception handling ve özel exception sınıfları
- **Veri Saklama**: .txt dosyaları ile veri saklama
- **Inner Class**: RestaurantApp içinde AppConfig inner class
- **String İşlemleri**: String methodları ve StringBuilder kullanımı

## Proje Yapısı

```
src/
├── com/
│   └── restoran/
│       ├── Main.java
│       ├── exception/      (Exception sınıfları)
│       ├── model/          (Model sınıfları)
│       ├── service/        (Servis sınıfları)
│       ├── data/           (Veri yönetimi)
│       ├── util/           (Utility sınıfları)
│       └── ui/             (Arayüz sınıfları)
```

## Notlar

- Veriler `data/` klasöründe .txt dosyalarında saklanır
- Müşteri girişi için sadece ad-soyad yeterlidir
- İşletme girişi için kullanıcı adı ve şifre gereklidir

