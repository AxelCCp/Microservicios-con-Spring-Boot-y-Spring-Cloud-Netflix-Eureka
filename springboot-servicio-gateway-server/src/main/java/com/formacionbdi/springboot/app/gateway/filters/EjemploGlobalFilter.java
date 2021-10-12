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
	//EXCHANGE : CON ESTE OBJ PODEMOS ACCEDER AL REQUEST Y AL RESPONSE.

	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// AQUÍ VA TODO EL CÓDIGO RELACIONADO A UN PRE FILTRO
		logger.info("Ejecutando filtro PRE");
		//MODIFICACIÓN DEL REQUEST
		exchange.getRequest().mutate().headers(h->h.add("token", "1234567"));
		
		// chain.filter() : PERMITE CONTINUAR CON LA EJECUCIÓN EN CADENA DE LOS DEMÁS FILTROS QUE PUEDAN HABER.
		// then() : EN THEN() VAN LOS FILTROS POST  
		return chain.filter(exchange).then(Mono.fromRunnable(() -> {
			logger.info("Ejecutando filtro POST");
			
			//OPTIONAL ES COMO UN IF, EN ESTE CASO SE PREGUNTA SI ES QUE ESTA PRESENTE
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
				exchange.getResponse().getHeaders().add("token",valor);
			});
			
			//SE CREA UNA COOKIE CON: NOMBRE=COLOR ... Y VALOR = ROJO   
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
			
			//HACE QUE SE DEVUELVA UN TEXTO PLANO Y NO JSON
			exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return -1;
	}

}
