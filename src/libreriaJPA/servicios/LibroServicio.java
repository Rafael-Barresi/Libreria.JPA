
package libreriaJPA.servicios;

import java.util.List;
import java.util.Scanner;
import libreriaJPA.entidades.Autor;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.entidades.Libro;
import libreriaJPA.persistencia.DAO;

/**
 *
 * @author Rafael
 */
public class LibroServicio extends DAO <Libro> {
    
    /*
     Creo clase LibroServicio y no libroDAO ya que el ejercicio asi lo solicita.
     Para recordar: una buena practica seria tener clase DAO y los servicios DAO 
     para cada entidad asi mejorar el encapsulamiento y la legibilidad del codigo
    
    Mapa de clases de mi interes seria: package Entidad con sus clases correspondientes a cada 
    entidad solicitada, package persistencia con clase DAO padre y una clase DAOServicio por cada entidad 
    hija de la clase DAO, una clase servicio por cada entidad donde desarrollar la logica de negocios,
    una clase interface Servicio por cada clase servicio que requiera interaccion con el usuario para
    obtener los datos deseados y respetar el encapsulamiento y por ultimo la clase Main donde se ejecutara
    lo anteriormente desarrollado.
     Agregado una clase controladora donde llamar los servicio y luego pasar al main

    */
    private AutorServicio as;
    private EditorialServicio es;
    private final MisFunciones mf;
    private Scanner leer;
    
    public LibroServicio(){
        this.as = new AutorServicio ();
        this.es = new EditorialServicio();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }

