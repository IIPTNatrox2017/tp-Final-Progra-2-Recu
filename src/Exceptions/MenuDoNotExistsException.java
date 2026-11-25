package Exceptions;

public class MenuDoNotExistsException extends RuntimeException
{
    public MenuDoNotExistsException(String message)
    {
        super(message);
    }
    public MenuDoNotExistsException()
    {
        super("No such menu exists!");
    }
}
