import model.Producte;

import java.util.Scanner;

public class IO {
    /**
     * Es demana per consola un Producte a l'usuari, es tracten els errors
     * @return Es retorna un producte SEMPRE.
     */
    public static Producte crearProducte() {
        Scanner scanner = new Scanner(System.in);

        String codi = null;
        String nom  = null;
        String descripcio  = null;
        float preuCompra = 0;
        float preuVenta = 0;

        //Demanar el CODI del producte
        boolean codiErroni;
        codiErroni = false;
        do {
            try {
                System.out.print("\nEntra el codi del producte: ");
                codi = scanner.nextLine();
            } catch (Exception e) {
                codiErroni = true;
            }
        } while (codiErroni);

        //Demanar el NOM del producte
        boolean nomErroni;
        nomErroni = false;
        do {
            try {
                System.out.print("\nEntra el nom del producte: ");
                nom = scanner.nextLine();
            } catch (Exception e) {
                nomErroni = true;
            }
        } while (nomErroni);

        //Demanar el DESCRIPCIO del producte
        boolean descErroni;
        descErroni = false;
        do {
            try {
                System.out.print("\nEntra la descripcio del producte: ");
                descripcio = scanner.nextLine();
            } catch (Exception e) {
                descErroni = true;
            }
        } while (descErroni);

        //Demanar el PREU COMPRA del producte
        boolean preuCompraErroni;
        preuCompraErroni = false;
        do {
            try {
                System.out.print("\nEntra el preu de compra del producte: ");
                preuCompra = Float.parseFloat(scanner.nextLine());
            } catch (Exception e) {
                preuCompraErroni = true;
            }
        } while (preuCompraErroni);

        //Demanar el PREU VENTA del producte
        boolean preuVentaErroni;
        preuVentaErroni = false;
        do {
            try {
                System.out.print("\nEntra el preu de venta del producte: ");
                preuVenta = Float.parseFloat(scanner.nextLine());
            } catch (Exception e) {
                preuVentaErroni = true;
            }
        } while (preuVentaErroni);

        return new Producte(codi,nom,descripcio,preuCompra,preuVenta);
    }
}
