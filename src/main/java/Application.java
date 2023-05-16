import daos.*;
import model.Maquina;
import model.Producte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {

    //Passar al DAO -->     //TODO: llegir les propietats de la BD d'un fitxer de configuració (Properties)
    //En general -->        //TODO: Afegir un sistema de Logging per les classes.

    private static ProducteDAO producteDAO = new ProducteDAO_MySQL();            //TODO: passar a una classe DAOFactory
    private static SlotDAO slotDAO = new SlotDAO_MySQL();
    private static MaquinaDAO maquinaDAO = new MaquinaDAO_MySQL();

    public static void main(String[] args) {

        Scanner lector = new Scanner(System.in);            //TODO: passar Scanner a una classe InputHelper
        int opcio = 0;

        do {
            mostrarMenu();
            opcio = lector.nextInt();

            switch (opcio) {
                case 1 -> mostrarMaquina();
                case 2 -> comprarProducte();
                case 3 -> mostrarInventari();
                case 4 -> afegirProductes();
                case 5 -> modificarMaquina();
                case 6 -> mostrarBenefici();
                case 0 -> System.out.println("Ens veiem!");
                default -> System.out.println("Opció no vàlida");
            }

        } while (opcio != 0);

    }

    private static void mostrarMenu() {
        System.out.println("\nMenú de la màquina expenedora");
        System.out.println("=============================");
        System.out.println("Selecciona la operació a realitzar introduïnt el número corresponent: \n");


        //Opcions per client / usuari
        System.out.println("- 1: Mostrar inventari de la màquina");
        System.out.println("- 2: Comprar un producte");

        //Opcions per administrador / manteniment
        System.out.println();
        System.out.println("- 3: Mostrar llistat de productes disponibles");
        System.out.println("- 4: Afegir producte");
        System.out.println("- 5: Modificar inventari");
        System.out.println("- 6: Mostrar benefici total");

        System.out.println();
        System.out.println("- 0 Sortir de l'aplicació");
    }
    private static void modificarMaquina() {
        /**
         * Ha de permetre:
         *      - modificar les posicions on hi ha els productes de la màquina (quin article va a cada lloc)
         *      - modificar stock d'un producte que hi ha a la màquina
         *      - afegir més ranures a la màquina
         */
    }

    private static void afegirProductes() {
        Producte producte;
        boolean crearProducte;

        //Crear el producte i inicialitzar-lo
        do {
            Scanner scanner = new Scanner(System.in);

            crearProducte = false;
            producte = IO.crearProducte();

            System.out.println(producte.toString());
            System.out.print("\nEstan totes les dades correctes? (s/n): ");
            String resposta = scanner.nextLine();

            if (resposta.equalsIgnoreCase("s")) crearProducte = true;

        } while (!crearProducte);

        //Afegir el producte a la base de dades
        try {
            producteDAO.createProducte(producte);

            System.out.println("Has afegit un nou producte! Productes actuals:");
            ArrayList<Producte> productes = producteDAO.readProductes();
            for (Producte prod : productes) {
                System.out.println("- " + prod);
            }

        } catch (SQLException e) {
            int errorCode = e.getErrorCode();

            if (errorCode == 1062) { //En cas de que el producte ja existeixi, mostrar l'error
                System.out.println("El producte ja existeix! hauries de canviar la clau primària.");
            }
        }

    }

    private static void mostrarInventari() {
        try {
            //Agafem tots els productes de la BD i els mostrem
            ArrayList<Producte> productes = producteDAO.readProductes();

            System.out.println("Inventari:");
            for (Producte prod : productes) {
                System.out.println("- " + prod);
            }

        } catch (SQLException e) {          //TODO: tractar les excepcions
            e.printStackTrace();
        }
    }

    private static void comprarProducte() {

        /**
         * Mínim: es realitza la compra indicant la posició on es troba el producte que es vol comprar
         * Ampliació (0.5 punts): es permet entrar el NOM del producte per seleccionar-lo (abans cal mostrar els
         * productes disponibles a la màquina)
         *
         * Tingueu en compte que quan s'ha venut un producte HA DE QUEDAR REFLECTIT a la BD que n'hi ha un menys.
         * (stock de la màquina es manté guardat entre reinicis del programa)
         */


    }

    private static void mostrarMaquina() {

        /** IMPORTANT **
         * S'està demanat NOM DEL PRODUCTE no el codiProducte (la taula Slot conté posició, codiProducte i stock)
         * també s'acceptarà mostrant només el codi producte, però comptarà menys.
         *
         * Posicio      Producte                Quantitat disponible
         * ===========================================================
         * 1            Patates 3D              8
         * 2            Doritos Tex Mex         6
         * 3            Coca-Cola Zero          10
         * 4            Aigua 0.5L              7
         */

        try {
            maquinaDAO.mostrarMaquina();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mostrarBenefici() {

        /** Ha de mostrar el benefici de la sessió actual de la màquina, cada producte té un cost de compra
         * i un preu de venda. La suma d'aquesta diferència de tots productes que s'han venut ens donaran el benefici.
         *
         * Simplement s'ha de donar el benefici actual des de l'últim cop que s'ha engegat la màquina. (es pot fer
         * amb un comptador de benefici que s'incrementa per cada venda que es fa)
         */

        /** AMPLIACIÓ **
         * En entrar en aquest menú ha de permetre escollir entre dues opcions: veure el benefici de la sessió actual o
         * tot el registre de la màquina.
         *
         * S'ha de crear una nova taula a la BD on es vagi realitzant un registre de les vendes o els beneficis al
         * llarg de la vida de la màquina.
         */
    }
}
