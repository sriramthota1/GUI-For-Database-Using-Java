import javax.swing.*; 
import java.awt.*; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener; 
public class srm{ 
private JFrame frame; 
private JTextField textField; 
private int value = 0; 
public static void main(String[] args) { 
    new srm().createAndShowGUI(); 
} 
private void createAndShowGUI() { 
frame = new JFrame("Value Updater"); 
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
frame.setSize(300, 150); 
frame.setLayout(new FlowLayout()); 
textField = new JTextField(10); 
textField.setEditable(false); 
updateTextField(); 
JButton increaseButton = new JButton("Increase"); 
JButton decreaseButton = new JButton("Decrease"); 
increaseButton.addActionListener(new ActionListener() { 
@Override 
public void actionPerformed(ActionEvent e) { 
value++; 
updateTextField(); 
} 
}); 
decreaseButton.addActionListener(new ActionListener() { 
@Override 
public void actionPerformed(ActionEvent e) { 
value--; 
updateTextField(); 
} 
}); 
frame.add(textField); 
frame.add(increaseButton); 
frame.add(decreaseButton); 
frame.setVisible(true); 
} 
private void updateTextField() { 
textField.setText("Value: " + value); 
} 
} 