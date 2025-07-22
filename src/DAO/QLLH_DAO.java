package DAO;

import Entity.QLLH;
import java.sql.*;
import java.util.*;
import util.XJdbc;

public class QLLH_DAO {

    public boolean insert(QLLH lop) {
        if (isMaLopTrungTrongNamHoc(lop.getMaLop(), lop.getNamHoc())) return false;
        if (isTenLopTrungTrongNamHoc(lop.getTenLop(), lop.getNamHoc())) return false;
        if (isPhongHocUsedForInsert(lop.getMaPhongHoc(), lop.getNamHoc())) return false;
        if (isGVCNDaChuNhiemLopKhac(lop.getMaNguoiDung(), "", lop.getNamHoc())) return false;
        if (isVuotQuaSucChuaKhiThem(lop.getMaPhongHoc(), lop.getSiSo(), "", lop.getNamHoc())) return false;

        String sql = "INSERT INTO LopHoc (MaLop, TenLop, Khoi, NienKhoa, SiSo, Ma_nguoi_dung, Ma_Phong_hoc) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getMaLop());
            ps.setString(2, lop.getTenLop());
            ps.setString(3, lop.getKhoi());
            ps.setString(4, lop.getNamHoc());
            ps.setInt(5, lop.getSiSo());
            ps.setString(6, lop.getMaNguoiDung());
            ps.setString(7, lop.getMaPhongHoc());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean update(QLLH lop) {
        if (!isCapNhatDuoc(lop)) return false;
        if (isPhongHocUsedForUpdate(lop.getMaPhongHoc(), lop.getNamHoc(), lop.getMaLop())) return false;
        if (isGVCNDaChuNhiemLopKhac(lop.getMaNguoiDung(), lop.getMaLop(), lop.getNamHoc())) return false;
        if (isVuotQuaSucChuaKhiThem(lop.getMaPhongHoc(), lop.getSiSo(), lop.getMaLop(), lop.getNamHoc())) return false;

        String sql = "UPDATE LopHoc SET TenLop=?, Khoi=?, NienKhoa=?, SiSo=?, Ma_nguoi_dung=?, Ma_Phong_hoc=? WHERE MaLop=?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getKhoi());
            ps.setString(3, lop.getNamHoc());
            ps.setInt(4, lop.getSiSo());
            ps.setString(5, lop.getMaNguoiDung());
            ps.setString(6, lop.getMaPhongHoc());
            ps.setString(7, lop.getMaLop());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean delete(String maLop) {
        String sql = "DELETE FROM LopHoc WHERE MaLop = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<QLLH> getAll() {
        List<QLLH> list = new ArrayList<>();
        // ✅ Sửa JOIN để lấy TenPhongHoc từ bảng PhongHoc
        String sql = "SELECT lh.*, ph.TenPhongHoc " +
                    "FROM LopHoc lh " +
                    "LEFT JOIN PhongHoc ph ON lh.Ma_Phong_hoc = ph.Ma_Phong_hoc";
        try (Connection conn = XJdbc.openConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new QLLH(
                    rs.getString("MaLop"),
                    rs.getString("TenLop"),
                    rs.getInt("SiSo"),
                    rs.getString("Khoi"),
                    rs.getString("Ma_nguoi_dung"),
                    rs.getString("NienKhoa"),
                    rs.getString("TenPhongHoc") != null ? rs.getString("TenPhongHoc") : rs.getString("Ma_Phong_hoc") // Ưu tiên TenPhongHoc, fallback về Ma_Phong_hoc
                ));
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    public boolean isMaLopTrungTrongNamHoc(String maLop, String namHoc) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE MaLop = ? AND NienKhoa = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maLop);
            ps.setString(2, namHoc);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isTenLopTrungTrongNamHoc(String tenLop, String namHoc) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE TenLop = ? AND NienKhoa = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenLop);
            ps.setString(2, namHoc);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isCapNhatDuoc(QLLH lop) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE TenLop = ? AND NienKhoa = ? AND MaLop <> ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, lop.getTenLop());
            ps.setString(2, lop.getNamHoc());
            ps.setString(3, lop.getMaLop());
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isPhongHocUsedForInsert(String maPhongHoc, String namHoc) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE Ma_Phong_hoc = ? AND NienKhoa = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhongHoc);
            ps.setString(2, namHoc);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isPhongHocUsedForUpdate(String maPhongHoc, String namHoc, String maLopHienTai) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE Ma_Phong_hoc = ? AND NienKhoa = ? AND MaLop <> ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhongHoc);
            ps.setString(2, namHoc);
            ps.setString(3, maLopHienTai);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isGVCNDaChuNhiemLopKhac(String maGVCN, String maLopHienTai, String nienKhoa) {
        String sql = "SELECT COUNT(*) FROM LopHoc WHERE Ma_nguoi_dung = ? AND MaLop <> ? AND NienKhoa = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGVCN);
            ps.setString(2, maLopHienTai);
            ps.setString(3, nienKhoa);
            ResultSet rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean isVuotQuaSucChuaKhiThem(String maPhongHoc, int siSoThem, String maLopBoQua, String nienKhoa) {
        String sql = "SELECT ISNULL(SUM(SiSo), 0) AS Tong FROM LopHoc WHERE Ma_Phong_hoc = ? AND NienKhoa = ? AND MaLop <> ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhongHoc);
            ps.setString(2, nienKhoa);
            ps.setString(3, maLopBoQua);
            ResultSet rs = ps.executeQuery();
            return rs.next() && (rs.getInt("Tong") + siSoThem > 40);
        } catch (SQLException e) {
            return false;
        }
    }

    public List<String> getAllMaGVCN() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT Ma_nguoi_dung FROM NguoiDung WHERE Ma_vai_tro = 'GVCN'";
        try (Connection conn = XJdbc.openConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("Ma_nguoi_dung"));
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    public List<String> getAllNamHoc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT MoTa FROM NamHoc ORDER BY MoTa DESC";
        try (Connection conn = XJdbc.openConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("MoTa"));
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    // ✅ Sửa để lấy TenPhongHoc thay vì Ma_Phong_hoc
    public List<String> getAllTenPhongHoc() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT TenPhongHoc FROM PhongHoc ORDER BY TenPhongHoc";
        try (Connection conn = XJdbc.openConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("TenPhongHoc"));
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    // ✅ Thêm phương thức để lấy Ma_Phong_hoc từ TenPhongHoc
    public String getMaPhongHocByTen(String tenPhongHoc) {
        String sql = "SELECT Ma_Phong_hoc FROM PhongHoc WHERE TenPhongHoc = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenPhongHoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("Ma_Phong_hoc");
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    // ✅ Thêm phương thức để lấy TenPhongHoc từ Ma_Phong_hoc
    public String getTenPhongHocByMa(String maPhongHoc) {
        String sql = "SELECT TenPhongHoc FROM PhongHoc WHERE Ma_Phong_hoc = ?";
        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhongHoc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("TenPhongHoc");
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public String autoGenerateTenLop(String khoi, String namHoc) {
        String sql = "SELECT TenLop FROM LopHoc WHERE Khoi = ? AND NienKhoa = ?";
        List<String> existing = new ArrayList<>();

        try (Connection conn = XJdbc.openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, khoi);
            ps.setString(2, namHoc);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                existing.add(rs.getString("TenLop"));
            }
        } catch (SQLException e) {
            return null;
        }

        for (int i = 1; i <= 99; i++) {
            String tenLop = khoi + "A" + i;
            if (!existing.contains(tenLop)) return tenLop;
        }

        return null;
    }

    // ❗ Không gọi hàm này nữa nếu không muốn tự sinh mã lớp
    public String autoGenerateMaLop(String khoi, String namHoc) {
        List<QLLH> allLop = getAll();
        int max = 0;
        for (QLLH lh : allLop) {
            String ma = lh.getMaLop();
            if (ma.startsWith("LH")) {
                try {
                    int so = Integer.parseInt(ma.substring(2));
                    if (so > max) max = so;
                } catch (Exception ignored) {}
            }
        }
        return String.format("LH%02d", max + 1);
    }
}