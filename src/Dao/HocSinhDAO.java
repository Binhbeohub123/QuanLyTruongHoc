package Dao;

import Model.HocSinh;
import connect.XJdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HocSinhDAO {
    private Connection conn;

    public HocSinhDAO() {
        this.conn = XJdbc.openConnection();
    }

    // 1. SELECT ALL JOIN PhuHuynh
    public List<HocSinh> selectAll() {
        List<HocSinh> list = new ArrayList<>();
        String sql = "SELECT hs.MaHocSinh, hs.HoTen, hs.NgaySinh, hs.GioiTinh, hs.Trang_thai, hs.MaLop, hs.Anh, " +
                     "ph.TenCha, ph.SDTCha, ph.TenMe, ph.SDTMe " +
                     "FROM HocSinh hs " +
                     "LEFT JOIN PhuHuynh ph ON hs.MaHocSinh = ph.MaHocSinh";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                HocSinh hs = new HocSinh(
                    rs.getString("MaHocSinh"),
                    rs.getString("HoTen"),
                    rs.getDate("NgaySinh"),
                    rs.getBoolean("GioiTinh"),
                    rs.getString("Trang_thai"),
                    rs.getString("MaLop"),
                    rs.getString("Anh"),
                    rs.getString("TenCha") != null ? rs.getString("TenCha") : "",
                    rs.getString("SDTCha") != null ? rs.getString("SDTCha") : "",
                    rs.getString("TenMe") != null ? rs.getString("TenMe") : "",
                    rs.getString("SDTMe") != null ? rs.getString("SDTMe") : ""
                );
                list.add(hs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. INSERT vào 2 bảng
    public boolean insert(HocSinh hs) {
        String sqlHS = "INSERT INTO HocSinh (MaHocSinh, HoTen, NgaySinh, GioiTinh, Trang_thai, MaLop, Anh) " +
                       "VALUES (?,?,?,?,?,?,?)";
        String sqlPH = "INSERT INTO PhuHuynh (MaPhuHuynh, MaHocSinh, TenCha, SDTCha, TenMe, SDTMe) " +
                       "VALUES (?,?,?,?,?,?)";
        String maPhuHuynh = "PH_" + hs.getMaHocSinh();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlHS)) {
                ps1.setString(1, hs.getMaHocSinh());
                ps1.setString(2, hs.getHoTen());
                ps1.setDate(3, new java.sql.Date(hs.getNgaySinh().getTime()));
                ps1.setBoolean(4, hs.isGioiTinh());
                ps1.setString(5, hs.getTrangThai());
                ps1.setString(6, hs.getMaLop());
                ps1.setString(7, hs.getAnhnguoidung());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlPH)) {
                ps2.setString(1, maPhuHuynh);
                ps2.setString(2, hs.getMaHocSinh());
                ps2.setString(3, hs.getTenCha());
                ps2.setString(4, hs.getSdtCha());
                ps2.setString(5, hs.getTenMe());
                ps2.setString(6, hs.getSdtMe());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        return false;
    }

    // 3. UPDATE 2 bảng
    public boolean update(HocSinh hs) {
        String sqlHS = "UPDATE HocSinh SET HoTen=?, NgaySinh=?, GioiTinh=?, Trang_thai=?, MaLop=?, Anh=? WHERE MaHocSinh=?";
        String sqlPH = "UPDATE PhuHuynh SET TenCha=?, SDTCha=?, TenMe=?, SDTMe=? WHERE MaHocSinh=?";
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlHS)) {
                ps1.setString(1, hs.getHoTen());
                ps1.setDate(2, new java.sql.Date(hs.getNgaySinh().getTime()));
                ps1.setBoolean(3, hs.isGioiTinh());
                ps1.setString(4, hs.getTrangThai());
                ps1.setString(5, hs.getMaLop());
                ps1.setString(6, hs.getAnhnguoidung());
                ps1.setString(7, hs.getMaHocSinh());
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlPH)) {
                ps2.setString(1, hs.getTenCha());
                ps2.setString(2, hs.getSdtCha());
                ps2.setString(3, hs.getTenMe());
                ps2.setString(4, hs.getSdtMe());
                ps2.setString(5, hs.getMaHocSinh());
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        return false;
    }

    // 4. DELETE 2 bảng
    public boolean delete(String maHocSinh) {
        String sqlDeletePH = "DELETE FROM PhuHuynh WHERE MaHocSinh = ?";
        String sqlDeleteScores = "DELETE FROM DiemSo WHERE MaHocSinh = ?";
        String sqlDeleteHS = "DELETE FROM HocSinh WHERE MaHocSinh = ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlDeletePH)) {
                ps1.setString(1, maHocSinh);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteScores)) {
                ps2.setString(1, maHocSinh);
                ps2.executeUpdate();
            }

            try (PreparedStatement ps3 = conn.prepareStatement(sqlDeleteHS)) {
                ps3.setString(1, maHocSinh);
                ps3.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
        }
        return false;
    }

    // 5. SEARCH JOIN PhuHuynh
    public List<HocSinh> search(String keyword) {
        List<HocSinh> list = new ArrayList<>();
        String sql = "SELECT hs.MaHocSinh, hs.HoTen, hs.NgaySinh, hs.GioiTinh, hs.Trang_thai, hs.MaLop, hs.Anh, " +
                     "ph.TenCha, ph.SDTCha, ph.TenMe, ph.SDTMe " +
                     "FROM HocSinh hs " +
                     "LEFT JOIN PhuHuynh ph ON hs.MaHocSinh = ph.MaHocSinh " +
                     "WHERE hs.MaHocSinh LIKE ? OR hs.HoTen LIKE ? OR hs.MaLop LIKE ?";
        String like = "%" + keyword + "%";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HocSinh hs = new HocSinh(
                        rs.getString("MaHocSinh"),
                        rs.getString("HoTen"),
                        rs.getDate("NgaySinh"),
                        rs.getBoolean("GioiTinh"),
                        rs.getString("Trang_thai"),
                        rs.getString("MaLop"),
                        rs.getString("Anh"),
                        rs.getString("TenCha") != null ? rs.getString("TenCha") : "",
                        rs.getString("SDTCha") != null ? rs.getString("SDTCha") : "",
                        rs.getString("TenMe") != null ? rs.getString("TenMe") : "",
                        rs.getString("SDTMe") != null ? rs.getString("SDTMe") : ""
                    );
                    list.add(hs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 6. Lấy danh sách lớp
    public List<String> selectAllMaLop() {
        List<String> ds = new ArrayList<>();
        String sql = "SELECT MaLop FROM LopHoc";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(rs.getString("MaLop"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 7. Sinh mã học sinh mới
    public String sinhMaHocSinhMoi() {
        String prefix = "HS";
        String sql = "SELECT TOP 1 MaHocSinh FROM HocSinh WHERE MaHocSinh LIKE ? " +
                     "ORDER BY CONVERT(INT, SUBSTRING(MaHocSinh, 3, LEN(MaHocSinh))) DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prefix + "%");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String last = rs.getString("MaHocSinh");
                    int num = Integer.parseInt(last.substring(prefix.length()));
                    int next = num + 1;
                    if (next < 10) {
                        next = 10;
                    }
                    return prefix + next;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefix + "10";
    }

    // 8. Kiểm tra trùng tên
    public boolean checkTrungTen(String ten) {
        String sql = "SELECT COUNT(*) FROM HocSinh WHERE HoTen = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ten);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkTrungTenKhiCapNhat(String ten, String maHS) {
        String sql = "SELECT COUNT(*) FROM HocSinh WHERE HoTen = ? AND MaHocSinh <> ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ten);
            ps.setString(2, maHS);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean kiemTraSoDienThoaiPhuHuynh(String sdt, String tenNhap) {
    String sql = "SELECT TenCha, SDTCha, TenMe, SDTMe FROM PhuHuynh WHERE SDTCha = ? OR SDTMe = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, sdt);
        ps.setString(2, sdt);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String tenChaDB = rs.getString("TenCha").trim().toLowerCase();
            String sdtChaDB = rs.getString("SDTCha").trim();
            String tenMeDB = rs.getString("TenMe").trim().toLowerCase();
            String sdtMeDB = rs.getString("SDTMe").trim();

            if (sdt.equals(sdtChaDB)) {
                return tenNhap.trim().toLowerCase().equals(tenChaDB);
            }

            if (sdt.equals(sdtMeDB)) {
                return tenNhap.trim().toLowerCase().equals(tenMeDB);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Có lỗi xảy ra thì return false để tránh nhập sai
    }
    return true; // Nếu SDT chưa tồn tại trong DB thì coi như hợp lệ
}
}
