import java.io.File;

public class TempPatient {
    public static void main(String[] args)
    {
        try{
            File f=new File("patients.txt");
            f.createNewFile();
        }catch(Exception e){}
    }
}
