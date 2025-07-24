package com.example.karakamyonculars;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene .control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import  javafx.scene.image.ImageView;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.time.LocalDate;

public class Main extends Application {
    Stage window;


    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        boolean res = LoginWindow.display("Log In", "gir ulan");
        if (!res){
            Platform.exit();
            return;
        }
        window = primaryStage;
        window.setTitle("ASK İYTE KARA KAMYONCULAR");


        String userName = LoginWindow.getUsername();


        BorderPane borderPane = new BorderPane();

        // TOP MENU
        HBox topMenu = new HBox();
        topMenu.setPadding(new Insets(15, 12, 15, 12));
        topMenu.setSpacing(10);

        Button buttonProfile = new Button(userName);
        buttonProfile.setPrefSize(100, 20);


        topMenu.getChildren().addAll(buttonProfile);
        borderPane.setTop(topMenu);

        // LEFT MENU
        VBox leftMenu = new VBox();
        leftMenu.setPadding(new Insets(10));
        leftMenu.setSpacing(8);
        Text title = new Text("Kara Kamyonculars");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        title.setFill(Color.web("#bd2088"));
        leftMenu.getChildren().add(title);
        borderPane.setLeft(leftMenu);

        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Change Password"),
                new Hyperlink("Ask Hüsna")};


        for (int i=0; i<2; i++) {
            VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            options[i].setFont(Font.font("Arial", FontWeight.BOLD, 11));
            //options[i].setFill(Color.web("#bd2088"));
            leftMenu.getChildren().add(options[i]);
        }
        options[0].setOnAction(e-> PasswordChangeWindow.display("CHANGE PASSWORD", "change password"));
        options[1].setOnAction(e-> ContactWindowController.display());


        // CENTER MENU
        GridPane centerMenu = new GridPane();
        centerMenu.setHgap(10);
        centerMenu.setVgap(10);
        centerMenu.setPadding(new Insets(0, 10, 0, 10));

        ColumnConstraints col1 = new ColumnConstraints(150);
        ColumnConstraints col2 = new ColumnConstraints(150);
        ColumnConstraints col3 = new ColumnConstraints(150);
        centerMenu.getColumnConstraints().addAll(col1, col2, col3);


        // Category in column 2, row 1
        String todaysQuestion = getQuestion();
        if(todaysQuestion == null) System.out.println("hata");

        Text category = new Text(todaysQuestion);

        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        category.setFill(Color.web("#bd2088"));
        centerMenu.add(category, 1, 0);
        category.setWrappingWidth(300);
        centerMenu.setVgap(10);
        centerMenu.setHgap(10);
        category.getStyleClass().add("category");

        Button userBut1 = new Button("Ayşeleybüs");
        centerMenu.add(userBut1, 1, 2);
        userBut1.setPrefSize(130, 20);
        userBut1.setOnAction(e -> {
            String buttonText = userBut1.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });

        Button userBut2 = new Button("Berkeleybüs");
        centerMenu.add(userBut2, 2, 2);
        userBut2.setPrefSize(130, 20);
        userBut2.setOnAction(e -> {
            String buttonText = userBut2.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
            });

        Button userBut3 = new Button("Hüsneleybüs");
        centerMenu.add(userBut3, 1, 3);
        userBut3.setPrefSize(130, 20);
        userBut3.setOnAction(e -> {
            String buttonText = userBut3.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });

        Button userBut4 = new Button("Seleneybüs");
        centerMenu.add(userBut4, 2, 3);
        userBut4.setPrefSize(130, 20);
        userBut4.setOnAction(e -> {
            String buttonText = userBut4.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });

        Button userBut5 = new Button("Şevvaleybüs");
        centerMenu.add(userBut5, 1, 4);
        userBut5.setPrefSize(130, 20);
        userBut5.setOnAction(e -> {
            String buttonText = userBut5.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });
        Button userBut6 = new Button("Yağmureleybüs");
        centerMenu.add(userBut6, 2, 4);
        userBut6.setPrefSize(130, 20);
        userBut6.setOnAction(e -> {
            String buttonText = userBut6.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });

        Button userBut7 = new Button("Zümreleybüs");
        centerMenu.add(userBut7, 1, 5);
        userBut7.setPrefSize(130, 20);
        userBut7.setOnAction(e -> {
            String buttonText = userBut7.getText();
            if (ResultsController.poll(buttonText))
                ResultsController.display();
        });



        borderPane.setCenter(centerMenu);


        TilePane rightMenu = new TilePane();
        rightMenu.setPadding(new Insets(5, 0, 5, 0));
        rightMenu.setVgap(4);
        rightMenu.setHgap(4);
        rightMenu.setPrefColumns(2);
        rightMenu.setStyle("-fx-background-color: #e7dae8;");

        String path = "/chart_1.png";
        InputStream stream = Main.class.getResourceAsStream(path); // data
        Image image = new Image(stream); // create an object
        ImageView chart_1 = new ImageView(image); // show the object

        Button displayButton = new Button("Poll Results");
        displayButton.setPrefSize(130, 20);
        displayButton.setOnAction(e -> {
            ResultsController.display();});

        chart_1.setFitHeight(190);
        chart_1.setFitWidth(190);
        rightMenu.getChildren().addAll(chart_1, displayButton);

        borderPane.setRight(rightMenu);
        rightMenu.setPrefWidth(200);
        borderPane.setLeft(leftMenu);

        Scene scene = new Scene(borderPane, 980, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/generalCss.css")).toExternalForm());
        window.setScene(scene);
        window.show();

    }

    private static String getQuestion() {
        String url = ConfigLoader.get("db.url");
        String sql = "SELECT question FROM questions  WHERE date_today = ?";
        String question = null;
        try (Connection conn = DriverManager.getConnection(url, ConfigLoader.get("db.username"), ConfigLoader.get("db.password"));
             PreparedStatement statement = conn.prepareStatement(sql)) {

            LocalDate today = LocalDate.now();
            String strToday = today.toString();

            statement.setString(1, strToday);
            try(ResultSet rs = statement.executeQuery()){

            if (rs.next()) {
                question = rs.getString("question");
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }



}
