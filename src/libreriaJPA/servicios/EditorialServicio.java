package libreriaJPA.servicios;

import java.util.Optional;
import java.util.Scanner;
import libreriaJPA.entidades.Editorial;
import libreriaJPA.exepciones.MiException;
import libreriaJPA.persistencia.DAO;
import libreriaJPA.persistencia.EditorialDAO;

/**
 * Servicio para la gestión de editoriales.
 * Esta clase se encarga de las operaciones CRUD relacionadas con las editoriales.
 *
 * @author Rafael
 */
public class EditorialServicio extends DAO<Editorial> {

    final EditorialDAO editorialDao;
    final MisFunciones mf;
    final Scanner leer;

    public EditorialServicio() {
        this.editorialDao = new EditorialDAO();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }

    /**
     * Crea una nueva editorial y la persiste en la base de datos.
     *
     * @return Editorial creada
     * @throws Exception Si ocurre un error durante la creación
     */
    public Editorial crearEditorial() throws Exception {

        System.out.println("\n***CREAR EDITORIAL***\n");

        Editorial editorial = new Editorial();

        try {

            while (true) {
                System.out.println("\nIngrese nombre: ");
                String nombre = mf.validarString(leer);

                Optional<Editorial> editorialOpt = editorialDao.buscarEditorialPorNombre(nombre);

                if (editorialOpt.isPresent()) {
                    System.out.println("\nExiste una editorial con ese nombre intentelo de nuevo");

                } else {
                    editorial.setNombre(nombre);
                    break;
                }
            } //FIN WHILE VALIDACION
            editorial.setAlta(true);

            guardar(editorial);

        } catch (MiException e) {
            System.out.println("ERROR al cargar la Editorial" + e.getMessage());
        }
        return editorial;
    }

