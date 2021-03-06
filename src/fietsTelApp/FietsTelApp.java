package fietsTelApp;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;



import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Dimension;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import com.sun.jna.NativeLibrary;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.*;



@SuppressWarnings("serial")
public class FietsTelApp extends JFrame {
	
	

	private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:/Program Files/VideoLAN/VLC";
//	private static final String Final = null;
    private final JFrame frame;
    private static EmbeddedMediaPlayerComponent mediaPlayerComponent;
    final int voetgangerlr =0;
         int voetgangerrl =0;
         int fietslr =0;
         int fietsrl =0;
         int autolr =0;
         int autorl =0;
         int camionlr =0;
         int camionrl =0;
    int rateMultiplier = 10;
    //private ArrayList <Tel> voetgangers;
    private ArrayList <Tel> fietsers;
    //private ArrayList <Tel> autos;
    //private ArrayList <Tel> camions;
    JFileChooser fc;
    float rate;
    
    String adres;
    String straat;
    String huisnummer;
    String postcode;
    String gemeente;
    
    float latitude;
    float longitude;
    
    String someDate;
	 SimpleDateFormat sdf;
	 SimpleDateFormat sdf2;
	 SimpleDateFormat sdf3;
	 SimpleDateFormat stf;
	 SimpleDateFormat weekday;
	 Date date;
	 

