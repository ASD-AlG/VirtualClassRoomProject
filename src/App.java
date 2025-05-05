import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {
public static void UICHANGEMETHOD()
{
try{
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(UnsupportedLookAndFeelException e)
        {
            e.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    
}
        public static void main(String[] args) throws Exception {
        new LoginForm();
        
    }
}
