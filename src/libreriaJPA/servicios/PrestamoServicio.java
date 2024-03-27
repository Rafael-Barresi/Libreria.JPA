package libreriaJPA.servicios;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import libreriaJPA.entidades.Cliente;
import libreriaJPA.entidades.Libro;
import libreriaJPA.entidades.Prestamo;
import libreriaJPA.persistencia.DAO;
import libreriaJPA.persistencia.PrestamoDAO;

/**
 * PrestamoServicio Esta clase tiene la responsabilidad de llevar adelante las
 * funcionalidades necesarias para generar prestamos, va a guardar la
 * información del cliente y del libro para persistirla en la base de datos.
 * (consulta, creación, modificación y eliminación).
 *
 * @author Rafael
 */
public class PrestamoServicio extends DAO<Prestamo> {

    private Scanner leer;
    private PrestamoDAO dao;
    private MisFunciones mf;
    private LibroServicio ls;
    private ClienteServicio cs;

    public PrestamoServicio() {
        this.leer = new Scanner(System.in);
        this.dao = new PrestamoDAO();
        this.ls = new LibroServicio();
        this.mf = new MisFunciones();
        this.cs = new ClienteServicio();
    }

    public void prestamo() {

        Prestamo prestamo = null;

        //Validar Cliente
        try {

            System.out.println("\nIngrese numero de documento: ");

            Cliente cliente = null;

            Long dni;

            while (true) {

                dni = mf.validarLongConLimite(leer, 10000000, 99999999);

                cliente = cs.buscarClientePorDocumento(dni);

                if (cliente == null) {
                    System.out.println("\n¿Este es el documento ingresado? " + "\n"
                            + dni + "\n"
                            + "Respuesta por si o no:");

                    String respuesta;

                    while (true) {

                        respuesta = mf.validarString(leer);

                        if (!respuesta.equalsIgnoreCase("si") || !respuesta.equalsIgnoreCase("no")) {
                            System.out.println("\nDebe indicar si o no " + "\n"
                                    + "Intentelo de nuevo: ");

                            switch (respuesta) {

                                case "si":
                                case "SI":

                                    System.out.println("\nEl cliente no existe en la libreria, desa registrarse?");
                                    
                                    String respuesta1;
                                    
                                    while (true) {                                        
                                        
                                        respuesta1 = mf.validarString(leer);
                                        
                                        if (!respuesta.equalsIgnoreCase("si") || !respuesta.equalsIgnoreCase("no")) {
                                        System.out.println("\nDebe indicar si o no " + "\n"
                                                + "Intentelo de nuevo: ");
                                        
                                        } else {
                                            throw new Exception("Salir del programa");
                                            
                                        } 
                                        break;
                                    }
                                    
                                    if (respuesta1.equalsIgnoreCase("si")) {
                                        cs.crearCliente();
                                    } else {
                                        break;
                                    }
                                    

                            }

                        } else {
                            break;
                        }

                    }
                } else {
                    prestamo.setCliente(cliente);
                    break;
                }

            }

        } catch (Exception e) {
        }

        try {

            System.out.println("\nIndique el titulo del libro a prestamo:");

            String titulo = null;

            Libro libro;

            Date fechaPrestamo;

            Date fechaDevolucion;

            while (true) {

                //valido que el libro buscado por el cliente exista.
                while (true) {

                    titulo = mf.validarString(leer);

                    libro = ls.buscarLibroPorTitulo(titulo);

                    if (libro == null) {
                        System.out.println("\nEl libro ingresado no existe en la base de datos." + "\n"
                                + "Intentelo de nuevo: ");
                    } else {
                        break;
                    }
                }//2

                System.out.println("\nIngrese la fecha del prestamo:");

                //Valido que la fecha ingresada sea igual o futura a la actual.
                while (true) {

                    LocalDate fecha = mf.pedirFecha();
                    fechaPrestamo = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    Date f1 = new Date();

                    int comparador = f1.compareTo(fechaPrestamo);

                    if (comparador < 0) {
                        break;

                    } else {
                        System.out.println("\nLa fecha ingresada no puede ser anterior a la actual.");
                    }

                }//3

                //Si no hay eje,plares disponibles para la fecha solicitada
                //se le ofrece la fecha disponible mas cercana.
                if (libro.getEjemplaresRestantes() < 1) {

                    List<Prestamo> prestamos = dao.buscarPrestamoPorFechaIsbn(fechaPrestamo, libro.getIsbn());

                    Date fechaProxima = new Date(0);

                    for (int i = 0; i < prestamos.size(); i++) {

                        if (prestamos.get(i).getFechaDevolucion().after(fechaProxima)) {

                            fechaProxima = prestamos.get(i).getFechaDevolucion();
                        }
                    }

                    SimpleDateFormat formato = new SimpleDateFormat("dd/mm/yyyy");
                    String fechaFormateada = formato.format(fechaProxima);

                    System.out.println("\nPor el momento no hay ejemplares disponible." + "\n"
                            + "La fecha mas cercana disponible a la ingresada es el  " + fechaFormateada);

                } else {
                    //CONFIRMO CAMBIOS EN OBJETO LIBRO Y pRESTAMO
                    libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);

                    ls.editar(libro);

                    prestamo.setLibro(libro);

                    prestamo.setFechaPrestamo(fechaPrestamo);

                    break;
                }

            }//FIN DEL PRIMER WHILE.

            Date fechaDEvolucion;

            while (true) {

                LocalDate fecha = mf.pedirFecha();
                fechaDEvolucion = Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());

                int comparador = fechaPrestamo.compareTo(fechaDEvolucion);

                if (comparador > 0) {
                    prestamo.setFechaDevolucion(fechaDEvolucion);
                    break;

                } else {
                    System.out.println("\nLa fecha ingresada no puede ser posterior a la fecha de prestamo.");
                }

            }
//    protected Cliente cliente;
//    protected Boolean alta;      
        } catch (Exception e) {
            System.out.println("\nERROR no se logro cargar prestamo: " + e.getMessage());
        }
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
