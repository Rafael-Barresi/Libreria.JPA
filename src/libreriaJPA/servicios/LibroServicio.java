package libreriaJPA.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import libreriaJPA.entidades.Autor;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.entidades.Libro;
import libreriaJPA.exepciones.MiException;
import libreriaJPA.persistencia.LibroDAO;

/**
 * Clase que gestiona los servicios relacionados con los libros. Permite crear,
 * modificar, eliminar y consultar libros.
 */
public class LibroServicio {

    final LibroDAO libroDao;
    final AutorServicio autorServicio;
    final EditorialServicio editorialServicio;
    final MisFunciones mf;
    final Scanner leer;

    public LibroServicio() {
        this.libroDao = new LibroDAO();
        this.autorServicio = new AutorServicio();
        this.editorialServicio = new EditorialServicio();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }

    /**
     * Crea un objeto Libro y lo persiste en la base de datos. Realiza
     * validaciones para evitar duplicados y errores de entrada.
     *
     * @throws Exception Si ocurre un error durante la creación del libro.
     */
    public void crearLibro() throws Exception {

        System.out.println("\n***CREAR LIBRO***\n");

        try {
            Libro libro = new Libro();

            // Validación de ISBN para asegurar que no exista en la BD
            while (true) {

                System.out.println("\n- Ingrese el ISBN de 5 digitos: ");
                Long isbn = mf.validarLongLargoEspecifico(leer, 5);

                Optional<Libro> libroOpt = libroDao.buscarLibroPorISBN(isbn);

                if (libroOpt.isPresent()) {
                    System.out.println("\nExiste un libro con ese isbn, intentelo de nuevo.!!!");

                } else {
                    libro.setIsbn(isbn);
                    break;
                }

            } //FIN WHILE VALIDACION ISBN SEVERIFICA QUE NO EXISTA EN LA BD

            // Limpieza del buffer antes de ingresar el título
            leer.nextLine().trim();
            System.out.println("\nIngrese titulo: ");
            libro.setTitulo(mf.validarString(leer));

            System.out.println("\nIngrese año:");

            Date fecha = new Date();
            libro.setAnio(mf.validarIntegerConLimite(leer, 1, fecha.getYear() + 1900));

            System.out.println("\nIngrese la cantidad de ejemplares: ");
            libro.setEjemplares(mf.validarInteger(leer));

            libro.setEjemplaresPrestados(0);

            //CARGA DEL AUTOR.
            Autor autor = null;
            System.out.println("\nEl Libro tiene Autor? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {
                try {
                    autor = autorServicio.cargarAutorLibro();
                } catch (MiException e) {
                    System.out.println("ERROR al cargar autor para libro!!!!" + e.getStackTrace());
                }
            }

            //Carga de la editorial.
            Editorial editorial = null;
            System.out.println("\nEl Libro tiene Editorial? si o no: ");
            String resp1 = mf.verificarRespuestaPorSiNo(leer);

            if (resp1.equalsIgnoreCase("si")) {
                try {
                    editorial = editorialServicio.cargarEditorialLibro();
                } catch (MiException e) {
                    System.out.println("ERROR al cargar editorial para libro!!!!" + e.getStackTrace());
                }
            }
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libro.setAlta(true);
            libroDao.guardar(libro);

        } catch (MiException e) {
            System.out.println("ERROR En la creacion de Libro.!!!");
            e.printStackTrace();
        }
    }

