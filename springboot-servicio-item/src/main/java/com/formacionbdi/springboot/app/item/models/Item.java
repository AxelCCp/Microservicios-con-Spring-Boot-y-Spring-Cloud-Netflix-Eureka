package com.formacionbdi.springboot.app.item.models;

public class Item {
	

	public Item() {
	}

	public Item(Producto producto, Integer cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}
	
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	//MÉTODO PARA CALCULAR PRECIO DEL PRODUCTO:
	//CON cantidad.doubleValue() CONVERTIMOS LA CANTIDAD QUE ES DE TIPO ENTERO A TIPO DOUBLE. 
	public Double getTotal() {
		return producto.getPrecio() * cantidad.doubleValue();
	}
	
	
	private Producto producto;
	private Integer cantidad;
}
