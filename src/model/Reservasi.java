package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservasi {
    private String kodeReservasi;
    private Kamar kamar;
    private Tamu tamu;
    private int jumlahTamu;
    private LocalDate tanggalCheckIn;
    private LocalDate tanggalCheckOut;
    private String status;
    private double totalBiaya;

    private int minimalMalamDiskon = 3;
    private double persentaseDiskon = 0.10;

    public Reservasi(String kodeReservasi, Kamar kamar, Tamu tamu, int jumlahTamu,
            LocalDate tanggalCheckIn, LocalDate tanggalCheckOut) {
        if (kodeReservasi == null || kodeReservasi.trim().isEmpty()) {
            throw new IllegalArgumentException("Kode reservasi tidak boleh kosong.");
        }
        if (kamar == null || tamu == null) {
            throw new IllegalArgumentException("Data kamar dan tamu wajib diisi.");
        }
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

    public long hitungLamaMenginap() {
        return ChronoUnit.DAYS.between(tanggalCheckIn, tanggalCheckOut);
    }

    public double hitungSubtotalKamar() {
        return hitungLamaMenginap() * kamar.getHargaPerMalam();
    }

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
        if (!"DIPESAN".equals(this.status)) {
            throw new IllegalStateException("Check-in gagal. Status reservasi saat ini: " + this.status);
        }
        this.status = "CHECK_IN";
    }

    public void checkOut() {
        if (!"CHECK_IN".equals(this.status)) {
            throw new IllegalStateException("Check-out gagal. Tamu harus berstatus CHECK_IN terlebih dahulu.");
        }
        this.status = "CHECK_OUT";
        this.kamar.setTersedia(true);
        hitungTotalBiaya();
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
        return String.format(
                "[%s] Tamu: %s | Kamar: %s | %s s/d %s (%d malam) | Diskon: Rp%.0f | Status: %s | Total: Rp%.0f",
                kodeReservasi, tamu.getNama(), kamar.getNomorKamar(),
                tanggalCheckIn, tanggalCheckOut, hitungLamaMenginap(), diskon, status, hitungTotalBiaya());
    }
}