import daos.*;
import model.Maquina;
import model.Producte;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
    //Passar al DAO -->     //TODO: llegir les propietats de la BD d'un fitxer de configuració (Properties)
    //En general -->        //TODO: Afegir un sistema de Logging per les classes.
    private static final ProducteDAO producteDAO = new ProducteDAO_MySQL();            //TODO: passar a una classe DAOFactory
    private static final MaquinaDAO maquinaDAO = new MaquinaDAO_MySQL();
    final static File beneficisCompres = new File("files/beneficisMaquina.txt");
    final static Path fitxerBenefii = Paths.get("files/beneficisMaquina.txt");
    final static Scanner lector = new Scanner(System.in);
    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());
    public static void main(String[] args) {
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

    /**
     * Es mostren les dades del menú
     */
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

    /**
     * Es demana que es vol fer, i es canvia les posicions de
     * la maquina, o s'afegeix stock o ranures
     * depenent del que l'usuari fa
     */
    private static void modificarMaquina() {
        System.out.println("""
                1- Canviar posicions
                2- Afegir Stock
                3- Afegir ranures
                """);
        int opcioModificar = Integer.parseInt(lector.nextLine());
        switch (opcioModificar) {
            case 1 -> {
                System.out.println("Entra les posicions que vols canviar: ");
                System.out.println("Posició 1");
                int pos1 = Integer.parseInt(lector.nextLine());
                System.out.println("Posició 2");
                int pos2 = Integer.parseInt(lector.nextLine());
                maquinaDAO.modificarMaquina(pos1, pos2);
            }
            case 2 -> {
                System.out.println("Entra el codi del producte del qual vol afegir stock: ");
                String codiProd = lector.nextLine();
                System.out.println("Quina quantitat vol afegir?");
                int stockAAfegir = Integer.parseInt(lector.nextLine());
                maquinaDAO.afegirStock(codiProd, stockAAfegir);
            }
            case 3 -> maquinaDAO.afegirRanura();
        }
    }

    /**
     * Es denama un producte a l'usuari i s'afegeix a la
     * base de dades.
     */
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

            LOGGER.log(Level.INFO,"S'afegeix un producte a la base de dades "+producte.getNom());

        } catch (SQLException e) {
            int errorCode = e.getErrorCode();

            if (errorCode == 1062) { //En cas de que el producte ja existeixi, mostrar l'error
                System.out.println("El producte ja existeix! hauries de canviar la clau primària.");
            }

            LOGGER.log(Level.WARNING,"Error al afegir el producte");
        }

    }

    /**
     * Es mostra l'inventari de la base de dades
     */
    private static void mostrarInventari() {
        try {
            //Agafem tots els productes de la BD i els mostrem
            ArrayList<Producte> productes = producteDAO.readProductes();

            System.out.println("Inventari:");
            for (Producte prod : productes) {
                System.out.println("- " + prod);
            }
            LOGGER.log(Level.INFO,"Es mostra l'inventari");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"No es pot mostrar l'inventari de productes");
            e.printStackTrace();
        }
    }

    /**
     * Es demana a l'usuari si vol entrar un producte sencer o un nom.
     * Desprès s'accedeix a la base de dades i es redueix 1 d'stock d'aquell
     * producte, i s'afegeix una linea al fitxer "beneficisMaquina.txt"
     */
    private static void comprarProducte() {
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

                        System.out.println(producte);
                        System.out.print("\nEstan totes les dades correctes? (s/n): ");
                        String resposta = lector.nextLine();

                        if (resposta.equalsIgnoreCase("s")) crearProducte = true;

                        try {
                            float preuVenda = producteDAO.comprarProducte(producte);
                            IO.escriureDades(beneficisCompres,Float.toString(preuVenda));
                        } catch (SQLException e) {
                            LOGGER.log(Level.WARNING,"El producte no existeix "+producte.getNom());
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
                        LOGGER.log(Level.INFO,"Es guarden les dades del benefici a l'arxiu de beneficisCompres, el preu afegit es "+preuVenda);
                    } catch (SQLException e) {
                        LOGGER.log(Level.WARNING,"El producte no existeix "+nomProducte);
                        throw new RuntimeException(e);
                    }

                    sortir = true;
                }
                default -> System.out.println("Opció incorrecta");
            }
        } while (!sortir);
    }

    /**
     * Es mostra el contingut de la màquina
     */
    private static void mostrarMaquina() {
        try {
            maquinaDAO.mostrarMaquina();
            LOGGER.log(Level.INFO,"Es mostra el contingut de la màquina");
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"No es pot mostrar el contingut de la màquina");
            throw new RuntimeException(e);
        }
    }

    /**
     * S'accedeix al fitxer de "beneficisMaquina.txt" i es mostra el
     * total de benefici.
     */
    private static void mostrarBenefici() {

        ArrayList<String> beneficis = llegirFitxer(fitxerBenefii);

        float beneficiTotal = 0;
        for (String s : beneficis) {
            float benefici = Float.parseFloat(s);

            beneficiTotal = beneficiTotal+benefici;
        }
        System.out.println( beneficiTotal);
        System.out.println("El benefici total es de " + beneficiTotal + "€");

        LOGGER.log(Level.INFO,"Es mostra el benefici i es de "+beneficiTotal);
    }

    /**
     * Es llegeix un fitxer
     * @param fitxer Se li passa un Path
     * @return Es retorna un arraylist d'Strings amb cada linea del fitxer
     */
    private static ArrayList<String> llegirFitxer(Path fitxer) {

        ArrayList<String> resultatAtleta = new ArrayList<>();

        try (Scanner lector = new Scanner(fitxer)) {
            while (lector.hasNextLine()) {
                String linea = lector.nextLine();

                resultatAtleta.add(linea);
            }
            LOGGER.log(Level.INFO,"Es mostra el contingut del fitxer "+fitxer.getFileName());
        } catch (Exception e) {
            System.out.println("Error: " + e);
            LOGGER.log(Level.WARNING,"Error al mostrar el contingut del fitxer "+fitxer);
        }

        return resultatAtleta;
    }
}
