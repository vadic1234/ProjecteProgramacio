package daos;

import java.sql.SQLException;

public interface MaquinaDAO {
    public void mostrarMaquina() throws SQLException;
    public void modificarMaquina(String nomProducte);
}