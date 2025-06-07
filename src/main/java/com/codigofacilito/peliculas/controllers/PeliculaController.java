package com.codigofacilito.peliculas.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codigofacilito.peliculas.entities.Actor;
import com.codigofacilito.peliculas.entities.Pelicula;
import com.codigofacilito.peliculas.services.IActorService;
import com.codigofacilito.peliculas.services.IArchivoService;
import com.codigofacilito.peliculas.services.IGeneroService;
import com.codigofacilito.peliculas.services.IPeliculaService;

import jakarta.validation.Valid;

@Controller
public class PeliculaController {

	private IPeliculaService service;
	private IGeneroService generoService;
	private IActorService actorService;
	private IArchivoService archivoService;

	public PeliculaController(IPeliculaService service, IGeneroService generoService, IActorService actorService, IArchivoService archivoService) {
		this.service = service;
		this.generoService = generoService;
		this.actorService = actorService;
		 this.archivoService = archivoService;
	}

	@GetMapping("pelicula")
	public String crear(Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("actores", actorService.findAll());
		model.addAttribute("titulo", "Nueva Pelicula");
		return "pelicula";
	}

	@GetMapping("pelicula/{id}")
	public String editar(@PathVariable(name = "id") Long id, Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula", pelicula);
		model.addAttribute("generos", generoService.findAll());
		model.addAttribute("actores", actorService.findAll());
		model.addAttribute("titulo", "Editar Pelicula");
		return "pelicula";
	}

	@PostMapping("/pelicula")
	public String guardar(@Valid Pelicula pelicula, BindingResult br, @ModelAttribute(name = "ids") String ids,
			Model model, @RequestParam("archivo") MultipartFile imagen) {

		if (br.hasErrors()) {
			model.addAttribute("generos", generoService.findAll());
			model.addAttribute("actores", actorService.findAll());
			return "pelicula";
		}
		
		if(!imagen.isEmpty()) {
				String archivo = pelicula.getNombre() + getExtension(imagen.getOriginalFilename());
				pelicula.setImagen(archivo);
				
				try {
					archivoService.guardar(archivo, imagen.getInputStream());
				} catch (IOException e) {
						e.printStackTrace();
				}
		}else{
			pelicula.setImagen("_default.jpg");
		}

		List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
		List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);

		pelicula.setProtagonistas(protagonistas);
		service.save(pelicula);
		return "redirect:home";
	}
	
	private String getExtension(String archivo) {
		return archivo.substring(archivo.lastIndexOf("."));
	}
	

	@GetMapping({ "/", "/home", "/index" })
	public String home(Model model) {
		model.addAttribute("peliculas", service.finAll());
		//model.addAttribute("msj", "La App esta en mantenimiento");
		//model.addAttribute("tipoMsj", "danger");
		return "home";
	}

}
