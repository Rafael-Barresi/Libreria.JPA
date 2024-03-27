
package libreriaJPA.servicios;

import java.util.Scanner;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.persistencia.DAO;

/**
 *
 * @author Rafael
 */
public class EditorialServicio extends DAO<Editorial> {
    
    private MisFunciones mf;
    private Scanner leer;
    
    public EditorialServicio () {
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }
    
    public Editorial buscarEditorialPorNombre (String nombre)throws NoResultException, Exception{
        
        Editorial editorial = new Editorial();
        
        try {
            if (nombre == null) {
                throw new Exception("\nDebe ingresar un nombre.");
            }
           conectar();
            
           editorial = (Editorial) em.createQuery("SELECT a FROM Editorial a WHERE a.nombre LIKE :nombre")
                   .setParameter("nombre", nombre)
                   .getSingleResult();
           
            desconectar();
            
        } catch (NoResultException e ) {
            return null;
        }
        
        return editorial;
    }
    
    public Editorial crearAutor () throws Exception{
        
        System.out.println("\n***CREAR EDITORIAL***\n");
        
        Editorial editorial;
                
        try {
           
            System.out.println("Ingrese nombre: ");     
            editorial = new Editorial();
            editorial.setNombre(mf.validarString(leer));
            editorial.setAlta(true);
            
            guardar(editorial);
                    
        } catch (Exception e) {
            return null;
        }
        
        return editorial;
    }
    
    public void modificarEditorial () throws Exception {
        
        try {
            
            System.out.println("\nIngrese el nombre de la editorial que desea modificar: ");
            
            String nombre = mf.validarString(leer);
            
            Editorial editorial = buscarEditorialPorNombre(nombre);
            
            if (editorial != null) {
                
                System.out.println("\nIngrese el nuevo nombre: ");
                
                editorial.setNombre(mf.validarString(leer));
                editar(editorial);
            
            } else {
                System.out.println("La editorial ingresada no existe en la base de datos.!!");
            }
              
        } catch (NoResultException e) {
            throw e;
        }
    }
    
    public void modificarAltaEditorial() throws Exception {
        
        try {
            
            System.out.println("\nIngrese el nombre de la editorial a modificar: ");
            String alta = mf.validarString(leer);
            
            Editorial editorial = buscarEditorialPorNombre(alta);
            
            if (editorial != null) {
                
                System.out.println("Ingrese 1 para dar de alta o 2 para dar de baja: ");
                int opc = mf.validarIntegerConLimite(leer, 1, 2);
                
                if (opc == 1) {
                    setAlta(editorial);
                    
                    if (editorial.getAlta()) {
                        System.out.println("\nSe modifico el atributo con exito!!!");
                    }
                    
                } else {
                    setBaja(editorial);
                    
                    if (!editorial.getAlta()) {
                        System.out.println("\nSe modifico el atributo con exito!!!");
                    }
                }
                
            } else {
                
                System.out.println("\nLa editorial busacada no existe en la base de datos.!!");
            }  
            
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    public void eliminarEditorial () throws Exception{
        
        try {
            
            System.out.println("\nIngrese el nombre de la editorial que desea eliminar:");
            String nombre = mf.validarString(leer);
            
            Editorial aux = buscarEditorialPorNombre(nombre);
            
            if (aux == null){
                System.out.println("\nNo existe Editorial con ese nombre en la base de datos");
                return;
            }
            
            conectar();
            
            Editorial editorial = em.find(Editorial.class, aux.getId());
            
            if (editorial != null) {
                
                eliminar(editorial);
       
            } else {
                
                System.out.println("\nLa editorial buscada no existe en la base de datos");
                
            }
            
            
        } catch (NoResultException e) {
            throw e;
            
        } finally {
            desconectar();
    }
        
    }
    
    public Editorial manejarEditorialInexistente() throws Exception {

        Editorial editorial = null;

        try {

            System.out.println("\nLa editorial ingresada no existe desea crearlo?" + "\n"
                    + "Si O No:  ");
            String respuesta;
            //Valido respuesta si o no.
            while (true) {

                respuesta = mf.validarString(leer);

                if (respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("no")) {
                    break;

                } else {

                    System.out.println("ERROR: La respuesta esta fuera de los parametros.");
                    leer.nextLine();
                }

            }

            if (respuesta.equalsIgnoreCase("si")) {
                editorial = crearAutor();

            } else {
                editorial = null;
            }

        } catch (Exception e) {
            throw e;
        }

        return editorial;
    }

    
}
