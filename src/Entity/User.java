package Entity;

/**
 *
 * @author mphuc
 */
public class User {

    private String maNguoiDung;        // Ma_nguoi_dung
    private String tenNguoiDung;       // Ten_nguoi_dung
    private String matKhau;            // Mat_khau
    private String email;              // Email
    private String ngayThangNamSinh;   // Ngay_thang_nam_sinh
    private String soDienThoai;        // So_dien_thoai
    private String gioiTinh;           // Gioi_tinh
    private String trangThai;          // Trang_thai
    private String anhNguoiDung;       // Anh_nguoi_dung
    private String diaChi;             // dia_chi
    private String maVaiTro;           // Ma_vai_tro
    private String boMon;              // BoMon

    public User() {
    }

    public User(String maNguoiDung, String tenNguoiDung, String matKhau, String email) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.matKhau = matKhau;
        this.email = email;
    }

    public User(String maNguoiDung, String tenNguoiDung, String matKhau, String email,
            String ngayThangNamSinh, String soDienThoai, String gioiTinh, String trangThai,
            String anhNguoiDung, String diaChi, String maVaiTro, String boMon) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.matKhau = matKhau;
        this.email = email;
        this.ngayThangNamSinh = ngayThangNamSinh;
        this.soDienThoai = soDienThoai;
        this.gioiTinh = gioiTinh;
        this.trangThai = trangThai;
        this.anhNguoiDung = anhNguoiDung;
        this.diaChi = diaChi;
        this.maVaiTro = maVaiTro;
        this.boMon = boMon;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private User user = new User();

        public UserBuilder maNguoiDung(String maNguoiDung) {
            user.maNguoiDung = maNguoiDung;
            return this;
        }

        public UserBuilder tenNguoiDung(String tenNguoiDung) {
            user.tenNguoiDung = tenNguoiDung;
            return this;
        }

        public UserBuilder matKhau(String matKhau) {
            user.matKhau = matKhau;
            return this;
        }

        public UserBuilder email(String email) {
            user.email = email;
            return this;
        }

        public UserBuilder ngayThangNamSinh(String ngayThangNamSinh) {
            user.ngayThangNamSinh = ngayThangNamSinh;
            return this;
        }

        public UserBuilder soDienThoai(String soDienThoai) {
            user.soDienThoai = soDienThoai;
            return this;
        }

        public UserBuilder gioiTinh(String gioiTinh) {
            user.gioiTinh = gioiTinh;
            return this;
        }

        public UserBuilder trangThai(String trangThai) {
            user.trangThai = trangThai;
            return this;
        }

        public UserBuilder anhNguoiDung(String anhNguoiDung) {
            user.anhNguoiDung = anhNguoiDung;
            return this;
        }

        public UserBuilder diaChi(String diaChi) {
            user.diaChi = diaChi;
            return this;
        }

        public UserBuilder maVaiTro(String maVaiTro) {
            user.maVaiTro = maVaiTro;
            return this;
        }

        public UserBuilder boMon(String boMon) {
            user.boMon = boMon;
            return this;
        }

        public User build() {
            return user;
        }
    }

    // Utility methods
    public String getRole() {
        if (maVaiTro != null) {
            switch (maVaiTro.toUpperCase()) {
                case "GV":
                    return "Giảng Viên";
                case "SV":
                    return "Sinh Viên";
                case "ADMIN":
                    return "Quản Trị Viên";
                case "QL":
                    return "Quản Lý";
                default:
                    return maVaiTro;
            }
        }
        return "Chưa xác định";
    }

    public boolean isActive() {
        return "Hoạt động".equalsIgnoreCase(trangThai) || "1".equals(trangThai);
    }

    public String getGenderDisplay() {
        if (gioiTinh != null) {
            switch (gioiTinh) {
                case "1":
                    return "Nam";
                case "0":
                    return "Nữ";
                default:
                    return gioiTinh;
            }
        }
        return "Chưa xác định";
    }

    public String getStatusDisplay() {
        if (trangThai != null) {
            switch (trangThai) {
                case "1":
                    return "Hoạt động";
                case "0":
                    return "Ngưng hoạt động";
                default:
                    return trangThai;
            }
        }
        return "Chưa xác định";
    }

    // Getters and Setters
    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNgayThangNamSinh() {
        return ngayThangNamSinh;
    }

    public void setNgayThangNamSinh(String ngayThangNamSinh) {
        this.ngayThangNamSinh = ngayThangNamSinh;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getAnhNguoiDung() {
        return anhNguoiDung;
    }

    public void setAnhNguoiDung(String anhNguoiDung) {
        this.anhNguoiDung = anhNguoiDung;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getBoMon() {
        return boMon;
    }

    public void setBoMon(String boMon) {
        this.boMon = boMon;
    }

    // Legacy compatibility methods (để tương thích với code cũ)
    @Deprecated
    public String getUsername() {
        return maNguoiDung;
    }

    @Deprecated
    public void setUsername(String username) {
        this.maNguoiDung = username;
    }

    @Deprecated
    public String getPassword() {
        return matKhau;
    }

    @Deprecated
    public void setPassword(String password) {
        this.matKhau = password;
    }

    @Deprecated
    public String getFullname() {
        return tenNguoiDung;
    }

    @Deprecated
    public void setFullname(String fullname) {
        this.tenNguoiDung = fullname;
    }

    @Deprecated
    public String getPhoto() {
        return anhNguoiDung;
    }

    @Deprecated
    public void setPhoto(String photo) {
        this.anhNguoiDung = photo;
    }

    @Deprecated
    public String getDateOfBirth() {
        return ngayThangNamSinh;
    }

    @Deprecated
    public void setDateOfBirth(String dateOfBirth) {
        this.ngayThangNamSinh = dateOfBirth;
    }

    @Deprecated
    public String getPhone() {
        return soDienThoai;
    }

    @Deprecated
    public void setPhone(String phone) {
        this.soDienThoai = phone;
    }

    @Deprecated
    public String getAddress() {
        return diaChi;
    }

    @Deprecated
    public void setAddress(String address) {
        this.diaChi = address;
    }

    @Deprecated
    public boolean isEnabled() {
        return isActive();
    }

    @Deprecated
    public void setEnabled(boolean enabled) {
        this.trangThai = enabled ? "1" : "0";
    }

    @Deprecated
    public boolean isManager() {
        return "QL".equals(maVaiTro) || "ADMIN".equals(maVaiTro);
    }

    @Deprecated
    public void setManager(boolean manager) {
        this.maVaiTro = manager ? "QL" : "NV";
    }

    @Override
    public String toString() {
        return "User{" +
                "maNguoiDung='" + maNguoiDung + '\'' +
                ", tenNguoiDung='" + tenNguoiDung + '\'' +
                ", email='" + email + '\'' +
                ", gioiTinh='" + getGenderDisplay() + '\'' +
                ", trangThai='" + getStatusDisplay() + '\'' +
                ", maVaiTro='" + getRole() + '\'' +
                ", boMon='" + boMon + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return maNguoiDung != null ? maNguoiDung.equals(user.maNguoiDung) : user.maNguoiDung == null;
    }

    @Override
    public int hashCode() {
        return maNguoiDung != null ? maNguoiDung.hashCode() : 0;
    }
}