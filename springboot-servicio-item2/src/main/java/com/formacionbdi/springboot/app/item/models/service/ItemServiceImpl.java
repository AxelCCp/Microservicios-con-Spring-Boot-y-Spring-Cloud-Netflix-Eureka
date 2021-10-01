package com.formacionbdi.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {

	// A TRAVÉS DEL CLIENTE REST OBTENEMOS EL OBJ QUE QUEREMOS DEL OTRO
	// MICROSERVICIO:
	// getForObject("URL", "TIPO DEL OBJ QUE QUEREMOS OBTENER. UNA LISTA DE
	// PRODUCTOS. EN ESTE CASO UN ARRAY");
	// USAMOS LA CLASE ARRAYS CON EL MÉTODO asList(), PARA ADAPTAR A LIST.

	// LUEGO TRANSFORMAMOS LA LISTA DE <Producto> a una lista de <item>.
	// STREAM(): PARA ESTO CONVERTIMOS "PRODUCTOS" EN UN FLUJO DE BYTES.
	// MAP(): CAMBIAMOS CADA ELEMENTO DEL FLUJO.
	// MAP(EXPRESIÓN LAMBDA... P: DE PRODUCTO / -> : FLECHA / NEW ITEM(LE PASAMOS LA
	// 'P' DE PRODUCTO , 1 : DE CANTIDAD ) ).
	// collect(Collectors.toList() : RE CONVERTIMOS LOS DATOS A LIST<>
	@Override
	public List<Item> findAll() {
		// TODO Auto-generated method stub
		List<Producto> productos = Arrays
				.asList(clienteRest.getForObject("http://localhost:8001/listar", Producto[].class));

		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		// TODO Auto-generated method stub
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		Producto producto = clienteRest.getForObject("http://localhost:8001/ver/{id}", Producto.class, pathVariables);
		return new Item(producto, cantidad);
	}

	@Autowired
	private RestTemplate clienteRest;

}
