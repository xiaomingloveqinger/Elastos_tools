package org.elastos.tools;

/**
 * 
 * a litter help kit
 * 
 * @author clark
 * 
 * Apr 3, 2018
 * 
 */
public final class Kit {
	
	/**
	 * clear the blank of string 
	 * @param str
	 * @return
	 */
	public static String clearBlank(String str){
		if(str == null){
			return "";
		}
		return str.replaceAll("\\s+", "");
	}
	
	/**
	 * check the str is blank or not 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null || "".equals(str))
			return true;
		return false;
	}
	
	/**
	 * get part of str . stoped when it hits endchar.
	 * @param str
	 * @param endchar
	 * @return
	 */
	public static String getPartStr(String str , char endchar){
		String retStr = "";
		char[] charArr = str.toCharArray();
		for(int i=0;i<charArr.length;i++){
			if(endchar == charArr[i])
				break;
			retStr += charArr[i];
		}
		return retStr;
	}
	
	
}
