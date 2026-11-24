package model;

import java.time.LocalDate;

public class Reservasi {
    private String kodeReservasi;
    private Kamar kamar;
    private Tamu tamu;
    private int jumlahTamu;
    private LocalDate tanggalCheckIn;
    private LocalDate tanggalCheckOut;
    private String status;
    private double totalBiaya;

    // [Bagian Kamu - Anggota 2] Atribut diskon lama menginap
    private int minimalMalamDiskon = 3;
    private double persentaseDiskon = 0.10; // 10%

    // TODO [Meyzaffa / Fitri - Fitur Deposit & Pembatalan]:
    // Tambahkan atribut: private double deposit; beserta getter & setter-nya di
    // bawah

    public Reservasi(String kodeReservasi, Kamar kamar, Tamu tamu, int jumlahTamu,
            LocalDate tanggalCheckIn, LocalDate tanggalCheckOut) {
        if (kodeReservasi == null || kodeReservasi.trim().equals("")) {
            throw new IllegalArgumentException("Kode reservasi tidak boleh kosong.");
        }
        if (kamar == null || tamu == null) {
            throw new IllegalArgumentException("Data kamar dan tamu wajib diisi.");
        }
        // Catatan untuk yang mengerjakan Cek Tanggal: bagian if (!kamar.isTersedia())
        // ini
        // boleh dihapus/disesuaikan nanti jika pengecekan sudah memakai rentang tanggal
        if (!kamar.isTersedia()) {
            throw new IllegalArgumentException("Kamar " + kamar.getNomorKamar() + " sedang tidak tersedia.");
        }
        if (jumlahTamu <= 0 || jumlahTamu > kamar.getKapasitas()) {
            throw new IllegalArgumentException("Jumlah tamu harus antara 1 hingga " + kamar.getKapasitas() + " orang.");
        }
        if (tanggalCheckIn == null || tanggalCheckOut == null || !tanggalCheckOut.isAfter(tanggalCheckIn)) {
            throw new IllegalArgumentException("Tanggal check-out harus setelah tanggal check-in.");
        }

        this.kodeReservasi = kodeReservasi.trim();
        this.kamar = kamar;
        this.tamu = tamu;
        this.jumlahTamu = jumlahTamu;
        this.tanggalCheckIn = tanggalCheckIn;
        this.tanggalCheckOut = tanggalCheckOut;
        this.status = "DIPESAN";
        this.kamar.setTersedia(false);
        this.totalBiaya = hitungTotalBiaya();
    }

    // Menghitung jumlah malam dengan perulangan tanggal sederhana
    public long hitungLamaMenginap() {
        long jumlahMalam = 0;
        LocalDate tanggalSekarang = tanggalCheckIn;
        while (tanggalSekarang.isBefore(tanggalCheckOut)) {
            jumlahMalam++;
            tanggalSekarang = tanggalSekarang.plusDays(1);
        }
        return jumlahMalam;
    }

    // TODO [Farid - Anggota 1 (fitur-tarif-weekend)]:
    // Ubah isi method hitungSubtotalKamar() ini.
    // Gunakan perulangan tanggal dari tanggalCheckIn ke tanggalCheckOut, lalu cek
    // harinya
    // (DayOfWeek.FRIDAY / SATURDAY / SUNDAY) untuk menerapkan tarif akhir pekan.
    public double hitungSubtotalKamar() {
        return hitungLamaMenginap() * kamar.getHargaPerMalam();
    }

    // [Bagian Kamu - Anggota 2] Menghitung nominal potongan diskon menginap
    public double hitungDiskonMenginap(double subtotal) {
        if (hitungLamaMenginap() >= minimalMalamDiskon) {
            return subtotal * persentaseDiskon;
        }
        return 0;
    }

    // Menggabungkan hasil hitungSubtotalKamar() (Farid) & hitungDiskonMenginap()
    // (Kamu)
    public double hitungTotalBiaya() {
        double subtotal = hitungSubtotalKamar();
        double diskon = hitungDiskonMenginap(subtotal);
        this.totalBiaya = subtotal - diskon;
        return this.totalBiaya;
    }

    public void checkIn() {
        if (!this.status.equals("DIPESAN")) {
            throw new IllegalStateException("Check-in gagal. Status reservasi saat ini: " + this.status);
        }
        this.status = "CHECK_IN";
    }

    public void checkOut() {
        if (!this.status.equals("CHECK_IN")) {
            throw new IllegalStateException("Check-out gagal. Tamu harus berstatus CHECK_IN terlebih dahulu.");
        }
        this.status = "CHECK_OUT";
        this.kamar.setTersedia(true);
        hitungTotalBiaya();
    }

    // TODO [Meyzaffa / Fitri - Fitur Deposit & Pembatalan (fitur-deposit-batal)]:
    // Buat method batalkanReservasi() dan hitungRefund() di sini

    // TODO [Meyzaffa / Fitri - Fitur Cek Bentrok Tanggal (fitur-cek-tanggal)]:
    // Buat method isTanggalBentrok(LocalDate masukBaru, LocalDate keluarBaru) di
    // sini

    public int getMinimalMalamDiskon() {
        return minimalMalamDiskon;
    }

    public void setMinimalMalamDiskon(int minimalMalamDiskon) {
        if (minimalMalamDiskon <= 0) {
            throw new IllegalArgumentException("Minimal malam diskon harus lebih dari 0.");
        }
        this.minimalMalamDiskon = minimalMalamDiskon;
    }

    public double getPersentaseDiskon() {
        return persentaseDiskon;
    }

    public void setPersentaseDiskon(double persentaseDiskon) {
        if (persentaseDiskon < 0 || persentaseDiskon > 1) {
            throw new IllegalArgumentException("Persentase diskon harus antara 0.0 hingga 1.0.");
        }
        this.persentaseDiskon = persentaseDiskon;
    }

    public String getKodeReservasi() {
        return kodeReservasi;
    }

    public Kamar getKamar() {
        return kamar;
    }

    public Tamu getTamu() {
        return tamu;
    }

    public int getJumlahTamu() {
        return jumlahTamu;
    }

    public LocalDate getTanggalCheckIn() {
        return tanggalCheckIn;
    }

    public LocalDate getTanggalCheckOut() {
        return tanggalCheckOut;
    }

    public String getStatus() {
        return status;
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    @Override
    public String toString() {
        double subtotal = hitungSubtotalKamar();
        double diskon = hitungDiskonMenginap(subtotal);
        return "[" + kodeReservasi + "] Tamu: " + tamu.getNama()
                + " | Kamar: " + kamar.getNomorKamar()
                + " | " + tanggalCheckIn + " s/d " + tanggalCheckOut
                + " (" + hitungLamaMenginap() + " malam)"
                + " | Diskon: Rp " + (long) diskon
                + " | Status: " + status
                + " | Total: Rp " + (long) hitungTotalBiaya();
    }
}