## Pembagian Tugas & Penjelasan Kode Per Anggota

### 1. Farid — Anggota 1 (`fitur-tarif-weekend`)
* **Inti Tugas:** Membuat harga kamar otomatis naik **20%** khusus pada malam akhir pekan (Jumat, Sabtu, dan Minggu).
* **File & Method yang Dikerjakan:**
  * `src/model/Reservasi.java` $\rightarrow$ variabel `persentaseTarifWeekend = 0.20`, method `hitungJumlahMalamWeekend()`, dan method `hitungSubtotalKamar()`.
  * `src/app/Main.java` $\rightarrow$ bagian tampilan `Tarif Weekend` di dalam method `cetakRincianBiaya()`.
* **Alur Logika Kode (Cara Kerja):**
  1. Jika tidak ada fitur ini, harga kamar cukup dikali langsung (`jumlahMalam * hargaNormal`). Namun karena tarif akhir pekan berbeda dengan hari biasa, program harus mengecek tanggal menginap **satu per satu dari hari pertama sampai hari terakhir**.
  2. Di dalam `hitungSubtotalKamar()`, dibuat variabel bantuan `tanggalSekarang` yang dimulai dari `tanggalCheckIn`.
  3. Menggunakan perulangan `while (tanggalSekarang.isBefore(tanggalCheckOut))`, program mengecek nama hari pada tanggal tersebut memakai fungsi bawaan Java `tanggalSekarang.getDayOfWeek()`.
  4. Jika harinya adalah `FRIDAY` (Jumat), `SATURDAY` (Sabtu), atau `SUNDAY` (Minggu), harga malam itu ditambah 20% (`hargaNormal + (hargaNormal * 0.20)`). Jika hari Senin–Kamis, memakai `hargaNormal`.
  5. Setelah harga malam tersebut ditambahkan ke `totalSubtotal`, tanggal dimajukan 1 hari (`tanggalSekarang.plusDays(1)`) sampai mencapai tanggal *check-out*.
* **Contoh Simulasi Saat Ditanya Dosen:**
  * *Kasus:* Tamu memesan Kamar 101 (Rp 350.000/malam) untuk *check-in* **Jumat** dan *check-out* **Minggu** (2 malam: malam Jumat dan malam Sabtu).
  * *Hasil:* Karena kedua malam tersebut jatuh di akhir pekan, masing-masing malam dikenakan tarif Rp 350.000 + Rp 70.000 (20%) = Rp 420.000. Total subtotalnya menjadi **Rp 840.000**.

---

### 2. Gilang Ardhi Maulana — Anggota 2 (`fitur-diskon-menginap`)
* **Inti Tugas:** Menyusun kerangka dasar OOP (`Kamar`, `Tamu`, menu `Main`), menghitung lama menginap, dan memberikan **diskon 10%** jika tamu menginap minimal **3 malam**.
* **File & Method yang Dikerjakan:**
  * `src/model/Kamar.java` & `src/model/Tamu.java` $\rightarrow$ seluruh atribut `private`, *constructor*, *getter*, dan *setter* bervalidasi.
  * `src/model/Reservasi.java` $\rightarrow$ variabel `minimalMalamDiskon = 3`, `persentaseDiskon = 0.10`, method `hitungLamaMenginap()`, `hitungDiskonMenginap()`, dan `hitungTotalBiaya()`.
  * `src/app/Main.java` $\rightarrow$ menu utama (`1–8`), penyimpanan data `ArrayList`, dan method `cetakRincianBiaya()`.
* **Alur Logika Kode (Cara Kerja):**
  1. Pada `Kamar.java` dan `Tamu.java`, seluruh data dibuat `private` (enkapsulasi) dan dilindungi di method `set...()` menggunakan `if` (misal harga tidak boleh $\le 0$, nama tidak boleh kosong). Jika salah input, program melempar pesan error `IllegalArgumentException` agar ditangkap oleh `try-catch` di `Main.java`.
  2. Pada `hitungLamaMenginap()`, program menghitung selisih hari dari `tanggalCheckIn` hingga sebelum `tanggalCheckOut` menggunakan perulangan `while` dan menambah penghitung `jumlahMalam++`.
  3. Pada `hitungDiskonMenginap(double subtotal)`, program mengecek syarat `if (hitungLamaMenginap() >= minimalMalamDiskon)`. Jika menginap 3 malam atau lebih, diskon dihitung sebesar `subtotal * 0.10`. Jika kurang dari 3 malam, diskonnya `0`.
  4. Pada `hitungTotalBiaya()`, hasil subtotal kamar (dari kode Farid) dikurangi dengan nominal diskon (`subtotal - diskon`).
* **Contoh Simulasi Saat Ditanya Dosen:**
  * *Kasus:* Tamu menginap **3 malam** pada hari Senin–Kamis di Kamar 101 (Rp 350.000/malam).
  * *Hasil:* Subtotal = 3 $\times$ Rp 350.000 = Rp 1.050.000. Karena lama menginap $\ge 3$ malam, mendapat diskon 10% = **Rp 105.000**, sehingga total tagihan akhir menjadi **Rp 945.000**.

---

