
package libreriaJPA.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *Modela un objeto Autor
 * @author Rafael
 */
@Entity
public class Autor implements Serializable, ConBooleano {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String nombre;
    private Boolean alta;
    
    public Autor () {
        
    }
    
    public Autor (Integer id, String nombre, Boolean alta) {
        
        this.id = id;
        this.nombre = nombre;
        this.alta = alta;
    }
    
    public Integer getId () {
        return id;
    }
    
    public String getNombre () {
        return nombre;
    }
    
    @Override
    public Boolean getAlta () {
        return alta;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public void setNombre (String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public void setAlta (Boolean alta) {
        this.alta = alta;
    }

    @Override
    public String toString() {
        return "Autor{" + "id=" + id + ", nombre=" + nombre + ", alta=" + alta + '}';
    }
}
