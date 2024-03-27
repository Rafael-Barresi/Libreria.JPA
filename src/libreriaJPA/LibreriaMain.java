
package libreriaJPA;

import java.util.List;
import java.util.Scanner;
import libreriaJPA.entidades.Autor;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.entidades.Libro;
import libreriaJPA.servicios.AutorServicio;
import libreriaJPA.servicios.EditorialServicio;
import libreriaJPA.servicios.LibroServicio;
import libreriaJPA.servicios.MisFunciones;

/**
 *
 * @author Rafael
 */
public class LibreriaMain {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        MisFunciones mf = new MisFunciones();

        Scanner leer = new Scanner(System.in);

        AutorServicio as = new AutorServicio();

        LibroServicio ls = new LibroServicio();

        EditorialServicio es = new EditorialServicio();

        boolean salir = false;

        try {

            while (!salir) {

                System.out.println("\n***MENU DE LIBRERIA***" + "\n"
                        + "ELIGA UNA DE LAS SIGUIENTES OPCIONES: " + "\n"
                        + "1- Búsqueda de un Autor por nombre. " + "\n"
                        + "2- Búsqueda de un libro por ISBN. " + "\n"
                        + "3- Búsqueda de un libro por Título. " + "\n"
                        + "4- Buscar libros de un autor. " + "\n"
                        + "5- Buscar libros de una editorial. " + "\n"
                        + "6- Modificar alta o baja de un autot, libro o editorial." + "\n"
                        + "7- Eliminar de la base de datos un autor, libro o editorial." + "\n"
                        + "8- Cargar libro." + "\n"
                        + "9- SALIR..." + "\n"
                        + "OPCION: ");

                Integer opc = mf.validarIntegerConLimite(leer, 1, 9);

                switch (opc) {

                    //1- Búsqueda de un Autor por nombre.
                    case 1:

                        System.out.println("\nIngrese nombre Del autor a buscar: \n");

                        String nombre = mf.validarString(leer);

                        Autor autor = as.buscarAutorPorNombre(nombre);
                        
                        if (autor == null) {
                            System.out.println("EL autor no existe en la base de datos.");
                            break;
                        }
                        
                        System.out.println(autor.toString());

                        break;

                    case 2:

                        System.out.println("\nIngrese ISBN de libro a buscar: \n");

                        Long isbn = mf.validarLongConLimite(leer, 10000, 99999);

                        Libro libro = ls.buscarLibroPorISBN(isbn);
                        
                        if (libro == null) {
                            System.out.println("\nEl libro no existe en la base de datos.");
                            break;
                        }

                        System.out.println(libro.toString());

                        break;

                    case 3:

                        System.out.println("\nIngrese el titulo del libro a buscar: \n");

                        String titulo = mf.validarString(leer);

                        Libro libro1 = ls.buscarLibroPorTitulo(titulo);
                        
                        if (libro1 == null) {
                            System.out.println("\nNo existe libro en la base de datos.");
                            break;
                        }

                        System.out.println(libro1.toString());

                        break;

                    case 4:

                        System.out.println("\nIngrese el nombre del autor: \n");

                        String nombreAutor = mf.validarString(leer);
                        
                        Autor a = as.buscarAutorPorNombre(nombreAutor);
                        
                        if (a == null) {
                            System.out.println("\nEl autor no existe en la base de datos.");
                            break;
                        }

                        List<Libro> libros = ls.buscarLibrosPorAutor(nombreAutor);

                        if (libros == null) {
                            System.out.println("No hay libros de ese autor.");

                        } else {

                            libros.stream().forEach(System.out::println);
                        }

                        break;

                    case 5:

                        System.out.println("\nIngrese el nombre de la editorial: ");

                        String editorial = mf.validarString(leer);
                        
                        Editorial e = es.buscarEditorialPorNombre(editorial);
                        
                        if (e == null) {
                            System.out.println("\nLa editorial no existe en la base de datos.");
                            break;
                        }

                        List<Libro> libros1 = ls.buscarLibroPorEditorial(editorial);

                        if (libros1 == null) {
                            System.out.println("\nNo hay Libros de esa editorial.");

                        } else {

                            System.out.println("");
                            libros1.stream().forEach(System.out::println);
                        }

                        break;

                    case 6:

                        System.out.println("\nIngrese" + "\n"
                                + "1- para modificar autor." + "\n"
                                + "2- Para modificar libro." + "\n"
                                + "3- Para modificar editorial." + "\n"
                                + "Su opcion: " + "\n");
                        Integer opcion = mf.validarIntegerConLimite(leer, 1, 3);

                        switch (opcion) {

                            case 1:

                                as.modificarAltaAutor();

                                break;

                            case 2:

                                ls.modificarAltaLibro();

                                break;

                            case 3:

                                es.modificarAltaEditorial();

                                break;
                        }
                        
                        break;

                    case 7:

                        System.out.println("\nIngrese" + "\n"
                                + "1- para eliminar autor." + "\n"
                                + "2- Para eliminar libro." + "\n"
                                + "3- Para eliminar editorial." + "\n"
                                + "Su opcion: " + "\n");
                        Integer opcion1 = mf.validarIntegerConLimite(leer, 1, 3);

                        switch (opcion1) {

                            case 1:

                                as.eliminarAutor();

                                break;

                            case 2:

                                System.out.println("\nIngrese el titulo del libro: ");
                                String titulo1 = mf.validarString(leer);

                                ls.eliminarLibro(titulo1);

                                break;

                            case 3:

                                es.eliminarEditorial();

                                break;
                        }
                        
                        break;

                    case 8:
                        
                        while (true) {

                            ls.crearLibro();

                            System.out.println("\nDesea agregar otro Libro? ");

                            String opcion2;

                            while (true) {

                                opcion2 = mf.validarString(leer);

                                if (opcion2.equalsIgnoreCase("si") || opcion2.equalsIgnoreCase("no")) {
                                    break;

                                } else {
                                    System.out.println("\nDebe ingresar si o no como respuesta valida.");
                                }
                            }

                            if (opcion2.equalsIgnoreCase("no")) {
                                break;
                            }
                        }

                        break;

                    case 9:

                        System.out.println("\n\n*****FIN DEL PROGRAMA*****\n\n");

                        salir = true;

                        break;
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
