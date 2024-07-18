
package libreriaJPA.controladores;

import java.util.Scanner;
import libreriaJPA.servicios.MisFunciones;
import libreriaJPA.servicios.PrestamoServicio;

/**
 *Esta clase modela un menu que da acceso a las operaciones disponibles
 * de los prestamos de la libreria.
 * @author Rafael
 */
public class PrestamoMenu {
    
    final PrestamoServicio prestamoServicio;
    final MisFunciones mf;
    final Scanner leer;
    
    public PrestamoMenu () {
        this.prestamoServicio = new PrestamoServicio();
        this.mf = new MisFunciones();
        this.leer = new Scanner(System.in);
    }
    public void menuPrestamo() throws Exception {

        boolean salir = false;

        while (!salir) {
            
            System.out.println(
                    "'\n***************************************************" + "\n"
                    + "----------------  PRESTAMO MENU ----------------" + "\n"
                    + "***************************************************" + "\n"
                    + "ELIGA UNA DE LAS SIGUIENTES OPCIONES: " + "\n\n"
                    + "1 - CARGAR NUEVO PRESTAMO EN BASE DE DATOS." + "\n"
                    + "2 - REALIZAR UNA DEVOLUCION DE LIBRO." + "\n"
                    + "3 - MODIFICAR ALTA PRESTAMOS." + "\n"
                    + "4 - CONSULTAR PRESTAMOS DE UN CLIENTE." + "\n"
                    + "5 - SALIR." + "\n\n"
                    + "OPCION: " + "\n");
            int opc = mf.validarIntegerConLimite(leer, 1, 5);
            
                        switch (opc) {

                case 1:
                    
                    prestamoServicio.prestamo();
                    break;

                case 2:
                    
                    prestamoServicio.devolucionLibroPrestamo();
                    break;

                case 3:
                   
                    prestamoServicio.modificarAltaPrestamo();
                    break;

                case 4:
                    
                    prestamoServicio.consultarPrestamosPorCliente();
                    break;

                case 5:
                   
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
