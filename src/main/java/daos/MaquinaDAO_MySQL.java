package daos;

import model.Maquina;

import java.sql.*;

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
        PreparedStatement pss = conn.prepareStatement("SELECT posicio,nom,quantitat FROM slot, producte WHERE slot.codi_producte = producte.codi_producte");
        ResultSet rss = pss.executeQuery();
        Maquina maquina = new Maquina();
        while (rss.next()) {
            String posicioSlot = rss.getString(1) + " " + rss.getString(2) + " " + rss.getString(3);
            maquina.getLlistaMaquina().add(posicioSlot);
        }


        for (int i = 0; i < maquina.getLlistaMaquina().size(); i++) {
            System.out.printf(maquina.getLlistaMaquina().get(i));
        }
    }

    @Override
    public void modificarMaquina() {

    }

    @Override
    public void mostrarBeneficis() {

    }
}
