package model;

public class Kamar {
    private String nomorKamar;
    private String tipeKamar;
    private double hargaPerMalam;
    private int kapasitas;
    private boolean tersedia;

    public Kamar(String nomorKamar, String tipeKamar, double hargaPerMalam, int kapasitas) {
        setNomorKamar(nomorKamar);
        setTipeKamar(tipeKamar);
        setHargaPerMalam(hargaPerMalam);
        setKapasitas(kapasitas);
        this.tersedia = true;
    }

    public String getNomorKamar() {
        return nomorKamar;
    }

    public void setNomorKamar(String nomorKamar) {
        if (nomorKamar == null || nomorKamar.trim().equals("")) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong.");
        }
        this.nomorKamar = nomorKamar.trim();
    }

    public String getTipeKamar() {
        return tipeKamar;
    }

    public void setTipeKamar(String tipeKamar) {
        if (tipeKamar == null || tipeKamar.trim().equals("")) {
            throw new IllegalArgumentException("Tipe kamar tidak boleh kosong.");
        }
        this.tipeKamar = tipeKamar.trim();
    }

    public double getHargaPerMalam() {
        return hargaPerMalam;
    }

    public void setHargaPerMalam(double hargaPerMalam) {
        if (hargaPerMalam <= 0) {
            throw new IllegalArgumentException("Harga kamar harus lebih dari 0.");
        }
        this.hargaPerMalam = hargaPerMalam;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public void setKapasitas(int kapasitas) {
        if (kapasitas <= 0) {
            throw new IllegalArgumentException("Kapasitas kamar harus lebih dari 0.");
        }
        this.kapasitas = kapasitas;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    @Override
    public String toString() {
        String statusKamar = "Terisi";
        if (tersedia) {
            statusKamar = "Tersedia";
        }
        return "Kamar " + nomorKamar + " [" + tipeKamar + "] | Kapasitas: " + kapasitas
                + " org | Rp " + (long) hargaPerMalam + "/malam | Status: " + statusKamar;
    }
}