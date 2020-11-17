package BitApp.model;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class Message {

    private List<String> messages = new ArrayList<>();
    private int numberOfChangedFiles = 0;
    private int numberOfUnchangedFiles = 0;

    //Dodaje wiadomość do listy
    public void addMessage(String message){
        messages.add(message);
    }
    //Zwiększa licznik zmienionych plików o 1
    public void incrementNumberOfChangedFiles(){
        numberOfChangedFiles +=1;
    }
    //Zwiększa licznik niezmienionych plików o 1
    public void incrementNumberOfUnchangedFiles(){
        numberOfUnchangedFiles +=1;
    }
    //Resetuje liczniki
    public void resetNumberOfChanges(){
        numberOfChangedFiles =0;
        numberOfUnchangedFiles =0;
    }
    //Pobiera licznik zmienionych plików
    public int getNumberOfChangedFiles() {
        return numberOfChangedFiles;
    }
    //Pobiera licznik niezmienionych plików
    public int getNumberOfUnchangedFiles() {
        return numberOfUnchangedFiles;
    }
    //Tworzy raport działania programu
    public String getAllMessagesAsString(){
        String result="";
        for (String s: messages){
            result = result + s + "\r\n";
        }
        return result;
    }
    //Usuwa wszystkie wiadomości
    public void clearAllMessages(){
        messages.clear();
    }

}
