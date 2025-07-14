package Model;

import java.util.Date;

public class HocSinh {
    private String maHocSinh;
    private String hoTen;
    private Date ngaySinh;         // yyyy-MM-dd
    private boolean gioiTinh;        // true=Nam, false=Nữ
    private String trangThai;        // \"Đang học\" hoặc \"Nghỉ học\"
    private String thongTinPhuHuynh;
    private String maLop;

    public String getAnhnguoidung() {
        return Anhnguoidung;
    }

    public void setAnhnguoidung(String Anhnguoidung) {
        this.Anhnguoidung = Anhnguoidung;
    }

    public HocSinh(String maHocSinh, String hoTen, Date ngaySinh, boolean gioiTinh, String trangThai, String thongTinPhuHuynh, String maLop, String Anhnguoidung) {
        this.maHocSinh = maHocSinh;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
        this.thongTinPhuHuynh = thongTinPhuHuynh;
        this.maLop = maLop;
        this.Anhnguoidung = Anhnguoidung;
    }
    private String Anhnguoidung;

    public HocSinh() { }

    // getters & setters
    public String getMaHocSinh() { return maHocSinh; }
    public void setMaHocSinh(String maHocSinh) { this.maHocSinh = maHocSinh; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public Date getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(Date ngaySinh) { this.ngaySinh = ngaySinh; }

    public boolean isGioiTinh() { return gioiTinh; }
    public void setGioiTinh(boolean gioiTinh) { this.gioiTinh = gioiTinh; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getThongTinPhuHuynh() { return thongTinPhuHuynh; }
    public void setThongTinPhuHuynh(String thongTinPhuHuynh) { this.thongTinPhuHuynh = thongTinPhuHuynh; }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
}
