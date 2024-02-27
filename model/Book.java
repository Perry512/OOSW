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

public class Book extends EntityBase implements IView {

    private static final String myTableName = "Book";

    protected Properties dependencies;

    //private Vector<Book> bookList;

    //GUI

    private String updateStatusMessage = "";

    //constructor
    public Book(String bookNumber) throws InvalidPrimaryKeyException {

        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (BookId = " + bookNumber + ")";

        Vector<Properties> resultRaw = getSelectQueryResult(query);
        //Ensure one book is retrieved

        if (resultRaw != null) {

            int size = resultRaw.size();

            if (size != 1) {

                throw new InvalidPrimaryKeyException("Multiple Books found");

            } else {

                Properties retrievedBookData = resultRaw.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedBookData.propertyNames();

                while (allKeys.hasMoreElements() == true) {

                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedBookData.getProperty(nextKey);
                    // int bookNumber = Integer.parseInt(retrievedBookData.getProperty("bookNumber"));

                    if (nextValue != null) {

                        persistentState.setProperty(nextKey, nextValue);

                    }

                }
            }

        } else { // where book is not found

            throw new InvalidPrimaryKeyException("No Book matching id: " + bookNumber);

        }

    }

    public Book(Properties props) {

        super(myTableName);
        
        // setDependencies();
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {

            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {

                persistentState.setProperty(nextKey, nextValue);

            }
        }

    }

    private void setDependencies() {

        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);

    }

    public Object getState(String key) {

        if (key.equals("UpdateStatusMessage")) {

            return updateStatusMessage;
        }

        return persistentState.getProperty(key);

    }

    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);

    }

    public void updateState(String key, Object value) {

        stateChangeRequest(key, value);

    }

    // public boolean verifyOwnership(AccountHolder cust) {

    //     if (cust == null) {

    //         return false;

    //     } else {

    //         String custid = (String)cust.getState("ID");
    //         String myOwnerid = (String)getState("OwnerID");

    //         return (custid.equals(myOwnerid));

    //     }

    // }

    public void update() {

        updateStateInDatabase();

    }

    private void updateStateInDatabase() {

        try {

            if (persistentState.getProperty("bookId") != null) {

                Properties whereClause = new Properties();

                whereClause.setProperty("bookId",
                persistentState.getProperty("bookId"));

                updatePersistentState(mySchema, persistentState, whereClause);

                updateStatusMessage = "Book data for book number: " + persistentState.getProperty("bookId") + "updated successfully";

            } else {

                Integer bookNumber =  
                    insertAutoIncrementalPersistentState(mySchema, persistentState);
                
                    persistentState.setProperty("bookId", "" + bookNumber.intValue());

                    updateStatusMessage = "Book data for new book: " + persistentState.getProperty("bookId")
                        + "successfully added to database!";

            }

        } catch (SQLException e) {

            updateStatusMessage = "Error in installing book data: " + e + "\n";

        }

    }

    public Vector<String> getEntryListView() {

        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("bookId"));
        v.addElement(persistentState.getProperty("bookTitle"));
        v.addElement(persistentState.getProperty("author"));
        v.addElement(persistentState.getProperty("pubYear"));
        v.addElement(persistentState.getProperty("status"));

        return v;

    }

    protected void initializeSchema(String tableName) {

        if(mySchema == null) {

            mySchema = getSchemaInfo(tableName);

        }
    }

    public String toString() {

        return "Book: " + persistentState.getProperty("bookTitle") + "; Author: " +
            persistentState.getProperty("author") + "; Year: " +
            persistentState.getProperty("pubYear");
            
    }
}
