package com.formacionbdi.springboot.app.productos.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoControlador {
	
	@GetMapping("/listar")
	public List<Producto>listar(){
		return productoService.findAll().stream().map(producto -> {
			//producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			producto.setPort(port);
			return producto;
		}).collect(Collectors.toList());
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable(value="id")Long id) {
		Producto producto = productoService.findById(id);
		
		//ESTABLECEMOS EL PUERTO PARA INFORMAR QUÉ PUERTO ESTÁ USANDO EL MICROSERVICIO."local.server.port" : HACE REFERENCIA AL "server.port" DEL APPLICATION PROPERTIES. 
		producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		
		/*
		//SIMULACIÓN DE FALLO PARA VER FUNCIONAMIENTO DE HYSTRIX
		boolean ok = false;
		if(!ok) {
			throw new RuntimeException("No se pudo cargar el producto");
		}*/
		
		
		/*
		//SIMULACIÓN DE PAUSA (THREAD SLEEP) TIMEOUT DE HYSTRIX. SE SIMULA UNA PAUSA DE 2 SEGUNDOS, PARA QUE DÉ ERROR. DA ERROR PQ EL TIEMPO POR DEFECTO EN RIBBON Y HYSTRIX ES DE 1 SEGUNDO. 
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
			
		return producto;
	}
	
	
	
	@Autowired
	private IProductoService productoService;
	@Autowired 
	private Environment env;  //CON ESTA VAIABLE PODEMOS OBTENER EL PUERTO QUE ESTAMOS USANDO. ESTO POR EL BALANCEO DE CARGA. 	
	@Value("${server.port}")
	private Integer port;
}
