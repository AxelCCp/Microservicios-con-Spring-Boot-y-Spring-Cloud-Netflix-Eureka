package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

//HABILITAMOS EL CLIENTE FEIGN QUE PODAMOS TENER IMPLEMENTADO EN EL PROYECTO. TAMBN PERMITIRÁ INTECTAR AL CLIENTE FEIGN EN OTROS COMPONENTES DEL PROYECTO. 
@EnableFeignClients
@SpringBootApplication
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
