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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.ParseException;

public class ListCatalogCtrl{
    public static Stage ListCatalogStage;
    private Stage dialogStage = new Stage();
    private static ToDoList selected;
    private static ObservableList<ToDoList> catalog;
    public ListCatalogCtrl(){
    }

    //for displaying the table
    @FXML private TableView<ToDoList> tableView;
    @FXML private TableColumn<ToDoList,String> nameColumn;

    public ListCatalogCtrl (Stage dialogStage){
        this.dialogStage = dialogStage;
        ListCatalogStage = this.dialogStage;
        ListCatalogStage.setResizable(false);
    }

    public static ToDoList getSelected(){
        return selected;
    }

    @FXML
    public void initialize(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        catalog = FXCollections.observableArrayList();
        catalog.addAll(ToDo.catalog.getCatalog());
        tableView.setItems(catalog);
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> updateSelected(newValue)
        );
    }

    private void updateSelected(ToDoList newValue){
        selected = newValue;
        ToDo.activeList = newValue;
    }

    @FXML
    void renameButtonClicked (ActionEvent action) throws IOException {
        // open a new window to prompt the user for a new lists name to overwrite the old list name
        // update the list name to the new name
        // update window/files
        if(selected == null)
            return;
        Parent root = FXMLLoader.load(getClass().getResource("RenameList.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setTitle("Edit List");
        dialogStage.setScene(scene);
        dialogStage.show();
        FXMLLoader loader = new FXMLLoader();
        RenameListCtrl controller = new RenameListCtrl();
        controller.setDialogStage(this.dialogStage);
        controller.setList(selected);
        loader.setController(controller);
    }

    @FXML
    void newButtonClicked (ActionEvent action) throws IOException{
        // prompt the user for a list name
        // create a new list using a name the user enters
        // update window
        Parent root = FXMLLoader.load(getClass().getResource("AddList.fxml"));
        Scene scene = new Scene(root);
        dialogStage.setTitle("New List");
        dialogStage.setScene(scene);
        dialogStage.show();
        FXMLLoader loader = new FXMLLoader();
        AddListCtrl controller = new AddListCtrl();
        controller.setDialogStage(this.dialogStage);
        loader.setController(controller);
    }

    @FXML
    void deleteButtonClicked (ActionEvent action){
        // delete the list and update the catalog
        // update the files
        if(selected == null)
            return;
        ToDo.catalog.deleteList(selected);
        selected = null;
        try{
            Parser.parseToCatalogFile(ToDo.catalog);
            ListCatalogCtrl.ListCatalogStage.close();
            Parent root = FXMLLoader.load(getClass().getResource("ListCatalog.fxml"));
            Scene scene = new Scene(root);
            ToDo.mainScene.setScene(scene);
            ToDo.mainScene.show();
            FXMLLoader loader = new FXMLLoader();
            ListCatalogCtrl controller = new ListCatalogCtrl(ToDo.mainScene);
            loader.setController(controller);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }
    }

    @FXML
    void onOpenButtonClicked(ActionEvent action) throws IOException, ParseException {
        // create the list options window for the list that was opened
        if(selected == null)
            return;
        ToDo.activeList = new ToDoList(ToDo.activeList.getName());
        ToDo.activeList.addAllItems(Parser.loadList(ToDo.activeList.getName()));
        Parent root = FXMLLoader.load(getClass().getResource("ListOptions.fxml"));
        Scene scene = new Scene(root);
        ToDo.mainScene.setTitle("New List");
        ToDo.mainScene.setScene(scene);
        ToDo.mainScene.show();
        FXMLLoader loader = new FXMLLoader();
        ListOptCtrl controller = new ListOptCtrl();
        controller.setStage(ToDo.mainScene);
        loader.setController(controller);
    }
}
