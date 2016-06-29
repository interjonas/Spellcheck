import java.awt.EventQueue;

import java.util.Arrays;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.regex.*;
// program //
public class spellingCheck {

	// Main window
	private JFrame frame;

	// Source list model (The class is defined at the bottom)
	private SortedListModel sourceListModel;

	// Destination list model
	private SortedListModel destListModel;

	private SortedListModel newElementModel;

	// Source List Control
	private JList sourceList;

	// Destination List Control
	private JList destList;

	private JList newList;

	public void clearSourceListModel() {
		sourceListModel.clear();
	}

	public void clearDestinationListModel() {
		destListModel.clear();
	}

	public void clearNewElementModel()
	{
		newElementModel.clear();
	}

	public void addSourceElements(Object newValue[]) {
		fillListModel(sourceListModel, newValue);
	}

	public void setSourceElements(Object newValue[]) {
		clearSourceListModel();
		addSourceElements(newValue);
	}

	public void addDestinationElements(Object newValue[]) 
	{
		fillListModel(destListModel, newValue);
	}

	public void addNewElements(Object newValue[])
	{
		fillListModel(newElementModel, newValue);
	}

	private void fillListModel(SortedListModel model, Object newValues[]) 
	{
		model.addAll(newValues);
	}

	private void clearSourceSelected() {
		Object selected[] = sourceList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; --i) {
			sourceListModel.removeElement(selected[i]);
		}
		sourceList.getSelectionModel().clearSelection();
	}

	private void clearDestinationSelected() {
		Object selected[] = destList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; --i) {
			destListModel.removeElement(selected[i]);
		}
		destList.getSelectionModel().clearSelection();
	}

	private void removeNewElements()
	{
		Object selected[] = newList.getSelectedValues();
		for (int i = selected.length - 1; i >= 0; i--)
		{
			newElementModel.removeElement(selected[i]);
		}
		newList.getSelectionModel().clearSelection();
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					spellingCheck window = new spellingCheck();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public spellingCheck() {
		// Init gui interface
		initialize();
	}

	//Initializes the contents of the JFrame
	private void initialize() {
		frame = new JFrame(); //Creates the JFrame
		frame.setBounds(100, 100, 753, 482); //Size of JFrame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		DictionaryStatus dictStatus = new DictionaryStatus();
		SourceStatus sourceStatus = new SourceStatus();
		//Creates Add file button
		JButton btnAddFile = new JButton("Select input file");
		btnAddFile.setBounds(10, 150, 150, 30);
		frame.getContentPane().add(btnAddFile);

		//Add action listener to "Add File" button
		btnAddFile.addActionListener(new ActionListener() {
			@SuppressWarnings("unused")
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Add file code
				if(!dictStatus.getStatus())
				{
					JOptionPane.showMessageDialog(null, 
				        		"Please pick a dictionary file before trying to process a text file.", 
				        		"Message", 
				        		JOptionPane.INFORMATION_MESSAGE);
				}
				else if(sourceStatus.getStatus() == true)
				{
					JOptionPane.showMessageDialog(null, 
				        		"Please \"clear and start over\" before trying to process a new text file.", 
				        		"Message", 
				        		JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					// open file dialog to get input file.
					FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
					fd.setFile("*.txt");
					fd.setVisible(true);
					
					// if user not selected a file
					if (fd.getFile() == null) {
						// show a message
				        JOptionPane.showMessageDialog(null, 
				        		"You have not selected input file", 
				        		"Open file", 
				        		JOptionPane.INFORMATION_MESSAGE);
				        
					} 
					else 
					{	
						String filename = fd.getDirectory() + fd.getFile();

						// clear original items
						clearSourceListModel();
						
						// ok, now starting read
						String temp = ""; // main string to hold contents of file.

						try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
						{
						    String line;
						    
						    // read line by line
						    while ((line = br.readLine()) != null) 
						    {
						    	// process the line.
						    	// add item to the source list
						    	//addSourceElements(new String[] {line});
						    	temp += line+"\n";
						    }
						    br.close();
						} 
						catch (Exception e1) 
						{
							// when exception rised, print stack traces();
							//e1.printStackTrace();
						}
						Pattern pattern = Pattern.compile("([^\\s\\W]*)");
						Matcher matcher = pattern.matcher(temp);
						while(matcher.find())
						{
							boolean add = true;
							if(!matcher.group().equals(""))
							{
								for(int i = 0; i < destListModel.getSize(); i++)
								{
									if(matcher.group().equals(destListModel.getElementAt(i).toString()))
									{
										add = false;
									}
								}
								if(add)
								{
									addSourceElements(new String[] {matcher.group()});
								}
							}
							add = true;
						}
						sourceStatus.setStatus(true);
					}
				}
			}
		});
		JButton btnDictionary = new JButton("Select dictionary file");
		btnDictionary.setBounds(10, 105, 150, 30);
		frame.getContentPane().add(btnDictionary);

			btnDictionary.addActionListener(new AddDictionary() 
			{
			@SuppressWarnings("unused")
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Add file code
				if(dictStatus.getStatus() == true)
				{
					JOptionPane.showMessageDialog(null, 
				        		"Please \"clear and start over\" before trying to change the dictionary.", 
				        		"Message", 
				        		JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					// open file dialog to get input file.
					FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
					fd.setFile("*.txt");
					fd.setVisible(true);
					
					// if user not selected a file
					if (fd.getFile() == null) {
						// show a message
				        JOptionPane.showMessageDialog(null, 
				        		"You have not selected a dictionary file", 
				        		"Open file", 
				        		JOptionPane.INFORMATION_MESSAGE);
				        
					} 
					else 
					{	
						String filename = fd.getDirectory() + fd.getFile();
						dictStatus.setDictFileName(fd.getFile());
						
						// ok, now starting read
						String temp = ""; // main string to hold contents of file.

						try (BufferedReader br = new BufferedReader(new FileReader(filename))) 
						{
						    String dict;
						    
						    // read line by line
						    while ((dict = br.readLine()) != null) 
						    {
						    	// process the line.
						    	// add item to the source list
						    	//addSourceElements(new String[] {line});
						    	temp += dict+"\n";
						    }
						    br.close();
						} 
						catch (Exception e1) 
						{
							// when exception rised, print stack traces();
							//e1.printStackTrace();
						}
						Pattern pattern = Pattern.compile("([^\\s\\W]*)");
						Matcher matcher = pattern.matcher(temp);
						while(matcher.find())
						{
							if(!matcher.group().equals(""))
							{
								addDestinationElements(new String[] {matcher.group()});
							}
						}
						dictStatus.setStatus(true);
					}
				}
			}
		});

		//Creates Clear button
		JButton btnClear = new JButton("Clear and start over");
		btnClear.setBounds(10, 195, 150, 30);
		frame.getContentPane().add(btnClear);

		//Add action listener to "Clear" button
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// clear source list
				clearSourceListModel();
				
				// clear dest list
				clearDestinationListModel();

				clearNewElementModel();

				//clear dict status
				dictStatus.setStatus(false);
				dictStatus.setDictFileName("");
				sourceStatus.setStatus(false);
			}
		});

		//Creates Save button
		JButton btnSave = new JButton("Save current file");
		btnSave.setBounds(10, 245, 150, 30);
		frame.getContentPane().add(btnSave);

		//Add action listener to "Save" button
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Save dictionary code
				
				// if there is no item in the destination list, then show error message
				if(!sourceStatus.getStatus()) {
					// error message
			        JOptionPane.showMessageDialog(null, 
			        		"Please load an input file before trying to save.", 
			        		"Save file", 
			        		JOptionPane.ERROR_MESSAGE);					
				} else {

					JOptionPane.showMessageDialog(null, 
			        		"Now saving file for words not added to dictionary.", 
			        		"Save file", 
			        		JOptionPane.INFORMATION_MESSAGE);
					
					// open save file dialog to get save file name for misspelled
					FileDialog fd = new FileDialog(frame, "Choose a file for words not added", FileDialog.SAVE);
					fd.setFile("*.txt");
					fd.setVisible(true);
					
					// get full file path
					String filename = fd.getDirectory() + fd.getFile();
					
					while(fd.getFile() == null || fd.getFile().equals(dictStatus.getDictFileName()))
					{
						JOptionPane.showMessageDialog(null, 
			        	"Error, please enter a valid file name.", 
			       		"Save file", 
			       		JOptionPane.INFORMATION_MESSAGE);

						// open save file dialog to get save file name for dictionary
						fd.setVisible(true);
						
						// get full file path
						filename = fd.getDirectory() + fd.getFile();
						
					}
					// ok, user selected save file name
					try {
						
						// open PrintWriter
						PrintWriter out = new PrintWriter(filename);
						
						for(int i = 0; i < sourceListModel.getSize(); i++)
						{
							// write spellingCheck line by line
							out.println(sourceListModel.getElementAt(i).toString());						
						}
						
				        JOptionPane.showMessageDialog(null, 
				        		"Misspelled words successfully saved to the file " + filename, 
				        		"Save Misspelled Words", 
				        		JOptionPane.INFORMATION_MESSAGE);				
				        
						// close the descriptor
						out.close();
						
					} 
					catch (Exception e2) 
					{
						// TODO Auto-generated catch block
						//e2.printStackTrace();
					}	

					JOptionPane.showMessageDialog(null, 
			        		"Now saving file for new words added to dictionary.", 
			        		"Save file", 
			        		JOptionPane.INFORMATION_MESSAGE);
					
					// open save file dialog to get save file name for misspelled
					FileDialog fdNew = new FileDialog(frame, "Choose a file for new words added", FileDialog.SAVE);
					fdNew.setFile("*.txt");
					fdNew.setVisible(true);
					
					// get full file path
					String filenameNew = fdNew.getDirectory() + fdNew.getFile();
					
					while(fdNew.getFile() == null || fdNew.getFile().equals(dictStatus.getDictFileName()))
					{
						JOptionPane.showMessageDialog(null, 
			        	"Error, please enter a valid file name.", 
			       		"Save file", 
			       		JOptionPane.INFORMATION_MESSAGE);

						// open save file dialog to get save file name for dictionary
						fdNew.setVisible(true);
						
						// get full file path
						filenameNew = fdNew.getDirectory() + fdNew.getFile();
						
					}
					// ok, user selected save file name
					try {
						
						// open PrintWriter
						PrintWriter out = new PrintWriter(filenameNew);
						
						for(int i = 0; i < newElementModel.getSize(); i++)
						{
							// write spellingCheck line by line
							out.println(newElementModel.getElementAt(i).toString());						
						}
						
				        JOptionPane.showMessageDialog(null, 
				        		"New words added to dictionary successfully saved to the file " + filenameNew, 
				        		"Save New Words", 
				        		JOptionPane.INFORMATION_MESSAGE);				
				        
						// close the descriptor
						out.close();
						
					} 
					catch (Exception e2) 
					{
						// TODO Auto-generated catch block
						//e2.printStackTrace();
					}	
					
					JOptionPane.showMessageDialog(null, 
			        		"Now saving the dictionary file.", 
			        		"Save file", 
			        		JOptionPane.INFORMATION_MESSAGE);

					// open save file dialog to get save file name for dictionary
					FileDialog fdDict = new FileDialog(frame, "Choose a file for the dictionary", FileDialog.SAVE);
					fdDict.setFile("*.txt");
					fdDict.setVisible(true);
					
					// get full file path
					String filenameDict = fdDict.getDirectory() + fdDict.getFile();
					
					while(fdDict.getFile() == null)
					{
						JOptionPane.showMessageDialog(null, 
			        	"Error, please enter a valid file name.", 
			       		"Save file", 
			       		JOptionPane.INFORMATION_MESSAGE);

						// open save file dialog to get save file name for dictionary
						fdDict.setVisible(true);
						
						// get full file path
						filenameDict = fdDict.getDirectory() + fdDict.getFile();
						
					}
					// ok, user selected save file name
					try {
						
						// open PrintWriter
						PrintWriter out = new PrintWriter(filenameDict);
						
						for(int i = 0; i < destListModel.getSize(); i++)
						{
							// write spellingCheck line by line
							out.println(destListModel.getElementAt(i).toString());						
						}
						
				        JOptionPane.showMessageDialog(null, 
				        		"Dictionary succesfully saved to the file " + filenameDict, 
				        		"Save Dictionary", 
				        		JOptionPane.INFORMATION_MESSAGE);				
				        
						// close the descriptor
						out.close();
						
					}
					catch (Exception e2) 
					{
						// TODO Auto-generated catch block
						//e2.printStackTrace();
					}	
				}
			}
		});

		//Creates Exit button
		JButton btnExit = new JButton("Exit program");
		btnExit.setBounds(10, 295, 150, 30);
		frame.getContentPane().add(btnExit);

		//Add action listener to "Exit" button
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Exit program code
		        System.exit(0);
			}
		});

		//Creates Add spellingCheck to dictonary button
		JButton btnAddGUI2 = new JButton("Add word");
		btnAddGUI2.setBounds(375, 200, 125, 30);
		frame.getContentPane().add(btnAddGUI2);

		// Add action listener to "Add" button
		btnAddGUI2.addActionListener(new AddListener());

		// Creates "Remove" button
		JButton btnRemove = new JButton("Remove word");
		btnRemove.setBounds(376, 245, 125, 30);
		//frame.getContentPane().add(btnRemove);

		// Add action listener to "Remove" button
		btnRemove.addActionListener(new RemoveListener());

		// Misspelled spellingCheck label
		JLabel lblMisspelledGUI2 = new JLabel("Misspelled words from file");
		lblMisspelledGUI2.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblMisspelledGUI2.setBounds(210, 56, 146, 20);
		frame.getContentPane().add(lblMisspelledGUI2);

		// Added to dictionary label
		JLabel lblGUI2AddedTo = new JLabel("Words in dictionary");
		lblGUI2AddedTo.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblGUI2AddedTo.setBounds(550, 56, 146, 20);
		frame.getContentPane().add(lblGUI2AddedTo);

		// List Control for misspelled spellingCheck
		sourceListModel = new SortedListModel();
		sourceList = new JList(sourceListModel);
		sourceList.setBounds(175, 86, 195, 310);

		JScrollPane scrollView1 = new JScrollPane(sourceList);
		scrollView1.setBounds(175, 86, 195, 310);	
		frame.getContentPane().add(scrollView1);

		// List control for added spellingCheck

		destListModel = new SortedListModel();
		destList = new JList(destListModel);
		destList.setBounds(510, 86, 195, 310);
		frame.getContentPane().add(destList);

		newElementModel = new SortedListModel();
		newList = new JList(newElementModel);

		JScrollPane scrollView2 = new JScrollPane(destList);
		scrollView2.setBounds(510, 86, 195, 310);
		frame.getContentPane().add( scrollView2 );
		
	}
	private class AddListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// get selected items from the source list
			Object selected[] = sourceList.getSelectedValues();
			
			// ok, then add them to the destination list
			addDestinationElements(selected);

			// and add them to the list of new things added which will be saved later
			addNewElements(selected);
			
			// clear the added items from the source list
			clearSourceSelected();
		}
	}
	private class AddDictionary implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			// get selected items from the source list
			//Object selected[] = sourceList.getSelectedValues();
			
			// ok, then add them to the destination list
			//addDestinationElements(selected);
			
			// clear the added items from the source list
			//clearSourceSelected();
		}
	}

	private class RemoveListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// get selected items from the destination list
			Object selected[] = destList.getSelectedValues();
			
			// ok, then add them to the source list			
			addSourceElements(selected);

			//remove for added list if it exists
			removeNewElements();
			
			// clear the added items from the destination list			
			clearDestinationSelected();
		}
	}

}
class DictionaryStatus 
{
	private boolean status;
	private String dictFileName;
	public DictionaryStatus()
	{
		status = false;
		dictFileName = "";
	}
	public boolean getStatus()
	{
		return status;
	}
	public void setStatus(boolean val)
	{
		status = val;
	}
	public String getDictFileName()
	{
		return dictFileName;
	}
	public void setDictFileName(String name)
	{
		dictFileName = name;
	}
}
class SourceStatus 
{
	private boolean status;
	public SourceStatus()
	{
		status = false;
	}
	public boolean getStatus()
	{
		return status;
	}
	public void setStatus(boolean val)
	{
		status = val;
	}
}	
class SortedListModel extends AbstractListModel {

	SortedSet model;

	public SortedListModel() 
	{
		model = new TreeSet();
	}
	@Override
	public int getSize() 
	{
		return model.size();
	}
	@Override
	public Object getElementAt(int index) 
	{
		return model.toArray()[index];
	}
	public void add(Object element) 
	{
		if (model.add(element)) 
		{
			fireContentsChanged(this, 0, getSize());
		}
	}
	public void addAll(Object elements[]) 
	{
		Collection c = Arrays.asList(elements);
		model.addAll(c);
		fireContentsChanged(this, 0, getSize());
	}
	public void clear() 
	{
		model.clear();
		fireContentsChanged(this, 0, getSize());
	}
	public boolean contains(Object element) 
	{
		return model.contains(element);
	}
	public Object firstElement() 
	{
		return model.first();
	}
	public Object lastElement() 
	{
		return model.last();
	}
	public boolean removeElement(Object element) 
	{
		boolean removed = model.remove(element);
		if (removed) {
			fireContentsChanged(this, 0, getSize());
		}
		return removed;
	}
}