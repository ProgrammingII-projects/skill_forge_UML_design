
import javax.swing.SwingUtilities;

import view.*;


public class test {
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            AdminDashboardFrame AdminDashboardFrame = new AdminDashboardFrame();
            AdminDashboardFrame.setVisible(true);
        });

    }
}
