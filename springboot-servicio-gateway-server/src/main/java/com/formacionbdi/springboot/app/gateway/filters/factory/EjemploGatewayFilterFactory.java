package com.formacionbdi.springboot.app.gateway.filters.factory;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;


//----------------------FILTRO PERSONALIZADO -------------------------//

//EL NOMBRE DE LA CLASE "EjemploGatewayFilterFactory" ESTÁ COMPUESTO POR 2 PARTES:
//Ejemplo: ES EL NOMBRE OPCIONAL QUE LE PODEMOS DAR A LA CLASE.
//GatewayFilterFactory : MIENTRAS QUE ESTA SEGUNDA PARTE DEBE SER ESCRITA DE ESTA MANERA, PARA QUE ESTA CLASE SEA RECONOCIDA POR SPRING. 

//<> : EL GENÉRICO CORRESPONDE A LA CLASE DE CONFIGURACIÓN Y A LOS PARÁMETROS QUE SE LE VAN PASAR AL FILTRO.           
@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion>{

	
	//CONSTRUCTOR
	public EjemploGatewayFilterFactory() {
		//AL CONSTRUCTOR DE LA CLASE PADRE LE PASAMOS LA CLASE Configuracion.
		super(Configuracion.class);
		
	}

	// ESTE MÉTODO RETORNA UN OBJ DE LA INTERFAZ "GatewayFilter". ESTA INTERFAZ TIENE UN SOLO MÉTODO, "filter()",.. 
	// .. EL CUAL DEVUELVE UN OBJ Mono<Void>.
	// """ Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain); """ 
	// ESTE MÉTODO ES SIMILAR A LOS MÉTODOS GLOBALES, CON UN exchange Y UN chain.
	@Override
	public GatewayFilter apply(Configuracion config) {
		// EN EL RETURN SE USA UNA FUNCIÓN LAMBDA QUE RECIBIRÁ LOS MISMOS PARÁMETROS QUE RECIBE MÉTODO FILTER() ..
		// .. DE LA INTERFAZ "GatewayFilter". NO OBSTANTE, SE USARÁN LOS PARÁMETROS DE FILTER(), SIN HACER MENCIÓN ..
		// ..DE ESTE MÉTODO FILTER() EN EL RETURN.
		
		
		
		return (exchange,chain) -> {
			//CÓDIGO CORRESPONDIENTE AL PRE: 
			
			logger.info("EJECUTANDO PRE GATEWAY FILTER FACTORY: " + config.mensaje);
			
			return chain.filter(exchange).then(Mono.fromRunnable(()-> {
				//CÓDIGO CORRESPONDIENTE AL POST:
				Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				logger.info("EJECUTANDO POST GATEWAY FILTER FACTORY: " + config.mensaje);
			}));
			
		}; 
	}
	
	
	// ESTE MÉTODO ESTÁ SOBREECRITO DE LA INTERFAZ "ShortcutConfigurable" Y ES PARA DAR UN ORDEN A LOS ARGUMENTOS QUE ..
	// .. RECIBE LA CLASE "CONFIGURACION" DESDE EL APPLICATION.YML. ACTUALMENTE LOS ARGUMENTOS ESTÁN ESCRITOS DE ESTA FORMA:
	/* args:
         mensaje: Hola mi mensaje personalizado
         cookieNombre: usuario
         cookieValor: xxxxxx
    */
	// SIM EMBARGO, EN EL APPLICATION.YML, SI SE QUIERE ESCRIBIR LOS ARGUMENTOS CON LA SIGUIENTE ABREVIACIÓN:
	// """  - Ejemplo=Hola mi mensaje personalizado, usuario, xxxxxxx  """  ...
	// ...HAY QUE USAR EL SIGUIENTE MÉTODO PARA ORDENAR LOS ARGUMENTOS:
	
	// NAAAAAHH.... HACETE UN MÉTODO List<String> CON RETURN Arrays.asList.
	/*
	public List<String> shortcutFieldOrder(){
		return Arrays.asList("mensaje", "cookieNombre", "cookieValor");
	}
	*/
	
	// CON ESTE OTRO MÉTODO SOBREESCRITO, SE CAMBIA EL NOMBRE DE LA CLASE "EjemploGatewayFilterFactory" POR ..
	// .. EjemploCookieGatewayFilterFactory.  AHORA BIEN, SI SE APLICA ESTE MÉTODO, HAY QUE IR AL APPLICATION.YML Y ..
	// .. CAMBIAR LA PALABRA ""Ejemplo" POR "EjemploCookie". PARA QUE TOME EL Gateway.
	/*
	public  String name() {
		return "EjemploCookie";
	}
	*/
	
	
	//CON LA CLASE CONFIGURACIÓN SE AGREGA UN MENSAJE Y UNA COOKIE PERSONALIZADA AL RESPONSE.
	public static class Configuracion {
		
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
	}
	
	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class); 
	
}
