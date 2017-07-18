package util;

/**
 * Created by MacBookPro13 on 2017. 7. 12..
 */

public class GmailAddress {
    private String[] gmail_list = {"ntsnt3001@gmail.com","ntsnt3002@gmail.com", "ntsnt3003@gmail.com"};

    public String GetGmail_ID(){
        String gmail_id = "";

        int random = (int) (Math.random() * 3);
        gmail_id = gmail_list[random];

        return gmail_id;
    }
}
