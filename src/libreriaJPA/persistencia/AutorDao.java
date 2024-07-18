package libreriaJPA.persistencia;

import java.util.Optional;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Autor;
import libreriaJPA.exepciones.MiException;

/**
 * Clase AutorDao que extiende de DAO<Autor> para realizar operaciones de
 * persistencia relacionadas con la entidad Autor. Incluye métodos para guardar,
 * editar, eliminar, dar de alta y dar de baja autores, así como para buscar autores
 * por su ID o nombre.
 * @author Rafael
 */
public class AutorDao extends DAO<Autor> {

    @Override
    public void guardar(Autor autor) {
        super.guardar(autor);
    }

    @Override
    public void editar(Autor autor) {
        super.editar(autor);
    }

    @Override
    public void eliminar(Autor autor) {
        super.eliminar(autor);
    }

    @Override
    public void setAlta(Autor autor) {
        super.setAlta(autor);
    }

    @Override
    public void setBaja(Autor autor) {
        super.setBaja(autor);
    }

    /**
     *Busca un autor por su numero ID el cual recibe por parametro.
     * @param id
     * @return Optional<Autor>.
     * @throws MiException 
     */
    public Optional<Autor> buscarAutorPorId(Integer id) throws MiException {

        if (id == null) {
            throw new MiException("\nDebe ingresar un id.!!!");
        }

        try {
            conectar();

            Autor autor = em.find(Autor.class, id);

            return Optional.of(autor);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }

    /**
     * Busca autor por su nombre el cual recibe por parametro.
     * @param nombre
     * @return Optional<Autor>.
     * @throws MiException 
     */
    public Optional<Autor> buscarAutorPorNombre(String nombre) throws  MiException {

        if (nombre == null) {
                throw new MiException("\nDebe ingresar un nombre.");
            }
        
        try {
            
            conectar();

            Autor autor = (Autor) em.createQuery("SELECT a FROM Autor a WHERE a.nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return Optional.of(autor);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    } 

}
