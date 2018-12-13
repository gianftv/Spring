package com.sopra.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name="facturas")//por metrica clase singular, la tabla plural
public class Factura implements Serializable{//implementar serializable evita problemas con la concurrencia


	private static final long serialVersionUID = 1L;
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	private String descripcion;
	

	private String observacion;
	
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	//RELACIONES EN JPA: FACTRURAS ES MUCHAS 
	
	@ManyToOne(fetch=FetchType.LAZY)//many (clase) to one (atributo)
	private Cliente cliente;
	
	/* UNA FACTURA PUEDE TENER MUCHOS ITEMS DE FACTURA(LINEAS DE FACTURA CADA UNA CON UN PRODUCTO)*/
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="factura_id")//clave foranea del tiem de factura
	private List<ItemFactura> items;
	
	
	
	
	public Factura() {
		this.items = new ArrayList<ItemFactura>();
	}



	@PrePersist
	public void prePersist() {
		
		
		createAt = new Date();
	}
	


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public Date getCreateAt() {
		return createAt;
	}


	public void setCreateAt(Date fecha) {
		this.createAt = fecha;
	}



	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


/*getter y setter creado al crear la entity itemfactura*/
	public List<ItemFactura> getItems() {
		return items;
	}



	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	/*METODO AÑADIDO AL CREAR LA ENTITY ITEMFACTURA*/
	public void addItemFactura(ItemFactura item) {
		
		this.items.add(item);		
	}
	public Double getTotal() {
		
		Double total=0.0;
		
		int size = items.size();
		
		for(int i=0; i> size;i++) 
			total += items.get(i).calcularImporte();
		
		
		return total;
	}
	

}
