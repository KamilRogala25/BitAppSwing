package BitApp.model;


import BitApp.exceptions.DirectoryIsEmptyExcepion;
import BitApp.exceptions.SelectionIsEmptyException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ByteWritter {

    private String path;
    private Message message = new Message();

    //Przeszukuje wskazaną lokalizację w poszukiwaniu plików
    public List<File> getAllFiles(String path) {
        File directory = new File(path);
        List<File> files = new ArrayList();
        File[] filesArray = directory.listFiles();
        files.addAll(Arrays.asList(filesArray));
        for (File file : filesArray) {
            if (file.isDirectory()) {
                files.addAll(getAllFiles(file.getAbsolutePath()));
            }
        }
        return files;
    }

    //Przeszukuje wskazaną lokalizację w poszukiwaniu plików danego typu
    public List<File> getFilesOfExtension(String path, String extension) throws DirectoryIsEmptyExcepion, SelectionIsEmptyException {
        if (path.isEmpty()) {
            throw new SelectionIsEmptyException("Proszę wskazać poprawną ścieżkę.");
        }
        ByteWritter byteWritter = new ByteWritter();
        List<File> allFiles = byteWritter.getAllFiles(path);
        List<File> results = new ArrayList();

        for (File file : allFiles) {
            String name = file.getAbsolutePath();
            if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase(extension)) {
                results.add(file);
            }
        }
        if (results.isEmpty()) {
            throw new DirectoryIsEmptyExcepion("Nie znaleziono żadnego pliku typu" + extension + ".");
        }
        return results;
    }

    //Odczyt pojedynczego pliku
    public String readFile(String path) throws IOException, OutOfMemoryError {
        File file = new File(path);
        byte[] fileBytes = Files.readAllBytes(Paths.get(path));
        StringBuilder sb = new StringBuilder("");
        for (byte b : fileBytes) {
            sb.append(b);
        }
        return sb.toString();
    }

    //Tworzenie backupu
    public void createBackup(List<File> toCopy) throws IOException {

        String backupPath = "D:\\Applications\\BitApp\\Backups\\";
        LocalDateTime localDateTime = LocalDateTime.now();
        StringBuilder backupPathBuilder = new StringBuilder("");
        backupPathBuilder.append(localDateTime.getYear());
        backupPathBuilder.append(localDateTime.getMonth());
        backupPathBuilder.append(localDateTime.getDayOfMonth());
        backupPathBuilder.append(localDateTime.getHour());
        backupPathBuilder.append(localDateTime.getMinute());
        backupPathBuilder.append(localDateTime.getSecond());
        String folderName = backupPathBuilder.toString();
        List<File> backup = new ArrayList<>();
        for (File file : toCopy) {
            String newPath = backupPath + folderName + "\\" + file.getAbsolutePath().substring(3);
            backup.add(new File(newPath));
            FileUtils.copyFile(file, new File(newPath));
        }
        message.addMessage("W lokalizacji " + folderName + " utworzono backup");
    }

    //Zmiana sekwencji bajtów
    public String replaceSequence(String path, CharSequence toFindSequ, CharSequence toReplaceSequ) throws IOException {
        if (toFindSequ.length() > 80 || toReplaceSequ.length() > 80 || (toFindSequ.length() > 80 && toReplaceSequ.length() > 80)) {
            message.addMessage("Nie rozpoczęto działania, Jedna z sekwencji jest za długa.");
            throw new IllegalStateException("Sekwencja jest za duża.");
        }
        if (toFindSequ.equals(toReplaceSequ)) {
            message.addMessage("Nie rozpoczęto działania, sekwencje są takie same.");
            throw new IllegalStateException("Sekwencje są równe.");
        }
        if (path.isEmpty() || toFindSequ == null || toReplaceSequ == null) {
            message.addMessage("Nie rozpoczęto działania, wprowadzono puste dane.");
            throw new IllegalStateException("Podano puste dane.");
        }
        ByteWritter fileModifier = new ByteWritter();
        String bytes = fileModifier.readFile(path);
        return bytes.replace(toFindSequ, toReplaceSequ);

    }

    //Dla danej lokalizacji zmienia sekwencję
    public void modifyFiles(String path, String extension, CharSequence toFindSequ, CharSequence toReplaceSequ) throws IOException, DirectoryIsEmptyExcepion, SelectionIsEmptyException {
        ByteWritter fileModifier = new ByteWritter();
        List<File> files = fileModifier.getFilesOfExtension(path, extension);
        String tempBytes;
        fileModifier.createBackup(files);
        message.addMessage("W lokalizacji D:\\Applications\\BitApp\\Backups\\ utworzono backup.");

        for (File file : files) {
            if (!files.isEmpty()) {
                tempBytes = fileModifier.replaceSequence(file.getAbsolutePath(), toFindSequ, toReplaceSequ);
                if (fileModifier.readFile(file.getAbsolutePath()).equals(tempBytes)) {
                    message.addMessage("Nie zmodyfikowano pliku " + file.getAbsolutePath());
                    message.incrementNumberOfUnchangedFiles();

                } else {
                    String tempPath = file.getAbsolutePath();
                    file.delete();
                    byte[] result = tempBytes.getBytes();
                    File resultFile = new File(tempPath);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(resultFile);
                        fos.write(result);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    message.addMessage("Zmodyfikowano plik " + file.getAbsolutePath());
                    message.incrementNumberOfChangedFiles();
                }
            }
        }
        message.addMessage("W lokalizacji " + path + ". Zmodyfikowano " + message.getNumberOfChangedFiles() + " plików typu " + extension + ".");
        if (message.getNumberOfUnchangedFiles() > 0) {
            message.addMessage("Nie zmodyfikowano " + message.getNumberOfUnchangedFiles() + " plików typu " + extension + ".");
        }
        message.resetNumberOfChanges();
    }

    //Generuje raport z działania pliku
    public String getReport() {
        return message.getAllMessagesAsString();
    }
    //Czyści raport
    public void clearReport(){
        message.clearAllMessages();
    }

}
