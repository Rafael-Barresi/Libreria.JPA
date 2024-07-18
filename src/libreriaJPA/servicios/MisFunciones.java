package libreriaJPA.servicios;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rafael
 */
public class MisFunciones {

    /**
     *
     * @return Devuelve una fecha valida
     */
    public LocalDate pedirFecha() {

        Scanner leer = new Scanner(System.in);

        int anio, mes, dia;

        System.out.println("\nIngrese el año: ");
        anio = validarIntegerConLimite(leer, 0, 3000);

        System.out.println("\nIngrese numero de mes: ");
        mes = validarIntegerConLimite(leer, 1, 12);

        int maxDias;
        if (mes == 2) {
            maxDias = (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0)) ? 29 : 28;
        } else {
            maxDias = (mes == 4 || mes == 6 || mes == 9 || mes == 11) ? 30 : 31;
        }

        System.out.println("\nIngrese numero del dia: ");
        dia = validarIntegerConLimite(leer, 1, maxDias);

        return LocalDate.of(anio, mes, dia);
    }

    /**
     * Valida que la entrada del teclado sea un String, contempla espacios.
     *
     * @param leer
     * @return String
     */
public String validarString(Scanner leer) {
        String string;

        while (true) {
            System.out.println("Por favor, ingrese una cadena:");
            string = leer.nextLine().trim(); // Trim to remove leading/trailing spaces

            if (!string.matches("^[a-zA-ZñÑ0-9 ]+$")) {
                msjeDatoNoValido();
            } else {
                break;
            }
        }

        return string;
    }


    /**
     * valida que lo que viene por teclado sea un entero y no termina hasta que
     * asi sea.
     *
     * @param leer
     * @return Integer validado.
     */
    public Integer validarInteger(Scanner leer) {

        Integer integer = 0;

        while (true) {

            if (leer.hasNextInt()) {
                integer = leer.nextInt();
                leer.nextLine();
                break;

            } else {

                System.out.println("ERROR dato no valido \n"
                        + "Intentelo de nuevo: ");
                leer.nextLine();
            }

        }

        return integer;
    }

    public Integer validarIntegerConLimite(Scanner leer, int min, int max) {

        Integer integer = 0;

        while (true) {

            try {

                if (leer.hasNextInt()) {
                    integer = leer.nextInt();

                    if (integer >= min && integer <= max) {
                        break;

                    } else {
                        msjeFueraDeRango();
                        leer.nextLine();
                    }

                } else {
                    throw new InputMismatchException();
                }

            } catch (InputMismatchException e) {
                msjeDatoNoValido();
                leer.nextLine();
            }
        }
        return integer;
    }

    public Long validarLongConLimite(Scanner leer, long min, long max) {
        String entrada;
        Long largo;

        while (true) {
            
            entrada = leer.next();

            // Verifica si la entrada es un número válido
            if (entrada.matches("\\d+")) {
                try {
                    largo = Long.parseLong(entrada);

                    // Verifica si el número está dentro del rango especificado
                    if (largo >= min && largo <= max) {
                        break;
                    } else {
                        msjeFueraDeRango();
                    }
                } catch (NumberFormatException e) {
                    msjeDatoNoValido();
                }
            } else {
                msjeDatoNoValido();
            }
        }
        return largo;
    }

    public Long validarLongLargoEspecifico(Scanner leer, int length) {
        String entrada;
        Long largo;

        while (true) {
            System.out.println("Por favor, ingrese un número con exactamente " + length + " dígitos:");
            entrada = leer.next();

            // Verifica si la entrada es un número y si tiene la longitud especificada
            if (entrada.matches("\\d{" + length + "}")) {
                try {
                    largo = Long.parseLong(entrada);
                    break;
                } catch (NumberFormatException e) {
                    msjeDatoNoValido();
                }
            } else {
                System.out.println("ERROR dato invalido!!!");;
            }
        }
        return largo;
    }

    public Integer validarIntegerLargoEspecifico(Scanner leer, int length) {

        Integer integer = 0;

        while (true) {

            if (leer.hasNextInt()) {
                integer = leer.nextInt();

                if (integer.toString().length() == length) {
                    leer.nextLine();
                    break;

                } else {

                    msjeFueraDeRango();
                    leer.nextLine();

                }

            } else {

                System.out.println("ERROR dato no valido \n"
                        + "Intentelo de nuevo: ");
                leer.nextLine();
            }

        }

        return integer;
    }

    public Double validarDouble(Scanner leer) {

        Double doble = 0d;

        while (true) {

            if (leer.hasNextDouble()) {
                doble = leer.nextDouble();
                leer.nextLine();
                break;

            } else {

                System.out.println("ERROR dato no valido \n"
                        + "Intentelo de nuevo: ");
                leer.nextLine();
            }

        }

        return doble;
    }

    /**
     * Devuelve mensaje de error para un numero fuera de rango.
     */
    public void msjeFueraDeRango() {
        System.out.println("El numero ingresado esta fuera de rango. " + "\n"
                + "intentelo de nuevo:");

    }

    /**
     * Devuelve mensaje de error para una entrada de dato no valida.
     */
    public void msjeDatoNoValido() {
        System.out.println("\nERROR, dato no valido. " + "\n"
                + "intentelo de nuevo:");
    }

    //Funcion validar cadena formato mail:
    /**
     *
     * @param correo
     * @return Devuelve true si la direccion de correo es valida o false si no
     * lo es.
     * @throws java.lang.Exception
     */
    public boolean validarCorreoElectronico(String correo) throws Exception {

        try {

            if (correo == null) {
                throw new Exception("Debe ingresar un correo electronico.");
            }

            // Patrón de expresión regular para validar una dirección de correo electrónico
            String patron = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

            // Compilar la expresión regular en un objeto Pattern
            Pattern pattern = Pattern.compile(patron);

            // Crear un objeto Matcher que evaluará si la cadena coincide con el patrón
            Matcher matcher = pattern.matcher(correo);

            // Verificar si la dirección de correo coincide con el patrón
            return matcher.matches();

        } catch (Exception e) {
            throw e;
        }
    }

    public String verificarRespuestaPorSiNo(Scanner leer) {

        String respuesta;

        while (true) {

            respuesta = validarString(leer);

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
