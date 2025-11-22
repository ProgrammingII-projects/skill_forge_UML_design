
import javax.swing.SwingUtilities;

import view.*;
import view.Admin.AdminDashboardFrame;


public class test {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            AdminDashboardFrame AdminDashboardFrame = new AdminDashboardFrame();
            AdminDashboardFrame.setVisible(true);
        });

    }
}
