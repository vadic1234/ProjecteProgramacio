package daos;

import model.Producte;
import model.Slot;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SlotDAO {

    public void createSlot(Slot slot) throws SQLException;

    public Slot readSlot() throws SQLException;

    public ArrayList<Slot> readSlots() throws SQLException;

    public void updateSlot(Slot slot) throws SQLException;

    public void deleteSlot(Slot slot) throws SQLException;

    public void deleteSlot(String posicio) throws SQLException;

}
