package proj.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import proj.utils.MapUtils.C1;
import twitter4j.JSONException;
import twitter4j.JSONObject;

/**
 * This class contains common utility methods used to process tweets ( like reading from JSON objects, writing to a file, etc )
 * 
 * @author sam 05/02/2015
 */
public class TweetProcessingUtil
{

	public static String getTweetCreatedAt( String tweetJson ) throws Exception
	{

		JsonParser parser = new JsonParser();
		JsonObject jsonObj = ( JsonObject ) parser.parse( tweetJson );

		JsonElement jsonDateEle = jsonObj.get( "created_at" );

		String s = getFormattedDateTime( jsonDateEle.getAsString() );
		return s;

	}
	
	public static String getTweetText( String tweetJson ) throws JSONException 
	{

		JSONObject jo = new JSONObject( tweetJson );
		String textFld = (String)jo.get( "text" );
		return textFld;

	}

	public static Map<String,C1> map = MapUtils.getNewMap();
	/**
	 * cleans the input string and returns the cleaned input. if the clenaned string contains less than 3 words empty string is returned. so check for
	 * empty should be performed at the calling end. removes stopwords as well.
	 * 
	 * @param tweet
	 * @param convertToLower is true, text will be converted to lowercase. if fasle, it preserves the case
	 * @return
	 */
	public static String processTweetText( String tweet, boolean converToLower )
	{
		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> tokensBefroeProcessing = tokenize( tweet);
		ArrayList<String> tokens = new ArrayList<String>();

		

		for ( String word : tokensBefroeProcessing )
		{

			String remLeading = word.replaceFirst( "^[^#@0-9a-zA-Z]+", "" ); // remove any character other than @0-9 or a-zA-Z from the begining e.g.
																				// @abcd -> @abcd, #abcd->abcd
			String remTrailing = remLeading.replaceAll( "[^0-9a-zA-Z]+$", "" ); // remove any character otehr than @0-9 or a-zA-A from the begining

			if ( remTrailing.length() >= 2 && isAsciiPrintable( remTrailing ) )
			{
				tokens.add( remTrailing );
			}
		}

		for ( int i = 0; i < tokens.size(); i++ )
		{
			String tmpToken;
			if (converToLower)
				tmpToken = tokens.get( i ).toLowerCase();
			else
				tmpToken = tokens.get( i) ;

			boolean isStw = Stopwords.isStopword( tmpToken.toLowerCase() ); //compare with the lowercase when checking for stopwords
			if(isStw)//if that word is a stopword
			{
				MapUtils.IncrementThisUnitInTheMap( tmpToken, map, "" );
			}
			else if( !isNoisy( tmpToken ) )
			{
				words.add( tmpToken );
			}
		}

		// if the tweet contains less than 3 words disregard that tweet
		if ( words.size() < 3 )
		{
			return "";
		}

		StringBuilder builder = new StringBuilder();
		for ( String value : words )
		{
			builder.append( value ).append( " " );
		}

		return builder.toString();
	}
 
	/**
	 * String Operations
	 */
	public static ArrayList<String> tokenize( String line)
	{
		 ArrayList<String> tokens = new ArrayList<String>();
		StringTokenizer strTok = new StringTokenizer( line );
		while ( strTok.hasMoreTokens() )
		{
			String token = strTok.nextToken();
			tokens.add( token );
		}
		return tokens;
	}

	/**
	 * removes mentions (@abc) and url strings (http://blahblah)
	 * 
	 * @param token
	 * @return
	 */
	private static boolean isNoisy( String token )
	{

		if ( token.toLowerCase().contains( "#pb#" ) || token.toLowerCase().startsWith( "http:" ) || token.toLowerCase().startsWith( "https:" ) )
			return true;
		if ( token.startsWith(  "@" ) ) // will remove mentions in tweets
		{
			return true;
		}
		return token.matches( "[\\p{Punct}]+" );
	}

	private static boolean isAsciiPrintable( String str )
	{
		if ( str == null )
		{
			return false;
		}
		int sz = str.length();
		for ( int i = 0; i < sz; i++ )
		{
			if ( isAsciiPrintable( str.charAt( i ) ) == false )
			{
				return false;
			}
		}
		return true;
	}

	private static boolean isAsciiPrintable( char ch )
	{
		return ch >= 32 && ch < 127;
	}

	private static String getFormattedDateTime( String createdAt ) throws ParseException
	{
		String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

		SimpleDateFormat sf = new SimpleDateFormat( TWITTER );
		Date d = sf.parse( createdAt );
		String date = String.valueOf( d.getDate() );

		if ( date.length() == 1 )
		{
			date = "0" + date;
		}

		int month = d.getMonth();
		String[] mm = new java.text.DateFormatSymbols().getShortMonths();

		return date + "_" + mm[month];
	}

}
