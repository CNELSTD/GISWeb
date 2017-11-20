<%
HttpSession sesion=request.getSession();
String status="";
try{
	status=sesion.getAttribute("msg_txt").toString();
	sesion.removeAttribute("msg_txt");
}catch(NullPointerException e){}
if (!status.equals("")){
	%><div class="advise"><%= status %></div><%
}
%>
	<div class="container">
		<div class="info">
			<h1>GIS Web Login</h1>
			<span>Página de Acceso</span>
		</div>
	</div>
	<div class="form">
		<div class="thumbnail">
			<img src="images/gis_logo.png"
				style="bottom: 35%; left: 3%; position: relative;" />
		</div>
		<form class="login-form" name="loginForm" action="./Signin" method="post">
			<input type="hidden" name="app" value="GISWeb" />
			<input type="hidden" name="myapp" value="76" />
			<input type="text" id="username" placeholder="USUARIO" name="username" /> 
			<input type="password" id="pass" placeholder="CONTRASEÑA" name="pass"/>
			<button type="submit">INGRESAR</button>
		</form>
	</div>

	<script type="text/javascript">
$(function(){
	$('#loginForm').submit(function(){
		if ($('#username').val()=='') showMessage('Ingrese el nombre de usuario');
		else if ($('#pass').val()=='') showMessage('Ingrese la contraseña');
		else{
			$('#loginForm .submit').addClass('disabled').text('Entrando...');
			return true;
		}
		return false;
	});
	$('.controls a').tooltip();
	$('#login').draggable({
		appendTo: "body"
	});
	if (navigator.appName == "Microsoft Internet Explorer"){
		$('#login').hide();
		$('#page').append('<div id="firefoxTip" style="right:110px;"><p class="text">Le recomendamos usar estos navegadores</p><span class="arrowTip"></span></div><span class="browser"><a href="http://www.mozilla.com/es-ES/firefox/" title="Firefox"><img src="images/Firefox.png" border="0" /></a><a href="http://www.google.com/chrome/?hl=es" title="Google Chrome"><img src="images/Chrome.png" border="0" /></a></span>');
	}
});
</script>