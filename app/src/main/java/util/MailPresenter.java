package util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;
import realm.RealmConfig;
import realm.model.User;

/**
 * Created by Sunghyun on 2017. 7. 12..
 * ATM내에서 메일 보내는 동작은 모두 여기서 하도록함.
 */

public class MailPresenter implements Mailable {

    Context context;

    public MailPresenter(Context context){
        this.context = context;
    }

    @Override
    public void SendBtn(String type, String FilePath){

        RealmConfig realmConfig = new RealmConfig();
        Realm mRealm = Realm.getInstance(realmConfig.User_DefaultRealmVersion(context));

        User user_db = mRealm.where(User.class).equalTo("no",1).findFirst();

        EmailInfo emailInfo = new EmailInfo();
        emailInfo.setMailType(type);
        emailInfo.setUserEmail(user_db.getEmail());
        emailInfo.setFilePath(FilePath);

        SendEmailTask sendEmailTask = new SendEmailTask();
        sendEmailTask.execute(emailInfo);
    }

    private class SendEmailTask extends AsyncTask<EmailInfo, String, String> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Toast.makeText(context, "메일 전송중...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(EmailInfo... data){
            /**
             * mailType
             * capture / record / log
             */
            String mailType = data[0].getMailType();
            String user_email = data[0].getUserEmail();
            String path = data[0].getFilePath();
            String mailTitle = "";
            if(mailType.equals("capture")){
                mailTitle = "ATM_CAPTURE";
            }else if(mailType.equals("record")){
                mailTitle = "ATM_RECORD";
            }else if(mailType.equals("log")){
                mailTitle = "ATM_CRASH_LOG";
            }

            if (this.isCancelled()) {
                // 비동기작업을 cancel해도 자동으로 취소해주지 않으므로,
                // 작업중에 이런식으로 취소 체크를 해야 한다.
                return null;
            }else{
                GmailAddress gmailAddress = new GmailAddress();
                String gmail = gmailAddress.GetGmail_ID();
                Log.d("ATM_EMAIL", "Gmail ID : "+gmail);
                Log.d("ATM_EMAIL", "User ID : "+user_email);
                try
                {
                    EmailClient email = new EmailClient(gmail,
                            "qalab123");
                    email.sendMailWithFile(context,"capture",mailTitle, "test",
                            gmail, user_email,
                            path, "atm_capture.png");

                } catch (Exception e)
                {
                    Log.d("sendEmail", "send Email FAIL");
                } // try-catch

            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(context, "메일 전송 완료", Toast.LENGTH_SHORT).show();
        }
    }
}
