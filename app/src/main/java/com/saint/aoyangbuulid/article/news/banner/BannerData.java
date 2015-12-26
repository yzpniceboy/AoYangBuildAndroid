package com.saint.aoyangbuulid.article.news.banner;

/**
 * Created by zzh on 15-12-2.
 */
public class BannerData {
    private int Id;
    private String Title;
    private String Image;
    private int PositionId;
    private String hot_content;
    private int page;
    private String Notice_Title;
    private String Notice;


    //region getter and setter
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPositionId() {
        return PositionId;
    }

    public void setPositionId(int positionId) {
        PositionId = positionId;
    }

    public String getHot_content() {
        return hot_content;
    }

    public void setHot_content(String hot_content) {
        this.hot_content = hot_content;
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }



    public String getNotice_Title() {
        return Notice_Title;
    }

    public void setNotice_Title(String notice_Title) {
        Notice_Title = notice_Title;
    }
    public String getNotice() {
        return Notice;
    }

    public void setNotice(String notice) {
        Notice = notice;
    }

    //endregion
}
