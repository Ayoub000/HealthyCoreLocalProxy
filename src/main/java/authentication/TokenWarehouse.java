package authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TokenWarehouse {
	
	private Map<String, TokenUnit> tokenMap = new HashMap<>();
	
	private static TokenWarehouse instance = new TokenWarehouse();
	public static TokenWarehouse getInstance()
	{
		return instance;
	}
	
	public String addToken(long userId)
	{
		String content = UUID.randomUUID().toString().replace("-", "");
		tokenMap.put(content, new TokenUnit(userId, content, 
										   System.currentTimeMillis() + 11 * 60 * 1000));
		return content;
	}
	
	public boolean isValidToken(String tokenContent)
	{
		return tokenMap.containsKey(tokenContent);
	}
	
	public boolean isExpiredToken(String tokenContent)
	{
		TokenUnit token = tokenMap.get(tokenContent);
		if(token.getExpiration() <= System.currentTimeMillis())
		{
			tokenMap.remove(tokenContent);
			return true;
		}
		return false;
	}

}
