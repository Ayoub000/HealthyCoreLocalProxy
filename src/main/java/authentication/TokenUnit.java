package authentication;

public class TokenUnit {

	private long id;
	private String content;
	private long expiration;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getExpiration() {
		return expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	
	public TokenUnit(long id, String content, long expiration) {
		super();
		this.id = id;
		this.content = content;
		this.expiration = expiration;
	}
	
	
}
