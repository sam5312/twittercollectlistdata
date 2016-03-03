package proj.acesstwitterapi;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterAuth
{
	/**
	 * authenticate and get a twitter instance
	 * @return
	 */
	public static Twitter getInstance()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("M92qwNxi7sX5OGH3mors4wph1")
		  .setOAuthConsumerSecret("Pm8u9IaZT0YGVnMIY7HcvuqWWN8Mvv6F3imcYmw088jfiEu6I2")
		  .setOAuthAccessToken("174655923-x5OUIGcCWoE6imYF7ExctfSEgdwVBtPXfNv6rHaO") //174655923-znFhPhg7anEApxwFVdl9WqRH5YhoOzk1UsbHWtZb")
		  .setOAuthAccessTokenSecret("qRNrVizZ0BCUWTb1dUwrcWgLr3hyLeI6LGADmdNImCFWm") //dy3gqAGg5AH8GkLD68WtIG9mZ6VQXVfzagTbS3cDqKs8i")
		  .setJSONStoreEnabled( true );
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		return twitter;
	}
}