    // dit stukje haalt de VLC library op
    public static void main(String[] args) {
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
					try {
						new FietsTelApp();
					} catch (ParseException e) {
						// TODO auto-generated catch block
						e.printStackTrace();
					}
            }
        });
        //System.out.println(LibVlc.INSTANCE.libvlc_get_version());
    }
    
	public FietsTelApp() throws ParseException {
    		
    	 
    	//datum en uur formatering:
    	someDate = "2018-08-01 07:00:00";
    	sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    	sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	stf = new SimpleDateFormat("HH:mm:ss");
    	weekday = new SimpleDateFormat("F");
    	date = sdf.parse(someDate);
    	 //System.out.println(date.getTime());
    	 //System.out.println(sdf.format(date));
    	 //System.out.println(weekday.format(date));
    	// einde datum en uur formatering
    	 
    	//Arraylists voor de gegevens    	 
    	// voetgangers  = new ArrayList<Tel>();
    	 fietsers = new ArrayList<Tel>();
    	// autos = new ArrayList<Tel>();
    	// camions = new ArrayList<Tel>();
    	 
    	 //vals adres indien er iets misloopt met het invullen van het adres
    	 straat ="valsestraat";
    	 huisnummer = "0";
    	 postcode = "0000";
    	 gemeente = "Onbestaandegemeente";
    	 adres = straat + " " + huisnummer + " "+ postcode +" " + gemeente;
    	 
    	    
    	 latitude = (float) 50.640175; //geografisch centrum van Belgi�
    	 longitude = (float) 4.666657; //geografisch centrum van Belgi�
         
    	
         
//Gui 
    	 
    	 	frame = new JFrame("FietsTelApp");
            frame.setBounds(100, 100, 900, 600);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    mediaPlayerComponent.release();
                    System.exit(0);
                }
            });
            
           
            JPanel contentPane = new JPanel();
            //contentPane.addKeyListener(new ToetsenbordHandler());
            contentPane.setLayout(new BorderLayout());
            JPanel rightPane = new JPanel();
            rightPane.setLayout(new BorderLayout());
            //rightPane.addKeyListener(new ToetsenbordHandler());
            JPanel countbuttonPane = new JPanel();
            countbuttonPane.setLayout(new GridLayout(2,2,5,10)); //verander gridlayout als voetgangers enz toegevoetd worden.
            //countbuttonPane.addKeyListener(new ToetsenbordHandler());
            JPanel exportPane = new JPanel();
            //exportPane.setLayout(new GridLayout());
            JPanel textPane = new JPanel();
            JPanel videoPane = new JPanel();
            videoPane.setLayout(new BorderLayout());
            
            contentPane.add(videoPane, BorderLayout.WEST);
            contentPane.add(rightPane, BorderLayout.EAST);
            
                        
            mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
            videoPane.add(mediaPlayerComponent, BorderLayout.CENTER);
            JPanel controlsPane = new JPanel();
            videoPane.add(controlsPane, BorderLayout.SOUTH); 
            
            rate =  mediaPlayerComponent.getMediaPlayer().getRate();
            //videoknoppen
            
                                
            JButton pauseButton = new JButton("Play/Pause");
            controlsPane.add(pauseButton);
            JButton rewindButton = new JButton("Rewind");
            controlsPane.add(rewindButton);
            JButton slowdownButton = new JButton("<<");
            controlsPane.add(slowdownButton);
            JLabel rateLabel = new JLabel ("" + rateMultiplier* rate+ "X");
            controlsPane.add(rateLabel);
            JButton speedupButton = new JButton(">>");
            controlsPane.add(speedupButton);
            JButton skipButton = new JButton("Skip");
            controlsPane.add(skipButton);
            
            // telknoppen   
          /*  //voetgangers
            JLabel voetgangerlrCountText = new JLabel (""+voetgangerlr);
            countbuttonPane.add(voetgangerlrCountText);
            voetgangerlrCountText.setHorizontalAlignment(JLabel.CENTER);
            JLabel voetgangerrlCountText = new JLabel (""+voetgangerrl);
            countbuttonPane.add(voetgangerrlCountText);
            voetgangerrlCountText.setHorizontalAlignment(JLabel.CENTER);
            JButton voetgangerlrButton= new JButton ("voetganger van links naar rechts");
            countbuttonPane.add(voetgangerlrButton);
            JButton voetgangerrlButton= new JButton ("voetganger van rechts naar links");
            countbuttonPane.add(voetgangerrlButton);
           */ 
            //fietsers
            JLabel fietslrCountText = new JLabel (""+fietslr);
            countbuttonPane.add(fietslrCountText);
            fietslrCountText.setHorizontalAlignment(JLabel.CENTER);
            JLabel fietsrlCountText = new JLabel (""+fietsrl);
            countbuttonPane.add(fietsrlCountText);
            fietsrlCountText.setHorizontalAlignment(JLabel.CENTER);
            JButton fietslrButton= new JButton ("Fiets van links naar rechts");
            countbuttonPane.add(fietslrButton);
            JButton fietsrlButton= new JButton ("Fiets van rechts naar links");
            countbuttonPane.add(fietsrlButton);
          /*  
            //autos
            JLabel autolrCountText = new JLabel (""+autolr);
            countbuttonPane.add(autolrCountText);
            autolrCountText.setHorizontalAlignment(JLabel.CENTER);
            JLabel autorlCountText = new JLabel (""+autorl);
            countbuttonPane.add(autorlCountText);
            autorlCountText.setHorizontalAlignment(JLabel.CENTER);
            JButton autolrButton= new JButton ("auto van links naar rechts");
            countbuttonPane.add(autolrButton);
            JButton autorlButton= new JButton ("auto van rechts naar links");
            countbuttonPane.add(autorlButton);
            
            //vrachtwagens
            JLabel autolrCountText = new JLabel (""+autolr);
            countbuttonPane.add(autolrCountText);
            autolrCountText.setHorizontalAlignment(JLabel.CENTER);
            JLabel autorlCountText = new JLabel (""+autorl);
            countbuttonPane.add(autorlCountText);
            autorlCountText.setHorizontalAlignment(JLabel.CENTER);
            JButton autolrButton= new JButton ("auto van links naar rechts");
            countbuttonPane.add(autolrButton);
            JButton autorlButton= new JButton ("auto van rechts naar links");
            countbuttonPane.add(autorlButton);
            */
            rightPane.add(countbuttonPane, BorderLayout.NORTH);
            
            //klad tekst
            
            JTextArea kladTekst= new JTextArea(10,30);
            JScrollPane scrollPane = new JScrollPane(kladTekst);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            textPane.add(scrollPane);
            kladTekst.setMargin(new Insets(5,5,5,5));
            kladTekst.setEditable(false);
            kladTekst.append("U kan gebruik maken van sneltoetsen:"+ "\n");
            kladTekst.append("- spatiebalk zet de video op pauze of play"+ "\n");
            kladTekst.append("- de pijltjes links en recht laten de video voorruit of achteruit springen"+ "\n");
            kladTekst.append("- de pijltjes naar boven en onder versnellen/vertragen de video"+ "\n");
            kladTekst.append("- getal 7 telt een fietser van links naar rechts"+ "\n");
            kladTekst.append("- getal 8 telt een fietser van rechts naar links"+ "\n");
            
            rightPane.add(textPane, BorderLayout.CENTER);
            
            
            //export
            
            JButton exportcsvButton = new JButton("Export .csv");
            exportPane.add(exportcsvButton);
           /* JButton UploadButton = new JButton("upload");
            exportPane.add(uploadButton);*/
            rightPane.add(exportPane, BorderLayout.SOUTH);
            
