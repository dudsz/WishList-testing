
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.lang3.*;

public class GUI extends JFrame {
	
	JTextField addField;
	String username = "";
	JList myList, sharedList;
	JComboBox box;
	JTable myTable, sharedTable;
	
	public GUI(String name) {
		
		username = name;
		createFrame(name);
	}
	
	public void createFrame(String name) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(name);
		setSize(300, 250);
		setLocationRelativeTo(null);

		getContentPane().add(new LeftPanel(), BorderLayout.WEST);
		getContentPane().add(new RightPanel2(name), BorderLayout.EAST);
		getContentPane().add(new CenterPanel2(name), BorderLayout.CENTER);
		getContentPane().add(new ButtonPanel(name), BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}
	
	class LeftPanel extends JPanel {
		String[] friends = {"otto", "duds", "alex"};
		
		public LeftPanel() {

			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(2, 2, 2, 2);
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("Add item: "), gbc);
			gbc.gridy++;
			add(new JLabel("Share to: "), gbc);
			
			gbc.gridy = 0;
			gbc.gridx++;
			gbc.anchor = GridBagConstraints.WEST;
			addField = new JTextField(15);
			add(addField, gbc);
			gbc.gridy++;
			box = new JComboBox(friends);
			add(box, gbc);
			setPreferredSize(new Dimension(250, 250));
		}
	}
	
	class CenterPanel extends JPanel {
		DefaultListModel listModel = null;
		
		public CenterPanel(String user) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(8, 8, 8, 8);
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("My wish list items"), gbc);
			gbc.gridy++;
			listModel = Main.userItems(user);
			myList = new JList(listModel);
			myList.setVisibleRowCount(4);
			myList.setFixedCellHeight(50);
			myList.setFixedCellWidth(140);
			myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			add(new JScrollPane(myList), gbc);
		}
	}
	
	class CenterPanel2 extends JPanel {
		DefaultTableModel mdl = null;
		
	    public CenterPanel2(String user) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(8, 8, 8, 8);
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("My wish list items"), gbc);
			gbc.gridy++;
			mdl = Main.updateMyTable(username); 
			mdl = Main.userItems2(username);
			myTable = new JTable(mdl) {
				public boolean isCellEditable(int row, int column) {   
					return false;         
				};
			};
			JScrollPane scP = new JScrollPane(myTable);
			scP.setPreferredSize(new Dimension(150, 250));
			add(scP, gbc);
	    }
	}
	
	class RightPanel extends JPanel {
		DefaultListModel listModel = null;
		
	    public RightPanel(String user) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(8, 8, 8, 8);
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("Items shared with me: "), gbc);
			gbc.gridy++;
			listModel = Main.sharedItems(user);
			sharedList = new JList(listModel);
			sharedList.setVisibleRowCount(4);
			sharedList.setFixedCellHeight(50);
			sharedList.setFixedCellWidth(140);
			sharedList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			add(new JScrollPane(sharedList), gbc);
		}
	}
	
	class RightPanel2 extends JPanel {
		DefaultTableModel mdl = null;
		
	    public RightPanel2(String user) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(8, 8, 8, 8);
			gbc.anchor = GridBagConstraints.WEST;
			add(new JLabel("Items shared with me: "), gbc);
			gbc.gridy++;
			mdl = Main.sharedTable(username); 
			sharedTable = new JTable(mdl) {
				public boolean isCellEditable(int row, int column) {   
					return column == 2;         
				};
				@Override
	            public Class getColumnClass(int column) {
	                switch (column) {
	                    case 0:
	                        return String.class;
	                    case 1:
	                        return String.class;
	                    case 2:
	                        return Boolean.class;
	                    default:
	                        return Boolean.class;
	                }
	            }
			};
			JScrollPane scP = new JScrollPane(sharedTable);
			scP.setPreferredSize(new Dimension(205, 250));
			add(scP, gbc);
	    }
	}
		
	class ButtonPanel extends JPanel implements ActionListener {
		
		private JButton addButton, updButton, shareButton;
		
		public ButtonPanel(String user) {
			setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.insets = new Insets(8, 8, 8, 8);

			addButton = new JButton("Add");
			updButton = new JButton("Update");
			shareButton = new JButton("Share");
			add(addButton, gbc);
			gbc.gridx++;
			add(updButton, gbc);
			gbc.gridx++;
			add(shareButton, gbc);
			
			addButton.addActionListener(this);
			updButton.addActionListener(this);
			shareButton.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addButton) {
				String item = addField.getText();
				if (!StringUtils.isEmpty(item)) {
					System.out.println("Add clicked. Item: " + item);
					Main.addItem(username, item);
	                myTable.setModel(Main.updateMyTable(username));
	                //myList.setModel(Main.updateMyList(username));
				}
			}
			
			if (e.getSource() == updButton) {
				System.out.println("Update clicked");
				int col = sharedTable.getSelectedColumn();
				int row = sharedTable.getSelectedRow();
				if (row >= 0 && col >= 0) {
					String item = (String) sharedTable.getModel().getValueAt(row, 0);
					String sharedBy = (String) sharedTable.getModel().getValueAt(row, 1);
					boolean bought = (boolean) sharedTable.getModel().getValueAt(row, 2);
					Main.updateSharedItem(sharedBy, item, bought);
				}
				sharedTable.setModel(Main.sharedTable(username));
			}
			
			if (e.getSource() == shareButton) {
				//String item = (String) myList.getSelectedValue();
				int col = myTable.getSelectedColumn();
				int row = myTable.getSelectedRow();
				System.out.println("Row: " + row + " and col: " + col);
				if (row >= 0 && col >= 0) {
					String item = (String) myTable.getModel().getValueAt(row, col);
					String shareWith = (String) box.getSelectedItem();
					if (!StringUtils.isEmpty(item) && !username.equals(shareWith)) {
						Main.shareItem(username, shareWith, item);
					}
				}
			}
		}
	}
}
