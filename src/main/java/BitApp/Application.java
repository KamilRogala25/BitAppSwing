package BitApp;

import BitApp.Exceptions.SelectionIsEmptyException;
import BitApp.Model.FileModifier;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
            FileModifier fileModifier = new FileModifier();
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
//                if (path.toString().isEmpty() || extension.toString().isEmpty() || toFindSequ.toString().isEmpty() || toReplaceSequ.toString().isEmpty()) {
//                    throw new SelectionIsEmptyException("Pola są puste. Wprowadź poprawne dane.");
//                    return;
//                }
                String pathString = path.getText();
                String extensionString = extension.getText();
                String toFindString = toFindSequ.getText();
                String toReplaceString = toReplaceSequ.getText();
                String message = fileModifier.modifyFiles(pathString,extensionString,toFindString,toReplaceString);
                JOptionPane.showMessageDialog(null, message + " Utworzono backup w lokalizacji D:\\Applications\\BitApp\\Backups");
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("BitApp");
        frame.setContentPane(new Application().panel);
        frame.setVisible(true);
    }
}
