package net.dohaw.play.divisions.managers;

public interface Manager {

    Object getContents();
    boolean hasContent(Object obj);
    void saveContents();
    void loadContents();
    boolean addContent(Object content);
    boolean removeContent(Object content);

}
