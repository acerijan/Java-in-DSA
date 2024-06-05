import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class Patient implements Serializable {
    int patientId;
    String patientName;
    int age;
    int priority;
    Patient next;

    Patient(int a, String b, int c, boolean d) {
        patientId = a;
        patientName = b;
        age = c;
        priority = (age < 60) ? 1 : 2;
        if (d) {
            priority = 3;
        }
    }
}

class QueueLogic {
    Patient start = null, end = null, temp, visit, i, j;
    String calledPatient;

    public void swap(Patient i, Patient j) {
        int t1;
        String t2;
        t1 = i.patientId;
        i.patientId = j.patientId;
        j.patientId = t1;
        t2 = i.patientName;
        i.patientName = j.patientName;
        j.patientName = t2;
        t1 = i.age;
        i.age = j.age;
        j.age = t1;
        t1 = i.priority;
        i.priority = j.priority;
        j.priority = t1;
    }

    public void enqueue(Patient visit) {
        visit.next = null;
        if (start == null) {
            start = visit;
            end = visit;
        } else {
            end.next = visit;
            end = visit;
        }
    }

    public void dequeue() {
        if (start == null) {
            calledPatient = "empty queue";
        } else {
            calledPatient = "patient no " + start.patientId + " please come forward";
            if (start.next == null) {
                start = null;
                end = null;
            } else {
                start = start.next;
            }
        }
    }

    public void sort() {
        for (i = start; i.next != null; i = i.next) {
            for (j = start; j.next != null; j = j.next) {
                if (j.priority < j.next.priority) {
                    swap(j, j.next);
                }
            }
        }
    }

    public void display() {
        temp = start;
        if (start == null) {
            System.out.println("empty queue");
        } else {
            System.out.println("the orders of patients are");
            while (temp != null) {
                System.out.println(temp.patientId);
                System.out.println(temp.patientName);
                System.out.println(temp.age + "\n\n");
                temp = temp.next;
            }
        }
    }

    public void search(int p,JLabel lbl) {
        int pos = 1;
        boolean hasId=false;
        temp = start;
        while (temp != null) {
            if (p == temp.patientId) {hasId=true;
                lbl.setText("id:"+temp.patientId+"name:"+temp.patientName+"age:"+temp.age+"position:"+pos);
            }
            pos++;
            temp = temp.next;
        }
        if(!hasId)
        {lbl.setText("no patient");}
    }
}

