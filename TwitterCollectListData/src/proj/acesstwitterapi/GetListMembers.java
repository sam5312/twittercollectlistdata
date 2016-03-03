package proj.acesstwitterapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import proj.conf.Paths;
import proj.utils.FileUtil;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * @author sam 30 Sep 2015
 * used to get members of a twitter list as given in the properties file. saves the list members to teh file system for each list
 */
public class GetListMembers
{
	private static Twitter twitter = TwitterAuth.getInstance();
	
	public static void main( String args[] ) throws Exception
	{
		readPropertiesFile( "twitterlists.properties" );
	}

	private static void readPropertiesFile( String file ) throws Exception
	{
		FileInputStream fileInput = new FileInputStream( new File( file ) );
		Properties properties = new Properties();
		properties.load( fileInput );
		fileInput.close();

		Enumeration enuKeys = properties.keys();
		
		while ( enuKeys.hasMoreElements() )
		{
			String username = ( String ) enuKeys.nextElement();
			String slug = properties.getProperty( username );
			System.out.println( "crawling" + username + ": " + slug );
			crawlThisList( username, slug );
		}
	}

	private static void crawlThisList(String uname, String slug) throws TwitterException
	{
		PagableResponseList<User> userListMembers = twitter.getUserListMembers( uname, slug, 5000, -1 );
//		PagableResponseList<User> userListMembers = twitter.getUserListMembers( uname, slug, -1 );

		System.out.println("number of members:" +  userListMembers.size() );

		String path = Paths.ROOT_DIR + Paths.LISTS + slug;
		File f = new File( path );
		FileUtil.delteFile( f); //delete if it already exists

		//write the screenames of the users in that list into the file system where file is identified by the list slug
		for ( User user : userListMembers )
		{
			FileUtil.writeToFile( f, user.getScreenName() );
		}	
		
//		long nextCursor = userListMembers.getNextCursor();
//		userListMembers = twitter.getUserListMembers( "davidebradford", "Tech", nextCursor );
//		// System.out.println( userListMembers.size() );
//		System.out.println( userListMembers.get( 0 ) );
//		System.out.println( userListMembers.get( userListMembers.size() - 1 ) );


	}

}
