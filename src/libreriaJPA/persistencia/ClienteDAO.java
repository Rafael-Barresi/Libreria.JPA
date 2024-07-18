package libreriaJPA.persistencia;

import java.util.Optional;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Cliente;
import libreriaJPA.exepciones.MiException;

/**
 * Clase ClienteDAO que extiende de DAO<Cliente> para realizar operaciones de
 * persistencia relacionadas con la entidad Cliente. Incluye métodos para guardar,
 * editar, eliminar, dar de alta y dar de baja clientes, así como para buscar clientes
 * por su ID o documento.
 * @author Rafael
 */
public class ClienteDAO extends DAO<Cliente> {

    @Override
    public void guardar(Cliente cliente) {
        super.guardar(cliente);
    }

    @Override
    public void editar(Cliente cliente) {
        super.editar(cliente);
    }

    @Override
    public void eliminar(Cliente cliente) {
        super.eliminar(cliente);
    }

    @Override
    public void setAlta(Cliente cliente) {
        super.setAlta(cliente);
    }

    @Override
    public void setBaja(Cliente cliente) {
        super.setBaja(cliente);
    }

    /**
     * Busca y devuelve Cliente por id.
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Optional<Cliente> buscarClientePorId(Integer id) throws MiException {

        if (id == null) {
            throw new MiException("\nDebe ingresar un id.!!!");
        }

        try {
            conectar();

           Cliente cliente = em.find(Cliente.class, id);

            return Optional.of(cliente);

        } catch (NoResultException e){
            return Optional.empty();
        } finally {
            desconectar();
        }
    }
    
    /**
     * Busca un cliente por su numero de documento el cual recibe por parametros.
     * @param documento
     * @return Otional<Cliente>
     * @throws Exception 
     */
    public Optional<Cliente> buscarPorDocumento(String documento) throws Exception {

        if (documento == null) {
            throw new MiException("\nDebe indicar un numero de documento.!!! RUTA: ClienteDao_buscarPorDocumento");
        }

        try {

            conectar();

            Cliente cliente = (Cliente) em.createQuery("SELECT c FROM Cliente c WHERE c.documento = :documento")
                    .setParameter("documento", documento)
                    .getSingleResult();

            return Optional.of(cliente);

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            desconectar();
        }

    }
}
