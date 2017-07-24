package presenter;

import io.realm.Realm;
import realm.model.DownLoadLink;

/**
 * Created by NAVER on 2017-07-22.
 */

public class WritePresenter implements Writeable{

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

    /**
     * DownLoadLink / Memo 수정 기능
     * @param mRealm
     * @param flag EditLink / EditMemo
     * @param no
     * @param title
     * @param contents
     */
    @Override
    public void Edit(Realm mRealm, String flag, int no, String title, String contents){
        if(flag.equals("EditLink")){
            DownLoadLink downLoadLink = mRealm.where(DownLoadLink.class).equalTo("no",no).findFirst();
            mRealm.beginTransaction();
            downLoadLink.setLinkTitle(title);
            downLoadLink.setLinkUrl(contents);
            mRealm.commitTransaction();
        }else if(flag.equals("EditMemo")){

        }
    }

}

