/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

	// reference to physical database connection.
	private Connection _connection = null;

	// handling the keyboard inputs through a BufferedReader
	// This variable can be global for convenience.
	static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	/**
	 * Creates a new instance of DBProject
	 *
	 * @param hostname the MySQL or PostgreSQL server hostname
	 * @param database the name of the database
	 * @param username the user name used to login to the database
	 * @param password the user login password
	 * @throws java.sql.SQLException when failed to make a connection.
	 */
	public DBProject(String dbname, String dbport, String user, String passwd) throws SQLException {

		System.out.print("Connecting to database...");
		try {
			// constructs the connection URL
			String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
			System.out.println("Connection URL: " + url + "\n");

			// obtain a physical connection
			this._connection = DriverManager.getConnection(url, user, passwd);
			System.out.println("Done");
		} catch (Exception e) {
			System.err.println("Error - Unable to Connect to Database: " + e.getMessage());
			System.out.println("Make sure you started postgres on this machine");
			System.exit(-1);
		} // end catch
	}// end DBProject

	/**
	 * Method to execute an update SQL statement. Update SQL instructions includes
	 * CREATE, INSERT, UPDATE, DELETE, and DROP.
	 *
	 * @param sql the input SQL string
	 * @throws java.sql.SQLException when update failed
	 */
	public void executeUpdate(String sql) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the update instruction
		stmt.executeUpdate(sql);

		// close the instruction
		stmt.close();
	}// end executeUpdate

	/**
	 * Method to execute an input query SQL instruction (i.e. SELECT). This method
	 * issues the query to the DBMS and outputs the results to standard out.
	 *
	 * @param query the input query string
	 * @return the number of rows returned
	 * @throws java.sql.SQLException when failed to execute the query
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}// end executeQuery

	public ResultSet runQuery(String query) throws SQLException {
		// creates a statement object
		Statement stmt = this._connection.createStatement();

		// issues the query instruction
		ResultSet rs = stmt.executeQuery(query);

		/*
		 ** obtains the metadata object for the returned result set. The metadata
		 ** contains row and column info.
		 */
		ResultSetMetaData rsmd = rs.getMetaData();
		int numCol = rsmd.getColumnCount();
		int rowCount = 0;

		// iterates through the result set and output them to standard out.
		boolean outputHeader = true;
		while (rs.next()) {
			if (outputHeader) {
				for (int i = 1; i <= numCol; i++) {
					System.out.print(rsmd.getColumnName(i) + "\t");
				}
				System.out.println();
				outputHeader = false;
			}
			for (int i = 1; i <= numCol; ++i)
				System.out.print(rs.getString(i) + "\t");
			System.out.println();
			++rowCount;
		} // end while
		stmt.close();
		return rowCount;
	}// end executeQuery

	/**
	 * Method to close the physical connection if it is open.
	 */
	public void cleanup() {
		try {
			if (this._connection != null) {
				this._connection.close();
			} // end if
		} catch (SQLException e) {
			// ignored.
		} // end try
	}// end cleanup

	/**
	 * The main execution method
	 *
	 * @param args the command line arguments this inclues the <mysql|pgsql> <login
	 *             file>
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("Usage: " + "java [-classpath <classpath>] " + DBProject.class.getName()
					+ " <dbname> <port> <user>");
			return;
		} // end if

		Greeting();
		DBProject esql = null;
		try {
			// use postgres JDBC driver.
			Class.forName("org.postgresql.Driver").newInstance();
			// instantiate the DBProject object and creates a physical
			// connection.
			String dbname = args[0];
			String dbport = args[1];
			String user = args[2];
			esql = new DBProject(dbname, dbport, user, "");

			boolean keepon = true;
			while (keepon) {
				// These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking");
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range");
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

				switch (readChoice()) {
				case 1:
					addCustomer(esql);
					break;
				case 2:
					addRoom(esql);
					break;
				case 3:
					addMaintenanceCompany(esql);
					break;
				case 4:
					addRepair(esql);
					break;
				case 5:
					bookRoom(esql);
					break;
				case 6:
					assignHouseCleaningToRoom(esql);
					break;
				case 7:
					repairRequest(esql);
					break;
				case 8:
					numberOfAvailableRooms(esql);
					break;
				case 9:
					numberOfBookedRooms(esql);
					break;
				case 10:
					listHotelRoomBookingsForAWeek(esql);
					break;
				case 11:
					topKHighestRoomPriceForADateRange(esql);
					break;
				case 12:
					topKHighestPriceBookingsForACustomer(esql);
					break;
				case 13:
					totalCostForCustomer(esql);
					break;
				case 14:
					listRepairsMade(esql);
					break;
				case 15:
					topKMaintenanceCompany(esql);
					break;
				case 16:
					numberOfRepairsForEachRoomPerYear(esql);
					break;
				case 17:
					keepon = false;
					break;
				default:
					System.out.println("Unrecognized choice!");
					break;
				}// end switch
			} // end while
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			// make sure to cleanup the created table and close the connection.
			try {
				if (esql != null) {
					System.out.print("Disconnecting from database...");
					esql.cleanup();
					System.out.println("Done\n\nBye !");
				} // end if
			} catch (Exception e) {
				// ignored.
			} // end try
		} // end try
	}// end main

	public static void Greeting() {
		System.out.println("\n\n*******************************************************\n"
				+ "              User Interface      	               \n"
				+ "*******************************************************\n");
	}// end Greeting

	/*
	 * Reads the users choice given from the keyboard
	 * 
	 * @int
	 **/
	public static int readChoice() {
		int input;
		// returns only if a correct value is given.
		do {
			System.out.print("Please make your choice: ");
			try { // read the integer, parse it and break.
				input = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			} // end try
		} while (true);
		return input;
	}// end readChoice

	public static void addCustomer(DBProject esql) {
		// Given customer details add the customer in the DB
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(customerID) from customer");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("First name: ");
			String first_name = scan.nextLine();
			System.out.println("Last name: ");
			String last_name = scan.nextLine();
			System.out.println("Address: ");
			String address = scan.nextLine();
			System.out.println("Phone Number: ");
			String phNo = scan.nextLine();
			System.out.println("DOB(mm/dd/yyyy): ");
			String dob = scan.nextLine();
			System.out.println("Gender(Male, Female, or Other): ");
			String gender = scan.nextLine();
			System.out.println("Your Information:\nCustomerID: " + id + "\nname: " + first_name + " " + last_name
					+ "\naddress: " + address + "\nphone number: " + phNo + "\nDOB: " + dob + "\nGender: " + gender);
			String sql = "insert into customer(customerID, fName, lName, Address, phNo, DOB, gender) values(" + id + ","
					+ "\'" + first_name + "\'" + "," + "\'" + last_name + "\'" + "," + "\'" + address + "\'" + ","
					+ "\'" + phNo + "\'" + "," + "\'" + dob + "\'" + "," + "\'" + gender + "\'" + ")";
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end addCustomer

	public static void addRoom(DBProject esql) {
		// Given room details add the room in the DB
		Scanner scan = new Scanner(System.in);
		try {
			System.out.println("Hotel ID: ");
			String hotelID = scan.nextLine();
			// scan.nextLine();
			System.out.println("RoomNo: ");
			String roomNo = scan.nextLine();
			// scan.nextLine();
			System.out.println("Room Type: ");
			String type = scan.nextLine();
			String sql = "insert into room(hotelID, roomNo, roomType) values(" + "\'" + hotelID + "\'" + "," + "\'"
					+ roomNo + "\'" + "," + "\'" + type + "\'" + ")";
			System.out.println("Add Room:\nHotel ID: " + hotelID + "\nroomNo: " + roomNo + "\nRoom Type: " + type);
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end addRoom

	public static void addMaintenanceCompany(DBProject esql) {
		// Given maintenance Company details add the maintenance company in the DB
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(cmpID) from maintenanceCompany");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("Company Name: ");
			String name = scan.nextLine();
			// scan.nextLine();
			System.out.println("Address: ");
			String address = scan.nextLine();
			// scan.nextLine();
			System.out.println("Certified?(Y/N): ");
			String certified = scan.nextLine();
			if (certified == "Y") {
				certified = "TRUE";
			} else {
				certified = "FALSE";
			}
			String sql = "insert into maintenancecompany(cmpID, name, address, isCertified) values(" + "\'" + id + "\'"
					+ "," + "\'" + name + "\'" + "," + "\'" + address + "\'" + "," + "\'" + certified + "\'" + ")";
			System.out.println("Add Maintenance Company:\nCompany ID: " + id + "\nCompany Name: " + name + "\nAddress: "
					+ address + "\nCertified: " + certified);
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// endi addMaintenanceCompany

	public static void addRepair(DBProject esql) {
		// Given repair details add repair in the DB
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(rID) from repair");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("Hotel ID: ");
			String hotelID = scan.nextLine();
			// scan.nextLine();
			System.out.println("RoomNo: ");
			String roomNo = scan.nextLine();
			// scan.nextLine();
			System.out.println("Maintenance Company ID: ");
			String cmpID = scan.nextLine();
			System.out.println("Repair Date(mm/dd/yyyy): ");
			String date = scan.nextLine();
			System.out.println("Description: ");
			String description = scan.nextLine();
			System.out.println("Repair Type: ");
			String type = scan.nextLine();
			System.out.println("Repair ID: " + id + "\nHotel ID: " + hotelID + "\nRoomNo: " + roomNo
					+ "\nMaintenance Company ID: " + cmpID + "\nRepair Date: " + date + "\nDescription: " + description
					+ "\nRepair Type: " + type);
			String sql = "insert into repair(rid, hotelID, roomNo, mcompany, repairdate, description, repairtype) values("
					+ "\'" + id + "\'" + "," + "\'" + hotelID + "\'" + "," + "\'" + roomNo + "\'" + "," + "\'" + cmpID
					+ "\'" + "," + "\'" + date + "\'" + "," + "\'" + description + "\'" + "," + "\'" + type + "\'"
					+ ")";
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

	}// end addRepair

	public static void bookRoom(DBProject esql) {
		// Given hotelID, roomNo and customer Name create a booking in the DB
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(bID) from booking");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("Customer ID: ");
			String cID = scan.nextLine();
			// scan.nextLine();
			System.out.println("Hotel ID: ");
			String hotelID = scan.nextLine();
			// scan.nextLine();
			System.out.println("RoomNo: ");
			String roomNo = scan.nextLine();
			System.out.println("Booking Date(mm/dd/yyyy): ");
			String date = scan.nextLine();
			System.out.println("Number of People: ");
			String numPpl = scan.nextLine();
			System.out.println("Price: ");
			String price = scan.nextLine();
			System.out.println("Booking ID: " + id + "\nCustomer ID: " + cID + "\nHotel ID: " + hotelID + "\nRoomNo: "
					+ roomNo + "\nBooking Date: " + date + "\nNumber of People: " + numPpl + "\nPrice: " + price);
			String sql = "insert into booking(bid, customer, hotelID, roomNo, bookingdate, noofpeople, price) values("
					+ "\'" + id + "\'" + "," + "\'" + cID + "\'" + "," + "\'" + hotelID + "\'" + "," + "\'" + roomNo
					+ "\'" + "," + "\'" + date + "\'" + "," + "\'" + numPpl + "\'" + "," + "\'" + price + "\'" + ")";
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end bookRoom

	public static void assignHouseCleaningToRoom(DBProject esql) {
		// Given Staff SSN, HotelID, roomNo Assign the staff to the room
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(asgID) from assigned");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("Staff ID: ");
			String sID = scan.nextLine();
			// scan.nextLine();
			System.out.println("Hotel ID: ");
			String hotelID = scan.nextLine();
			// scan.nextLine();
			System.out.println("RoomNo: ");
			String roomNo = scan.nextLine();
			System.out.println(
					"Assignment ID: " + id + "\nStaff ID: " + sID + "\nHotel ID: " + hotelID + "\nRoomNo: " + roomNo);
			String sql = "insert into assigned(asgid, staffID, hotelID, roomNo) values(" + "\'" + id + "\'" + "," + "\'"
					+ sID + "\'" + "," + "\'" + hotelID + "\'" + "," + "\'" + roomNo + "\'" + ")";
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end assignHouseCleaningToRoom

	public static void repairRequest(DBProject esql) {
		// Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request
		// in the DB
		Scanner scan = new Scanner(System.in);
		try {
			long id = 0;
			ResultSet res = esql.executeQuery("select max(reqID) from request");
			if (res.next()) {
				id = res.getLong(1) + 1;
			}
			System.out.println("Manager ID: ");
			String mID = scan.nextLine();
			// scan.nextLine();
			System.out.println("Repair ID: ");
			String rID = scan.nextLine();
			// scan.nextLine();
			System.out.println("Request Date(mm/dd/yyyy): ");
			String date = scan.nextLine();
			System.out.println("Description: ");
			String description = scan.nextLine();
			System.out.println("Request ID: " + id + "\nManager ID: " + mID + "\nRepair ID: " + rID + "\nRequest Date: "
					+ date + "\nDescription: " + description);
			String sql = "insert into request(reqid, managerID, repairID, requestdate, description) values(" + "\'" + id
					+ "\'" + "," + "\'" + mID + "\'" + "," + "\'" + rID + "\'" + "," + "\'" + date + "\'" + "," + "\'"
					+ description + "\'" + ")";
			esql.executeUpdate(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end repairRequest

	public static void numberOfAvailableRooms(DBProject esql) {
		// Given a hotelID, get the count of rooms available
		// Your code goes here.
		// ...
		// ...
	}// end numberOfAvailableRooms

	public static void numberOfBookedRooms(DBProject esql) {
		// Given a hotelID, get the count of rooms booked
		// Your code goes here.
		// ...
		// ...
	}// end numberOfBookedRooms

	public static void listHotelRoomBookingsForAWeek(DBProject esql) {
		// Given a hotelID, date - list all the rooms available for a week(including the
		// input date)
		// Your code goes here.
		// ...
		// ...
	}// end listHotelRoomBookingsForAWeek

	public static void topKHighestRoomPriceForADateRange(DBProject esql) {
		// List Top K Rooms with the highest price for a given date range
		// Your code goes here.
		// ...
		// ...
	}// end topKHighestRoomPriceForADateRange

	public static void topKHighestPriceBookingsForACustomer(DBProject esql) {
		// Given a customer Name, List Top K highest booking price for a customer
		// Your code goes here.
		// ...
		// ...
	}// end topKHighestPriceBookingsForACustomer

	public static void totalCostForCustomer(DBProject esql) {
		// Given a hotelID, customer Name and date range get the total cost incurred by
		// the customer
		// Your code goes here.
		// ...
		// ...
	}// end totalCostForCustomer

	public static void listRepairsMade(DBProject esql) {
		// Given a Maintenance company name list all the repairs along with repairType,
		// hotelID and roomNo
		Scanner scan = new Scanner(System.in);
		try {
			String query = "select r.rid, r.repairtype, r.hotelid, r.roomno from repair r where r.mcompany = (select m.cmpid from maintenancecompany m where m.name = ";
			System.out.println("Maintenance Company Name: ");
			String input = scan.nextLine();
			System.out.println("Maintenance Company Name: " + input);
			query += input + ")";
			int rowCount = esql.runQuery(query);
			System.out.println("total row(s): " + rowCount);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}// end listRepairsMade

	public static void topKMaintenanceCompany(DBProject esql) {
		// List Top K Maintenance Company Names based on total repair count (descending
		// order)
		// Your code goes here.
		// ...
		// ...
	}// end topKMaintenanceCompany

	public static void numberOfRepairsForEachRoomPerYear(DBProject esql) {
		// Given a hotelID, roomNo, get the count of repairs per year
		// Your code goes here.
		// ...
		// ...
	}// end listRepairsMade

}// end DBProject
