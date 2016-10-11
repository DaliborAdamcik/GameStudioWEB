<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="sk.tsystems.gamestudio.services.jdbc.StatisticsDTO.*" %>

<div id="stats">
<h2>Statistics</h2>
<b>User count:</b> ${stats.getUserCount()} <br>
<b>Game count:</b> ${stats.getGameCount()} <br>
<b>Games played:</b> ${stats.getScoreCount()} <br>
<b>Rating count:</b> ${stats.getRatingCount()} <br>
<b>Average rating:</b> ${stats.getAvgRatingGS()} (for all games / GameStudio)<br><br/>

<table>
	<tr>
		<th colspan="9">Game statistics
	</tr>
	<tr>
		<th rowspan="2">ID
		<th rowspan="2">Name
		<th rowspan="2">Run<br>count
		<th rowspan="2">Commented
		<th colspan="3">Rating
		<th colspan="3">Score achieved
	</tr>
	<tr>
		<th>Count
		<th>For game
		<th>Average GS
		<th>Min<br>
		<th>Max<br>
	</tr>
    <c:forEach items="${stats.getGamestat()}" var="game">
        <tr>
            <td>${game.getGid()}
            <td>${game.getName()}
            <td>${game.getRunCount()}
            <td>${game.getCommentCnt()}
            <td>${game.getRatingCount()}
            <td>${game.getRatingGame()}
            <td>${game.getRatingGS()}
            <td>${game.getScoreMin()}
            <td>${game.getScoreMax()}
        </tr>
    </c:forEach>
</table>
<br/>
<table>
	<tr>
		<th colspan="9">Most active player(s)
	</tr>
	<tr>
		<th>ID
		<th>Name
		<th>Game run count
	</tr>
       <c:forEach items="${stats.getMostactiveplayers()}" var="player">
           <tr>
               <td>${player.getUid()}
               <td>${player.getName()}
               <td>${player.getPlays()}
           </tr>
       </c:forEach>
</table>
</div>
