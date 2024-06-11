module com.gp.arqueras_javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mybatis;


    opens com.gp.arqueras_javafx to javafx.fxml;
    exports com.gp.arqueras_javafx;
}