package net.dohaw.play.divisions.exceptions;

public class InvalidCustomItemKeyException extends Exception {

    public InvalidCustomItemKeyException(String customItemKey){
        super("A custom item with the key " + customItemKey + " could not be found!");
    }

}
