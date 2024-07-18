
package libreriaJPA.entidades;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *Clase Entidad que modela objetos Edittorial.
 * @author Rafael
 */
@Entity
public class Editorial implements Serializable, ConBooleano {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private Boolean alta;
    
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
        return "Editorial{" + "id=" + id + ", nombre=" + nombre + ", alta=" + alta + '}';
    }
}
