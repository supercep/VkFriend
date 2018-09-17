package ru.unic.controller;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.json.simple.parser.ParseException;

public class MainController implements Initializable {
    static String userId;
    static String access_token;

    @FXML // fx:id="loginButton"
    private Button loginButton; // Value injected by FXMLLoader

    @FXML // fx:id="nameField"
    private TextField nameField; // Value injected by FXMLLoader

    @FXML // fx:id="sonameField"
    private PasswordField sonameField; // Value injected by FXMLLoader

    @FXML // fx:id="helloTab"
    private Label helloTab; // Value injected by FXMLLoader

    @FXML // fx:id="tabPane"
    private TabPane tabPane; // Value injected by FXMLLoader

    @FXML // fx:id="tabPane"
    private Tab tabPaneReg; // Value injected by FXMLLoader

    @FXML // fx:id="tabPane"
    private Tab tabPaneAbout; // Value injected by FXMLLoader

    @FXML // fx:id="tabPaneFr"
    private Tab tabPaneFr; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="textareaField"
    private TextArea textareaField; // Value injected by FXMLLoader

    @FXML // fx:id="dateCalendar"
    private TextField dateCalendar; // Value injected by FXMLLoader

    @FXML // fx:id="eraseButton"
    private Button eraseButton; // Value injected by FXMLLoader

    @FXML // fx:id="createBarButton"
    private Button createBarButton; // Value injected by FXMLLoader

    @FXML // fx:id="frArea"
    private TextArea frArea; // Value injected by FXMLLoader

    public void initialize(URL location, ResourceBundle resources) {

        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'hello.ru'.";
        assert createBarButton != null : "fx:id=\"createBarButton\" was not injected: check your FXML file 'hello.ru'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'hello.ru'.";
        assert sonameField != null : "fx:id=\"sonameField\" was not injected: check your FXML file 'hello.ru'.";
        assert passwordField != null : "fx:id=\"passTextField\" was not injected: check your FXML file 'hello.ru'.";
        assert helloTab != null : "fx:id=\"helloTab\" was not injected: check your FXML file 'hello.ru'.";
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'hello.ru'.";
        assert tabPaneReg != null : "fx:id=\"tabPaneReg\" was not injected: check your FXML file 'hello.ru'.";
        assert tabPaneFr != null : "fx:id=\"tabPaneCharts\" was not injected: check your FXML file 'hello.ru'.";
        assert tabPaneAbout != null : "fx:id=\"tabPaneAbout\" was not injected: check your FXML file 'hello.ru'.";
        assert textareaField != null : "fx:id=\"textareaField\" was not injected: check your FXML file 'hello.ru'.";
        assert dateCalendar != null : "fx:id=\"dateCalendar\" was not injected: check your FXML file 'hello.ru'.";
        assert eraseButton != null : "fx:id=\"eraseButton\" was not injected: check your FXML file 'hello.ru'.";
        assert frArea != null : "fx:id=\"eraseButton\" was not injected: check your FXML file 'hello.ru'.";
    }

    public void loginAction(ActionEvent actionEvent) {
        String fullInfo, login, password;
        if (fieldsChecker("login")) { //Проверяем, все ли поля заполнены и если да, то приветствуем
            login = nameField.getText();
            password = sonameField.getText();
            autoriseController(login, password);
            if (access_token != null && access_token != "" && userId != null && userId != "") {
                fullInfo = nameField.getText() + " " + userId + " " + halfDay("getTime") + "\n";
                tabPaneAbout.setDisable(false);
                tabPaneFr.setDisable(false);
                putToDB(fullInfo);
            } else {
                alert("Вы не зашли", "", "Проверьте правильность введенных данных!");
                putToDB("Поля заполнены, произошла ошибка при получении токена, время - " + halfDay("getTime") + " Login - " + nameField.getText() + "\n");
            }
        } else {
            alert("Вы не зашли", "", "Введите Логин и пароль!");
        }
    }
    public void createBar(ActionEvent actionEvent) throws ParseException {
        delDogs();
    }

    private void putToDB(String fullInfo) {
        String currentDate = halfDay("date");
        File filePath = new File("historyLogs");
        filePath.mkdir();
        File file = new File(filePath + "/" + currentDate + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter writer = new FileWriter(filePath + "/" + currentDate + ".txt", true)) {
            // запись всей строки
            writer.write(fullInfo);

            writer.flush();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    }


    public void getPassword(ActionEvent actionEvent) {  //passAction on EnterPress
        String currentDate;
        if (dateCalendar.getText().isEmpty() || dateCalendar.getText() == "") {
            currentDate = halfDay("date");
        } else {
            currentDate = dateCalendar.getText();
        }

        if (passwordField.getText().equals("Удалов")) {
            textareaField.setText("");
            int count = 0;
            File filePath = new File("historyLogs");
            filePath.mkdir();

            try (FileReader reader = new FileReader(filePath + "/" + currentDate + ".txt")) {
                // читаем посимвольно
                int c;
                String stage = new String();
                while ((c = reader.read()) != -1) {
                    stage += (char) c;
                }

                textareaField.setText(stage);

            } catch (IOException ex) {

                System.out.println(ex.getMessage());
            }
            passwordField.setText("");

        }
    }

    public boolean fieldsChecker(String action) {
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            helloTab.setVisible(true);
            helloTab.setText("Вы не ввели Логин.");
            return false;
        } else if (sonameField.getText() == null || sonameField.getText().trim().isEmpty()) {
            helloTab.setVisible(true);
            helloTab.setText("Вы не ввели Пароль.");
            return false;
        } else {
            helloTab.setVisible(true);
            if (action == "login") {
                helloTab.setText(halfDay("time") + nameField.getText() + "!");
                return true;
            }
        }
        return false;
    }

