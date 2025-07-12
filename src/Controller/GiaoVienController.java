package Controller;

import Dao.GiaoVienDAO;
import Model.GiaoVien;
import java.util.List;

public class GiaoVienController {
    private final GiaoVienDAO dao;

    public GiaoVienController() {
        this.dao = new GiaoVienDAO(null); // Kết nối đã được xử lý nội bộ trong DAO
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
}
