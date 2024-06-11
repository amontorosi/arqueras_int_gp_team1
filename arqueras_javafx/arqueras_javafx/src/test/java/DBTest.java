import com.gp.arqueras_javafx.HandlerDB;
import org.junit.jupiter.api.Test;

public class DBTest {
    @Test
    public void testCreateDB(){
        try {
            HandlerDB handler = new HandlerDB("jdbc:mysql://localhost:3306", "root", "toor");
            handler.runDBCreationScript();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    public void testInsertDB() {
        try {
            HandlerDB handler = new HandlerDB("jdbc:mysql://localhost:3306/arqueras_db", "arqueras", "arqueras");
            handler.insertScore("test 1", "easy", 50);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
