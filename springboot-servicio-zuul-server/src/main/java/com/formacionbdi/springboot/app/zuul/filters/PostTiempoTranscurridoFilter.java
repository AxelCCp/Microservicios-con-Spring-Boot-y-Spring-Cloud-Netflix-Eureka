package com.formacionbdi.springboot.app.zuul.filters;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

//FILTRO """PRE""" QUE CALCULA EL TIEMPO DE INICIO :::

//AL HEREDAR DE ZUULFILTER, SPRING LO IDENTIFICA CON UN FILTRO DE ZUUL. ZUULFLTER ES ABSTRACTA, POR LO TANTO HAY QUE IMPLEMENTAR SUS MÉTODOS

@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter {

	//SE CREA UN LOG PARA MOSTRAR LA INFORMACIÓN POR LA CONSOLA.
	private static Logger log = LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);
	
	
	//MÉTODO DE VALIDACIÓN. CON... TRUE: SE EJECUTA EL FILTRO  / FALSE: NO SE EJECUTA EL FILTRO.   
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	//MÉTODO QUE RESUELVE LA LÓGICA DE FILTRO QUE SE PUEDA APLICAR
	@Override
	public Object run() throws ZuulException {
		
		// TODO Auto-generated method stub
		//PARA PASAR DATOS AL Request, OBTENENOS EL OBJ HTTPRequest.
		RequestContext ctx = RequestContext.getCurrentContext();
		//CON "ctx" SE OBTIENE EL REQUEST.
		HttpServletRequest request = ctx.getRequest();
		
		//-------
		log.info("Entrando a post filter");
		//-------
		
		//SE OBTIENE EL TIEMPO DE INICIO QUE ES CALCULADO EN EL PRE.  
		Long tiempoDeInicio = (Long) request.getAttribute("tiempoDeInicio");
		Long tiempoFinal = System.currentTimeMillis();
		Long tiempoTranscurrido = tiempoFinal - tiempoDeInicio;
		log.info(String.format("Tiempo transcurrido en segundos: %s seg.", tiempoTranscurrido.doubleValue()/1000.00));
		log.info(String.format("Tiempo transcurrido en milisegundos: %s miliSeg.", tiempoTranscurrido));
		return null;
	}

	//MÉTODO EL TIPO DEL FILTRO. ES ESTE CASO : "POST"
	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "post";
	}
	
	//MÉTODO DEL ...
	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
