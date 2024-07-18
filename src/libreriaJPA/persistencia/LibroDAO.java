package libreriaJPA.persistencia;

import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Libro;
import libreriaJPA.exepciones.MiException;

/**
 * Clase LibroDAO que extiende de DAO<Libro> para realizar operaciones de
 * persistencia relacionadas con la entidad Libro. Incluye métodos para guardar,
 * editar, eliminar, dar de alta y dar de baja libros, así como para buscar libros
 * por su ISBN, título, autor o editorial.
 * 
 * @autor Rafael
 */
public class LibroDAO extends DAO<Libro> {

    @Override
    public void guardar(Libro libro) {
        super.guardar(libro);
    }

    @Override
    public void editar(Libro libro) {
        super.editar(libro);
    }

    @Override
    public void eliminar(Libro libro) {
        super.eliminar(libro);
    }

    @Override
    public void setAlta(Libro libro) {
        super.setAlta(libro);
    }

    @Override
    public void setBaja(Libro libro) {
        super.setBaja(libro);
    }

    /**
     * Busca y devuelve un Libro por su ISBN.
     *
     * @param id El ISBN del libro a buscar.
     * @return Un Optional<Libro> que contiene el libro si se encuentra, o vacío si no.
     * @throws MiException Si el ISBN proporcionado es nulo.
     */
    public Optional<Libro> buscarLibroPorISBN(Long id) throws MiException {

        if (id == null) {
            throw new MiException("\nDebe indicar el isbn.");
        }

        try {
            conectar();

            Libro libro = (Libro) em.createQuery("SELECT l FROM Libro l WHERE l.isbn = :id")
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(libro);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }

    /**
     * Busca y devuelve una lista de Libros por su título.
     *
     * @param titulo El título de los libros a buscar.
     * @return Un Optional<List<Libro>> que contiene la lista de libros si se encuentran, o vacío si no.
     * @throws MiException Si el título proporcionado es nulo.
     */
    public Optional<List<Libro>> buscarLibroPorTitulo(String titulo) throws MiException {

        if (titulo == null) {
            throw new MiException("\nDebe ingresar un titulo valido.");
        }
        try {

            conectar();

            List<Libro> libros = em.createQuery("SELECT l FROM Libro l WHERE l.titulo LIKE :titulo")
                    .setParameter("titulo", titulo)
                    .getResultList();

            return Optional.of(libros);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }

    /**
     * Busca y devuelve una lista de Libros por el nombre de su autor.
     *
     * @param autor El nombre del autor de los libros a buscar.
     * @return Un Optional<List<Libro>> que contiene la lista de libros si se encuentran, o vacío si no.
     * @throws MiException Si el nombre del autor proporcionado es nulo.
     */
    public Optional<List<Libro>> buscarLibrosPorAutor(String autor) throws MiException {

        if (autor == null) {
            throw new MiException("\nDebe ingresar un autor Valido.!!!");
        }

        try {

            conectar();

            List<Libro> libros = em.createQuery("SELECT l FROM Libro l JOIN l.autor a WHERE a.nombre LIKE :autor")
                    .setParameter("autor", autor)
                    .getResultList();

            return Optional.of(libros);

        } catch (Exception e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }

    /**
     * Busca y devuelve una lista de Libros por el nombre de su editorial.
     *
     * @param editorial El nombre de la editorial de los libros a buscar.
     * @return Un Optional<List<Libro>> que contiene la lista de libros si se encuentran, o vacío si no.
     * @throws MiException Si el nombre de la editorial proporcionado es nulo.
     */
    public Optional<List<Libro>> buscarLibroPorEditorial(String editorial) throws MiException {

        if (editorial == null) {
            throw new MiException("Debe indicar una editorial.!!!");
        }
        try {
            
            conectar();

            List<Libro> libros = em.createQuery("SELECT l FROM Libro l JOIN Editorial e WHERE e.nombre = :editorial")
                    .setParameter("editorial", editorial)
                    .getResultList();
            
            return Optional.of(libros);
        
        } catch (Exception e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }
    
}
