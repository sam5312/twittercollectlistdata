package proj.data.preprocessing;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import proj.conf.Paths;
import proj.utils.FileUtil;
import proj.utils.MapUtils;
import proj.utils.MapUtils.C1;

/**
 * 
 * @author sam
 *
 * 10 Oct 2015	5:15:26 pm
 */
public class CalcSlangUsageStatistics
{
	
	public static void main( String[] args )
	{
		start();
	}

	private static void start()
	{
		ArrayList<String> lines = new ArrayList<String>(  );
		FileUtil.readLines( new File(Paths.ROOT_DIR + "logs/slangoutput.txt"), lines );
		
		Map<String,C1> map = MapUtils.getNewMap();
		
		for ( String line : lines )
		{
			String[] splits = line.split( "\\|" );
			if(splits.length == 2)
			{
				String key = splits[0];
				String val = splits[1];
				
				MapUtils.IncrementThisUnitInTheMap( key, map, val );
//				if(map.containsKey( key ))
//				{
//					C1 c1 = map.get( key );
//					c1.freq++;
//					map.put( key, c1 );
//				}
//				else {
//					C1 c = new C1();
//					c.freq = 1;
//					c.word = val; 
//					map.put( key, c );
//				}
			}
		}
		
		FileUtil.delteFile(  new File(Paths.ROOT_DIR+ Paths.LOGS+"slagfreq.txt"));
//		Object[] array = map.entrySet().toArray();
//	   
//		Arrays.sort(array, new Comparator() {
//	        public int compare(Object o1, Object o2) {
//	            return ((Map.Entry<String, C1>) o2).getValue().freq.compareTo( ((Map.Entry<String, C1>) o1).getValue().freq);
//	        }
//	    });
		
		Object[] array = MapUtils.sortMap( map );
		
		for ( Object e : array )
		{
			String str = ( ( Map.Entry<String, C1> ) e ).getKey() + " : " + ( ( Map.Entry<String, C1> ) e ).getValue().word + " : " + ( ( Map.Entry<String, C1> ) e ).getValue().freq;
			FileUtil.writeToFile( new File(Paths.ROOT_DIR+ Paths.LOGS+"slagfreq.txt"), str );
		}
		
	}
	

}

