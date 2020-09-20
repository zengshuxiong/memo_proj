//package com.github.torlight.jvmtit;

/**
 * Hello world!
 *
 */
public class App {
    
    public static void main( String[] args ){
        System.out.println( "Hello World!" );
		queryData();
    }
	
	public static void queryData(){
		try {
            throw new NullPointerException("Some error message XXX");
        } catch (Exception e) {
            // didn't print.
        } 
	}
}
