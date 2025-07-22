package Controller;

import Entity.User;
import java.util.List;

/**
 * @author mphuc
 */
public interface UserController {
    
    // Quản lý cơ bản
    void create();
    void edit();
    void delete();
    void clear();
    void save();
    
    // Hiển thị và tải dữ liệu
    void load();
    void refresh();
    void fillToTable();
    void fillToForm();
    
    // Tìm kiếm và lọc
    void search();
    void searchByKeyword(String keyword);
    void filterByRole(String maVaiTro);
    void filterByStatus(String trangThai);
    void filterByDepartment(String boMon);
    void filterByGender(String gioiTinh);
    
    // Navigation
    void first();
    void previous();
    void next();
    void last();
    
    // Validation
    boolean validateForm();
    boolean validateUsername(String maNguoiDung);
    boolean validateEmail(String email);
    boolean validatePhone(String soDienThoai);
    
    // Utility methods
    void selectTableRow(int index);
    User getFormData();
    void setFormData(User user);
    void clearForm();
    
    // Export/Import
    void exportToExcel();
    void importFromExcel();
    void printUserList();
    
    // User management specific
    void changePassword();
    void resetUserPassword(String maNguoiDung);
    void activateUser(String maNguoiDung);
    void deactivateUser(String maNguoiDung);
    void assignRole(String maNguoiDung, String maVaiTro);
    
    // Statistics
    void showUserStatistics();
    List<User> getUsersByRole(String maVaiTro);
    List<User> getActiveUsers();
    List<User> getInactiveUsers();
    int getTotalUsers();
    int getUserCountByDepartment(String boMon);
    
    // Photo management
    void uploadPhoto();
    void removePhoto();
    void viewPhoto();
    
    // Advanced search
    void advancedSearch();
    void searchByDateRange(String fromDate, String toDate);
    void searchByAge(int minAge, int maxAge);
    
    // Default implementations
    default void showMessage(String message) {
        // Hiển thị thông báo
        System.out.println("Message: " + message);
    }
    
    default void showError(String error) {
        // Hiển thị lỗi
        System.err.println("Error: " + error);
    }
    
    default void showSuccess(String message) {
        // Hiển thị thành công
        System.out.println("Success: " + message);
    }
    
    default boolean confirmDelete(String maNguoiDung) {
        // Xác nhận xóa user
        return false;
    }
    
    default void logUserAction(String action, String maNguoiDung) {
        // Ghi log hành động
        System.out.println("Action: " + action + " for user: " + maNguoiDung);
    }
    
    default boolean isValidEmail(String email) {
        // Kiểm tra email hợp lệ
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    default boolean isValidPhone(String phone) {
        // Kiểm tra số điện thoại hợp lệ (Việt Nam)
        return phone != null && phone.matches("^(0|\\+84)[3-9][0-9]{8}$");
    }
    
    default String generateUsername(String tenNguoiDung) {
        // Tạo username từ tên người dùng
        if (tenNguoiDung == null || tenNguoiDung.trim().isEmpty()) {
            return "";
        }
        return tenNguoiDung.toLowerCase()
                          .replaceAll("\\s+", "")
                          .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                          .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                          .replaceAll("[ìíịỉĩ]", "i")
                          .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                          .replaceAll("[ùúụủũưừứựửữ]", "u")
                          .replaceAll("[ỳýỵỷỹ]", "y")
                          .replaceAll("[đ]", "d")
                          .replaceAll("[^a-z0-9]", "");
    }
}