//end GUI
//Actions!!
            Action pauseAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	mediaPlayerComponent.getMediaPlayer().pause();
                }
            };
            Action rewindAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	mediaPlayerComponent.getMediaPlayer().skip(-10000);
                }
            };
            Action skipAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	mediaPlayerComponent.getMediaPlayer().skip(10000);
                }
            };
            Action slowdownAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	float rate =  mediaPlayerComponent.getMediaPlayer().getRate();
                	//System.out.println(rate);
                	//rate= (float) (rate * 0.5);
                	if (rate == 16 || rate == 8 || rate ==2) {
                	rate = (float) (rate*0.5);
                	}else if ( rate == 4 ){
                		rate = 3;
                	}else if ( rate == 3 ){
                		rate = 2;
                	}else if ( rate == 1 ){
                		rate = (float) 0.75;
                	}else if ( rate == 0.75 ){
                		rate = (float) 0.5;
                	}else if ( rate == 0.5 ){
                		rate = (float) 0.25;
                	}else if ( rate == 0.25 ){
                		rate = (float) 0.1;
                	}
                	//System.out.println(rate);
                	mediaPlayerComponent.getMediaPlayer().setRate(rate);
                	rateLabel.setText(rate*rateMultiplier+ "X");
                	
                	
                }
            };
            Action speedupAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	
                	//System.out.println(rate);
                	//rate= (float) (rate * 2);
                	if (rate == 8 || rate ==4 ||  rate == 1) {
                	rate = (float) (rate*2);
                	}else if (rate == 2 ){
                		rate = 3;
                	}else if ( rate == 3 ){
                		rate = 4;
                	}else if ( rate == 0.75 ){
                	}else if ( rate == 0.5 ){
                		rate = (float) 0.75;
                	}else if ( rate == 0.25 ){
                		rate = (float) 0.50;
                	}else if (rate <= 0.25) {
                		rate = (float) 0.25;
                	}
                	
                	//System.out.println(rate);
                	mediaPlayerComponent.getMediaPlayer().setRate(rate);
                	rateLabel.setText(rate*rateMultiplier+ "X");
                }
            };
            
            Action fietslrAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    fietslr ++;
                    long videotime = mediaPlayerComponent.getMediaPlayer().getTime()*rateMultiplier;				
                 	long time = date.getTime() + videotime;
                    Tel tel = new Tel (time, "heen", "fiets");
                    kladTekst.append("fiets van links naar rechts " + sdf2.format(time) +" " + stf.format(time) + "\n");
                    fietsers.add(tel);
                    fietslrCountText.setText("" + fietslr);
                }
            };
            Action fietsrlAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	fietsrl ++;
 	             	long videotime = mediaPlayerComponent.getMediaPlayer().getTime()*rateMultiplier;				
 	             	long time = date.getTime() + videotime;
 	             	Tel tel = new Tel (time, "terug", "fiets");
 	             	kladTekst.append("fiets van rechts naar links " + sdf2.format(time) +" " + stf.format(time) + "\n");
 					fietsers.add(tel);
 					fietsrlCountText.setText("" + fietsrl);
                }
            };
            
            Action exportCsvAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	try {
                		String tijd = sdf3.format(date);
                		String bestandsnaam = adres  + " "+ tijd + ".csv";
                		OutputStream os = new FileOutputStream(bestandsnaam);
                		            		
                		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os,"UTF-8") );
                        bw.write("Locatie-id;adres;latitude;longitude;richting;methode;kwaliteit;periode-van;periode-tot;weekdag;tijd-van;tijd-tot;per;fiets;fiets-heen;fiets-terug");//csv header
                        bw.newLine();
                        for (Tel object: fietsers) {                            
                            bw.write(object.getCsvLine());
                            bw.newLine();
                        }
                        bw.flush();
                        bw.close();
                        JOptionPane.showMessageDialog(null,"bestand" + bestandsnaam + "is weggeschreven naar de folder waar het programma zich bevindt");
                    } catch (UnsupportedEncodingException e1) {
                    } catch (FileNotFoundException e1) {
                    } catch (IOException e1) {
                    }
                }
            };

