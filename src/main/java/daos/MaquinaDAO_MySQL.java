package daos;

import model.Maquina;

import java.sql.*;
import java.util.ArrayList;

public class MaquinaDAO_MySQL implements MaquinaDAO {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "vadic2004";
    private Connection conn = null;

    public MaquinaDAO_MySQL() {
        try {
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_ROUTE, DB_USER, DB_PWD);
            System.out.println("Conexió establerta satisfactoriament");
        } catch (Exception e) {
            System.out.println("S'ha produit un error en intentar connectar amb la base de dades. Revisa els paràmetres");
            System.out.println(e);
        }
    }

    /**
     * Metode que mostrà els slots, els productes i la seva quantitat d'una màquina.
     * @throws SQLException
     */

    @Override
    public void mostrarMaquina() throws SQLException {
        ArrayList<Maquina> llistaMaquina = new ArrayList<>();
        PreparedStatement pss = conn.prepareStatement("SELECT posicio,nom,quantitat FROM slot, producte WHERE slot.codi_producte = producte.codi_producte");
        ResultSet rss = pss.executeQuery();
        while (rss.next()) {
            Maquina maquina = new Maquina();
            maquina.setSlot_posicio(rss.getString(1));
            maquina.setNom_producte(rss.getString(2));
            maquina.setQuantitat_stock(rss.getString(3));

            llistaMaquina.add(maquina);
        }

        for (Maquina maquina : llistaMaquina) {
            System.out.printf("%-10s  ", maquina.getSlot_posicio());
            System.out.printf("%5s %10s\n", maquina.getNom_producte(), maquina.getQuantitat_stock());
        }

    }

    /**
     * Metode que permet intercanviar les posicions dels productes d'una màquina
     * @param pos1 posició que es vol intercanviar
     * @param pos2 posició que es vol intercanviar
     */
    @Override
    public void modificarMaquina(int pos1, int pos2) {
        try {
            PreparedStatement obtenirSlot1 = conn.prepareStatement("SELECT * FROM slot WHERE posicio = ?");
            obtenirSlot1.setInt(1, pos1);

            PreparedStatement obtenirSlot2 = conn.prepareStatement("SELECT * FROM slot WHERE posicio = ?");
            obtenirSlot2.setInt(1, pos2);

            ResultSet rs1 = obtenirSlot1.executeQuery();
            ResultSet rs2 = obtenirSlot2.executeQuery();

            if (rs1.next() && rs2.next()) {
                PreparedStatement ps = conn.prepareStatement("UPDATE slot SET codi_producte = ? WHERE posicio = ?");
                ps.setString(1, rs2.getString("codi_producte"));
                ps.setInt(2, pos1);

                PreparedStatement ps2 = conn.prepareStatement("UPDATE slot SET codi_producte = ? WHERE posicio = ?");
                ps2.setString(1, rs1.getString("codi_producte"));
                ps2.setInt(2, pos2);

                ps.executeUpdate();
                ps2.executeUpdate();
            } else {
                System.err.println("Camp clau entrat no vàlid");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metode que permet afegir stock a un producte
     * @param codi_prod codi del producte passat com a parametre
     * @param quantitatStock quantitat d'estoc passada com a parametre
     */
    @Override
    public void afegirStock(String codi_prod, int quantitatStock) {
        try {
            PreparedStatement afegirStock = conn.prepareStatement("UPDATE slot SET quantitat = quantitat + ? WHERE codi_producte = ?");
            afegirStock.setInt(1, quantitatStock);
            afegirStock.setString(2, codi_prod);
            afegirStock.executeUpdate();
        } catch (SQLException e) {
            System.err.println("El codi entrat no està a la màquina");
            throw new RuntimeException(e);
        }

    }

    /**
     * Metode que afegeix un slot nou sense cap producte
     */
    @Override
    public void afegirRanura() {
        try {
            PreparedStatement obtenerRanura = conn.prepareStatement("SELECT posicio FROM slot ORDER BY posicio DESC limit 1");
            ResultSet rs = obtenerRanura.executeQuery();

            if (rs.next()) {
                int numPosicio = rs.getInt("posicio");
                numPosicio++;

                PreparedStatement afegirRanura = conn.prepareStatement("INSERT INTO slot(posicio) VALUES(?)");
                afegirRanura.setInt(1, numPosicio);
                afegirRanura.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
