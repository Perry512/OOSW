// specify the package
package model;

// system imports
import java.util.Properties;
import java.util.Vector;
import javafx.scene.Scene;

// project imports
import exception.InvalidPrimaryKeyException;
import event.Event;
import database.*;

import impresario.IView;

import userinterface.View;
import userinterface.ViewFactory;

public class PatronCollection extends EntityBase implements IView {

    private static final String myTableName = "Patron";

    private Vector<Patron> patrons;
    //GUI

    //Constructor
    //
    public PatronCollection() {
        super(myTableName);
        patrons = new Vector<Patron>();
    }

    public PatronCollection(Patron patron) throws Exception {

        super(myTableName);

        if(patron == null) {

            new Event(Event.getLeafLevelClassName(this), "<init>", "Missing patron information", Event.FATAL);
            throw new Exception ("Unexpected error: Patron.<init>: account holder information is null");

        }

        String patronId = (String)patron.getState("patronId");

        if (patronId == null) {

            new Event(Event.getLeafLevelClassName(this), "<init>", "Data Bad: Patron has no id", Event.FATAL);

            throw new Exception("patronId is null");

        }

        String query = "SELECT * FROM " + myTableName + " WHERE (patronId = " + patronId + ")";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {

            patrons = new Vector<Patron>();

            for (int i = 0; i < allDataRetrieved.size(); i++) {

                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(i);

                Patron newPatron = new Patron(nextPatronData);


                if (newPatron != null) {

                    addPatron(newPatron);

                }
            }
        } else {

            throw new InvalidPrimaryKeyException("No Patron found");

        }

    }
    
    private void addPatron(Patron p) {

        //patron.add(p)????
        int index = findIndexToAdd(p);
        patrons.insertElementAt(p, index);

        }

    private int findIndexToAdd(Patron p) {

        int low = 0;
        int high = patrons.size() - 1;
        int middle;

        while (low <= high) {

            middle = (low + high) / 2;

            Patron midSession = patrons.elementAt(middle);

            int result = Patron.compare(p, midSession);

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

    public Object getState(String key) {

        if (key.equals("Patrons")) {

            return patrons;

        } else if (key.equals("PatronList")) {

            return this;

        }

        return null;

    }

    public void stateChangeRequest(String key, Object value) {

        myRegistry.updateSubscribers(key, this);

    }

    public Patron retrieve(String patronId) {

        Patron retValue = null;

            for (int i = 0; i < patrons.size(); i++) {

                Patron nextPatron = patrons.elementAt(i);
                String nextPatronNum = (String)nextPatron.getState("PatronId");

                if (nextPatronNum.equals(patronId)) {

                    retValue = nextPatron;
                    return retValue;

                }

            } 

        return retValue;

    }

    public void updateState(String key, Object value) {

        stateChangeRequest(key, value);

    }

    protected void createAndShowView() {

        Scene localScene = myViews.get("PatronCollectionView");

        if (localScene == null) {

            View newView = ViewFactory.createView("ParonCollectionView", this);
            localScene = new Scene(newView);

            myViews.put("PatronCollectionView", localScene);


        }

        swapToView(localScene);

    }

    protected void initializeSchema(String tableName) {

        if (mySchema == null) {

            mySchema = getSchemaInfo(tableName);

        }
        
    }

    public Vector<Patron> findPartonsOlderThan(int year) {

        String query = "'SELECT * FROM " + myTableName + " WHERE dateOfBith <= " + fixDateFormat(year) + ")'";
        Vector<Patron> resultUnp = getSelectQueryResult(query);

        if (resultUnp != null) {

            if (resultUnp.size() <= 0) {

                System.out.println("No Patron born before " + year + " found\n");

            } else {

                return resultUnp;

            }

        }

        return new Vector<>();

    }

    public Vector<Patron> findPatronsYoungerThan(String year) {

        String query = "SELECT * FROM " + myTableName + " WHERE dateOfBirth >= ('" + year + "')";
        Vector<Patron> resultUnp = getSelectQueryResult(query);

        if (resultUnp != null) {

            if(resultUnp.size() <= 0) {

                System.out.println("No Patron born after " + year + " found\n");

            } else {

                return resultUnp;

            }

        }

        return new Vector<>();

    }

    public Vector<Patron> findPatronsAtZipCode(String zip) {

        String query = "SELECT * FROM " + myTableName + " WHERE zip LIKE '%" + zip + "%'";
        Vector<Patron> resultUnp = getSelectQueryResult(query);

        if (resultUnp != null) {

            if (resultUnp.size() <= 0) {

                System.out.println("No Patron with Zip: " + zip + " found\n");

            } else {

                return resultUnp;

            }

        } 
        
        return new Vector<>();

    }

    public Vector<Patron> findPatronsWithNameLike(String name) {

        String query = "SELECT * FROM '" + myTableName + "' WHERE name LIKE '%" + name + "%'";
        Vector<Patron> resultUnp = getSelectQueryResult(query);

        if(resultUnp != null) {

            if (resultUnp.size() <= 0) {

                System.out.println("No Patrons with name like: " + name + " found\n");

            } else {

                return resultUnp;

            }

        }

        return new Vector<>();

    }


    private int fixDateFormat(int yearIn) {

        return (yearIn % 10000);

    }
}