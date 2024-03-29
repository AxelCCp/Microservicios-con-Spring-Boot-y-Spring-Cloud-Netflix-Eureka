package com.formacionbdi.springboot.app.item.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.IItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ItemController { 

	// A TRAVÉS DEL MÉTODO LISTAR(), CON @REQUESTPARAM SE PUEDE OBTENER LA INFORMACIÓN AGREGADA A LOS FILTROS DE FÁBRICA ..
	// .. EN EL APPLICATION.YML DE SERVER-GATEWAY-SERVER.
	// SE INYECTAN LOS PARAMETROS nombre Y token-request QUE VIENEN DEL REQUEST, QUE FUERON AÑADIDOS EN EN APPLICATION.YML .. 
	// ..DEL SERVER-GATEWAY-SERVER.
	
	// CON "required = false" NO ES OBLIGATORIO MANDAR EL PARÁMETRO NOMBRE DESDE GATEWAY. DE ESTA MANERA SE CONSIGUE EVITAR..
	// LA GENERACIÓN DE UN ERROR, SI ES QUE NO SE LLEGA A ENVIAR DESDE EL GATEWAY EL PARÁMETRO QUE ESPERA ESTE MÉTODO LISTAR. 
	
	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name="nombre", required = false)String nombre, @RequestHeader(name="token-request", required = false) String token) {
		System.out.println(nombre); //SE IMPRIME SOLO PARA VERLOS EN CONSOLA
		System.out.println(token);
		return itemService.findAll();
	}

	@HystrixCommand(fallbackMethod="metodoAlternativo")  //MÉTODO ALTERNATIVO POR SI ES QUE FALLA LA COMUNICACIÓN. 
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable(value = "id") Long id, @PathVariable(value = "cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	
	public Item metodoAlternativo(Long id, Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("sin datos por falla de conexión");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return item;
		
	}

	@Autowired
	//@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private IItemService itemService;
}