    /**
     * Verifica la existencia de un libro y permite buscar por título o ISBN. Si
     * el libro está disponible, actualiza los ejemplares prestados y restantes.
     *
     * @return Optional con el libro encontrado, vacío si no se encuentra o no
     * está disponible.
     * @throws Exception Si ocurre un error durante la búsqueda.
     */
    public Optional<Libro> cargarLibroPrestamo() throws Exception {

        try {
            Libro libro = new Libro();
            boolean salida = true;
            while (salida) {

                List<Libro> libros;

                // Bucle para solicitar el nombre o ISBN del libro
                while (true) {

                    boolean banderaISBN = false, banderaEntrada = false;
                    Long ISBN;
                    System.out.println("\nIngrese el nombre o el codigo ISBN del libro: ");
                    String entrada = mf.validarString(leer);

                    // Si la entrada es un número, se busca por ISBN
                    if (entrada.matches("\\d+")) {
                        ISBN = Long.valueOf(entrada);

                        Optional<Libro> libroOptISBN = libroDao.buscarLibroPorISBN(ISBN);

                        if (libroOptISBN.isPresent()) {
                            libro = libroOptISBN.get();
                            banderaISBN = true;
                        }

                    } else {

                        // Si la entrada es texto, se busca por título
                        Optional<List<Libro>> libroOptNombre = libroDao.buscarLibroPorTitulo(entrada);
                        if (libroOptNombre.isPresent()) {
                            libros = libroOptNombre.get();

                            // Si se encuentran múltiples libros con el mismo título
                            if (libros.size() > 1) {

                                int[] posicionLibro = new int[libros.size()];

                                for (int i = 0; i < libros.size(); i++) {
                                    posicionLibro[i] = i + 1;
                                    System.out.println("OPCION: " + (i + 1) + " - Titluo:  " + libros.get(i).getTitulo() + " ISBN: " + libros.get(i).getIsbn());
                                    System.out.println("psicion libro: " + posicionLibro[i]);
                                }
                                System.out.println("lenght: " + libros.size());
                                System.out.println("\nINDIQUE NUMERO DE OPCION: ");
                                int opc = mf.validarIntegerConLimite(leer, 1, libros.size());
                                libro = libros.get(opc - 1);

                            } else {
                                libro = libros.get(0);
                            }
                            banderaEntrada = true;
                        }
                    }
                    if (!banderaISBN && !banderaEntrada) {
                        System.out.println("\nNo se encontro Libro con los siguentes datos: " + entrada + "\n"
                                + "\nIntentelo de nuevo: ");

                    } else if (libro.getEjemplaresRestantes() < 1) {
                        System.out.println("El Libro no se encuentra disponible en este momento");
                        return Optional.empty();

                    } else {
                        libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                        libro.setEjemplaresRestantes();
                        libroDao.editar(libro);
                        salida = false;
                        break;
                    }
                }
            }
            return Optional.of(libro);

        } catch (MiException e) {
            System.out.println("ERROR al validar libro para prestamo. " + e.getMessage());
            e.printStackTrace();

            return Optional.empty();
        }
    }

    /**
     * Modifica el atributo "alta" de un libro. Permite activar o desactivar un
     * libro.
     *
     * @throws MiException Si ocurre un error durante la modificación.
     */
    public void modificarAltaLibro() throws MiException {

        try {

            Libro libro;

            // Bucle para solicitar y validar el ISBN del libro
            while (true) {

                System.out.println("\nIngrese el codigo ISBN del libro a modificar: ");
                Long ISBN = mf.validarLongLargoEspecifico(leer, 5);

                Optional<Libro> libroOpt = libroDao.buscarLibroPorISBN(ISBN);

                if (libroOpt.isPresent()) {
                    libro = libroOpt.get();
                    break;

                } else {
                    System.out.println("No existe libro con ese codigo ISBN, intentelo de nuevo:");
                }
            }

            // Mostrar información del libro y confirmar modificación
            System.out.println("\n TITULO: " + libro.getTitulo() + "\n"
                    + " ESTADO: " + libro.getAlta() + "\n"
                    + " Quiere modificarlo? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {

                libro.setAlta(libro.getAlta() ? Boolean.FALSE : Boolean.TRUE);
                System.out.println("\nEl Libro se modifico con exito!!!\n");
            } else {
                System.out.println("\nNo se modifico el alta del libro.");
                return;
            }
            //SE PERSISTE EN LA BASE DE DATOS.
            libroDao.editar(libro);

        } catch (MiException e) {
            System.out.println("ERROR en: LibroServicio/modificarAltaLibro. " + e.getMessage());
        }

    }

