package com.formacionbdi.springboot.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter{
	
	//EL FILTER PASA 2 ARGUMENTOS, EL EXCHANGE Y LA CADENA.
	//EXCHANGE : CON ESTE OBJ PODEMOS ACCEDER AL REQUEST Y AL RESPONSE.

	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		logger.info("Ejecutando filtro PRE");
		return chain.filter(exchange).then(Mono.fromRunnable(() ->{
			
		}));
	}

}
