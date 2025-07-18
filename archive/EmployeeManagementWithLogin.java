import javax.swing.*;

public class EmployeeManagementWithLogin {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Simple login dialog with hidden password
            String username = JOptionPane.showInputDialog(null, "Enter username:");
            if (username == null || username.trim().isEmpty()) {
                System.exit(0);
            }
            
            // Use JPasswordField for hidden password input
            JPasswordField passwordField = new JPasswordField();
            int option = JOptionPane.showConfirmDialog(null, passwordField, "Enter password:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            
            if (option != JOptionPane.OK_OPTION) {
                System.exit(0);
            }
            
            String password = new String(passwordField.getPassword());
            if (password.trim().isEmpty()) {
                System.exit(0);
            }
            
            // Simple validation - check against admin/admin123
            if ("admin".equals(username.trim()) && "admin123".equals(password.trim())) {
                JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Launch the existing Employee Management Frame
                try {
                    // Run the original main method
                    java.lang.reflect.Method method = Class.forName("com.motorph.employeeapp.gui.EmployeeManagementFrame")
                        .getMethod("main", String[].class);
                    method.invoke(null, (Object) new String[]{});
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error launching Employee Management: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        });
    }
}
