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

    public SlotDAO_MySQL() {
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
     * Es crea un slot a la base de dades
     * @param slot Es passa un slot per parametre per fer l'insert
     * @throws SQLException Si hi ha algun error de la base de dades, es mostrara al catch
     */
    @Override
    public void createSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO slot VALUES(?,?,?)");

        ps.setInt(1, slot.getPosicio());
        ps.setInt(2, slot.getQuantitat());
        ps.setString(3, slot.getCodi());

        int rowCount = ps.executeUpdate();
    }

    @Override
    public Slot readSlot() throws SQLException {
        return null;
    }

    /**
     * Es llegeixen tots els slots i es retornen en un Arraylist d'Slots
     * @return Es retorna un ArrayList d'slots
     * @throws SQLException Si hi ha algun error a la base de dades, sortira al catch
     */
    @Override
    public ArrayList<Slot> readSlots() throws SQLException {
        ArrayList<Slot> llistaSlots = new ArrayList<Slot>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM slot");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Slot slot = new Slot();

            slot.setPosicio(rs.getInt(1));
            slot.setQuantitat(rs.getInt(2));
            slot.setCodi(rs.getString(3));

            llistaSlots.add(slot);
        }

        return llistaSlots;
    }

    /**
     * S'actualitza un slot existent
     * @param slot Necesari per fer l'update
     * @throws SQLException Si hi ha algun error a la base de dades, sortira al catch
     */
    @Override
    public void updateSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE slot SET posicio = ?, quantitat = ? WHERE codi = ?");
        ps.setInt(1, slot.getPosicio());
        ps.setInt(2, slot.getQuantitat());
        ps.setString(3, slot.getCodi());

        ps.executeUpdate();

    }

    /**
     * S'elimina un slot existent
     * @param slot Necesari per fer el delete
     * @throws SQLException Si hi ha algun error a la base de dades, sortira al catch
     */
    @Override
    public void deleteSlot(Slot slot) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM slot WHERE posicio = ?");
        ps.setInt(1, slot.getPosicio());
        ps.executeUpdate();
    }

    /**
     * S'elimina un slot de la base de dades
     * @param posicio Necesari per fer el delete
     * @throws SQLException Si hi ha algun error a la base de dades, sortira al catch
     */
    @Override
    public void deleteSlot(int posicio) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM slot WHERE posicio = ?");
        ps.setInt(1, posicio);
        ps.executeUpdate();
    }
}
