package edu.eci.cvds.sampleprj.dao;


import java.util.List;

//import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.samples.entities.Item;


public interface ItemDAO {

    public void registrarItem(Item it) throws PersistenceException;

    public Item consultarItem(int itemId) throws PersistenceException;

    public List<Item> consultarItemsDisponibles() throws PersistenceException;

    public void actualizarTarifaItem(int id, long tarifa)throws PersistenceException;

}