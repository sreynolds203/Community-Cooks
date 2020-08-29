import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static java.lang.Integer.parseInt;

public class Recipes extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // variables
        //labels
    static JLabel recipeNameLabel;
    static JLabel authorNameLabel;
    static JLabel newIngredient;
    static JLabel stepsLabel;
        //text fields
    static JTextField recipeNameInput;
    static JTextField authorNameInput;
    static JTextField ingredient;
    static JTextField quantity;
    static JTextField measurement;
        //button
    static JButton addIngredient;
    static JButton deleteIngredient;
    static JButton save;
    static JButton cancel;
        //table
    static JTable ingredientTable;
        //text area
    static JTextArea stepsArea;
        //scroll pane
    static JScrollPane ingredScrollPane;
    static JScrollPane stepScrollPane;
        //table model
    static DefaultTableModel model;
    //method for building frame
    public Recipes(){
        //frame components
        setTitle("New Recipe");
        setBounds(40,40,550,725);
        getContentPane().setLayout(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        //labels
        recipeNameLabel = new JLabel("Recipe Name:");
        recipeNameLabel.setBounds(10,10,100,25);
        getContentPane().add(recipeNameLabel);
        authorNameLabel = new JLabel("Author Name:");
        authorNameLabel.setBounds(10,67,100,25);
        getContentPane().add(authorNameLabel);
        newIngredient = new JLabel("New Ingredient:");
        newIngredient.setBounds(10,127,100,25);
        getContentPane().add(newIngredient);
        stepsLabel = new JLabel("Steps:");
        stepsLabel.setBounds(10,393,100,25);
        getContentPane().add(stepsLabel);
        //text fields
        recipeNameInput = new JTextField();
        recipeNameInput.setBounds(10,37,200,25);
        getContentPane().add(recipeNameInput);
        authorNameInput = new JTextField();
        authorNameInput.setBounds(10,94,200,25);
        getContentPane().add(authorNameInput);
        ingredient = new JTextField("Ingredient Name");
        ingredient.setBounds(10,154,200,25);
        getContentPane().add(ingredient);
        quantity = new JTextField("QTY");
        quantity.setBounds(212,154,50,25);
        getContentPane().add(quantity);
        measurement = new JTextField("(cup, oz, tbsp.)");
        measurement.setBounds(264,154,85,25);
        getContentPane().add(measurement);
        //focus listeners
        ingredient.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                ingredient.selectAll();
            }
            @Override
            public void focusLost(FocusEvent e) {
                //do nothing
            }
        });
        quantity.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                quantity.selectAll();
            }
            @Override
            public void focusLost(FocusEvent e) {
                //do nothing
            }
        });
        measurement.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                measurement.selectAll();
            }
            @Override
            public void focusLost(FocusEvent e) {
                //do nothing
            }
        });
        //buttons
        addIngredient = new JButton("Add");
        addIngredient.setBounds(351,154,70,25);
        getContentPane().add(addIngredient);
        deleteIngredient = new JButton("Delete");
        deleteIngredient.setBounds(426,154,70,25);
        getContentPane().add(deleteIngredient);
        save = new JButton("Save");
        save.setBounds(160,630,75,40);
        getContentPane().add(save);
        cancel = new JButton("Cancel");
        cancel.setBounds(300,630,75,40);
        getContentPane().add(cancel);
        //table
        ingredientTable = new JTable();
        //text area
        stepsArea = new JTextArea();
        stepsArea.setLineWrap(true);
        stepsArea.setWrapStyleWord(true);
        //creates table columns
        Object [] columns = {"QTY", "Measurement", "Ingredient"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columns);
        ingredientTable.setModel(model);
        ingredientTable.getColumnModel().getColumn(0).setMaxWidth(50);
        ingredientTable.getColumnModel().getColumn(1).setMaxWidth(175);
        ingredientTable.getColumnModel().getColumn(2).setMaxWidth(290);
        //scroll pane
        ingredScrollPane = new JScrollPane(ingredientTable);
        ingredScrollPane.setBounds(10,183,515,200);
        getContentPane().add(ingredScrollPane);
        stepScrollPane = new JScrollPane();
        stepScrollPane.setBounds(10,420,515,200);
        stepScrollPane.setViewportView(stepsArea);
        getContentPane().add(stepScrollPane);
        //adds action method to frame
        actionEvent();
    }
    //creates events for buttons
    public void actionEvent(){
        addIngredient.addActionListener(this);
        deleteIngredient.addActionListener(this);
        save.addActionListener(this);
        cancel.addActionListener(this);
    }
    //creates rows for tables
    Object[] row = new Object[3];
    //creates actions for the events when buttons are clicked
    public void actionPerformed(ActionEvent e){
        //if the add button is clicked
        if(e.getSource() == addIngredient){
            try {
                int q = Integer.parseInt(quantity.getText());
                //adds ingredient inputs to new row in table
                row[2] = ingredient.getText();
                row[0] = q;
                row[1] = measurement.getText();
                model.addRow(row);
            } catch (NumberFormatException numberFormatException) {
                //catches if quantity is not integer
                numberFormatException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Quantity must be a number");
            }
        }
        //if the delete button is clicked
        if(e.getSource() == deleteIngredient){
            int remove = ingredientTable.getSelectedRow();
            //removes row from table
            model.removeRow(remove);
        }
        //if the save button is clicked
        if(e.getSource() == save){
            //checks input of the recipe and author name fields
            if (recipeNameInput.getText().length() < 1 || authorNameInput.getText().length() < 1){
                //if field is empty
                JOptionPane.showMessageDialog(null, "Please enter recipe name and author");
            }
            else {
                //if fields are correct
                try {
                    Connection conn = SqlConnection.getConnection();
                    Statement statement = null;
                    Statement statement2 = null;
                    Statement statement3 = null;
                    String sql1 = "insert into recipes (recipe_name, " + "author) values(?,?);";
                    //inserts values into table
                    statement = conn.createStatement();
                    conn.setAutoCommit(false);
                    //inserts values into table
                    ((PreparedStatement) statement).setString(1, recipeNameInput.getText());
                    ((PreparedStatement) statement).setString(2, authorNameInput.getText());
                    String sql2 = "insert into ingredients " + "(recipe_id, ingredient, quantity, measurement) values((SELECT " + "recipe_id FROM recipes WHERE recipe_name = '" + recipeNameInput.getText() + "'),?,?,?);";
                    statement2 = conn.createStatement();
                    int i;
                    int count = ingredientTable.getRowCount();
                    for (i = 0; i < count; i++) {
                        try {
                            int quant = parseInt(ingredientTable.getValueAt(i, 0).toString());
                            String meas = ingredientTable.getValueAt(i, 1).toString();
                            String ingr = ingredientTable.getValueAt(i, 2).toString();
                            ((PreparedStatement) statement2).setString(1, ingr);
                            ((PreparedStatement) statement2).setInt(2, quant);
                            ((PreparedStatement) statement2).setString(3, meas);
                            statement2.addBatch(sql2);
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    String sql3 = "insert into steps " + "(recipe_id, steps) values ((SELECT recipe_id FROM recipes WHERE " + "recipe_name = '" + recipeNameInput.getText() + "'),?)";
                    statement3 = conn.createStatement();
                    ((PreparedStatement) statement3).setString(1, stepsArea.getText());
                    try {
                        statement.execute(sql1);
                        statement2.executeBatch();
                        statement3.execute(sql3);
                        conn.commit();
                        dispose();
                        new CommunityCooks();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        if(e.getSource() == cancel){
            dispose();
            new CommunityCooks();
        }
    }
}