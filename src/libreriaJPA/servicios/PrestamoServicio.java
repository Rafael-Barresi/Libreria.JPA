package libreriaJPA.servicios;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import libreriaJPA.entidades.Cliente;
import libreriaJPA.entidades.Libro;
import libreriaJPA.entidades.Prestamo;
import libreriaJPA.exepciones.MiException;
import libreriaJPA.persistencia.DAO;
import libreriaJPA.persistencia.PrestamoDAO;

/**
 * PrestamoServicio: Esta clase tiene la responsabilidad de gestionar las
 * funcionalidades necesarias para generar préstamos, guarda la información
 * del cliente y del libro para persistirla en la base de datos.
 * Incluye la consulta, creación, modificación y eliminación de préstamos.
 * 
 * @autor Rafael
 */
public class PrestamoServicio extends DAO<Prestamo> {

    final Scanner leer;
    final PrestamoDAO prestamoDao;
    final MisFunciones mf;
    final LibroServicio libroServicio;
    final ClienteServicio clienteServicio;

    public PrestamoServicio() {
        this.leer = new Scanner(System.in);
        this.prestamoDao = new PrestamoDAO();
        this.libroServicio = new LibroServicio();
        this.mf = new MisFunciones();
        this.clienteServicio = new ClienteServicio();
    }

