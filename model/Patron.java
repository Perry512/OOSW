// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

public class Patron extends EntityBase implements IView {

    private static final String myTableName = "Patron";

    protected Properties dependencies;

    // GUI

    private String updateStatusMessage = "";

    // constructor

    public Patron(String patronNumber) throws InvalidPrimaryKeyException {

        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (patronNumber = " + patronNumber + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {

            int size = allDataRetrieved.size();

            if (size != 1) {

                throw new InvalidPrimaryKeyException("Multiple patrons matching id: " + patronNumber + " found.");

            } else {

                Properties retrievedPatronData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedPatronData.propertyNames();

                while (allKeys.hasMoreElements()) {

                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedPatronData.getProperty(nextKey);

                    if (nextValue != null) {

                        persistentState.setProperty(nextKey, nextValue);

                    }

                }

            }

        } else {

            throw new InvalidPrimaryKeyException("No Patron matching id: " + patronNumber + " found.");

        }

    }

    public Patron(Properties props) {

        super(myTableName);

        setDependencies();
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

    // public boolean verifyOwnership()

    public void update() {

        updateStateInDatabase();

    }

    private void updateStateInDatabase() {

        try {

            if (persistentState.getProperty("patronId") != null) {

                Properties whereClause = new Properties();
                whereClause.setProperty("patronId", persistentState.getProperty("patronId"));

                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Patron data for patron number: " + persistentState.getProperty("patronId") + "installed into database";

            }

        } catch (SQLException e) {

            updateStatusMessage = "Error in installing patron data in database: " + e;

        }
        // maybe useful debug System.out.println("updateState: " + updateStatusMessage);
    }

    public Vector<String> getEntryListView() {

        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("patronId"));
        v.addElement(persistentState.getProperty("name"));
        v.addElement(persistentState.getProperty("address"));
        v.addElement(persistentState.getProperty("city"));
        v.addElement(persistentState.getProperty("stateCode"));
        v.addElement(persistentState.getProperty("zip"));
        v.addElement(persistentState.getProperty("email"));
        v.addElement(persistentState.getProperty("dateOfBirth"));
        v.addElement(persistentState.getProperty("status"));

        return v;

    }

    protected void initializeSchema(String tableName) {

        if (mySchema == null) {

            mySchema = getSchemaInfo(tableName);

        }
    }

    public String toString() {

        return "Patron: " + persistentState.getProperty("patronId") + "; Name: " +
            persistentState.getProperty("name") + "; Address: " +
            persistentState.getProperty("address") + "; city: " + 
            persistentState.getProperty("city") + "; stateCode: " +
            persistentState.getProperty("stateCode") + "; zip: " +
            persistentState.getProperty("zip") + "; email: " + 
            persistentState.getProperty("dateOfBirth") + "; status: " +
            persistentState.getProperty("status");

    }    
    
}