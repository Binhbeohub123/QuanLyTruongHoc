package Dao;

import Model.HocSinh;
import connect.XJdbc;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HocSinhDAO {
    private Connection conn;
     
    public HocSinhDAO() {
        this.conn = XJdbc.openConnection();
    }

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
                rs.getString("TenCha"),
                rs.getString("SDTCha"),
                rs.getString("TenMe"),
                rs.getString("SDTMe")
            );
            list.add(hs);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public boolean insert(HocSinh hs) {
    String sql = "INSERT INTO HocSinh (MaHocSinh, HoTen, NgaySinh, GioiTinh, Trang_thai, MaLop, Anh, TenCha, SDTCha, TenMe, SDTMe) "
               + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, hs.getMaHocSinh());
        ps.setString(2, hs.getHoTen());
        ps.setDate(3, new java.sql.Date(hs.getNgaySinh().getTime()));
        ps.setBoolean(4, hs.isGioiTinh());
        ps.setString(5, hs.getTrangThai());
        ps.setString(7, hs.getMaLop());
        ps.setString(8, hs.getAnhnguoidung());
        ps.setString(9, hs.getTenCha());
        ps.setString(10, hs.getSdtCha());
        ps.setString(11, hs.getTenMe());
        ps.setString(12, hs.getSdtMe());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean update(HocSinh hs) {
    String sql = "UPDATE HocSinh SET HoTen=?, NgaySinh=?, GioiTinh=?, Trang_thai=?, MaLop=?, Anh=?, "
               + "TenCha=?, SDTCha=?, TenMe=?, SDTMe=? WHERE MaHocSinh=?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, hs.getHoTen());
        ps.setDate(2, new java.sql.Date(hs.getNgaySinh().getTime()));
        ps.setBoolean(3, hs.isGioiTinh());
        ps.setString(4, hs.getTrangThai());
        ps.setString(6, hs.getMaLop());
        ps.setString(7, hs.getAnhnguoidung());
        ps.setString(8, hs.getTenCha());
        ps.setString(9, hs.getSdtCha());
        ps.setString(10, hs.getTenMe());
        ps.setString(11, hs.getSdtMe());
        ps.setString(12, hs.getMaHocSinh());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean delete(String maHocSinh) {
    String sqlDeleteScores = "DELETE FROM DiemSo WHERE MaHocSinh = ?";
    String sqlDeleteHS     = "DELETE FROM HocSinh WHERE MaHocSinh = ?";

    try {
        conn.setAutoCommit(false);  // bắt đầu transaction

        // 1. Xóa điểm liên quan
        try (PreparedStatement ps1 = conn.prepareStatement(sqlDeleteScores)) {
            ps1.setString(1, maHocSinh);
            ps1.executeUpdate();
        }

        // 2. Xóa bản ghi HocSinh
        int affected;
        try (PreparedStatement ps2 = conn.prepareStatement(sqlDeleteHS)) {
            ps2.setString(1, maHocSinh);
            affected = ps2.executeUpdate();
        }

        conn.commit();  // nếu cả hai bước thành công thì commit
        return affected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        try {
            conn.rollback();  // rollback khi có lỗi
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    } finally {
        try {
            conn.setAutoCommit(true);
        } catch (SQLException ignored) {}
    }
}
    public List<HocSinh> search(String keyword) {
    List<HocSinh> list = new ArrayList<>();
    String sql = "SELECT * FROM HocSinh WHERE MaHocSinh LIKE ? OR HoTen LIKE ? OR MaLop LIKE ?";
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
                    rs.getString("TenCha"),
                    rs.getString("SDTCha"),
                    rs.getString("TenMe"),
                    rs.getString("SDTMe")
                );
                list.add(hs);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
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

                // Bỏ qua từ HS001 đến HS009 => bắt đầu từ HS10 trở lên
                if (next < 10) {
                    next = 10;
                }

                return prefix + next;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Nếu chưa có mã nào trong DB, bắt đầu từ HS10 luôn
    return prefix + "10";
}
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
}

