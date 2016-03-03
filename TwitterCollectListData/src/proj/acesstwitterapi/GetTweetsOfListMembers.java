package proj.acesstwitterapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import proj.conf.Paths;
import proj.utils.FileUtil;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.User;

/**
 * responsible for: reading list members file to get usernames and crawling past tweets of users using the twitter rest API. will output files
 * containing
 * 
 * @author sam 6 Oct 2015 4:08:55 pm
 */

public class GetTweetsOfListMembers
{
	static int apicounter = 0;
	
	public static void main( String args[] ) throws IOException
	{
		ArrayList<String> listNames = FileUtil.readPropFileAndGetValues( "twitterlists.properties" );
		readListFiles(listNames);
//		File f = new File( Config.WRITE_ROOT_DIR + Config.LISTS );
//		readDir( f.listFiles() );
	}
	
	private static void readListFiles( ArrayList<String> files )
	{
		for ( String fname : files ) // for each list
		{
			File file = new File( Paths.ROOT_DIR + Paths.LISTS + fname);

			if ( !file.isDirectory() )
			{
				String listSlug = file.getName();
				if (listSlug.equalsIgnoreCase( "travel" ))
				{
				ArrayList<String> userNames = new ArrayList<>();
				FileUtil.readLines( file.getPath(), userNames );
				getTweetsOfTheseUsers( listSlug, userNames );
				}
			}
		}
	}

	/**
	 * collect tweets of the  members in the list given
	 * @param slug list where members will be crawled
	 * @param usernames twitter users who are mambers of the given list
	 */
	private static void getTweetsOfTheseUsers( String slug, List<String> usernames )
	{
		System.out.println( "proceeeisng list name : " + slug );
		Twitter twit = TwitterAuth.getInstance();

		try
		{
			User verifyCredentials = twit.verifyCredentials();
			System.out.println( "user " + verifyCredentials.getScreenName() + " suceessfully authenticated" );
		}
		catch ( TwitterException e2 )
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
		for ( String user : usernames ) //UnitedAirlines
		{
			
			File file = new File( Paths.ROOT_DIR + Paths.LISTS + slug + "_list" + "/" + user );
			if (file.exists()) //if thie user has been downloaded (eg from a previous run) don't download him again
				continue;
			
			if (user.equals( "USAirways" ) || user.equals( "continental" )) //can't get tweets of this user so ignore it. throws a missing credentials exception.
				continue;
			
			List<String> tweetsJson = new ArrayList<String>();

			int pageno = 1;
			while ( true )
			{
				try
				{ 
					Paging page = new Paging( pageno, 100 );
					ResponseList<Status> userTimeline = twit.getUserTimeline( user, page );
					apicounter++;
					
					if ( userTimeline.size() == 0 ) // has reached the end of the usertime timeline where max is 3200 tweets, break out of the loop
					{
						break;
					}

					for ( Status status : userTimeline )
					{
						String rawJSON = TwitterObjectFactory.getRawJSON( status );// DataObjectFactory.getRawJSON( status );
						tweetsJson.add( rawJSON );
					}
					
					pageno++;
				}
				catch ( TwitterException e )
				{
					if (String.valueOf( e.getStatusCode()).equals( "429")) //HTTP 429 “Too Many Requests” response code i
					{
						try
						{
							System.out.println( "API COUNTER VAL" + apicounter );
							Thread.sleep( 15 *1000*60 ); //wait 15 minutes before the next call
						}
						catch ( InterruptedException e1 )
						{
							e1.printStackTrace();
						}
						//due to API rate limi, just bypassti
					}
					else if(e.getErrorCode() == 34) //user doesn't exist error
					{
						System.out.println( user + "\t" + e.getErrorMessage() );
						break;
					}
					else
					{
						e.printStackTrace();
					}
				}

			}

			System.out.println( user + " Total: " + tweetsJson.size() );

			for ( String tweetJson : tweetsJson )
			{
//				File file = new File( Paths.ROOT_DIR + Paths.LISTS + slug + "_list" + "/" + user );
				FileUtil.writeToFile( file, tweetJson );
			}

		}
	}
}
