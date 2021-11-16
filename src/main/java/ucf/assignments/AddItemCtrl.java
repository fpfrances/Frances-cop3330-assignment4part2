/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Filipe Frances
 */
package ucf.assignments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class AddItemCtrl{
    public static Stage AddItemStage;
    private Stage dialogStage;
    // gui nodes
    @FXML
    private TextField name;
    @FXML
    private TextArea description;
    @FXML
    private DatePicker date;
    public void setDialogStage(Stage stage){
        dialogStage = stage;
        AddItemStage = stage;
        AddItemStage.setResizable(false);
    }

    public AddItemCtrl (){
    }

    @FXML
    void submitButtonClicked(ActionEvent action){
        // check if the object data is considered valid
        // create a new object from user input data
        // add the object to the current list
        if(isInputValid()){
            ToDoItem newItem = new ToDoItem(name.getText(),description.getText(),date.getValue());
            ToDo.activeList.addItem(newItem);
            try{
                AddItemStage.close();
                ListOptCtrl.ListOptionsStage.close();
                Parent root = FXMLLoader.load(getClass().getResource("ListOptions.fxml"));
                Scene scene = new Scene(root);
                ToDo.mainScene.setTitle("List Options");
                ToDo.mainScene.setScene(scene);
                ToDo.mainScene.show();
                FXMLLoader loader = new FXMLLoader();
                ListOptCtrl controller = new ListOptCtrl();
                controller.setStage(ToDo.mainScene);
                loader.setController(controller);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private boolean isInputValid(){
        // check if its alphanumerical name
        // check if its 1-20 characters long
        // check if its already exists another list with same name
        // if any of the above are incorrect, create an error dialog and ask the user to fix the input data
        String errorMessage = "";
        for(ToDoItem item : ToDo.activeList.getList())
            if(item.getName().equalsIgnoreCase(name.getText())){
                errorMessage += "Item already exists!\n";
                break;
            }
        if (name.getText() == null || name.getText().length() == 0 ){
            errorMessage += "Not a valid name!\n";
        }
        if (name.getText().length() >20){
            errorMessage += "Name too long!\n";
        }
        if (description.getText() == null || description.getText().length() == 0 ){
            errorMessage += "Not a valid Description!\n";
        }
        if (description.getText().length() >256){
            errorMessage += "Description too long!\n";
        }
        if (date.getValue() == null){
            errorMessage += "Invalid Date!\n";
        }
        if (errorMessage.length() == 0){
            return true;
        }else{
            // Show the error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    void cancelButtonClicked (ActionEvent action){
        dialogStage.close();
    }
}