// end Actions            
            

//listeners voor de knoppen
   //videoknoppen
                       
            pauseButton.addActionListener(pauseAction);
            rewindButton.addActionListener(rewindAction);
            slowdownButton.addActionListener(slowdownAction);
            speedupButton.addActionListener(speedupAction);
            skipButton.addActionListener(skipAction);

   //telknoppen
            //voetgangerlrButton.addActionListener(voetgangerlrAction);
            //voetgangerrlButton.addActionListener(voetgangerrlAction);
            fietslrButton.addActionListener(fietslrAction);
            fietsrlButton.addActionListener(fietsrlAction);
            //autolrButton.addActionListener(autolrAction);
            //autorlButton.addActionListener(autorlAction);
            //camionlrButton.addActionListener(camionlrAction);
            //camionrlButton.addActionListener(camionrlAction);
            
            exportcsvButton.addActionListener(exportCsvAction);
            
//einde listeners knoppen

//keybindings
            
   //telbindings
            InputMap im = contentPane.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = contentPane.getActionMap();
            im.put(KeyStroke.getKeyStroke("NUMPAD7"),"7");
            am.put("7", fietslrAction);
            im.put(KeyStroke.getKeyStroke("NUMPAD8"),"8");
            am.put("8", fietsrlAction);
            
  //videobindings
            im.put(KeyStroke.getKeyStroke("RIGHT"),"right");
            am.put("right", skipAction);
            im.put(KeyStroke.getKeyStroke("LEFT"),"left");
            am.put("left", rewindAction);
            im.put(KeyStroke.getKeyStroke("UP"),"up");
            am.put("up", speedupAction);
            im.put(KeyStroke.getKeyStroke("DOWN"),"down");
            am.put("down", slowdownAction);
            im.put(KeyStroke.getKeyStroke("SPACE"),"space");
            am.put("space", pauseAction);
            

//einde keybindings
            
//mediaplayer events
            
            mediaPlayerComponent.getMediaPlayer().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                @Override
                public void playing(MediaPlayer mediaPlayer) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            frame.setTitle(String.format(
                                "Tellapp - %s",
                                mediaPlayerComponent.getMediaPlayer().getMediaMeta().getTitle()
                            ));
                        }
                    });
                }

                @Override
                public void finished(MediaPlayer mediaPlayer) {
                   
                }

                @Override
                public void error(MediaPlayer mediaPlayer) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            JOptionPane.showMessageDialog(
                                frame,
                                "Failed to play media",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                            closeWindow();
                        }
                    });
                }
            });     
            
