package com.teniosAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.DriverManager;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
/**
 * Servlet implementation class ExternalAPI
 */
@WebServlet("/ExternalAPI")
public class ExternalAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;


	public ExternalAPI() {
        // TODO Auto-generated constructor stub
    	super();
    	
    }
    public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
	}
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doPost(request, response);

	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Request Sending:");
		//REQUEST
		StringBuilder sb = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    try {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line).append('\n');
	        }
	    } finally {
	        reader.close();
	    }
	    
	    String strJson = sb.toString();
		
	    try {
			JSONObject jsondata = new JSONObject(strJson);
			System.out.println(jsondata);
			String id = null;
			String jstringcustno = jsondata.getString("customerNumber");
			String jstringaccesskey = jsondata.getString("accessKey");
			String variables = jsondata.getString("variables");
			String allvariables = variables.toString();
			JSONObject jsonvariablesdata = new JSONObject(allvariables);
			String jstringdestNum  = jsonvariablesdata.getString("destination_number");
			String jstringansweredtime  = jsonvariablesdata.getString("answered_time");
			String jstringcallerid  = jsonvariablesdata.getString("caller_id_number");
			String jstringloop = jsondata.getString("loopCount");
			String jstringstatus = jsondata.getString("requestStatus");
			String jstringprocess = jsondata.getString("blocksProcessingResult");
			
			/*Print request variable on the screen
			System.out.println(jstringcustno);
			System.out.println(jstringaccesskey);
			System.out.println(jstringdestNum);
			System.out.println(jstringansweredtime);
			System.out.println(jstringcallerid);
			System.out.println(jstringloop);
			System.out.println(jstringstatus);
			System.out.println(jstringprocess);*/
			
			//Database connection
			try {
				String MyDriver = "com.mysql.jdbc.Driver";
				String db 		="jdbc:mysql://192.168.1.189/Call_Control";
				Class.forName(MyDriver);
				java.sql.Connection conn = DriverManager.getConnection(db, "root", "tenios");
				java.sql.PreparedStatement st = conn.prepareStatement("insert into call_table(id,customerNo,accessKey,destination_number,answered_time,caller_id_number,loopCount,requestStatus,blocksProcessingResult) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				st.setString(1, id);
				st.setString(2, jstringcustno);
				st.setString(3, jstringaccesskey);
				st.setString(4, jstringdestNum);
				st.setString(5, jstringansweredtime);
				st.setString(6, jstringcallerid);	
				st.setString(7, jstringloop);
				st.setString(8, jstringstatus);
				st.setString(9, jstringprocess);
				st.executeUpdate();
				conn.close();
				
		} catch (Exception e) {
			// TODO: handle exception
		System.out.println(e.toString());
		
		}

	    } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    }
	    	//RESPONSE
	    	System.out.println("Response Comming");
	    	response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        String myResponseJson = "{\"blocks\" :[ {"
	        		+ "\"blockType\":ANNOUNCEMENT" 
	        		+","
	        		+ "\"announcementName\":Voicemail_Ansage"
	        		+","
	        		+ "\"standardAnnouncement\":true"
	        		+ "}"
	        		+ "]"
	        		+ "}";
	        try {
				JSONObject jsonObject = new JSONObject(myResponseJson);
				System.out.println(jsonObject);
				out.println(myResponseJson);
				out.close();
	        		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	
}    
	
