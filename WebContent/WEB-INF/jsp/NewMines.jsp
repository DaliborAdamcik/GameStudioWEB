<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
Width <input type='number' min=2 max=10 id='mine_row' value="10" />
Height <input type='number' min=2 max=10 id='mine_col' value="10"  />
Mines <input type='number' min=2 max=10 id='mine_mine'  value="10" />
<input type='button' value='Start new game :)' onclick="mines_newgame();" />
<ul>
	<li>Unmarked mines: <span id="unmark"></span></li>
	<li>State: <span id="state"></span></li>
</ul>
<!--runnable 
	function run_game_${GameIDi}(){mines_newgame();} 
	function action_game_${GameIDi}(son){mines_parse(son);} 
$-->
<!--linkscript mines.js #-->
<!--linkcss /-->