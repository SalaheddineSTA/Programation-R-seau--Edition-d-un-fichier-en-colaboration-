/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.datatransfer.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import static project1.LoginPanel.frame;
import static project1.main1.ipp;
import static project1.main1.portt;
import static project1.main1.ppp;
import static project1.main1.s;
import static project1.main1.ss;

public class txteditor extends JFrame implements ActionListener, WindowListener {
	HashMap<String,String> inn = new HashMap<String,String>();
        String filename="";
        int idP;
	JTextArea jta=new JTextArea();
	File fnameContainer;
	
	public txteditor(String text,HashMap<String,String> hss,String filename,int idP){
                inn.putAll(hss);
                this.filename=filename;
                this.idP=idP;
		Font fnt=new Font("Verdana",Font.PLAIN,15);
                
		Container con=getContentPane();
		
		JMenuBar jmb=new JMenuBar();
		JMenu jmfile = new JMenu("File");
		JMenu jmedit = new JMenu("Edit");
		/*JMenu jmhelp = new JMenu("Help");*/
		
		con.setLayout(new BorderLayout());
		//trying to add scrollbar
		JScrollPane sbrText = new JScrollPane(jta);
		sbrText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sbrText.setVisible(true);
		
                System.out.println("teeeeext::"+ text);
		jta.setFont(fnt);
                
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
                jta.setText("");
                jta.setText(text);
		con.add(sbrText);

		/*createMenuItem(jmfile,"New");
		createMenuItem(jmfile,"Open");*/
		createMenuItem(jmfile,"Save");
		jmfile.addSeparator();
		createMenuItem(jmfile,"Exit");
		
		createMenuItem(jmedit,"Cut");
		createMenuItem(jmedit,"Copy");
		createMenuItem(jmedit,"Paste");
		
		/*createMenuItem(jmhelp,"About Notepad");*/
		
		jmb.add(jmfile);
		jmb.add(jmedit);
/*		jmb.add(jmhelp);*/
		
		setJMenuBar(jmb);
		setIconImage(Toolkit.getDefaultToolkit().getImage("notepad.gif"));
		addWindowListener(this);
		setSize(500,500);
		setTitle("Untitled.txt ");
				
		setVisible(true);
            
	}

	public void createMenuItem(JMenu jm,String txt){
		JMenuItem jmi=new JMenuItem(txt);
		jmi.addActionListener(this);
		jm.add(jmi);
	}
	
	public void actionPerformed(ActionEvent e){	
		JFileChooser jfc=new JFileChooser();
		
		/*if(e.getActionCommand().equals("New")){ 
			//new
			this.setTitle("Untitled.txt - Notepad");
			jta.setText("");
			fnameContainer=null;
		}else if(e.getActionCommand().equals("Open")){
			//open
			int ret=jfc.showDialog(null,"Open");
			
			if(ret == JFileChooser.APPROVE_OPTION)
			{
				try{
					File fyl=jfc.getSelectedFile();
					OpenFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+ " - Notepad");
					fnameContainer=fyl;
				}catch(IOException ers){}
			}
			
		}else*/ if(e.getActionCommand().equals("Save")){
			//save
			/*if(fnameContainer != null){
				jfc.setCurrentDirectory(fnameContainer);		
				jfc.setSelectedFile(fnameContainer);
			}
			else {
				//jfc.setCurrentDirectory(new File("."));
				jfc.setSelectedFile(new File("Untitled.txt"));
			}
			
			int ret=jfc.showSaveDialog(null);
				
			if(ret == JFileChooser.APPROVE_OPTION){
				try{
					
					File fyl=jfc.getSelectedFile();
					SaveFile(fyl.getAbsolutePath());
					this.setTitle(fyl.getName()+ " - Notepad");
					fnameContainer=fyl;
					
				}catch(Exception ers2){}*/
            
                
                JSONObject obj = new JSONObject();
                obj.put("Action", "GetContents");
                obj.put("id",inn.get("id"));
                obj.put("session",inn.get("session"));
                obj.put("fileName",filename);
                obj.put("idP",idP);
                obj.put("ctx",jta.getText());
                StringWriter out = new StringWriter();
            try {
                obj.writeJSONString(out);
            } catch (IOException ex) {
                Logger.getLogger(main1.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                //create socket
                s=ss.CreateSocket(ipp,portt);
                if (s.isConnected())
                {
                    System.out.println("Connexion is ready ");}
                    //send json file with socket
                    ss.send(s, String.valueOf(out),null,frame);
                     class Th implements Runnable{
                         
                        @Override
                    public void run() {
                          while(true){
                            JSONObject fileC = new ServerThread().getFileContents();
                            txteditor ttt=null;
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(main1.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(fileC.size()>0){
                                System.out.println("fiiiiiile"+fileC.toJSONString());
                                
                                for(Iterator iterator = fileC.keySet().iterator(); iterator.hasNext();) {
                                    
                                    String ctx = (String) iterator.next();
                                    String value = (String) fileC.get(ctx); 
                                    ttt = new txteditor(value,inn,filename,idP);
                                    fileC.clear();
                                    ttt.setVisible(true);
                                }  
                                JFrame frame = new YesNo("Whould you confirm this changes",inn,filename,idP,jta.getText(),ttt);
                                frame.setVisible(true);
                                return;
                          }
                             fileC.clear();
                    }
                                
                    }
                    
                }
                 Thread tt= new Thread(new Th());
                 tt.start();
                 
                    }
  
		else if(e.getActionCommand().equals("Exit")){
			//exit
			Exiting();
		}else if(e.getActionCommand().equals("Copy")){
			//copy
			jta.copy();
		}else if(e.getActionCommand().equals("Paste")){
			//paste
			jta.paste();
		}/*else if(e.getActionCommand().equals("About Notepad")){ 
			//about
			JOptionPane.showMessageDialog(this,"Created by: Ferdinand Silva (http://ferdinandsilva.com)","Notepad",JOptionPane.INFORMATION_MESSAGE);
		}*/else if(e.getActionCommand().equals("Cut")){
			jta.cut();
		}
	}
	
	public void OpenFile(String fname) throws IOException {	
		//open file code here
		BufferedReader d=new BufferedReader(new InputStreamReader(new FileInputStream(fname)));
		String l;
		//clear the textbox
		jta.setText("");
	
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
		while((l=d.readLine())!= null) {
			jta.setText(jta.getText()+l+"\r\n");
		}
		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		d.close();
	}
	
	public void SaveFile(String fname) throws IOException {
		setCursor(new Cursor(Cursor.WAIT_CURSOR));
		DataOutputStream o=new DataOutputStream(new FileOutputStream(fname));
		o.writeBytes(jta.getText());
		o.close();		
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void windowDeactivated(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	
	public void windowClosing(WindowEvent e) {
		Exiting();
	}
	
	public void windowOpened(WindowEvent e){}
	
	public void Exiting() {
		this.dispose();
	}
	
	/*public static void main (String[] args) {
		txteditor notp=new txteditor(null);	
	}*/
			
}