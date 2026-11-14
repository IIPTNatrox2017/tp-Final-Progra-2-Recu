package Exceptions;

public class RepositoryWriteException extends RuntimeException
{
    public RepositoryWriteException(String message)
    {
        super(message);
    }
}
