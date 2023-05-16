package daos;

import model.Slot;

import java.sql.*;
import java.util.ArrayList;

public class SlotDAO_MySQL implements SlotDAO {

    //Dades de connexió a la base de dades
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "vadic2004";
    private Connection conn = null;

    public SlotDAO_MySQL()
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


    @Override
    public void createSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO slot VALUES(?,?,?)");

        ps.setInt(1,slot.getPosicio());
        ps.setInt(2,slot.getQuantitat());
        ps.setString(3,slot.getCodi());

        int rowCount = ps.executeUpdate();
    }

    @Override
    public Slot readSlot() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<Slot> readSlots() throws SQLException {
        ArrayList<Slot> llistaSlots = new ArrayList<Slot>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot");

        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Slot slot = new Slot();

            slot.setPosicio(rs.getInt(1));
            slot.setQuantitat(rs.getInt(2));
            slot.setCodi(rs.getString(3));

            llistaSlots.add(slot);
        }

        return llistaSlots;
    }

    @Override
    public void updateSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE slot SET posicio = ?, quantitat = ? WHERE codi = ?");
        ps.setInt(1,slot.getPosicio());
        ps.setInt(2,slot.getQuantitat());
        ps.setString(3,slot.getCodi());

        ps.executeUpdate();

    }

    @Override
    public void deleteSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM slot WHERE posicio = ?");
        ps.setInt(1,slot.getPosicio());
        ps.executeUpdate();
    }

    @Override
    public void deleteSlot(int posicio) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM slot WHERE posicio = ?");
        ps.setInt(1,posicio);
        ps.executeUpdate();
    }
}
