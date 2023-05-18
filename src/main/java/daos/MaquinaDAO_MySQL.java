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

    @Override
    public void mostrarMaquina() throws SQLException {
        int tabular = 10;
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
            System.out.printf("%5s %10s\n", maquina.getNom_producte(), tabular, maquina.getQuantitat_stock());
        }

    }

    @Override
    public void modificarMaquina(String nomProducte) {
        try {
            PreparedStatement obtenirProducte = conn.prepareStatement("SELECT codi_producte FROM producte WHERE nom = ?");
            obtenirProducte.setString(1,nomProducte);
            ResultSet rs = obtenirProducte.executeQuery();
            String codiProducte = rs.getString(1);
            PreparedStatement ps = conn.prepareStatement("UPDATE slot SET codi_producte = ? WHERE posicio = ?");
            ps.setString(1,codiProducte);
            ps.setInt(2,posicio);
            ResultSet actualitzarSlot =

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