//einde mediaplayer events
            frame.setContentPane(contentPane);
            frame.setVisible(true);
               
            JOptionPane.showMessageDialog(null,"selecteer een videobestand");
            fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);
            File file = null;
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                kladTekst.append("Video geselecteerd: " + file.getPath() + "." + "\n");
                
            } else {
            	kladTekst.append("Geen video geselecteerd." + "\n");
            }
            
            mediaPlayerComponent.getMediaPlayer().prepareMedia(file.getPath());
            //mediaPlayerComponent.getMediaPlayer().prepareMedia("file:///");
            //video afspelen, op pauze zetten en terugspoelen om niet te starten met een zwart scherm maar met de eerste frame. lelijk maar het werkt :)
            mediaPlayerComponent.getMediaPlayer().play();
            try {
            	
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            mediaPlayerComponent.getMediaPlayer().pause();
            mediaPlayerComponent.getMediaPlayer().skip(-1000);
            //einde hack voor eerste frame juist te zetten
            
            
            
            JTextField straatField = new JTextField(15);
            JTextField huisnummerField = new JTextField(15);
            JTextField postcodeField = new JTextField(15);
            JTextField gemeenteField = new JTextField(15);
            JTextField lengtegraadField = new JTextField(15);
            lengtegraadField.setText("50.640175");//geografisch centrum van Belgi�
            JTextField breedtegraadField = new JTextField(15);
            breedtegraadField.setText("4.666657");//geografisch centrum van Belgi�
            JTextField rateField = new JTextField(15);
            rateField.setText("1");
            JTextField tijdField = new JTextField(15);
            tijdField.setText("2018-08-01 07:00:00");

            JPanel locatiePanel = new JPanel();
            locatiePanel.setLayout(new GridLayout(8,2,10,10));
            
            locatiePanel.add(new JLabel("straatnaam:"));
            locatiePanel.add(straatField);
            locatiePanel.add(new JLabel("huisnummer:"));
            locatiePanel.add(huisnummerField);
            locatiePanel.add(new JLabel("postcode:"));
            locatiePanel.add(postcodeField);
            locatiePanel.add(new JLabel("gemeente:"));
            locatiePanel.add(gemeenteField);
            locatiePanel.add(new JLabel("afspeelsnelheid:"));
            locatiePanel.add(rateField);
            locatiePanel.add(new JLabel("breedtegraad:"));
            locatiePanel.add(breedtegraadField);
            locatiePanel.add(new JLabel("lengtegraad:"));
            locatiePanel.add(lengtegraadField);
            locatiePanel.add(new JLabel("starttijd video:"));
            locatiePanel.add(tijdField);

            
            int result = JOptionPane.showConfirmDialog(null, locatiePanel, 
                     "Geef een adres en de coordinaten op.", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                
                straat = straatField.getText();
                huisnummer = huisnummerField.getText();
                postcode = postcodeField.getText();
                gemeente = gemeenteField.getText();
                
                adres = straat + " " + huisnummer + " "+ postcode +" " + gemeente;
                
                // hier moet nog een errordetectie komen voor de float.
                latitude =  Float.parseFloat( breedtegraadField.getText());
                longitude = Float.parseFloat(lengtegraadField.getText());
                
                someDate = tijdField.getText();
                date = sdf.parse(someDate);
                
                kladTekst.append(adres + "\n");
                kladTekst.append("breedtegraad: "+ latitude + " lengtegraad: " + longitude + "\n");
                kladTekst.append ("afspeelsnelheid: " + rate + "\n");
                kladTekst.append ("starttijd video: " + someDate + "\n");
                
 
            }
            
                        
           

        }
     

	private void closeWindow() {
         frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
     }

    /* public static EmbeddedMediaPlayerComponent getmcp() {
    	 return  mediaPlayerComponent;
     }*/
     
