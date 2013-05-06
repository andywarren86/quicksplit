<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- Styles -->
<link type="text/css" href="css/style.css" rel="stylesheet" />
<link type="text/css" href="css/ui-lightness/jquery-ui-1.10.0.custom.css" rel="stylesheet" />

<!-- jQuery -->
<script type="text/javascript" src="http://code.jquery.com/jquery-1.9.0.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.10.0.custom.min.js"></script>
<script type="text/javascript" src="js/quicksplit.js"></script>

<!-- Load all players into array because it's just handy -->
<script type="text/javascript">
	var players = [];
	<c:forEach items="${Players}" var="player">
	  players.push( "${player.name}" );
	</c:forEach>
</script>