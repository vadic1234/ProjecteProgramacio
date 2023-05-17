import daos.*;
import model.Maquina;
import model.Producte;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
public class Application {
    //Passar al DAO -->     //TODO: llegir les propietats de la BD d'un fitxer de configuració (Properties)
    //En general -->        //TODO: Afegir un sistema de Logging per les classes.

    private static ProducteDAO producteDAO = new ProducteDAO_MySQL();            //TODO: passar a una classe DAOFactory
    private static SlotDAO slotDAO = new SlotDAO_MySQL();
    private static MaquinaDAO maquinaDAO = new MaquinaDAO_MySQL();
    private static Maquina maquina = new Maquina();
    final static File beneficisCompres = new File("files/beneficisMaquina.txt");
    final static Path fitxerBenefii = Paths.get("files/beneficisMaquina.txt");
    final static Scanner lector = new Scanner(System.in);

    public static void main(String[] args) {
                   //TODO: passar Scanner a una classe InputHelper
        int opcio = 0;

        do {
            mostrarMenu();
            opcio = Integer.parseInt(lector.nextLine());

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

        boolean sortir;

        do {
            System.out.println("Vols entrar per producte o pel nom? (producte/nom)");
            String opcio = lector.nextLine();
            sortir = false;

            switch (opcio) {
                case "producte" -> {
                    Producte producte;
                    boolean crearProducte;
                    //Crear el producte i inicialitzar-lo
                    do {
                        crearProducte = false;
                        producte = IO.crearProducte();

                        System.out.println(producte.toString());
                        System.out.print("\nEstan totes les dades correctes? (s/n): ");
                        String resposta = lector.nextLine();

                        if (resposta.equalsIgnoreCase("s")) crearProducte = true;

                        try {
                            float preuVenda = producteDAO.comprarProducte(producte);
                            maquinaDAO.modificarMaquina();
                            IO.escriureDades(beneficisCompres,Float.toString(preuVenda));
                        } catch (SQLException e) {
                            //si el producte no existeix
                            throw new RuntimeException(e);
                        }

                    } while (!crearProducte);

                    sortir = true;
                }

                case "nom" -> {
                    System.out.println("Entra el nom");
                    String nomProducte = lector.nextLine();

                    try {
                        float preuVenda = producteDAO.comprarProducte(nomProducte);

                        IO.escriureDades(beneficisCompres,Float.toString(preuVenda));
                    } catch (SQLException e) {
                        //si el producte no existeix
                        throw new RuntimeException(e);
                    }

                    sortir = true;
                }
                default -> System.out.println("Opció incorrecta");
            }
        } while (!sortir);



    }

    private static void mostrarMaquina() {
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

        ArrayList<String> beneficis = llegirFitxer(fitxerBenefii);

        float beneficiTotal = 0;
        for (String s : beneficis) {
            float benefici = Float.parseFloat(s);

            beneficiTotal = beneficiTotal+benefici;
        }
        System.out.println(beneficiTotal);
    }

    private static ArrayList<String> llegirFitxer(Path fitxerResultatsMarato) {

        ArrayList<String> resultatAtleta = new ArrayList<>();

        try (Scanner lector = new Scanner(fitxerResultatsMarato)) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();

                resultatAtleta.add(linea);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return resultatAtleta;
    }
}
