package api;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.google.gson.Gson;

import authentication.TokenWarehouse;
import authentication.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Authenticate
 */
public class Authenticate extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Authenticate() 
    { }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String credentials = request.getHeader("authorization");
		URL url = new URL("http://localhost:8001/authenticate");
		try
		{
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Authorization", credentials);
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			
			/*String json = "{\"credentials\": \"" + credentials + "\"}";
			try(OutputStream os = con.getOutputStream()) 
			{
			    byte[] input = json.getBytes("utf-8");
			    os.write(input, 0, input.length);			
			}*/
			
			con.setReadTimeout(5000);
			
			try
			{
				if(con.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED)
				{
					try(InputStreamReader isr = new InputStreamReader(con.getInputStream(), "utf-8"))
					{
						try(BufferedReader br = new BufferedReader(isr))
						{
						    StringBuilder masterResp = new StringBuilder();
						    String line = null;
						    while ((line = br.readLine()) != null) 
						    {
						    	masterResp.append(line.trim());
						    }
						    
						    Gson json = new Gson();
							User user = json.fromJson(masterResp.toString(), User.class);
							
							response.setHeader("Authorization", "Basic " + TokenWarehouse.getInstance().addToken(user.getId()));
						}
					}
					response.setStatus(HttpURLConnection.HTTP_ACCEPTED);
				}
				else
				{
					response.setStatus(HttpURLConnection.HTTP_FORBIDDEN);
				}
			}
			catch(SocketTimeoutException e)
			{
				response.setStatus(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
			}
		}
		catch(ConnectException e)
		{
			response.setStatus(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
		}
	}

}
