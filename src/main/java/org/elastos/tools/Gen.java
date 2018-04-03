package org.elastos.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * a simple car to java file test  
 * 
 * @author clark
 * 
 * Apr 3, 2018
 */
public class Gen {
	
	public static class OutputHelper{
		
		public static String STATIC_OUTPUT_HELP = 
				"arguments can not be null .at least 1;\n "
			  + "Example : Gen test.car";
		
	}
	
	private static final String PKG_REG = "^(?:\\s)*(?:importlib\\()(?:.*)(?:\\)\\;)$";
	
	
	private static final String I_REG = "^(?:\\s)*(?:interface)(?:.*)(?:\\{)$";
	
	
	private static final String C_REG = "^(?:\\s)*(?:class)(?:.*)(?:\\{)$";
	
	// package storage
	private static List<String> pkgList = new ArrayList<>();
	
	// interface storage
	private static List<String> iList = new ArrayList<>();
	
	// interface method storage
	private static Map<String,List<String>> i_method_list = new HashMap<>();
	
	// class storage
	private static List<String> cList = new ArrayList<>();
	
	// class method storage
	private static Map<String,List<String>> c_method_list = new HashMap<>();
	
	public static void main(String[] args) {
		if (args.length < 1){
			System.out.println(OutputHelper.STATIC_OUTPUT_HELP);
			return ;
		}
		
		for(int i=0;i<args.length;i++){
			exec(args[i]);
		}
	}
	
	public static void exec(String fileName){
		
		File file = new File(fileName);
		
		if(!file.exists()){
			System.out.println("File " + fileName + " is not find" );
			return ;
		}
		System.out.println(file.getName());
		if(!file.getName().endsWith(".car")){
			System.out.println("Not a .car file");
			return ;
		}
		
		Boolean started =false;
		Boolean moduleStart = false;
		Boolean classStart = false;
		Boolean interfaceStart = false;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			List<String> storage = new ArrayList<>();
			while( (line = br.readLine()) != null ) {
				if(started){
					if(!Kit.isBlank(Kit.clearBlank(line))){
						storage.add(line);	
					}
				}else{
					if("module".equals(line.replaceAll("\\s+", ""))){
						started = true;
					}else{
						continue;
					};
				}
			}
			
			for(int i=0;i<storage.size();i++){
				
				String str = storage.get(i);
				if(Pattern.matches(PKG_REG, str)){
					String pkgName = str.split("\\(\"")[1].split("\"\\)")[0];
					pkgList.add("import " + pkgName +";");
					continue;
				};
				
				if(Pattern.matches(I_REG, str)){
					interfaceStart = true;
					String iName = str.split("interface")[1].split("\\{")[0];
					iList.add(Kit.clearBlank(iName));
					continue;
				}
				
				if(Pattern.matches(C_REG, str)){
					classStart = true;
					String cName = str.split("class")[1].split("\\{")[0];
					cList.add(Kit.clearBlank(cName));
					continue;
				}
				
				if( "{".equals(Kit.clearBlank(str))){
					moduleStart = true;
					continue;
				}
				
				if (interfaceStart == true && "}".equals(Kit.clearBlank(str))){
					interfaceStart = false;
					continue;
				}else if (classStart == true && "}".equals(Kit.clearBlank(str))){
					classStart = false;
					continue;
				}else if(moduleStart == true && "}".equals(Kit.clearBlank(str))){
					moduleStart = false;
					continue;
				}
				
				if(interfaceStart == true){
					
					String methodStr = "";
					
					for(int j = i;;j++){
						
						String i_str = storage.get(j);
						
						if(i_str.indexOf(";") != -1){
							int index = i_str.indexOf("[out]");
							if(index != -1){
								String tmpOutput = Kit.getPartStr(i_str.substring(index+"[out]".length()), ')');
								if(tmpOutput.indexOf("String") != -1){
									methodStr = " String " + methodStr;
								}
								methodStr += i_str.replace(tmpOutput, "").replace("[out]", "");
							}else{
								methodStr += i_str;
							}
							break;
						}else{
							methodStr += i_str;
						}
						
						i++;
					}
					
					List<String> m_list = i_method_list.get(iList.get(iList.size()-1));
					if(m_list == null){
						m_list = new ArrayList<>();
						i_method_list.put(Kit.clearBlank(iList.get(iList.size()-1)), m_list);
					}
					m_list.add(methodStr);
					
				}
				
				if(classStart == true){
					
					
					String c_str = str;
					
					int i_index =  c_str.indexOf("interface") ;
					if( i_index != -1){
						String i_name = c_str.substring(i_index + "interface".length(), c_str.indexOf(";"));
						List<String> i_m_list = i_method_list.get(Kit.clearBlank(i_name));
						for(int z= 0; z < i_m_list.size();z++){
							
							String i_m = i_m_list.get(z);
							
							String c_m = i_m.substring(0, i_m.indexOf(";"));
							
							if(c_m.indexOf("void") != -1 ){
								c_m += "{\n}";
							}else{
								c_m += "{\n return null; \n}";
							}
							
							List<String> c_m_list = c_method_list.get(cList.get(cList.size() - 1));
							if(c_m_list == null){
								c_m_list = new ArrayList<>();
								c_method_list.put(cList.get(cList.size() - 1), c_m_list);
							}
							c_m_list.add(c_m);
						}
					}
				}
			}
			makeClass("./src/test/java/org/elastos/tools");
			makeInterface("./src/test/java/org/elastos/tools");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void makeClass(String dirName){
		
		File dir = new File(dirName);
		
		if(!dir.exists()){
			dir.mkdirs();
		};
		
		for(int i=0;i<cList.size();i++){
			
			String fileName = Kit.clearBlank(cList.get(i));
			
			File file = new File(dirName + "/" + fileName +".java");
			
			FileWriter fw  = null;
			try {
				
				fw = new FileWriter(file);
				// add package and import 
				fw.write("package org.elastos.tools;\n");
				
				for(int j = 0 ; j < pkgList.size() ; j ++ ){
					fw.write(pkgList.get(j)+"\n");	
				}
				
				// write prefix class 
				fw.write("public class " + fileName +"{\n");
				
				// write method of class
				List<String> m_list = c_method_list.get(fileName);
				for(int j=0;j<m_list.size();j++){
					fw.write(m_list.get(j)+"\n");
				}
				
				// write stufix class
				fw.write("\n}");
				fw.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	private static void makeInterface(String dirName){
		
		File dir = new File(dirName);
		
		if(!dir.exists()){
			dir.mkdirs();
		};
		
		for(int i=0;i<iList.size();i++){
			
			String fileName = Kit.clearBlank(iList.get(i));
			
			File file = new File(dirName + "/" + fileName + ".java");
			
			FileWriter fw  = null;
			try {
				
				fw = new FileWriter(file);
				
				fw.write("package org.elastos.tools;\n");
				// write prefix interface 
				fw.write("public interface " + fileName +"{\n");
				
				// write method of interface
				List<String> m_list = i_method_list.get(fileName);
				for(int j=0;j<m_list.size();j++){
					fw.write(m_list.get(j));
				}
				
				// write stufix class
				fw.write("\n}");
				fw.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
}
