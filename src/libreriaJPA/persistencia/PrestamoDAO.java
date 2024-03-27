
package libreriaJPA.persistencia;

import java.util.Date;
import java.util.List;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Libro;
import libreriaJPA.entidades.Prestamo;

/**
 *
 * @author Rafael
 */
public class PrestamoDAO extends DAO<Prestamo> {
    
     @Override
    public void guardar (Prestamo prestamo) {
        super.guardar(prestamo);
    }
    
    @Override
    public void editar (Prestamo prestamo) {
        super.editar(prestamo);
    }
    
    @Override
    public void eliminar (Prestamo prestamo) {
        super.eliminar(prestamo);
    }
    
    @Override
    public void setAlta (Prestamo prestamo) throws Exception {
        super.setAlta(prestamo);
    }
    
    @Override
    public void setBaja (Prestamo prestamo) throws Exception {
        super.setBaja(prestamo);
    }
    /**
     * Busca y Devuelve objeto Prestamo por id.
     * @param id
     * @return
     * @throws Exception 
     */
    public Prestamo buscarPrestamoPorId (Integer id) throws Exception {
        
        Prestamo prestamo = null;
        
        try {
            
            if (id == null) {
                throw new Exception ("\nDebe ingresar un id.!!!");
            }
            conectar();
            prestamo = em.find(Prestamo.class, id); 
            
        } catch (NoResultException e) {
            System.out.println("\nNo se encontro cliente con ese id.!!!");
        
        } finally {
            desconectar();
        }
        
        return prestamo;
    }
    
    /**
     * Busca todos los prestamos de un cliente devuelve List.
     * @param idCliente
     * @return
     * @throws Exception 
     */
    public List<Prestamo> buscarPrestamosPorcliente (Integer idCliente) throws Exception {
        
        List<Prestamo> prestamos = null;
        
        try {
            
            if (idCliente == null) {
                throw new Exception("\nDebe indicar el id de un cliente.!!!");
            }
            
            conectar();
            
            prestamos = em.createQuery("SELECT p FROM Prestamo p JOIN p.cliente c WHERE c.id LIKE :idCliente")
                    .setParameter("idCliente", idCliente)
                    .getResultList();
            
        } catch (NoResultException e) {
            System.out.println("\nEl cliente no tiene prestamos.");
            
        } finally {
            desconectar();
        }
        
        return prestamos;
    }
    
    /**
     * Busca prestamos cuya fecha de devolucion sean menor a la fecha de prestamo solicitado.
     * @param fechaPrestamo
     * @param isbn
     * @return
     * @throws Exception 
     */
    public List<Prestamo> buscarPrestamoPorFechaIsbn (Date fechaPrestamo, Long isbn) throws Exception {
        
        List<Prestamo> prestamos = null;
        
        try {
            
            if (fechaPrestamo == null) {
                throw new Exception ("\nDebe ingresar una fecha de prestamo.!!!");
            }
            
            conectar();
            
            prestamos =  em.createQuery("SELECT p FROM Prestamo p "
                                      + "JOIN p.libro l "
                                      + "WHERE p.fechaDevolucion < :fechaPrestamo "
                                      + "AND l.isbn = :isbn")
                    .setParameter("fechaPrestamo", fechaPrestamo)
                    .setParameter("isbn", isbn)
                    .getResultList();
            
        } catch (NoResultException e) {
            System.out.println("\nNo se encontraron coincidencias en la base de datos.!!!");
            
        } finally {
            desconectar();
        }
        
        return prestamos;
    }
}
