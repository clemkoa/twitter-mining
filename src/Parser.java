import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {
	public String fileName;
	public ArrayList<Tweet> tweetList;
	
	public Parser(String fileName) {
		this.fileName = fileName;
		tweetList = new ArrayList<Tweet>();
	}
	
	public void getHashtags() {
		try {			
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			// On first line, detect if there are spaces after each ':'
			Pattern p, patternUser;
			String line = br.readLine();
			boolean withSpace;
			if (line.startsWith("{\"contributors\": ")) {
				withSpace = true;
				p = Pattern.compile("\"hashtags\": \\[(.*)");
				patternUser = Pattern.compile("user\":.*?\"id\": (.*?),");
			} else  {
				withSpace = false;
				p = Pattern.compile("\"hashtags\":\\[(.*)");
				patternUser = Pattern.compile("user\":.*?\"id\":(.*?),");
			}
			
			// Reset stream
			br.close();
			br = new BufferedReader(new FileReader(fileName));
			
			int numLines = 0;
			while ((line = br.readLine()) != null) {			
				//to get the user id
				Matcher matchUser = patternUser.matcher(line);
				long id = 0l;
				if (matchUser.find()) {
					String suce = matchUser.group(1);
					id = Long.parseLong(suce);
				}
				
				
				//hashtags
				Matcher m = p.matcher(line);
				if (m.find() && !m.group(1).startsWith("]")) {
					String s = m.group(1);
					int i = -1;
					int c = 0;
					while (c >= 0) {
						int open = s.indexOf("[", i+1);
						int close = s.indexOf("]", i+1);
						if (open < close) {
							i = open;
							c++;
						} else {
							i = close;
							c--;
						}
					}
					tweetList.add(new Tweet(m.group(1), withSpace, id));
				}
				numLines++;
			}
			
			br.close();	
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/*		Pattern pattern = Pattern.compile("hashtags\":\\[(.*?)\\],");
		Matcher matcher = pattern.matcher(data);
		if (matcher.find()) {
		    return matcher.group(1); 
		}
		return "";*/
	}
}
