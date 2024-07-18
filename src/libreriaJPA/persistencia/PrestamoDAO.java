package libreriaJPA.persistencia;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Prestamo;
import libreriaJPA.exepciones.MiException;

/**
 * Clase PrestamoDAO que extiende de DAO<Prestamo> para realizar operaciones de
 * persistencia relacionadas con la entidad Prestamo. Incluye métodos para guardar,
 * editar, eliminar, dar de alta y dar de baja préstamos, así como para buscar préstamos
 * por su ID, cliente o fecha e ISBN.
 * 
 * @autor Rafael
 */
public class PrestamoDAO extends DAO<Prestamo> {

    @Override
    public void guardar(Prestamo prestamo) {
        super.guardar(prestamo);
    }

    @Override
    public void editar(Prestamo prestamo) {
        super.editar(prestamo);
    }

    @Override
    public void eliminar(Prestamo prestamo) {
        super.eliminar(prestamo);
    }

    @Override
    public void setAlta(Prestamo prestamo) {
        super.setAlta(prestamo);
    }

    @Override
    public void setBaja(Prestamo prestamo) {
        super.setBaja(prestamo);
    }

    /**
     * Busca y devuelve un Prestamo por su ID.
     *
     * @param id El ID del préstamo a buscar.
     * @return Un Optional<Prestamo> que contiene el préstamo si se encuentra, o vacío si no.
     * @throws MiException Si el ID proporcionado es nulo.
     */
    public Optional<Prestamo> buscarPrestamoPorId(Integer id) throws Exception {

        if (id == null) {
            throw new MiException("\nDebe ingresar un id. PrestamoDao/buscarPrestamoPorId   !!!");
        }

        try {

            conectar();

            Prestamo prestamo = em.find(Prestamo.class, id);
            if (prestamo != null) {
                return Optional.of(prestamo);
            } else {
                return Optional.empty();
            }

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }
    }

    /**
     * Busca y devuelve una lista de Prestamos de un cliente por su ID.
     *
     * @param idCliente El ID del cliente cuyos préstamos se buscan.
     * @return Un Optional<List<Prestamo>> que contiene la lista de préstamos si se encuentran, o vacío si no.
     * @throws Exception Si el ID del cliente proporcionado es nulo.
     */
    public Optional<List<Prestamo>> buscarPrestamosPorcliente(Integer idCliente) throws Exception {

        if (idCliente == null) {
            throw new Exception("\nDebe indicar el id de un cliente.!!!");
        }

        try {
            List<Prestamo> prestamos = null;

            conectar();

            prestamos = em.createQuery("SELECT p FROM Prestamo p JOIN p.cliente c WHERE c.id = :idCliente")
                    .setParameter("idCliente", idCliente)
                    .getResultList();

            if (prestamos != null) {
                return Optional.of(prestamos);
            } else {
                return Optional.empty();
            }

        } catch (NoResultException e) {
            return Optional.empty();

        } finally {
            desconectar();
        }
    }

    /**
     * Busca y devuelve una lista de Prestamos cuya fecha de devolución es menor a la fecha de préstamo solicitada
     * y cuyo ISBN del libro coincide con el proporcionado.
     *
     * @param fechaPrestamo La fecha de préstamo a comparar.
     * @param isbn El ISBN del libro prestado.
     * @return Un Optional<List<Prestamo>> que contiene la lista de préstamos si se encuentran, o vacío si no.
     * @throws MiException Si la fecha de préstamo proporcionada es nula.
     */
    public Optional<List<Prestamo>> buscarPrestamoPorFechaIsbn(Date fechaPrestamo, Long isbn) throws Exception {

        if (fechaPrestamo == null) {
            throw new MiException("\nDebe ingresar una fecha de prestamo.!!!");
        }

        try {

            conectar();

            List<Prestamo> prestamos = em.createQuery("SELECT p FROM Prestamo p "
                    + "JOIN p.libro l "
                    + "WHERE p.fechaDevolucion < :fechaPrestamo "
                    + "AND l.isbn = :isbn")
                    .setParameter("fechaPrestamo", fechaPrestamo)
                    .setParameter("isbn", isbn)
                    .getResultList();

            return prestamos.isEmpty() ? Optional.empty() : Optional.of(prestamos);

        } finally {
            desconectar();
        }

    }

}
