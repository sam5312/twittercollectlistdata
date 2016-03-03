package proj.data.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import proj.conf.Paths;
import proj.utils.FileUtil;

/**
 * once u get freq terms used in lists, this class is used to filter out users who doesn't have some n number of freq terms 
 * used in their profile.
 * @author sam
 *
 * 14 Oct 2015	10:34:01 am
 */
public class FilterUsersFromLists
{
	public static void main(String args[])
	{
		start();
	}
	
	private static void start()
	{
		try
		{
			ArrayList<String> fileNames = FileUtil.readPropFileAndGetValues( "liststoprocess.properties" );
			for ( String listName : fileNames )
			{
				File f = new File( Paths.ROOT_DIR + Paths.LOGS+listName + "/freqterms.txt"); //open the freq terms file for that part. list
				
				ArrayList<String> lines = new ArrayList<>();
				FileUtil.readLines( f, lines );
				
				for ( String term : lines )
				{
					getUsersForThisTerm(term,listName);
				}
				
			}
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void getUsersForThisTerm(String term,String listName)
	{
		File dir = new File(Paths.ROOT_DIR+Paths.LISTS_PRO+Paths.SWR + listName);
		File[] files = dir.listFiles();
		for ( File userProf : files )
		{
			getInstancesOfThisKw(userProf,term);
		}
	}

	private static void getInstancesOfThisKw( File userProf, String term )
	{
		ArrayList<String> lines = new ArrayList<>();
		FileUtil.readLines( userProf, lines );
		
		for ( String line : lines )
		{
			
		}
	}
}
   