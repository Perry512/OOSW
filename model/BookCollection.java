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

    public void findBooksYoungerThan(String year) {
        int searchYear = Integer.parseInt(year);
        String query = "SELECT * FROM " + myTableName + " WHERE (pubYear <= " + searchYear + ")";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        //handle recieved data
        if (resultUnp != null) {
            
            Vector<Book> foundBooks;

            if (resultUnp.size() <= 0) {

                System.out.println("No Books made after " + year + " found\n");

            } else {

                System.out.println((Book)resultUnp.toString());
                //return resultUnp.toString();
            }
        }

        //return new Vector(); // return empty vector if nothing is found

    }

    public Vector<Book> findBookWithTitleLike(String title) {

        String searchName = title;
        String query = "SELECT * FROM " + myTableName + " WHERE bookTitle LIKE '%" + title + "%'";
        Vector<Book> resultUnp = getSelectQueryResult(query);

        if(resultUnp != null) {

            int (size <= 0) {

                System.out.println("No Books with title like: " + );
            }
        }


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
