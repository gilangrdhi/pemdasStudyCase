package app;

import model.Kamar;
import model.Reservasi;
import model.Tamu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Kamar> daftarKamar = new ArrayList<>();
    static ArrayList<Reservasi> daftarReservasi = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        isiDataAwal();

        boolean jalan = true;
        while (jalan) {
            tampilkanMenu();
            String pilihan = input.nextLine();

            if (pilihan.equals("1")) {
                tambahKamar();
            } else if (pilihan.equals("2")) {
                tampilkanDaftarKamar(false);
            } else if (pilihan.equals("3")) {
                tampilkanDaftarKamar(true);
            } else if (pilihan.equals("4")) {
                buatReservasi();
            } else if (pilihan.equals("5")) {
                prosesCheckIn();
            } else if (pilihan.equals("6")) {
                prosesCheckOut();
            } else if (pilihan.equals("7")) {
                tampilkanSemuaReservasi();
            } else if (pilihan.equals("8")) {
                prosesPembatalanReservasi();
            } else if (pilihan.equals("0")) {
                jalan = false;
                System.out.println("Program selesai. Terima kasih!");
            } else {
                System.out.println("Pilihan tidak valid, silakan pilih 0 - 8.");
            }
        }
    }

    static void isiDataAwal() {
        daftarKamar.add(new Kamar("101", "Standard", 350000, 2));
        daftarKamar.add(new Kamar("102", "Deluxe", 550000, 3));
        daftarKamar.add(new Kamar("201", "Suite", 900000, 4));
    }

    static void tampilkanMenu() {
        System.out.println("\n=== SISTEM RESERVASI HOTEL ===");
        System.out.println("1. Tambah Kamar Baru");
        System.out.println("2. Lihat Semua Kamar");
        System.out.println("3. Lihat Kamar Tersedia");
        System.out.println("4. Buat Reservasi Baru");
        System.out.println("5. Check-In Tamu");
        System.out.println("6. Check-Out & Hitung Total Biaya");
        System.out.println("7. Lihat Daftar Reservasi");
        System.out.println("8. Batalkan Reservasi");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    static void tambahKamar() {
        try {
            System.out.print("Nomor Kamar: ");
            String nomor = input.nextLine();

            if (cariKamar(nomor) != null) {
                System.out.println("Gagal: Nomor kamar sudah ada.");
                return;
            }

            System.out.print("Tipe Kamar (Standard/Deluxe/Suite): ");
            String tipe = input.nextLine();

            System.out.print("Harga per Malam (Rp): ");
            double harga = Double.parseDouble(input.nextLine());

            System.out.print("Kapasitas Maksimal Tamu: ");
            int kapasitas = Integer.parseInt(input.nextLine());

            Kamar kamarBaru = new Kamar(nomor, tipe, harga, kapasitas);
            daftarKamar.add(kamarBaru);
            System.out.println("Berhasil menambah kamar: " + kamarBaru);
        } catch (Exception e) {
            System.out.println("Gagal menambah kamar: " + e.getMessage());
        }
    }

    static void tampilkanDaftarKamar(boolean hanyaTersedia) {
        if (hanyaTersedia) {
            System.out.println("\n--- DAFTAR KAMAR TERSEDIA ---");
        } else {
            System.out.println("\n--- SEMUA DAFTAR KAMAR ---");
        }

        boolean adaKamar = false;
        for (Kamar k : daftarKamar) {
            if (!hanyaTersedia || k.isTersedia()) {
                System.out.println("- " + k);
                adaKamar = true;
            }
        }

        if (!adaKamar) {
            System.out.println("Tidak ada data kamar.");
        }
    }

    static void buatReservasi() {
        try {
            tampilkanDaftarKamar(false);
            System.out.print("\nPilih Nomor Kamar: ");
            String nomorKamar = input.nextLine();
            Kamar kamar = cariKamar(nomorKamar);

            if (kamar == null) {
                System.out.println("Kamar tidak ditemukan.");
                return;
            }

            System.out.print("ID Tamu (NIK/KTP): ");
            String idTamu = input.nextLine();
            System.out.print("Nama Tamu: ");
            String namaTamu = input.nextLine();
            System.out.print("No. Telepon: ");
            String noTelp = input.nextLine();

            Tamu tamu = new Tamu(idTamu, namaTamu, noTelp);

            System.out.print("Jumlah Tamu: ");
            int jumlahTamu = Integer.parseInt(input.nextLine());

            System.out.print("Tanggal Check-In (YYYY-MM-DD): ");
            LocalDate checkIn = LocalDate.parse(input.nextLine());

            System.out.print("Tanggal Check-Out (YYYY-MM-DD): ");
            LocalDate checkOut = LocalDate.parse(input.nextLine());

            // [Bagian Meyzaffa & Fitri] Cek bentrok tanggal pada kamar yang dipilih
            if (!isKamarTersediaPadaTanggal(kamar, checkIn, checkOut)) {
                System.out.println("Reservasi ditolak: Kamar sudah dipesan pada rentang tanggal tersebut.");
                return;
            }

            // [Bagian Meyzaffa & Fitri] Input nominal deposit saat reservasi
            System.out.print("Nominal Deposit (Rp): ");
            double deposit = Double.parseDouble(input.nextLine());

            String kodeRes = "RSV-" + (daftarReservasi.size() + 1);
            Reservasi reservasi = new Reservasi(kodeRes, kamar, tamu, jumlahTamu, checkIn, checkOut);
            reservasi.setDeposit(deposit);
            daftarReservasi.add(reservasi);

            System.out.println("\nReservasi berhasil dibuat!");
            cetakRincianBiaya(reservasi);
        } catch (Exception e) {
            System.out.println("Reservasi gagal: " + e.getMessage());
        }
    }

    // [Bagian Meyzaffa & Fitri] Mengecek semua reservasi aktif agar tanggal tidak
    // bentrok
    static boolean isKamarTersediaPadaTanggal(Kamar kamar, LocalDate checkIn, LocalDate checkOut) {
        for (Reservasi r : daftarReservasi) {
            if (r.getKamar().getNomorKamar().equalsIgnoreCase(kamar.getNomorKamar())) {
                if (r.isTanggalBentrok(checkIn, checkOut)) {
                    return false;
                }
            }
        }
        return true;
    }

    static void prosesCheckIn() {
        System.out.print("Masukkan Kode Reservasi (contoh: RSV-1): ");
        String kode = input.nextLine();
        Reservasi r = cariReservasi(kode);

        if (r == null) {
            System.out.println("Kode reservasi tidak ditemukan.");
            return;
        }

        try {
            r.checkIn();
            System.out.println("Check-in berhasil untuk " + r.getTamu().getNama());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static void prosesCheckOut() {
        System.out.print("Masukkan Kode Reservasi (contoh: RSV-1): ");
        String kode = input.nextLine();
        Reservasi r = cariReservasi(kode);

        if (r == null) {
            System.out.println("Kode reservasi tidak ditemukan.");
            return;
        }

        try {
            r.checkOut();
            System.out.println("\nCheck-out berhasil!");
            cetakRincianBiaya(r);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // [Bagian Meyzaffa & Fitri] Proses pembatalan reservasi & hitung refund deposit
    static void prosesPembatalanReservasi() {
        System.out.print("Masukkan Kode Reservasi yang ingin dibatalkan: ");
        String kode = input.nextLine();
        Reservasi r = cariReservasi(kode);

        if (r == null) {
            System.out.println("Kode reservasi tidak ditemukan.");
            return;
        }

        try {
            System.out.print("Tanggal Pembatalan (YYYY-MM-DD): ");
            LocalDate tglBatal = LocalDate.parse(input.nextLine());

            r.batalkanReservasi(tglBatal);
            double refund = r.hitungRefund();

            System.out.println("\nReservasi " + r.getKodeReservasi() + " berhasil dibatalkan.");
            System.out.println("Deposit Awal : Rp " + (long) r.getDeposit());
            System.out.println("Dana Refund  : Rp " + (long) refund);
        } catch (Exception e) {
            System.out.println("Pembatalan gagal: " + e.getMessage());
        }
    }

    // [Bagian Anggota 2] Menampilkan rincian biaya, diskon, dan deposit
    static void cetakRincianBiaya(Reservasi r) {
        long malam = r.hitungLamaMenginap();
        double subtotal = r.hitungSubtotalKamar();
        double diskon = r.hitungDiskonMenginap(subtotal);
        int persen = (int) (r.getPersentaseDiskon() * 100);
        double sisaBayar = r.getTotalBiaya() - r.getDeposit();
        if (sisaBayar < 0) {
            sisaBayar = 0;
        }

        System.out.println("--- RINCIAN BIAYA (" + r.getKodeReservasi() + ") ---");
        System.out.println("Lama Menginap : " + malam + " malam (" + r.getTanggalCheckIn() + " s/d "
                + r.getTanggalCheckOut() + ")");
        System.out.println("Subtotal      : Rp " + (long) subtotal);
        if (diskon > 0) {
            System.out.println("Diskon (" + persen + "%)  : -Rp " + (long) diskon + " (Menginap >= "
                    + r.getMinimalMalamDiskon() + " malam)");
        } else {
            System.out.println("Diskon        : Rp 0");
        }
        System.out.println("Total Tagihan : Rp " + (long) r.getTotalBiaya());
        System.out.println("Deposit       : Rp " + (long) r.getDeposit());
        System.out.println("Sisa Bayar    : Rp " + (long) sisaBayar);
    }

    static void tampilkanSemuaReservasi() {
        System.out.println("\n--- DAFTAR RESERVASI ---");
        if (daftarReservasi.isEmpty()) {
            System.out.println("Belum ada data reservasi.");
            return;
        }
        for (Reservasi r : daftarReservasi) {
            System.out.println("- " + r);
        }
    }

    static Kamar cariKamar(String nomorKamar) {
        for (Kamar k : daftarKamar) {
            if (k.getNomorKamar().equalsIgnoreCase(nomorKamar)) {
                return k;
            }
        }
        return null;
    }

    static Reservasi cariReservasi(String kode) {
        for (Reservasi r : daftarReservasi) {
            if (r.getKodeReservasi().equalsIgnoreCase(kode)) {
                return r;
            }
        }
        return null;
    }
}