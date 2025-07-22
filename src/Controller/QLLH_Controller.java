package Controller;

import DAO.QLLH_DAO;
import Entity.QLLH;

import java.util.List;

public class QLLH_Controller {

    private final QLLH_DAO dao = new QLLH_DAO();

    // ========================= DANH SÁCH =========================
    public List<QLLH> getDanhSachLop() {
        return dao.getAll();
    }

    public List<String> getAllMaGVCN() {
        return dao.getAllMaGVCN();
    }

    public List<String> getAllNamHoc() {
        return dao.getAllNamHoc();
    }

    // ✅ Sửa để lấy danh sách TenPhongHoc thay vì Ma_Phong_hoc
    public List<String> getAllTenPhongHoc() {
        return dao.getAllTenPhongHoc();
    }

    // ✅ Thêm phương thức chuyển đổi
    public String getMaPhongHocByTen(String tenPhongHoc) {
        return dao.getMaPhongHocByTen(tenPhongHoc);
    }

    public String getTenPhongHocByMa(String maPhongHoc) {
        return dao.getTenPhongHocByMa(maPhongHoc);
    }

    // ========================= THÊM =========================
    public boolean addLopHoc(QLLH lop) {
        return dao.insert(lop);
    }

    public String addLopHocWithMessage(QLLH lop) {
        if (dao.isMaLopTrungTrongNamHoc(lop.getMaLop(), lop.getNamHoc())) {
            return "⚠ Mã lớp đã tồn tại trong cùng niên khóa!";
        }
        if (dao.isTenLopTrungTrongNamHoc(lop.getTenLop(), lop.getNamHoc())) {
            return "⚠ Tên lớp đã tồn tại trong cùng niên khóa!";
        }
        if (dao.isPhongHocUsedForInsert(lop.getMaPhongHoc(), lop.getNamHoc())) {
            return "⚠ Phòng học đã được sử dụng cho lớp khác trong năm học!";
        }
        if (dao.isGVCNDaChuNhiemLopKhac(lop.getMaNguoiDung(), "", lop.getNamHoc())) {
            return "⚠ Giáo viên đã là GVCN của lớp khác trong năm học!";
        }
        if (dao.isVuotQuaSucChuaKhiThem(lop.getMaPhongHoc(), lop.getSiSo(), "", lop.getNamHoc())) {
            return "⚠ Tổng sĩ số trong phòng học vượt quá 40!";
        }

        boolean success = dao.insert(lop);
        return success ? "✅ Thêm lớp học thành công!" : "❌ Thêm lớp thất bại (lỗi không xác định)";
    }

    // ========================= CẬP NHẬT =========================
    public boolean updateLopHoc(QLLH lop) {
        return dao.update(lop);
    }

    public String updateLopHocWithMessage(QLLH lop) {
        if (dao.isPhongHocUsedForUpdate(lop.getMaPhongHoc(), lop.getNamHoc(), lop.getMaLop())) {
            return "⚠ Phòng học đã được sử dụng cho lớp khác trong năm học!";
        }
        if (dao.isVuotQuaSucChuaKhiThem(lop.getMaPhongHoc(), lop.getSiSo(), lop.getMaLop(), lop.getNamHoc())) {
            return "⚠ Tổng sĩ số trong phòng học vượt quá 40!";
        }

        boolean success = dao.update(lop);
        return success ? "✅ Cập nhật lớp học thành công!" : "❌ Cập nhật thất bại (lỗi không xác định)";
    }

    // ========================= XÓA =========================
    public boolean deleteLopHoc(String maLop) {
        return dao.delete(maLop);
    }

    // ========================= TỰ SINH =========================
    public String generateTenLop(String khoi, String namHoc) {
        return dao.autoGenerateTenLop(khoi, namHoc);
    }

    public String generateMaLop(String khoi, String namHoc) {
        return dao.autoGenerateMaLop(khoi, namHoc);
    }
}