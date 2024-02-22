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

public abstract class BookCollection extends EntityBase implements IView {

    private static final String myTableName = "Book";

    private Vector<Book> bookList;
    //GUI

    //constructor
    public BookCollection(){

        super(myTableName);

    }

    public Vector<Book> findBooksOlderThan(String year) {
        String searchYear = year;
        Vector<Book>correctBooks = new Vector<>();

        return correctBooks;
        
    }

    public Vector<Book> findBooksYoungerThan(String year) {
        int searchYear = Integer.parseInt(year);
        String query = "SELECT * FROM " + myTableName + "WHERE (pubYear <= " + searchYear + ")";
        Vector<Book> resultRaw = getSelectQueryResult(query);

        //handle recieved data
        if (resultRaw != null) {
            
            int size = resultRaw.size();
            resultRaw = new Vector();

            if (size <= 0) {

                System.out.println("No Books of size " + size + " found\n");

            } else {

                return resultRaw;
            }
        }
    }

    public Vector<Book> findBookWithTitleLike(String title) {

        String searchName = title;
        

    }

    public Vector<Book> findBookWithAuthorLike(String auth) {

        
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


}