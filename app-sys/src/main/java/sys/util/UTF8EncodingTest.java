package sys.util;

import java.io.File;
import java.io.IOException;

import org.h2.store.fs.FileUtils;

public class UTF8EncodingTest {
	 public static void showBinary(String s){  
	        showBinary(s.getBytes());         
	    }  
	    public static void showBinary(byte[] buf){  
	        for(byte b:buf) {  
	            System.out.printf("%x ", b);  
	        }  
	        System.out.println();  
	    }  
	      
//	    public static boolean isUTF8(File file) {  
//	        try {  
//	            byte[] buf = FileUtils.readFileToByteArray(file);  
//	            System.out.println("\t<<>>");  
//	            showBinary(buf);  
//	            String UTF8Cntent = FileUtils.readFileToString(file, "UTF-8");  
//	            String big5Cntent = new String(buf, "Big5");  
//	            String defCntent = new String(buf); //Default is UTF8  
//	            System.out.println("\t<<>>\n"+UTF8Cntent);  
//	            showBinary(UTF8Cntent);  
//	            System.out.println("\t<<>>\n"+big5Cntent);  
//	            showBinary(big5Cntent);  
//	            System.out.println("\t<<>>\n"+defCntent);  
//	            showBinary(defCntent);  
//	              
//	            if(buf.length == UTF8Cntent.getBytes().length) {  
//	                byte[] buf_utf8 = UTF8Cntent.getBytes();  
//	                for(int i=0;i
//	                    if(buf_utf8[i]!=buf[i]){  
//	                        return false;  
//	                    }  
//	                }  
//	                return true;  
//	            }  
//	        } catch (IOException e) {  
//	            e.printStackTrace();  
//	        }  
//	        return false;  
//	    }  
	          
	      
//	    public static void main(String args[]){  
//	        File utf8f = new File("E:/Temp/TestData/utf8.txt");  
//	        File big5f = new File("E:/Temp/TestData/big5.txt");  
//	        if(isUTF8(utf8f)){  
//	            System.out.println(utf8f.getAbsolutePath()+" is utf8 encoding!\n");  
//	        } else {  
//	            System.out.println(utf8f.getAbsolutePath()+" isn't utf8 encoding!\n");  
//	        }  
//	        if(isUTF8(big5f)){  
//	            System.out.println(big5f.getAbsolutePath()+" is utf8 encoding!\n");  
//	        } else {  
//	            System.out.println(big5f.getAbsolutePath()+" isn't utf8 encoding!\n");  
//	        }         
//	    }  

}