    public String halfDay(String half) { // По текущему времени вычисляем часть дня
        String halfOfDay = new String();
        String minHalf[];
        int minutes, hours;
        Date now = new Date();

        if (half == "time") {

            DateFormat df = new SimpleDateFormat("HH:mm");

            // Model Data
            String dateTimeString = df.format(now);

            minHalf = dateTimeString.split(":");

            hours = Integer.parseInt(minHalf[0]);
            minutes = Integer.parseInt(minHalf[1]);
            if ((hours >= 6 && minutes >= 0) && (hours <= 11 && minutes <= 59)) {
                halfOfDay = "Доброе утро, ";
            } else if ((hours >= 12 && minutes >= 0) && (hours <= 18 && minutes <= 59)) {
                halfOfDay = "Добрый день, ";
            } else if ((hours >= 19 && minutes >= 0) && (hours <= 21 && minutes <= 59)) {
                halfOfDay = "Добрый вечер, ";
            } else if (((hours >= 22 && minutes >= 0) && (hours <= 23 && minutes <= 59) || (hours >= 0 && minutes >= 0) && (hours <= 5 && minutes <= 59))) {
                halfOfDay = "Доброй ночи, ";
            }
        } else if (half == "date") {
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

            // Model Data
            halfOfDay = df.format(now);
        } else if (half == "getTime") {
            DateFormat df = new SimpleDateFormat("HH:mm");

            // Model Data
            halfOfDay = df.format(now);
        }
        return halfOfDay;
    }

    public void selectStat(Event event) {
        setDatesOnTable();
    }

    public void eraseStat(ActionEvent actionEvent) {
        setDatesOnTable();
        dateCalendar.setText("");
    }

    public void setDatesOnTable() {
        File filePath = new File("historyLogs");
        int count = 0;
        String[] files = filePath.list(new FilenameFilter() {

            @Override
            public boolean accept(File folder, String name) {
                return name.endsWith(".txt");
            }

        });
        for (int b = 0; b < files.length; b++) {
            count++;
        }
        String fileList = new String();
        for (int i = 0; i < count; i++) {
            fileList += files[i];
        }

        count = 0;

        String[] dateList = fileList.split(".txt");

        for (int b = 0; b < dateList.length; b++) {
            count++;
        }
        String dateListArr = new String();
        dateListArr += "Сейчас, если у Вас есть доступ, Вы можете Выбрать эти даты: \n";
        for (int i = 0; i < count; i++) {
            dateListArr += dateList[i] + "\n";
        }

        textareaField.setText(dateListArr);

    }

    public static String getHTML(String urlToRead) {
        URL url;
        HttpURLConnection conn;
        BufferedReader rd;
        String line;
        String result = "";
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void autoriseController(String userLogin, String userPassword) {
        String htmlRet = getHTML("https://oauth.vk.com/token?grant_type=password&client_id=2274003&client_secret=hHbZxrka2uZ6jB1inYsH&username=" + userLogin + "&password=" + userPassword);
        access_token = htmlRet.split("\"")[3];
        userId = htmlRet.split("\"")[8].replace(":", "").replace("}", "");
    }

    public String methodController(String method, String params, String access_token) {
        return getHTML("https://api.vk.com/method/" + method + "?" + params + "&access_token=" + access_token + "&v=5.84");
    }

    public void delDogs() throws ParseException {
        String preFriendList = methodController("friends.get", "user_id=" + userId, access_token);
        String friendList = preFriendList.split(":")[3].replace("[", "").replace("]", "").replace("}", "");
        String getInfo = methodController("users.get", "user_ids=" + friendList, access_token);
        ArrayList finRes = jsonParse(getInfo);
        String delHist = new String();
        if (finRes.size() > 0) {
            for (int i = 0; i < finRes.size(); i++) {
                methodController("friends.delete", "user_id=" + finRes.get(i), access_token);
                delHist += finRes.get(i) + ", ";
            }
            frArea.setText("Мы удалили следующих друзей- " + delHist);
        }
        frArea.setText("У Вас нет забаненных или удаленных друзей!");

    }

    public void alert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public ArrayList jsonParse(String text) throws ParseException {
        String[] pairUserStat = new String[0];
        int langSize = 0;

            // read the json file
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(text);

            // get an array from the JSON object
            JSONArray lang = (JSONArray) jsonObject.get("response");
            langSize = lang.size();
            pairUserStat = new String[lang.size()];
            // take the elements of the json array
            for (int i = 0; i < lang.size(); i++) {
                JSONObject jsonObject1 = (JSONObject) lang.get(i);
                long id = (long) jsonObject1.get("id");
                String dFlag = (String.valueOf(jsonObject1.get("deactivated")));

                pairUserStat[i] = id + ";" + dFlag;
            }


        ArrayList finRes = new ArrayList();


        for (int i = 0; i < langSize; i++) {
            if (pairUserStat[i].contains("deleted") || pairUserStat[i].contains("banned"))
                finRes.add(pairUserStat[i].split(";")[0]);
        }

        return finRes;
    }
}
