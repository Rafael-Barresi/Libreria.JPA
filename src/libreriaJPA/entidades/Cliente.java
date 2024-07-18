
package libreriaJPA.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entidad Cliente
*  La entidad cliente modela los clientes (a quienes se les presta libros) de la biblioteca. Se
 * almacenan los datos personales y de contacto de ese cliente.
 * @author Rafael
 */
@Entity
public class Cliente implements Serializable, ConBooleano {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column (unique = true)
    protected String documento;
    protected String nombre;
    protected String apellido;
    protected String telefono;
    protected Boolean alta;

    public Cliente() {
    }

    public Cliente(Integer id, String documento, String nombre, String apellido, String telefono, Boolean alta) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.alta = alta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", documento=" + documento + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + '}';
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
