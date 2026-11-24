package model;

import java.time.DayOfWeek;
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

    // [Bagian Farid - Anggota 1] Atribut kenaikan tarif akhir pekan (20%)
    private double persentaseTarifWeekend = 0.20;

    // [Bagian Anggota 2] Atribut diskon lama menginap
    private int minimalMalamDiskon = 3;
    private double persentaseDiskon = 0.10; // 10%

    // [Bagian Meyzaffa & Fitri] Atribut deposit & tanggal pembatalan
    private double deposit;
    private LocalDate tanggalBatal;

    public Reservasi(String kodeReservasi, Kamar kamar, Tamu tamu, int jumlahTamu,
            LocalDate tanggalCheckIn, LocalDate tanggalCheckOut) {
        if (kodeReservasi == null || kodeReservasi.trim().equals("")) {
            throw new IllegalArgumentException("Kode reservasi tidak boleh kosong.");
        }
        if (kamar == null || tamu == null) {
            throw new IllegalArgumentException("Data kamar dan tamu wajib diisi.");
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

    // [Bagian Farid - Anggota 1] Menghitung jumlah malam yang jatuh di akhir pekan
    public long hitungJumlahMalamWeekend() {
        long malamWeekend = 0;
        LocalDate tanggalSekarang = tanggalCheckIn;
        while (tanggalSekarang.isBefore(tanggalCheckOut)) {
            DayOfWeek hari = tanggalSekarang.getDayOfWeek();
            if (hari == DayOfWeek.FRIDAY || hari == DayOfWeek.SATURDAY || hari == DayOfWeek.SUNDAY) {
                malamWeekend++;
            }
            tanggalSekarang = tanggalSekarang.plusDays(1);
        }
        return malamWeekend;
    }

    // [Bagian Farid - Anggota 1] Menghitung subtotal kamar dengan tarif weekend
    // (Jumat, Sabtu, Minggu)
    public double hitungSubtotalKamar() {
        double totalSubtotal = 0;
        LocalDate tanggalSekarang = tanggalCheckIn;
        double hargaNormal = kamar.getHargaPerMalam();

        while (tanggalSekarang.isBefore(tanggalCheckOut)) {
            DayOfWeek hari = tanggalSekarang.getDayOfWeek();
            if (hari == DayOfWeek.FRIDAY || hari == DayOfWeek.SATURDAY || hari == DayOfWeek.SUNDAY) {
                double hargaWeekend = hargaNormal + (hargaNormal * persentaseTarifWeekend);
                totalSubtotal = totalSubtotal + hargaWeekend;
            } else {
                totalSubtotal = totalSubtotal + hargaNormal;
            }
            tanggalSekarang = tanggalSekarang.plusDays(1);
        }
        return totalSubtotal;
    }

    // [Bagian Anggota 2] Menghitung nominal potongan diskon menginap
    public double hitungDiskonMenginap(double subtotal) {
        if (hitungLamaMenginap() >= minimalMalamDiskon) {
            return subtotal * persentaseDiskon;
        }
        return 0;
    }

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
        this.kamar.setTersedia(false);
    }

    public void checkOut() {
        if (!this.status.equals("CHECK_IN")) {
            throw new IllegalStateException("Check-out gagal. Tamu harus berstatus CHECK_IN terlebih dahulu.");
        }
        this.status = "CHECK_OUT";
        this.kamar.setTersedia(true);
        hitungTotalBiaya();
    }

    // [Bagian Meyzaffa & Fitri] Pembatalan reservasi & perhitungan refund deposit
    public void batalkanReservasi(LocalDate tanggalPembatalan) {
        if (!this.status.equals("DIPESAN")) {
            throw new IllegalStateException("Pembatalan gagal. Status reservasi saat ini: " + this.status);
        }
        if (tanggalPembatalan == null) {
            throw new IllegalArgumentException("Tanggal pembatalan tidak boleh kosong.");
        }
        this.status = "DIBATALKAN";
        this.tanggalBatal = tanggalPembatalan;
        this.kamar.setTersedia(true);
    }

    public double hitungRefund() {
        if (!this.status.equals("DIBATALKAN")) {
            throw new IllegalStateException("Refund hanya bisa dihitung untuk reservasi yang dibatalkan.");
        }
        if (!tanggalBatal.isAfter(tanggalCheckIn.minusDays(3))) {
            return deposit;
        }
        return deposit * 0.5;
    }

    // [Bagian Meyzaffa & Fitri] Cek apakah rentang tanggal baru bentrok dengan
    // reservasi ini
    public boolean isTanggalBentrok(LocalDate masukBaru, LocalDate keluarBaru) {
        if (this.status.equals("DIBATALKAN") || this.status.equals("CHECK_OUT")) {
            return false;
        }
        return masukBaru.isBefore(tanggalCheckOut) && keluarBaru.isAfter(tanggalCheckIn);
    }

    public double getPersentaseTarifWeekend() {
        return persentaseTarifWeekend;
    }

    public void setPersentaseTarifWeekend(double persentaseTarifWeekend) {
        if (persentaseTarifWeekend < 0) {
            throw new IllegalArgumentException("Persentase tarif weekend tidak boleh negatif.");
        }
        this.persentaseTarifWeekend = persentaseTarifWeekend;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        if (deposit < 0) {
            throw new IllegalArgumentException("Deposit tidak boleh negatif.");
        }
        this.deposit = deposit;
    }

    public LocalDate getTanggalBatal() {
        return tanggalBatal;
    }

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
                + " | Deposit: Rp " + (long) deposit
                + " | Diskon: Rp " + (long) diskon
                + " | Status: " + status
                + " | Total: Rp " + (long) hitungTotalBiaya();
    }
}