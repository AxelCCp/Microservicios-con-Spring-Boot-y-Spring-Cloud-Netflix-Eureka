package com.formacionbdi.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.formacionbdi.springboot.app.item.models.Producto;

//DECLARAMOS QUE ESTA INTERFAZ ES UN CLIENTE FEIGN Y PONEMOS EL NOMBRE DEL MICROSERVICIO AL CUAL NOS QUEREMOS CONECTAR.
//EL NOMBRE servicio-productos LO SACAMOS DEL APPLICATION.PROPERTIES.


@FeignClient(name = "servicio-productos")
public interface IProductoClienteRest {

		
	// @PONEMOS LAS RUTAS DEL CONTROLADOR DEL MICROSERVICIO
	// ""SPRINGBOOT-SERVICIO-PRODUCTOS". DE ESTA MANERA CONSUMIMOS EL SERVICIO..
	// ..QUE VIENE EN FORMATO JSON DESDE LOS MÉTODOS HANDLER DE SU CONTROLADOR.

	
	// MÉTODO PARA OBTENER EL LISTADO DE PRODUCTOS.
	@GetMapping("/listar")
	public List<Producto> listar();

	// MÉTODO PARA OBTENER DETALLE SEGÚN ID Y CANTIDAD.
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable(value = "id") Long id);
	
	
	//CONFIGURAMOS RIBBON EN FEIGN, PARAELBALANCEODE CARGA 
}
