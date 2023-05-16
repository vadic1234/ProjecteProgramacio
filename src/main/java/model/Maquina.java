package model;

import daos.SlotDAO_MySQL;

import java.sql.*;
import java.util.ArrayList;

public class Maquina { //Classe màquina per guardar la informació que es vol mostrar

    private ArrayList<String> llistaMaquina = new ArrayList<>();
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "vadic2004";
    private Connection conn = null;

    public Maquina()
    {
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
     * Metode que fa una consulta a 2 taules, les dades que retorna les guarda a una cadena de Strings i la afageix a un arrayList.
     * @throws SQLException
     */
    public void mostrarMaquina() throws SQLException {
        PreparedStatement pss = conn.prepareStatement("SELECT posicio,nom,quantitat FROM slot, producte WHERE codi = codi_producte");
        ResultSet rss = pss.executeQuery();
        while(rss.next())
        {
            String posicioSlot = rss.getString(1) + " " + rss.getString(2) + " " + rss.getString(3);
            llistaMaquina.add(posicioSlot);
        }


        for(int i = 0; i < llistaMaquina.size();i++){
            System.out.printf(llistaMaquina.get(i));
        }
    }
}
