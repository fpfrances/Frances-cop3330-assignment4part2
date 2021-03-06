/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Filipe Frances
 */
package ucf.assignments;
import java.io.BufferedReader;
import java.io.File;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedList;

public class Parser{
    private static final JSONParser parse = new JSONParser();
    public static LinkedList<ToDoItem> loadList(String listName) throws IOException{
        try{
            return jsonReadList(FileHandler.getReader(listName + ".json"));
        }catch (IOException | ParseException e){
            new File(FileHandler.getDirectory()+"/List_Data/catalog.json").createNewFile();
        }
        return new LinkedList<>();
    }

    public static LinkedList<ToDoList> loadCatalog() throws IOException{
        try{
            return jsonReadCatalog(FileHandler.getReader("catalog.json"));
        }catch (IOException | ParseException e){
            System.out.println(new File(FileHandler.getDirectory() + "/List_Data/").mkdir());
            new File(FileHandler.getDirectory()+"/List_Data/catalog.json").createNewFile();
        }
        return new LinkedList<>();
    }

    private static LinkedList<ToDoItem> jsonReadList(BufferedReader in) throws IOException, ParseException{
        JSONArray jsonArray = (JSONArray) parse.parse(in);
        LinkedList<ToDoItem> list = new LinkedList<>();
        for (Object item : jsonArray){
            JSONObject jsonObj = (JSONObject) item;
            String name = (String) jsonObj.get("name");
            String description = (String) jsonObj.get("description");
            LocalDate date = parseDate((String) jsonObj.get("date"));
            boolean complete = (boolean) jsonObj.get("complete");
            ToDoItem newItem = new ToDoItem(name, description, date);
            newItem.setComplete(complete);
            list.add(newItem);
        }
        in.close();
        return list;
    }

    private static LinkedList<ToDoList> jsonReadCatalog(BufferedReader in) throws IOException, ParseException{
        JSONArray jsonArray = (JSONArray) parse.parse(in);
        LinkedList<ToDoList> catalog = new LinkedList<>();
        for (Object list : jsonArray){
            JSONObject jsonObj = (JSONObject) list;
            String name = (String) jsonObj.get("name");
            catalog.add(new ToDoList(name));
        }
        in.close();
        return catalog;
    }

    public static LocalDate parseDate(String stringDate){
        return LocalDate.parse(stringDate);
    }

    public static void parseToListFile(ToDoList list) throws IOException{
        JSONArray array = new JSONArray();
        for (ToDoItem item : list.getList()){
            JSONObject object = new JSONObject();
            object.put("name", item.getName());
            object.put("description", item.getDescription());
            object.put("date", String.valueOf(item.getDate()));
            object.put("complete", item.isComplete());
            array.add(object);
        }
        FileHandler.writeToFile(array.toJSONString(), list.getName() + ".json");
    }

    public static void parseToCatalogFile(Catalog catalog ) throws IOException{
        JSONArray array = new JSONArray();
        for(ToDoList list : catalog.getCatalog()){
            JSONObject object = new JSONObject();
            object.put("name" , list.getName());
            array.add(object);
        }
        FileHandler.writeToFile(array.toJSONString(),"catalog.json");
    }
}