### 3. Meyzaffa — Anggota 3 (`fitur-deposit-batal`)
* **Inti Tugas:** Mengelola uang jaminan (**deposit**) saat pemesanan, memproses **pembatalan reservasi**, dan menghitung uang kembali (**refund**) sesuai batas waktu H-3.
* **File & Method yang Dikerjakan:**
  * `src/model/Reservasi.java` $\rightarrow$ variabel `deposit` dan `tanggalBatal`, method `batalkanReservasi(LocalDate tanggalPembatalan)`, dan method `hitungRefund()`.
  * `src/app/Main.java` $\rightarrow$ input deposit di `buatReservasi()`, hitungan `sisaBayar` di `cetakRincianBiaya()`, dan Menu 8 `prosesPembatalanReservasi()`.
* **Alur Logika Kode (Cara Kerja):**
  1. Saat membuat reservasi baru di `Main.java`, petugas memasukkan nominal `deposit` yang disimpan ke objek reservasi lewat `reservasi.setDeposit(deposit)`. Di rincian biaya, tagihan dikurangi deposit untuk menampilkan `sisaBayar` yang harus dilunasi tamu.
  2. Saat tamu ingin membatalkan pesanan (Menu 8), method `batalkanReservasi()` mengecek apakah statusnya masih `"DIPESAN"`. Jika tamu sudah terlanjur `"CHECK_IN"` atau `"CHECK_OUT"`, pembatalan otomatis ditolak.
  3. Jika status masih `"DIPESAN"`, status diubah menjadi `"DIBATALKAN"` dan tanggal hari pembatalan disimpan ke variabel `tanggalBatal`.
  4. Di dalam `hitungRefund()`, program menghitung batas maksimal H-3 sebelum *check-in* memakai `tanggalCheckIn.minusDays(3)`. Jika pembatalan dilakukan pada atau sebelum H-3 (`!tanggalBatal.isAfter(...)`), deposit dikembalikan **100%**. Jika lewat dari H-3 (misal H-2 atau H-1), deposit dipotong setengah (**refund 50%**).
* **Contoh Simulasi Saat Ditanya Dosen:**
  * *Kasus:* Tamu membayar deposit **Rp 200.000** untuk jadwal *check-in* tanggal **10 Desember** (berarti batas H-3 adalah **7 Desember**).
  * *Hasil 1:* Jika dibatalkan tanggal **7 Desember**, uang kembali 100% = **Rp 200.000**.
  * *Hasil 2:* Jika dibatalkan tanggal **8 Desember** (sudah lewat H-3), uang kembali 50% = **Rp 100.000**.

---

### 4. Fitri — Anggota 4 (`fitur-cek-tanggal`)
* **Inti Tugas:** Mencegah bentrok jadwal (**double booking**) pada kamar yang sama, sekaligus mengizinkan kamar dipesan oleh tamu lain pada tanggal yang kosong.
* **File & Method yang Dikerjakan:**
  * `src/model/Reservasi.java` $\rightarrow$ method `isTanggalBentrok(LocalDate masukBaru, LocalDate keluarBaru)` dan pemindahan `this.kamar.setTersedia(false)` dari *constructor* ke method `checkIn()`.
  * `src/app/Main.java` $\rightarrow$ method `isKamarTersediaPadaTanggal(Kamar kamar, LocalDate checkIn, LocalDate checkOut)`.
* **Alur Logika Kode (Cara Kerja):**
  1. Pada kode awal, saat tamu baru memesan kamar (`DIPESAN`), status kamar langsung diubah menjadi `tersedia = false` sehingga kamar tidak bisa dipesan sama sekali untuk bulan depan. Fitri mengubahnya agar `tersedia = false` baru aktif saat tamu benar-benar datang (`checkIn()`).
  2. Sebagai gantinya, ketersediaan kamar dicek berdasarkan **rentang tanggal** melalui `isKamarTersediaPadaTanggal()` di `Main.java`.
  3. Method tersebut melakukan perulangan `for` untuk memeriksa seluruh data di `daftarReservasi` yang nomor kamarnya sama.
  4. Di dalam `isTanggalBentrok()`, reservasi lama yang statusnya sudah `"DIBATALKAN"` atau `"CHECK_OUT"` langsung dilewati (`return false`) karena kamarnya sudah kosong kembali.
  5. Untuk reservasi yang masih aktif, program mengecek irisan tanggal dengan rumus: `masukBaru.isBefore(tanggalCheckOut) && keluarBaru.isAfter(tanggalCheckIn)`. Jika kedua kondisi benar, artinya jadwal bertabrakan (`return true`) dan pesanan baru ditolak.
* **Contoh Simulasi Saat Ditanya Dosen:**
  * *Data Awal:* Kamar 101 sudah dipesan Tamu A untuk tanggal **10 Desember s/d 13 Desember**.
  * *Kasus 1 (Ditolak):* Tamu B ingin memesan Kamar 101 tanggal **12 Desember s/d 15 Desember**. Karena 12 Des sebelum 13 Des **dan** 15 Des setelah 10 Des, jadwal dinyatakan **bentrok** dan otomatis ditolak.
  * *Kasus 2 (Diterima):* Tamu C memesan Kamar 101 tanggal **13 Desember s/d 15 Desember**. Karena Tamu A sudah *check-out* pada 13 Desember (`13 Des` tidak sebelum `13 Des`), jadwal dinyatakan **aman** dan reservasi berhasil dibuat.
