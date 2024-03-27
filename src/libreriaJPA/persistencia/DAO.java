
package libreriaJPA.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import libreriaJPA.entidades.ConBooleano;

/**
 *
 * @author Rafael
 * @param <T>
 */
public class DAO<T> {

    /*
    <T> Declaramos que es generico esto significa que puede recibir cualquier tipo
    de objetos para los metodos creados dentro de la clase DAO.
    EMF creada de manera que no se pueda alterar a travez de los hijos de clase
    */
    protected final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("LibrosJPAPU");
    protected EntityManager em = EMF.createEntityManager();
    
    /*Verificamos que si conexion es falso se genera la misma.*/
    protected void conectar(){
        if (!em.isOpen()) {
            em = EMF.createEntityManager();
        }
    }
    
    /*si la conexion esta abierta la cerramos.*/
    protected void desconectar(){
        if (em.isOpen()) {
            em.close();
        }
    }
    
    protected void guardar (T objeto) {
        //conectamos a la base de datos
        conectar();
        //Iniciamos la transaccion.
        em.getTransaction().begin();
        //Persistimos el objeto
        em.persist(objeto);
        //Commit confirmamos la persistencia del objeto.
        em.getTransaction().commit();
        //cerramos conexion.
        desconectar();    
    }

    protected void editar(T objeto) {
        //conectamos a la base de datos
        conectar();
        //Iniciamos la transaccion.
        em.getTransaction().begin();
        //Aplicamos el metodo merge() para modificar.
        em.merge(objeto);
        //commit confirmamos la actualizacion.
        em.getTransaction().commit();
        //Desconectamos de la base de datos.
        desconectar();
    }
    
    protected void eliminar(T objeto) {
        //conectamos a la base de datos
        conectar();
        //Iniciamos la transaccion.
        em.getTransaction().begin();
        //instruccion de remover
        em.remove(objeto);
        //commit confirmamos la eliminacion.
        em.getTransaction().commit();
        
        desconectar();
    }
    
    protected void setAlta(T objeto) throws Exception {
        
        try{
            
            if (objeto == null) {
                throw new Exception("\nDebe indicar un objeto!");
            }
            
        if (objeto instanceof ConBooleano) {
            ConBooleano aux = (ConBooleano) objeto;
            
            aux.setAlta(true);     
            editar(objeto);    
        } 
        }catch (Exception e) {
            throw e;
        }
    }
    
    protected void setBaja (T objeto) throws Exception {
        
        try {
            
            if (objeto == null) {
                throw new Exception("\nDebe indicar un objeto.");
            }
            
            if (objeto instanceof ConBooleano) {
                ConBooleano aux = (ConBooleano) objeto;
               
                aux.setAlta(false);
                editar(objeto);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
