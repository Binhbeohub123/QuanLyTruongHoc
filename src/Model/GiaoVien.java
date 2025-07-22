package Model;

import java.util.Date;

public class GiaoVien {
    private String maNguoiDung;
    private String tenNguoiDung;
    private String email;
    private Date ngaySinh;
    private boolean gioiTinh;
    private String trangThai;
    private String diaChi;
    private String soDienThoai;
    private String boMon;
    private String matKhau;
    private String anhNguoiDung;
    private String Vaitro;

    public String getVaitro() {
        return Vaitro;
    }

    public void setVaitro(String Vaitro) {
        this.Vaitro = Vaitro;
    }

    // Getter và Setter
    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }

    public String getTenNguoiDung() { return tenNguoiDung; }
    public void setTenNguoiDung(String tenNguoiDung) { this.tenNguoiDung = tenNguoiDung; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public boolean isGioiTinh() { return gioiTinh; }
    public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getBoMon() { return boMon; }
    public void setBoMon(String boMon) { this.boMon = boMon; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    
    public String getAnhNguoiDung() { return anhNguoiDung; }
    public void setAnhNguoiDung(String anhNguoiDung) { this.anhNguoiDung = anhNguoiDung; }


    public GiaoVien() {
    }

    public GiaoVien(String maNguoiDung, String tenNguoiDung, String email, Date ngaySinh, boolean gioiTinh, String trangThai, String diaChi, String soDienThoai, String boMon, String matKhau,String Vaitro, String anhNguoiDung) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.email = email;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.boMon = boMon;
        this.matKhau = matKhau;
        this.Vaitro = Vaitro;
        this.anhNguoiDung = anhNguoiDung;
    }
    public GiaoVien(String maNguoiDung, String tenNguoiDung, String email, Date ngaySinh, boolean gioiTinh,String trangThai, String diaChi, String soDienThoai, String boMon) {
    this.maNguoiDung = maNguoiDung;
    this.tenNguoiDung = tenNguoiDung;
    this.email = email;
    this.ngaySinh = ngaySinh;
    this.gioiTinh = gioiTinh;
    this.trangThai = trangThai;
    this.diaChi = diaChi;
    this.soDienThoai = soDienThoai;
    this.boMon = boMon;
}
}
