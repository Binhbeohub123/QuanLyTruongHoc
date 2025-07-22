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
    public String kiemTraThongTin(String tenCha, String tenMe, String sdtCha, String sdtMe) {
    try {
        // Check trùng tên cha mẹ
        if (kiemTraTrungTenChaMe(tenCha, tenMe)) {
            return "Tên cha và mẹ không được trùng nhau.";
        }

        // Nếu SDTCha có nhập thì kiểm tra đúng tên cha
        if (!sdtCha.isEmpty()) {
            boolean hopLe = dao.kiemTraSoDienThoaiPhuHuynh(sdtCha, tenCha);
            if (!hopLe) {
                return "Số điện thoại cha đã tồn tại nhưng không đúng tên cha tương ứng!";
            }
        }

        // Nếu SDTMe có nhập thì kiểm tra đúng tên mẹ
        if (!sdtMe.isEmpty()) {
            boolean hopLe = dao.kiemTraSoDienThoaiPhuHuynh(sdtMe, tenMe);
            if (!hopLe) {
                return "Số điện thoại mẹ đã tồn tại nhưng không đúng tên mẹ tương ứng!";
            }
        }

        return "Thông tin hợp lệ.";
    } catch (Exception e) {
        e.printStackTrace();
        return "Lỗi hệ thống: " + e.getMessage();
    }
}

public boolean kiemTraTrungTenChaMe(String tenCha, String tenMe) {
    return tenCha.trim().equalsIgnoreCase(tenMe.trim());
}
}
