package daos;

import model.Producte;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProducteDAO_MySQL implements ProducteDAO {

    //Dades de connexió a la base de dades
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_ROUTE = "jdbc:mysql://localhost:3306/expenedora";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "vadic2004";
    private Connection conn = null;
    public ProducteDAO_MySQL()
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
     * Metode que permet inserir un nou producte a la base de dades. Les dades d'aquest producte són les que el metode passa com a parametre.
     * @param p producte amb els atributs entrats per teclat
     * @throws SQLException
     */
    @Override
    public void createProducte(Producte p) throws SQLException {

        PreparedStatement ps = conn.prepareStatement("INSERT INTO producte VALUES(?,?,?,?,?)");

        ps.setString(1,p.getCodiProducte());
        ps.setString(2,p.getNom());
        ps.setString(3,p.getDescripcio());
        ps.setFloat(4,p.getPreuCompra());
        ps.setFloat(5,p.getPreuVenta());

        int rowCount = ps.executeUpdate();
    }

    @Override
    public Producte readProducte() throws SQLException {
        return null;
    }

    /**
     * Metode que mostre els productes que hi han a la base de dades. Primer es fa una consulta on el resultat retornat s'anirà guardant a una llista de productes que més tard es mostrarà.
     * @return
     * @throws SQLException
     */
    @Override
    public ArrayList<Producte> readProductes() throws SQLException {
        ArrayList<Producte> llistaProductes = new ArrayList<Producte>();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM producte");

        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
            Producte p = new Producte();

            p.setCodiProducte(rs.getString(1));
            p.setNom(rs.getString(2));
            p.setDescripcio(rs.getString(3));
            p.setPreuCompra(rs.getFloat(4));
            p.setPreuVenta(rs.getFloat(5));

            llistaProductes.add(p);
        }

        return llistaProductes;
    }

    /**
     * Metode per actualitzar un producte. L'actualització consisteix en intercanviar les dades del producte amb les noves dades entrat per l'usuari amb l'anterior ja entrat a la base de dades
     * @param p producte amb noves dades entrades per l'usuari
     * @throws SQLException
     */

    @Override
    public void updateProducte(Producte p) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE producte SET nom = ?, descripcio = ?, preu_copmra = ?, preu_venta = ? WHERE codi_producte = ?");
        ps.setString(1,p.getNom());
        ps.setString(2,p.getDescripcio());
        ps.setFloat(3,p.getPreuCompra());
        ps.setFloat(4,p.getPreuVenta());
        ps.setString(5,p.getCodiProducte());

        ps.executeUpdate();
    }

    /**
     * Metode que borra un producte segons el seu codi.
     * @param p producte enviat com a parametre amb dades entrades per l'usuari
     * @throws SQLException
     */
    @Override
    public void deleteProducte(Producte p) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM producte WHERE codi_producte = ?");
        ps.setString(1,p.getCodiProducte());
        ps.executeUpdate();
    }

    /**
     * Metode que borra un producte segons el seu codi.
     * @param codiProducte cadena de text entrada per l'usuari
     * @throws SQLException
     */
    @Override
    public void deleteProducte(String codiProducte) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("DELETE * FROM producte WHERE codi_producte = ?");
        ps.setString(1,codiProducte);
        ps.executeUpdate();

    }

    /**
     * Metode que permet comprar un producte. Quan el producte es comprat, es resta el seu estoc automàticament i tambè es retorna el seu benefici.
     * @param producte producte que es vol comprar entrat per l'usuari.
     * @return
     * @throws SQLException
     */

    @Override
    public float comprarProducte(Producte producte) throws SQLException {
        PreparedStatement selectPreuVenda = conn.prepareStatement("SELECT preu_venta FROM producte WHERE codi_producte = ?");
        selectPreuVenda.setString(1,producte.getCodiProducte());
        ResultSet preuVenda = selectPreuVenda.executeQuery();

        PreparedStatement ps = conn.prepareStatement("UPDATE slot SET quantitat = quantitat-1 WHERE codi_producte = ?");
        ps.setString(1,producte.getCodiProducte());
        ps.executeUpdate();

        return preuVenda.getFloat(1);
    }

    /**
     * Metode que permet comprar un producte pel seu codi. Restar el seu estoc i retornar el benefici guanyat.
     * @param nomProducte
     * @return
     * @throws SQLException
     */

    @Override
    public float comprarProducte(String nomProducte) throws SQLException {
        PreparedStatement selectPreuVenda = conn.prepareStatement("SELECT preu_venta FROM producte WHERE nom = ?");
        selectPreuVenda.setString(1, nomProducte);
        ResultSet preuVenda = selectPreuVenda.executeQuery();

        PreparedStatement selectCodiProducte = conn.prepareStatement("SELECT codi_producte FROM producte WHERE nom = ?");
        selectCodiProducte.setString(1, nomProducte);
        ResultSet resultSet = selectCodiProducte.executeQuery();

        String codiProducte = null;
        if (resultSet.next()) {
            codiProducte = resultSet.getString("codi_producte");
        }

        PreparedStatement updateQuantitat = conn.prepareStatement("UPDATE slot SET quantitat = quantitat - 1 WHERE codi_producte = ?");
        updateQuantitat.setString(1, codiProducte);
        updateQuantitat.executeUpdate();

        if (preuVenda.next()) {
            float preuVenta = preuVenda.getFloat("preu_venta");
            System.out.println(preuVenta);
            return preuVenta;
        } else {
            throw new RuntimeException("No se encontró el precio de venta para el producto: " + nomProducte);
        }


    }
}
