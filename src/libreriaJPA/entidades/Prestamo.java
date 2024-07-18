
package libreriaJPA.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *Entidad Préstamo
 *modela objetos Prestamo
 * @author Rafael
 */
@Entity
public class Prestamo implements Serializable, ConBooleano {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date fechaPrestamo;
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date fechaDevolucion;
    @OneToOne
    protected Libro libro;
    @OneToOne
    @ManyToOne
    protected Cliente cliente;
    protected Boolean alta;

    public Prestamo() {
        this.fechaPrestamo = new Date();
    }

    public Prestamo(Integer id, Date fechaPrestamo, Date fechaDevolucion, Libro libro, Cliente cliente) {
        this.id = id;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.libro = libro;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Prestamo{" + "id=" + id + ", fechaPrestamo=" + fechaPrestamo + ", fechaDevolucion=" + fechaDevolucion + ", libro=" + libro + ", cliente=" + cliente + '}';
    }

    @Override
    public Boolean getAlta() {
        return alta;
    }

    @Override
    public void setAlta(Boolean alta) {
        this.alta = alta;
    }
    
    
    
}
