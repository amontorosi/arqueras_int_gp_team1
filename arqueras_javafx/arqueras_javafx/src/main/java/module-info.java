module com.gp.arqueras_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.gp.arqueras_javafx to javafx.fxml;
    exports com.gp.arqueras_javafx;
}