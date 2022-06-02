<%@ page import="edu.cmu.xuzheng.geoipwebservicelogging.AnalyticResult" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>GeoIP Dashboard</title>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<h1><%= "GeoIP Dashboard" %>
</h1>
<br/>
<h1>Analytics</h1>
<!--https://stackoverflow.com/questions/25962218/how-to-send-list-from-servlet-to-jsp-->
<% AnalyticResult result = (AnalyticResult) request.getAttribute("analytics"); %>
<ol>
    <li>
        <h3>Five Most Recently Searched IP Address: </h3>
        <ul>
            <%
                List<String> IPs = result.getMostRecentFiveSearchedIP();
                for (String IP : IPs) {
            %>
            <li><%=IP%></li>
            <%}%>
        </ul>
    </li>
    <br/>
    <li>
        <h3>Top Five States Where the search IPs Come From: </h3>
        <ul>
            <%
                List<String> states = result.getTopFiveStates();
                for (String state : states) {
            %>
            <li><%=state%></li>
            <%}%>
        </ul>
    </li>
    <br/>
    <li>
        <h3>Top Five Android Systems user use: </h3>
        <ul>
            <%
                List<String> devices = result.getTopFiveUserDevices();
                for (String device : devices) {
            %>
            <li><%=device%></li>
            <%}%>
        </ul>
    </li>
    <br/>
    <li>
        <h3>Average Latency Of this Webservice: </h3>
        <p><%=result.getAverageLatencyOfWebservice()%> milliseconds</p>
    </li>
    <br/>
    <li>
        <h3>Average Latency Of Third Party API(IP Geolocation API): </h3>
        <p><%=result.getAverageLatencyOf3rdAPI()%> milliseconds</p>
    </li>
</ol>
<br/>
<h1>Full Logs</h1>
<table>
    <thead>
    <tr>
        <td>user Android system</td>
        <td>user IP</td>
        <td>searched IP</td>
        <td>timestamp when request received from Android</td>
        <td>timestamp when request sent to third party API</td>
        <td>timestamp when data received from third party API</td>
        <td>timestamp when data sent to Android</td>
        <td>data sent to Android</td>
    </tr>
    </thead>
    <tbody>

    <%
        ArrayList<JSONObject> logs = (ArrayList<JSONObject>) request.getAttribute("logs");
        for (JSONObject log : logs) {
    %>
    <tr>
        <td><%=log.getString("userDevice")%>
        </td>
        <td><%=log.getString("userIP")%>
        </td>
        <td><%=log.getString("paramIP")%>
        </td>
        <td><%=log.getLong("timeReceivedFromAndroid")%>
        </td>
        <td><%=log.getLong("timeSentToAPI")%>
        </td>
        <td><%=log.getLong("timeReceivedFromAPI")%>
        </td>
        <td><%=log.getLong("timeSentToAndroid")%>
        </td>
        <td><%=log.getString("dataSentToAndroid")%>
        </td>
    </tr>
    <%}%>
    </tbody>
</table>
</body>
</html>