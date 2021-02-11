package net.cyberhub.tkdkid1000;

import java.io.File;
import java.io.IOException;

public class CMD {

	public static void executeCommand(String filename, File dir) throws IOException{
		File file = new File(dir, filename);
	    if(!file.isFile()){
	        throw new IllegalArgumentException("The file " + filename + " does not exist");
	    }
	    if(isLinux()){
	        Runtime.getRuntime().exec(new String[] {"/bin/sh", "-C", file.getAbsolutePath()}, null, dir);
	    }else if(isWindows()){
	        Runtime.getRuntime().exec("cmd /c start cmd /c " + file.getAbsolutePath(), null, dir);
	    }
	}
	public static boolean isLinux(){
	    String os = System.getProperty("os.name");  
	    return os.toLowerCase().indexOf("linux") >= 0;
	}

	public static boolean isWindows(){
	    String os = System.getProperty("os.name");
	    return os.toLowerCase().indexOf("windows") >= 0;
	}
}
