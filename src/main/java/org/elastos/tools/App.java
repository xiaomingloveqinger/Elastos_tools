package org.elastos.tools;

import ru.lanwen.verbalregex.VerbalExpression;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	
    	// 1
    	VerbalExpression testRegex = VerbalExpression.regex()
                .startOfLine().then("http").maybe("s")
				.then("://")
				.maybe("www.").anythingBut(" ")
				.endOfLine()
				.build();
    	
    	String url = "https://www.google.com";
    	
    	System.out.println(testRegex.testExact(url) + "  " + testRegex.toString());
    	
    	
    	
//    	
//    	// 2
//    	VerbalExpression testReg1 = VerbalExpression.regex().startOfLine().space().zeroOrMore().
//    			then("importlib(").anything().then(");").build();
//    	
//    	String url2 = "importlib(\"Elastos.CoreLibrary\");";
//    	System.out.println(testReg1.testExact(url2)  +" " + testReg1.toString());
    	
    	// 3
//    	VerbalExpression testReg3 = VerbalExpression.regex().startOfLine().space().zeroOrMore().
//    			then("interface").anything().then("{").build();
//    	
//    	String url3 = "interface ITest {";
//    	System.out.println(testReg3.testExact(url3)  +" " + testReg3.toString());
//    	
//    	String reg = "^(?:\\s)*(?:interface)(?:.*)(?:\\{)";
//    	Pattern pattern = Pattern.compile(reg);
//
//    	String[] ret = pattern.split(url3);
//    	for(int i=0;i<ret.length;i++){
//    		System.out.println(ret[i]);
//    	}
    	
//    	String regex = "http://|//.";
//
//    	Pattern p = Pattern.compile(regex);
//
//    	String[] ret = p.split("http://www.baidu.com");
//
//    	for(int i = 0; i < ret.length; i++) {
//
//    		System.out.println(ret[i]);
//    		
//    	}
    	
//    	String str = "1\n2";
    	
//    	System.out.println(str);
    	
    	System.out.println(VerbalExpression.regex().startOfLine().space().zeroOrMore().
		then("importlib(").anything().then(");").endOfLine().build().toString());
    	
    	System.out.println(VerbalExpression.regex().startOfLine().space().zeroOrMore().
    			then("interface").anything().then("{").endOfLine().build().toString());
    	    	
    	System.out.println(VerbalExpression.regex().startOfLine().space().zeroOrMore().
    			then("class").anything().then("{").endOfLine().build().toString());
    	

    }
}
