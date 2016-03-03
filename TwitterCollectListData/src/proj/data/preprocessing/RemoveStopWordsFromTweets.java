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
 * is used to remove stopwords from tweet text and saves them back to the fs. 
 * input: tweets after removing slangs
 * output: tweets with no sws
 * @author sam
 *
 * 12 Oct 2015	3:27:17 pm
 */
public class RemoveStopWordsFromTweets
{

	public static void main( String[] args )
	{
		readUserTweets();
		Object[] sortMap = MapUtils.sortMap( TweetProcessingUtil.map );
		
		for ( Object e : sortMap )
		{
			String str = ( ( Map.Entry<String, C1> ) e ).getKey() + " : " + ( ( Map.Entry<String, C1> ) e ).getValue().word + " : " + ( ( Map.Entry<String, C1> ) e ).getValue().freq;
			FileUtil.writeToFile( new File(Paths.ROOT_DIR+ Paths.LOGS+"swremvfreq.txt"), str );
		}
	}
	
	/**
	 * reads files (user profiles) inside lists
	 */
	private static void readUserTweets()
	{
		try
		{
			ArrayList<String> fileNames = FileUtil.readPropFileAndGetValues( "liststoprocess.properties" );

			for ( String listFile : fileNames )
			{
		
//				File f = new File( Config.ROOT_DIR + Config.LISTS + listFile ); // open folder
				File f = new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.NOSLANG + listFile );
				
				File[] files = f.listFiles(); // files inside the folder

				for ( File uName : files ) // each file contains tweets of a particular user
				{
					ArrayList<String> tweets = new ArrayList<>();
					FileUtil.readLines( uName, tweets );
					for ( String tweet : tweets )
					{
						parseTweet( tweet, ( listFile + "/" + uName.getName() ), false );
					}

				}
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	
	private static void parseTweet(String tweet, String newfilename, boolean isTweetJson)
	{		
		StringBuilder tweetTxtStrBuilder = new StringBuilder();
		try
		{
			String tweetTxt = null;
			
			if (isTweetJson) //tweeet json is passed
			{
				tweetTxt = TweetProcessingUtil.getTweetText( tweet );
			}
			else //a string is passed
			{
				tweetTxt = tweet;
			}
			
			
			tweetTxtStrBuilder = new StringBuilder( tweetTxt ); //tweet text
			//
			try
			{
				{
					if (tweetTxtStrBuilder.toString().trim().length() > 0)
					{
						String cleanedTweet = TweetProcessingUtil.processTweetText( tweetTxtStrBuilder.toString(), true );
						
						if ( cleanedTweet.trim().length() > 0) //only if its a valid tweet
						{
							FileUtil.writeToFile( new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.SWR 
																	+ newfilename ), cleanedTweet );
						}
					}
				}
	
			}
			catch ( NullPointerException e )
			{
				System.out.println(e);
				System.exit( 1 );
			}
		
		}
		catch ( Exception e ) // expanded_url can be null
		{
			System.out.println( e  + " "  + tweet);
			System.exit( 1 );
		}
	}

}
