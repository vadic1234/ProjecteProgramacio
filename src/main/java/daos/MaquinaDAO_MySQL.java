package daos;

import model.Maquina;

import java.sql.*;
import java.util.ArrayList;

public class MaquinaDAO_MySQL implements MaquinaDAO {

    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "1324";
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

        ArrayList<Maquina> llistaMaquina = new ArrayList<>();
        PreparedStatement pss = conn.prepareStatement("SELECT posicio,nom,quantitat FROM slot, producte WHERE codi = codi_producte");
        ResultSet rss = pss.executeQuery();
        while (rss.next()) {
            Maquina maquina = new Maquina();
            maquina.setSlot_posicio(rss.getString(1));
            maquina.setNom_producte(rss.getString(2));
            maquina.setQuantitat_stock(rss.getString(3));
        }


        for (int i = 0; i < llistaMaquina.size(); i++) {
            System.out.printf(llistaMaquina.get(i).getSlot_posicio() + "\t\t" + llistaMaquina.get(i).getNom_producte() + "\t\t" + llistaMaquina.get(i).getQuantitat_stock());

        }

    }

    @Override
    public void modificarMaquina() {

    }

    @Override
    public void mostrarBeneficis() {

    }
}