////////////////////////////////////////

    public class Tel{
    	private long time;
    	private String richting;
    	private String voertuig;
    	private String lijn;
    
     public Tel (long time, String richting, String voertuig ) {
    	 this.time = time;
    	 this.richting = richting;
    	 this.voertuig = voertuig;	
     }
     
     public String getCsvLine() {
    	 adres = straat + " " + huisnummer + " "+ postcode +" " + gemeente;
    	 if (richting == "heen") { 
    		lijn = ";"+ adres +";"+ latitude + ";" + longitude +";"+";"+"visueel"+";"+"90"+";"+ sdf2.format(time)+";"+sdf2.format(time)+";"+ weekday.format(time) +";" + stf.format(time) +";" + stf.format(time) +";" + "0" +";" + "1"+";" +  "1" +";" + "0"  ;	
    	 } else {
    	    lijn = ";"+ adres +";"+ latitude + ";" + longitude +";"+";"+"visueel"+";"+"90"+";"+ sdf2.format(time)+";"+sdf2.format(time)+";"+ weekday.format(time) +";" + stf.format(time) +";" + stf.format(time) +";" + "0" +";" + "1"+";" +  "0" +";" + "1"  ;
    	 }
    	return lijn;
    	 
     }
     
    }
    
// example for CKAN upload. Doesn't do anything yet.
    
public static String uploadFile()
    {
        String myApiKey="112e0480-7d1d-4529-a094-fcba7eecaede";
        String uploadFileName="/path/to/file.ext";
        String HOST="http://dataplatform-proto.ugent.be";
        String line;
        StringBuilder sb = new StringBuilder();
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        File file = new File(uploadFileName);   
        SimpleDateFormat dateFormatGmt = new  SimpleDateFormat("yyyyMMdd_HHmmss");
        String date=dateFormatGmt.format(new Date()); 

        HttpPost postRequest;
        file = new File(uploadFileName);
        try {

          ContentBody cbFile = new FileBody(file, ContentType.TEXT_HTML);
          HttpEntity reqEntity = MultipartEntityBuilder.create()
           .addPart("file", cbFile)
           .addPart("key", new StringBody(uploadFileName+date,ContentType.TEXT_PLAIN))
           .addPart("package_id",new StringBody("dataSetName",ContentType.TEXT_PLAIN))
           .addPart("url",new StringBody("path/to/save/dir",ContentType.TEXT_PLAIN))
           .addPart("upload",cbFile)
           .addPart("comment",new StringBody("comments",ContentType.TEXT_PLAIN))
           .addPart("notes", new StringBody("notes",ContentType.TEXT_PLAIN))
           .addPart("author",new StringBody("AuthorName",ContentType.TEXT_PLAIN))
           .addPart("author_email",new StringBody("AuthorEmail",ContentType.TEXT_PLAIN))
           .addPart("title",new StringBody("title",ContentType.TEXT_PLAIN))
           .addPart("description",new StringBody("file Desc"+date,ContentType.TEXT_PLAIN))       
           .build();

          postRequest = new HttpPost(HOST+"/api/action/resource_create");
               postRequest.setEntity(reqEntity);
               postRequest.setHeader("X-CKAN-API-Key", myApiKey);

          HttpResponse response = httpclient.execute(postRequest);
          int statusCode = response.getStatusLine().getStatusCode();
          BufferedReader br = new BufferedReader(
                  new InputStreamReader((response.getEntity().getContent())));

          sb.append(statusCode+"\n");
          if(statusCode!=200){
             //System.out.println("statusCode =!=" +statusCode);           
          }
          else System.out.println("OK"); 

           while ((line = br.readLine()) != null) {
              sb.append(line+"\n");
            //System.out.println("+"+line);
          }

          httpclient.close();
          return sb.toString();
   }catch (IOException ioe) {
   //System.out.println(ioe);
   return "error"+ioe;
   } finally {
   httpclient.getConnectionManager().shutdown();

   }
}
}
    
     
        

