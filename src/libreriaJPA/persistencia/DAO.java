package libreriaJPA.persistencia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import libreriaJPA.entidades.ConBooleano;

/**
 * Clase genérica DAO que proporciona métodos básicos para realizar operaciones
 * de persistencia con la base de datos. Esta clase puede manejar cualquier tipo
 * de entidad gracias al uso de generics.
 *
 * @param <T> Tipo de la entidad gestionada por este DAO.
 * @autor Rafael
 */
public class DAO<T> {

    /**
     * EntityManagerFactory utilizado para crear instancias de EntityManager.
     * Este es final para asegurar que no puede ser alterado por las subclases.
     */
    protected final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("LibrosJPAPU");

    /**
     * EntityManager utilizado para interactuar con la base de datos.
     */
    protected EntityManager em = EMF.createEntityManager();

    /**
     * Conecta a la base de datos inicializando el EntityManager si no está ya
     * abierto.
     */
    protected void conectar() {
        if (!em.isOpen()) {
            em = EMF.createEntityManager();
        }
    }

    /**
     * Desconecta de la base de datos cerrando el EntityManager si está abierto.
     */
    protected void desconectar() {
        if (em.isOpen()) {
            em.close();
        }
    }

    /**
     * Guarda un objeto en la base de datos.
     *
     * @param objeto El objeto a guardar.
     */
    protected void guardar(T objeto) {
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

    /**
     * Edita un objeto existente en la base de datos.
     *
     * @param objeto El objeto a editar.
     */
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

    /**
     * Elimina un objeto de la base de datos.
     *
     * @param objeto El objeto a eliminar.
     */
    protected void eliminar(T objeto) {
        conectar();
        em.getTransaction().begin();

        if (!em.contains(objeto)) {
            objeto = em.merge(objeto); // Asegura que la entidad esté gestionada
        }
        em.remove(objeto);
        em.getTransaction().commit();
        desconectar();
    }

    /**
     * Establece el estado de "alta" de un objeto que implementa ConBooleano a
     * verdadero.
     *
     * @param objeto El objeto a actualizar.
     * @throws IllegalArgumentException Si el objeto es nulo o no implementa
     * ConBooleano.
     */
    protected void setAlta(T objeto) throws IllegalArgumentException {

        if (objeto == null) {
            throw new IllegalArgumentException("Debe indicar un objeto.");
        }

        if (objeto instanceof ConBooleano) {
            ConBooleano aux = (ConBooleano) objeto;

            aux.setAlta(true);
            editar(objeto);
        }
    }
    
     /**
     * Establece el estado de "baja" de un objeto que implementa ConBooleano a falso.
     *
     * @param objeto El objeto a actualizar.
     * @throws IllegalArgumentException Si el objeto es nulo o no implementa ConBooleano.
     */
    protected void setBaja(T objeto) throws IllegalArgumentException {

        if (objeto == null) {
            throw new IllegalArgumentException("Debe indicar un objeto.");
        }

        if (objeto instanceof ConBooleano) {
            ConBooleano aux = (ConBooleano) objeto;
            aux.setAlta(false);
            editar(objeto);
        }
    }
}
