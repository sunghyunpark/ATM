package presenter;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import realm.model.DownLoadLink;
import realm.model.Memo;

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
            Memo memoVO = new Memo();
            String created_at = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            try{
                mRealm.beginTransaction();
                memoVO.setNo(no);
                memoVO.setMemoTitle(title);
                memoVO.setMemoContents(contents);
                memoVO.setCreated_at(created_at);
            }catch (Exception e){

            }finally {
                mRealm.copyToRealmOrUpdate(memoVO);
                mRealm.commitTransaction();
            }
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
            String created_at = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Memo memo = mRealm.where(Memo.class).equalTo("no",no).findFirst();
            mRealm.beginTransaction();
            memo.setMemoTitle(title);
            memo.setMemoContents(contents);
            memo.setCreated_at(created_at);
            mRealm.commitTransaction();
        }
    }

}

