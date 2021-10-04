package com.formacionbdi.springboot.app.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

 
@RibbonClient(name="servicio-productos") //CONFIGURAMOS RIBBON EN LA CLASE PRINCIPAL, PARA EL CLIENTE FEIGN QUE TENEMOS(IProductoClienteRest).
@EnableFeignClients //HABILITAMOS EL CLIENTE FEIGN QUE PODAMOS TENER IMPLEMENTADO EN EL PROYECTO. TAMBN PERMITIRÁ INTECTAR AL CLIENTE FEIGN EN OTROS COMPONENTES DEL PROYECTO.
@SpringBootApplication
public class SpringbootServicioItemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioItemApplication.class, args);
	}

}
