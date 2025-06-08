function actorSelected(select) {
	let index = select.selectedIndex;
	let option = select.options[index];
	let id = option.value;
	let nombre = option.text;
	let urlImagen = option.dataset.url;

	if (id === "0") return; // Evita seleccionar la opción por defecto

	option.disabled = true;
	select.selectedIndex = 0;

	agregarActor(id, nombre, urlImagen);

	let ids = $("#ids").val();

	if (ids === "") {
		$("#ids").val(id);
	} else {
		$("#ids").val(ids + "," + id);
	}
}

function agregarActor(id, nombre, urlImagen) {
	let htmlString = `
		<div class="card col-md-3 m-2" style="width: 10rem;" id="actor-${id}">
			<img src="${urlImagen}" class="card-img-top" alt="${nombre}">
			<div class="card-body">
				<p class="card-text">${nombre}</p>
				<button type="button" class="btn btn-danger" data-id="${id}" onclick="eliminarActor(this); return false;">Eliminar</button>
			</div>
		</div>`;

	$("#protagonistas_container").append(htmlString);
}

function eliminarActor(btn) {
	let id = btn.dataset.id;
	let node = btn.closest(".card");

	let arrayIds = $("#ids").val().split(",").filter(idActor => idActor !== id);
	$("#ids").val(arrayIds.join(","));

	// Rehabilita la opción del select
	$("#protagonistas option[value='" + id + "']").prop("disabled", false);

	$(node).remove();
}


function previsualizar(){
	let reader= new FileReader;
	
	reader.readAsDataURL(document.getElementById("archivo").files[0]);
	
	reader.onload = function(e){
		
		let vista = document.getElementById("vista_previa");
		vista.classList.remove("d-none");
		vista.style.backgroundImage = 'url("' + e.target.result + '")';
		
	}
	
	
}

