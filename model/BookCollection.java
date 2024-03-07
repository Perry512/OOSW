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
        Vector resultUnp = getSelectQueryResult(query);

        if (resultUnp != null) {

            bookList = new Vector<Book>();

            for (int count = 0; count < resultUnp.size(); count++){

                Properties nextBookData = (Properties) resultUnp.elementAt(count);
                Book book = new Book(nextBookData);

                if (book !=null) {

                    int index = findIndexToAdd(book);
                    bookList.insertElementAt(null, index);

                }

            }

        }

        return new Vector<>();
        
    }

    public Vector<Book> findBooksYoungerThan(String year) {

        int searchYear = Integer.parseInt(year);
        String query = "SELECT * FROM '" + myTableName + "' WHERE (pubYear <= " + fixDateFormat(searchYear) + ")";
        Vector resultUnp = getSelectQueryResult(query);

        //handle recieved data
        if (resultUnp != null) {

            bookList = new Vector<Book>();

            for (int i = 0; i < resultUnp.size(); i++) {

                Properties nextBookData = (Properties) resultUnp.elementAt(i);
                Book book = new Book(nextBookData);

                if (book != null) {

                    int index = findIndexToAdd(book);
                    bookList.insertElementAt(null, index);

                }
                
            }

        }

        return new Vector(); // return empty vector if nothing is found

    }

    public Vector<Book> findBookWithTitleLike(String title) {

        String searchName = title;
        String query = "SELECT * FROM " + myTableName + " WHERE bookTitle LIKE '%" + title + "%'";
        Vector resultUnp = getSelectQueryResult(query);

        if(resultUnp != null) {

            bookList = new Vector<Book>();
            
            for (int i = 0; i < resultUnp.size(); i++) {

                Properties nextBookData = (Properties) resultUnp.elementAt(i);
                Book book = new Book(nextBookData);

                if (book != null) {

                    int index = findIndexToAdd(book);
                    bookList.insertElementAt(null, index);

                    System.out.println("Book " + book.toString() + " has been found");

                }

            }

        }

        return new Vector(); //return an empty Vector if nothing found

    }

    public Vector<Book> findBookWithAuthorLike(String auth) {

        String searchName = auth;
        String query = "SELECT * FROM " + myTableName + " WHERE author LIKE '%" + auth + "%'";
        Vector resultUnp = getSelectQueryResult(query);

        if (resultUnp == null) {

            return new Vector(); // return an empty Vector if nothing found

        } else {

            bookList = new Vector<Book>();

            for (int i = 0; i < resultUnp.size(); i++) {

                Properties nextBookData = (Properties)resultUnp.elementAt(i);
                Book book = new Book(nextBookData);
                System.out.println("Books found and added to database");

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
        if (key.equals("Books"))
			return bookList;
		else
		if (key.equals("BookList"))
			return this;
		return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    private int findIndexToAdd(Book b) {

        int low = 0;
        int high = bookList.size() - 1;
        int middle;

        while (low <= high) {

            middle = (low + high) / 2;

            Book midSession = bookList.elementAt(middle);

            int result = Book.compare(b, midSession);

            if (result == 0) {

                return middle;

            } else if (result < 0) {

                high = middle - 1;

            } else {

                low = middle + 1;

            }

        }

        return low;

    }

}
