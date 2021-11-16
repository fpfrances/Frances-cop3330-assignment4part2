/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Filipe Frances
 */
package ucf.assignments;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest{
    @Test
    void parseDate_test(){
        LocalDate actual = Parser.parseDate("2021-10-23");
        LocalDate expected = LocalDate.of(2021,10,23);
        if(!actual.equals(expected))
            fail();
    }
    @Test
    void FileHandler_test_read_and_write() throws IOException{
        BufferedWriter writer = FileHandler.getWriter("testList.txt");
        writer.write("Computer Engineering");
        writer.close();
        File test = new File(FileHandler.getDirectory() + "/List_Data/testList.txt");
        if(!test.exists())
            fail();
        BufferedReader reader = FileHandler.getReader(test.getName());
        if(!reader.readLine().equalsIgnoreCase("Computer Engineering"))
            fail();
        reader.close();
        if(!test.delete())
            fail();
    }
    @Test
    void parseToListFile_Test() throws IOException{
        ToDoList test = new ToDoList("test");
        test.addItem(new ToDoItem("Z","z",Parser.parseDate("0000-01-01")));
        test.addItem(new ToDoItem("Y","y",Parser.parseDate("0000-02-02")));
        test.addItem(new ToDoItem("X","x",Parser.parseDate("0000-03-03")));
        Parser.parseToListFile(test);
        LinkedList<ToDoItem> test2 = Parser.loadList("test");
        for(ToDoItem item : test.getList()){
            //System.out.printf("> %s : %s : %s%n",item.getName(),item.getDescription(),item.getDate());
            ToDoItem tempItem = test2.removeFirst();
            //System.out.printf("> %s : %s : %s%n",tempItem.getName(),tempItem.getDescription(),tempItem.getDate());
            if(!item.getName().contentEquals(tempItem.getName()))
                fail();
            if(!item.getDescription().contentEquals(tempItem.getDescription()))
                fail();
            if(!item.getDate().equals(tempItem.getDate()))
                fail();
        }
        File delete = new File(FileHandler.getDirectory() + "/List_Data/test.json");
        delete.delete();
    }
    @Test
    void parseToCatalogFile() throws IOException{
        Catalog test1 = new Catalog();
        test1.addList(new ToDoList("L"));
        test1.addList(new ToDoList("P"));
        test1.addList(new ToDoList("D"));
        Parser.parseToCatalogFile(test1);
        LinkedList<ToDoList> test2 = Parser.loadCatalog();
        for (ToDoList list : test1.getCatalog()){
            //System.out.printf("> %s : %s%n",list.getName(),list.getSize());
            ToDoList tempList = test2.removeFirst();
            //System.out.printf("> %s : %s%n",tempList.getName(),tempList.getSize());
            if (!list.getName().contentEquals(tempList.getName()))
                fail();
            if (!list.getSize().contentEquals(tempList.getSize()))
                fail();
        }
        File delete = new File(FileHandler.getDirectory() + "/List_Data/catalog.json");
        System.out.println(delete.delete());
    }
}