    /**
     * Modifica los atributos de un libro. Permite modificar título, año,
     * ejemplares, estado (alta), autor y editorial.
     *
     * @throws Exception Si ocurre un error durante la modificación.
     */
    public void modificarLibro() throws Exception {

        try {

            Libro libro;

            // Bucle para solicitar y validar el ISBN del libro
            while (true) {

                System.out.println("\nIngrese codigo ISBN del libro a modificar: ");
                Long ISBN = mf.validarLongLargoEspecifico(leer, 5);

                Optional<Libro> libroOpt = libroDao.buscarLibroPorISBN(ISBN);

                if (libroOpt.isPresent()) {
                    libro = libroOpt.get();

                    System.out.println("\nTITULO: " + libro.getTitulo() + " ISBN: " + libro.getIsbn() + "\n"
                            + " Quiere modificar este Libro? si o no:");
                    leer.nextLine().trim();
                    String respuesta = mf.verificarRespuestaPorSiNo(leer);

                    if (respuesta.equalsIgnoreCase("si")) {
                        break;
                    } else {
                        System.out.println("\nIntentelo de nuevo.");
                    }
                } else {
                    System.out.println("\nNo se encontro libro correspondiente a ISBN: " + ISBN);
                }
            }//FIN DE VALIDACION DE LIBRO PARA MODIFICAR

            // Modificación de atributos del libro
            while (true) {
                System.out.println("\nSeleccione atributo a modificar: " + "\n"
                        + "-Titulo." + "\n"
                        + "-Año." + "\n"
                        + "-Ejemplares." + "\n"
                        + "-Alta" + "\n"
                        + "-Autor." + "\n"
                        + "-Editorial" + "\n"
                        + "OPCION: ");
                String respuesta = mf.validarString(leer);

                switch (respuesta.toLowerCase()) {

                    case "titulo":
                        System.out.println("\nIngrese un nuevo titulo: ");
                        String titulo = mf.validarString(leer);
                        libro.setTitulo(titulo);

                        if (libro.getTitulo().equalsIgnoreCase(titulo)) {
                            System.out.println("\nEl titulo se acualizo co exito.!!!");
                        }
                        break;

                    case "año":
                        Date fecha = new Date();
                        System.out.println("\nIngrese nuevo añp: ");
                        leer.nextLine().trim();
                        Integer anio = mf.validarIntegerConLimite(leer, 1, fecha.getYear() + 1900);
                        libro.setAnio(anio);
                        break;

                    case "ejemplares":
                        System.out.println("\nIngrese nuevo valor: ");
                        Integer ejemplares = mf.validarInteger(leer);
                        libro.setEjemplares(ejemplares);

                        if (libro.getEjemplaresPrestados() > libro.getEjemplares()) {
                            libro.setEjemplaresPrestados(libro.getEjemplares());
                        }
                        break;

                    case "alta":
                        libro.setAlta(libro.getAlta() ? Boolean.FALSE : Boolean.TRUE);
                        System.out.println("\nSe modifico alta con exito!!!!");
                        break;

                    case "autor":

                        autorServicio.modificarAutorLibro(libro.getAutor().getId());

                        break;

                    case "editorial":
                        Editorial editorial = new Editorial();

                        if (libro.getEditorial() == null) {
                            System.out.println("\nEl Libro no posee editorial asignada, Desea agregarla? " + "\n"
                                    + " si o no? " + "\n"
                                    + " RESPUESTA:");
                            String respuesta1 = mf.verificarRespuestaPorSiNo(leer);

                            if (respuesta1.equalsIgnoreCase("si")) {
                                editorial = editorialServicio.cargarEditorialLibro();
                                libro.setEditorial(editorial);
                                break;

                            } else {
                                break;
                            }
                        } else {

                            while (true) {
                                System.out.println("\nNueva Editorial para el libro, Ingrese ID: ");
                                Integer idEditorial = mf.validarInteger(leer);
                                editorial = editorialServicio.buscarEditorial(idEditorial);

                                if (editorial == null) {
                                    System.out.println("\nDebe indicar un ID valido.!!!");

                                } else {
                                    libro.setEditorial(editorial);
                                    break;
                                }

                            }
                        }

                        libro.setEditorial(editorial);
                        break;

                    default:
                        System.out.println("\nEL dato ingresado no es una opcion valida intentelo de nuevo.!!!");
                        continue;
                }

                System.out.println("\nDesea continuar modificando? si o no " + "\n"
                        + "RESPUESTA: ");
                String confirmacion = mf.verificarRespuestaPorSiNo(leer);
                libroDao.editar(libro);

                if (confirmacion.equalsIgnoreCase("no")) {
                    break;
                }

            }
        } catch (MiException e) {
            System.out.println("\nERROR al intentar modificar libro " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un libro de la base de datos. Solicita confirmación del usuario
     * antes de eliminar.
     *
     * @throws MiException Si ocurre un error durante la eliminación.
     */
    public void eliminarLibro() throws MiException {

        try {
            Libro libro;
            Long ISBN;
            // Bucle para solicitar y validar el ISBN del libro
            while (true) {

                System.out.println("\nIngrese codigo ISBN del libro a modificar: ");
                ISBN = mf.validarLongLargoEspecifico(leer, 5);

                Optional<Libro> libroOpt = libroDao.buscarLibroPorISBN(ISBN);

                if (libroOpt.isPresent()) {
                    libro = libroOpt.get();

                    System.out.println("\nTITULO: " + libro.getTitulo() + " ISBN: " + libro.getIsbn() + "\n"
                            + "Quiere ELIMINAR PERMANENTEMENTE este Libro? si o no:");
                    leer.nextLine().trim();
                    String respuesta = mf.verificarRespuestaPorSiNo(leer);

                    if (respuesta.equalsIgnoreCase("si")) {
                        libroDao.eliminar(libro);
                        Optional<Libro> libroOpt1 = libroDao.buscarLibroPorISBN(ISBN);
                        if (libroOpt1.isEmpty()) {
                            System.out.println("\nEl Libro se elimino con Exito!!!");

                        } else {
                            System.out.println("\nNo se pudo eliminar el Libro.");
                        }
                        break;
                    } else {
                        System.out.println("\nELIMINACION cancelada.");
                        return;
                    }
                } else {
                    System.out.println("\nNo se encontro libro correspondiente a ISBN: " + ISBN);
                }
            }//FIN DE VALIDACION DE LIBRO PARA MODIFICAR
        } catch (MiException e) {
            System.out.println("ERROR al intentar eliminar libro " + e.getMessage());
        }
    }

    /**
     * Realiza la devolución de un libro. Actualiza los ejemplares prestados y
     * restantes.
     *
     * @param isbn ISBN del libro a devolver.
     * @throws MiException Si ocurre un error durante la devolución.
     */
    public void devolucionLibro(Long isbn) throws MiException {

        if (isbn == null) {
            throw new MiException("]nDebe indicar un isbn valido.");
        }

        try {

            Optional<Libro> libroOpt = libroDao.buscarLibroPorISBN(isbn);

            if (libroOpt.isPresent()) {
                Libro libro = libroOpt.get();
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
                libro.setEjemplaresRestantes();
                libroDao.editar(libro);
                System.out.println("\nEl libro se devolivo con exito.");

            } else {
                System.out.println("\nNo esxiste libro con ese ISBN !!!");
            }

        } catch (MiException e) {
            System.out.println("ERROR no fue posible realizar devolucion libro.");
        }
    }

    /**
     * Consulta libros por título y muestra la información de los libros
     * encontrados.
     *
     * @throws MiException Si ocurre un error durante la consulta.
     */
    public void consultarLibro() throws MiException {

        try {

            System.out.println("\nIngrese el titulo del libro");
            String titulo = mf.validarString(leer);

            Optional<List<Libro>> librosOpt = libroDao.buscarLibroPorTitulo(titulo);

            List<Libro> libros;

            if (librosOpt.isPresent()) {
                libros = librosOpt.get();
                mostrarLibro(libros);

            } else {
                System.out.println("\nNo se encontraron coincidencias con el titulo.");
            }

        } catch (MiException e) {
            System.out.println("ERROR al consultar libro");
        }

    }

    /**
     * Muestra la información de una lista de libros.
     *
     * @param libros Lista de libros a mostrar.
     * @throws MiException Si ocurre un error durante la visualización.
     */
    public void mostrarLibro(List<Libro> libros) throws MiException {

        if (libros == null) {
            throw new MiException("ERROR debe ingresar una lista de libros!!!");
        }
        for (int i = 0; i < libros.size(); i++) {

            System.out.println("\nTITULO: " + libros.get(i).getTitulo() + "\n"
                    + "ISBN: " + libros.get(i).getIsbn());
            if (libros.get(i).getAutor() == null) {
                System.out.println("AUTOR: No posee autor.");
            } else {
                System.out.println("AUTOR: " + libros.get(i).getAutor().getNombre());
            }
            if (libros.get(i).getEditorial() == null) {
                System.out.println("EDITORIAL: No posee editorial.");
            } else {
                System.out.println("EDITORIAL: " + libros.get(i).getEditorial().getNombre());
            }
            System.out.println("AÑO : " + libros.get(i).getAnio() + "\n"
                    + "EJEMPLARES : " + libros.get(i).getEjemplares() + "\n"
                    + "EJEMPLARES PRESTADOS: " + libros.get(i).getEjemplaresPrestados() + "\n"
                    + "EJEMPLARES RESTANTES: " + libros.get(i).getEjemplaresRestantes() + "\n"
                    + "ESTADO: " + libros.get(i).getAlta()
                    + "\n");

        }
    }
}
