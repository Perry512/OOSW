package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;


public class Librarian implements IView, IModel {

    private Properties dependencies;
    private ModelRegistry myRegistry;

    private AccountHolder myAccountHolder;

    // GUI
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";

    // constructor for class
    // ========================================

    public Librarian() {

        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        myRegistry = new ModelRegistry("Librarian");

        if(myRegistry == null) {

            new Event(Event.getLeafLevelClassName(this), "Librarian", "Could not instantiate registry", Event.ERROR);

        }

        setDependencies();

        createAndShowLibrarianView();

    }

    private void setDependencies() {

        dependencies = new Properties();

        dependencies.setProperty("Login", "LoginError");
        dependencies.setProperty("Deposit", "TransactionError");

        myRegistry.setDependencies(dependencies);

    }

    // get value in particular field
    public Object getState(String key) {

        if (key.equals("LoginError") == true) {

            return loginErrorMessage;

        } else if (key.equals("TransactionError") == true) {

            return transactionErrorMessage;

        } else if (key.equals("Name") == true) {

            if (myAccountHolder != null) {

                return myAccountHolder.getState("Name");

            } else {

                return "Undefined";

            }
        } else {

            return "";

        }

    }

    public void stateChangeRequest(String key, Object value) {

        if (key.equals("Login") == true) {

            if (value != null) {

                loginErrorMessage = "";

                boolean flag = loginAccountHolder((Properties) value);

                if (flag) {

                    createAndShowTransactionChoiceView();

                }

            }

        } else if (key.equals("CancelTransaction")) {

            createAndShowTransactionChoiceView();

        } else if (key.equals("Deposit")) {

            String transType = key;

            if (myAccountHolder != null) {

                doTransaction(transType);

            } else {

                transactionErrorMessage = "Transaction impossible >:(";

            }

        } else if (key.equals("Logout")) {

            myAccountHolder = null;
            myViews.remove("TransactionChoiceView");

            createAndShowLibrarianView();

        }

        myRegistry.updateSubscribers(key, this);

    }

    public void updateState(String key, Object value) {

        stateChangeRequest(key, value);

    }   

    public boolean loginAccountHolder(Properties props) {

        try {

            myAccountHolder = new AccountHolder(props);
            return true;

        } catch (InvalidPrimaryKeyException e) {

                loginErrorMessage = "ERROR: " + e.getMessage();
                return false;

        } catch (PasswordMismatchException e) {

            loginErrorMessage = "ERROR: " + e.getMessage();
            return false;

        }

    }

    public void doTransaction(String transactionType) {

        try {

            Transaction tran = TransactionFactory.createTransaction(transactionType, myAccountHolder);
            tran.subscribe("CancelTransaction", this);
            tran.stateChangeRequest("DoYourJob", "");

        } catch (Exception e) {

            transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction";
            new Event (Event.getLeafLevelClassName(this), "createTransaction", "Transaction Creation Faliure: Unrecognized transaction " + e.toString(), Event.ERROR);

        }

    }

    private void createAndShowTransactionChoiceView() {

        Scene currentScene = (Scene)myViews.get("TransactionChoiceView");

        if (currentScene == null) {

            //create initial view
            View newView = ViewFactory.createView("TransactionChoiceView", this);
            currentScene = new Scene(newView);
            myViews.put("TransactionChoiceView", currentScene);

        }

        swapToView(currentScene);

    }

    private void createAndShowLibrarianView() {

        Scene currentScene = (Scene)myViews.get("LibrarianView");

        if (currentScene == null) {

            View newView = ViewFactory.createView("LibrarianView", this);
            currentScene = new Scene(newView);
            myViews.put("LibrarianView", currentScene);

        }

        swapToView(currentScene);

    }

    public void subscribe(String key, IView subcriber) {

        myRegistry.subscribe(key, subcriber);

    }

    public void unSubscribe(String key, IView subscriber) {

        myRegistry.unSubscribe(key, subscriber);

    }

    public void swapToView (Scene newScene) {

        if (newScene == null) {

            System.out.println("Librarian.swapToView(): missing view for display");

            new Event(Event.getLeafLevelClassName(this), "swapToView", "Missing view for display ", Event.ERROR);

            return;

        }

        myStage.setScene(newScene);
        myStage.sizeToScene();

        WindowPosition.placeCenter(myStage);
        
    }

}
