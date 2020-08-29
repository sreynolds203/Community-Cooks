import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class CommunityCooks extends JFrame implements ActionListener {
    /**
     * serialization
     */
    private static final long serialVersionUID = 1L;
    // variables
    static JLabel welcome;
    static JButton create;
    static JButton delete;
    static JLabel nameLabel;
    static JLabel detailLabel;
    static JTextArea detailArea;
    static JScrollPane listScrollPane;
    static JScrollPane detailScrollPane;
    static DefaultListModel<String> nameListModel;
    static JList<String> nameList;
    //constructor to build the frame
    public CommunityCooks(){
        //frame components
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(25,25,1225,725);
        setTitle("Community Cooks");
        getContentPane().setLayout(null);
        setVisible(true);
        setResizable(false);
        //welcome banner
        welcome = new JLabel("Welcome to Community Cooks!");
        welcome.setBounds(10,10,250,25);
        getContentPane().add(welcome);
        //labels
        nameLabel = new JLabel("Recipe Name:");
        nameLabel.setBounds(10,95,150,25);
        getContentPane().add(nameLabel);
        detailLabel = new JLabel("Recipe Details:");
        detailLabel.setBounds(380,95,150,25);
        getContentPane().add(detailLabel);
        //action buttons
        create = new JButton("Create");
        create.setBounds(10,50,100,25);
        getContentPane().add(create);
        delete = new JButton("Delete");
        delete.setBounds(120,50,100,25);
        getContentPane().add(delete);
        detailArea = new JTextArea();
        detailArea.setEditable(false);
        detailArea.setLineWrap(true);
        detailArea.setWrapStyleWord(true);
        detailScrollPane = new JScrollPane(detailArea);
        detailScrollPane.setBounds(380,120,810,550);
        getContentPane().add(detailScrollPane);
        //Action events for buttons
        compileList();
        nameList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = (String) nameList.getSelectedValue();
                int id = 0;
                String author = null;
                String step = null;
                try {
                    Connection conn = SqlConnection.getConnection();
                    Statement statement = null;
                    Statement statement2 = null;
                    Statement statement3 = null;
                    ResultSet result = null;
                    ResultSet result2 = null;
                    ResultSet result3 = null;
                    String sql1 =
                            "Select recipe_id FROM recipes WHERE recipe_name = '" + name + "';";
                    statement = conn.createStatement();
                    result = statement.executeQuery(sql1);
                    while(result.next()){
                        id = result.getInt("recipe_id");
                    }
                    String sql2 = "SELECT author, steps FROM recipes JOIN steps ON recipes" +
                            ".recipe_id = steps.recipe_id WHERE recipes.recipe_id = "+ id + ";";
                    statement2 = conn.createStatement();
                    result2 = statement2.executeQuery(sql2);
                    while (result2.next()){
                        author = result2.getString("author");
                        step = result2.getString("steps");
                    }
                    String output1 = "Recipe: " +name + "\n" + "Author: " + author + " \n \n";
                    detailArea.setText(output1);
                    String sql3 = "Select quantity, measurement, ingredient FROM " +
                            "ingredients WHERE recipe_id = '" + id + "';";
                    statement3 = conn.createStatement();
                    result3 = statement3.executeQuery(sql3);
                    detailArea.append("Ingredients: \n\n");
                    int q;
                    String m;
                    String i;
                    String ingList;
                    while((result3 != null) && (result3.next())){
                        q = result3.getInt("quantity");
                        m = result3.getString("measurement");
                        i = result3.getString("ingredient");
                        ingList = q + " " + m + " " + i + "\n";
                        detailArea.append(ingList);
                    }
                    detailArea.append("\n");
                    String output2 = "\n Directions: \n\n" + step;
                    detailArea.append(output2);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        actionEvent();
    }
    public void compileList(){
        nameListModel = new DefaultListModel<>();
        try {
            Connection conn = SqlConnection.getConnection();
            Statement statement = null;
            ResultSet result = null;
            String sql = "SELECT recipe_name FROM recipes ORDER BY recipe_name ASC;";
            statement = conn.createStatement();
            result = statement.executeQuery(sql);
            while((result != null) && (result.next())){
                nameListModel.addElement(result.getString("recipe_name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        nameList = new JList<String>(nameListModel);
        listScrollPane = new JScrollPane(nameList);
        listScrollPane.setBounds(10,120,350,550);
        getContentPane().add(listScrollPane);
    }
    public void actionEvent(){
        create.addActionListener(this);
        delete.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == create){
            dispose();
            new Recipes();
        }
        if(e.getSource() == delete){
            String deleteName = (String) nameList.getSelectedValue();
            int deleteIndex = nameList.getSelectedIndex();
            nameListModel.remove(deleteIndex);
            detailArea.setText(null);
            try {
                Connection conn = SqlConnection.getConnection();
                Statement statement =null;
                Statement statement2 = null;
                ResultSet result = null;
                String sql =
                        "SELECT recipe_id FROM recipes WHERE recipe_name = '" + deleteName + "';";
                statement = conn.createStatement();
                result = statement.executeQuery(sql);
                int nameID = 0;
                while(result.next()){
                    nameID = result.getInt("recipe_id");
                }
                String sql2 = "DELETE FROM recipes WHERE recipe_id = '" + nameID + "';";
                statement2 = conn.createStatement();
                statement2.execute(sql2);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}