<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@ page language="java" contentType="text/html; charset=windows-1250"  pageEncoding="windows-1250"%>
<style>
#rexy {

}

.hotwo {
	text-align: center;
	border-radius: 0.2em;
	padding: 0.2em;
	margin-bottom: 2em;
	background-color: #ffffe6;
	font-size: 1.1em;
	font-weight: bold;
}

#regs {
text-align: center;
	border-radius: 0.2em;
	padding: 0.2em;
	margin: 0.5em;
	background-color: #ccffcc; 
	font-size: 1.5em;
	font-weight: bold;
}

#rtries {
	border-radius: 0.2em;
	padding: 0.2em;
	margin: 0.5em;
	background-color: #ff99cc; 
	font-size: 1.0em;
}

#rexy>span {
	border-radius: 0.2em;
	padding: 0.2em;
	margin-top: 0.2em;
	display: inline-block;
} 

.rwrd {
	cursor: pointer;
	background-color: #b3e6ff;
} 

.rwrd:HOVER {
	background-color: #33bbff;
} 

.rehit {
	background-color: #99e699;
} 

.remiss {
	background-color: #ff8080;
} 


</style>
<!--runnable 
	function run_game_${GameIDi}(){regexmatch_newgame();} 
	function action_game_${GameIDi}(son){regexmatch_parse(son);} 
$-->
<!--linkscript regexmatch.js #-->
<!--linkcss /-->