import javax.swing.JFrame; 
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;
import javax.swing.JList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.awt.Component;
import javax.swing.JTextArea;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.WindowConstants;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import java.awt.Color;

public class Chat_GUI implements GUIInterface, ActionListener, ComponentListener {
	
	private JFrame inputframe;
	private JTextField messageInput;
	private JTextField separateInput;
	private JLabel lblTitle;
	private JLabel lblErrorMessage;
	private JList<String> listRooms;
	private DefaultListModel<String> userRooms;
	private JList<String> listMessages;
	private DefaultListModel<String> userMessages;
	private Client client;
	private Integer actualWidth;
	
	
	// by MChaker on https://stackoverflow.com/questions/30027582/limit-the-number-of-characters-of-a-jtextfield
	public class MaxLengthTextDocument extends PlainDocument {
	    //Store maximum characters permitted
	    private int maxChars;
	   
	    
	    public void setMaxChars(int maxChars) {
	    	this.maxChars = maxChars;
		}

	    @Override
	    public void insertString(int offs, String str, AttributeSet a)
	            throws BadLocationException {
	        // the length of string that will be created is getLength() + str.length()
	        if(str != null && (getLength() + str.length() < maxChars)){
	            super.insertString(offs, str, a);
	        }
	    }

	}
	
	public void componentResized(ComponentEvent evt) {
        actualWidth = evt.getComponent().getWidth(); 
        System.out.println(actualWidth);
        listMessages.setCellRenderer(new MyCellRenderer(actualWidth-100));
    }

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//by Andrew on StackOverflow (https://stackoverflow.com/questions/7861724/is-there-a-word-wrap-property-for-jlabel/7861833#7861833)
		@SuppressWarnings("serial")
	class MyCellRenderer extends DefaultListCellRenderer {
		   public static final String HTML_1 = "<html><body style='width: ";
		   public static final String HTML_2 = "px'>";
		   public static final String HTML_3 = "</html>";
		   private int width;

