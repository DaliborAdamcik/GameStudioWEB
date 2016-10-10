<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%><%@page contentType="text/html" pageEncoding="windows-1250"%>
<script>
function openMe(link) {
	window.location.href = link;
}
</script>

<div id="mainmenu">
	<span onclick="openMe('HourlyStat');">BP All</span> 
	<span onclick="openMe('HourlyStat2g');">BP 2 Game</span> 
<!-- 	<span id="gs_signin" onclick="gs_signin_showdlg();">Sign IN</span>  -->
</div>
