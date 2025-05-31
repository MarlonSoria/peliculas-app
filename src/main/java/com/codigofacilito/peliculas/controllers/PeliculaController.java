package com.codigofacilito.peliculas.controllers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.codigofacilito.peliculas.entities.Actor;
import com.codigofacilito.peliculas.entities.Pelicula;
import com.codigofacilito.peliculas.services.IActorService;
import com.codigofacilito.peliculas.services.IGeneroService;
import com.codigofacilito.peliculas.services.IPeliculaService;


@Controller
public class PeliculaController  {
	
	private IPeliculaService service;
	private IGeneroService  generoService;
	private IActorService actorService;
	
	public PeliculaController(IPeliculaService service, IGeneroService generoService, IActorService actorService) {
		this.service = service;
		this.generoService = generoService;
		this.actorService = actorService;
	}
	
	@GetMapping("pelicula")
	public String crear(Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula",pelicula);
		model.addAttribute("generos",generoService.findAll());
		model.addAttribute("actores",actorService.findAll());
		model.addAttribute("titulo","Nueva Pelicula");
		return "pelicula";
	}
	
	@GetMapping("pelicula/{id}")
	public String editar(@PathVariable(name = "id")Long id, Model model) {
		Pelicula pelicula = new Pelicula();
		model.addAttribute("pelicula",pelicula);
		model.addAttribute("generos",generoService.findAll());
		model.addAttribute("actores",actorService.findAll());
		model.addAttribute("titulo","Editar Pelicula");
		return "pelicula";
	}
	
	
	@PostMapping("/pelicula")
	public String guardar(Pelicula pelicula , @ModelAttribute(name ="ids" ) String ids) {
	
		List<Long> idsProtagonistas = Arrays.stream(ids.split(",")).map(Long:: parseLong).collect(Collectors.toList());
		List<Actor> protagonistas = actorService.findAllById(idsProtagonistas);
		
		pelicula.setProtagonistas(protagonistas);
		service.save(pelicula);
		return "redirect:home";
	}
	
	@GetMapping({"/","/home","/index"})
	public String home() {
		return "home";
	}
	

}
