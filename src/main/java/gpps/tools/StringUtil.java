package gpps.tools;

public class StringUtil {
	public static String checkNullAndTrim(String name,String value) throws IllegalArgumentException
	{
		if(value==null||(value=value.trim()).length()==0)
			throw new IllegalArgumentException(name+"must not be null");
		return value;
	}
	public static boolean isEmpty(String value)
	{
		return (value==null||value.trim().length()==0);
	}
}
