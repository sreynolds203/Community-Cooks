import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//beginning of sign in class
public class SignIn extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // variables
    static JLabel usernameLabel;
    static JLabel passwordLabel;
    static JTextField username;
    static JPasswordField password;
    static JButton login;
    static JButton register;
    static JCheckBox showPass;
    //constructor to build the frame
    SignIn(){
        //frame components
        setTitle("Log in");
        setBounds(40,40,380,250);
        getContentPane().setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        //labels
        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(20,20,140,70);
        getContentPane().add(usernameLabel);
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(20,70,100,70);
        getContentPane().add(passwordLabel);
        //fields
        username = new JTextField();
        username.setBounds(180,43,165,23);
        getContentPane().add(username);
        password = new JPasswordField();
        password.setBounds(180,93,165,23);
        getContentPane().add(password);
        //buttons
        login = new JButton("Log in");
        login.setBounds(55,150,100,35);
        getContentPane().add(login);
        register = new JButton("Register");
        register.setBounds(205,150,100,35);
        getContentPane().add(register);
        //checkbox
        showPass = new JCheckBox("Show Password");
        showPass.setBounds(180,115,165,23);
        getContentPane().add(showPass);
        actionEvent();
    }
    //assigns the action listener to buttons
    public void actionEvent(){
        login.addActionListener(this);
        register.addActionListener(this);
        showPass.addActionListener(this);
    }
    //actions for each button
    @Override
    public void actionPerformed(ActionEvent e){
        //verify users credentials and signs in
        if(e.getSource() == login){
            try {
                //gets username and password
                String inputUser = username.getText();
                char[] inputPass = password.getPassword();
                //converts password to String
                String convertedPass = new String(inputPass);
                Connection conn = SqlConnection.getConnection();
                Statement statement = null;
                ResultSet result = null;
                String sql = "SELECT username, user_pass FROM users " +
                "WHERE username = '" + inputUser +"';";
                //prepares SELECT statement
                statement = conn.createStatement();
                //statement result
                result = statement.executeQuery(sql);
                //statement variables
                String user = "";
                String pass = "";
                while(result.next()){
                    //assigns statement variables
                    user = result.getString("username");
                    pass = result.getString("user_pass");
                }
                //checks for correct credentials
                if(inputUser.equals(user) && BCrypt.checkpw(convertedPass,
                        pass)){
                    //closes sign in and opens new window
                    dispose();
                    new CommunityCooks();
                }
                else{
                    JOptionPane.showMessageDialog(null, "Incorrect " +
                            "credentials");
                }
            }
            catch(SQLException f){
                f.printStackTrace();
            }
        }
        //calls Register class when button pressed
        if(e.getSource() == register){
            dispose();
            new Register();
        }
        //shows characters in password field when checked
        if(e.getSource() == showPass){
            if(showPass.isSelected()){
                password.setEchoChar((char)0);
            }
            else {
                password.setEchoChar('*');
            }
        }
    }
}
