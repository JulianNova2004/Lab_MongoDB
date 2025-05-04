package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.nosql.annotations.DataFormatType;
import org.eclipse.persistence.nosql.annotations.Field;
import org.eclipse.persistence.nosql.annotations.NoSql;

/**
 *
 * @author 57300
 */
@NoSql(dataFormat = DataFormatType.MAPPED)
@Embeddable
@XmlRootElement
public class Producto{

    private String nombre;
    private String marca;
    @NotNull
    @Column(name = "fecha_compra", updatable = false)
    @Temporal(TemporalType.DATE)
    private Calendar fechaCompra;
   

    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    
    @PreUpdate
    private void updateTimestamp() {
        this.fechaCompra = Calendar.getInstance();
    }

    public void setFechaCompra(Calendar fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Calendar getFechaCompra() {
        return fechaCompra;
    }

    
}
