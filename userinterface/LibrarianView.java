// specify the package
package userinterface;

// system imports
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.Properties;

// project imports
import impresario.IModel;

/** The class containing Librarian View */
//===========================================
public class LibrarianView extends View {

    // GUI
    protected TextField accountNumber;
    
    // For error message
    protected MessageView statusLog;

    // constructor for the class

    public LibrarianView(IModel librarian) {

        super(librarian, "LibrarianView");

        // create container
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));
        
        // add a title for panel
        container.getChildren().add(createTitle());

        // create our GUI components, add them to this container
        container.getChildren().add(createFormContent());

        container.getChildren().add(createStatusLog("           "));

        getChildren.add(container);

        populateFields();

        myModel.subscribe("ServiceCharge", this);
        myModel.subscribe("UpdateStatusMessage", this);

    }

    private Node createTitle() {

        HBox container = new HBox();
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" Librarian View ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;

    }

    private VBox createFormContent () {

        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));

        Text prompt = new Text("ACCOUNT INFORMATION");
        prompt.setWrappingWidth(400);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0,0,2,1);

        Text accNumLabel = new Text(" Account Number : ");
        Font myFont = Font.font("Helevetica", FontWeight.BOLD, 12);
        accNumLabel.setFont(myFont);
        accNumLabel.setWrappingWidth(150);
        accNumLabel.setTextAlignment(TextAlignment.RIGHT);
        grid.add(accNumLabel, 0, 1);

        accountNumber = new TextField();
        accountNumber.setEditable(false);
        grid.add(accountNumber, 1, 1);

        Text acctTypeLabel = new Text(" Account Type: ");
        

    }
}