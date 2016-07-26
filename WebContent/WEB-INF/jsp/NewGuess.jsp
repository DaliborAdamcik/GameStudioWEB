<%@ page language="java" contentType="text/html; charset=windows-1250"
    pageEncoding="windows-1250"%>
Width <input type='number' min=10 max=100 id='guess_maxnum' value="20" />
<input type='button' value='Start new game :)' onclick="guess_newgame(document.getElementById('guess_maxnum').value);" />
<br />
<h2 id="iguess"></h2>