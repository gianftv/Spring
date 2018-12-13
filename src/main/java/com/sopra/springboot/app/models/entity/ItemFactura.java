package com.sopra.springboot.app.models.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="facturas_items")
public class ItemFactura implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer cantidad;
	/*UN PRODUCTO PUEDE TENER MUCHOS ITEMS DE FACTURAS , YO PUEDO 
	 * COMPRAR MUCHAS UNIDADES DEL MISMO PRODUCTO Y GENERARE MUCHOS ITEMS DE FACTURA PARA ESE PRODUCTO
	 * EN UN PRODUCTO NUNCA VAMOS A QUERER LISTAR LOS ITEMS, CON LO CUAL NO TENDREMOS RELACION BIDIRECCIONAL NI INVERSA
	 * , NO ES NECESARIO, SIMPLEMENTE LO MAPEAMOS POR EL LADO DEL ITEM FACTURA, NO POR EL LADO DEL PRODUCTO, ES POR 
	 * TANTO UNA RELACION UNIDIRECCIONAL
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="producto_id")
	private Producto producto;
	

	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public Integer getCantidad() {
		return cantidad;
	}



	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	
	public Double calcularImporte() {
		return cantidad.doubleValue()*producto.getPrecio();
	}





}
