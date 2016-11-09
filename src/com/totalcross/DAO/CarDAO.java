package com.totalcross.DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import totalcross.sql.Connection;
import totalcross.sql.Statement;
import totalcross.sql.ResultSet;

import com.totalcross.Model.Car;
import com.totalcross.Exceptions.*;
import totalcross.util.InvalidDateException;

public class CarDAO {
	
	public void createIfNotExistTable(Connection dbcon) throws SQLException {
	    Statement conn = dbcon.createStatement();
	    conn.execute("CREATE TABLE IF NOT EXISTS car (name varchar, model varchar primary key, year datetime)");
	    conn.close();
	}
	
    public Car selectByModel(String model, Connection dbcon) throws SQLException {
        Statement conn = dbcon.createStatement();
        
        String query = "SELECT * FROM car WHERE model = '" + model + "'";
        ResultSet rs = conn.executeQuery(query);

        Car ev = new Car();
        while (rs.next()) {
            ev.setName(rs.getString(1));
            ev.setModel(rs.getString(2));
            ev.setYear(rs.getDate(3));
        }
        
        return ev;
    }
	
	public void insert(Car ev, Connection dbcon) throws SQLException, RecordAlreadyExists, InvalidDateException {
	    Statement conn = dbcon.createStatement();
	
		Car car;
		car = selectByModel(ev.getModel(), dbcon);
		
		if (car.getName() == null) {
		    conn.executeUpdate("INSERT INTO car VALUES ('" + ev.getName()  + "','" + ev.getModel() + "','" + ev.getYear().getSQLString() + "')");            
		} else {
		    throw new RecordAlreadyExists();
		}
		
		conn.close();
	}
	
	public List<Car> listAll(Connection dbcon) throws SQLException, ThereAreNoRecords {
		List<Car> cars = new ArrayList<Car>();
		Car car;
		
        Statement conn = dbcon.createStatement();
        String query = "SELECT * FROM car";
        ResultSet rs = conn.executeQuery(query);
        
        while (rs.next()) {
            car = new Car();
            car.setName(rs.getString(1));
            car.setModel(rs.getString(2));
            car.setYear(rs.getDate(3));
            cars.add(car);
        }
        
        if (cars.size() == 0)
            throw new ThereAreNoRecords();
        
        return cars;
	}
	
	public void update(Car ev, Connection dbcon) throws SQLException, RecordNotExists, InvalidDateException {
        
		Statement conn = dbcon.createStatement();

        Car car;
        car = selectByModel(ev.getModel(), dbcon);

        if (car.getName() != null)
            conn.executeUpdate("UPDATE car SET name = '" + ev.getName() + "', year = '" + ev.getYear().getSQLString() + "' WHERE model = '" + ev.getModel() + "'");
        else
            throw new RecordNotExists();
        
        conn.close();
	}
	
    public void delete(Car ev, Connection dbcon) throws SQLException, RecordNotExists {
        
    	Statement conn = dbcon.createStatement();

    	 Car car;
         car = selectByModel(ev.getModel(), dbcon);

        if (car.getName() != null)
            conn.executeUpdate("DELETE FROM car WHERE model = '" + ev.getModel() + "'");
        else
            throw new RecordNotExists();
        
        conn.close();
    }
}
