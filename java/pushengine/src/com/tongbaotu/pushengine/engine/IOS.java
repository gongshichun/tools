package com.tongbaotu.pushengine.engine;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.*;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import com.tongbaotu.pushengine.define.Define;
import com.tongbaotu.pushengine.exception.MyException;

public class IOS {
	private String keyPath = null;
	private String ksPassword = null;
	private String serverHost = null;
	private Socket socket = null;
	private Socket feedBackSocket = null; 
    private final int serverPort = 2195;
	private String ksType = "PKCS12";
	private String ksAlgorithm = "SunX509";
	private static final int FEEDBACK_TUPLE_SIZE = 38;
 
	 //初始化
	public IOS() throws MyException {
		Properties props = new Properties();

		try {
	        props.load(new FileInputStream(Define.CONFIG_PATH + "ios_push.properties"));
	        
	    } catch (IOException e) {
	    	throw new MyException("ymhhouException redis.properties IOException:" + e.getMessage());
		}

		this.ksPassword = props.getProperty("password");

		if(props.getProperty("environment").equals("product")) {
			this.keyPath  = Define.CONFIG_PATH + props.getProperty("certificate");
			this.serverHost = props.getProperty("serverHost");
		} else if(props.getProperty("environment").equals("test")) {
			this.keyPath  = Define.CONFIG_PATH + props.getProperty("testCertificate");
			System.out.println(keyPath + "22");
			this.serverHost = props.getProperty("testServerHost");
		}
		createClient();
//		createFeedBackClient();
	
		
	}

	public void send(String message, String deviceToken) {
		this.send(message,  deviceToken, "1");
	}

	public void send(String message, String deviceToken, String badge) {
		try {
	        StringBuilder content = new StringBuilder();
	        content.append("{\"aps\":");
	        content.append("{\"alert\":\"").append(message)
//	        	.append("\",\"badge\":1,\"sound\":\"")
	            .append("\",\"badge\":"+ badge +",\"sound\":\"")
	            .append("ping1").append("\"}");
	        content.append(",\"cpn\":{\"t0\":")
	            .append(System.currentTimeMillis()).append("}");
	        content.append("}");
	        byte[] msgByte = makebyte((byte)1, deviceToken, content.toString(), 10000001);
	        this.write(msgByte);
		} catch (Exception e) {
			System.out.println("client is broken");
		}
	}
	
	//建立链接
    private void createClient() {

		try{
			InputStream certInput = new FileInputStream(keyPath);
	        KeyStore keyStore = KeyStore.getInstance(ksType);
	        keyStore.load(certInput, ksPassword.toCharArray());

	        KeyManagerFactory kmf = KeyManagerFactory.getInstance(ksAlgorithm);
	        kmf.init(keyStore, ksPassword.toCharArray());
	         
	        SSLContext sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(kmf.getKeyManagers(), null, null);
	         
	        SSLSocketFactory socketFactory = sslContext.getSocketFactory();
	        this.socket = socketFactory.createSocket(serverHost, serverPort);
		} catch (Exception e) {
			System.out.println("eror");
		}
    }

    private void write(byte[] msgByte) {
    	try {
    		this.socket.getOutputStream().write(msgByte);
    		this.socket.getOutputStream().flush();
    		socket.getOutputStream().flush();
    	} catch (Exception e) {
    		this.createClient();
    		this.write(msgByte);
    	}	  
    }
    
