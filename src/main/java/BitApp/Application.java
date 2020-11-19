package BitApp;

import BitApp.model.ByteWritter;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class Application extends JFrame {
    private JTextField toFindSequ;
    private JTextField toReplaceSequ;
    private JTextField path;
    private JTextField extension;
    private JButton executeButton;
    private JLabel pathDescription;
    private JLabel extensionDescription;
    private JLabel toFindSequDescription;
    private JLabel toReplaceSequDescription;
    private JLabel greetingsLabel;
    private JPanel panel;

    public Application(String title) {
        super("BitApp");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(panel);
        this.pack();
    }

    public Application() {
        executeButton.addActionListener(new ActionListener() {
            ByteWritter byteWritter = new ByteWritter();

            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                String pathString = path.getText();
                String extensionString = extension.getText();
                String toFindString = toFindSequ.getText();
                String toReplaceString = toReplaceSequ.getText();
                if (pathString.isEmpty() || extensionString.isEmpty() || toFindString.isEmpty() || toReplaceString.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Jedno lub więcej pól jest puste. Proszę sprawdzić wprowadzone dane.");
                }
                if (!pathString.contains(":") || !pathString.contains("\\")) {
                    JOptionPane.showMessageDialog(null, "Scieżka dostępu jest niepoprawna. Proszę sprawdzić wprowadzone dane.");
                }
                if (toFindString.equals(toReplaceString)) {
                    JOptionPane.showMessageDialog(null, "Sekwencje są identyczne. Proszę sprawdzić wprowadzone dane.");
                }
                if (toFindString.length() > 80 || toReplaceString.length() > 80) {
                    JOptionPane.showMessageDialog(null, "Jedna lub więcej sekwencji jest za długa. Proszę sprawdzić wprowadzone dane.");
                }
                if (byteWritter.getAllFiles(pathString).isEmpty()){
                    JOptionPane.showMessageDialog(null, "Wskazany folder jest pusty. Proszę sprawdzić wprowadzone dane.");
                }
                if (byteWritter.getAllFiles(pathString).stream().filter(file -> file.getAbsolutePath().endsWith(extensionString)).count()==0){
                    JOptionPane.showMessageDialog(null, "Wskazany folder nie zawiera plików z danym rozszerzeniem. Proszę sprawdzić wprowadzone dane.");
                }
                else {
                    byteWritter.modifyFiles(pathString, extensionString, toFindString, toReplaceString);
                    JOptionPane.showMessageDialog(null, byteWritter.getReport());
                    byteWritter.clearReport();
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("BitApp");
        frame.setContentPane(new Application().panel);
        frame.setVisible(true);
    }
}