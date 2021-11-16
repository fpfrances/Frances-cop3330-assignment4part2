/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Filipe Frances
 */
package ucf.assignments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;

public class ListOptCtrl{
    public static Stage ListOptionsStage;
    private Stage dialogStage = new Stage();

    @FXML
    private Label listName;

    @FXML
    private CheckBox completed;

    @FXML
    private Label itemDescription;

    @FXML
    private Label itemDate;

    @FXML
    private Label itemName;

    private static ToDoItem selected;

    private ObservableList<ToDoItem> list;

    @FXML private TableView<ToDoItem> tableView;
    @FXML private TableColumn<ToDoItem,String> nameColumn;


    @FXML
    private void initialize(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        this.list = FXCollections.observableArrayList();
        this.list.addAll(ToDo.activeList.getList());
        this.listName.setText(ToDo.activeList.getName());
        tableView.setItems(this.list);
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showItem(newValue)
        );
        showItem(null);
    }

    public void setStage(Stage stage){
        dialogStage = stage;
        ListOptionsStage = stage;
        ListOptionsStage.setResizable(false);
    }

    public static ToDoItem getSelected(){
        return selected;
    }

    private void showItem(ToDoItem newValue){
        selected = newValue;
        if(newValue == null){
            this.itemName.setText("");
            this.itemDate.setText("");
            this.itemDescription.setText("");
            return;
        }
        this.itemName.setText(newValue.getName());
        this.itemDate.setText(newValue.getDate().toString());
        this.itemDescription.setText(newValue.getDescription());
        this.completed.setSelected(newValue.isComplete());
    }

    @FXML
    void completeBoxChecked(ActionEvent event){
        // update the current item's 'complete' value
        if(selected == null)
            return;
        selected.swapComplete();
    }

    @FXML
    void allButtonClicked(ActionEvent event){
        // show all items in the current list
        list = FXCollections.observableArrayList();
        list.addAll(ToDo.activeList.getList());
        tableView.setItems(list);
    }

    @FXML
    void completedButtonClicked(ActionEvent event){
        // only show all the items in the current list marked as 'complete'
        ToDoList visibleList = new ToDoList(ToDo.activeList.getName());
        for(ToDoItem item : ToDo.activeList.getList()) {
            if(item.isComplete())
                visibleList.addItem(item);
        }
        list = FXCollections.observableArrayList();
        list.addAll(visibleList.getList());
        tableView.setItems(list);
    }

    @FXML
    void deleteButtonClicked(ActionEvent event){
        // get the currently viewed item's data and create a new temporary object
        // ask the user to confirm if they want to follow through with the deletion
        // if yes, then call the deletion method for that list
        // if no, return and do nothing
        if(selected == null)
            return;
        try{
            ToDo.activeList.getList().remove(selected);
            Parent root = FXMLLoader.load(getClass().getResource("ListOptions.fxml"));
            Scene scene = new Scene(root);
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

    @FXML
    void editButtonClicked(ActionEvent event) throws IOException{
        // get the currently viewed item's data and create a new temporary object
        // ask the user for updated information
        // call the edit item method for that list
        // return to list page
        if(selected == null)
            return;
        Parent root = FXMLLoader.load(getClass().getResource("EditItem.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setTitle("Edit Item");
        dialogStage.setScene(scene);
        dialogStage.show();
        FXMLLoader loader = new FXMLLoader();
        EditItemCtrl controller = new EditItemCtrl();
        controller.setDialogStage(dialogStage);
        loader.setController(controller);
    }

    @FXML
    void incompleteButtonClicked(ActionEvent event){
        // only show all the of the items in the current list marked as 'incomplete'
        ToDoList visibleList = new ToDoList(ToDo.activeList.getName());
        for(ToDoItem item : ToDo.activeList.getList()){
            if(!item.isComplete())
                visibleList.addItem(item);
        }
        list = FXCollections.observableArrayList();
        list.addAll(visibleList.getList());
        tableView.setItems(list);
    }

    @FXML
    void newButtonClicked(ActionEvent event) throws IOException{
        // prompt the user for new item data
        // use data to create a new item object
        // call add item method to current list
        Parent root = FXMLLoader.load(getClass().getResource("AddItem.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setTitle("New Item");
        dialogStage.setScene(scene);
        dialogStage.show();
        FXMLLoader loader = new FXMLLoader();
        AddItemCtrl controller = new AddItemCtrl();
        controller.setDialogStage(dialogStage);
        loader.setController(controller);
    }

    @FXML
    void returnToListsButtonClicked(ActionEvent event) throws IOException{
        // reopen/bring the catalog window the foreground
        // close list window
        Parent root = FXMLLoader.load(getClass().getResource("ListCatalog.fxml"));
        Scene scene = new Scene(root);
        ToDo.mainScene.setScene(scene);
        ToDo.mainScene.show();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new ListCatalogCtrl(ToDo.mainScene));
    }

    @FXML
    void saveListButtonClicked(ActionEvent action) throws IOException{
        Parser.parseToCatalogFile(ToDo.catalog);
        Parser.parseToListFile(ToDo.activeList);
    }

    public ListOptCtrl(){
    }
}