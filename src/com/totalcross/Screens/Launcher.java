package com.totalcross.Screens;

import com.totalcross.DAO.CarDAO;
import com.totalcross.Exceptions.RecordAlreadyExists;
import com.totalcross.Exceptions.RecordNotExists;
import com.totalcross.Exceptions.ThereAreNoRecords;
import com.totalcross.Model.Car;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import totalcross.sql.Connection;
import totalcross.sql.DriverManager;
import totalcross.sys.*;
import totalcross.ui.*;
import totalcross.ui.dialog.*;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.InvalidDateException;
import totalcross.util.Date;
import totalcross.ui.ListBox;

public class Launcher extends MainWindow {
	private Connection conn;
	private Edit edName, edYear, edModel;
	private Button btnAddNewCar, btnClearCar, btnUpdateCar, btnDeleteCar, btnListAllCars;
	private ListBox listBox = new ListBox();
	
	public Launcher() {
		super("CRUD TotalCross", VERTICAL_GRADIENT);
		
		gradientTitleStartColor = 0;
		gradientTitleEndColor = 0xAAAAFF;
		
		setUIStyle(Settings.Android);
		Settings.uiAdjustmentsBasedOnFontHeight = true;
		setBackColor(0xDDDDFF);
	}
	
	@Override
	public void initUI() {
		add(new Label("# CAR INFO #"), CENTER, TOP + 50);
		
		add(new Label("Name: "), LEFT+50, AFTER + 100, PARENTSIZE+30, PREFERRED);
		add(edName = new Edit(), RIGHT-50, SAME, PARENTSIZE+80, PREFERRED);
		
		add(new Label("Model: "), LEFT+50, AFTER + 100, PARENTSIZE+30, PREFERRED);
		add(edModel = new Edit(), RIGHT-50, SAME, PARENTSIZE+70, PREFERRED);

		add(new Label("Year: "), LEFT+50, AFTER + 100, PARENTSIZE+30, PREFERRED);
		add(edYear = new Edit(), RIGHT-50, SAME, PARENTSIZE+80, PREFERRED);
		edYear.setMode(Edit.DATE);
				
		Spacer sp = new Spacer(0, 0);
		add(sp, CENTER, BOTTOM - 1650, PARENTSIZE + 10, PREFERRED);
		
		add(btnAddNewCar = new Button("Add Car"), LEFT, AFTER, PARENTSIZE + 50, PREFERRED, sp);
		add(btnClearCar = new Button("Cancel"), RIGHT, SAME, PARENTSIZE + 50, PREFERRED, sp);
		
		
		Spacer sp2 = new Spacer(0, 0);
		add(sp2, CENTER, BOTTOM - 300, PARENTSIZE + 10, PREFERRED);
		
		add(btnListAllCars = new Button("List"), CENTER, SAME, PARENTSIZE + 100, PREFERRED, sp2);
		
		Spacer sp3 = new Spacer(0, 0);		
		add(sp3, CENTER, BOTTOM - 150, PARENTSIZE + 10, PREFERRED);
		
		add(btnUpdateCar = new Button("Update"), LEFT, AFTER, PARENTSIZE + 50, PREFERRED, sp3);
		add(btnDeleteCar = new Button("Delete"), RIGHT, SAME, PARENTSIZE + 50, PREFERRED, sp3); 
		
		// realizar conexao com o banco.
		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + Convert.appendPath(Settings.appPath, "test.db"));			
			CarDAO carDao = new CarDAO();
			carDao.createIfNotExistTable(conn);
			
			System.out.println("Connection established!");
		} catch (Exception e) {
			MessageBox.showException(e, true);
			exit(0);
		}
				
		Toast.posY = CENTER;
	}
	
	@Override
	 public void onEvent(Event e) {
	      try {
	         switch (e.type) {
	            case ControlEvent.PRESSED:
	               if (e.target == btnAddNewCar){
	            	  doInsert();
	               } else if(e.target == btnClearCar) {
	            	   clear();
	               } else if(e.target == btnListAllCars){
	            	   doListAll();
	               } else if(e.target == btnUpdateCar) {
	            	   doUpdate();
	               } else if(e.target == btnDeleteCar) {
	            	   doDelete();
	               }
	               break;
	         }
	      }
	      catch (Exception ee) {
	         MessageBox.showException(ee,true);
	      }
	   }
	 
	 private void doInsert() throws SQLException, InvalidDateException {
			
	        if (edName.getLength() == 0 || edYear.getLength() == 0 || edModel.getLength() == 0) {
	            Toast.show("Enter all data!", 2000);
	        } else {
	            Car car = new Car();
	            
	    		car.setName(edName.getText());
	    		car.setModel(edModel.getText());
	    		car.setYear(new Date(edYear.getText()));
	    		
	    		CarDAO dao = new CarDAO();
	    		
	            try {
	    			dao.insert(car, conn);
	    			clear();
	    			Toast.show("Car saved!", 2000);
	    		} catch (RecordAlreadyExists e) {
	    			// TODO: handle exception
	    			Toast.show("Car already registered!", 2000);
	    		}
	        }
		}
		
		private void doListAll() throws SQLException {

			List <Car> cars = new ArrayList<Car>();
			CarDAO dao = new CarDAO();
			
			try {
				cars = dao.listAll(conn);
				
				listBox = new ListBox(); 
				add(listBox);
				listBox.add(cars.toString().replace("[", " ").replace("]", "").split(","));
				listBox.setRect(CENTER, BOTTOM-350, PARENTSIZE + 100, PREFERRED);
				
			} catch (ThereAreNoRecords e) {
				// TODO: handle exception
				Toast.show("Empty list!", 2000);
			}
		}
		
		private void doUpdate() throws SQLException, InvalidDateException {

	        if (edName.getLength() == 0 || edYear.getLength() == 0 || edModel.getLength() == 0) {
	            Toast.show("Enter all data!", 2000);
	        } else {
	            Car car = new Car();
	            
	    		car.setName(edName.getText());
	    		car.setModel(edModel.getText());
	    		car.setYear(new Date(edYear.getText()));
	    		
	    		CarDAO dao = new CarDAO();
	    		
	            try {
	    			dao.update(car, conn);
	    			clear();
	    			Toast.show("Contato atualizado!", 2000);
	    		} catch (RecordNotExists e) {
	    			// TODO: handle exception
	    			Toast.show("Contato inexistente!", 2000);
	    		}
	        }
		}
		
		private void doDelete() throws SQLException, InvalidDateException {
			
			if (edModel.getLength() == 0) {
				Toast.show("Fill out car model.", 2000);
			} else {
	            Car car = new Car();
	            
	    		car.setModel(edModel.getText());
	    		
	    		CarDAO dao = new CarDAO();
	    		
	            try {
	    			dao.delete(car, conn);
	    			clear();
	    			Toast.show("Car deleted!", 2000);
	    		} catch (RecordNotExists e) {
	    			// TODO: handle exception
	    			Toast.show("No car found with this model!", 2000);
	    		}
			}
	}
}
