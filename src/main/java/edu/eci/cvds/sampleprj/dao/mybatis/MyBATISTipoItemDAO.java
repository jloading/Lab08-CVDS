package edu.eci.cvds.sampleprj.dao.mybatis;

import java.util.List;

import com.google.inject.Inject;

import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;

public class MyBATISTipoItemDAO implements TipoItemDAO{
    @Inject
    private TipoItemMapper tipoItemMapper;

    @Override
    public TipoItem consultarTipoItem(int id) throws PersistenceException {
        try {
            return tipoItemMapper.getTipoItem(id);
        }catch (Exception e) {
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_TIPO_ITEM);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws PersistenceException {
        try {
            return tipoItemMapper.getTiposItems();
        } catch (Exception e) {
            throw new PersistenceException(ExcepcionServiciosAlquiler.ERROR_TIPO_ITEMS);
        }
    }

}