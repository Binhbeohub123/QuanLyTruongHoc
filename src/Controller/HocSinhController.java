package Controller;

import Dao.HocSinhDAO;
import Model.HocSinh;
import java.util.List;

public class HocSinhController {
    private final HocSinhDAO dao;

    public HocSinhController() {
        this.dao = new HocSinhDAO(); // HocSinhDAO tự khởi tạo kết nối DB bên trong
    }

    // Lấy danh sách tất cả học sinh
    public List<HocSinh> hienThiTatCaHocSinh() {
        return dao.selectAll();
    }

    // Thêm mới học sinh
    public boolean Themmoi(HocSinh hs) {
        if (hs == null || hs.getMaHocSinh() == null || hs.getMaHocSinh().isEmpty()) {
            return false; // validate đơn giản: mã HS không được null/empty
        }
        return dao.insert(hs);
    }

    // Cập nhật thông tin học sinh
    public boolean capNhat(HocSinh hs) {
        if (hs == null || hs.getMaHocSinh() == null || hs.getMaHocSinh().isEmpty()) {
            return false;
        }
        return dao.update(hs);
    }

    // Xóa học sinh theo mã
    public boolean xoa(String maHocSinh) {
        if (maHocSinh == null || maHocSinh.isEmpty()) {
            return false;
        }
        return dao.delete(maHocSinh);
    }

    // Tìm kiếm học sinh theo từ khóa (tên, mã, lớp… tuỳ DAO triển khai)
    public List<HocSinh> timKiemHocSinh(String keyword) {
        return dao.search(keyword);
    }
    public String sinhMaHocSinhMoi() {
        return dao.sinhMaHocSinhMoi();
        }
    public boolean kiemTraTrungTenHocSinh(String ten) {
        return dao.checkTrungTen(ten);
    }

    // Check trùng tên khi cập nhật
    public boolean kiemTraTrungTenHocSinhKhiCapNhat(String ten, String maHS) {
        return dao.checkTrungTenKhiCapNhat(ten, maHS);
    }

}
