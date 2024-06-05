
import java.io.DataOutputStream;
import java.io.FileOutputStream;

public class Temp {
    public static void main(String[] args)
    {
        try{DataOutputStream d=new DataOutputStream(new FileOutputStream("tracker.txt"));
        d.writeInt(0);
        d.close();
    }catch(Exception e){

    }
}
}