    /**
     * Crea un objeto Libro y lo almacena en la base de datos.
     * @throws Exception 
     */
    public void crearLibro() throws Exception {

        System.out.println("\n***CREAR LIBRO***\n");

        try {
            Libro libro = new Libro();

            System.out.println("\n- Ingrese el ISBN de 5 digitos: ");
            libro.setIsbn(mf.validarLongLargoEspecifico(leer, 5));
            
            System.out.println("\nIngrese titulo: ");
            libro.setTitulo(mf.validarString(leer));
            
            System.out.println("\nIngrese año:");
            libro.setAnio(mf.validarIntegerLargoEspecifico(leer, 4));
            
            System.out.println("\nIngrese la cantidad de ejemplares: ");
            libro.setEjemplares(mf.validarInteger(leer));
            
            System.out.println("\nIngrese la cantidad de ejemplares prestados:");
            libro.setEjemplaresPrestados(mf.validarInteger(leer));
            
            Integer restantes = libro.getEjemplares() - libro.getEjemplaresPrestados();
            libro.setEjemplaresRestantes(restantes);
            
            //Carga del Autor.
            
            System.out.println("\nIngrese el nombre del autor: ");
                
            String nomreAutor = mf.validarString(leer);
                
            Autor autor = as.buscarAutorPorNombre(nomreAutor);
           
            //Si el autor ingresado por parametro no existe en la base de datos 
            //Se maneja la inexistencia del mismo dandole al usuario de crear un nuevo 
            //autor de no desearlo el valor queda nulo.
            if (autor == null) {
                autor = as.manejarUsuarioInexistente();
                libro.setAutor(autor);
                
            } else {       
                libro.setAutor(autor);
            }
            
            //Carga de la editorial.
            
            System.out.println("\nIngrese el nombre de la editorial: ");
                
            String nombreEditorial = mf.validarString(leer);
                
            Editorial editorial = es.buscarEditorialPorNombre(nombreEditorial);
           
            //En caso de inexistencia de la editorial ingresada se le ofrece al usuario 
            //crear una nueva.
            if (editorial == null) {
                editorial = es.manejarEditorialInexistente();
                libro.setEditorial(editorial);
                
            } else {       
                libro.setEditorial(editorial);
            }
            
            guardar(libro);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }
    /**
     * Busca libro por id devuelve el objeto de coincidencia.
     * @param id
     * @return 
     */
    public Libro buscarLibroPorISBN(Long id) {

        Libro libro = null;

        try {

            if (id == null) {
                throw new Exception("\nDebe indicar el isbn.");
            }

            libro = (Libro) em.createQuery("SELECT l FROM Libro l WHERE l.isbn LIKE :id")
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (Exception e) {
            return null;
        }
        return libro;
    }
    
    /**
     * Busca libro por su titulo devuelve objeto de concidencia
     * @param titulo
     * @return 
     */
    public Libro buscarLibroPorTitulo (String titulo) {
        
        Libro libro = null;
        
        try {
            
            if (titulo == null) {
                throw new Exception("\nDebe ingresar un titulo valido.");
            }
            
            libro = (Libro) em.createQuery("SELECT l FROM Libro l WHERE l.titulo LIKE :titulo")
                    .setParameter("titulo", titulo)
                    .getSingleResult();
            
        } catch (Exception e) {
            return null;
        }
        
        return libro;
    }
    /**
     * Busca libro por nombre de autor  devuelve lista de libros
     * @param autor
     * @return 
     */
    public List<Libro> buscarLibrosPorAutor (String autor) {
        
        List<Libro> libros = null;
        
        try {
            
            if (autor == null) {
                throw new Exception("\nDebe ingresar un autor Valido.!!!");
            }
            
            libros = em.createQuery("SELECT l FROM Libro l JOIN l.autor a WHERE a.nombre LIKE :autor")
                    .setParameter("autor", autor)
                    .getResultList();
              
        } catch (Exception e) {
            return null;
        }
        
        return libros;
        
    }
    
    /**
     * Busca libro por nombre de editorial devuelve lista de libros.
     * @param editorial
     * @return 
     */
    public List<Libro> buscarLibroPorEditorial (String editorial) {
        
        List <Libro> libros = null;
        
        try {
            
            if (editorial == null) {
                throw new Exception("Debe indicar una editorial.!!!");
            }
            
            libros = em.createQuery("SELECT l FROM Libro l JOIN Editorial e WHERE e.nombre LIKE :editorial")
                    .setParameter("editorial", editorial)
                    .getResultList(); 
            
        } catch (Exception e) {
            return null;
        }
        
        return libros;
    }
    /**
     * Busca un libro por sutitulo y lo elimina de la base de datos
     * @param titulo 
     */
    public void eliminarLibro (String titulo) {
        
        try {
            
            if (titulo == null) {
                throw new Exception("\nDebe ingresar un titulo valido.");
            }
            
            Libro libro = buscarLibroPorTitulo(titulo);
            eliminar(libro);
            
            if (libro == null) {
                System.out.println("\nEl libro se Elimino con exito con exito!!!");
            }
            
        } catch (Exception e) {
            System.out.println("\nNO SE PUEDE ELIMINAR EL OBJETO\n");
        }
        
    }
    
    public void modificarAltaLibro() throws Exception {
        
        try {
            
            System.out.println("\nIngrese el nombre del libro a modificar: ");
            String alta = mf.validarString(leer);
            
            Libro libro = buscarLibroPorTitulo(alta);
            
            if (libro != null) {
                
                System.out.println("Ingrese 1 para dar de alta o 2 para dar de baja: ");
                int opc = mf.validarIntegerConLimite(leer, 1, 2);
                
                if (opc == 1) {
                    setAlta(libro);
                    
                    if (libro.getAlta()) {
                        System.out.println("\nEl atributo se modifico con exito!!!");
                    }
                    
                } else {
                    setBaja(libro);
                    
                    if (!libro.getAlta()) {
                        System.out.println("\nEl atributo se modifico con exito!!!");
                    }
                }
                
            } else {
                
                System.out.println("\nEl libro busacado no existe en la base de datos.!!");
            }
            
            
        } catch (Exception e) {
            throw e;
        }
        
    }
    
    @Override
    public void editar (Libro libro) {
        super.editar(libro);
    }

    
}
