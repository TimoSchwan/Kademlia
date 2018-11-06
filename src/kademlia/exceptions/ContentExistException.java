package kademlia.exceptions;

/**
 * An exception used to indicate that a content already exist on the DHT
 *
 * @author Joshua Kissoon
 * @created 20140322
 */
public class ContentExistException extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContentExistException()
    {
        super();
    }

    public ContentExistException(String message)
    {
        super(message);
    }
}
