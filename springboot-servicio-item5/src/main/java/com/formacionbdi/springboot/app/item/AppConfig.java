package com.formacionbdi.springboot.app.item;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;

@Configuration
public class AppConfig {

	// INYECTAMOS ESTE CLIENTE RESTTEMPLATE. ESTE BEAN ES UN CLIENTE QUE TRABAJA CON
	// APIREST(CLIENTE HTTP) QUE PUEDE ACCEDER A...
	// ...RECURSOS QUE ESTÁN EN OTRO MICROSERVICIO.
	@Bean(name = "clienteRest")
	@LoadBalanced // PARA HACER BALANCEO DE CARGA CON REST TEMPLATE. CON ESTO RESTTEMPLATE UA RIBBON PARA BALANCEAR.
	public RestTemplate registrarRestTemplate() {

		return new RestTemplate();
	}
	
	// ESTA ES LA CONFIGURACIÓN DEL CIRCUIT BREAKER FACTORY DE RESILIENCE4J.
	// SIN AGREGAR ESTAS CONFIGURACIONES, EL CIRCUIT BRAKER FUNCIONA CON SUS VALORES POR DEFECTO.
	
	@Bean
	public Customizer <Resilience4JCircuitBreakerFactory>defaultCustomizer(){
		//DEVUELVE UNA EXPRESIÓN LAMBDA, QUE EMITE UN ARGUMENTO FACTORY -> QUE LUEGO SE CONFIGURA.
		//DENTRO DE LAMBDA SE EMITE CADA CONFIGURACIÓN.
		// id -> : ES UN IDENTIFICADOR Y HACE REFERENCIA AL NOMBRE QUE SE LE DA AL CIRCUIT BREAKER ..
		// .. "cbFactory.create("items")" EN EL ITEMCONTROLLER.
		return factory -> factory.configureDefault(id -> {
			//SE CREA UN OBJ Resilience4JConfigBuilder Y SE LE PASA EL ID.
			//ESTE "ID" SE APLICARÁ A CUALQUIR CIRCUIT BREAKER QUE PUEDA TENER LA APP.
			return new Resilience4JConfigBuilder(id)
					//PERSONALIZACIÓN DEL CIRCUITBREAKER
					.circuitBreakerConfig(CircuitBreakerConfig.custom()
							//CONFIG. TAMAÑO VENTANA DESLIZANTE A 10 REQUEST.
							.slidingWindowSize(10)
							//TASA DE FALLAS AL 50%
							.failureRateThreshold(50)
							//TIEMPO DE ESPERA EN EL ESTADO ABIERTO A 10 SEG. EL TIEMPO DE RESTAURACIÓN DE LA CONEXIÓN 
							.waitDurationInOpenState(Duration.ofSeconds(10L))
							//NÚMERO DE REQUEST PERMITIDAS EN EL ESTADO SEMIABIERTO
							.permittedNumberOfCallsInHalfOpenState(5)
							//CONFIGURACIÓN DE LAS LLAMADAS LENTAS. NÚMERO EN PORCENTAJE. 100% ES POR DEFECTO.  SI ES MÁS DE 100% SE ACTIVA EL CIRCUIT BREAKER.
							.slowCallRateThreshold(50)
							//SE ESTABLECE EL TIEMPO MÁXIMO QUE DEBERÍA DURAR UNA LLAMADA. SI UNA LLAMADA SUPERA EL TIEMPO DESIGNADO, SE CONSIDERARÁ UNA LLAMADA LENTA. 
							.slowCallDurationThreshold(Duration.ofSeconds(2L))
							//SE CONSTRUYEN LAS CONFIGURACIONES PERSONALIZADAS
							.build())
					//TIEMPO LÍMITE POR DEFAULT (1 SEG) QUE PUEDE DEMORAR LA REQUEST ANTES DE QUE DÉ ERROR /AQUÍ NO SE INVOCA EL BUILD() YA QUE VA POR DEFECTO.
					//.timeLimiterConfig(TimeLimiterConfig.ofDefaults())
					//AHORA PERSONALIZADO A MÁS DE 1 SEG
					.timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(6L)).build())
					//CON ESTE ÚLTIMO BUILD SE CONSTRUYE TODO.
					.build();
		});
	}
	
	
}
