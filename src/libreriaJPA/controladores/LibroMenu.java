
package libreriaJPA.controladores;

import java.util.Scanner;
import libreriaJPA.servicios.LibroServicio;
import libreriaJPA.servicios.MisFunciones;

/**
 *Esta clase modela un menu con las operaciones disponibles
 * para los libros de la libreria
 * @author Rafael
 */
public class LibroMenu {

    final LibroServicio libroServicio;
    final MisFunciones mf;
    final Scanner leer;

    public LibroMenu() {
        this.libroServicio = new LibroServicio();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }

    public void menuLibro() throws Exception {

        boolean salir = false;

        while (!salir) {
            System.out.println(
                    "'\n***************************************************" + "\n"
                    + "----------------  LIBRO MENU ---------------------- " + "\n"
                    + "***************************************************" + "\n"
                    + "ELIGA UNA DE LAS SIGUIENTES OPCIONES: " + "\n\n"
                    + "1 - CARGAR LIBRO EN BASE DE DATOS." + "\n"
                    + "2 - MODIFICAR ALTA DE UN LIBRO." + "\n"
                    + "3 - MODIFICAR LIBRO." + "\n"
                    + "4 - ELIMINAR LIBRO DE LA BASE DE DATOS." + "\n"
                    + "5 - CONSULTAR LIBRO." + "\n"
                    + "6 - SALIR." + "\n\n"
                    + "OPCION: " + "\n");
            int opc = mf.validarIntegerConLimite(leer, 1, 6);

            switch (opc) {

                case 1:
                    libroServicio.crearLibro();
                    break;

                case 2:
                    libroServicio.modificarAltaLibro();
                    break;

                case 3:
                    libroServicio.modificarLibro();
                    break;

                case 4:
                    libroServicio.eliminarLibro();
                    break;

                case 5:
                    libroServicio.consultarLibro();
                    break;

                case 6:
                    System.out.println(
                            "\n**************************************************" + "\n"
                            + "--------------------------FIN---------------------------" + "\n"
                            + "**************************************************\n\n");
                    salir = true;
                    break;

                default:
                    throw new AssertionError();
            }

        }

    }
}
