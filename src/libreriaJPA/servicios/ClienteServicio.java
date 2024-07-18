package libreriaJPA.servicios;

import java.util.Optional;
import java.util.Scanner;
import libreriaJPA.entidades.Cliente;
import libreriaJPA.exepciones.MiException;
import libreriaJPA.persistencia.ClienteDAO;

/**
 * Esta clase tiene la responsabilidad de llevar adelante las funcionalidades
 * necesarias para administrar clientes (consulta, creación, modificación y
 * eliminación).
 *
 * @author Rafael
 */
public class ClienteServicio {

    final ClienteDAO clienteDao;
    final Scanner leer;
    final MisFunciones mf;

    /**
     * Constructor que inicializa las dependencias necesarias.
     */
    public ClienteServicio() {

        this.clienteDao = new ClienteDAO();
        this.leer = new Scanner(System.in);
        this.mf = new MisFunciones();
    }

    /**
     * Crea un cliente y lo persiste en la base de datos. el parametro recibido
     * es utilizado cuando crearCliente es llamado desde Crear prestamo cuando
     * se ingresa el dni y no es encontrado en la base de datos se ofrece crear
     * uno nuevo y se utiliza el dni ingresado para setear el objeto
     *
     * @return Cliente
     */
    public Cliente crearCliente(Long dniParametro) {

        try {
            Cliente cliente = new Cliente();

            System.out.println("\n***CARGAR NUEVO CLIENTE***\n");

            String dni;

            // VALIDO QUE NO EXISTA OTRO CLIENTE CON EL MISMO DOCUMENTO.
            while (true) {

                Long documento;
                if (dniParametro != null) {
                    String auxDni = dniParametro.toString();
                    Optional<Cliente> cOpt = clienteDao.buscarPorDocumento(auxDni);

                    if (cOpt.isPresent()) {
                        System.out.println("ERROR el cliente ya existe en la base de datos");
                        break;

                    } else {
                        cliente.setDocumento(auxDni);
                        break;
                    }

                } else {
                    System.out.println("\nIngrese numero de documento: ");

                    documento = mf.validarLongConLimite(leer, 10000000, 99999999);

                    dni = documento.toString();
                    Optional<Cliente> clienteOpt = clienteDao.buscarPorDocumento(dni);

                    if (clienteOpt.isPresent()) {
                        System.out.println("\nYa existe un cliente con ese numero de documento " + "\n"
                                + "Intentelo de nuevo: ");

                    } else {
                        cliente.setDocumento(dni);
                        break;
                    }
                }
            } //FIN WHILE VALIDACION CLIENTE EXISTENTE.

            String nombre;
            System.out.println("\nIngrese nombre: ");
            nombre = mf.validarString(leer);

            String apellido;
            System.out.println("\nIngrese apellido: ");
            apellido = mf.validarString(leer);

            String telefono;
            System.out.println("Ingrese telefono: ");
            Long tel = mf.validarLongConLimite(leer, 100000L, Long.MAX_VALUE);
            telefono = tel.toString();

            cliente.setApellido(apellido);
            cliente.setNombre(nombre);
            cliente.setTelefono(telefono);
            cliente.setAlta(Boolean.TRUE);

            clienteDao.guardar(cliente);

            //SE BUSCA EL CLIENTE EN LA BASE DE DATOS Y SE DA MENSAJE DE ESTADO.
            Optional<Cliente> clienteOpt = clienteDao.buscarClientePorId(cliente.getId());

            if (clienteOpt.isPresent()) {
                System.out.println("\nEl cliente se ha cargado con exito.");

            } else {
                System.err.println("Error al cargar el cliente: el cliente no se encontró en la base de datos.");

            }
            return cliente;
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Modifica y persiste atributos de objeto Cliente
     *
     * @throws MiException
     * @throws Exception
     */
    public void modificarClientel() throws MiException, Exception {

        try {

            String dni;
            Cliente cliente = new Cliente();

            while (true) {

                Long documento;

                System.out.println("\nIngrese numero de documento: ");

                documento = mf.validarLongConLimite(leer, 10000000, 99999999);

                dni = documento.toString();
                Optional<Cliente> clienteOpt = clienteDao.buscarPorDocumento(dni);

                if (clienteOpt.isPresent()) {

                    cliente = clienteOpt.get();

                    System.out.println("\nIngrese Nuevo nombre: ");
                    cliente.setNombre(mf.validarString(leer));

                    System.out.println("\n El estado del alta es: " + cliente.getAlta() + "\n"
                            + "Desea modificarlo Si o No?: ");

                    String resp = mf.verificarRespuestaPorSiNo(leer);

                    if (resp.equalsIgnoreCase("si")) {
                        cliente.setAlta(cliente.getAlta() ? Boolean.FALSE : Boolean.TRUE);

                    } else {
                        clienteDao.editar(cliente);
                        break;
                    }
                }
                clienteDao.editar(cliente);
                break;
            }
        } catch (MiException e) {
            System.out.println("ERROR al intentar modificar Editorial. " + e.getMessage());
        }
    }

    /**
     * Modifica y persiste el atributo de alta (Boolean) de un cliente.
     *
     * @throws MiException
     */
    public void modificarAltaCliente() throws MiException, Exception {

        try {

            Cliente cliente;
            String dni;

            while (true) {

                Long documento;

                System.out.println("\nIngrese numero de documento: ");

                documento = mf.validarLongConLimite(leer, 10000000, 99999999);
                dni = documento.toString();

                Optional<Cliente> clienteOpt = clienteDao.buscarPorDocumento(dni);

                if (clienteOpt.isPresent()) {
                    cliente = clienteOpt.get();
                    break;
                } else {
                    System.out.println("No existe editorial con ese nombre, intentelo de nuevo:");
                }
            }//FIN WHILE VALIDACION DE CLIENTE EXISTENTE

            System.out.println("\n CLIENTE: " + cliente.getNombre() + "\n"
                    + "ESTADO: " + cliente.getAlta() + "\n"
                    + "Quiere modificarlo? si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {

                cliente.setAlta(cliente.getAlta() ? Boolean.FALSE : Boolean.TRUE);
            } else {
                return;
            }
            //SE PERSISTE EN LA BASE DE DATOS.
            clienteDao.editar(cliente);

        } catch (MiException e) {
            System.out.println("ERROR en: ClienteServicio/ModificarAltaCliente. " + e.getMessage());
        }

    }

    /**
     * Devuelve un cliente válido para realizar un préstamo, en caso de no
     * existir ofrece la opción de registrar uno nuevo.
     * Maneja cliente inexistente
     * @return Cliente
     * @throws Exception
     */
    public Cliente validarClienteParaPrestamo() throws Exception {

        Cliente cliente = new Cliente();

        try {

            System.out.println("\nIngrese numero de documento: ");

            Long documento;

            while (true) {

                long min = 2000000;
                long max = 99999999;

                documento = mf.validarLongConLimite(leer, min, max);
                leer.nextLine().trim();
                String dni = documento.toString();
                Optional<Cliente> clienteOpt = clienteDao.buscarPorDocumento(dni);

                if (clienteOpt.isEmpty()) {
                    System.out.println("\n¿Este es el documento ingresado? " + "\n"
                            + dni + "\n"
                            + "Respuesta por si o no:");

                    String respuesta = verificarRespuestaPorSiNo();

                    if (respuesta.equalsIgnoreCase("si")) {

                        System.out.println("\nEl cliente no existe en la libreria, desa registrarlo?");

                        String respuesta1 = verificarRespuestaPorSiNo();

                        if (respuesta1.equalsIgnoreCase("si")) {
                            cliente = crearCliente(documento);
                            break;
                        } else {
                           return null;
                        }

                    } else {
                        System.out.println("\n" + "Fin del prestamo. ");
                        return null;
                    }

                } else {
                    cliente = clienteOpt.get();
                    System.out.println("\nEl cliente ha sido seleccionado correctamente. ");
                    break;
                }
            }//FIN WHILE PRINCIPAL.
        } catch (Exception e) {
            System.out.println("\nERROR en la validacion de cliente para prestamo: ClienteServicio_validarClienteParaPrestamo " + "\n" + e.getMessage());
        }
        return cliente;
    }

    /**
     * Elimina un cliente a eleccion de la base de datos.
     *
     * @throws Exception
     */
    public void eliminarCliente() throws Exception {

        try {

            Cliente cliente;
            String dniComp;
            String dni;

            while (true) {

                Long documento;

                System.out.println("\nIngrese numero de documento: ");

                documento = mf.validarLongConLimite(leer, 10000000, 99999999);
                dni = documento.toString();

                Optional<Cliente> clienteOpt = clienteDao.buscarPorDocumento(dni);

                if (clienteOpt.isPresent()) {
                    cliente = clienteOpt.get();
                    dniComp = cliente.getDocumento();
                    break;

                } else {
                    System.out.println("No existe cliente con ese documento, intentelo de nuevo:");
                }
            }//FIN WHILE VALIDACION DE CLIENTE EXISTENTE.

            System.out.println("\n CLIENTE: " + cliente.getNombre() + "\n"
                    + "ESTADO: " + cliente.getAlta() + "\n"
                    + "Seguro quiere eliminar PERMANENTEMENTE el cliente?si o no: ");
            String resp = mf.verificarRespuestaPorSiNo(leer);

            if (resp.equalsIgnoreCase("si")) {
                clienteDao.eliminar(cliente);

                if (clienteDao.buscarPorDocumento(dniComp).isEmpty()) {
                    System.out.println("\nEl cliente con el numeri de documento: " + dniComp + " a sido eliminado correctamente.");

                } else {
                    return;
                }

            }
        } catch (MiException e) {
            System.out.println("ERROR no se elimino editorial!!!!" + e.getMessage());
        }

    }

    /**
     * Verifica que la respuesta ingresada sea "si" o "no".
     *
     * @return La respuesta verificada.
     */
    public String verificarRespuestaPorSiNo() {

        String respuesta;

        while (true) {

            respuesta = mf.validarString(leer);

            if (!respuesta.equalsIgnoreCase("si") && !respuesta.equalsIgnoreCase("no")) {
                System.out.println("\nDebe indicar si o no " + "\n"
                        + "Intentelo de nuevo: ");

            } else {
                break;
            }
        }
        return respuesta;
    }
}
