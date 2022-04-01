package edu.eci.cvds.samples.services.impl;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import edu.eci.cvds.sampleprj.dao.ClienteDAO;
import edu.eci.cvds.sampleprj.dao.ItemDAO;
import edu.eci.cvds.sampleprj.dao.PersistenceException;
import edu.eci.cvds.sampleprj.dao.TipoItemDAO;
import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.ItemRentado;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquiler;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.swing.DefaultRowSorter;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.javassist.bytecode.ExceptionTable;
import org.mybatis.guice.transactional.Transactional;

@Singleton
public class ServiciosAlquilerImpl implements ServiciosAlquiler {

    @Inject
    private ItemDAO itemDAO;

    @Inject
    private ClienteDAO clienteDAO;

    @Inject
    private TipoItemDAO tipoItemDAO;


    @Override
    public long valorMultaRetrasoxDia(int itemId) throws ExcepcionServiciosAlquiler{
        try {
            return  itemDAO.consultarItem(itemId).getTarifaxDia();
        }catch(PersistenceException e) {
            throw new ExcepcionServiciosAlquiler("El item ingresado no esta registrado.");
        }
    }

    @Override
    public Cliente consultarCliente(long docu) throws ExcepcionServiciosAlquiler {
        try {
            Cliente cliente =  clienteDAO.consultarCliente(docu);
            if (cliente == null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.CLIENTE_NO_EXISTENTE);
            else return cliente;

        }catch(PersistenceException e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_CLIENTE + docu);
        }
    }

    @Override
    public List<ItemRentado> consultarItemsCliente(long idcliente) throws ExcepcionServiciosAlquiler {
        try {
            Cliente cliente =  clienteDAO.consultarCliente(idcliente);
            if (cliente==null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.CLIENTE_NO_EXISTENTE);
            else return cliente.getRentados();
        }catch(PersistenceException e) {
            throw new ExcepcionServiciosAlquiler( ExcepcionServiciosAlquiler.ERROR_CLIENTE + idcliente);
        }

    }

    @Override
    public List<Cliente> consultarClientes() throws ExcepcionServiciosAlquiler {
        try {
            return clienteDAO.consultarClientes();
        }catch(PersistenceException e) {
            throw new ExcepcionServiciosAlquiler( ExcepcionServiciosAlquiler.ERROR_CLIENTES );
        }
    }

    @Override
    public Item consultarItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            Item item= itemDAO.consultarItem(id);
            if (itemDAO==null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ITEM_NO_EXISTENTE);
            else return item;
        } catch (PersistenceException ex) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_ITEM + id);
        }
    }

    @Override
    public List<Item> consultarItemsDisponibles() throws ExcepcionServiciosAlquiler {
        try {
            return itemDAO.consultarItemsDisponibles();
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_ITEMS );
        }
    }

    @Override
    public long consultarMultaAlquiler(int iditem, Date fechaDevolucion) throws ExcepcionServiciosAlquiler {
        List<Cliente> clientes = consultarClientes();
        for (Cliente c:clientes) {
            List<ItemRentado> rentados = c.getRentados();
            for (ItemRentado ir:rentados) {
                if( ir.getItem().getId()==iditem) {
                    LocalDate fechaFin=ir.getFechafinrenta().toLocalDate();
                    LocalDate fechaEntrega=fechaDevolucion.toLocalDate();
                    long diasRetraso = ChronoUnit.DAYS.between(fechaFin, fechaEntrega);
                    if(diasRetraso<=0){
                        return 0;
                    }
                    return diasRetraso*valorMultaRetrasoxDia(ir.getItem().getId());
                }
            }
        }
        throw  new ExcepcionServiciosAlquiler("El item"+iditem+"no se encuentra rentado");
    }

    @Override
    public TipoItem consultarTipoItem(int id) throws ExcepcionServiciosAlquiler {
        try {
            TipoItem tipoItem =  tipoItemDAO.consultarTipoItem(id);
            if (tipoItem == null) throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.TIPO_ITEM_NO_EXISTENTE);
            else return tipoItem;
        }catch(Exception e){
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_TIPO_ITEM + id);
        }
    }

    @Override
    public List<TipoItem> consultarTiposItem() throws ExcepcionServiciosAlquiler {
        try {
            return tipoItemDAO.consultarTiposItem();
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_TIPO_ITEMS );
        }

    }

    @Override
    @Transactional
    public void registrarAlquilerCliente(Date date, long docu, Item item, int numdias) throws ExcepcionServiciosAlquiler {
        try {
            if(numdias<1)throw  new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_DIAS_RENTA);
            if(consultarCliente(docu)!=null && (consultarItem(item.getId())!=null)) {
                LocalDate inicio = date.toLocalDate();
                LocalDate fin  = inicio.plusDays(numdias);
                clienteDAO.agregarItemRentadoACliente(docu,item.getId(),date,java.sql.Date.valueOf(fin));
            }
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_REGISTRAR_ALQUILER);
        }
    }

    @Override
    @Transactional
    public void registrarCliente(Cliente c) throws ExcepcionServiciosAlquiler {
        try {
            clienteDAO.registrarCliente(c);
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_REGISTRAR_CLIENTE);
        }
    }

    @Override
    public long consultarCostoAlquiler(int iditem, int numdias) throws ExcepcionServiciosAlquiler {
        return consultarItem(iditem).getTarifaxDia()*numdias;
    }

    @Override
    @Transactional
    public void actualizarTarifaItem(int id, long tarifa) throws ExcepcionServiciosAlquiler {
        try {
            itemDAO.actualizarTarifaItem(id,tarifa);
        }catch (PersistenceException e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_ACTUALIZAR_TARIFA);
        }
    }

    @Override
    @Transactional
    public void registrarItem(Item i) throws ExcepcionServiciosAlquiler {
        try {
            if (itemDAO.consultarItem(i.getId()) == null) itemDAO.registrarItem(i);
            else throw new Exception();
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_REGISTRAR_ITEM);
        }
    }

    @Override
    @Transactional
    public void vetarCliente(long docu, boolean estado) throws ExcepcionServiciosAlquiler {
        try {
            clienteDAO.vetarCliente(docu,estado);
        }catch (Exception e) {
            throw new ExcepcionServiciosAlquiler(ExcepcionServiciosAlquiler.ERROR_VETAR_CLIENTE);
        }
    }

}