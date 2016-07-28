<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="sk.tsystems.gamestudio.services.jdbc.StatisticsDTO.*" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View</title>
    </head>
    <body>
    	<h1>Štatistiky</h1>        
        <b>Počet užívateľov:</b> ${stats.getUserCount()} <br>
        <b>Počet hier:</b> ${stats.getGameCount()} <br>
        <b>Počet hraní hier:</b> ${stats.getScoreCount()} <br>
        <b>Počet hodnotení:</b> ${stats.getRatingCount()} <br>
        <b>Priemerné hodnotenie štúdia:</b> ${stats.getAvgRatingGS()} <br>


        <c:forEach items="${stats.getGamestat()}" var="game">
            <tr>
                <td>${student.firstName}
                <td>${student.lastName}
            </tr>
        </c:forEach>
        
    </body>
</html>


	public List<GSStatPlayer> getMostactiveplayers() {
		return mostactiveplayers;
	}

	public List<GSStatGame> getGamestat() {
		return gamestat;
	}
