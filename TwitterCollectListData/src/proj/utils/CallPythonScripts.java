package proj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import proj.conf.Paths;

/**
 * use this class for calling python scripts using Jython
 * @author sam
 *
 * 25 Nov 2015	4:06:14 pm
 */
public class CallPythonScripts
{
	public static void main(String args[]) throws IOException
	{
		finduserentropy();
	}
	
	public static void finduserentropy() throws IOException
	{
//		String[] args = {"hello"};
//		PythonInterpreter interp = new PythonInterpreter();
//		PythonInterpreter.initialize( System.getProperties(), System.getProperties(), args );
//		interp.exec( Config.ROOT_DIR + "pythonscripts/test.py");
		
//		Process p = Runtime.getRuntime().exec("python " +  Config.ROOT_DIR + "pythonscripts/test.py" + " hello");
//		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		ProcessBuilder pb = new ProcessBuilder( "python", Paths.ROOT_DIR + "pythonscripts/test.py", "hellooo" );
		Process p = pb.start();
		
		BufferedReader in = new BufferedReader( new InputStreamReader( p.getInputStream() ) );
		
		String line ;
		while ( (line = in.readLine()) != null )
		{
			System.out.println( line);
		}
		
	}
	
}
