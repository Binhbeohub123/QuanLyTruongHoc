package Model;

import java.util.Date;

public class HocSinh {
    private String maHocSinh;
    private String hoTen;
    private Date ngaySinh;         // yyyy-MM-dd
    private boolean gioiTinh;        // true=Nam, false=Nữ
    private String trangThai;        // \"Đang học\" hoặc \"Nghỉ học\"
    private String maLop;
    private String tenCha;
    private String sdtCha;
    private String tenMe;
    private String sdtMe;

    public String getAnhnguoidung() {
        return Anhnguoidung;
    }

    public void setAnhnguoidung(String Anhnguoidung) {
        this.Anhnguoidung = Anhnguoidung;
    }

    public HocSinh(String maHocSinh, String hoTen, Date ngaySinh, boolean gioiTinh, String trangThai, String maLop, String Anhnguoidung,
               String tenCha, String sdtCha, String tenMe, String sdtMe) {
    this.maHocSinh = maHocSinh;
    this.hoTen = hoTen;
    this.ngaySinh = ngaySinh;
    this.gioiTinh = gioiTinh;
    this.trangThai = trangThai;
    this.maLop = maLop;
    this.Anhnguoidung = Anhnguoidung;
    this.tenCha = tenCha;
    this.sdtCha = sdtCha;
    this.tenMe = tenMe;
    this.sdtMe = sdtMe;
}

    public String getTenCha() {
        return tenCha;
    }

    public void setTenCha(String tenCha) {
        this.tenCha = tenCha;
    }

    public String getSdtCha() {
        return sdtCha;
    }

    public void setSdtCha(String sdtCha) {
        this.sdtCha = sdtCha;
    }

    public String getTenMe() {
        return tenMe;
    }

    public void setTenMe(String tenMe) {
        this.tenMe = tenMe;
    }

    public String getSdtMe() {
        return sdtMe;
    }

    public void setSdtMe(String sdtMe) {
        this.sdtMe = sdtMe;
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


    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
}
