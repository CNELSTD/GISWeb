/**
 * 
 */

$('document').ready(function(){

	/*SE INICIALIZA EN FALSE SIEMPRE EL CHECK DE FOTOS*/
	$('.chk_solo_fotos').prop('checked', false);
	
	/*CUADORS ESTADISTICOS*/
	$.ajax({
		type: 'POST',
		url: 'jsp/getEstadisticas.jsp?jsoncallback=?',
		dataType: 'json',
		success: function(data){
			var chart = new CanvasJS.Chart("chartStats1", {
				animationEnabled: true,

				title:{
					text:"ESTADISTICA GENERAL",
					fontSize: 22,
				},
				axisX:{
					interval: 1
				},
				axisY:{
					title: "Estados",
					titleFontSize: 15,
				},
				axisY2:{
					interlacedColor: "rgba(30,186,217,.2)",
					gridColor: "rgba(1,77,101,.1)",
					title: "Progreso de Revisiones",
					titleFontSize: 15,

				},
				data: [{
					type: "bar",
					name: "companies",
					axisYType: "secondary",

					dataPoints: data.estadisticas1
				}]
				/*data: [{
					type: "bar",
					name: "companies",
					axisYType: "secondary",
					color: "#014D65",
					dataPoints: [
						{ y: 3, label: "Sweden" },
						{ y: 7, label: "Taiwan" },
						{ y: 5, label: "Russia" },
						{ y: 9, label: "Spain" },
						{ y: 7, label: "Brazil" },
						{ y: 7, label: "India" },
						{ y: 9, label: "Italy" },
						{ y: 8, label: "Australia" },
						{ y: 11, label: "Canada" },
						{ y: 15, label: "South Korea" },
						{ y: 12, label: "Netherlands" },
						{ y: 15, label: "Switzerland" },
						{ y: 25, label: "Britain" },
						{ y: 28, label: "Germany" },
						{ y: 29, label: "France" },
						{ y: 52, label: "Japan" },
						{ y: 103, label: "China" },
						{ y: 134, label: "US" }
					]
				}]*/
			});
			chart.render();

			var html = '<strong style="font-size:20px;">ESTADISTICA POR GRUPO</strong>'+
			'<br><br>'+
			'<table class="table table-striped">'+
			'<thead>'+
			'<tr>'+
			'<td>GRUPO</td>'+
			'<td>ACTUALIZACION</td>'+
			'<td>ACTUALIZACION SIG</td>'+
			'<td>SIN NOVEDAD</td>'+
			'<td>NUEVOS</td>'+
			'</tr>'+
			'</thead>'+
			'<tbody>';
			$.each(data.estadisticas2, function(i, stats){
				html += '<tr>'+
				'<td>'+stats.grp+'</td>'+
				'<td>'+stats.est1+'</td>'+
				'<td>'+stats.est2+'</td>'+
				'<td>'+stats.est3+'</td>'+
				'<td>'+stats.est4+'</td>'+
				'</tr>';
			});
			html += '</tbody>'+
			'</table>';
			$('#chartStats2').html(html);
			$.each(data.estadisticas3, function(i, stats3){
				$('#chartStats3').html('<strong>Postes Totales: '+stats3.tot+'</strong>');				
			});
		},error: function(data){
			alert('Error al armar data estadistica');
		}
	});
	/*FIN CUADORS ESTADISTICOS*/

	$('#select-subestaciones, #select-alimentadores').html('<option>Cargando...</option>');
	$.ajax({
		type: "POST",
		url: "jsp/getOpciones.jsp?jsoncallback=?",
		dataType: "json",
		success: function(data) {
			var html = '';
			$.each(data.Subestaciones, function(i, item){
				html += '<option value="'+item.cod+'">'+item.nom+'</option>';
			});
			$('#select-subestaciones').html(html);
			$('#select-subestaciones').change(function(){
				var subestacion_id = $(this).val();
				$.each(data.Subestaciones, function(i, subestacion){
					if (subestacion_id==subestacion.cod){
						html = '';
						$.each(subestacion.Alimentadores, function(j, alimentador){
							html += '<option value="'+alimentador.cod+'">'+alimentador.nom+'</option>';
						});
						$('#select-alimentadores').html(html);
					}
				});
			}).trigger('change');
		}
	});

	$('#select-grupos, #select-grupos2').html('<option>Cargando...</option>');
	$.ajax({
		type: "POST",
		url: "jsp/getGrupos.jsp?jsoncallback=?",
		dataType: "json",
		success: function(data) {
			var html = '';
			$.each(data.Grupos, function(i, item){
				html += '<option value="'+item.cod+'">'+item.desc+'</option>';
			});
			$('#select-grupos, #select-grupos2').html(html);
		}
	});

	$('#select-grupos3, #select-grupos4, #select-grupos5 ').html('<option>Cargando...</option>');
	$.ajax({
		type: "POST",
		url: "jsp/getGrupos.jsp?jsoncallback=?",
		dataType: "json",
		success: function(data) {
			var html = '';
			$.each(data.Grupos, function(i, item){
				html += '<option value="'+item.cod+'">'+item.desc+'</option>';
			});
			html += '<option value="TODOS">TODOS</option>';
			$('#select-grupos3, #select-grupos4, #select-grupos5').html(html);
		}
	});



	$('#select-grupos2, #select-asignaciones').html('<option>Cargando...</option>');
	$.ajax({
		type: "POST",
		url: "jsp/getAsignaciones.jsp?jsoncallback=?",
		dataType: "json",
		success: function(data) {
			var html = '';
			$.each(data.Grupos, function(i, item){
				html += '<option value="'+item.cod+'">'+item.desc+'</option>';
			});
			$('#select-grupos2').html(html);
			$('#select-grupos2').change(function(){
				var grupo_id = $(this).val();
				$.each(data.Grupos, function(i, grupo){
					if (grupo_id==grupo.cod){
						html = '';
						$.each(grupo.Asignaciones, function(j, asignacion){
							html += '<option value="'+asignacion.cod+'">'+asignacion.fec+' ('+asignacion.desc+')</option>';
						});
						$('#select-asignaciones').html(html);
					}
				});
			}).trigger('change');
		}
	});

	$('#btnAsignar').on( "click", function() {
		var subcode = $( "#select-subestaciones option:selected" ).val();
		var alicode = $( "#select-alimentadores option:selected" ).val();
		var subdesc = $( "#select-subestaciones option:selected" ).text();
		var alidesc = $( "#select-alimentadores option:selected" ).text();
		var grupo = $( "#select-grupos option:selected" ).val();

		$.ajax({
			type: "POST",
			url: "jsp/asignar.jsp",
			data:{subcode:subcode,
				alicode:alicode,
				subdesc:subdesc,
				alidesc:alidesc,
				grupo:grupo
			},
			dataType: 'json',
			success: function(data) {
				if (data.status == 'OK') {
					alert('Trabajo asignado con Exito');
				} else alert(data.msg);
			},
			error: function() {
				alert('Error al momento de asignar');
			}
		});
	});


	$('#btnGrupos').on( "click", function() {
		$.ajax({
			type: 'GET',
			url: 'jsp/getAllGrupos.jsp?jsoncallback=?',
			data: {option:'get'},
			dataType: 'json',
			success: function(data){
				if (data.status=='OK'){
					var html = '<div class="modal fade" id="grupos" role="dialog">'+
					'<div class="modal-dialog" style="width:750px;">'+
					'<div class="modal-content">'+
					'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal">&times;</button>'+
					'<h4 class="modal-title">Lista de Grupos</h4>'+
					'</div>'+
					'<div class="modal-body">'+
					'<div class="row-fluid">'+
					'<div class="col-md-12">'+
					'<table class="table table-bordered">'+
					'<thead>'+
					'<tr>'+
					'<td>NOMBRE</td>'+
					'<td>DESCRIPCION</td>'+
					'<td>PROYECTO</td>'+
					'<td>FECHA INGRESO</td>'+
					'<td>ESTADO</td>'+
					'<td>OPCIONES</td>'+
					'</tr>'+
					'</thead>'+
					'<tbody>';
					$.each(data.Grupos, function(i, grupo){
						html += '<tr>'+
						'<td>'+grupo.cod+'</td>'+
						'<td>'+grupo.desc+'</td>'+
						'<td>'+grupo.pro+'</td>'+
						'<td>'+grupo.fec+'</td>'+
						'<td>'+grupo.est+'</td>'+
						'<td><a class="btn btn-primary btnEditGrupo" id="btnEditGrupo" data-code="'+grupo.cod+'" alt="Editar" title="Editar"><i class="fa fa-edit"></i></a>'+
						'&nbsp;&nbsp;'+
						'<a class="btn btn-danger btnDeleteGrupo" id="btnDeleteGrupo" data-code="'+grupo.cod+'" alt="Eliminar" title="Eliminar"><i class="fa fa-trash-o"></i></a>'+
						'</td>'+
						'</tr>';
					});
					html += '</tbody>'+
					'</table>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'<div class="modal-footer">'+
					'<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>';
					$('body').append(html);
					$("#grupos").modal('show');
				}
			}
		});
	});


	$('#ver-data-actualizar').on( "click", function() {
		$('#btnActualizarData').hide();
		$.blockUI({ css: { 
			border: 'none', 
			padding: '15px', 
			backgroundColor: '#000', 
			'-webkit-border-radius': '10px', 
			'border-radius': '10px', 
			opacity: .7, 
			color: '#fff' 
		}, message: "Verificando estado de la base de datos ..." });
		$.ajax({
			type: "POST",
			url: "jsp/getEstadoBDD.jsp?jsoncallback=?",
			dataType: "json",
			success: function(data) {
				if (data.estado == 'NO DISPONIBLE'){
					$('#registros').html('<div><p style="color:red;"><strong>Base de Datos no Compactada, comuniquese con el Departamento TIC\'s para el respectivo soporte.</strong></p></div>');
					$.unblockUI();
				}else{
					$.unblockUI();
					$('#labelActualizar').hide();
					$('#registros').html('<div class="cargando">Cargando Data...</div>');
					$.ajax({
						type: 'GET',
						url: 'jsp/getDataParaActualizarSIG.jsp',
						dataType: 'json',
						success: function(data){
							html = '<table border="0" width="100%" class="table table-bordered"">'+
							'<thead>'+
							'<tr>'+
							'<th>N\u00B0</th>'+
							'<th>OBJECT ID</th>'+
							'<th>GLOBAL ID</th>'+
							'<th>NUMPOSTE</th>'+
							'<th>GRUPO</th>'+
							'<th>FECHA REVISION</th>'+
							'<th>HORA REVISION</th>'+
							'<th>#TRANSACCION</th>'+
							'<th><input type="checkbox" id="checkAllN2" name="chN" /></th>'+
							'</tr>'+
							'</thead>'+
							'<tbody>';
							$.each(data.postes, function(j, item){
								html += '<tr>'+
								'<td>'+(j+1)+'</td>'+
								'<td>'+item.oid+'</td>'+
								'<td>'+item.gid+'</td>'+
								'<td>'+item.npt+'</td>'+
								'<td>'+item.grp+'</td>'+
								'<td>'+item.fec+'</td>'+
								'<td>'+item.hor+'</td>'+
								'<td>'+item.tra+'</td>'+
								'<td class="center">'+
								'<input type="checkbox" name="itemNew[]" value="'+item.oid+':'+item.fec+'" class="checkN" />'+
								'</td>'+
								'</tr>';
							});
							html += '</tbody>'+
							'</table>';
							$('#btnActualizarData').show();
							$('#labelActualizar').show();
							$('#registros').html(html);
							if (data.postes.length>0){
								$('#registros #checkAllN2').click(function(){
									var check = $(this).prop('checked');
									$('#registros .checkN').each(function(Index){
										$(this).prop('checked', check);
									});
								});
							}
						}
					});
				}
			},error: function(data){
				$.alert('Error al cargar data para actualizar al SIG');
				//$.alert(data.msg);
				$.unblockUI();
			}
		});
	});

	$("#txtDesde, #txtHasta" ).datepicker({
		//defaultDate: "+1w",
		changeMonth: true,
		numberOfMonths: 2,
		dateFormat: "yymmdd"
	});
	$('#select-opcion-busqueda').on('change', function() {
		if (this.value == 1 ){
			$('#opciones-busqueda_1').show( "slow" );
			$('#opciones-busqueda_2').hide( "slow" );
			$('#txtNumPoste').val('');
		}else{
			$('#opciones-busqueda_2').show( "slow" );
			$('#opciones-busqueda_1').hide( "slow" );
			$('#txtDesde').val('');
			$('#txtHasta').val('');
		}
	})

	$('#btnBusquedaXOpciones1, #btnBusquedaXOpciones2').on("click", function() {
		var desde = '';
		var hasta = '';
		var grupo = '';
		var numPoste = '';
		var estado = '';
		var solo_fotos = '';
		var opcionBusqueda = $('#select-opcion-busqueda option:selected').val();
		if (opcionBusqueda == 1){
			desde = $('#txtDesde').val();
			hasta = $('#txtHasta').val();
			grupo = $('#select-grupos5 option:selected').val();
			estado = $('#select-estados option:selected').val();
			
			if( $('.chk_solo_fotos').prop('checked') ) {
			    solo_fotos = 'SI'
			}
			
			if (desde == '' || hasta == ''){
				alert('Los campos de Fecha DESDE y HASTA, son requeridos');
				return false;
			}
		}else{
			var numPoste = $('#txtNumPoste').val();
			if (numPoste == ''){
				alert('El campo numPoste es Requerido');
				return false;
			}
		}

		$('#labelPostes').hide();
		$('#postes').html('<div class="cargando">Cargando Data...</div>');
		$.blockUI({ css: { 
			border: 'none', 
			padding: '15px', 
			backgroundColor: '#000', 
			'-webkit-border-radius': '10px', 
			'border-radius': '10px', 
			opacity: .7, 
			color: '#fff' 
		}, message: "Cargando Data..." });
		$.ajax({
			type: 'POST',
			url: 'jsp/getListaPostes.jsp',
			data:{
				desde : desde,
				hasta : hasta,
				grupo : grupo,
				estado : estado,
				numPoste : numPoste,
				soloFotos : solo_fotos
			},
			dataType: 'json',
			success: function(data){
				//$('.chk_solo_fotos').prop('checked', false);
				var html = '';
				var countFotosP = 0;
				var countFotosT = 0;
				$.each(data.totalFotosP, function(j, item){
					countFotosP = item.totP;
				});
				$.each(data.totalFotosT, function(j, item){
					countFotosT = item.totT;
				});
				
				html = '<div class="alert alert-warning" role="alert">'+
				  'Total Fotos de Poste subidas: <strong>'+countFotosP+'</strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Total Fotos de Trafos subidas: <strong>'+countFotosT+'</strong>'+
				  '</div>';
				
				html += '<table border="0" width="100%" class="table table-hover" style="font-size: 13px;">'+
				'<thead>'+
				'<tr>'+
				'<th>N\u00B0</th>'+
				'<th>OBJECT ID</th>'+
				'<th>GLOBAL ID</th>'+
				'<th>NUMPOSTE</th>'+
				'<th>ALIMENTADOR</th>'+
				'<th>GRUPO</th>'+
				'<th>FECHA REVISION</th>'+
				'<th>HORA REVISION</th>'+
				'<th>#TRANSACCION</th>'+
				'<th>ESTADO</th>'+
				'<th>FOTO POSTE</th>'+
				'<th>FOTO TRAFO</th>'+
				'</tr>'+
				'</thead>'+
				'<tbody>';
				$.each(data.postes, function(j, item){
					html += '<tr>'+
					'<td>'+(j+1)+'</td>'+
					'<td><a href="#" title="Ver Elementos Poste" class="verElementosPoste" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fec="'+item.fec+'" data-oid="'+item.oid+'" data-nua="'+item.numa+'" data-gru="'+item.grp+'" >'+item.oid+'</a></td>'+
					'<td>'+item.gid+'</td>'+
					'<td>'+item.npt+'</td>'+
					'<td>'+item.alim+'</td>'+
					'<td>'+item.grp+'</td>'+
					'<td>'+item.fec+'</td>'+
					'<td>'+item.hor+'</td>'+
					'<td>'+item.numt+'</td>'+
					'<td>'+item.est+'</td>'+
					'<td align="center">'+
					(item.fotp == ''? '<a href="#" title="Subir Foto Poste" class="uploadPoste" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fre="'+item.fec+'" data-gru="'+item.grp+'" ><img src="images/upload.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fotp+'&tipo=POSTE" target="_blank" data-toggle="tooltip" title="Ver Foto Poste"><img src="images/picture.png" border="0" width="22" height="22" /></a>&nbsp;&nbsp;&nbsp;<a href="#" data-toggle="tooltip" class="eliminarFotoPoste" title="Eliminar Foto Poste" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fre="'+item.fec+'" data-gru="'+item.grp+'" data-file="'+item.fotp+'" data-tipo="POSTE"><img src="images/file_delete.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'<td align="center">'+
					(item.fott == ''? '<a href="#" title="Subir Foto Transformador" class="uploadTrafo" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fre="'+item.fec+'" data-gru="'+item.grp+'" ><img src="images/upload.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fott+'&tipo=TRAFO" target="_blank" data-toggle="tooltip" title="Ver Foto Transformador"><img src="images/picture.png" border="0" width="22" height="22" /></a>&nbsp;&nbsp;&nbsp;<a href="#" data-toggle="tooltip" class="eliminarFotoTrafo" title="Eliminar Foto Trafo" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fre="'+item.fec+'" data-gru="'+item.grp+'" data-file="'+item.fott+'" data-tipo="TRAFO"><img src="images/file_delete.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'</tr>';
				});
				html += '</tbody>'+
				'</table>';
				$('#labelPostes').show();
				$('#postes').html(html);
				$.unblockUI();

				$('.verElementosPoste').on( "click", function() {
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fec = $(this).data('fec');
					var oid = $(this).data('oid');
					var numa = $(this).data('nua');
					var grp = $(this).data('gru');
					$.ajax({
						type: 'POST',
						url: 'jsp/getElementosByPoste.jsp',
						dataType: 'json',
						data : {
							posteGI : gid,
							numPoste : npt,
							fecRevision : fec,
							posteOI : oid,
							numAsig : numa,
							grupo : grp
						},
						success: function(data){
							var html = '<div class="modal fade" id="modalElementosPoste" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
							'<div class="modal-dialog" role="document" style="width:50%;">'+
							'<div class="modal-content">'+
							'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
							'<h4 class="modal-title" id="myModalLabel">Elementos de POSTE</h4>'+
							'</div>'+
							'<div class="modal-body"">'+
							'<div data-accordion-group>';
							var titulo = true;
							var entra = false;
							var count = 0;
							$.each(data.capacitores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Capacitores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Capacitor #' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>PotenciaKVA: '+item.pote+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}
							$.each(data.conductoresB, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores de Baja</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor de Baja # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase: '+item.fase+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.conductoresM, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores de Media</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor de Media # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase: '+item.fase+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Secuencia: '+item.secu+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.conductoresN, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores Neutros</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor Neutro # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.estructuras, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Estructuras Soporte</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Estructura Soporte # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Cantidad: '+item.cant+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.luminarias, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Luminarias</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Luminaria # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Trago GI: '+item.trgi+'</li>'+
								'<li>Num. Trafo: '+item.nutr+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.medidores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Medidores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Medidor # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Serie: '+item.seri+'</li>'+
								'<li>Num. Empresa: '+item.nuem+'</li>'+
								'<li>CoordX: '+item.corx+'</li>'+
								'<li>CoordY: '+item.cory+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.seccionadores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Seccionadores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Seccionador # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Tipo: '+item.tipo+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Posicion Actual A: '+item.posa+'</li>'+
								'<li>Posicion Actual B: '+item.posb+'</li>'+
								'<li>Posicion Actual C: '+item.posc+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.tensores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Tensores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Tensor # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.transformadores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Transformadores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Transformador # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Num. Trafo: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase Conexion: '+item.fase+'</li>'+
								'<li>Propiedad: '+item.prop+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.observaciones, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Observaciones</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								html += '<div><p>Observaci\u00F3n </p>'+
								'<ul>'+
								'<li>Texto: '+item.obs+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
							}

							html += '</div>'+
							'</div>'+
							'<div class="modal-footer">'+
							'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
							'</div>'+
							'</div>'+
							'</div>'+
							'</div>';
							$('#verElementosModal').html(html);
							$('#modalElementosPoste').modal('show');
							$('.accordion').accordion({
								"transitionSpeed": 400
							});
						},error: function(data){
							$.unblockUI();
						}
					});
				});

				$('.uploadPoste').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fre = $(this).data('fre');
					var grp = $(this).data('gru');
					var html = '<div class="modal fade" id="uploadModalPoste" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
					'<div class="modal-dialog" role="document">'+
					'<div class="modal-content">'+
					'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
					'<h4 class="modal-title" id="myModalLabel">Subir Foto de POSTE</h4>'+
					'</div>'+
					'<div class="modal-body">'+
					'<form class="form-horizontal" method="POST" enctype="multipart/form-data" id="fileUploadFormPoste">'+
					'<div class="form-group">'+
					'<label for="fotoPoste" class="col-sm-2 control-label">Foto Poste:</label>'+
					'<div class="col-sm-10">'+
					'<input type="hidden" name="objectID" id="txtObjectIDP" value="">'+
					'<input type="hidden" name="globalID" id="txtGlobalIDP" value="">'+
					'<input type="hidden" name="numPoste" id="txtNumPosteP" value="">'+
					'<input type="hidden" name="fecRevision" id="txtFecRevisionP" value="">'+
					'<input type="hidden" name="grupo" id="txtGrupoP" value="">'+
					'<input type="hidden" name="tipo" value="POSTE">'+
					'<input type="file" class="form-control" id="txtPosteFile" name="file" accept="image/jpeg">'+
					'</div>'+
					'</div>'+
					'</form>'+
					'</div>'+
					'<div class="modal-footer">'+
					'<button type="button" class="btn btn-default" id="btnCloseModalPoste" data-dismiss="modal">Close</button>'+
					'<button type="button" class="btn btn-primary" id="btnSubirFotoPoste">Subir Foto</button>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>';
					$('#uploadModal').html(html);
					$('#uploadModalPoste').modal('show');
					$('#uploadModalPoste').on('shown.bs.modal', function (e) {
						$('#txtObjectIDP').val(oid);
						$('#txtGlobalIDP').val(gid);
						$('#txtNumPosteP').val(npt);
						$('#txtFecRevisionP').val(fre);
						$('#txtGrupoP').val(grp);
					});
					$('#btnSubirFotoPoste').on( "click", function() {
						var form = $('#fileUploadFormPoste')[0];
						var data = new FormData(form);
						$.ajax({
							url: "./UploadFile",
							type: "POST",
							data: data,
							enctype: 'multipart/form-data',
							processData: false,
							contentType: false,
						}).done(function(data) {
							if (data.status == 'OK'){ 
								alert(data.msg);
							}else{
								alert(data.msg);
							}
							$('#uploadModalPoste').modal('hide');
							$("#uploadModalPoste").on('hidden.bs.modal', function () {
								$('#txtPosteFile').val('')
								$('#txtObjectIDP').val('');
								$('#txtGlobalIDP').val('');
								$('#txtFecRevisionP').val('');
								$('#txtGrupoP').val('');
								$('#txtNumPosteP	').val('');
							});
							$( "#btnBusquedaXOpciones1" ).trigger( "click" );
						}).fail(function(data) {
							alert('ERROR');
							alert(data.msg);
						});
					});
				});
				$('.uploadTrafo').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fre = $(this).data('fre');
					var grp = $(this).data('gru');
					var html = '<div class="modal fade" id="uploadModalTrafo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
					'<div class="modal-dialog" role="document">'+
					'<div class="modal-content">'+
					'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
					'<h4 class="modal-title" id="myModalLabel">Subir Foto de TRANSFORMADOR</h4>'+
					'</div>'+
					'<div class="modal-body">'+
					'<form class="form-horizontal" method="POST" enctype="multipart/form-data" id="fileUploadFormTrafo">'+
					'<div class="form-group">'+
					'<label for="fotoTrafo" class="col-sm-2 control-label">Foto Trafo:</label>'+
					'<div class="col-sm-10">'+
					'<input type="hidden" name="objectID" id="txtObjectIDT" value="">'+
					'<input type="hidden" name="globalID" id="txtGlobalIDT" value="">'+
					'<input type="hidden" name="numPoste" id="txtNumPosteT" value="">'+
					'<input type="hidden" name="fecRevision" id="txtFecRevisionT" value="">'+
					'<input type="hidden" name="grupo" id="txtGrupoT" value="">'+
					'<input type="hidden" name="tipo" id="txtTipoT" value="TRAFO">'+
					'<input type="file" class="form-control" id="txtTrafoFile" name="file" accept="image/jpeg">'+
					'</div>'+
					'</div>'+
					'</form>'+
					'</div>'+
					'<div class="modal-footer">'+
					'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
					'<button type="button" class="btn btn-primary" id="btnSubirFotoTrafo">Subir Foto</button>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>';
					$('#uploadModal').html(html);
					$('#uploadModalTrafo').modal('show');
					$('#uploadModalTrafo').on('shown.bs.modal', function (e) {
						$('#txtObjectIDT').val(oid);
						$('#txtGlobalIDT').val(gid);
						$('#txtNumPosteT').val(npt);
						$('#txtFecRevisionT').val(fre);
						$('#txtGrupoT').val(grp);
					});
					$('#btnSubirFotoTrafo').on( "click", function() {
						var form = $('#fileUploadFormTrafo')[0];
						var data = new FormData(form);
						$.ajax({
							url: "./UploadFile",
							type: "POST",
							data: data,
							enctype: 'multipart/form-data',
							processData: false,
							contentType: false,
						}).done(function(data) {
							if (data.status == 'OK'){
								alert(data.msg);
							}else{
								alert(data.msg);
							}
							$('#uploadModalTrafo').modal('hide');
							$("#uploadModalTrafo").on('hidden.bs.modal', function () {
								$('#txtTrafoFile').val('');
								$('#txtObjectIDT').val('');
								$('#txtGlobalIDT').val('');
								$('#txtNumPosteT').val('');
								$('#txtFecRevisionT').val('');
								$('#txtGrupoT').val('');
							});
							$( "#btnBusquedaXOpciones1" ).trigger( "click" );
						}).fail(function(data) {
							alert(data.msg);
						});
					});
				});
				$('.eliminarFotoPoste').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fre = $(this).data('fre');
					var grp = $(this).data('gru');
					var file = $(this).data('file');
					var tipo = $(this).data('tipo');
					$.confirm({
						title: 'Confirmaci\u00F3n!',
						content: '\u00BFEst\u00E1 seguro de borrar la siguiente foto.?',
						type: 'red',
						theme : 'material',
						typeAnimated: true,
						icon: 'fa fa-question',
						buttons: {
							confirm:{
								text: 'Aceptar',
								btnClass: 'btn-blue',
								action: function(){
									$.ajax({
										type: 'POST',
										url: './EliminarFile',
										dataType: 'json',
										data : {
											posteOI : oid,
											posteGI : gid,
											numPoste : npt,
											fecRevision : fre,
											grupo : grp,
											file : file,
											tipo : tipo
										},
										success: function(data){
											$.alert(data.msg);
											$( "#btnBusquedaXOpciones1" ).trigger( "click" );
										},error: function(data){
											$.alert(data.msg);
										}
									});
								}
							},
							cancel: function () {
								$.alert('Cancelado!');
							}
						},
					});
				});
				$('.eliminarFotoTrafo').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fre = $(this).data('fre');
					var grp = $(this).data('gru');
					var file = $(this).data('file');
					var tipo = $(this).data('tipo');
					$.confirm({
						title: 'Confirmaci\u00F3n!',
						content: '\u00BFEst\u00E1 seguro de borrar la siguiente foto.?',
						type: 'red',
						theme : 'material',
						typeAnimated: true,
						icon: 'fa fa-question',
						buttons: {
							confirm:{
								text: 'Aceptar',
								btnClass: 'btn-blue',
								action: function(){
									$.ajax({
										type: 'POST',
										url: './EliminarFile',
										dataType: 'json',
										data : {
											posteOI : oid,
											posteGI : gid,
											numPoste : npt,
											fecRevision : fre,
											grupo : grp,
											file : file,
											tipo : tipo
										},
										success: function(data){
											$.alert(data.msg);
											$( "#btnBusquedaXOpciones1" ).trigger( "click" );
										},error: function(data){
											$.alert(data.msg);
										}
									});
								}
							},
							cancel: function () {
								$.alert('Cancelado!');
							}
						},
					});
				});
			},error:function(data){
				$.unblockUI();
			}
		});
	});

	/*FUNCION PARA EL ROL ADMINISTRADOR*/
	$('#btnBusquedaXOpciones1Admin, #btnBusquedaXOpciones2Admin').on("click", function() {
		var desde = '';
		var hasta = '';
		var grupo = '';
		var numPoste = '';
		var estado = '';
		var solo_fotos = '';
		var opcionBusqueda = $('#select-opcion-busqueda option:selected').val();
		if (opcionBusqueda == 1){
			desde = $('#txtDesde').val();
			hasta = $('#txtHasta').val();
			grupo = $('#select-grupos5 option:selected').val();
			estado = $('#select-estados option:selected').val();
			
			if( $('.chk_solo_fotos').prop('checked') ) {
			    solo_fotos = 'SI'
			}
			
			if (desde == '' || hasta == ''){
				alert('Los campos de Fecha DESDE y HASTA, son requeridos');
				return false;
			}
		}else{
			var numPoste = $('#txtNumPoste').val();
			if (numPoste == ''){
				alert('El campo numPoste es Requerido');
				return false;
			}
		}

		$('#labelPostes').hide();
		$('#postes').html('<div class="cargando">Cargando Data...</div>');
		$.blockUI({ css: { 
			border: 'none', 
			padding: '15px', 
			backgroundColor: '#000', 
			'-webkit-border-radius': '10px', 
			'border-radius': '10px', 
			opacity: .7, 
			color: '#fff' 
		}, message: "Cargando Data..." });
		$.ajax({
			type: 'POST',
			url: 'jsp/getListaPostes.jsp',
			data:{
				desde : desde,
				hasta : hasta,
				grupo : grupo,
				estado : estado,
				numPoste : numPoste,
				soloFotos : solo_fotos
			},
			dataType: 'json',
			success: function(data){
				//$('.chk_solo_fotos').prop('checked', false);
				var html = '';
				var countFotosP = 0;
				var countFotosT = 0;
				$.each(data.totalFotosP, function(j, item){
					countFotosP = item.totP;
				});
				$.each(data.totalFotosT, function(j, item){
					countFotosT = item.totT;
				});
				
				html = '<div class="alert alert-warning" role="alert">'+
				  'Total Fotos de Poste subidas: <strong>'+countFotosP+'</strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Total Fotos de Trafos subidas: <strong>'+countFotosT+'</strong>'+
				  '</div>';
				
				html += '<table border="0" width="100%" class="table table-hover" style="font-size: 13px;">'+
				'<thead>'+
				'<tr>'+
				'<th>N\u00B0</th>'+
				'<th>OBJECT ID</th>'+
				'<th>GLOBAL ID</th>'+
				'<th>NUMPOSTE</th>'+
				'<th>ALIMENTADOR</th>'+
				'<th>GRUPO</th>'+
				'<th>FECHA REVISION</th>'+
				'<th>HORA REVISION</th>'+
				'<th>#TRANSACCION</th>'+
				'<th>ESTADO</th>'+
				'<th>FOTO POSTE</th>'+
				'<th>FOTO TRAFO</th>'+
				'</tr>'+
				'</thead>'+
				'<tbody>';
				$.each(data.postes, function(j, item){
					html += '<tr>'+
					'<td>'+(j+1)+'</td>'+
					'<td><a href="#" title="Ver Elementos Poste" class="verElementosPoste" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fec="'+item.fec+'" data-oid="'+item.oid+'" data-nua="'+item.numa+'" data-gru="'+item.grp+'" >'+item.oid+'</a></td>'+
					'<td>'+item.gid+'</td>'+
					'<td>'+item.npt+'</td>'+
					'<td>'+item.alim+'</td>'+
					'<td>'+item.grp+'</td>'+
					'<td>'+item.fec+'</td>'+
					'<td>'+item.hor+'</td>'+
					'<td>'+item.numt+'</td>'+
					'<td>'+item.est+'</td>'+
					'<td align="center">'+
					(item.fotp == ''? '<a href="#" title="No hay Foto" ><img src="images/sin-imagen.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fotp+'&tipo=POSTE" target="_blank" data-toggle="tooltip" title="Ver Foto Poste"><img src="images/picture.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'<td align="center">'+
					(item.fott == ''? '<a href="#" title="No hay Foto" ><img src="images/sin-imagen.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fott+'&tipo=TRAFO" target="_blank" data-toggle="tooltip" title="Ver Foto Transformador"><img src="images/picture.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'</tr>';
				});
				html += '</tbody>'+
				'</table>';
				$('#labelPostes').show();
				$('#postes').html(html);
				$.unblockUI();
				
				$('.verElementosPoste').on( "click", function() {
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fec = $(this).data('fec');
					var oid = $(this).data('oid');
					var numa = $(this).data('nua');
					var grp = $(this).data('gru');
					$.ajax({
						type: 'POST',
						url: 'jsp/getElementosByPoste.jsp',
						dataType: 'json',
						data : {
							posteGI : gid,
							numPoste : npt,
							fecRevision : fec,
							posteOI : oid,
							numAsig : numa,
							grupo : grp
						},
						success: function(data){
							var html = '<div class="modal fade" id="modalElementosPoste" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
							'<div class="modal-dialog" role="document" style="width:50%;">'+
							'<div class="modal-content">'+
							'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
							'<h4 class="modal-title" id="myModalLabel">Elementos de POSTE</h4>'+
							'</div>'+
							'<div class="modal-body"">'+
							'<div data-accordion-group>';
							var titulo = true;
							var entra = false;
							var count = 0;
							$.each(data.capacitores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Capacitores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Capacitor #' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>PotenciaKVA: '+item.pote+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}
							$.each(data.conductoresB, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores de Baja</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor de Baja # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase: '+item.fase+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.conductoresM, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores de Media</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor de Media # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase: '+item.fase+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Secuencia: '+item.secu+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.conductoresN, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Conductores Neutros</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Conductor Neutro # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.estructuras, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Estructuras Soporte</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Estructura Soporte # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Cantidad: '+item.cant+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.luminarias, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Luminarias</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Luminaria # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Trago GI: '+item.trgi+'</li>'+
								'<li>Num. Trafo: '+item.nutr+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.medidores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Medidores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Medidor # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Serie: '+item.seri+'</li>'+
								'<li>Num. Empresa: '+item.nuem+'</li>'+
								'<li>CoordX: '+item.corx+'</li>'+
								'<li>CoordY: '+item.cory+'</li>'+
								'<li>Voltaje: '+item.volt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.seccionadores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Seccionadores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Seccionador # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Tipo: '+item.tipo+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Posicion Actual A: '+item.posa+'</li>'+
								'<li>Posicion Actual B: '+item.posb+'</li>'+
								'<li>Posicion Actual C: '+item.posc+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.tensores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Tensores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Tensor # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.transformadores, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Transformadores</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								count++;
								html += '<div><p>Transformador # ' + count + '</p>'+
								'<ul>'+
								'<li>ObjectID: '+item.oid+'</li>'+
								'<li>GlobalID: '+item.gid+'</li>'+
								'<li>Estructura: '+item.estr+'</li>'+
								'<li>Num. Trafo: '+item.estr+'</li>'+
								'<li>Subtipo: '+item.subt+'</li>'+
								'<li>Fase Conexion: '+item.fase+'</li>'+
								'<li>Propiedad: '+item.prop+'</li>'+
								'<li>Estado: '+item.esta+'</li>'+
								'<li>Fecha Revision: '+item.fecr+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
								count = 0;
							}

							$.each(data.observaciones, function(j, item){
								if (titulo) {
									html+= '<div class="accordion" data-accordion>'+
									'<div data-control>Observaciones</div>'+
									'<div data-content>';
								}
								titulo = false;
								entra = true;
								html += '<div><p>Observaci\u00F3n </p>'+
								'<ul>'+
								'<li>Texto: '+item.obs+'</li>'+
								'</ul></div>';

							});
							if (entra){
								html += '</div>'+
								'</div>';
								entra = false;
								titulo = true;
							}

							html += '</div>'+
							'</div>'+
							'<div class="modal-footer">'+
							'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
							'</div>'+
							'</div>'+
							'</div>'+
							'</div>';
							$('#verElementosModal').html(html);
							$('#modalElementosPoste').modal('show');
							$('.accordion').accordion({
								"transitionSpeed": 400
							});
						},error: function(data){

						}
					});
				});
			},error:function(data){
				$.unblockUI();
			}
		});
	});
	/*FIN FUNCION PARA EL ROL ADMINISTRADOR*/

	$("#select-opcion-busqueda").val("1");
	$('#opciones-busqueda_1').show( "slow" );
	$('#opciones-busqueda_2').hide( "slow" );

	$('#lista-postes').on("click", function() {
		$("#select-opcion-busqueda").val("1");
		$('#opciones-busqueda_1').show( "slow" );
		$('#opciones-busqueda_2').hide( "slow" );
		$('#postes').html('');		
		$('#txtDesde').val('');
		$('#txtHasta').val('');
		$('#txtNumPoste').val('');
	});



	/*FUNCIONA PARA OBSERVAR DATA REVISIONES*/
	$('#btnRevisar').on("click", function() {
		$.blockUI({ css: { 
			border: 'none', 
			padding: '15px', 
			backgroundColor: '#000', 
			'-webkit-border-radius': '10px', 
			'border-radius': '10px', 
			opacity: .7, 
			color: '#fff' 
		}, message: "Revisando diferencias, espere un momento..." });
		$.ajax({
			type: "POST",
			url: "jsp/getRevisiones.jsp?jsoncallback=?",
			dataType: "json",
			success: function(data) {
				var html = '<table id="tabla-revisiones" class="table table-striped table-bordered" width="100%" cellspacing="0">'+
				'<thead>'+
				'<tr>'+
				'<th>N\u00B0</th>'+
				'<th> FECHA TRANSACCION </th>'+
				'<th> GRUPO </th>'+
				'<th> NUMASIGNACION </th>'+
				'<th> SUBESTACION </th>'+
				'<th> ALIMETADOR </th>'+
				'<th> POSTES TOTALES</th>'+
				'<th> POSTES PARCIALES </th>'+
				'<th> DIFERENCIAS </th>'+
				'<th> REVISAR </th>'+
				'<th> EXPORTAR </th>'+
				'</tr>'+
				'</thead>'+
				'<tbody>';
				$.each(data.Revisiones, function(i, item){
					html += '<tr>'+
					'<td>'+(i+1)+'</td>'+
					'<td>'+item.fect+'</td>'+
					'<td>'+item.grup+'</td>'+
					'<td>'+item.numa+'</td>'+
					'<td>'+item.sube+'</td>'+
					'<td>'+item.alim+'</td>'+
					'<td>'+item.poto+'</td>'+
					'<td>'+item.pono+'</td>'+
					'<td>'+item.dife+'</td>'+
					'<td align="center">'+
					(item.dife > 0 ? '<a href="#" title="Corregir diferencias" class="corregirDiferencias" data-fect="'+item.fect+'" data-grup="'+item.grup+'" data-numa="'+item.numa+'" data-sube="'+item.sube+'" data-alim="'+item.alim+'" data-poto="'+item.poto+'" data-pono="'+item.pono+'" data-dife="'+item.dife+'" ><img src="images/corregir.png" border="0" width="22" height="22" /></a>':'<img src="images/revisado.png" border="0" width="22" height="22" />')+
					'</td>'+
					'<td align="center">'+
					'<form method="post" action="./Reporteria">'+
					'<input type="hidden" name="opcion" value="reporte3" />'+
					'<input type="hidden" name="fect" value="'+item.fect+'" />'+
					'<input type="hidden" name="grupo" value="'+item.grup+'" />'+
					'<input type="hidden" name="numa" value="'+item.numa+'" />'+
					'<button type="submit" class="btn btn-success"><span class="glyphicon glyphicon-save-file" aria-hidden="true"></span> Exportar Diferencia</button>'+
					'</form>'+
					'</td>'+
					'</tr>';
				});
				html += '</tbody></table>';
				$('#content').html(html);
				$('#tabla-revisiones').DataTable({
					paging: false
				});
				$.unblockUI();
				
				$('.corregirDiferencias').on("click", function() {
					var fec = $(this).data('fect');
					var grup = $(this).data('grup');
					var numa = $(this).data('numa');
					var sube = $(this).data('sube'); 
					var alim = $(this).data('alim');
					var poto = $(this).data('poto');
					var pono = $(this).data('pono');
					var dife = $(this).data('dife');
					$.confirm({
						title: 'Confirmaci\u00F3n!',
						content: 'Una vez corrigida la diferencia, no se podr\u00E1 descargar el Reporte(Excel) de Diferencias, \u00BFDesea continuar de todas maneras\u00F1..?',
						type: 'red',
						theme : 'material',
						typeAnimated: true,
						icon: 'fa fa-question',
						buttons: {
							confirm:{
								text: 'Aceptar',
								btnClass: 'btn-blue',
								action: function(){
									$.blockUI({ css: { 
										border: 'none', 
										padding: '15px', 
										backgroundColor: '#000', 
										'-webkit-border-radius': '10px', 
										'border-radius': '10px', 
										opacity: .7, 
										color: '#fff' 
									}, message: "Ejecutando Proceso, espere unos minutos por favor ..." });
									$.ajax({
										type: 'POST',
										url: 'jsp/corregirDiferencias.jsp',
										dataType: 'json',
										data : {
											fecTransaccion : fec,
											numAsig : numa,
											grupo : grup,
											subesta : sube,
											alimenta : alim,
											posteTotales : poto,
											posteNormales : pono,
											diferencia : dife
										},
										success: function(data){
											if (data.status == 'OK'){
												$.unblockUI();
												$.alert(data.msg);
											}else{
												$.unblockUI();
												$.alert(data.msg);
											}
											$("#btnRevisar").trigger( "click" );
										},error: function(data){
											alert('ERROR: ' + data.msg);
											$.unblockUI();
											$("#btnRevisar").trigger( "click" );
										}
									});
								}
							},
							cancel: function () {
								$.unblockUI();
								$.alert('Cancelado!');
							}
						},
					});
				});
			},error: function(data){
				alert('Error al cargar data para revisiones');
				$.unblockUI();
			}
		});
	});

	/*$('#lista-postes').on("click", function() {
		$('#labelPostes').hide();
		$('#postes').html('<div class="cargando">Cargando Data...</div>');
		$.ajax({
			type: 'GET',
			url: 'jsp/getListaPostes.jsp',
			dataType: 'json',
			success: function(data){
				html = '<table border="0" width="100%" class="table table-hover">'+
				'<thead>'+
				'<tr>'+
				'<th>N\u00B0</th>'+
				'<th>OBJECT ID</th>'+
				'<th>GLOBAL ID</th>'+
				'<th>NUMPOSTE</th>'+
				'<th>GRUPO</th>'+
				'<th>FECHA REVISION</th>'+
				'<th>HORA REVISION</th>'+
				'<th>FOTO POSTE</th>'+
				'<th>FOTO TRAFO</th>'+
				'</tr>'+
				'</thead>'+
				'<tbody>';
				$.each(data.postes, function(j, item){
					html += '<tr>'+
					'<td>'+(j+1)+'</td>'+
					'<td><a href="#" title="Ver Elementos Poste" class="verElementosPoste" data-gid="'+item.gid+'" data-npt="'+item.npt+'" data-fec="'+item.fec+'">'+item.oid+'</a></td>'+
					'<td>'+item.gid+'</td>'+
					'<td>'+item.npt+'</td>'+
					'<td>'+item.grp+'</td>'+
					'<td>'+item.fec+'</td>'+
					'<td>'+item.hor+'</td>'+
					'<td align="center">'+
					(item.fotp == ''? '<a href="#" title="Subir Foto Poste" class="uploadPoste" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" ><img src="images/upload.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fotp+'&tipo=POSTE" target="_blank" data-toggle="tooltip" title="Ver Foto Poste"><img src="images/picture.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'<td align="center">'+
					(item.fott == ''? '<a href="#" title="Subir Foto Transformador" class="uploadTrafo" data-oid="'+item.oid+'" data-gid="'+item.gid+'" data-npt="'+item.npt+'" ><img src="images/upload.png" border="0" width="22" height="22" /></a>':'<a href="./ObtenerImagen?fileName='+item.fott+'&tipo=TRAFO" target="_blank" data-toggle="tooltip" title="Ver Foto Transformador"><img src="images/picture.png" border="0" width="22" height="22" /></a>')+
					'</td>'+
					'</tr>';
				});
				html += '</tbody>'+
				'</table>';
				$('#labelPostes').show();
				$('#postes').html(html);

				$('.verElementosPoste').on( "click", function() {
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var fec = $(this).data('fec');
					$.ajax({
						type: 'POST',
						url: 'jsp/getElementosByPoste.jsp',
						dataType: 'json',
						data : {
							posteGI : gid,
							numPoste : npt,
							fecRevision : fec
						},
						success: function(data){
							var html = '<div class="modal fade" id="modalElementosPoste" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
							'<div class="modal-dialog" role="document" style="width:50%;">'+
							'<div class="modal-content">'+
							'<div class="modal-header">'+
							'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
							'<h4 class="modal-title" id="myModalLabel">Elementos de POSTE</h4>'+
							'</div>'+
							'<div class="modal-body"">'+
							'<div id="accordion">';
							var titulo = true;
							var count = 0;
							$.each(data.capacitores, function(j, item){
								if (titulo) html+= '<h3>Capacitores</h3><div>';
								titulo = false;
								count++;
								html += '<p>Capacitor #' + count + '</p>'+
								'<ul>'+
								'<li>Estructura: '+item.est+'</li>'+
								'<li>Subtipo: '+item.sub+'</li>'+
								'</ul></div>';

							});
							$.each(data.conductoresM, function(j, item){
								if (titulo) html+= '<h3>Capacitores Media</h3><div style="height:500px !important;">';
								titulo = false;
								count++;
								html += '<p>Capacitor de Media#' + count + '</p>'+
								'<ul>'+
								'<li>Estructura: '+item.est+'</li>'+
								'<li>Subtipo: '+item.sub+'</li>'+
								'</ul></div>';

							});
							html += '<h3>Section 1</h3>'+
							'<div>'+
							'<p>'+
							'Mauris mauris ante, blandit et, ultrices a, suscipit eget, quam. Intege'+
							'</p>'+
							'</div>'+
							'<h3>Section 2</h3>'+
							'<div>'+
							'<p>'+
							'Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet'+
							'</p>'+
							'</div>'+
							'<h3>Section 3</h3>'+
							'<div>'+
							'<p>'+
							'Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet'+
							'</p>'+
							'</div>'+
							'<h3>Section 4</h3>'+
							'<div>'+
							'<p>'+
							'Sed non urna. Donec et ante. Phasellus eu ligula. Vestibulum sit amet'+
							'</p>'+
							'</div>'+
							'</div>'+
							'</div>'+
							'<div class="modal-footer">'+
							'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
							'</div>'+
							'</div>'+
							'</div>'+
							'</div>';
							$('#verElementosModal').html(html);
							$('#modalElementosPoste').modal('show');
							$( "#accordion" ).accordion({
								heightStyle: "auto"
							});
						},error: function(data){

						}
					});
				});

				$('.uploadPoste').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var html = '<div class="modal fade" id="uploadModalPoste" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
					'<div class="modal-dialog" role="document">'+
					'<div class="modal-content">'+
					'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
					'<h4 class="modal-title" id="myModalLabel">Subir Foto de POSTE</h4>'+
					'</div>'+
					'<div class="modal-body">'+
					'<form class="form-horizontal" method="POST" enctype="multipart/form-data" id="fileUploadFormPoste">'+
					'<div class="form-group">'+
					'<label for="fotoPoste" class="col-sm-2 control-label">Foto Poste:</label>'+
					'<div class="col-sm-10">'+
					'<input type="hidden" name="objectID" id="txtObjectID" value="">'+
					'<input type="hidden" name="globalID" id="txtGlobalID" value="">'+
					'<input type="hidden" name="numPoste" id="txtNumPoste" value="">'+
					'<input type="hidden" name="tipo" value="POSTE">'+
					'<input type="file" class="form-control" id="txtPosteFile" name="file" accept="image/jpeg">'+
					'</div>'+
					'</div>'+
					'</form>'+
					'</div>'+
					'<div class="modal-footer">'+
					'<button type="button" class="btn btn-default" id="btnCloseModalPoste" data-dismiss="modal">Close</button>'+
					'<button type="button" class="btn btn-primary" id="btnSubirFotoPoste">Subir Foto</button>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>';
					$('#uploadModal').html(html);
					$('#uploadModalPoste').modal('show');
					$('#uploadModalPoste').on('shown.bs.modal', function (e) {
						$('#txtObjectID').val(oid);
						$('#txtGlobalID').val(gid);
						$('#txtNumPoste').val(npt);
					});
					$('#btnSubirFotoPoste').on( "click", function() {
						var form = $('#fileUploadFormPoste')[0];
						var data = new FormData(form);
						$.ajax({
							url: "./UploadFile",
							type: "POST",
							data: data,
							enctype: 'multipart/form-data',
							processData: false,
							contentType: false,
						}).done(function(data) {
							if (data.status == 'OK') alert(data.msg);
							$('#uploadModalPoste').modal('hide');
							$("#uploadModalPoste").on('hidden.bs.modal', function () {
								$('#txtPosteFile').val('')
								$('#txtObjectID').val('');
								$('#txtGlobalID').val('');
								$('#txtNumPoste').val('');
							});
							$( "#lista-postes" ).trigger( "click" );
						}).fail(function(data) {
							alert(data.msg);
						});

						//$('#txtPosteFile').ajaxfileupload({
							  //'action' : 'UploadFile',
							  //'valid_extensions' : ['jpg','jpeg'],
							  //'onComplete' : function(response) {
							      //$('#upload').hide();
							      //$('#message').show();

							      //var statusVal = JSON.stringify(response.status);

							     //if(statusVal == "false")
							     //{
							     //$("#message").html("<font color='red'>"+JSON.stringify(response.message)+"</font>");
							     //}  
							     //if(statusVal == "true")
							     //{
							     //$("#message").html("<font color='green'>"+JSON.stringify(response.message)+"</font>");
							     //}                  
							//},
							//'onStart' : function() {
							        //$('#upload').show();
							        //$('#message').hide();
							//}
							//});
					});
				});
				$('.uploadTrafo').on( "click", function() {
					var oid = $(this).data('oid');
					var gid = $(this).data('gid');
					var npt = $(this).data('npt');
					var html = '<div class="modal fade" id="uploadModalTrafo" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">'+
					'<div class="modal-dialog" role="document">'+
					'<div class="modal-content">'+
					'<div class="modal-header">'+
					'<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>'+
					'<h4 class="modal-title" id="myModalLabel">Subir Foto de TRANSFORMADOR</h4>'+
					'</div>'+
					'<div class="modal-body">'+
					'<form class="form-horizontal" method="POST" enctype="multipart/form-data" id="fileUploadFormTrafo">'+
					'<div class="form-group">'+
					'<label for="fotoTrafo" class="col-sm-2 control-label">Foto Trafo:</label>'+
					'<div class="col-sm-10">'+
					'<input type="hidden" name="objectID" id="txtObjectIDT" value="">'+
					'<input type="hidden" name="globalID" id="txtGlobalIDT" value="">'+
					'<input type="hidden" name="numPoste" id="txtNumPosteT" value="">'+
					'<input type="hidden" name="tipo" id="txtTipoT" value="TRAFO">'+
					'<input type="file" class="form-control" id="txtTrafoFile" name="file" accept="image/jpeg">'+
					'</div>'+
					'</div>'+
					'</form>'+
					'</div>'+
					'<div class="modal-footer">'+
					'<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>'+
					'<button type="button" class="btn btn-primary" id="btnSubirFotoTrafo">Subir Foto</button>'+
					'</div>'+
					'</div>'+
					'</div>'+
					'</div>';
					$('#uploadModal').html(html);
					$('#uploadModalTrafo').modal('show');
					$('#uploadModalTrafo').on('shown.bs.modal', function (e) {
						$('#txtObjectIDT').val(oid);
						$('#txtGlobalIDT').val(gid);
						$('#txtNumPosteT').val(npt);
					});
					$('#btnSubirFotoTrafo').on( "click", function() {
						var form = $('#fileUploadFormTrafo')[0];
						var data = new FormData(form);
						$.ajax({
							url: "./UploadFile",
							type: "POST",
							data: data,
							enctype: 'multipart/form-data',
							processData: false,
							contentType: false,
						}).done(function(data) {
							if (data.status == 'OK') alert(data.msg);
							$('#uploadModalTrafo').modal('hide');
							$("#uploadModalTrafo").on('hidden.bs.modal', function () {
								$('#txtTrafoFile').val('');
								$('#txtObjectIDT').val('');
								$('#txtGlobalIDT').val('');
								$('#txtNumPosteT').val('');
							});
							$( "#lista-postes" ).trigger( "click" );
						}).fail(function(data) {
							alert(data.msg);
						});
					});
				});
			}
		});

	});*/


	$('#btnActualizarData').on( "click", function() {		
		var foo=true;
		var eachfoo=false;
		$("#registros input[type='checkbox']").each(function(){
			if ($(this).prop('checked')) eachfoo=true;
		});
		if (eachfoo==false){
			alert('Debe poner un visto por lo menos en un item de la Lista');
			foo = eachfoo;
		}
		if (foo){
			$('#btnActualizarData').text('Actualizando, espere por favor...');
			$('#btnActualizarData').attr('disabled', 'true');
			var postes = new Array();
			$("#registros input[type='checkbox']:checked").each(function(){
				if ($(this).attr('id')!='checkAllN' && $(this).attr('id')!='checkAll2'){
					var foo2 = true;
					for (var i in postes){
						if (postes[i]==$(this).val())
							foo2=false;
					}
					if (foo2){
						postes.push($(this).val());
					}
					//alert(postes);
				}
			});

			$.ajax({
				type: "POST",
				url: "./ActualizarDataSig",
				data: "numpostes="+postes,
				success: function(msg){
					$("#solicitudes_reg .checkN").each(function(){
						if ($(this).prop('checked')) $(this).parents('tr').remove();
					});
					$('#btnActualizarData').text('Actualizar Data SIG');
					$('#btnActualizarData').removeAttr('disabled');
					$( "#ver-data-actualizar" ).trigger( "click" );
					alert( msg );
				}
			});


		}
	});

	$( "#desde, #hasta, #desde2, #hasta2" ).datepicker({
		//defaultDate: "+1w",
		changeMonth: true,
		numberOfMonths: 2,
		dateFormat: "yymmdd"
	});
});