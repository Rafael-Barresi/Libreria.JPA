
package libreriaJPA.persistencia;

import javax.persistence.NoResultException;
import libreriaJPA.entidades.Cliente;

/**
 *
 * @author Rafael
 */
public class ClienteDAO extends DAO<Cliente> {
    
    @Override
    public void guardar (Cliente cliente) {
        super.guardar(cliente);
    }
    
    @Override
    public void editar (Cliente cliente) {
        super.editar(cliente);
    }
    
    @Override
    public void eliminar (Cliente cliente) {
        super.eliminar(cliente);
    }
    
    @Override
    public void setAlta (Cliente cliente) throws Exception {
        super.setAlta(cliente);
    }
    
    @Override
    public void setBaja (Cliente cliente) throws Exception {
        super.setBaja(cliente);
    }
    
    /**
     * Busca y devuelve Cliente por id.
     * @param id
     * @return
     * @throws Exception 
     */
    public Cliente buscarClientePorId (Integer id) throws Exception{
        
        Cliente cliente = null;
        
        try {
            
            if (id == null) {
                throw new Exception ("\nDebe ingresar un id.!!!");
            }
            conectar();
            
              cliente = em.find(Cliente.class, id);    
            
        } catch (NoResultException e) {
            System.out.println("\nNo se encontro cliente con ese id.!!!");
        
        } finally {
            desconectar();
        }
        
        return cliente;
    }
    
    public Cliente buscarPorDocumento (Long documento) throws Exception {
        
        Cliente cliente = null;
        
        try {
            
            if (documento == null) {
                throw new Exception ("\nDebe indicar un numero de documento.!!!");
            }
            conectar();
            
            cliente = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.documento LIKE :documento")
                    .setParameter("documento", documento)
                    .getSingleResult();
            
        } catch (NoResultException e) {
            System.out.println("\nNo se encontro ningun cliente con ese numero de documento.");
            
        } finally {
            desconectar();
        }
        
        return cliente;
    }
    
}
