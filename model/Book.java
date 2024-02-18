package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;
import impresario.ModelRegistry;
import userinterface.View;
import userinterface.ViewFactory;

public abstract class Book extends EntityBase implements IView {

    private static final String myTableName = "Account";

    protected Properties dependencies;

    //private Vector<Book> bookList;

    //GUI

    private String updateStatusMessage = "";

    //constructor
    public Book(String bookNumber) throws InvalidPrimaryKeyException {

        super(myTableName);

        String query = "SELECT * FROM " + myTableName + " WHERE (BookId = " + bookNumber + ")";

        Vector<Properties> resultRaw = getSelectQueryResult(query);
        //Ensure one book is retrieved

        if (resultRaw != null) {

            int size = resultRaw.size();
        }

    }
}