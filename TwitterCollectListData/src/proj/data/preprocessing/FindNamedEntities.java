package proj.data.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import proj.conf.Paths;
import proj.utils.FileUtil;

/**
 * recognizes nes from tweet text and saves them back to the fs. doesn't convert text to lowercase as we need to identify 
 * frequent NEs.
 * 
 * @author sam
 *
 * 26 Nov 2015	3:36:06 pm 
 */
public class FindNamedEntities
{
	/**
	 * @param args
	 */
	static	MaxentTagger tagger = new MaxentTagger("stanford-postagger-2015-04-20/models/english-left3words-distsim.tagger");
	
	public static void main( String[] args )
	{
//		String tagString = tagger.tagString( "I'm a drug dealer,I don't wear no suits and pretend to be something I'm not. I have respect for human\u2026 https://t.co/0SStD6ejrC" );
//		
//		System.out.println( tagString );
		
		readUserTweets();
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
				tweetTxt = proj.utils.TweetProcessingUtil.getTweetText( tweet );
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
						String cleanedTweet = proj.utils.TweetProcessingUtil.processTweetText( tweetTxtStrBuilder.toString(), false );

						
						if ( cleanedTweet.trim().length() > 0) //only if its a valid tweet
						{
							String tagString = tagger.tagString(cleanedTweet);
							FileUtil.writeToFile( new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.NES 
																	+ newfilename ), tagString );
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
