package Util;

import Entity.User;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.XJdbc;

/**
 * Lớp tiện ích hỗ trợ truy vấn và chuyển đổi sang đối tượng
 *
 * @author NghiemN
 * @version 2.0 - Updated for new database schema
 */
public class XQuery {

    // Mapping giữa method names và column names cho database mới
    private static final Map<String, String> COLUMN_MAPPING = new HashMap<>();
    
    static {
        // Mapping cho User entity
        COLUMN_MAPPING.put("setMaNguoiDung", "Ma_nguoi_dung");
        COLUMN_MAPPING.put("setTenNguoiDung", "Ten_nguoi_dung");
        COLUMN_MAPPING.put("setMatKhau", "Mat_khau");
        COLUMN_MAPPING.put("setEmail", "Email");
        COLUMN_MAPPING.put("setNgayThangNamSinh", "Ngay_thang_nam_sinh");
        COLUMN_MAPPING.put("setSoDienThoai", "So_dien_thoai");
        COLUMN_MAPPING.put("setGioiTinh", "Gioi_tinh");
        COLUMN_MAPPING.put("setTrangThai", "Trang_thai");
        COLUMN_MAPPING.put("setAnhNguoiDung", "Anh_nguoi_dung");
        COLUMN_MAPPING.put("setDiaChi", "dia_chi");
        COLUMN_MAPPING.put("setMaVaiTro", "Ma_vai_tro");
        COLUMN_MAPPING.put("setBoMon", "BoMon");
        
        // Legacy compatibility mapping
        COLUMN_MAPPING.put("setUsername", "Ma_nguoi_dung");
        COLUMN_MAPPING.put("setPassword", "Mat_khau");
        COLUMN_MAPPING.put("setFullname", "Ten_nguoi_dung");
        COLUMN_MAPPING.put("setPhone", "So_dien_thoai");
        COLUMN_MAPPING.put("setDateOfBirth", "Ngay_thang_nam_sinh");
        COLUMN_MAPPING.put("setPhoto", "Anh_nguoi_dung");
        COLUMN_MAPPING.put("setAddress", "dia_chi");
        COLUMN_MAPPING.put("setEnabled", "Trang_thai");
        COLUMN_MAPPING.put("setManager", "Ma_vai_tro");
    }

    /**
     * Truy vấn 1 đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> B getSingleBean(Class<B> beanClass, String sql, Object... values) {
        List<B> list = XQuery.getBeanList(beanClass, sql, values);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Truy vấn nhiều đối tượng
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql câu lệnh truy vấn
     * @param values các giá trị cung cấp cho các tham số của SQL
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    public static <B> List<B> getBeanList(Class<B> beanClass, String sql, Object... values) {
        List<B> list = new ArrayList<>();
        try {
            ResultSet resultSet = XJdbc.executeQuery(sql, values);
            while (resultSet.next()) {
                list.add(XQuery.readBean(resultSet, beanClass));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }

    /**
     * Tạo bean với dữ liệu đọc từ bản ghi hiện tại
     *
     * @param <B> kiểu của đối tượng cần chuyển đổi
     * @param resultSet tập bản ghi cung cấp dữ liệu
     * @param beanClass lớp của đối tượng kết quả
     * @return kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     */
    private static <B> B readBean(ResultSet resultSet, Class<B> beanClass) throws Exception {
        B bean = beanClass.getDeclaredConstructor().newInstance();
        Method[] methods = beanClass.getDeclaredMethods();
        
        for (Method method : methods) {
            String methodName = method.getName();
            if (methodName.startsWith("set") && method.getParameterCount() == 1) {
                try {
                    String columnName = getColumnName(methodName);
                    if (columnName != null) {
                        Object value = getColumnValue(resultSet, columnName, method.getParameterTypes()[0]);
                        if (value != null) {
                            method.invoke(bean, value);
                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SQLException e) {
                    System.out.printf("+ Error setting value for method '%s': %s\r\n", methodName, e.getMessage());
                }
            }
        }
        return bean;
    }

    /**
     * Lấy tên column từ method name
     */
    private static String getColumnName(String methodName) {
        // Kiểm tra trong mapping trước
        if (COLUMN_MAPPING.containsKey(methodName)) {
            return COLUMN_MAPPING.get(methodName);
        }
        
        // Fallback: bỏ "set" và sử dụng tên còn lại
        if (methodName.startsWith("set") && methodName.length() > 3) {
            return methodName.substring(3);
        }
        
        return null;
    }

    /**
     * Lấy giá trị từ ResultSet với type conversion
     */
    private static Object getColumnValue(ResultSet resultSet, String columnName, Class<?> targetType) throws SQLException {
        Object value = resultSet.getObject(columnName);
        
        if (value == null) {
            return null;
        }

        // Type conversion cho các trường hợp đặc biệt
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof String) {
                String strValue = (String) value;
                return "1".equals(strValue) || "true".equalsIgnoreCase(strValue) || 
                       "Hoạt động".equalsIgnoreCase(strValue) || "Active".equalsIgnoreCase(strValue);
            } else if (value instanceof Number) {
                return ((Number) value).intValue() == 1;
            }
        }
        
        if (targetType == String.class && !(value instanceof String)) {
            return value.toString();
        }

        return value;
    }

