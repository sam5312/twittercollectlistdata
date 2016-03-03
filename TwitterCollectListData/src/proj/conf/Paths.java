package proj.conf;
/**
 * contains variables used in the code
 * @author sam
 *
 * 6 Oct 2015	1:18:12 pm
 */
public class Paths
{

	/**
	 * naming convention: if a path ends in a folder, end the string with '/' 
	 */
	
	
	/*
	 * data collected from apis and everything will be written under this folder
	 */
	public static final String ROOT_DIR = "/media/sam/Sam/twitter_personalization/";
	
	/*
	 * contains files for each list with list members as lines
	 */
	public static final  String LISTS = "lists/";
	
	public static final String LISTS_PRO = "lists_preprssed/";
	
	/*
	 * contains files containing slags downloaded from the web
	 */
	public static final String SLANGS = "slangs/";
	
	/**
	 * log files go here
	 */
	public static final String LOGS = "logs/";
	
	/*
	 * location of tweets after removing slang terms
	 */
	public static final String NOSLANG = "noslang/";
	
	/**
	 * location of tweets after remving stopwords
	 */
	public static final String SWR = "noswnoslang/";
	public static final String NOSWs = "nosws/";

	/**
	 * location of tweets with NEs recognized. haven't converted to lowercase
	 */
	public static final String NES = "withnes/";
	
	/**
	 * contains filtered users based on no of times that particular user's tweets has a high frequ. term appearing
	 */
	public static final String USERFILT = "usersfiltered/";






}
