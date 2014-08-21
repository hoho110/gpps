package gpps.service.exception;

public class InsufficientBalanceException extends Exception{
	public InsufficientBalanceException()
	{
		super();
	}
	public InsufficientBalanceException(String msg)
	{
		super(msg);
	}
}
