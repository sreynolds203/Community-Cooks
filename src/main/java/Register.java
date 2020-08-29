//imported java libraries
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;

//beginning of register class
public class Register extends JFrame implements ActionListener  {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // variables
    static JLabel firstNameLabel;
    static JLabel lastNameLabel;
    static JLabel emailLabel;
    static JLabel usernameLabel;
    static JLabel passwordLabel;
    static JLabel confirmPassLabel;
    static JLabel passwordLength;
    static JTextField firstName;
    static JTextField lastName;
    static JTextField email;
    static JTextField username;
    static JPasswordField password;
    static JPasswordField confPass;
    static JButton registerButton;
    static JButton cancelButton;
    static JCheckBox showPass;
    //constructor to build frame
    Register(){
        //frame components
        new JFrame();
        setTitle("Registration");
        setBounds(40,40,380,450);
        getContentPane().setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        //labels
        firstNameLabel = new JLabel("First Name");
        firstNameLabel.setBounds(20,20,140,70);
        getContentPane().add(firstNameLabel);
        lastNameLabel = new JLabel("Last Name");
        lastNameLabel.setBounds(20,70,100,70);
        getContentPane().add(lastNameLabel);
        emailLabel = new JLabel("Email");
        emailLabel.setBounds(20,120,100,70);
        getContentPane().add(emailLabel);
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(20,170,100,70);
        getContentPane().add(usernameLabel);
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20,220,140,70);
        getContentPane().add(passwordLabel);
        passwordLength = new JLabel("(Password must be at least 8 characters)");
        passwordLength.setBounds(20,264,195,23);
        passwordLength.setFont(new Font("Arial", Font.PLAIN, 9));
        getContentPane().add(passwordLength);
        confirmPassLabel = new JLabel("Confirm Password");
        confirmPassLabel.setBounds(20,270,150,70);
        getContentPane().add(confirmPassLabel);
        //text fields and password fields
        firstName = new JTextField();
        firstName.setBounds(180,43,165,23);
        getContentPane().add(firstName);
        lastName = new JTextField();
        lastName.setBounds(180,93,165,23);
        getContentPane().add(lastName);
        email = new JTextField();
        email.setBounds(180,143,165,23);
        getContentPane().add(email);
        username = new JTextField();
        username.setBounds(180,193,165,23);
        getContentPane().add(username);
        password = new JPasswordField();
        password.setBounds(180,243,165,23);
        getContentPane().add(password);
        confPass = new JPasswordField();
        confPass.setBounds(180,293,165,23);
        getContentPane().add(confPass);
        //buttons
        registerButton = new JButton("Register");
        registerButton.setBounds(65,350,100,35);
        getContentPane().add(registerButton);
        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(205,350,100,35);
        getContentPane().add(cancelButton);
        //checkbox
        showPass = new JCheckBox("Show Password");
        showPass.setBounds(230,265,165,23);
        getContentPane().add(showPass);
        actionEvent();
    }
    //validates the email format that the user enters
    public static boolean isValid(String email) {
        //acceptable String format
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;//failed pattern
        }
        return pat.matcher(email).matches();//correct pattern
    }
    //action event listener for buttons
    public void actionEvent(){
        registerButton.addActionListener(this);
        cancelButton.addActionListener(this);
        showPass.addActionListener(this);
    }
    //performs action when button is clicked
    @Override
    public void actionPerformed(ActionEvent e){
        //register button action
        if(e.getSource() == registerButton){
            try{
                String inputUsername = username.getText();
                char[] inputPass = password.getPassword();
                char[] inputConfPass = confPass.getPassword();
                String convertedPass = new String(inputPass);
                String convertedConfPass = new String(inputConfPass);
                Connection conn = SqlConnection.getConnection();
                Statement statement = null;
                String sql = "insert into users values(?,?,?,?,?)";
                //inserts values into table
                statement = conn.createStatement();
                // statement.executeQuery(sql);
                //checks the first and last name length. Cannot be empty
                if(firstName.getText().length() < 1 || lastName.getText().length() < 1){
                    JOptionPane.showMessageDialog(null, "First and last name " +
                            "cannot be blank.");
                }
                else {
                    //writes the first and last name to database
                    ((PreparedStatement) statement).setString(1, firstName.getText());
                    ((PreparedStatement) statement).setString(2, lastName.getText());
                }
                //checks the email format
                if(isValid(email.getText())){
                    //writes email to database
                    ((PreparedStatement) statement).setString(3, email.getText());
                }
                else {
                    JOptionPane.showMessageDialog(null, "Enter valid email.");
                }
                Statement statement2 = null;
                ResultSet rs = null;
                String sql2 = "SELECT * FROM users WHERE username = '" + inputUsername + "'";
                statement2 = conn.createStatement();
                rs = statement2.executeQuery(sql2);
                if (rs.next()){
                    JOptionPane.showMessageDialog(null, "username already in use.");
                }
                else {
                    //writes username to database
                    ((PreparedStatement) statement).setString(4, username.getText());
                }
                //checks password to ensure confirmation matches
                //encrypts password for storing in database
                if(convertedPass.equals(convertedConfPass) && convertedPass.length() > 7){
                    //hashes password
                    String pass = BCrypt.hashpw(convertedPass,
                            BCrypt.gensalt());
                    //writes hashed password to database
                    ((PreparedStatement) statement).setString(5, pass);
                    //attempts to execute the prepared statement
                    statement.executeQuery(sql);
                    JOptionPane.showMessageDialog(null, "Registered Successfully");
                    dispose();
                    new SignIn();
                }
                else if (convertedPass.length() <= 7){
                    JOptionPane.showMessageDialog(null, "Password not long " +
                            "enough");
                }
                else{
                    JOptionPane.showMessageDialog(null, "Password did not match");
                }
            } catch(SQLException f){
                //prevents SQL execution if any fields are not correct
                f.printStackTrace();
            }
        }
        //closes window and returns to log in window
        if (e.getSource() == cancelButton){
            dispose();
            new SignIn();
        }
        //shows the password in password text field
        if(e.getSource() == showPass){
            if(showPass.isSelected()){
                password.setEchoChar((char)0);
            }
            else {
                //hides password when unchecked
                password.setEchoChar('*');
            }
        }
    }
}