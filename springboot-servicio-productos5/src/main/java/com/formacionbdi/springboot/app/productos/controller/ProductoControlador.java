package com.formacionbdi.springboot.app.productos.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
		
		//SE LANZA UNA EXCEPTION PARA EFECTOS DE PRUEBA
		if(id.equals(10L)) throw new IllegalStateException("Producto no encontrado");
		
		//SIMULACIÓN DE UNA LLAMADA LENTA. SI SE LLAMA AL ID 7, TENDRÁ UNA DEMORA DE 5 SEGUNDOS
		if(id.equals(7L)) {
			try {
				TimeUnit.SECONDS.sleep(5L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Producto producto = productoService.findById(id);
		//ESTABLECEMOS EL PUERTO PARA INFORMAR QUÉ PUERTO ESTÁ USANDO EL MICROSERVICIO."local.server.port" : HACE REFERENCIA AL "server.port" DEL APPLICATION PROPERTIES. 
		producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
				
			
		return producto;
	}
	
	
	
	@Autowired
	private IProductoService productoService;
	@Autowired 
	private Environment env;  //CON ESTA VAIABLE PODEMOS OBTENER EL PUERTO QUE ESTAMOS USANDO. ESTO POR EL BALANCEO DE CARGA. 	
	@Value("${server.port}")
	private Integer port;
}
