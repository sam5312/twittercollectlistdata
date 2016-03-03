package proj.data.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import proj.conf.Paths;
import proj.utils.FileUtil;
import proj.utils.MapUtils;
import proj.utils.TweetProcessingUtil;
import proj.utils.MapUtils.C1;

/**
 * used to find freq terms used in lists. purpose: to find initial query terms
 * input: tweets after remv sws
 * output: most freq. terms for used in each list
 * @author sam
 *
 * 13 Oct 2015	4:01:07 pm
 */
public class FindFreqTermsUsedInLists
{

	public static void main( String[] args )
	{
		readUserTweets();
		printMapToFS();
	}
	
	private static void printMapToFS()
	{
		

	}

	/**
	 * reads files (user profiles) inside lists
	 */
	private static void readUserTweets()
	{
		try
		{
			ArrayList<String> fileNames = FileUtil.readPropFileAndGetValues( "liststoprocess.properties" );

			for ( String listFileStr : fileNames )
			{
				Map<String,C1> map = MapUtils.getNewMap();
		
//				File f = new File( Config.ROOT_DIR + Config.LISTS + listFile ); // open folder
				File f = new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.SWR + listFileStr );
				
				File[] files = f.listFiles(); // files inside the folder

				for ( File uName : files ) // each file contains tweets of a particular user
				{
					ArrayList<String> tweets = new ArrayList<>();
					FileUtil.readLines( uName, tweets );
					for ( String tweet : tweets )
					{
						ArrayList<String> tokens = TweetProcessingUtil.tokenize( tweet );
						for ( String token : tokens )
						{
							MapUtils.IncrementThisUnitInTheMap( token, map,"");
						}
						
					}

				}
				
				Object[] array = MapUtils.sortMap( map );

				for ( Object e : array )
				{
					String str = ( ( Map.Entry<String, C1> ) e ).getKey() + " : "  + ( ( Map.Entry<String, C1> ) e ).getValue().freq;
					FileUtil.writeToFile( new File( Paths.ROOT_DIR + Paths.LOGS + listFileStr + "/freqterms.txt" ), str );
				}
				map.clear();
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}
	

}
