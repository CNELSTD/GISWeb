<%@ page import="java.util.*" import="java.text.Format"
	import="java.text.SimpleDateFormat"%>
<%
	HttpSession sesion = request.getSession();
	if (sesion.getAttribute("username") == null)
		response.sendRedirect("./?p=login");
	Format date_format = new SimpleDateFormat("yyyy-MM-dd");
	Format date_format2 = new SimpleDateFormat("yyyyMMdd");
	Date date = new Date();
	String fecha = date_format.format(date);
	String fecha2 = date_format2.format(date);

	String myip = request.getRequestURL().toString();
	String ip = myip.substring(0, myip.lastIndexOf(':'));
	String host = (ip.indexOf("190.") != -1 ? "190.90.187.227:8501" : "172.18.126.7");
	String qr = (ip.indexOf("190.") != -1 ? "qr_bm_public.png" : "qr_bm_private.png");
	String app = (ip.indexOf("190.") != -1 ? "190.90.187.227:0808" : "172.18.115.94:8080");
%>


<br />
<div class="row">
	<div class="col-md-2"></div>
	<div class="col-md-8">
		<div class="btn-group pull-right" style="top: 15%;">

			<a href="#" class="btn btn-primary" id="about"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-question"></i> Acerca </span>
			</a>
			<a href="#" class="btn btn-primary" id="btnRevisar"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-gear"></i> Revisar Diferencias </span>
			</a>
			<!-- <a href="#" class="btn btn-primary" data-toggle="modal"
				data-target="#myModalGroup"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-gear"></i> Proyectos </span>
			</a> <a href="#" class="btn btn-primary" id="btnGrupos"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-users"></i> GRUPOS </span>
			</a> -->
			<a href="#" class="btn btn-primary" data-toggle="modal"
				data-target="#myModal"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-download"></i> DESCARGAR APP </span>
			</a> <a href="?p=logout" class="btn btn-primary"> <span
				style="color: #F2F2F2; display: block; padding: 3px;"><i
					class="fa fa-times"></i></span>
			</a>
		</div>
		<header>
			<a href="#" onclick="redirect()"><img alt=""
				src="images/gis_logo.png" style="width: 10%; height: 10%; top: 30%;">
			</a> GIS WEB
		</header>
		<br /> <br />
		<div id="content">
			<div class="row">
				<div>

					<!-- Nav tabs -->
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a href="#stats"
							id="estadisticas" aria-controls="stats" role="tab"
							data-toggle="tab"><i class="fa fa-bar-chart"></i>
								ESTADISTICAS </a></li>
						<li role="presentation"><a href="#work" aria-controls="work"
							role="tab" data-toggle="tab"><i class="fa fa-list-ul"></i>
								ASIGNAR TRABAJO </a></li>
						<li role="presentation"><a href="#sync" aria-controls="sync"
							role="tab" data-toggle="tab"><i class="fa fa-refresh"></i>
								SINCRONIZAR </a></li>
						<li role="presentation"><a href="#report"
							aria-controls="report" role="tab" data-toggle="tab"><i
								class="fa fa-file-excel-o"></i> REPORTE </a></li>
						<li role="presentation"><a href="#actualizar"
							id="ver-data-actualizar" aria-controls="actualizar" role="tab"
							data-toggle="tab"><i class="fa fa-upload"></i> ACTUALIZAR
								DATA SIG </a></li>
						<li role="presentation"><a href="#fotos" id="lista-postes"
							aria-controls="fotos" role="tab" data-toggle="tab"><i
								class="fa fa-picture-o"></i> FOTOS </a></li>
					</ul>

					<!-- Tab panes -->
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane active" id="stats"
							align="center">
							<br>
							<div class="row-fluid">
								<div class="col-md-12">
									<div id="chartStats3" align="left" class="alert alert-info"></div>
								</div>
							</div>
							<div class="row-fluid">
								<div class="col-md-7">
								<div id="chartStats1" ></div>
								</div>
								<div class="col-md-5">
								<div id="chartStats2" ></div>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="work" align="center">
							<br />
							<p style="padding: 5px 10px">Seleccione la Subestación, el
								Alimentador y asigne el trabajo al Grupo.</p>
							<br />
							<form class="form-horizontal">
								<div class="form-group">
									<label class="col-sm-2 control-label">Subestaciones:</label>
									<div class="col-sm-10">
										<select class="form-control" id="select-subestaciones"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Alimentadores:</label>
									<div class="col-sm-10">
										<select class="form-control" id="select-alimentadores"></select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Grupos:</label>
									<div class="col-sm-10">
										<select class="form-control" id="select-grupos">
										</select>
									</div>
								</div>
								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10">
										<button type="button" class="btn btn-success" id="btnAsignar">
											<i class="fa fa-check"></i> Asignar
										</button>
									</div>
								</div>
							</form>
						</div>
						<div role="tabpanel" class="tab-pane" id="sync" align="center">
							<br />
							<p style="padding: 5px 10px">
								Descarga el archivo <strong>scriptGIS.sql</strong> y
								c&aacute;rgalo en la <strong>Memoria Interna</strong> de tu
								Dispositivo con Android
							</p>

							<form id="syncForm" action="./DownloadScript" method="post"
								name="syncForm" class="form-horizontal">
								<div class="form-group">
									<label class="col-sm-2 control-label">Grupos:</label>
									<div class="col-sm-10">
										<select class="form-control" id="select-grupos2" name="grupo">
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-sm-2 control-label">Asignaciones:</label>
									<div class="col-sm-10">
										<select class="form-control" id="select-asignaciones"
											name="asignacion">
										</select>
									</div>
								</div>
								<br />
								<p>
									<button type="submit" class="btn btn-primary btn-lg">
										<i class="fa fa-file-o"></i> Archivo SQL
									</button>
								</p>
							</form>

							<p style="padding: 5px 10px">
								Seleccion el archivo <strong>scriptGIS_GX.sql</strong> y subalo
							</p>

							<form id="uploadForm" action="./jsp/upload.jsp" method="post"
								name="UploadSyncForm" enctype="multipart/form-data"
								class="form-horizontal">
								<div class="form-group">
									<label class="col-sm-2 control-label">Buscar Archivo:</label>
									<div class="col-sm-10">
										<input type="file" class="form-control" name="uploadfile"
											value="Examinar" />
									</div>
								</div>
								<br />
								<p>
									<button type="submit" class="btn btn-success btn-lg">
										<i class="fa fa-file-o"></i> Subir Archivo SQL
									</button>
								</p>
							</form>
						</div>

						<div role="tabpanel" class="tab-pane" id="report">
							<br />
							<p style="padding: 5px 10px">Seleccione el grupo, detalle un
								rango de fechas y obtenga un reporte detallado del trabajo
								realizado por dicho grupo</p>
							<div class="row">
								<div class="col-md-6">
									<form id="reportForm" action="./Reporteria" method="post"
										name="reportForm" class="form-horizontal">
										<input type="hidden" id="opcion" name="opcion"
											value="reporte1">
										<div class="form-group">
											<label class="col-sm-2 control-label">Grupos:</label>
											<div class="col-sm-10">
												<select class="form-control" id="select-grupos3"
													name="grupo">
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label">Desde:</label>
											<div class="col-sm-10">
												<input type="text" id="desde" name="desde"
													placeholder="Rango desde" class="form-control"
													readonly="readonly" required="required">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label">Hasta:</label>
											<div class="col-sm-10">
												<input type="text" id="hasta" name="hasta"
													placeholder="Rango hasta" class="form-control"
													readonly="readonly" required="required">
											</div>
										</div>
										<br />
										<div class="form-group">
											<label class="col-sm-2 control-label"></label>
											<div class="col-sm-10">
												<button type="submit" class="btn btn-success btn-lg">
													<i class="fa fa-file-o"></i> Reporte Subidas Parciales
												</button>
											</div>
										</div>
									</form>
								</div>

								<div class="col-md-6">
									<form id="reportForm" action="./Reporteria" method="post"
										name="reportForm" class="form-horizontal">
										<input type="hidden" id="opcion" name="opcion"
											value="reporte2">
										<div class="form-group">
											<label class="col-sm-2 control-label">Grupos:</label>
											<div class="col-sm-10">
												<select class="form-control" id="select-grupos4"
													name="grupo">
												</select>
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label">Desde:</label>
											<div class="col-sm-10">
												<input type="text" id="desde2" name="desde"
													placeholder="Rango desde" class="form-control"
													readonly="readonly" required="required">
											</div>
										</div>
										<div class="form-group">
											<label class="col-sm-2 control-label">Hasta:</label>
											<div class="col-sm-10">
												<input type="text" id="hasta2" name="hasta"
													placeholder="Rango hasta" class="form-control"
													readonly="readonly" required="required">
											</div>
										</div>
										<br />
										<div class="form-group">
											<label class="col-sm-2 control-label"></label>
											<div class="col-sm-10">
												<button type="submit" class="btn btn-success btn-lg">
													<i class="fa fa-file-o"></i> Reporte Subidas Totales
												</button>
											</div>
										</div>

									</form>
								</div>
							</div>
						</div>
						<div role="tabpanel" class="tab-pane" id="actualizar"
							align="center">
							<br />
							<div id="labelActualizar" align="left">
								<p class="bg-info">
									<strong>Lista de Poste para actualizar en el SIG</strong>
								</p>
							</div>
							<button class="btn btn-success" id="btnActualizarData">
								<i class="fa fa-upload"></i> Actualizar Data SIG
							</button>
							<br /> <br />
							<div id="registros"></div>
						</div>
						<div role="tabpanel" class="tab-pane" id="fotos">
							<br />
							<div id="labelPostes" align="left">
								<p class="bg-info">
									<strong>Lista de Poste</strong>
								</p>
							</div>
							<br />
							<div class="row">
								<div class="col-md-2">
									<p>Opciones de busqueda</p>
									<select id="select-opcion-busqueda" class="form-control">
										<option value="1">Por fechas y grupo</option>
										<option value="2">Por número de poste</option>
									</select>
								</div>

								<div class="col-md-10">
									<p style="color: red;">*Todos los campos son requerido</p>
									<div>
										<form class="form-inline">
											<div id="opciones-busqueda_1">
												<div class="form-group">
													<label for="desde">Desde:</label> <input type="text"
														class="form-control" id="txtDesde" placeholder="Desde"
														readonly="readonly">
												</div>
												<div class="form-group">
													<label for="hasta">Hasta:</label> <input type="email"
														class="form-control" id="txtHasta" placeholder="Hasta"
														readonly="readonly">
												</div>
												<div class="form-group">
													<label for="hasta">Grupo:</label> <select
														class="form-control" id="select-grupos5"></select>
												</div>
												<div class="form-group">
													<label for="hasta">Estado:</label> <select
														class="form-control" id="select-estados">
														<option value="ACTUALIZACION">ACTUALIZACION</option>
														<option value="ACTUALIZA-SIG">ACTUALIZA-SIG</option>
														<option value="INGRESO">INGRESO</option>
														<option value="TODOS">TODOS</option>
													</select>
												</div>&nbsp;
												<div class="form-group">
													<input type="checkbox" alt="Registros solo con fotos" id="chk_solo_fotos" title="Registros solo con fotos" class="form-control chk_solo_fotos">
												</div>&nbsp;
												<button type="button" class="btn btn-primary"
													id="btnBusquedaXOpciones1Admin">
													<i class="fa fa-search"></i> Buscar Postes
												</button>
											</div>
											<div id="opciones-busqueda_2" style="display: none;">
												<div class="form-group">
													<label for="numPoste">Número de Poste:</label> <input
														type="number" class="form-control" id="txtNumPoste"
														placeholder="# de Poste">
												</div>
												<button type="button" class="btn btn-primary"
													id="btnBusquedaXOpciones2Admin">
													<i class="fa fa-search"></i> Buscar Postes
												</button>
											</div>

										</form>
									</div>
									<!-- <div id="opciones-busqueda_2" style="display: none;">
									<p>Por número de poste</p>
									<form class="form-inline">
										<div class="form-group">
											<label for="numPoste">Número de Poste:</label> <input
												type="number" class="form-control" id="txtNumPoste"
												placeholder="# de Poste">
										</div>
										<button type="button" class="btn btn-primary"
											id="btnBusquedaOpcion2">
											<i class="fa fa-search"></i> Buscar Postes
										</button>
									</form>
								</div>-->
								</div>

							</div>
							<br /> <br />
							<div id="postes"></div>
							<div id="uploadModal"></div>
							<div id="verElementosModal"></div>
						</div>
					</div>

				</div>

			</div>
		</div>
	</div>
	<div class="col-md-2"></div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">Modal title</h4>
				</div>
				<div class="modal-body" align="center">
					DESCARGE EL INSTALADOR DE LA APP PARA ANDROID HACIENDO CLIC EN EL
					BOTON DE ABAJO
					<h3>Aplicación</h3>
					<a href="apk/gismovil.apk" class="btn btn-warning" target="_top"><i
						class="fa fa-download"></i> GIS MOVIL para Android</a> <br> <br>
					<p>Fecha Última actualización: 18 - SEPTIEMBRE - 2017</p>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				</div>
			</div>
		</div>
	</div>
	
	<div id="acercaModal"></div>

	<script>
function redirect() {
    location.reload();
}
</script>

</div>




