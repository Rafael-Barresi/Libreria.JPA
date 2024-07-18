package libreriaJPA.servicios;

import java.util.Optional;
import java.util.Scanner;
import libreriaJPA.entidades.Autor;
import libreriaJPA.exepciones.MiException;
import libreriaJPA.persistencia.AutorDao;

/**
 * Clase AutorServicio que se encarga de las funcionalidades necesarias para
 * administrar autores, incluyendo su creación, modificación, eliminación y
 * búsqueda.
 *
 * @autor Rafael
 */
public class AutorServicio {

    final AutorDao autorDao;
    final MisFunciones mf;
    final Scanner leer;

    /**
     * Constructor que inicializa las dependencias necesarias.
     */
    public AutorServicio() {
        this.autorDao = new AutorDao();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }

    /**
     * Crea un nuevo autor y lo persiste en la base de datos.
     *
     * @return El autor creado.
     * @throws Exception
     */
    public Autor crearAutor() throws Exception {

        System.out.println("\n***CREAR AUTOR***\n");

        Autor autor = new Autor();

        try {

            while (true) {
                System.out.println("\nIngrese nombre: ");
                String nombre = mf.validarString(leer);

                Optional<Autor> autorOpt = autorDao.buscarAutorPorNombre(nombre);

                if (autorOpt.isPresent()) {
                    System.out.println("\nExiste un autor con ese nombre intentelo de nuevo");

                } else {
                    autor.setNombre(nombre);
                    break;
                }
            } //FIN WHILE VALIDACION
            autor.setAlta(true);

            autorDao.guardar(autor);

        } catch (MiException e) {
            System.out.println("ERROR al cargar el Autor" + e.getMessage());
        }
        return autor;
    }

