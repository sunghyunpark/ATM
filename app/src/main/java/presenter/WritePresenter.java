package presenter;

import io.realm.Realm;
import io.realm.internal.Context;
import realm.model.DownLoadLink;

/**
 * Created by NAVER on 2017-07-22.
 */

public class WritePresenter implements Writeable{

    Context context;

    /**
     * DownLoadLink / Memo 글쓰기 기능
     * @param mRealm Realm 객체
     * @param flag Link / Memo
     * @param no Realm에 넣을 no
     * @param title 글 제목
     * @param contents 글 내용
     */
    @Override
    public void Write(Realm mRealm, String flag, int no, String title, String contents){
        if(flag.equals("Link")){
            DownLoadLink downLoadLinkVO = new DownLoadLink();
            try{
                mRealm.beginTransaction();
                downLoadLinkVO.setNo(no);
                downLoadLinkVO.setLinkTitle(title);
                downLoadLinkVO.setLinkUrl(contents);
            }catch (Exception e){

            }finally {
                mRealm.copyToRealmOrUpdate(downLoadLinkVO);
                mRealm.commitTransaction();
            }
        }else{

        }
    }

    @Override
    public void Edit(){

    }

}

