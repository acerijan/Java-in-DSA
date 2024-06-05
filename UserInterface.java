import java.util.Scanner;
import java.io.*;

class Patient implements Serializable{
    int patientId;
    String patientName;
    int age;
    int priority;
    Patient next;
    Patient(int a,String b,int c){
        Scanner s=new Scanner(System.in);
        patientId=a;
        patientName=b;
        age=c;
        priority=(age<60)?1:2;
        System.out.println("is your condition critical[y/n]");
        if(s.next().charAt(0)=='y')
            {priority=3;}
    }

}
class QueueLogic{
    Patient start=null,end=null,temp,visit,i,j;
    public void swap(Patient i,Patient j)
    {   int t1;String t2;
        t1=i.patientId;
        i.patientId=j.patientId;
        j.patientId=t1;
        t2=i.patientName;
        i.patientName=j.patientName;
        j.patientName=t2;
        t1=i.age;
        i.age=j.age;
        j.age=t1;
        t1=i.priority;
        i.priority=j.priority;
        j.priority=t1;
        
    }
    public void enqueue(Patient visit){
        visit.next=null;
        if(start==null)
        {
            start=visit;
            end=visit;
        }
        else{
            end.next=visit;
            end=visit;
        }
    }
    public void dequeue(){
        if(start==null)
        {
            System.out.println("empty queue");
        }else
        {
            System.out.println("the queue has been updated");
            System.out.println("patient no "+ start.patientId+" please come forward");
            if(start.next==null)
            {
                start=null;end=null;
            }else{
                start=start.next;
            }
        }
    }
    public void sort()
    {   
        for(i=start;i.next!=null;i=i.next)
        {
            for(j=start;j.next!=null;j=j.next){
                if(j.priority<j.next.priority)
                {
                    swap(j,j.next);
                }
            }
        }
    }
    public void display()
    {
        temp=start;
        if(start==null)
        {
            System.out.println("enpty queue");
        }else{
        System.out.println("the orders of patient are");
        while (temp!=null) {
            System.out.println(temp.patientId);
            System.out.println(temp.patientName);
            System.out.println(temp.age+"\n\n");
            temp=temp.next;
        }
    }
    }
    public void search(int p)
    {int pos=1;
        temp=start;
        if(start==null)
        {
            System.out.println("enpty queue");
        }
        System.out.println("your info is");
        while (temp!=null) {
            if(p==temp.patientId){
            System.out.println(temp.patientId);
            System.out.println(temp.patientName);
            System.out.println(temp.age);
            System.out.println("position is "+pos+"\n");
            }
            pos++;
            temp=temp.next;
        }
    }
}
class FileHandler{
    public void writer(Patient s){
        try{
            FileOutputStream fos=new FileOutputStream("patients.txt");
            ObjectOutputStream oos=new ObjectOutputStream(fos);
            while(s!=null){
            Patient temp=s;
            oos.writeObject(temp);
            s=s.next;
            }
            oos.close();
            fos.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void reader(QueueLogic q){
        try{   FileInputStream fis=new FileInputStream("patients.txt");
            ObjectInputStream ois=new ObjectInputStream(fis);
            while(fis.available()>0){
            Patient temp=(Patient)ois.readObject();
            temp.next=null;
            q.enqueue(temp);
            }
            ois.close();
            fis.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
public class UserInterface{
    static int count=0;
    public static void idTracker()
    {
        try(DataInputStream d=new DataInputStream(new FileInputStream("tracker.txt"));){
                count=d.readInt();
                d.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void idSetter()
    {
        try(DataOutputStream d=new DataOutputStream(new FileOutputStream("tracker.txt"));){
            d.writeInt(count);
            d.close();
    }catch(Exception e){
        e.printStackTrace();
    }
    }
    public static void staffLogic(Scanner s,QueueLogic q)
    {   int n1;
        System.out.println("enter your choice\n1.call patient\n2.view queue\n3.exit");
                n1=s.nextInt();
                switch(n1)
                {
                    case 1:
                    q.dequeue();
                    break;
                    case 2:
                    q.display();
                    break;
                    case 3:
                    idSetter();
                    System.exit(0);
                    break;
                    default:
                    System.out.println("wrong choice");
                }
    }
    public static void patientLogic(Scanner s,QueueLogic q,FileHandler f)
    {   int n2;
        System.out.println("enter your choice \n1.register\n2.search\n3.view queue\n4.exit");
                n2=s.nextInt();
                switch(n2)
                {
                    case 1:
                    System.out.println("enter name,age");
                    String name=s.next();
                    int age=s.nextInt();
                    count++;
                    Patient p=new Patient(count, name, age);
                    System.out.println("your id is :"+count);
                    q.enqueue(p);
                    q.sort();
                    break;
                    case 2:
                    System.out.println("enter your id");
                    int id=s.nextInt();
                    q.search(id);
                    break;
                    case 3:
                    q.display();
                    break;
                    case 4:
                    idSetter();
                    f.writer(q.start);
                    System.exit(0);
                    break;
                    default:
                    System.out.println("wrong choice");
                }
    }
    public static void main(String[] args)
    {   int staffId[]={00001,00002};
        int staffPassword[]={12345,67890};
        int inpid;
        int n,inppwd;
        idTracker();
        Scanner s=new Scanner(System.in);
        QueueLogic q=new QueueLogic();
        FileHandler f=new FileHandler();
        f.reader(q);
        while(true){
            System.out.println("are you: \n1.staff\n 2.patient\n3.exit");
            n=s.nextInt();
        switch(n)
        {   
            case 1:
            boolean a=false;
                System.out.println("enter id and password");
                inpid=s.nextInt();
                inppwd=s.nextInt();
                for(int i=0;i<2;i++)
                {
                    if(inpid==staffId[i] && inppwd==staffPassword[i])
                    {a=true;}
                }
                if(a)
                {staffLogic(s, q);}
                else{
                    System.out.println("wrong id,password refer to administrator");
                }
                break;
            case 2:
                patientLogic(s,q,f);
                break;
            case 3:
                idSetter();
                f.writer(q.start);
                System.exit(0);
            default:
                System.out.println("worng choice");
        }
    }
    }
}





