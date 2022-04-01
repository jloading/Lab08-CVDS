package edu.eci.cvds.sampleprj.dao.mybatis;

import com.google.inject.Inject;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import java.util.List;

public class MyBATISItemDAO implements ItemDAO{

    @Inject
    private ItemMapper itemMapper;

    @Override
    public void registrarItem(Item it) throws PersistenceException{
        try{
            itemMapper.insertarItem(it);
        }
        catch(Exception e){
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_REGISTRAR_ITEM);
        }
    }

    @Override
    public Item consultarItem(int id) throws PersistenceException {
        try{
            return itemMapper.consultarItem(id);
        }
        catch(Exception e){
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_ITEM);
        }
    }
    @Override
    public List<Item> consultarItemsDisponibles() throws PersistenceException {
        try {
            return itemMapper.consultarItemsDisponibles();
        }catch (Exception e) {
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_ITEMS );
        }
    }

    @Override
    public void actualizarTarifaItem(int id, long tarifa) throws PersistenceException {
        try {
            itemMapper.actualizarTarifaItem(id,tarifa);
        }catch (Exception e) {
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_ACTUALIZAR_TARIFA);
        }
    }



}