class FileHandler {
    public void writer(Patient s) {
        try {
            FileOutputStream fos = new FileOutputStream("patients.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            while (s != null) {
                Patient temp = s;
                oos.writeObject(temp);
                s = s.next;
            }
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reader(QueueLogic q) {
        try {
            FileInputStream fis = new FileInputStream("patients.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (fis.available() > 0) {
                Patient temp = (Patient) ois.readObject();
                temp.next = null;
                q.enqueue(temp);
            }
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class HospitalQueueManagementSystem {
    static int count;
    QueueLogic q;
    Patient p;
    FileHandler f;
    private JFrame frame;
    private JPanel panel, loginPanel;
    private JButton staffButton, callButton, viewListButton, loginButton;
    private JLabel callLabel, loginLabel;
    private JButton patientButton;
    private JButton admissionButton, admit;
    private JButton searchButton;
    private JButton backButton;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField name, age;
    private JRadioButton r1, r2;
    private ButtonGroup grp;

    public HospitalQueueManagementSystem() {
        q = new QueueLogic();
        f = new FileHandler();
        idTracker();
        f.reader(q);
        frame = new JFrame("Hospital Queue Management System");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                f.writer(q.start);
                idSetter();
                System.exit(0);
            }
        });

        // Create the main panel with background image
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("chill.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create login panel
        loginPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("chill.jpg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        loginLabel = new JLabel("Staff Login");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
        loginLabel.setForeground(Color.WHITE);
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateStaff();
            }
        });

        // Adding buttons to the main panel
        staffButton = new JButton("Click if staff");
        patientButton = new JButton("Click if patient");
        backButton = new JButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(staffButton, gbc);
        gbc.gridy = 1;
        panel.add(patientButton, gbc);
        staffButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });
        patientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPatientInterface();
            }
        });

        frame.add(panel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void showLoginPanel() {
        panel.removeAll();
        panel.add(loginPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void authenticateStaff() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.equals("chill") && password.equals("chill")) {
            setStaffInterface();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void idTracker() {
        try (DataInputStream d = new DataInputStream(new FileInputStream("tracker.txt"))) {
            count = d.readInt();
            d.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void idSetter() {
        try (DataOutputStream d = new DataOutputStream(new FileOutputStream("tracker.txt"))) {
            d.writeInt(count);
            d.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPatientInterface() {
        panel.removeAll();
        admissionButton = new JButton("Admission");
        searchButton = new JButton("Search via ID");
        viewListButton = new JButton("View entire list");
        callLabel = new JLabel("Result of action");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(admissionButton, gbc);
        gbc.gridy = 1;
        panel.add(searchButton, gbc);
        gbc.gridy = 2;
        panel.add(viewListButton, gbc);
        gbc.gridy = 3;
        panel.add(backButton, gbc);
        gbc.gridy = 4;
        panel.add(callLabel, gbc);

        admissionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                admission();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
        viewListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewList();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDefaultInterface();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public void setDefaultInterface() {
        panel.removeAll();
        panel.add(staffButton);
        panel.add(patientButton);
        frame.revalidate();
        frame.repaint();
    }

    public void setStaffInterface() {
        panel.removeAll();
        callButton = new JButton("Call next patient");
        viewListButton = new JButton("View entire list");
        callLabel = new JLabel("Result of action");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(callButton, gbc);
        gbc.gridy = 1;
        panel.add(viewListButton, gbc);
        gbc.gridy = 2;
        panel.add(backButton, gbc);
        gbc.gridy = 3;
        panel.add(callLabel, gbc);

        callButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                q.dequeue();
                callLabel.setText(q.calledPatient);
            }
        });
        viewListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewList();
            }
        });
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDefaultInterface();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public void admission() {
        JFrame admissionFrame = new JFrame("Patient Admission");
        admissionFrame.setSize(400, 300);
        admissionFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //JLabel idLabel = new JLabel("ID:");
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel emergencyLabel = new JLabel("Emergency:");
        //id = new JTextField(10);
        name = new JTextField(10);
        age = new JTextField(10);
        r1 = new JRadioButton("Yes");
        r2 = new JRadioButton("No");
        grp = new ButtonGroup();
        grp.add(r1);
        grp.add(r2);
        admit = new JButton("Admit");

        //gbc.gridx = 0;
        //gbc.gridy = 0;
        //admissionFrame.add(idLabel, gbc);
        //gbc.gridx = 1;
        //admissionFrame.add(id, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        admissionFrame.add(nameLabel, gbc);
        gbc.gridx = 1;
        admissionFrame.add(name, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        admissionFrame.add(ageLabel, gbc);
        gbc.gridx = 1;
        admissionFrame.add(age, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        admissionFrame.add(emergencyLabel, gbc);
        gbc.gridx = 1;
        admissionFrame.add(r1, gbc);
        gbc.gridx = 2;
        admissionFrame.add(r2, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        admissionFrame.add(admit, gbc);

        admit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                admitPatient();
            }
        });

        admissionFrame.setVisible(true);
    }

    public void admitPatient() {
        count++;
        String nameText = name.getText();
        int ageNum = Integer.parseInt(age.getText());
        boolean emergency = r1.isSelected();

        Patient newPatient = new Patient(count, nameText, ageNum, emergency);
        q.enqueue(newPatient);
        q.sort();

        //id.setText("");
        name.setText("");
        age.setText("");
        grp.clearSelection();
    }

    public void search() {
        JFrame searchFrame = new JFrame("Search Patient");
        searchFrame.setSize(400, 200);
        searchFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField(10);
        JButton searchButton = new JButton("Search");
        JLabel resultOfSearch=new JLabel("your result");
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchFrame.add(idLabel, gbc);
        gbc.gridx = 1;
        searchFrame.add(idField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        searchFrame.add(searchButton, gbc);
        gbc.gridx= 1;
        gbc.gridy= 2;
        searchFrame.add(resultOfSearch,gbc);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int idNum = Integer.parseInt(idField.getText());
                q.search(idNum,resultOfSearch);
            }
        });

        searchFrame.setVisible(true);
    }

    public void viewList() {
        JFrame listFrame = new JFrame("Patient List");
        listFrame.setSize(400, 400);
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        listFrame.add(scrollPane);

        StringBuilder patientList = new StringBuilder();
        Patient temp = q.start;
        while (temp != null) {
            patientList.append("ID: ").append(temp.patientId)
                    .append(", Name: ").append(temp.patientName)
                    .append(", Age: ").append(temp.age)
                    .append(", Priority: ").append(temp.priority)
                    .append("\n");
            temp = temp.next;
        }

        textArea.setText(patientList.toString());
        listFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new HospitalQueueManagementSystem();
    }
}