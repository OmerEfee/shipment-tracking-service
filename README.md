# 📦 LogiTrack - Shipment Tracking Service

Kurumsal standartlarda; State Design Pattern, Spring Data JPA ve MS SQL Server kullanılarak geliştirilmiş Kargo Takip ve Durum Yönetimi REST API servisi.

## 🚀 Öne Çıkan Özellikler

- **State Pattern ile Durum Yönetimi:** Kargo durum geçişleri (`CREATED` -> `IN_TRANSIT` -> `OUT_FOR_DELIVERY` -> `DELIVERED` / `CANCELLED`) spagetti kod yerine nesne yönelimli State Pattern ile kural tabanlı yönetilir.
- **Factory Pattern & Dependency Injection:** Durum sınıfları Spring Context üzerinden dinamik toplanır ve $O(1)$ karmaşıklıkla eşleştirilir (Open/Closed Principle).
- **Audit Logging (Tarihçe Kaydı):** Her durum değişikliği zaman damgası ve konum bilgisiyle `shipment_status_logs` tablosuna otomatik işlenir.
- **Güvenli Takip Kodu Üretimi:** Kafa karıştırıcı karakterleri arındırılmış, tarih bazlı ve `SecureRandom` kullanan kurumsal takip formatı (`TRK-YYYYMMDD-XXXXX`).
- **Global Exception Handling:** `@RestControllerAdvice` ile tüm sistem ve validasyon hataları standart JSON formatında istemciye sunulur.
- **Swagger / OpenAPI:** Canlı test edilebilir API dokümantasyonu.
- **Birim Testleri:** Mockito ve JUnit 5 ile izole edilmiş iş mantığı ve durum geçiş testleri.

## 🛠️ Teknoloji Yığını

- **Dil & Framework:** Java 17, Spring Boot 4.x
- **Veritabanı:** MS SQL Server (Hibernate 7, Spring Data JPA)
- **Dokümantasyon:** SpringDoc OpenAPI (Swagger UI)
- **Test:** JUnit 5, Mockito
- **Araçlar:** Lombok, Jakarta Validation

## 🔌 API Uç Noktaları

| Metot | Uç Nokta | Açıklama |
|---|---|---|
| `POST` | `/api/v1/shipments` | Yeni kargo kaydı oluşturur |
| `GET` | `/api/v1/shipments/{trackingNumber}` | Takip numarasıyla kargo ve tarihçe sorgular |
| `PATCH` | `/api/v1/shipments/{trackingNumber}/status` | Kargo durumunu günceller (State denetimli) |

## 📖 Canlı Dokümantasyon
Uygulama çalışırken tarayıcıdan erişilebilir:
`http://localhost:8080/swagger-ui/index.html`