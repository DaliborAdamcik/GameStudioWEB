<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
<input type='button' value='Start new game :)' onclick="mines_newgame();" />
<style>
	#unamarkedmi {
		text-align: center;
		border-radius: 0.2em;
		padding: 0.2em;
		margin-bottom: 2em;
		background-color: #ffffb3;
		font-size: 1.1em;
		font-weight: bold;
	}	
</style>
<div id="unamarkedmi">Unmarked mines: <span id="unmark"></span></div>
<span style="display: none;">State: <span id="state"></span></span>

<!--runnable 
	function run_game_${GameIDi}(){mines_newgame();} 
	function action_game_${GameIDi}(son){mines_parse(son);} 
$-->
<!--linkscript mines.js #-->
<!--linkcss /-->
