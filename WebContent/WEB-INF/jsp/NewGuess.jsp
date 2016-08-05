<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1250" pageEncoding="windows-1250"%>

Width <input type='number' min=10 max=100 id='guess_maxnum' value="100" />
<input id="rung_${GameIDi}" type='button' value='Start new game :)' onclick="guess_newgame(document.getElementById('guess_maxnum').value);" />
<br />
<h2 id="iguess"></h2>
<!--runnable 
	function run_game_${GameIDi}(){guess_newgame(document.getElementById('guess_maxnum').value);} 
	function action_game_${GameIDi}(son){guess_parse(son);} 
$-->
<!--linkscript guess.js #-->
<!--linkcss /-->