		   public MyCellRenderer(int width) {
		      this.width = width;
		   }
		   @Override
		   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {			      
			   String text = HTML_1 + String.valueOf(width) + HTML_2 + value.toString() + HTML_3;
			      return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
			   }
			}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void createChatInterface(String nickname) {
		
		userRooms = new DefaultListModel<>();
		userMessages = new DefaultListModel<>();
		userRooms.addElement("Room1");
		userRooms.addElement("Room2");
		userRooms.addElement("Room3");
		userRooms.addElement("Room4");
		
		JFrame frame = new JFrame();
		frame.setTitle("RMI Chat for " + nickname);
		frame.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0};
		gridBagLayout.rowHeights = new int[] {0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		
		JTabbedPane user_rooms_tabbed = new JTabbedPane(JTabbedPane.TOP);
		user_rooms_tabbed.setMaximumSize(new Dimension(60, 0));
		user_rooms_tabbed.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		user_rooms_tabbed.setPreferredSize(new Dimension(60, 0));
		user_rooms_tabbed.setMinimumSize(new Dimension(60, 0));
		GridBagConstraints gbc_user_rooms_tabbed = new GridBagConstraints();
		gbc_user_rooms_tabbed.insets = new Insets(0, 0, 0, 5);
		gbc_user_rooms_tabbed.fill = GridBagConstraints.BOTH;
		gbc_user_rooms_tabbed.gridx = 0;
		gbc_user_rooms_tabbed.gridy = 0;
		frame.getContentPane().add(user_rooms_tabbed, gbc_user_rooms_tabbed);
		
		JPanel rooms_panel = new JPanel();
		rooms_panel.setMaximumSize(new Dimension(70, 32767));
		rooms_panel.setMinimumSize(new Dimension(70, 0));
		rooms_panel.setPreferredSize(new Dimension(70, 0));
		user_rooms_tabbed.addTab("Rooms", null, rooms_panel, null);
		GridBagLayout gbl_rooms_panel = new GridBagLayout();
		gbl_rooms_panel.columnWidths = new int[]{0, 0};
		gbl_rooms_panel.rowHeights = new int[]{0, 0, 0};
		gbl_rooms_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_rooms_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		rooms_panel.setLayout(gbl_rooms_panel);
		
		listRooms = new JList<String>(userRooms);
		listRooms.setMaximumSize(new Dimension(50, 0));
		listRooms.setMinimumSize(new Dimension(50, 0));
		listRooms.setPreferredSize(new Dimension(50, 0));
		listRooms.setValueIsAdjusting(true);
		GridBagConstraints gbc_listRooms = new GridBagConstraints();
		gbc_listRooms.insets = new Insets(0, 0, 5, 0);
		gbc_listRooms.fill = GridBagConstraints.BOTH;
		gbc_listRooms.gridx = 0;
		gbc_listRooms.gridy = 0;
		rooms_panel.add(listRooms, gbc_listRooms);
		
		JButton btnAddRoom = new JButton("Add");
		btnAddRoom.setPreferredSize(new Dimension(50, 29));
		btnAddRoom.setMinimumSize(new Dimension(50, 29));
		btnAddRoom.setMaximumSize(new Dimension(50, 29));
		GridBagConstraints gbc_btnAddRoom = new GridBagConstraints();
		gbc_btnAddRoom.gridx = 0;
		gbc_btnAddRoom.gridy = 1;
		rooms_panel.add(btnAddRoom, gbc_btnAddRoom);
		btnAddRoom.setActionCommand("new_room");
		btnAddRoom.addActionListener(this);
		
		JPanel users_panel = new JPanel();
		users_panel.setPreferredSize(new Dimension(0, 0));
		users_panel.setMinimumSize(new Dimension(0, 0));
		user_rooms_tabbed.addTab("Users", null, users_panel, null);
		GridBagLayout gbl_users_panel = new GridBagLayout();
		gbl_users_panel.columnWidths = new int[] {0, 0};
		gbl_users_panel.rowHeights = new int[] {0, 0, 0};
		gbl_users_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_users_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		users_panel.setLayout(gbl_users_panel);
		
		JList listUsers = new JList();
		listUsers.setMaximumSize(new Dimension(50, 0));
		listUsers.setMinimumSize(new Dimension(50, 0));
		listUsers.setPreferredSize(new Dimension(50, 0));
		GridBagConstraints gbc_listUsers = new GridBagConstraints();
		gbc_listUsers.insets = new Insets(0, 0, 5, 0);
		gbc_listUsers.fill = GridBagConstraints.BOTH;
		gbc_listUsers.gridx = 0;
		gbc_listUsers.gridy = 0;
		users_panel.add(listUsers, gbc_listUsers);
		
		JButton btnAddUser = new JButton("Add");
		btnAddUser.setMaximumSize(new Dimension(50, 29));
		btnAddUser.setMinimumSize(new Dimension(50, 29));
		btnAddUser.setPreferredSize(new Dimension(50, 29));
		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.gridx = 0;
		gbc_btnAddUser.gridy = 1;
		users_panel.add(btnAddUser, gbc_btnAddUser);
		btnAddUser.setActionCommand("new_user");
		btnAddUser.addActionListener(this);
		
		JPanel chat_panel = new JPanel();
		chat_panel.setMinimumSize(new Dimension(0, 0));
		chat_panel.setPreferredSize(new Dimension(300, 0));
		chat_panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		GridBagConstraints gbc_chat_panel = new GridBagConstraints();
		gbc_chat_panel.weighty = 1.0;
		gbc_chat_panel.weightx = 1.0;
		gbc_chat_panel.insets = new Insets(5, 0, 13, 5);
		gbc_chat_panel.fill = GridBagConstraints.BOTH;
		gbc_chat_panel.gridx = 1;
		gbc_chat_panel.gridy = 0;
		frame.getContentPane().add(chat_panel, gbc_chat_panel);
		GridBagLayout gbl_chat_panel = new GridBagLayout();
		gbl_chat_panel.columnWidths = new int[]{0, 0};
		gbl_chat_panel.rowHeights = new int[]{0, 0, 0};
		gbl_chat_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_chat_panel.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		chat_panel.setLayout(gbl_chat_panel);
		chat_panel.addComponentListener(this);
		
		listMessages = new JList<String>(userMessages);
		GridBagConstraints gbc_messagesDisplay = new GridBagConstraints();
		gbc_messagesDisplay.gridwidth = 2;
		gbc_messagesDisplay.insets = new Insets(0, 0, 5, 0);
		gbc_messagesDisplay.fill = GridBagConstraints.BOTH;
		gbc_messagesDisplay.gridx = 0;
		gbc_messagesDisplay.gridy = 0;
		chat_panel.add(listMessages, gbc_messagesDisplay);
		
		messageInput = new JTextField();
		GridBagConstraints gbc_messageInput = new GridBagConstraints();
		gbc_messageInput.insets = new Insets(0, 5, 0, 5);
		gbc_messageInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_messageInput.gridx = 0;
		gbc_messageInput.gridy = 1;
		chat_panel.add(messageInput, gbc_messageInput);
		MaxLengthTextDocument maxLength = new MaxLengthTextDocument();
		maxLength.setMaxChars(256); 
		messageInput.setDocument(maxLength);

		
		JButton btnSend = new JButton("Send");
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		chat_panel.add(btnSend, gbc_btnSend);
		btnSend.setActionCommand("send");
		btnSend.addActionListener(this);
		
		frame.getRootPane().setDefaultButton(btnSend);
		frame.setSize(600,400);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}
	
	
	public void createInputInterface(String inputInterfaceType) {
		
		//règles pour editer l'affichage selon les 3 cas login, ajouter un utilisateur et créer un salon.
		String inputTitle = "Login";
		String welcomeMessage = "Choose your nickname :";
		String buttonText = "Log in";
		String errorMessage = null;
		
		//Initialise main frame
		inputframe = new JFrame();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		inputframe.setPreferredSize(new Dimension(80, 40));
		inputframe.getContentPane().setLayout(gridBagLayout);
		
		//Checkbox pour signaler un salon privé (invisible, sauf pour l'ajout d'un salon)
		JCheckBox chckbxPrivate = new JCheckBox("Private");
		chckbxPrivate.setVisible(false);
		
		if (inputInterfaceType == "login") {
			inputTitle = "Login";
			welcomeMessage = "Choose your nickname :";
			buttonText = "Log in";
		} else if (inputInterfaceType == "add_user"){
			inputTitle = "Add a user";
			welcomeMessage = "Enter the nickname of the autorised user :";
			buttonText = "Add";
		} else if (inputInterfaceType == "add_room"){
			inputTitle = "Create a new room";
			welcomeMessage = "Enter the name of the desired room :";
			buttonText = "Create";
			chckbxPrivate.setVisible(true);
		}
		
		//éléments d'interface
		inputframe.setTitle(inputTitle);
		lblTitle = new JLabel(welcomeMessage);
		
		separateInput = new JTextField();
		separateInput.setColumns(10);
		
		lblErrorMessage = new JLabel(errorMessage);
		lblErrorMessage.setForeground(Color.RED);
		
		JButton btnSubmit = new JButton(buttonText);
		btnSubmit.setActionCommand(inputInterfaceType);
		btnSubmit.addActionListener(this);
		
		
		//Préparation de la postion des éléments dans la grille
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 0;
		
		GridBagConstraints gbc_textInput = new GridBagConstraints();
		gbc_textInput.insets = new Insets(0, 20, 5, 20);
		gbc_textInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInput.gridx = 0;
		gbc_textInput.gridy = 1;
		
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 2;
		
		GridBagConstraints gbc_chckbxPrivate = new GridBagConstraints();
		gbc_chckbxPrivate.fill = GridBagConstraints.VERTICAL;
		gbc_chckbxPrivate.gridx = 0;
		gbc_chckbxPrivate.gridy = 3;
		
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 4;
		
		//Ajouter les éléments à la grille
		inputframe.getContentPane().add(lblTitle, gbc_lblTitle);
		inputframe.getContentPane().add(separateInput, gbc_textInput);
		inputframe.getContentPane().add(lblErrorMessage, gbc_lblErrorMessage);
		inputframe.getContentPane().add(chckbxPrivate, gbc_chckbxPrivate);
		inputframe.getContentPane().add(btnSubmit, gbc_btnSubmit);
		
		
		inputframe.getRootPane().setDefaultButton(btnSubmit);
		inputframe.setSize(300,200);
		inputframe.setLocationRelativeTo(null);
		inputframe.setVisible(true);
		
		
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton btn = (JButton)evt.getSource();
		String command = btn.getActionCommand();
		System.err.println(command);
		if (command.equals("login")) {
			String inputText = separateInput.getText();
			//vérifier si vide
			if (inputText.equals("")) {
				lblErrorMessage.setText("You can't login without a nickname!");
			} else {
				client = new Client(-1, inputText, null);
				//envoyer le login au serveur
				//recevoir l'erreur si login déjà existant
				boolean login_exists = false;
				
				//si login existe, lancer le chat, sinon afficher le message d'erreur
				if (login_exists == false) {
					createChatInterface(inputText);
				} else {
					lblErrorMessage.setText("Nickname already taken!");
				}
			}	
		} else if (command.equals("new_user")) {
			createInputInterface("add_user");
		} else if (command.equals("add_user")) {
			String inputText = separateInput.getText();
			//vérifier si vide
			if (inputText.equals("")) {
				lblErrorMessage.setText("No nickname has been mentionned!");
			} else {
				//envoyer le nom d'utilisateur au serveur
				//recevoir l'erreur si le login est déjà existant
				boolean user_exists = false;
				
				//si l'utilisateur existe, l'ajouter au salon, sinon afficher le message d'erreur
				if (user_exists == true) {
					//ajout à la room
				} else {
					lblErrorMessage.setText("The user has to be online!");
				}
			}	
			// add user to room
		} else if (command.equals("new_room")) {
			createInputInterface("add_room");
		} else if (command.equals("add_room")) {
			String inputText = separateInput.getText();
			//vérifier si vide
			if (inputText.equals("")) {
				lblErrorMessage.setText("No room name has been mentionned!");
			} else {
				//envoyer le nom du salon + utlisateur au serveur
				//recevoir l'erreur si le salon est déjà existant
				/*
				 * METHODE SERVEUR
				 * appel serveur si existante dans la liste
				*/
				boolean room_exists = false;
				
				//si le salon existe, ajouter le salon, sinon afficher le message d'erreur
				if (room_exists == false) {
					/*
					 * METHODE SERVEUR
					 * ajout de la room + utilisateur au server
					*/
					userRooms.addElement(inputText);
					listRooms.setModel(userRooms);
					inputframe.dispatchEvent(new WindowEvent(inputframe, WindowEvent.WINDOW_CLOSING));
				} else {
					lblErrorMessage.setText("Room exists already!");
				}
			}	
		} else if (command.equals("send")) {
			
			String inputText = messageInput.getText();
			messageInput.setText("");
			Date date = new Date(evt.getWhen());
			SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
			String timeStamp = ft.format(date);
			String message = "[" + timeStamp + "] " + client.getName() + " : " + inputText ;
			
			/*
			 * METHODE SERVEUR
			 * envoi au serveur du message à envoyer aux autres utilisareurs
			*/
			userMessages.addElement(message);
			listMessages.setModel(userMessages);
			inputframe.dispatchEvent(new WindowEvent(inputframe, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/*public void addElement (String elementType, String element, Hashtable<Integer, String> map) {
		
	}*/
	
}
