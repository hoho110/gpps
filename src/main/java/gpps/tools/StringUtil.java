package gpps.tools;

public class StringUtil {
	public static String checkNullAndTrim(String name, String value)
			throws IllegalArgumentException {
		if (value == null || (value = value.trim()).length() == 0)
			throw new IllegalArgumentException(name + "must not be null");
		return value;
	}
	public static String strFormat(String value)
	{
		if(isEmpty(value))
			return "";
		return value.trim();
	}
	public static boolean isEmpty(String value) {
		return (value == null || value.trim().length() == 0);
	}

	public static boolean isDigit(String value) {
		if (isEmpty(value))
			return false;
		char[] c = value.toCharArray(); // 把输入的字符串转成字符数组
		for (int i = 0; i < c.length; i++) {
			if (!Character.isDigit(c[i])) { // 判断是否为数字
				return false;
			}
		}
		return true;
	}
	public static void main(String[] args)
	{
		System.out.println(isDigit("12314124124"));
		System.out.println(isDigit("12a24124"));
	}
}
