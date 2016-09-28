package com.optum.bc4.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

	/**
	 * 
	 * @author RKrahl
	 *
	 */
	public class HelperClass {

		private Logger log = LoggerFactory.getLogger(this.getClass());
		
		private final String SECRET_KEY_ENV = "BC4_KEY";
		private final String PROFILE_ENV = "SPRING_PROFILES_BC4";
		private final String APP_PROP_FILE = "./optumbc4.properties";
		

		String profile;
		static SecretKey key;
		static Cipher ecipher;
		static Cipher dcipher;
		String dbServer;
		String dbName;
		String dbUsername;
		String dbPassword;
		int dbServerPort;		
				
		@Test
		public void databaseTest() throws Exception {
			
			
			String secretKeyStr = System.getenv(SECRET_KEY_ENV);
			if (secretKeyStr == null) {
				log.warn("The " + SECRET_KEY_ENV + " Environment Variable Is NOT Set.  Cannot Get DB Properties.");
				log.warn("Setting dssDbServer to null to prevent unexpected access errors");
				dbServer = null;
				return;
			}
			log.info(SECRET_KEY_ENV + " is set in the environment"); 
			
			profile = System.getenv(PROFILE_ENV);
			if (profile == null) {
				Assert.fail(PROFILE_ENV + " is not setup -- can't get properties file info");
				return;
			}
			
			log.info(PROFILE_ENV + " is set in the environment");
			log.info("using profile: " + profile);

			// Load properties
			Properties appProperties = new java.util.Properties();
			InputStream in = getClass().getResourceAsStream(APP_PROP_FILE);
			appProperties.load(in);
			in.close();
	    
			// configure database
			dbServer = appProperties.getProperty("bc4.db_server." + profile);
			if (dbServer == null) {
				log.warn("MySQL DB Server NOT Setup in " + APP_PROP_FILE);
				return;
			}
			log.info("using MySQL DB Server " + dbServer); 
			
			// Convert the secretKeyStr to the actual SecretKey
			byte[] newKeyByte = BASE64DecoderStream.decode(secretKeyStr.getBytes("UTF8"));
			key = new SecretKeySpec(newKeyByte, "DESede");

			// Set the database properties
			String dbServerPortStr = appProperties.getProperty("bc4.db_server_port." + profile);
			try {
				dbServerPort = Integer.parseInt(dbServerPortStr);
				log.info("using MySQL DB Port: " + dbServerPort);
			} catch (Exception e) {
				Assert.fail("dbServerPort is not set in the " + APP_PROP_FILE + " file !!!");
			}
			dbName = appProperties.getProperty("bc4.db_name." + profile);
			log.info("using MySQL DB Name: " + dbName);

			// Get the encrypted username and password
			String encryptedDbUserName = appProperties.getProperty("bc4.db_username." + profile);
			String encryptedDbPassword = appProperties.getProperty("bc4.db_password." + profile);

			String tunnelHost = appProperties.getProperty("bc4.db_tunnel_host." + profile);
			
			dbUsername = decrypt(encryptedDbUserName, key);
			dbPassword = decrypt(encryptedDbPassword, key);			
			
			String dbUrl = "";
			
      if (!(tunnelHost == null)) 
	      dbUrl = setupTunnel();
	    else
	      dbUrl = "jdbc:mysql://" + dbServer + ":" + dbServerPort + "/" + dbName;
								
	 		String query = "SELECT aname FROM account WHERE account_type_uid = '5' LIMIT 1";

			try {				
				Class.forName("com.mysql.jdbc.Driver");
				
				Connection dbConn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
				Statement statement = dbConn.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				while (resultSet.next()) {		        	
					String name = resultSet.getString("aname");
					log.info("name = " + name); 
				}
				dbConn.close();
			} catch (ClassNotFoundException e) {e.printStackTrace();
			} catch (SQLException e) {e.printStackTrace(); } 					
		}

		/**
	   * Method to setup tunneling if situations require it
	   * 
	   * @return JDBC URL to be used while tunneling 
	   * @throws Exception
	   */
	  protected String setupTunnel() throws Exception {        
	    // Load additional properties 
	    Properties appProperties = new java.util.Properties();
	    InputStream in = getClass().getResourceAsStream(APP_PROP_FILE);
	    appProperties.load(in);
	    in.close();
	    
	    
	    // Get the encrypted tunnel username and password
	    String eTunnelDbUserName = appProperties.getProperty("bc4.db_tunnel_user." + profile);
	    String eTunnelDbPassword = appProperties.getProperty("bc4.db_tunnel_password." + profile);
	    
	        
	    // Get the decrypted tunnel username and password
	    String dbTunnelUser = decrypt(eTunnelDbUserName, key);   
	    String dbTunnelPassword = decrypt(eTunnelDbPassword, key); 
	    
	    // Get tunnel port 
	    String dbTunnelPort = appProperties.getProperty("bc4.db_tunnel_port." + profile);  
	    int tunnelPort = Integer.parseInt(dbTunnelPort);
	    String tunnelHost = appProperties.getProperty("bc4.db_tunnel_host." + profile);
	    int localPort = 13306;
	    
          try {
	          JSch jsch = new JSch();
	            
              Session session = jsch.getSession(dbTunnelUser, tunnelHost, tunnelPort);
	            session.setPassword(dbTunnelPassword);
	            session.setConfig("StrictHostKeyChecking", "no");
	      log.info("Establishing Connection...");
	            session.connect();
	            int assigned_port=session.setPortForwardingL(localPort, dbServer, dbServerPort);
	            log.info("localhost:"+assigned_port+" -> "+dbServer+":"+dbServerPort);
	        } catch(Exception e){System.err.print(e);} 
	        return ("jdbc:mysql://localhost" + ":" + localPort + "/" + dbName);
	  }

      @Test
	    public void decryptUsingEnvVariableKey() throws Exception {

	    	String encryptedString = "dtpW/RmJIN6LA8FbUm4e1Q==";
	    	log.info("**> original encrypted string = \"" + encryptedString + "\"");

			// If the SECRET_KEY_ENV is not set, log warning and skip setting db properties
			String keyStr = System.getenv(SECRET_KEY_ENV);
			if (keyStr == null)
				Assert.fail("The dssKey " + SECRET_KEY_ENV + " Environment Variable is not set !!!");
		
			byte[] newKeyByte = BASE64DecoderStream.decode(keyStr.getBytes("UTF8"));		
			SecretKey newkey = new SecretKeySpec(newKeyByte, "DESede");		
			log.info(decrypt(encryptedString, newkey));			
	    } 
	    
		@Test
		public void encryptUsingEnvVariableKey() throws Exception {
	    	String inTheClear = "in the clear"; 
	    	
	    	log.info("**> original in the clear text = \"" + inTheClear + "\"");

			// If the SECRET_KEY_ENV is not set, log warning and skip setting db properties
			String keyStr = System.getenv(SECRET_KEY_ENV);
			if (keyStr == null)
				Assert.fail("The key expected in " + SECRET_KEY_ENV + " Environment Variable is not set !!!");
		
			byte[] newKeyByte = BASE64DecoderStream.decode(keyStr.getBytes("UTF8"));		
			SecretKey newkey = new SecretKeySpec(newKeyByte, "DESede");				
			String encryptedString = encrypt(inTheClear, newkey);   
			log.info("The inTheClear String \"" + inTheClear + "\" Encrypted is \"" + encryptedString + "\"");
		} 
		    
		@Test  /*** WARNING !!!  Only Use this IF you plan to RE-KEY all your encryptions or you're building a new environment ***/
		public void getNewEncryptionKey() throws Exception {
	    	String newEncryptionKey = keyBuilder();
	    	log.info("**> the key is = \"" + newEncryptionKey + "\"");
	    	log.info("***** add this new key to your system environment variables *****");
	    }     	

		/**
		   * Encrypted data is arbitrary binary data which contains "invalid" characters
		   * and is not valid UTF-8 data and cannot be converted conventionally.
		   * Therefore, encrypted data must first be converted to Base64.
		 */
		private static String decrypt(String encryptedString, SecretKey key) throws Exception {

		    dcipher = Cipher.getInstance("DESede");
		    dcipher.init(Cipher.DECRYPT_MODE, key);
		    ;

		    // decode with base64 to get bytes
		    byte[] dec = BASE64DecoderStream.decode(encryptedString.getBytes("UTF8"));
		    byte[] utf8 = dcipher.doFinal(dec);

		    // create new string based on the specified charset
		    return new String(utf8, "UTF8");
		  }

		 /**
		   * Encrypted data is arbitrary binary data which contains "invalid" characters
		   * and is not valid UTF-8 data and cannot be converted conventionally.
		   * Therefore, encrypted data must first be converted to Base64.
		   */
		  private static String encrypt(String inTheClearText, SecretKey key) throws Exception {

		    ecipher = Cipher.getInstance("DESede");
		    ecipher.init(Cipher.ENCRYPT_MODE, key);

		    // encode the string into a sequence of bytes using the named charset
		    // storing the result into a new byte array.
		    byte[] utf8 = inTheClearText.getBytes("UTF8");
		    byte[] enc = ecipher.doFinal(utf8);

		    // encode to base64
		    enc = BASE64EncoderStream.encode(enc);
		    return new String(enc, "UTF8");
		  }
		
		/**
		   * @return - Helper function returns an encrypted base64 String "base64Key" to
		   *         be stored as an environment variable to be used for encrypting and
		   *         decrypting. This "base64Key" key uses DESede with a key size of 168
		   *         bits. DES (The Data Encryption Standard) is now out of date. DESede
		   *         is a triple variant of DES and increases the key space which helps
		   *         prevent brute force attacks. This value must be converted into the
		   *         actual SecretKey as follows:
		   * 
		   *         byte[] newKeyByte =
		   *         BASE64DecoderStream.decode(base64Key.getBytes("UTF8")); key = new
		   *         SecretKeySpec(newKeyByte, "DESede");
		   *
		   */
		  private String keyBuilder() throws Exception {

		    KeyGenerator keyGenerator = KeyGenerator.getInstance("DESede");
		    keyGenerator.init(168);
		    SecretKey secretKey = keyGenerator.generateKey();
		    byte[] encodedKeyByteArray = secretKey.getEncoded();
		    byte[] encodedKeyB64 = BASE64EncoderStream.encode(encodedKeyByteArray);
		    return new String(encodedKeyB64, "UTF8");
		  }	
	}
