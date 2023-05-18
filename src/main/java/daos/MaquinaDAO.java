package daos;

import java.sql.SQLException;

public interface MaquinaDAO {
    public void mostrarMaquina() throws SQLException;
    public void modificarMaquina(int pos1, int pos2);

    public void afegirStock(String codi_prod, int quantitatStock);

    public void afegirRanura();
}