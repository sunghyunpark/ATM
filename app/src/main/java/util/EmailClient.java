package util;


import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class EmailClient
{
    private String mMailHost = "smtp.gmail.com";
    private Session mSession;

    public EmailClient(String user, String pwd)
    {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", mMailHost);
        mSession = Session
                .getInstance(props, new EmailAuthenticator(user, pwd));
    } // constructor

    public void sendMail(String subject, String body, String sender,
                         String recipients)
    {
        try
        {
            Message msg = new MimeMessage(mSession);
            msg.setFrom(new InternetAddress(sender));
            msg.setSubject(subject);
            msg.setContent(body, "text/html;charset=EUC-KR");
            msg.setSentDate(new Date());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    recipients));
            Transport.send(msg);
        } catch (Exception e)
        {
            Log.d("lastiverse", "Exception occured : ");
            Log.d("lastiverse", e.toString());
            Log.d("lastiverse", e.getMessage());
        } // try-catch
    } // vodi sendMail

    public void sendMailWithFile(Context mcontext, String type, String subject, String body, String sender,
                                 String recipients, String filePath, String fileName)
    {
        try
        {
            Message msg = new MimeMessage(mSession);
            msg.setFrom(new InternetAddress(sender));
            msg.setSubject(subject);
            msg.setContent(body, "text/html;charset=EUC-KR");
            msg.setSentDate(new Date());
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(
                    recipients));

            Multipart multipart=new MimeMultipart();

            if(filePath != null){
                if(fileSizeCheck(filePath)){
                    MimeBodyPart mbp2 = new MimeBodyPart();
                    FileDataSource fds = new FileDataSource(filePath);
                    mbp2.setDataHandler(new DataHandler(fds));
                    mbp2.setFileName(MimeUtility.encodeText(fds.getName(), "UTF-8", "B"));

                    multipart.addBodyPart(mbp2);
                }else{
                    throw new Exception("file size overflow !");
                }
            }

            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mc);

            //이메일 내용
            BodyPart messageBodyPart = new MimeBodyPart();
            /*
            TelephonyManager telephony=(TelephonyManager)mcontext.getSystemService(Context.TELEPHONY_SERVICE);
            messageBodyPart.setText(Build.BOARD+"\n"+Build.BRAND+"\n"+Build.CPU_ABI+"\n"+Build.DEVICE+"\n"+Build.DISPLAY+"\n"+Build.FINGERPRINT+"\n"+Build.HOST+"\n"
                    +Build.ID+"\n"+Build.MANUFACTURER+"\n"+Build.MODEL+"\n"+Build.PRODUCT+"\n"+Build.TAGS+"\n"+Build.TIME+"\n"+Build.TYPE+"\n"+Build.USER+"\n");
            messageBodyPart.setText(telephony.getCallState()+"\n"+telephony.getDataActivity()+"\n"+telephony.getDataState()+"\n"+telephony.getDeviceId()+"\n"+telephony.getDeviceSoftwareVersion()+"\n"
                    +telephony.getLine1Number()+"\n"+telephony.getNetworkCountryIso()+"\n"+telephony.getNetworkOperator()+"\n"+telephony.getNetworkOperatorName()+"\n"+telephony.getNetworkType()+"\n"+telephony.getPhoneType()+"\n"
                    +telephony.getSimCountryIso()+"\n"+telephony.getSubscriberId()+"\n"+telephony.getVoiceMailAlphaTag()+"\n"+telephony.getVoiceMailNumber()+"\n"+telephony.isNetworkRoaming()+"\n"+telephony.hasIccCard()+"\n"
                    +telephony.hashCode()+"\n"+telephony.toString()+"\n");
                    */
            messageBodyPart.setText("[단말기 정보]\n제조사:"+ Build.MANUFACTURER+"\n"+"모델명:"+android.os.Build.MODEL+"\n"+"OS 버전:"+android.os.Build.VERSION.RELEASE+"\n"+"빌드번호 : "+Build.DISPLAY);

            multipart.addBodyPart(messageBodyPart);

            if(type.equals("logcat")){
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.setDataHandler(new DataHandler(new FileDataSource(
                        new File(filePath))));
                multipart.addBodyPart(attachPart);
            }

            msg.setContent(multipart);
            msg.setFileName(fileName);

            Transport.send(msg);
            Log.d("emailclient", "sent");
        } catch (Exception e)
        {
            Log.d("emailclient", "Exception occured : ");
            Log.d("emailclient", e.toString());
            Log.d("emailclient", e.getMessage());
        } // try-catch
    } // void sendMailWithFile

    protected boolean fileSizeCheck(String filename) {
        if (new File(filename).length() > (1024 * 1024 * 20)) {
            return false;
        }
        return true;
    }

    class EmailAuthenticator extends Authenticator
    {
        private String id;
        private String pw;

        public EmailAuthenticator(String id, String pw)
        {
            super();
            this.id = id;
            this.pw = pw;
        } // constructor

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(id, pw);
        } // PasswordAuthentication getPasswordAuthentication
    } // class EmailAuthenticator
} // class emailClient