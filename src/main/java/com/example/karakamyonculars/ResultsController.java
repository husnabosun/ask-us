package com.example.karakamyonculars;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ResultsController {

    private static Stage window;
    public static int ayseCounter = 0;
    public static int berkCounter = 0;
    public static int husnaCounter = 0;
    public static int sevvalCounter = 0;
    public static int selenCounter = 0;
    public static int yagmurCounter = 0;
    public static int zumraCounter = 0;


    @FXML private Text ayseText;
    @FXML private Text berkText;
    @FXML private Text husnaText;
    @FXML private Text sevvalText;
    @FXML private Text selenText;
    @FXML private Text yagmurText;
    @FXML private Text zumraText;
    @FXML public Button backButton;

    @FXML private Label ayseVote;
    @FXML private Label berkVote;
    @FXML private Label husnaVote;
    @FXML private Label selenVote;
    @FXML private Label sevvalVote;
    @FXML private Label yagmurVote;
    @FXML private Label zumraVote;

    public static void display(){
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("KARA KAMYONCULAR POLL RESULTS");
        window.setMinWidth(400);

        String url = ConfigLoader.get("db.url");

        ArrayList<String> users = new ArrayList<>(Arrays.asList("Hüsneleybüs", "Ayşeleybüs", "Berkeleybüs", "Seleneybüs", "Şevvaleybüs", "Yağmureleybüs", "Zümreleybüs"));
        String sql = "SELECT voted_name FROM user_votes WHERE voter_name = ? AND vote_date = ?";

        LocalDate today = LocalDate.now();

        try{
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(ResultsController.class.getResource("/Results.fxml")));
            Parent root = loader.load();
            ResultsController controller = loader.getController();
            window.setScene(new Scene(root));
            window.show();
            try(Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
                PreparedStatement statement = conn.prepareStatement(sql)){
                for (String i : users){
                    statement.setString(1, i);
                    statement.setDate(2, java.sql.Date.valueOf(today));
                    ResultSet rs = statement.executeQuery();
                    if(rs.next()){
                        String votedName = rs.getString("voted_name");
                        switch (votedName) {
                            case "Ayşeleybüs" -> ayseCounter++;
                            case "Hüsneleybüs" -> husnaCounter++;
                            case "Berkeleybüs" -> berkCounter++;
                            case "Seleneybüs" -> selenCounter++;
                            case "Şevvaleybüs" -> sevvalCounter++;
                            case "Zümreleybüs" -> zumraCounter++;
                            case "Yağmureleybüs" -> yagmurCounter++;
                        }
                    }
                }
                controller.setResults(ayseCounter, husnaCounter, berkCounter, selenCounter, sevvalCounter, zumraCounter, yagmurCounter);
                resetCounters();
            }
            catch (SQLException e){
                e.printStackTrace();
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void setResults(int ayse, int husna, int berk, int selen, int sevval, int zumra, int yagmur) {
        ayseText.setText("Ayşebüs: " + ayseCounter);
        berkText.setText("Berkbüs: " + berkCounter);
        husnaText.setText("Hüsnabüs: " + husnaCounter);
        sevvalText.setText("Şevvalbüs: " + sevvalCounter);
        selenText.setText("Selenbüs: " + selenCounter);
        yagmurText.setText("Yağmurella: " + yagmurCounter);
        zumraText.setText("Zümrabüs: " + zumraCounter);
    }

    public void closeRes(){
        window.close();
    }

    public static boolean poll(String buttonText){
        boolean didVote = true;
        String url = ConfigLoader.get("db.url");
        String loggedUser = LoginWindow.getUsername();
        LocalDate today = LocalDate.now();
        String strToday = today.toString();

        String isValidSql = "SELECT * FROM user_votes WHERE voter_name = ? AND vote_date = ?";
        try(Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
            PreparedStatement statement = conn.prepareStatement(isValidSql)){
            statement.setString(1, LoginWindow.getUsername());
            statement.setString(2, strToday);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Zaten bugün oy kullandın ulan");
                alert.showAndWait();
                didVote = false;
            }
            else{
                String sql = "INSERT INTO user_votes (voter_name, voted_name, vote_date) VALUES (?, ?, ?)";

                try(PreparedStatement statement2 = conn.prepareStatement(sql)){
                    statement2.setString(1, loggedUser);
                    statement2.setString(2, buttonText);
                    statement2.setString(3, strToday);

                    statement2.executeUpdate();
                    cleanMemory(url);

                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return didVote;
        }

    private static void resetCounters() {
        ayseCounter = 0;
        husnaCounter = 0;
        berkCounter = 0;
        selenCounter = 0;
        sevvalCounter = 0;
        zumraCounter = 0;
        yagmurCounter = 0;
    }

    private static String whoToWho(String voterName) {
        String url = ConfigLoader.get("db.url");
        String sql = "SELECT voted_name FROM user_votes WHERE voter_name = ? AND vote_date = ?";
        LocalDate today = LocalDate.now();
        String strToday = today.toString();

        String voteFor = null;

        try (Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, voterName);
            statement.setString(2, strToday);
            ResultSet rs = statement.executeQuery();
            if (rs.next())
                voteFor = rs.getString("voted_name");
            else
                voteFor = "oy vermedi";

            return voteFor;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        yagmurVote.setText(whoToWho("Yağmureleybüs"));
        ayseVote.setText(whoToWho("Ayşeleybüs"));
        berkVote.setText(whoToWho("Berkeleybüs"));
        husnaVote.setText(whoToWho("Hüsneleybüs"));
        selenVote.setText(whoToWho("Seleneybüs"));
        sevvalVote.setText(whoToWho("Şevvaleybüs"));
        zumraVote.setText(whoToWho("Zümreleybüs"));

    }

    public static void cleanMemory(String url){
        String sql = "DELETE FROM user_votes WHERE vote_date < ?";
        LocalDate today = LocalDate.now();

        try(Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
            PreparedStatement statement = conn.prepareStatement(sql)){
            LocalDate sixDaysAgo = today.minusDays(6);
            String strSixDaysAgo = sixDaysAgo.toString();

            statement.setString(1, strSixDaysAgo);
            statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    }
