package daos;

import model.Maquina;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MaquinaDAO {
    public void mostrarMaquina() throws SQLException;
    public void modificarMaquina();
    public void mostrarBeneficis();
}
