/**
 * 
 */

$(function(){
	$('#about').click(function(){
		var html = '<div class="modal fade" id="acerca" role="dialog">'+
		'<div class="modal-dialog" style="width:660px;">'+
		'<div class="modal-content">'+
		'<div class="modal-header">'+
		'<button type="button" class="close" data-dismiss="modal">&times;</button>'+
		'<h4 class="modal-title">Sobre la aplicaci\u00F3n</h4>'+
		'</div>'+
		'<div class="modal-body">'+
		'<div class="row-fluid">'+
		'<div class="col-md-4">'+
		'<div style="display:table-row; vertical-align:middle;">'+
		'<div style="margin: 45px 10px; text-align:center;"><img src="images/about.png" border="0" /></div>'+
		'</div>'+
		'</div>'+
		'<div class="col-md-8">'+
		'<h3>GIS WEB</h3>'+
		'<h5>v2.2</h5>'+
		'<p>Aplicaci\u00F3n desarrollada por la Empresa El\u00E9ctrica P\u00FAblica Estrat\u00F3gica CNEL EP Unidad de Negocios Santo Domingo.</p>'+
		'<p><strong>Contacto: </strong>02 3730 900 ext. 614</p>'+
		'</div>'+
		'</div>'+
		'</div>'+
		'<div class="modal-footer">'+
		'<button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>'+
		'</div>'+
		'</div>'+
		'</div>'+
		'</div>';
		$('#acercaModal').html(html);
		$("#acerca").modal('show');
	});

});