    private void createFeedBackClient() {
    	try {
    		InputStream certInput = new FileInputStream(keyPath);
            KeyStore keyStore = KeyStore.getInstance(ksType);
            keyStore.load(certInput, ksPassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(ksAlgorithm);
            kmf.init(keyStore, ksPassword.toCharArray());
             
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
             
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            this.feedBackSocket = socketFactory.createSocket("feedback.push.apple.com", 2196);
    	} catch (Exception e) {
    		//to do 
    	}
    
    }
    
    public boolean checkToken(String deviceToken) {
    	boolean ret = true;
    	String newToKen = "";
    	this.createFeedBackClient();
    	System.out.println(this.feedBackSocket);
    	System.out.println(this.socket);
    	try {

    		System.out.println();
    		InputStream socketStream = feedBackSocket.getInputStream();
    		// Read bytes        
			byte[] b = new byte[1024];
			ByteArrayOutputStream message = new ByteArrayOutputStream();
			int nbBytes = 0;
			// socketStream.available can return 0
			// http://forums.sun.com/thread.jspa?threadID=5428561
			System.out.println(socketStream.read(b));
			
			while ((nbBytes = socketStream.read(b, 0, 1024)) != -1) {
				System.out.println("werwrrw");
				message.write(b, 0, nbBytes);
			}
			
			byte[] listOfDevices = message.toByteArray();
			int nbTuples = listOfDevices.length / FEEDBACK_TUPLE_SIZE;
			System.out.println(listOfDevices);
			System.out.println(socketStream.available()+ "+++" + listOfDevices.length);
			for (int i = 0; i < nbTuples; i++) {
				int offset = i * FEEDBACK_TUPLE_SIZE;

				// Build date
				int index = 1;
				int firstByte = 0;
				int secondByte = 0;
				int thirdByte = 0;
				int fourthByte = 0;
				long anUnsignedInt = 0;

				firstByte = (0x000000FF & ((int) listOfDevices[offset]));
				secondByte = (0x000000FF & ((int) listOfDevices[offset + 1]));
				thirdByte = (0x000000FF & ((int) listOfDevices[offset + 2]));
				fourthByte = (0x000000FF & ((int) listOfDevices[offset + 3]));
				index = index + 4;
				anUnsignedInt = ((long) (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte)) & 0xFFFFFFFFL;
				// Build device token length

				int deviceTokenLength = listOfDevices[offset + 4] << 8 | listOfDevices[offset + 5];
	
				// Build device token
				int octet = 0;
				for (int j = 0; j < 32; j++) {
					octet = (0x000000FF & ((int) listOfDevices[offset + 6 + j]));
					newToKen = newToKen.concat(String.format("%02x", octet));
				}
				System.out.println(newToKen);
			}
    		
 
    	} catch (Exception e) {
    		System.out.println("write failed +++");
//    		this.createClient();
//    	
    	}
		return ret;	  
	}
	

    /**
     * 组装apns规定的字节数组  使用增强型
     * 
     * @param command
     * @param deviceToken
     * @param payload
     * @return
     * @throws IOException
     */
    private static byte[] makebyte(byte command, String deviceToken, String payload, int identifer) {
         
        byte[] deviceTokenb = decodeHex(deviceToken);
        byte[] payloadBytes = null;
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(boas);
 
        try {
            payloadBytes = payload.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
         
        try {
            dos.writeByte(command);
            dos.writeInt(identifer);//identifer
            dos.writeInt(Integer.MAX_VALUE);
            dos.writeShort(deviceTokenb.length);
            dos.write(deviceTokenb);
            dos.writeShort(payloadBytes.length);
            dos.write(payloadBytes);
            return boas.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static final Pattern pattern = Pattern.compile("[ -]");
    private static byte[] decodeHex(String deviceToken) {
        String hex = pattern.matcher(deviceToken).replaceAll("");
         
        byte[] bts = new byte[hex.length() / 2];
        for (int i = 0; i < bts.length; i++) {
            bts[i] = (byte) (charval(hex.charAt(2*i)) * 16 + charval(hex.charAt(2*i + 1)));
        }
        return bts;
    }
 
    private static int charval(char a) {
        if ('0' <= a && a <= '9')
            return (a - '0');
        else if ('a' <= a && a <= 'f')
            return (a - 'a') + 10;
        else if ('A' <= a && a <= 'F')
            return (a - 'A') + 10;
        else{
            throw new RuntimeException("Invalid hex character: " + a);
        }
    }

}
