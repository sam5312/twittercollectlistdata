package proj.data.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import proj.conf.Paths;
import proj.utils.FileUtil;
import proj.utils.TweetProcessingUtil;
import twitter4j.JSONException;
import twitter4j.JSONObject;

/**
 * replaces slangs in tweet text with their propoer english name
 * slang list is taken from, http://www.noslang.com/dictionary/
 * 
 * saves the tweet text to lists_preprssed directory
 * @author sam
 *
 * 9 Oct 2015	11:30:08 am
 */
public class RemoveSlangsFromTweets
{
	static Map<String,String> map = new HashMap<>();
	
	public static void main(String args[])
	{
		System.out.println( "started" );
		readSlangFiles();
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
				File f = new File( Paths.ROOT_DIR + Paths.LISTS + listFile ); // open folder
				File[] files = f.listFiles(); // files inside the folder

				for ( File uName : files ) // each file contains tweets of a particular user
				{
					ArrayList<String> lines = new ArrayList<>();
					FileUtil.readLines( uName, lines );
					removeSlags( lines, listFile, uName.getName() );

				}
			}

		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
	}

	private static void removeSlags( ArrayList<String> lines , String ln, String un)
	{
		System.out.println( "processing list: " + ln + " and user: " + un );
		for ( String tweetTxt : lines )
		{
			try
			{
				String textFld = TweetProcessingUtil.getTweetText( tweetTxt );
				textFld.replaceAll( "\n", " " ); // replace new lines with spaces

				String[] splits = textFld.split( " " );

				StringBuilder sb = new StringBuilder();

				for ( String token : splits )
				{
					token = token.trim();
					// token = token.toLowerCase();
					if ( map.containsKey( token ) ) // chk if the token is a slang
					{
						String string = map.get( token );
						sb.append( string ).append( " " );
						System.out.println( token + " | " + string );
					}
					else
					{
						sb.append( token ).append( " " );
					}
				}
				FileUtil.writeToFile( new File( Paths.ROOT_DIR + Paths.LISTS_PRO + Paths.NOSLANG + ln + "/" + un ), sb.toString() );
			}
			catch ( JSONException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void readSlangFiles()
	{
		File[] slangfiles = new File(Paths.ROOT_DIR + Paths.SLANGS).listFiles();
		for ( File file : slangfiles )
		{
			ArrayList<String> lines = new ArrayList<>();
			FileUtil.readLines( file, lines );
			addToMap(lines);
		}
	}

	private static void addToMap( ArrayList<String> lines )
	{
		for ( String line : lines )
		{
			if ( line.length() > 0 )
			{
				String[] splits = line.split( "-" );
				if ( splits.length > 2 )
				{
					// do nothing
				}
				else
				{
					String slang = splits[0].trim();///.toLowerCase();
					String engword = splits[1].trim(); //.toLowerCase();
					map.put( slang, engword );

				}
			}
		}
	}
	
	
}
