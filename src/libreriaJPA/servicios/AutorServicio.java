
package libreriaJPA.servicios;

import java.util.Scanner;
import javax.persistence.NoResultException;
import libreriaJPA.entidades.Autor;
import libreriaJPA.persistencia.DAO;

/**
 *
 * @author Rafael
 */
public class AutorServicio extends DAO<Autor>{
    
    /*
    Esta clase tiene la responsabilidad de llevar adelante las funcionalidades necesarias para
    administrar autores (consulta, creación, modificación y eliminación).
    
    7) Crear los métodos para dar de alta/bajo o editar dichas entidades.
    8) Búsqueda de un Autor por nombre.
    */
    
    private MisFunciones mf;
    private Scanner leer;
    
    public AutorServicio () {
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }
    
    public Autor buscarAutorPorNombre (String nombre)throws NoResultException{
        
        Autor autor = new Autor();
        
        try {
            
           conectar();
            
           autor = (Autor) em.createQuery("SELECT a FROM Autor a WHERE a.nombre LIKE :nombre")
                   .setParameter("nombre", nombre)
                   .getSingleResult();
           
           desconectar();
                    
        } catch (NoResultException e ) {
            return null;
        }
        
        return autor;
    }
    
    public Autor crearAutor () throws Exception{
        
        System.out.println("\n***CREAR AUTOR***\n");
        
        Autor autor;
                
        try {
           
            System.out.println("Ingrese nombre: ");     
            autor = new Autor();
            autor.setNombre(mf.validarString(leer));
            autor.setAlta(true);
            
            guardar(autor);
                    
        } catch (Exception e) {
            return null;
        }
        
        return autor;
    }
    
    public void modificarAutor () throws Exception {
        
        try {
            
            System.out.println("\nIngrese el nombre del autor que desea modificar: ");
            
            String nombre = mf.validarString(leer);
            
            Autor autor = buscarAutorPorNombre(nombre);
            
            if (autor != null) {
                
                System.out.println("\nIngrese el nuevo nombre: ");
                
                autor.setNombre(mf.validarString(leer));
                editar(autor);
            
            } else {
                System.out.println("EL autor ingresado no existe en la base de datos.!!");
            }
            
            
        } catch (NoResultException e) {
            throw e;
        }
    }
    
    public void modificarAltaAutor() throws Exception {
        
        try {
            
            System.out.println("\nIngrese el nombre del autor a modificar: ");
            String alta = mf.validarString(leer);
            
            Autor autor = buscarAutorPorNombre(alta);
            
            if (autor != null) {
                
                System.out.println("\nIngrese 1 para dar de alta o 2 para dar de baja: ");
                int opc = mf.validarIntegerConLimite(leer, 1, 2);
                
                if (opc == 1) {
                    
                    setAlta(autor);
                    
                    if (autor.getAlta()) {
                        System.out.println("\nEl atributo se modifico con exito!!! " + autor.getAlta());
                    }
                    
                } else {
                    
                    setBaja(autor);
                    
                     if (!autor.getAlta()) {
                        System.out.println("\nEl atributo se modifico con exito!!! " + autor.getAlta());
                    }
                
                }
                
            } else {
                
                System.out.println("\nEl autor busacado no existe en la base de datos.!!");
            }
            
            
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    public void eliminarAutor () throws Exception{
        
        try {
            
            System.out.println("\nIngrese el nombre del autor que desea eliminar:");
            String nombre = mf.validarString(leer);
            
            Autor autor = buscarAutorPorNombre(nombre);
            
            if (autor != null) {
                
                eliminar(autor);
                
                autor = buscarAutorPorNombre(nombre);
                
                if (autor == null) {
                    
                    System.out.println("\nEl autor indicado se a eliminado de la base de datos.");
                }   
                
            } else {
                
                System.out.println("\nEl autor buscado no existe en la base de datos");
            }
            
            
        } catch (NoResultException e) {
            throw e;
        }
        
    }
    
    public Autor manejarUsuarioInexistente() throws Exception {

        Autor autor = null;

        try {

            System.out.println("El autor ingresado no existe desea crearlo?" + "\n"
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
                autor = crearAutor();

            } else {
                autor = null;
            }

        } catch (Exception e) {
            throw e;
        }

        return autor;
    }
    
    
    
}
