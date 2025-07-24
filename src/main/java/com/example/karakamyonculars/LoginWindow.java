package com.example.karakamyonculars;
import javafx.stage.*;
import  javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.sql.*;
import java.util.Objects;

public class LoginWindow {
    private static boolean loginSuccessful;
    private static String username;

    public static boolean display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setVgap(10);
        grid.setHgap(10);

        Label nameLabel = new Label("Username");
        nameLabel.getStyleClass().add("nameLabel");
        GridPane.setConstraints(nameLabel, 0, 0);

        TextField UserName = new TextField();
        UserName.setPromptText("adını gir ULAN");
        UserName.setAlignment(Pos.CENTER);
        UserName.getStyleClass().add("nameInput");
        GridPane.setConstraints(UserName, 1, 0);


        Label passwordLabel = new Label("Password");
        passwordLabel.getStyleClass().add("passwordLabel");
        GridPane.setConstraints(passwordLabel, 0, 1);

        PasswordField UserPassword = new PasswordField();
        UserPassword.setPromptText("şifreni gir ULAN");
        UserPassword.setAlignment(Pos.CENTER);
        UserPassword.getStyleClass().add("passwordInput");
        GridPane.setConstraints(UserPassword, 1, 1);


        Button loginButton = new Button("Log In");
        loginButton.setOnAction(e -> {
            if (UserPassword.getText().isEmpty()){
                showAlert(Alert.AlertType.ERROR, window, "Form Error!", "Please enter your password");
                return;
            }
            if (UserName.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, window, "Form Error!", "Please enter your username");
                return;
            }
            if(!isValid(UserName, UserPassword)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("error");
                alert.setHeaderText("Log In is unsuccessful");
                alert.setContentText("Please enter username and password");
                alert.showAndWait();

            }
            else{
                username = UserName.getText();
                loginSuccessful = true;
                window.close();
            }

        });
        GridPane.setConstraints(loginButton, 1, 2);




        grid.getChildren().addAll(nameLabel, UserName, passwordLabel, UserPassword, loginButton);
        Scene scene = new Scene(grid, 300 , 300);
        window.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(LoginWindow.class.getResource("/generalCss.css")).toExternalForm());
        window.showAndWait();
        return loginSuccessful;

    }
    private static boolean isValid(TextField UserName, PasswordField UserPassword) {
        boolean valid = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = ConfigLoader.get("db.url");
            String sql = "SELECT * FROM users WHERE UserName = ? AND UserPassword = ?";
            try (Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
                 PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, UserName.getText());
                statement.setString(2, UserPassword.getText());

                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next())
                        valid = true;
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

       return valid;
    }
    public static String getUsername(){
        return username;
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }


}
