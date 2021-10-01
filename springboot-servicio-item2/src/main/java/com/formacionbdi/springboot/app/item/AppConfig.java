package com.formacionbdi.springboot.app.item;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	// INYECTAMOS ESTE CLIENTE RESTTEMPLATE. ESTE BEAN ES UN CLIENTE QUE TRABAJA CON
	// APIREST(CLIENTE HTTP) QUE PUEDE ACCEDER A...
	// ...RECURSOS QUE ESTÁN EN OTRO MICROSERVICIO.
	@Bean(name = "clienteRest")
	public RestTemplate registrarRestTemplate() {

		return new RestTemplate();
	}
}
