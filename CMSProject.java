import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// ================== Main ==================
public class CMSProject {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    } catch (Exception ignored) {
    }
    SwingUtilities.invokeLater(RoleSelectionUI::new);
  }
}

// ============== Models & Database ==============
class Student {
  private final String name;

  public Student(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

class Professor {
  private final String name;

  public Professor(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

class Course {
  private final String name;
  private final String professorName;
  private final Set<Student> enrolledStudents = new HashSet<>();

  public Course(String name, String professorName) {
    this.name = name;
    this.professorName = professorName;
  }

  public String getName() {
    return name;
  }

  public String getProfessorName() {
    return professorName;
  }

  public Set<Student> getEnrolledStudents() {
    return enrolledStudents;
  }

  public void enrollStudent(Student s) {
    enrolledStudents.add(s);
  }

  public void removeStudent(Student s) {
    enrolledStudents.remove(s);
  }
}

class InMemoryDatabase {
  public static final List<Student> students = new ArrayList<>();
  public static final List<Professor> professors = new ArrayList<>();
  public static final List<Course> courses = new ArrayList<>();
}

// ============== Services ==============
class UserService {
  public void addStudent(String name) {
    if (getStudentByName(name) == null)
      InMemoryDatabase.students.add(new Student(name));
  }

  public void addProfessor(String name) {
    if (getProfessorByName(name) == null)
      InMemoryDatabase.professors.add(new Professor(name));
  }

  public Student getStudentByName(String name) {
    return InMemoryDatabase.students.stream()
        .filter(s -> s.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }

  public Professor getProfessorByName(String name) {
    return InMemoryDatabase.professors.stream()
        .filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }

  public void deleteStudent(String name) {
    InMemoryDatabase.students.removeIf(s -> s.getName().equalsIgnoreCase(name));
  }

  public void deleteProfessor(String name) {
    InMemoryDatabase.professors.removeIf(p -> p.getName().equalsIgnoreCase(name));
  }
}

class CourseService {
  public void addCourse(String name, String professor) {
    InMemoryDatabase.courses.add(new Course(name, professor));
  }

  public List<Course> getCoursesByProfessor(String name) {
    return InMemoryDatabase.courses.stream()
        .filter(c -> c.getProfessorName().equalsIgnoreCase(name)).toList();
  }

  public List<Course> getCoursesByStudent(Student s) {
    return InMemoryDatabase.courses.stream()
        .filter(c -> c.getEnrolledStudents().contains(s)).toList();
  }

  public void enrollStudent(String courseName, Student s) {
    for (Course c : InMemoryDatabase.courses) {
      if (c.getName().equalsIgnoreCase(courseName)) {
        c.enrollStudent(s);
      }
    }
  }

  public void unenrollStudent(String courseName, Student s) {
    for (Course c : InMemoryDatabase.courses) {
      if (c.getName().equalsIgnoreCase(courseName)) {
        c.removeStudent(s);
      }
    }
  }
}

// ============== UI Components ==============

class RoleSelectionUI extends JFrame {
  public RoleSelectionUI() {
    setTitle("Select Role");
    setSize(300, 200);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new GridLayout(3, 1, 10, 10));
    setResizable(false);

    JButton adminBtn = styledButton("Admin", new Color(0x2196F3));
    JButton profBtn = styledButton("Professor", new Color(0x4CAF50));
    JButton stuBtn = styledButton("Student", new Color(0xFF5722));

    adminBtn.addActionListener(e -> {
      String user = JOptionPane.showInputDialog(this, "Username:");
      String pass = JOptionPane.showInputDialog(this, "Password:");
      if ("admin".equals(user) && "admin123".equals(pass)) {
        dispose();
        new AdminUI();
      } else {
        JOptionPane.showMessageDialog(this, "Invalid Credentials");
      }
    });

    profBtn.addActionListener(e -> {
      String name = (String) JOptionPane.showInputDialog(this, "Select Professor:",
          "Login", JOptionPane.PLAIN_MESSAGE, null,
          InMemoryDatabase.professors.stream().map(Professor::getName).toArray(), null);
      if (name != null) {
        dispose();
        new ProfessorUI(name);
      }
    });

    stuBtn.addActionListener(e -> {
      String name = (String) JOptionPane.showInputDialog(this, "Select Student:",
          "Login", JOptionPane.PLAIN_MESSAGE, null,
          InMemoryDatabase.students.stream().map(Student::getName).toArray(), null);
      if (name != null) {
        dispose();
        new StudentUI(name);
      }
    });

    add(adminBtn);
    add(profBtn);
    add(stuBtn);
    setVisible(true);
  }

  private JButton styledButton(String text, Color color) {
    JButton btn = new JButton(text);
    btn.setBackground(color);
    btn.setForeground(Color.WHITE);
    btn.setFont(new Font("Arial", Font.BOLD, 14));
    return btn;
  }
}

class AdminUI extends JFrame {
  UserService us = new UserService();
  CourseService cs = new CourseService();
  JTextArea area = new JTextArea();

  public AdminUI() {
    setTitle("Admin Dashboard");
    setSize(600, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    JPanel top = new JPanel(new GridLayout(2, 3, 10, 10));
    top.setBorder(new EmptyBorder(10, 10, 10, 10));

    JButton addStu = new JButton("Add Student");
    JButton addProf = new JButton("Add Professor");
    JButton addCourse = new JButton("Add Course");
    JButton view = new JButton("View All");
    JButton del = new JButton("Delete User");
    JButton back = new JButton("Logout");

    addStu.setBackground(new Color(0x81C784));
    addProf.setBackground(new Color(0x4DB6AC));
    addCourse.setBackground(new Color(0x64B5F6));
    view.setBackground(new Color(0xFFD54F));
    del.setBackground(new Color(0xE57373));
    back.setBackground(Color.GRAY);

    top.add(addStu);
    top.add(addProf);
    top.add(addCourse);
    top.add(view);
    top.add(del);
    top.add(back);

    area.setEditable(false);
    JScrollPane scroll = new JScrollPane(area);

    add(top, BorderLayout.NORTH);
    add(scroll, BorderLayout.CENTER);

    addStu.addActionListener(e -> {
      String name = JOptionPane.showInputDialog(this, "Enter Student Name:");
      if (name != null && !name.isBlank())
        us.addStudent(name);
    });

    addProf.addActionListener(e -> {
      String name = JOptionPane.showInputDialog(this, "Enter Professor Name:");
      if (name != null && !name.isBlank())
        us.addProfessor(name);
    });

    addCourse.addActionListener(e -> {
      String cname = JOptionPane.showInputDialog(this, "Course Name:");
      String pname = (String) JOptionPane.showInputDialog(this, "Select Professor:",
          "Assign Professor", JOptionPane.PLAIN_MESSAGE, null,
          InMemoryDatabase.professors.stream().map(Professor::getName).toArray(), null);
      if (cname != null && pname != null)
        cs.addCourse(cname, pname);
    });

    view.addActionListener(e -> {
      StringBuilder sb = new StringBuilder();
      sb.append("Students: ").append(InMemoryDatabase.students.stream().map(Student::getName).toList()).append("\n");
      sb.append("Professors: ").append(InMemoryDatabase.professors.stream().map(Professor::getName).toList())
          .append("\n");
      sb.append("Courses: ").append(InMemoryDatabase.courses.stream().map(Course::getName).toList()).append("\n");
      area.setText(sb.toString());
    });

    del.addActionListener(e -> {
      String name = JOptionPane.showInputDialog(this, "Name to delete:");
      us.deleteStudent(name);
      us.deleteProfessor(name);
    });

    back.addActionListener(e -> {
      dispose();
      new RoleSelectionUI();
    });

    setVisible(true);
  }
}

class ProfessorUI extends JFrame {
  public ProfessorUI(String pname) {
    setTitle("Professor Dashboard");
    setSize(500, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    JTextArea area = new JTextArea();
    area.setEditable(false);

    JButton view = new JButton("View My Courses");
    JButton logout = new JButton("Logout");

    view.setBackground(Color.ORANGE);
    logout.setBackground(Color.GRAY);

    view.addActionListener(e -> {
      StringBuilder sb = new StringBuilder();
      for (Course c : new CourseService().getCoursesByProfessor(pname)) {
        sb.append("Course: ").append(c.getName()).append("\n");
        sb.append("Students: ").append(c.getEnrolledStudents().stream().map(Student::getName).toList()).append("\n\n");
      }
      area.setText(sb.toString());
    });

    logout.addActionListener(e -> {
      dispose();
      new RoleSelectionUI();
    });

    JPanel top = new JPanel();
    top.add(view);
    top.add(logout);
    add(top, BorderLayout.NORTH);
    add(new JScrollPane(area), BorderLayout.CENTER);
    setVisible(true);
  }
}

class StudentUI extends JFrame {
  CourseService cs = new CourseService();
  Student student;

  public StudentUI(String name) {
    student = new UserService().getStudentByName(name);
    setTitle("Student Dashboard");
    setSize(500, 400);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JTextArea area = new JTextArea();
    area.setEditable(false);

    JComboBox<String> courseDropdown = new JComboBox<>(
        InMemoryDatabase.courses.stream().map(Course::getName).toArray(String[]::new));

    JButton view = new JButton("My Courses");
    JButton enroll = new JButton("Enroll");
    JButton unenroll = new JButton("Unenroll");
    JButton back = new JButton("Logout");

    view.setBackground(new Color(0x64B5F6));
    enroll.setBackground(new Color(0x81C784));
    unenroll.setBackground(new Color(0xFF8A65));
    back.setBackground(Color.GRAY);

    view.addActionListener(e -> {
      List<Course> list = cs.getCoursesByStudent(student);
      area.setText("Enrolled:\n" + list.stream().map(Course::getName).toList());
    });

    enroll.addActionListener(e -> {
      String cname = (String) courseDropdown.getSelectedItem();
      if (cname != null)
        cs.enrollStudent(cname, student);
    });

    unenroll.addActionListener(e -> {
      String cname = (String) courseDropdown.getSelectedItem();
      if (cname != null)
        cs.unenrollStudent(cname, student);
    });

    back.addActionListener(e -> {
      dispose();
      new RoleSelectionUI();
    });

    JPanel top = new JPanel();
    top.add(courseDropdown);
    top.add(view);
    top.add(enroll);
    top.add(unenroll);
    top.add(back);
    add(top, BorderLayout.NORTH);
    add(new JScrollPane(area), BorderLayout.CENTER);
    setVisible(true);
  }
}
