<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
	Width <input type='number' min=2 max=10 id='row' value="4" />
	Height <input type='number' min=2 max=10 id='col' value="4"  />
	<input type='button' value='Start new game :)' onclick="stones_newgame();" />
<br />
<!--runnable 
	function run_game_${GameIDi}(){stones_newgame();} 
	function action_game_${GameIDi}(son){stones_parse(son);} 
-->