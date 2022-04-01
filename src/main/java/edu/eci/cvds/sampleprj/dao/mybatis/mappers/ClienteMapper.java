package edu.eci.cvds.sampleprj.dao.mybatis.mappers;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import edu.eci.cvds.samples.entities.Cliente;
import edu.eci.cvds.samples.entities.Item;


public interface ClienteMapper {

    public Cliente consultarCliente(@Param("idcli") long id);

    /**
     * Registrar un nuevo item rentado asociado al cliente identificado
     * con 'idc' y relacionado con el item identificado con 'idi'
     * @param id
     * @param idit
     * @param fechainicio
     * @param fechafin
     */
    public void agregarItemRentadoACliente(@Param("idcli")long id,
                                           @Param("idit")int idit,
                                           @Param("fechainicio")Date fechainicio,
                                           @Param("fechafin")Date fechafin);

    /**
     * Consultar todos los clientes
     * @return
     */

    public List<Cliente> consultarClientes();

    public void registrarCliente(@Param("cliente") Cliente c);

    public void vetarCliente(@Param("cliente")long docu, @Param("estado")boolean estado);

    public List<Item> consultarItemsRentadosCliente(@Param("idCliente") int idCliente);

}