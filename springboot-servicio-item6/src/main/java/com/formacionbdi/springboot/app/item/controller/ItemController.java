package com.formacionbdi.springboot.app.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


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

	// CREACIÓN DE CIRCUIT BREAKER DE RESILIENSE4J CON ANOTACIONES:
	// SI SE USAN ANOTACIONES Y NO CÓDIGO JAVA PARA CREAR EL CIRCUIT BREAKER, EL CIRCUIT BREAKER SOLO LEERÁ ..
	// .. LA CONFIGURACIÓN DEL APPLICATION.PROPERTIES O DEL APPLICATION.YML. NO VA A LEER EL APPCONFIG.JAVA.
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable(value = "id") Long id, @PathVariable(value = "cantidad") Integer cantidad) {
		
		//SE CREA UN NUEVO CIRCUITO IDENTIFICADO COMO "items".
		//DESPUÉS CON RUN() SIGUE LA CONEXIÓN HACIA EL MICROSERVICIO. 
		//SI EXISTEN ERRORES, SE ABRE EL MÉTODO ALTERNATIVO.
		return cbFactory.create("items").run(()->itemService.findById(id, cantidad), e->metodoAlternativo(id,cantidad,e));
	}
	
	//IGUAL AL MÉTODO DETALLE PERO USANDO ANOTACIONES:
	@CircuitBreaker(name="items", fallbackMethod="metodoAlternativo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable(value = "id") Long id, @PathVariable(value = "cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad);
	}
	
	// IGUAL AL MÉTODO DETALLE PERO USANDO ANOTACIONES:
	// CON TIMELIMITER, EL MÉTODO NO TIENE COMO SABER EL TIEMPO DE DEMORA DEL REQUEST, POR LO TANTO HAY QUE ENVOLVER..
	// .. ESTA LLAMADA EN UNA CLASE CON NOMBRE: COMPLETABLEFUTURE. ESTA CLASE ENVUELVE A UNA LLAMADA ASINCRÓNICA.. 
	// .. , TAMBIÉN REPRESENTA UNA LLAMADA FUTURA Y MANEJA EL TIMEOUT. Y EL RETURN SE ENVUELVE EN UNA FUNCIÓN LAMBDA..
	// .. CON EL MÉTODO SUPPLYASYNC(), PARA LLAMADAS ASÝNCRONAS.
	// ***EL TIPO DEL MÉTODO DETALLE(), ES ENVUELTO ENTRE PARÉNTESIS ANGULARES COMO UN GENÉRICO. 
	@CircuitBreaker(name="items", fallbackMethod="metodoAlternativo2")//TAMBN SE PUEDEN COMBINAR AMBAS ANOTACIONES.
	@TimeLimiter(name="items")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable(value = "id") Long id, @PathVariable(value = "cantidad") Integer cantidad) {
		return CompletableFuture.supplyAsync(()-> itemService.findById(id, cantidad));
	}
	
	
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
		logger.info(e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("sin datos por falla de conexión");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return item;
	}
	
	//ESTE MÉTODO ALTERNATIVO ES PARA "public CompletableFuture<Item> detalle3"
	//SI EL MÉTODO HANDLER ESTÁ CON @TimeLimiter Y CompletableFuture<Item>, EL MÉTODO ALTERNATIVO TAMBIÉN. 
	public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e) {
		logger.info(e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("sin datos por falla de conexión");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return CompletableFuture.supplyAsync(()-> item);
	}
	
	// ResponseEntity : ES UN OBJ SPRING QUE REPRESENTA EL CONTENIDO QUE SE VA A GUARDAR EN LA RESPUESTA. EN ESTE CASO..
	// SE GUARDARÁN LAS CONFIGURACIONES. 
	// <?> : PUEDE DEVOLVER CUALQUIER TIPO DE GENÉRICO.
	@GetMapping("/obtener-config")
	public ResponseEntity <?> obtenerConfiguracion(@Value("${server.port}")String puerto){
		//LA INFORMACIÓN DE LA CONFIGURACIÓN SE GUARDA EN UN HASHMAP CON NOMBRE DE ATRIBUTO Y VALOR.
		
		Map<String,String>json = new HashMap<>(); 
		logger.info(texto);
		json.put("texto", texto);
		json.put("puerto", puerto);
		// SE GUARDA EL JSON EN EL RESPONSE ENTITY Y SE INDICA EL STATUS DE LA RESPUESTA HTTP CON STATUS 200.
		return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}


	@Autowired
	//@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
}