    /**
     * Thêm mapping cho column name tùy chỉnh
     */
    public static void addColumnMapping(String methodName, String columnName) {
        COLUMN_MAPPING.put(methodName, columnName);
    }

    /**
     * Xóa mapping cho method name
     */
    public static void removeColumnMapping(String methodName) {
        COLUMN_MAPPING.remove(methodName);
    }

    /**
     * Lấy tất cả mapping hiện tại
     */
    public static Map<String, String> getAllMappings() {
        return new HashMap<>(COLUMN_MAPPING);
    }

    /**
     * Truy vấn số lượng bản ghi
     */
    public static int getRowCount(String sql, Object... values) {
        try {
            ResultSet resultSet = XJdbc.executeQuery(sql, values);
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return 0;
    }

    /**
     * Kiểm tra xem có bản ghi nào tồn tại không
     */
    public static boolean exists(String sql, Object... values) {
        return getRowCount(sql, values) > 0;
    }

    // Demo methods với database schema mới
    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
        demo4();
    }

    private static void demo1() {
        System.out.println("=== Demo 1: Tìm user theo mã và mật khẩu ===");
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ma_nguoi_dung=? AND Mat_khau=?";
        User user = XQuery.getSingleBean(User.class, sql, "GV001", "123456");
        if (user != null) {
            System.out.println("Found user: " + user);
        } else {
            System.out.println("User not found");
        }
    }

    private static void demo2() {
        System.out.println("\n=== Demo 2: Tìm user theo tên ===");
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ten_nguoi_dung LIKE ?";
        List<User> list = XQuery.getBeanList(User.class, sql, "%Nguyễn%");
        System.out.println("Found " + list.size() + " users");
        for (User user : list) {
            System.out.println("- " + user.getTenNguoiDung());
        }
    }

    private static void demo3() {
        System.out.println("\n=== Demo 3: Tìm user theo vai trò ===");
        String sql = "SELECT * FROM [dbo].[NguoiDung] WHERE Ma_vai_tro=?";
        List<User> list = XQuery.getBeanList(User.class, sql, "GV");
        System.out.println("Found " + list.size() + " giảng viên");
    }

    private static void demo4() {
        System.out.println("\n=== Demo 4: Đếm số user ===");
        String sql = "SELECT COUNT(*) FROM [dbo].[NguoiDung] WHERE Trang_thai=?";
        int count = XQuery.getRowCount(sql, "1");
        System.out.println("Số user đang hoạt động: " + count);
    }
}