package com.example.karakamyonculars;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Objects;

import static com.example.karakamyonculars.LoginWindow.showAlert;

public class PasswordChangeWindow {

    public static void display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(400);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(30, 30, 30, 30));
        grid.setVgap(10);
        grid.setHgap(10);

        Label newPasswordLabel = new Label("New Password");
        newPasswordLabel.getStyleClass().add("newPasswordLabel");
        GridPane.setConstraints(newPasswordLabel, 0, 0);

        PasswordField NewPassword = new PasswordField();
        NewPassword.setPromptText("yeni şifre ULAN");
        NewPassword.setAlignment(Pos.CENTER);
        NewPassword.getStyleClass().add("NewPassword");
        GridPane.setConstraints(NewPassword, 1, 0);

        Label confirmPasswordLabel = new Label("Confirm Password");
        confirmPasswordLabel.getStyleClass().add("confirmPasswordLabel");
        GridPane.setConstraints(confirmPasswordLabel, 0, 1);

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("yeni şifre tekrar");
        confirmPassword.setAlignment(Pos.CENTER);
        confirmPassword.getStyleClass().add("confirmPassword");
        GridPane.setConstraints(confirmPassword, 1, 1);

        Button button = new Button("Change Password");
        button.setOnAction(e -> {
            if (confirmPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, window, "Form Error!", "Please enter your new password again");
                return;
            }
            if (NewPassword.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, window, "Form Error!", "Please enter your new password");
                return;
            }
            if (!confirmPassword.getText().equals(NewPassword.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Change is unsuccessful");
                alert.setContentText("New password and confirmation do not match.");
                alert.showAndWait();
            } else {
                String newPassword = NewPassword.getText();
                String url = ConfigLoader.get("db.url");
                String userName = LoginWindow.getUsername();
                String sql = "UPDATE users SET UserPassword = ? WHERE UserName = ?";
                try (Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
                     PreparedStatement statement = conn.prepareStatement(sql)) {
                    statement.setString(1, newPassword);
                    statement.setString(2, userName);

                    int affectedRows = statement.executeUpdate();

                    if (affectedRows > 0){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Password changed successfully!");
                        alert.showAndWait();

                        window.close();
                    }
                } catch (SQLException a) {
                    a.printStackTrace();
                }
            }
        });
        GridPane.setConstraints(button, 1, 2);

        grid.getChildren().addAll(newPasswordLabel, NewPassword, confirmPasswordLabel, confirmPassword, button);
        grid.setAlignment(Pos.CENTER);
        Scene scene = new Scene(grid, 400, 300);
        window.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(LoginWindow.class.getResource("/generalCss.css")).toExternalForm());
        window.showAndWait();

    }
}
