
package libreriaJPA.servicios;

import java.util.Scanner;
import libreriaJPA.entidades.Cliente;
import libreriaJPA.persistencia.ClienteDAO;

/**
 *ClienteServicio
 *Esta clase tiene la responsabilidad de llevar adelante las funcionalidades necesarias para
 *administrar clientes (consulta, creación, modificación y eliminación).
 * @author Rafael
 */
public class ClienteServicio {
    
    private ClienteDAO dao;
    private Scanner leer;
    private MisFunciones mf;
    
    public ClienteServicio (){
        
        this.dao = new ClienteDAO();
        this.leer = new Scanner(System.in);
        this.mf = new MisFunciones();
    }
    
    /**
     * Crea un cliente y lo persiste en la base de datos.
     */
    public void crearCliente() {

        Cliente cliente = new Cliente();

        try {

            System.out.println("\n***CARGAR NUEVO CLIENTE***\n");

            Long documento;
            
            // VALIDO QUE NO EXISTA OTRO CLIENTE CON EL MISMO DOCUMENTO.
            while (true) {

                System.out.println("\nIngrese numero de documento: ");
                documento = mf.validarLongConLimite(leer, 10000000, 99999999);

                Cliente c1 = dao.buscarPorDocumento(cliente.getDocumento());

                if (c1 != null) {
                    System.out.println("\nYa existe un cliente con ese numero de documento " + "\n"
                            + "Intentelo de nuevo: ");

                } else {
                    
                    cliente.setDocumento(documento);
                    break;
                }

            }

            System.out.println("\nIngrese nombre: ");
            cliente.setNombre(mf.validarString(leer));
            
            System.out.println("\nIngrese apellido: ");
            cliente.setApellido(mf.validarString(leer));
            
            System.out.println("Ingrese telefono: ");
            Integer telefono = mf.validarIntegerConLimite(leer, 6, 12);
 
            String tel = telefono.toString();
            cliente.setTelefono(tel);
            
            cliente.setAlta(Boolean.FALSE);
            
            dao.guardar(cliente);
            
            //SE BUSCA EL CLIENTE EN LA BASE DE DATOS Y SE DA MENSAJE DE ESTADO.
            Cliente c1 = dao.buscarClientePorId(cliente.getId());
            
            if (c1 != null) {
                System.out.println("\nEl cliente se ha cargado con exito.");
                
            } else {
                System.out.println("ERROR al cargar cliente.!!!");
            
            }
            
        } catch (Exception e) {
            System.out.println("ERROR DE SISTEMA!!!" + e.getMessage());
        
        }
        
    }
    
    
    public Cliente buscarClientePorDocumento(Long documento) throws Exception {
        
        Cliente cliente = dao.buscarPorDocumento(documento);
        
        return cliente;
    }
    
    

}
/*

c) Tareas a realizar
1) Al alumno le toca desarrollar, las siguientes funcionalidades:
2) Creación de un Cliente nuevo
3) Crear entidad Préstamo
4) Registrar el préstamo de un libro.
5) Devolución de un libro
6) Búsqueda de todos los préstamos de un Cliente.
• Agregar validaciones a todas las funcionalidades de la aplicación:
• Validar campos obligatorios.
• No ingresar datos duplicados.
• No generar condiciones inválidas. Por ejemplo, no se debe permitir prestar más
ejemplares de los que hay, ni devolver más de los que se encuentran prestados.
No se podrán prestar libros con fecha anterior a la fecha actual, etc.
*/