    /**
     * Modifica un autor existente.
     *
     * @throws MiException
     */
    public void modificarAutor() throws MiException {

        try {

            while (true) {

                System.out.println("\nIngrese el nombre del autor que desea modificar: ");

                String nombre = mf.validarString(leer);
                Optional<Autor> autorOpt = autorDao.buscarAutorPorNombre(nombre);

                if (autorOpt.isPresent()) {
                    Autor autor = autorOpt.get();
                    leer.next();
                    System.out.println("\nIngrese nuevo nombre: ");
                    String aux = mf.validarString(leer);
                    autor.setNombre(aux);

                    System.out.println("\n El estado del alta es: " + autor.getAlta() + "\n"
                            + "Desea modificarlo Si o No?: ");

                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        if (autor.getAlta()) {
                            autor.setAlta(Boolean.FALSE);
                        } else {
                            autor.setAlta(Boolean.TRUE);
                        }
                    } else {
                        return;
                    }

                    autorDao.editar(autor);
                    break;

                } else {
                    System.out.println("El autor no existe en la base de datos, Intentelo de nuevo: ");
                }
            }

        } catch (MiException e) {
            System.out.println("ERROR al intentar modificar Autor. " + e.getMessage());
        }
    }

    /**
     * Modifica el estado de alta de un autor.
     *
     * @throws MiException
     */
    public void modificarAltaAutor() throws MiException {

        try {

            Autor autor;

            while (true) {

                System.out.println("\nIngrese el nombre del autor a modificar: ");
                String nombre = mf.validarString(leer);

                Optional<Autor> autorOpt = autorDao.buscarAutorPorNombre(nombre);

                if (autorOpt.isPresent()) {
                    autor = autorOpt.get();
                    break;

                } else {
                    System.out.println("No existe autor con ese nombre, intentelo de nuevo:");
                }
            }

            System.out.println("\n AUTOR: " + autor.getNombre() + "\n"
                    + "ESTADO: " + autor.getAlta() + "\n"
                    + "Quiere modificarlo? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {

                autor.setAlta(autor.getAlta() ? Boolean.FALSE : Boolean.TRUE);
            } else {
                return;
            }
            //SE PERSISTE EN LA BASE DE DATOS.
            autorDao.editar(autor);

        } catch (MiException e) {
            System.out.println("ERROR en: AutorServicio/ModificarAutor. " + e.getMessage());
        }

    }

    /**
     * Elimina un autor de la base de datos.
     *
     * @throws Exception
     */
    public void eliminarAutor() throws Exception {

        try {

            Autor autor;
            String nombreComp;
            while (true) {

                System.out.println("\nIngrese el nombre del autor a eliminar: ");
                String nombre = mf.validarString(leer);

                Optional<Autor> autorOpt = autorDao.buscarAutorPorNombre(nombre);

                if (autorOpt.isPresent()) {
                    autor = autorOpt.get();
                    nombreComp = autor.getNombre();
                    break;

                } else {
                    System.out.println("No existe editorial con ese nombre, intentelo de nuevo:");
                }
            }//FIN WHILE VALIDACION EDITORIAL.

            System.out.println("\n AUTOR: " + autor.getNombre() + "\n"
                    + "ESTADO: " + autor.getAlta() + "\n"
                    + "Seguro quiere eliminar PERMANENTEMENTE el autor ? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {
                autorDao.eliminar(autor);

                if (autorDao.buscarAutorPorNombre(nombreComp).isEmpty()) {
                    System.out.println("\nEl Autor  " + nombreComp + " a sido eliminado correctamente.");

                } else {
                    return;
                }

            }
        } catch (MiException e) {
            System.out.println("ERROR no se elimino autor!!!!" + e.getMessage());
        }

    }

    /**
     * Busca un autor por su nombre o ID. Si no existe, lo crea.
     *
     * @return El autor encontrado o creado.
     * @throws Exception
     */
    public Autor cargarAutorLibro() throws Exception {

        try {
            Autor autor = new Autor();

            while (true) {

                boolean banderaId = false, banderaEntrada = false;
                Integer id;
                System.out.println("\nIngrese el nombre o el id del Autor: ");
                String entrada = leer.nextLine();

                if (entrada.matches("\\d+")) {
                    id = Integer.valueOf(entrada);

                    Optional<Autor> autorOptId = autorDao.buscarAutorPorId(id);

                    if (autorOptId.isPresent()) {
                        autor = autorOptId.get();
                        banderaId = true;
                    }
                } else {
                    Optional<Autor> autorOptNombre = autorDao.buscarAutorPorNombre(entrada);
                    if (autorOptNombre.isPresent()) {
                        autor = autorOptNombre.get();
                        banderaEntrada = true;
                    }
                }
                if (!banderaId && !banderaEntrada) {
                    System.out.println("\nNo se encontro autor con los siguentes datos: " + entrada + "\n"
                            + "Son coresctos los datos ibgresados? si o no: ");
                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        autor = crearAutor();
                        return autor;
                    } else {
                        System.out.println("Intentelo de nuevo.");
                    }
                } else {
                    break;
                }
            }
            return autor;
        } catch (MiException e) {
            System.out.println("ERROR al crear autor para libro. " + e.getMessage());
            throw e;
        }
    }

    /**
     * Modifica el autor de un libro específico.
     *
     * @param id El ID del autor a modificar.
     * @throws MiException
     */
    public void modificarAutorLibro(Integer id) throws MiException {

        if (id == null) {
            throw new MiException("\nERROR debe ingresar  un dato tipo Integer !!");
        }

        try {

            while (true) {

                Optional<Autor> autorOpt = autorDao.buscarAutorPorId(id);

                if (autorOpt.isPresent()) {
                    Autor autor = autorOpt.get();
                    System.out.println("\nIngrese nuevo nombre: ");
                    String nombre = mf.validarString(leer);
                    autor.setNombre(nombre);

                    System.out.println("\n El estado del alta es: " + autor.getAlta() + "\n"
                            + "Desea modificarlo Si o No?: ");

                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        if (autor.getAlta()) {
                            autor.setAlta(Boolean.FALSE);
                        } else {
                            autor.setAlta(Boolean.TRUE);
                        }
                    } else {
                        return;
                    }
                    System.out.println("\nEl autor se modifico con exitosi");
                    autorDao.editar(autor);
                    break;

                } else {
                    System.out.println("El autor no existe en la base de datos, Intentelo de nuevo: ");
                }
            }

        } catch (MiException e) {
            System.out.println("ERROR al intentar modificar Autor. " + e.getMessage());
        }
    }
}
