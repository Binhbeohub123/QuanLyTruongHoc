package DAO;

import Entity.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import util.XJdbc;

/**
 *
 * @author mphuc
 */
public class UsersDAOImpl implements UserDAO {

    @Override
    public User findById(String maNguoiDung) {
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ma_nguoi_dung = ?";
        try {
            ResultSet rs = XJdbc.executeQuery(sql, maNguoiDung);
            if (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void insert(User entity) {
        String sql = "INSERT INTO [dbo].[NguoiDung](Ma_nguoi_dung, Ten_nguoi_dung, Mat_khau, Email, Ngay_thang_nam_sinh, So_dien_thoai, Gioi_tinh, Trang_thai, Anh_nguoi_dung, dia_chi, Ma_vai_tro, BoMon) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        XJdbc.executeUpdate(sql,
                entity.getMaNguoiDung(),
                entity.getTenNguoiDung(),
                entity.getMatKhau(),
                entity.getEmail(),
                entity.getNgayThangNamSinh(),
                entity.getSoDienThoai(),
                entity.getGioiTinh(),
                entity.getTrangThai(),
                entity.getAnhNguoiDung(),
                entity.getDiaChi(),
                entity.getMaVaiTro(),
                entity.getBoMon()
        );
    }

    @Override
    public void update(User entity) {
        String sql = "UPDATE [dbo].[NguoiDung] SET Ten_nguoi_dung=?, Mat_khau=?, Email=?, Ngay_thang_nam_sinh=?, So_dien_thoai=?, Gioi_tinh=?, Trang_thai=?, Anh_nguoi_dung=?, dia_chi=?, Ma_vai_tro=?, BoMon=? WHERE Ma_nguoi_dung=?";
        XJdbc.executeUpdate(sql,
                entity.getTenNguoiDung(),
                entity.getMatKhau(),
                entity.getEmail(),
                entity.getNgayThangNamSinh(),
                entity.getSoDienThoai(),
                entity.getGioiTinh(),
                entity.getTrangThai(),
                entity.getAnhNguoiDung(),
                entity.getDiaChi(),
                entity.getMaVaiTro(),
                entity.getBoMon(),
                entity.getMaNguoiDung()
        );
    }

    @Override
    public void delete(String maNguoiDung) {
        String sql = "DELETE FROM [dbo].[NguoiDung] WHERE Ma_nguoi_dung=?";
        XJdbc.executeUpdate(sql, maNguoiDung);
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM [dbo].[NguoiDung] ORDER BY Ma_nguoi_dung";
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql);
            while (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<User> selectByRole(String maVaiTro) {
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ma_vai_tro = ? ORDER BY Ma_nguoi_dung";
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, maVaiTro);
            while (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<User> selectByKeyword(String keyword) {
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ten_nguoi_dung LIKE ? OR Ma_nguoi_dung LIKE ? ORDER BY Ma_nguoi_dung";
        List<User> list = new ArrayList<>();
        try {
            String pattern = "%" + keyword + "%";
            ResultSet rs = XJdbc.executeQuery(sql, pattern, pattern);
            while (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<User> selectByStatus(String trangThai) {
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Trang_thai = ? ORDER BY Ma_nguoi_dung";
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, trangThai);
            while (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<User> selectByDepartment(String boMon) {
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE BoMon = ? ORDER BY Ma_nguoi_dung";
        List<User> list = new ArrayList<>();
        try {
            ResultSet rs = XJdbc.executeQuery(sql, boMon);
            while (rs.next()) {
                User user = new User();
                user.setMaNguoiDung(rs.getString("Ma_nguoi_dung"));
                user.setTenNguoiDung(rs.getString("Ten_nguoi_dung"));
                user.setMatKhau(rs.getString("Mat_khau"));
                user.setEmail(rs.getString("Email"));
                user.setNgayThangNamSinh(rs.getString("Ngay_thang_nam_sinh"));
                user.setSoDienThoai(rs.getString("So_dien_thoai"));
                user.setGioiTinh(rs.getString("Gioi_tinh"));
                user.setTrangThai(rs.getString("Trang_thai"));
                user.setAnhNguoiDung(rs.getString("Anh_nguoi_dung"));
                user.setDiaChi(rs.getString("dia_chi"));
                user.setMaVaiTro(rs.getString("Ma_vai_tro"));
                user.setBoMon(rs.getString("BoMon"));
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}