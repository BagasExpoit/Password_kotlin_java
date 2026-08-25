# Password Generator (Android - Kotlin + Java)

Aplikasi Android native untuk membuat kata sandi acak yang kuat.

## Fitur
- Generator password (panjang & jenis karakter dapat diatur) — logika inti ditulis dalam **Java** (`PasswordUtils.java`), dipanggil dari **Kotlin** (`MainActivity.kt`).
- Splash screen dengan animasi logo saat aplikasi dibuka (`SplashActivity.kt`).
- Menu **Pengaturan**: pilih tema (Terang / Gelap / Ikuti Sistem) dan aktif/nonaktifkan animasi pembuka.
- Desain antarmuka bernuansa **cream & gold walet** (lihat `res/values/colors.xml`).
- Halaman **Tentang & Kontak Pengembang** (email & GitHub dapat diketuk langsung).

## Struktur Proyek
```
app/src/main/java/com/dev/passwordgenerator/
├── SplashActivity.kt      # splash + animasi
├── MainActivity.kt        # layar generator password
├── SettingsActivity.kt    # pengaturan tema & animasi
├── AboutActivity.kt       # tentang & kontak
├── ThemeManager.kt        # penyimpan preferensi tema
└── PasswordUtils.java     # logika pembuatan password (Java)
```

## Menjalankan di Android Studio
1. Buka folder proyek ini di Android Studio (Giraffe atau lebih baru).
2. Biarkan Android Studio menghasilkan `gradle-wrapper.jar` otomatis (File → Sync Project with Gradle Files), atau jalankan `gradle wrapper` sekali bila punya Gradle terpasang secara lokal.
3. Jalankan pada emulator/perangkat.

## Ubah Kontak Pengembang
Edit `app/src/main/res/values/strings.xml`:
```xml
<string name="developer_name">Nama Anda</string>
<string name="developer_email">email@anda.com</string>
<string name="developer_github">github.com/username-anda</string>
```

## Build & Release Otomatis (GitHub Actions)
Workflow `.github/workflows/release.yml` akan otomatis:
1. Build APK release saat Anda push tag versi, misalnya:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. Membuat GitHub Release baru dan mengunggah file APK (`PasswordGenerator-v1.0.0.apk`) sebagai output/asset rilis.

Bisa juga dijalankan manual lewat tab **Actions → Build and Release APK → Run workflow**.

## Menandatangani APK (Signing / JKS)

APK release sekarang bisa ditandatangani otomatis. Ada 2 cara:

### A. Build lokal (Android Studio / terminal)
1. Buat keystore (jalankan di komputer Anda, jangan di CI):
   ```bash
   keytool -genkeypair -v \
     -keystore release-key.jks \
     -alias password-generator-key \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -storepass PASSWORD_STORE_ANDA \
     -keypass PASSWORD_KEY_ANDA \
     -dname "CN=Nama Anda, OU=Dev, O=Perusahaan, L=Kota, ST=Provinsi, C=ID"
   ```
2. Letakkan `release-key.jks` di root proyek (sejajar folder `app/`).
3. Salin `keystore.properties.example` menjadi `keystore.properties`, isi dengan password & alias Anda. File ini sudah masuk `.gitignore` — jangan pernah di-commit.
4. Jalankan `./gradlew assembleRelease` — APK yang dihasilkan sudah ditandatangani.

### B. Build otomatis via GitHub Actions (signed release)
Tambahkan **4 GitHub Secrets** di repo Anda (Settings → Secrets and variables → Actions → New repository secret):

| Nama Secret         | Isi                                                              |
|----------------------|-------------------------------------------------------------------|
| `KEYSTORE_BASE64`    | Hasil `base64 -w 0 release-key.jks` (isi file jks dalam base64)  |
| `KEYSTORE_PASSWORD`  | Password store keystore Anda                                     |
| `KEY_ALIAS`          | Alias key, contoh: `password-generator-key`                     |
| `KEY_PASSWORD`       | Password key Anda                                                 |

Command untuk membuat nilai `KEYSTORE_BASE64` (di komputer Anda):
```bash
base64 -w 0 release-key.jks > keystore_base64.txt
# lalu copy isi keystore_base64.txt ke secret KEYSTORE_BASE64
```

Setelah secret terisi, setiap push tag `v*.*.*` akan otomatis menghasilkan **APK yang sudah ditandatangani** dan diunggah ke GitHub Release. Workflow menghapus file keystore dari runner setelah build selesai (tidak disimpan/di-commit).

> Jika secret belum diisi, workflow tetap bisa build tapi APK-nya **unsigned** (hanya untuk uji internal, tidak bisa diinstal update di atas versi signed).
