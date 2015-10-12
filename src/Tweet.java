import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Tweet {
	private ArrayList<String> hashtags;
	private long userId;
	
	Tweet () {
		hashtags = new ArrayList<String>();
		userId = 0;
	}
	
	Tweet(ArrayList<String> hashtags, long userId) {
		for (String hashtag : hashtags) {
			this.hashtags.add(hashtag);
		}
		
		this.userId = userId;
	}
	
	Tweet(ArrayList<String> hashtags) {
		this(hashtags, 0);
	}
	
	Tweet(String hashtagsString, Boolean withSpaces, long userId) {
		this.userId = userId;
		hashtags = new ArrayList<String>();
		
		Pattern p;
		if (withSpaces)
			p = Pattern.compile("\"text\": \"(.*?)\"");
		else
			p = Pattern.compile("\"text\":\"(.*?)\"");
        Matcher m = p.matcher(hashtagsString);
        
        while(m.find())
            addHashtag(m.group(1));
	}
	
	Tweet(String hashtagsString, Boolean withSpaces) {
		hashtags = new ArrayList<String>();
		
		Pattern p;
		if (withSpaces)
			p = Pattern.compile("\"text\": \"(.*?)\"");
		else
			p = Pattern.compile("\"text\":\"(.*?)\"");
        Matcher m = p.matcher(hashtagsString);
        
        while(m.find())
            addHashtag(m.group(1));
	}
	
	public void addHashtag(String hashtag) {
		String s = hashtag.toLowerCase();
		if (s.contains("\\")) {
			int oldI = -1;
			int i = -1;
			StringBuffer buf = new StringBuffer();
			do {
				oldI = i;
				i = s.indexOf('\\', i);
				if (i != -1) {
					buf.append(s.substring(0, i)); // Add start
					String c = s.substring(i+2, i+6);
					int v = Integer.parseInt(c, 16);
					if (v >= 192 && v <= 197)
						buf.append("A");
					else if (v == 198)
						buf.append("AE");
					else if (v == 199)
						buf.append("C");
					else if (v >= 200 && v <= 203)
						buf.append("E");
					else if (v >= 204 && v <= 207)
						buf.append("I");
					else if (v == 209)
						buf.append("N");
					else if (v >= 210 && v <= 214)
						buf.append("O");
					else if (v == 216)
						buf.append("O");
					else if (v >= 217 && v <= 220)
						buf.append("U");
					else if (v >= 224 && v <= 229)
						buf.append("a");
					else if (v == 230)
						buf.append("ae");
					else if (v == 231)
						buf.append("c");
					else if (v >= 232 && v <= 235)
						buf.append("e");
					else if (v >= 236 && v <= 239)
						buf.append("i");
					else if (v == 241)
						buf.append("n");
					else if (v >= 242 && v <= 246)
						buf.append("o");
					else if (v == 248)
						buf.append("o");
					else if (v >= 249 && v <= 252)
						buf.append("u");
					else {
						if (c.startsWith("00")) // Otherwise other languages
							System.out.println("ERROR : Unhandled unicode caracter : " + c);
						buf.append(String.valueOf((char) v));
					}
					i += 6;
				} else if (oldI != s.length()) {
					buf.append(s.substring(oldI));
				}
			} while (i != -1);
			
			s = buf.toString();
		}
		
		if (!hashtags.contains(s))
			hashtags.add(s);	
	}
	
	public boolean containsHashtag(String hashtag) {
		for (String hash : hashtags) {
			if (hash.equals(hashtag))
				return true;
		}
		return false;
	}
	
	public ArrayList<String> getList() {
		return hashtags;
	}
	
	public void setList(ArrayList<String> hashtags) {
		this.hashtags = hashtags;
	}
	
	public int getSize() {
		return hashtags.size();
	}
	
	public void printTweet() {
		for (String hashtag : hashtags) 
			System.out.println(hashtag);
	}
	
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(int i) {
		userId = i;
	}
}
