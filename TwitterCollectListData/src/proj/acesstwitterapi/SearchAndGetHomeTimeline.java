package proj.acesstwitterapi;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.Gson;

import proj.utils.FileUtil;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

/**
 * @author sam 20 Aug 2015
 */
public class SearchAndGetHomeTimeline
{

	static int apicounter = 0;
	static String path = "/media/fatcat/sameendra/personalizedsearch/";

	public static void main( String[] args )
	{
//		 search();

//		FileUtil.delteFile( new File( path + "apple_res/" ) );
//		search2( "apple", "apple_res/" );

		 getuserspasttweets();

	}

	private static void getuserspasttweets()
	{
		Twitter twitter = TwitterAuth.getInstance();

		String outdir = "userdata/";
//		String[] users = new String[] {  "@JavaMountCoffee","@GoldMtnCoffee"}; //TravellerKaskus
		String[] users = new String[] {"@programmingncr", "@javacodegeeks" } ; //"@javinpaul"};
		
		String prefix = "prog_"; //will be the prefix to the input files
		
//		String[] users = new String[] { "@applespotlight" }; //"@applesnpearsAu", "@applenws" }; // "@Knooorlun" };


		for ( String user : users )
		{
			System.out.println( "collecting tweets for " + user);
			int pageno = 1;
			List<Status> statuses = new ArrayList<Status>();

			while ( true )
			{
				try
				{
					int size = statuses.size();
					Paging page = new Paging( pageno, 100 );
					pageno++;
					statuses.addAll( twitter.getUserTimeline( user, page ) );
					if ( statuses.size() == size )
						break;
				}
				catch ( TwitterException e )
				{
					e.printStackTrace();
				}
				System.out.println( "so far collected tweets: " + statuses.size() );
			}

			System.out.println( "Total: " + statuses.size() );

			FileUtil.delteFile( new File( path + outdir + user ) );
			for ( Status status : statuses )
			{

				// String rawJSON = TwitterObjectFactory.getRawJSON( status );//DataObjectFactory.getRawJSON( status );
				// System.out.println( rawJSON );
				//
				// JSONObject jo = new JSONObject( status );
				// System.out.println( jo );

				Gson gson = new Gson();
				String json = gson.toJson( status );
				//
//				System.out.println( json );
				//
				
	
				FileUtil.writeToFile( new File( path + outdir + prefix+ user + ".dat" ), json );
				FileUtil.writeToFile(new File( path + outdir + prefix + user.substring( 1 ) + ".dat" ), status.getText().replace( "\n", " " ) );
			
			}

		}
	}

	/**
	 * uses query.setMaxId()
	 */
	private static void search2( String query, String outdirname )
	{

		Twitter twitter = TwitterAuth.getInstance();
		Query q = new Query( query );
		q.setLang( "en" );
		q.setCount( 100 );
		long lastid = 671046221935550464L ; // Long.MAX_VALUE;
		int count = 1;

		while ( true )
		{
			try
			{
				Map<String, List<String>> usersinresults = new HashMap<String, List<String>>();

				QueryResult result = twitter.search( q );
				apicounter++;
				List<Status> tweets = result.getTweets();
				System.out.println( "Gathered " + tweets.size() + " tweets" );

				if ( tweets.size() == 0 )
					break;

				for ( Status t : tweets )
				{
					count++;
					if ( t.getId() < lastid )
						lastid = t.getId(); //for apple = 671046221935550464

					System.out.println( "Tweet: " + t.getText().replace( "\n", " " ) + " *|* " + t.getCreatedAt() + t.getLang() );
					String screenName = t.getUser().getScreenName(); // tweet owner

					if ( usersinresults.containsKey( screenName ) )
					{
						System.out.println( "*************** found user already!" + screenName );

						List<String> usertweets = usersinresults.get( screenName );
						usertweets.add( t.getText().replace( "\n", " " ) + " *|* " + t.getCreatedAt() );
						usersinresults.put( screenName, usertweets );
					}
					else
					{
						List<String> usertweets = new ArrayList<>();
						usertweets.add( t.getText().replace( "\n", " " ) );
						usersinresults.put( screenName, usertweets );
					}
					FileUtil.writeToFile( new File( path + outdirname + query + ".dat" ), TwitterObjectFactory.getRawJSON( t ) );

				}

				q.setMaxId( lastid - 1 );
				System.out.println( "total tweets so far = " + count );
				writemaptofs( outdirname + "users/", usersinresults );

			}
			catch ( TwitterException e )
			{
				if ( String.valueOf( e.getStatusCode() ).equals( "429" ) ) // HTTP 429 “Too Many Requests” response code i
				{
					try
					{
						System.out.println( "API COUNTER VAL" + apicounter );
						Thread.sleep( 15 * 1000 * 60 ); // wait 15 minutes before the next call
					}
					catch ( InterruptedException e1 )
					{
						e1.printStackTrace();
					}
					// due to API rate limit, just bypassti
				}
				else
				{
					e.printStackTrace();
				}
			}
		}

	}

	private static void writemaptofs( String outdir, Map<String, List<String>> usersinresults )
	{
		Set<Entry<String, List<String>>> entrySet = usersinresults.entrySet();
		for ( Entry<String, List<String>> entry : entrySet )
		{
			String username = entry.getKey();
			List<String> tweets = entry.getValue();

			for ( String tweet : tweets )
			{
				FileUtil.writeToFile( new File( path + outdir + username + ".dat" ), tweet );
			}

		}
	}

	private static void search()
	{

		Twitter twitter = TwitterAuth.getInstance();
		String s = "mixer/";

		try
		{
			String queryStr = "mixer";
			Query query = new Query( queryStr );
			query.setCount( 100 );

			QueryResult result = twitter.search( query );

			int count = 1;
			int counteng = 1;
			do
			{
				List<Status> tweets = result.getTweets();
				for ( Status tweet : tweets )
				{
					count++;
					// if (tweet.getLang().equals( "en" ))
					{
						counteng++;
						String twttxt = tweet.getText().replace( "\n", " " );
						System.out.println( "Tweet: " + twttxt + tweet.getCreatedAt() );

						Gson gson = new Gson();
						String json = gson.toJson( tweet );

						FileUtil.writeToFile( new File( path + s + queryStr + "2.dat" ), twttxt );
					}

				}
				System.out.println( count + " tweets retreived" );
				query = result.nextQuery();
				if ( query != null )
				{
					try
					{
						Thread.sleep( 3000 );
					}
					catch ( InterruptedException e )
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result = twitter.search( query );
				}
			}
			while ( query != null );

			System.out.println( count + " eng tweets = " + counteng );

		}
		catch ( TwitterException te )
		{
			te.printStackTrace();
			System.out.println( "Failed to search tweets: " + te.getMessage() );
			System.exit( -1 );
		}
	}

	// private static void getTimeline()
	// {
	// try
	// {
	//
	// Twitter twitter = getInstance();
	// // gets Twitter instance with default credentials
	// // Twitter twitter = new TwitterFactory().getInstance();
	// User user = twitter.verifyCredentials();
	// List<Status> statuses = twitter.getHomeTimeline();
	// System.out.println( "Showing @" + user.getScreenName() + "'s home timeline." );
	// for ( Status status : statuses )
	// {
	// System.out.println( "@" + status.getUser().getScreenName() + " - " + status.getText() );
	// }
	// }
	// catch ( TwitterException te )
	// {
	// te.printStackTrace();
	// System.out.println( "Failed to get timeline: " + te.getMessage() );
	// System.exit( -1 );
	// }
	// }

}