    /**
     * Crea un préstamo validando un cliente y un libro que se encuentre
     * disponible para su préstamo, así como las fechas de préstamo y devolución.
     * 
     * @throws Exception Si ocurre un error durante la creación del préstamo.
     */
    public void prestamo() throws Exception {

        Prestamo prestamo = new Prestamo();

        try {
            // Validar cliente para préstamo
            Cliente cliente;
            //Devuelve objeto Cliente valido para recibir prestamo.
            cliente = clienteServicio.validarClienteParaPrestamo();
            prestamo.setCliente(cliente);

            //Carga libro 
            Libro libro;
            while (true) {

                Optional<Libro> libroOpt = libroServicio.cargarLibroPrestamo();

                if (libroOpt.isEmpty()) {
                    System.out.println("Le interesa otro titulo?  si o no " + "\n"
                            + "RESPUESTA: ");
                    String respuesta = mf.verificarRespuestaPorSiNo(leer);

                    if (respuesta.equalsIgnoreCase("no")) {
                        System.out.println("\nFin: No se realizo prestamo!!!");
                        return;
                    }
                } else {
                    libro = libroOpt.get();
                    prestamo.setLibro(libro);
                    break;
                }
            }

           // Solicitar y validar fecha de devolución
            System.out.println("Ingrese la fecha de devolucion: ");

            Date fechaDevolucion = fechaDevolucion(prestamo.getFechaPrestamo());

            prestamo.setFechaDevolucion(fechaDevolucion);

            // Configurar el préstamo como activo y guardarlo en la base de datos
            prestamo.setAlta(Boolean.TRUE);
            //Se persiste el objeto prestamo en la base de datos.
            prestamoDao.guardar(prestamo);

            System.out.println("\nSe realizo Prestamo con exito!!!");

        } catch (MiException e) {
            System.out.println("ERROR: Se produjo un error al cargar prestamos " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Da de baja el préstamo, persiste las modificaciones en el libro y en el préstamo.
     * 
     * @throws Exception Si ocurre un error durante la devolución del libro.
     */
    public void devolucionLibroPrestamo() throws Exception {

        try {

            System.out.println("\nIngrese ID del prestamo: ");
            int id = mf.validarInteger(leer);

            Optional<Prestamo> prestamoOpt = prestamoDao.buscarPrestamoPorId(id);

            if (prestamoOpt.isPresent()) {
                Prestamo prestamo = prestamoOpt.get();

                if (prestamo.getAlta().equals(Boolean.FALSE)) {
                    System.out.println("\nLa devolucion ya fue realizada.");
                    return;
                
                } else {
                    libroServicio.devolucionLibro(prestamo.getLibro().getIsbn());
                    prestamo.setAlta(false);
                    prestamoDao.editar(prestamo);
                    System.out.println("\nSe Realizo la devolucio del libro y el fin del prestamo.");
                }

            } else {
                System.out.println("\nNo se encontro Prestamo co ese ID.");
                return;
            }
        } catch (MiException e) {
            System.out.println("ERROR al intentar devolucion !!!");
        } catch (Exception e) {
            System.out.println("ERROR inesperado " + e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * Modifica el atributo Boolean alta del préstamo.
     * 
     * @throws MiException Si ocurre un error durante la modificación.
     * @throws Exception Si ocurre un error general.
     */
    public void modificarAltaPrestamo() throws MiException, Exception {

        try {

            Prestamo prestamo;

            // Solicitar y validar el ID del préstamo
            while (true) {

                System.out.println("\nIngrese el id del prestamo : ");
                int id = mf.validarInteger(leer);

                Optional<Prestamo> prestamoOpt = prestamoDao.buscarPrestamoPorId(id);

                if (prestamoOpt.isPresent()) {
                    prestamo = prestamoOpt.get();
                    break;

                } else {
                    System.out.println("No existe prestamo con ese id, intentelo de nuevo:");
                }
            }

            // Confirmar y modificar el estado del préstamo
            System.out.println("\nPRESTAMO: " + prestamo.getId() + "\n"
                    + " ESTADO: " + prestamo.getAlta() + "\n"
                    + " Quiere modificarlo? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {

                prestamo.setAlta(prestamo.getAlta() ? Boolean.FALSE : Boolean.TRUE);
                System.out.println("\nSe modifico el alta con exito!!!");
            } else {
                System.out.println("Se cancelo la modificacion del alta.");
                return;
            }
           // Persistir el cambio en la base de datos.
            prestamoDao.editar(prestamo);

        } catch (MiException e) {
            System.out.println("ERROR en: PrestamoServicio/ModificarAlta. " + e.getMessage());
        }

    }

    /**
     * Busca y muestra por pantalla todos los préstamos de un cliente.
     * 
     * @throws Exception Si ocurre un error durante la consulta.
     */
    public void consultarPrestamosPorCliente() throws Exception {

        try {

            while (true) {
                System.out.println("Ingrese el Id del cliente que quiere consultar: ");
                int id = mf.validarInteger(leer);

                Optional<List<Prestamo>> prestamosOpt = prestamoDao.buscarPrestamosPorcliente(id);

                if (prestamosOpt.isPresent() && !prestamosOpt.get().isEmpty()) {
                    List<Prestamo> prestamos = prestamosOpt.get();

                    System.out.println("\nCLIENTE: " + prestamos.get(0).getCliente().getNombre()
                            + " DOCUMENTO: " + prestamos.get(0).getCliente().getDocumento() + "\n");

                    for (int i = 0; i < prestamos.size(); i++) {
                        System.out.println("PRESTAMO ID : " + prestamos.get(i).getId() + "\n"
                                + "TITULO LIBRO: " + prestamos.get(i).getLibro().getTitulo() + "\n"
                                + "FECHA PRESTAMO: " + prestamos.get(i).getFechaPrestamo() + "\n"
                                + "FECHA DEVOLUCION: " + prestamos.get(i).getFechaDevolucion() + "\n"
                                + "ESTADO ALTA: " + prestamos.get(i).getAlta() + "\n");
                    }

                    break;

                } else {
                    System.out.println("No Existen prestamos para ese cliente.");
                    break;
                }
            }
        } catch (MiException e) {
            System.out.println("ERROR al consultar prestamo por cliente" + e.getMessage());
        }

    }

    /**
     * Devuelve una fecha válida para la devolución del préstamo.
     * 
     * @param fechaPrestamo La fecha en que se realizó el préstamo.
     * @return La fecha de devolución válida.
     * @throws Exception Si ocurre un error durante la obtención de la fecha.
     */
    public Date fechaDevolucion(Date fechaPrestamo) throws Exception {

        Date fechaDevolucion = null;

        try {

            while (true) {

                LocalDate fecha = mf.pedirFecha();
                fechaDevolucion = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());

                if (fechaPrestamo.before(fechaDevolucion)) {

                    break;

                } else {
                    System.out.println("\nLa fecha ingresada no puede ser anterior a la fecha de prestamo.");
                }

            }

        } catch (Exception e) {
            throw e;
        }

        return fechaDevolucion;
    }
}
