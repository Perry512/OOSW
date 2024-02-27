package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;
import event.Event;
import impresario.IView;
import impresario.ModelRegistry;
import userinterface.View;
import userinterface.ViewFactory;

public class BookCollection extends EntityBase implements IView {

    private static final String myTableName = "Book";

    private Vector<Book> bookList;
    //GUI

    //constructor
    public BookCollection(){

        super(myTableName);

    }

    public Vector<Book> findBooksOlderThan(String year) {
        int searchYear = Integer.parseInt(year);
        String query = "SELECT * FROM " + " WHERE (pubYear >= " + fixDateFormat(searchYear) + ")";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        if (resultUnp != null) {

            if (resultUnp.size() <=0) {

                System.out.println("No Books made after " + year + " found\n");

            } else {

                return resultUnp;

            }

        }

        return new Vector<>();
        
    }

    public Vector<Book> findBooksYoungerThan(String year) {
        int searchYear = Integer.parseInt(year);
        String query = "SELECT * FROM '" + myTableName + "' WHERE (pubYear <= " + fixDateFormat(searchYear) + ")";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        //handle recieved data
        if (resultUnp != null) {

            if (resultUnp.size() <= 0) {

                System.out.println("No Books made after " + year + " found\n");

            } else {

                //System.out.println((Book)resultUnp.toString());
                return resultUnp;
            }

        }

        return new Vector(); // return empty vector if nothing is found

    }

    public Vector<Book> findBookWithTitleLike(String title) {

        String searchName = title;
        String query = "SELECT * FROM " + myTableName + " WHERE bookTitle LIKE '%" + title + "%'";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        if(resultUnp != null) {

            if (resultUnp.size() <= 0) {

                System.out.println("No Books with title like: " + title + " found\n");

            } else { 

                return resultUnp;
            }
        }

        return new Vector(); //return an empty Vector if nothing found

    }

    public Vector<Book> findBookWithAuthorLike(String auth) {

        String searchName = auth;
        String query = "SELECT * FROM " + myTableName + " WHERE author LIKE '%" + auth + "%'";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        if (resultUnp == null) {

            return new Vector(); // return an empty Vector if nothing found

        } else {

            if (resultUnp.size() <= 0) {

                System.out.println("No Books with author: " + auth + " found\n");

            } else {

                return resultUnp;

            }

        }

        return new Vector();
        
    }

    void instantiateBookList () {

        bookList = new Vector<>();

        return;

    }

    protected void initializeSchema(String tableName) {

        if (mySchema == null) {

            mySchema = getSchemaInfo(tableName);

        }
    }

    private int fixDateFormat(int fullDate) {

        return (fullDate % 10000);

    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }


    // UNSURE
    @Override
    public Object getState(String key) {
        if (key.equals("Accounts"))
			return bookList;
		else
		if (key.equals("AccountList"))
			return this;
		return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

}
