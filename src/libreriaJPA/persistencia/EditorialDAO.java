package libreriaJPA.persistencia;

import java.util.Optional;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.exepciones.MiException;

/**
 * Clase EditorialDAO que extiende de DAO<Editorial> para realizar operaciones de
 * persistencia relacionadas con la entidad Editorial. Incluye métodos para guardar,
 * editar, eliminar, dar de alta y dar de baja editoriales, así como para buscar editoriales
 * por su ID o nombre.
 * 
 * @autor Rafael
 */
public class EditorialDAO extends DAO<Editorial> {

    @Override
    public void guardar(Editorial editorial) {
        super.guardar(editorial);
    }

    @Override
    public void editar(Editorial editorial) {
        super.editar(editorial);
    }

    @Override
    public void eliminar(Editorial editorial) {
        super.eliminar(editorial);
    }

    @Override
    public void setAlta(Editorial editorial) {
        super.setAlta(editorial);
    }

    @Override
    public void setBaja(Editorial editorial) {
        super.setBaja(editorial);
    }

    /**
     * Busca y devuelve una Editorial por su ID.
     *
     * @param id El ID de la editorial a buscar.
     * @return Un Optional<Editorial> que contiene la editorial si se encuentra, o vacío si no.
     * @throws MiException Si el ID proporcionado es nulo.
     */
    public Optional<Editorial> buscarEditorialPorId(Integer id) throws MiException {

        if (id == null) {
            throw new MiException("\nDebe ingresar un id.!!!");
        }

        try {
            conectar();

            Editorial editorial = em.find(Editorial.class, id);

            return Optional.ofNullable(editorial);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }
    
    /**
     * Busca y devuelve una Editorial por su nombre.
     *
     * @param nombre El nombre de la editorial a buscar.
     * @return Un Optional<Editorial> que contiene la editorial si se encuentra, o vacío si no.
     * @throws MiException Si el nombre proporcionado es nulo.
     */
    public Optional<Editorial> buscarEditorialPorNombre(String nombre) throws  MiException {

        if (nombre == null) {
                throw new MiException("\nDebe ingresar un nombre.");
            }
        
        try {
            
            conectar();

            Editorial editorial = (Editorial) em.createQuery("SELECT a FROM Editorial a WHERE a.nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return Optional.of(editorial);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    } 
    
}
