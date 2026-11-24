package model;

public class Tamu {
    private String idTamu;
    private String nama;
    private String noTelepon;

    public Tamu(String idTamu, String nama, String noTelepon) {
        setIdTamu(idTamu);
        setNama(nama);
        setNoTelepon(noTelepon);
    }

    public String getIdTamu() {
        return idTamu;
    }

    public void setIdTamu(String idTamu) {
        if (idTamu == null || idTamu.trim().isEmpty()) {
            throw new IllegalArgumentException("ID Tamu tidak boleh kosong.");
        }
        this.idTamu = idTamu.trim();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tamu tidak boleh kosong.");
        }
        this.nama = nama.trim();
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        if (noTelepon == null || noTelepon.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor telepon tidak boleh kosong.");
        }
        this.noTelepon = noTelepon.trim();
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - Telp: %s", nama, idTamu, noTelepon);
    }
}