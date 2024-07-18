package libreriaJPA;

import java.util.Scanner;
import libreriaJPA.controladores.LibroMenu;
import libreriaJPA.controladores.PrestamoMenu;
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
        LibroMenu libroMenu = new LibroMenu();
        PrestamoMenu prestamoMenu = new PrestamoMenu();
     
        System.out.println("\n *********************************************" + "\n"
                                     + " *********************************************" + "\n"
                                     + " *              LIBREROS  JPA                 *" + "\n"
                                     + " *********************************************" + "\n"
                                     + " *********************************************" + "\n\n");
        try {
            
            boolean salir = false;
           
            while (!salir) {
                
                System.out.println( "***** MENU PRINCIPAL *****\n\n"
                        + "Eliga una de las siguientes opciones: \n\n"
                        + "1 - PRESTAMOS\n"
                        + "2 - LIBROS\n"
//                        + "3 - CLIENTES.\n"        //en desarrollo. 
//                        + "4 - AUTORES\n"         //en desarrollo. 
//                        + "5 - EDITORIALES\n" //en desarrollo.
                        //Varios de los metodos de los menues en desarrollo se ejecutan en menu prestamo y libro.
                        + "3 - SALIR.\n"
                        + "OPCION: \n");
                        
                        int opc = mf.validarIntegerConLimite(leer, 1, 3);
                switch (opc) {
                    
                    case 1:
                        
                        prestamoMenu.menuPrestamo();
                        break;
                        
                    case 2:
                  
                        libroMenu.menuLibro();
                        break;
                        
                    case 3:
                        
                        System.out.println("\n***** FIN DEL PROGRAMA *****\n\n");
                        salir = true;
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
