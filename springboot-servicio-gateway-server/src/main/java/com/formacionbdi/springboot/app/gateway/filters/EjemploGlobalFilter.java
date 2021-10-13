package com.formacionbdi.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered{
	
	//EL FILTER PASA 2 ARGUMENTOS, EL EXCHANGE Y LA CADENA.
	//EXCHANGE : CON ESTE OBJ PODEMOS ACCEDER AL REQUEST Y AL RESPONSE, PARA MODIFICARLOS, HACER VALIDACIONES,ETC.
	//CHAIN : ES LA CADENA DE FILTRO.

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// AQUÍ VA TODO EL CÓDIGO RELACIONADO A UN PRE FILTRO.
		// MODIFICACIÓN DEL REQUEST. LA MODIFICACIÓN DEL REQUEST SIEMPRE VA EN EL PRE, ANTES DE QUE SE ENVÍE..
		// .. LA REQUEST AL MICROSERVICIO.
		// .mutate() : HACE QUE LA REQUEST SEA MUTABLE.
		logger.info("Ejecutando filtro PRE");
		exchange.getRequest().mutate().headers(h->h.add("token", "1234567"));
		
		// chain.filter() : PERMITE CONTINUAR CON LA EJECUCIÓN EN CADENA DE LOS DEMÁS FILTROS QUE PUEDAN HABER.
		// then() : EN THEN() VAN LOS FILTROS POST  
		// Mono.fromRunnable(()  : PERMITE CREAR UN OBJ REACTIVO (UN MONO<VOID>) A TRAVÉS DE UNA FUNCIÓN LAMBDA
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("Ejecutando filtro POST");
			
			// exchange.getRequest().getHeaders() : SE OBTIENE EL TOKEN QUE SE AÑADIÓ EN LA CABECERA A MODO DE EJEMPLO.
			// getFirst() : PARA OBTENER UN CONTENIDO EN PARTICULAR. 
			// OPTIONAL() : SE USA CON LAS EXPRESIONES LAMBDA Y VIENE A REEMPLAZAR A UN IF().
			// ifPresent() : SE PREGUNTA SI ESTÁ PRESENTE EL VALOR DEL TOKEN O NO.
			// (valor -> : REPRESENTA A "TOKEN".
			// exchange.getResponse().getHeaders().add("token",valor) : ESTANDO PRESENTE, AGREGA EL TOKEN A LA CABECERA DEL RESPONSE. ESTO, YA QUE ESTABA EN EL REQUEST DEL PRE, DONDE FUE AÑADIDO. 
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
				exchange.getResponse().getHeaders().add("token",valor);
			});
			
			//SE CREA UNA COOKIE CON: NOMBRE=COLOR ... Y VALOR = ROJO   
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			
			//SE CAMBIA EL TIPO DE CONTENIDO Y HACE QUE SE DEVUELVA UN TEXTO PLANO EN VEZ DE UN JSON
			exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	// CON ESTE MÉTODO SOBREESCRITO DE LA INTERFAZ "Ordered", SE LE PUEDE DAR UN ORDEN DE EJECUCIÓN A LOS FILTROS..
	// .. GLOBALES, YA QUE UN FILTRO PUEDE DEPENDER DE OTRO. AHORA BIEN, AL ASIGNARLE UN VALOR NEGATIVO AL MÉTODO..
	// .. public int getOrder(), SE CONSIGUE QUE LOS MÉTODOS SE EJECUTEN ORDENADAMENTE DESDE EL PRE AL POST.
	 
	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;  //SE CAMBIA A 1 PARA QUE TAMBIÉN PUEDA ESCRIBIR VALORES.            
	}
	
	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);

}
