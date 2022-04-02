package edu.eci.cvds.samples.services.client;

import edu.eci.cvds.samples.entities.Item;
import edu.eci.cvds.samples.entities.TipoItem;
import edu.eci.cvds.samples.services.ExcepcionServiciosAlquiler;
import edu.eci.cvds.samples.services.ServiciosAlquilerFactory;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String args[]) throws SQLException, ExcepcionServiciosAlquiler, ParseException {

        System.out.println(ServiciosAlquilerFactory.getInstance().getServiciosAlquiler().consultarItem(1269345));
    }
}