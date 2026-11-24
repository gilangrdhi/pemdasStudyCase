# Sistem Reservasi Hotel (Java OOP)

Aplikasi berbasis konsol (*Command Line Interface*) untuk mengelola data kamar, data tamu, proses reservasi, *check-in*, *check-out*, pembatalan, serta perhitungan biaya menginap otomatis menggunakan konsep Pemrograman Berorientasi Objek (OOP) pada Java.

---

## Struktur Folder Proyek

```text
pemdasStudyCase/
├── docs/
│   └── class-diagram.png        # Dokumentasi Class Diagram sistem
└── src/
    ├── app/
    │   └── Main.java            # Alur menu utama, input pengguna, & tampilan rincian biaya
    └── model/
        ├── Kamar.java           # Entitas kamar (nomor, tipe, harga/malam, kapasitas, status)
        ├── Tamu.java            # Entitas tamu (ID/NIK, nama, nomor telepon)
        └── Reservasi.java       # Entitas transaksi & seluruh logika perhitungan bisnis
