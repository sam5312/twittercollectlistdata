package proj.data.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import proj.conf.Paths;
import proj.utils.FileUtil;
import proj.utils.TweetProcessingUtil;

/**
 * input: raw tweets
 * output: tweets with sws removed
 * @author sam
 *
 * 10 Feb 2016	11:09:47 am
 */
public class RemoveSWsOnly
{

	public static void main( String[] args )
	{
		readUserTweets();
	}
	
	private static void readUserTweets()
	{
		try
		{
			ArrayList<String> fileNames = FileUtil.readPropFileAndGetValues( "liststoprocess.properties" );

			for ( String listFile : fileNames )
			{
				File f = new File( Paths.ROOT_DIR + Paths.LISTS + listFile ); // open folder
				File[] files = f.listFiles(); // files inside the folder

				for ( File uName : files ) // each file contains tweets of a particular user
				{
					ArrayList<String> lines = new ArrayList<>();
					FileUtil.readLines( uName, lines );
					for ( String tweet : lines )
					{
						parseTweet( tweet, ( listFile + "/" + uName.getName() ), true );
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
							FileUtil.writeToFile( new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.NOSWs + newfilename ), cleanedTweet );
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