    /**
     * Modifica los atributos de una editorial existente y los persiste.
     *
     * @throws MiException Si ocurre un error durante la modificación
     */
    public void modificarEditorial() throws MiException {

        try {

            while (true) {

                System.out.println("\nIngrese el nombre de la editorial que desea modificar: ");

                String nombre = mf.validarString(leer);
                Optional<Editorial> editorialOpt = editorialDao.buscarEditorialPorNombre(nombre);

                if (editorialOpt.isPresent()) {
                    Editorial editorial = editorialOpt.get();
                    editorial.setNombre(nombre);

                    System.out.println("\n El estado del alta es: " + editorial.getAlta() + "\n"
                            + "Desea modificarlo Si o No?: ");

                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        if (editorial.getAlta()) {
                            editorial.setAlta(Boolean.FALSE);
                        } else {
                            editorial.setAlta(Boolean.TRUE);
                        }
                    } else {
                        return;
                    }

                    editorialDao.editar(editorial);
                    break;

                } else {
                    System.out.println("La editorial seleccionada no existe en la base de datos");
                }
            }
        } catch (MiException e) {
            System.out.println("ERROR al intentar modificar Editorial. " + e.getMessage());
        }
    }

    /**
     * Modifica el estado de alta de una editorial existente por ID y la persiste.
     *
     * @param id ID de la editorial a modificar
     * @throws MiException Si ocurre un error durante la modificación
     */
    public void modificarAltaEditorialLibro(Integer id) throws MiException {

        if (id == null) {
            throw new MiException("ERROR, debe indicar un dato de tipo Integer.");
        }

        try {

            Editorial editorial;

            while (true) {

                Optional<Editorial> editorialOpt = editorialDao.buscarEditorialPorId(id);

                if (editorialOpt.isPresent()) {
                    editorial = editorialOpt.get();
                    break;

                } else {
                    System.out.println("No existe editorial con ese nombre, intentelo de nuevo:");
                }
            }
            System.out.println("\n*************** MODIFICAR EDITORIAL DE LIBRO ******************");
            System.out.println("\nIngrese nuevo nombre");
            String nombre = mf.validarString(leer);
            editorial.setNombre(nombre);

            System.out.println("\n EDITORIAL: " + editorial.getNombre() + "\n"
                    + " ESTADO: " + editorial.getAlta() + "\n"
                    + " Quiere modificarlo? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {

                editorial.setAlta(editorial.getAlta() ? Boolean.FALSE : Boolean.TRUE);
            } else {
                return;
            }
            //SE PERSISTE EN LA BASE DE DATOS.
            System.out.println("\nSe modifico la editorial con exito");
            editorialDao.editar(editorial);

        } catch (MiException e) {
            System.out.println("ERROR en: EditorialServicio/ModificarAlta. " + e.getMessage());
        }

    }

    /**
     * Elimina una editorial existente de la base de datos.
     *
     * @throws Exception Si ocurre un error durante la eliminación
     */
    public void eliminarEditorial() throws Exception {

        try {

            Editorial editorial;
            String nombreComp;
            while (true) {

                System.out.println("\nIngrese el nombre de la editorial a eliminar: ");
                String nombre = mf.validarString(leer);

                Optional<Editorial> editorialOpt = editorialDao.buscarEditorialPorNombre(nombre);

                if (editorialOpt.isPresent()) {
                    editorial = editorialOpt.get();
                    nombreComp = editorial.getNombre();
                    break;

                } else {
                    System.out.println("No existe editorial con ese nombre, intentelo de nuevo:");
                }
            }//FIN WHILE VALIDACION EDITORIAL.

            System.out.println("\n EDITORIAL: " + editorial.getNombre() + "\n"
                    + "ESTADO: " + editorial.getAlta() + "\n"
                    + "Seguro quiere eliminar PERMANENTEMENTE la Editorial?si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {
                editorialDao.eliminar(editorial);

                if (editorialDao.buscarEditorialPorNombre(nombreComp).isEmpty()) {
                    System.out.println("\nLa editorial  " + nombreComp + " a sido eliminada correctamente.");

                } else {
                    return;
                }

            }
        } catch (MiException e) {
            System.out.println("ERROR no se elimino editorial!!!!" + e.getMessage());
        }

    }

    /**
     * Carga y devuelve una editorial, permitiendo su creación si no existe.
     *
     * @return Editorial cargada o creada
     * @throws Exception Si ocurre un error durante la carga o creación
     */
    public Editorial cargarEditorialLibro() throws Exception {

        try {
            Editorial editorial = new Editorial();

            while (true) {

                boolean banderaId = false, banderaEntrada = false;
                Integer id;
                System.out.println("\nIngrese el nombre o el id de la Editorial: ");
                String entrada = leer.nextLine();

                if (entrada.matches("\\d+")) {
                    id = Integer.valueOf(entrada);

                    Optional<Editorial> editorialOptId = editorialDao.buscarEditorialPorId(id);

                    if (editorialOptId.isPresent()) {
                        editorial = editorialOptId.get();
                        banderaId = true;
                    }
                } else {

                    Optional<Editorial> editorialOptNombre = editorialDao.buscarEditorialPorNombre(entrada);
                    if (editorialOptNombre.isPresent()) {
                        editorial = editorialOptNombre.get();
                        banderaEntrada = true;
                    }
                }
                if (!banderaId && !banderaEntrada) {
                    System.out.println("\nNo se encontro editorial con los siguentes datos: " + entrada + "\n"
                            + "Son coresctos los datos ibgresados? si o no: ");
                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        editorial = crearEditorial();
                        return editorial;
                    } else {
                        System.out.println("Intentelo de nuevo.");
                    }
                } else {
                    break;
                }
            }
            return editorial;
        } catch (MiException e) {
            System.out.println("ERROR al crear editorial para libro. " + e.getMessage());
            throw e;
        }
    }

    /**
     * Busca una editorial por su ID.
     * Este método se usa en LibroServicio para modificar la editorial de un libro determinado.
     *
     * @param id ID de la editorial a buscar
     * @return Editorial encontrada o null si no se encuentra
     * @throws MiException Si ocurre un error durante la búsqueda
     */
public Editorial buscarEditorial(Integer id) throws MiException {

    if (id == null) {
        throw new MiException("ERROR debe indicar un dato de tipo Integer.");
    }

    try {
        Optional<Editorial> editorialOpt = editorialDao.buscarEditorialPorId(id);

        if (editorialOpt.isPresent()) {
            return editorialOpt.get();
        } else {
            return null; // Maneja el caso donde no se encuentra la editorial
        }

    } catch (MiException e) {
        System.out.println("\nERROR al modificar la editorial del libro!!!");
        return null;
    }
}

}
