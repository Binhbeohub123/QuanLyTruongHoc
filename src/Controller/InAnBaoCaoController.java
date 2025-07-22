package Controller;

import Dao.InAnBaoCaoDAO;
import java.util.List;
import java.util.Map;

public class InAnBaoCaoController {
    private final InAnBaoCaoDAO dao;

    public InAnBaoCaoController() {
        this.dao = new InAnBaoCaoDAO();
    }

    // Các phương thức không phục vụ load combobox, giữ nguyên tên hàm DAO để View dễ chuyển đổi

    public List<Object[]> loadHocSinhTheoLop(String maLop) {
        return dao.loadHocSinhTheoLop(maLop);
    }

    public List<Object[]> loadBangDiemTheoDanhSachHocSinh(List<String> dsMaHocSinh, String maLop, String hocKy) {
        return dao.loadBangDiemTheoDanhSachHocSinh(dsMaHocSinh, maLop, hocKy);
    }

    public List<Object[]> loadBangDiemCaNhan(String maHocSinh, String hocKy) {
        return dao.loadBangDiemCaNhan(maHocSinh, hocKy);
    }

    public List<Object[]> loadSiSoLop(String maLop) {
        return dao.loadSiSoLop(maLop);
    }

    public List<Object[]> loadTatCaCoSoVatChat() {
        return dao.loadTatCaCoSoVatChat();
    }

    public List<Object[]> getCSVCTheoPhong(String maPhong) {
        return dao.getCSVCTheoPhong(maPhong);
    }

    public String getMaPhongHocTheoLopVaNienKhoa(String maLop, String nienKhoa) {
        return dao.getMaPhongHocTheoLopVaNienKhoa(maLop, nienKhoa);
    }
}
