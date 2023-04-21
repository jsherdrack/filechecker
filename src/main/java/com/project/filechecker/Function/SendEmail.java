package com.project.filechecker.Function;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendEmail {
    public static void main(String[] args) {
        String to = "j.sherdrack@har.mrc.ac.uk"; // recipient email address
        String from = "autoemailer@mrcharwell.onmicrosoft.com"; // sender email address
        String clientId = "your_client_id"; // client ID for your Azure AD application
        String clientSecret = "your_client_secret"; // client secret for your Azure AD application
        String tenantId = "your_tenant_id"; // tenant ID for your Azure AD tenant
        String subject = "test"; // subject of email
        String body = readFile("Report.txt"); // read contents of file into email body

        // get access token from Azure AD
        String accessToken = getAccessToken(clientId, clientSecret, tenantId);

        // set properties for Office 365 SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.office365.com");

        // create session with OAuth2 authentication
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, accessToken);
            }
        });

        try {
            // create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // send message
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // method to read contents of a file into a string
    public static String readFile(String filename) {
        String content = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                content += line + "\n";
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    // method to get an access token from Azure AD
    public static String getAccessToken(String clientId, String clientSecret, String tenantId) {
        String authority = "https://login.microsoftonline.com/" + tenantId;
        AuthenticationContext context = null;
        AuthenticationResult result = null;
        ExecutorService service = Executors.newFixedThreadPool(1);
        try {
            context = new AuthenticationContext(authority, true, service);
            result = context.acquireToken("https://graph.microsoft.com", new ClientCredential(clientId, clientSecret), null).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result == null) {
            throw new RuntimeException("Access token not found");
        }
        return result.getAccessToken();
    }
}





//public class SendEmail {
//    public static void main(String[] args) {
//        String to = "j.sherdrack@har.mrc.ac.uk"; // recipient email address
//        String from = "autoemailer@mrcharwell.onmicrosoft.com"; // sender email address
//        String password = "FileCheck123"; // sender email password
//        String subject = "test"; // subject of email
//        String body = readFile("C:\\Users\\adm-j.sherdrack\\Documents\\filechecker-master\\report.txt"); // read contents of file into email body
//
//        // set properties for Office 365 SMTP server
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.office365.com");
//        props.put("mail.smtp.port", "587");
//
//        // create session with authentication
//        Session session = Session.getInstance(props, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(from, password);
//            }
//        });
//
//        try {
//            // create message
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(from));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            message.setSubject(subject);
//            message.setText(body);
//
//            // send message
//            Transport.send(message);
//
//            System.out.println("Email sent successfully!");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    // method to read contents of a file into a string
//    public static String readFile(String filename) {
//        String content = "";
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader(filename));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                content += line + "\n";
//            }
//            reader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return content;
//    }
//}
//public class SendEmail {
//    public static String USER_NAME = "autoemailer@mrcharwell.onmicrosoft.com";  //user name
//    public static String PASSWORD = "FileCheck123"; //  password
//    public static String[] RECIPIENT = {"j.sherdrack@har.mrc.ac.uk"}; //list of recipients
//    static String strLine = null;
//    public static StringBuffer mailString = new StringBuffer("");
//
//
//    /**
//     * Function to connect to the Office 365 mail server with the proposed variables
//     */
//    public static void sendFromOutlook(String from, String pass, String[] to, String subject, String body) {
//        Properties props = System.getProperties();
//        String host = "outlook.office365.com";
//        props.put("mail.imap.ssl.enable", "true");
//        props.put("mail.store.protocol", "imap");
//        props.put("mail.imap.starttls.enable", "true");
//        props.put("mail.imap.host", host);
//        props.put("mail.imap.user", from);
//        props.put("mail.imap.password", pass);
//        props.put("mail.imap.port", "993");
//        props.put("mail.debug", "true");
//        props.put("mail.debug.auth", "true");
//        props.put("mail.imap.auth", "true");
//        props.put("mail.imap.auth.mechanisms", "XOAUTH2");
//
//        Session session = Session.getDefaultInstance(props);
//        MimeMessage message = new MimeMessage(session);
//
//        try {
//            message.setFrom(new InternetAddress(from));
//            InternetAddress[] toAddress = new InternetAddress[to.length];
//
//            // To get the array of addresses
//            for( int i = 0; i < to.length; i++ ) {
//                toAddress[i] = new InternetAddress(to[i]);
//            }
//
//            for( int i = 0; i < toAddress.length; i++) {
//                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
//            }
//
//            message.setSubject(subject);
//            message.setText(body);
//            Store store = session.getStore("imap");
//            store.connect(host, from, pass);
//        //    store.sendMessage(message, message.getAllRecipients());
//          //  store.close();
//        }
//        //Let's you know if there is an error and highlights the issue
//        catch (AddressException ae) {
//            ae.printStackTrace();
//        }
//        catch (MessagingException me) {
//            me.printStackTrace();
//        }
//    }
//
//    /**
//    *Function to read given txt file
//     */
//    public static void readFile(){
//        try{
//            // Open the file that is the first
//            // command line parameter
//            FileInputStream fstream = new FileInputStream("C:\\Users\\adm-j.sherdrack\\Documents\\filechecker-master\\Report.txt");
//            // Get the object of DataInputStream
//            DataInputStream in = new DataInputStream(fstream);
//            BufferedReader br = new BufferedReader(new InputStreamReader(in));
//
//            //Read File Line By Line
//            while ((strLine = br.readLine()) != null)   {
//                // Print the content on the console
//                mailString.append(strLine + "\n");
//            }
//            //Close the input stream
//            in.close();
//        }catch (Exception e){//Catch exception if any
//            System.err.println("Error: " + e.getMessage());
//        }
//    }
//}
