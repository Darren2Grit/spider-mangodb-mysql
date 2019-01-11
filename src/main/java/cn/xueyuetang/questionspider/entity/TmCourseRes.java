package cn.xueyuetang.questionspider.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author darren
 * @since 2018-11-15
 */
public class TmCourseRes implements Serializable {

    private static final long serialVersionUID = 1L;

    private String resId;

    private String resName;

    private String courseId;

    private Integer resStatus;

    private LocalDateTime bCreatedate;

    private String fileUrl;

    private Integer fileType;

    private Integer norder;

    private String knowage;

    private String knowagename;

    private String wikicontent;

    private String bModifyor;

    private LocalDateTime bModifydate;

    private Integer score;


    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getResStatus() {
        return resStatus;
    }

    public void setResStatus(Integer resStatus) {
        this.resStatus = resStatus;
    }

    public LocalDateTime getbCreatedate() {
        return bCreatedate;
    }

    public void setbCreatedate(LocalDateTime bCreatedate) {
        this.bCreatedate = bCreatedate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Integer getNorder() {
        return norder;
    }

    public void setNorder(Integer norder) {
        this.norder = norder;
    }

    public String getKnowage() {
        return knowage;
    }

    public void setKnowage(String knowage) {
        this.knowage = knowage;
    }

    public String getKnowagename() {
        return knowagename;
    }

    public void setKnowagename(String knowagename) {
        this.knowagename = knowagename;
    }

    public String getWikicontent() {
        return wikicontent;
    }

    public void setWikicontent(String wikicontent) {
        this.wikicontent = wikicontent;
    }

    public String getbModifyor() {
        return bModifyor;
    }

    public void setbModifyor(String bModifyor) {
        this.bModifyor = bModifyor;
    }

    public LocalDateTime getbModifydate() {
        return bModifydate;
    }

    public void setbModifydate(LocalDateTime bModifydate) {
        this.bModifydate = bModifydate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "TmCourseRes{" +
        ", resId=" + resId +
        ", resName=" + resName +
        ", courseId=" + courseId +
        ", resStatus=" + resStatus +
        ", bCreatedate=" + bCreatedate +
        ", fileUrl=" + fileUrl +
        ", fileType=" + fileType +
        ", norder=" + norder +
        ", knowage=" + knowage +
        ", knowagename=" + knowagename +
        ", wikicontent=" + wikicontent +
        ", bModifyor=" + bModifyor +
        ", bModifydate=" + bModifydate +
        ", score=" + score +
        "}";
    }
}
