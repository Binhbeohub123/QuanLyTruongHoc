package Controller;

import DAO.GiaoVienDAO;
import Model.GiaoVien;
import java.util.List;

public class GiaoVienController {
    private final GiaoVienDAO dao;

    public GiaoVienController() {
        this.dao = new GiaoVienDAO(); // Kết nối đã được xử lý nội bộ trong DAO
    }

    // Lấy danh sách giáo viên
    public List<GiaoVien> hienThiTatCaGiaoVien() {
        return dao.getAll();
    }

    // Thêm mới giáo viên
    public boolean themMoi(GiaoVien gv) {
        if (gv == null || gv.getMaNguoiDung() == null || gv.getMaNguoiDung().isEmpty()) {
            return false; // Validate đơn giản
        }
        return dao.insert(gv);
    }

    // Sửa thông tin giáo viên
    public boolean capNhat(GiaoVien gv) {
        if (gv == null || gv.getMaNguoiDung() == null || gv.getMaNguoiDung().isEmpty()) {
            return false;
        }
        return dao.update(gv);
    }

    // Xóa giáo viên theo mã
    public boolean xoa(String maNguoiDung) {
        if (maNguoiDung == null || maNguoiDung.isEmpty()) {
            return false;
        }
        return dao.delete(maNguoiDung);
    }
    public List<GiaoVien> timKiemGiaoVien(String keyword) {
    return dao.search(keyword);
}
    public boolean capNhatMatKhau(String maNguoiDung, String matKhauMoi) {
    if (maNguoiDung == null || maNguoiDung.isEmpty() || matKhauMoi == null || matKhauMoi.isEmpty()) {
        return false;
    }
    return dao.capNhatMatKhau(maNguoiDung, matKhauMoi);
}
    public String layMatKhauTuDB(String maGV) {
    return dao.layMatKhauTheoMa(maGV);
}
    public String layAnhNguoiDung(String maNguoiDung) {
    return dao.layAnhNguoiDung(maNguoiDung);
}
    public boolean xoaGiaoVienVaCapNhatLopHoc(String maGiaoVien) {
    return dao.xoaGiaoVienVaCapNhatLopHoc(maGiaoVien);
}
    public boolean xoaGiaoVien(String maGiaoVien) {
    return dao.xoaGiaoVienVaCapNhatLopHoc(maGiaoVien);
}
    public List<String> layDanhSachLopChuNhiem(String maGiaoVien) {
    return dao.layDanhSachLopChuNhiem(maGiaoVien);
}
    public boolean Giaovienvenull(String maGiaoVien){
         return dao.giaLapChuaCoGiaoVien(maGiaoVien);
     }
    public boolean kiemTraTrungTenNguoiDung(String ten) {
    return dao.kiemTraTrungTenNguoiDung(ten);
}

// Check trùng email
public boolean kiemTraTrungEmail(String email) {
    return dao.kiemTraTrungEmail(email);
}

// Check trùng số điện thoại
public boolean kiemTraTrungSDT(String sdt) {
    return dao.kiemTraTrungSoDienThoai(sdt);
}
public boolean kiemTraTrungEmailKhiCapNhat(String email, String maNguoiDung) {
    return dao.kiemTraTrungEmailKhiCapNhat(email, maNguoiDung);
}

public boolean kiemTraTrungSDTKhiCapNhat(String sdt, String maNguoiDung) {
    return dao.kiemTraTrungSDTKhiCapNhat(sdt, maNguoiDung);
}

public boolean kiemTraTrungTenKhiCapNhat(String ten, String maNguoiDung) {
    return dao.kiemTraTrungTenKhiCapNhat(ten, maNguoiDung);
}
}
