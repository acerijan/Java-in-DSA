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

    public void search(int p, JLabel lbl) {
        int pos = 1;
        boolean hasId = false;
        temp = start;
        while (temp != null) {
            if (p == temp.patientId) {
                hasId = true;
                String message = "ID: " + temp.patientId + "\nName: " + temp.patientName + "\nAge: " + temp.age + "\nPosition: " + pos;
                JOptionPane.showMessageDialog(null, message, "Patient Details", JOptionPane.INFORMATION_MESSAGE);
                break; // Assuming we want to show details for only the first match
            }
            pos++;
            temp = temp.next;
        }
        if (!hasId) {
            JOptionPane.showMessageDialog(null, "No patient found", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // Set the window icon
        ImageIcon windowIcon = new ImageIcon("windowIcon.png");
        frame.setIconImage(windowIcon.getImage());

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
                ImageIcon icon = new ImageIcon("chill.jpeg");
                g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 600); // Set the preferred size
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
                ImageIcon icon = new ImageIcon("chill1.jpeg");
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
        gbc.gridx = 1;
        panel.add(searchButton, gbc);
        gbc.gridx = 2;
        panel.add(viewListButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(callLabel, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        admissionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setAdmission();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPatient();
            }
        });

        viewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPatients();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                panel.add(staffButton);
                panel.add(patientButton);
                frame.revalidate();
                frame.repaint();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public void setAdmission() {
        panel.removeAll();
        JLabel nameLabel = new JLabel("Name:");
        JLabel ageLabel = new JLabel("Age:");
        JLabel emergencyLabel = new JLabel("Emergency:");

        name = new JTextField(20);
        age = new JTextField(20);
        r1 = new JRadioButton("Yes");
        r2 = new JRadioButton("No");
        grp = new ButtonGroup();
        grp.add(r1);
        grp.add(r2);

        admit = new JButton("Admit");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(name, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(ageLabel, gbc);
        gbc.gridx = 1;
        panel.add(age, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(emergencyLabel, gbc);
        gbc.gridx = 1;
        panel.add(r1, gbc);
        gbc.gridx = 2;
        panel.add(r2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        panel.add(admit, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        admit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                admitPatient();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPatientInterface();
            }
        });

        frame.revalidate();
        frame.repaint();
    }


    public void admitPatient() {
        String pname = name.getText();
        int page = Integer.parseInt(age.getText());
        boolean emergency = r1.isSelected();
        count++;
        p = new Patient(count, pname, page, emergency);
        JOptionPane.showMessageDialog(frame, "Patient Admitted: \nID: " + p.patientId + "\nName: " + p.patientName + "\nAge: " + p.age + "\nPriority: " + p.priority);
        q.enqueue(p);
        q.sort();
        setPatientInterface();  // Go back to the patient interface after admitting the patient
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
        gbc.gridx = 1;
        panel.add(viewListButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(callLabel, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                q.dequeue();
                callLabel.setText(q.calledPatient);
            }
        });

        viewListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPatients();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.removeAll();
                panel.add(staffButton);
                panel.add(patientButton);
                frame.revalidate();
                frame.repaint();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public void searchPatient() {
        panel.removeAll();
        JLabel searchLabel = new JLabel("Enter ID:");
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        callLabel = new JLabel("Result of action");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(searchLabel, gbc);
        gbc.gridx = 1;
        panel.add(searchField, gbc);
        gbc.gridx = 2;
        panel.add(searchButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        panel.add(callLabel, gbc);
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(searchField.getText());
                q.search(id, callLabel);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPatientInterface();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public void displayPatients() {
        panel.removeAll();
        JTextArea textArea = new JTextArea(20, 50);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        Patient temp = q.start;

        while (temp != null) {
            textArea.append("ID: " + temp.patientId + ", Name: " + temp.patientName + ", Age: " + temp.age + ", Priority: " + temp.priority + "\n");
            temp = temp.next;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(scrollPane, gbc);
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(backButton, gbc);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setPatientInterface();
            }
        });

        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        new HospitalQueueManagementSystem();
    }
}