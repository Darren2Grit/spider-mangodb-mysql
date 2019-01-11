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
public class TmKnowledge implements Serializable {

    private static final long serialVersionUID = 1L;

    private String resId;

    private String code;

    private String name;

    private String courseId;

    private Integer status;

    private String remark;

    private Integer degree;

    private String question;

    private LocalDateTime bCreatedate;

    private String bModifyor;

    private LocalDateTime bModifydate;

        /**
     * 是否题库自己创建，0否，1是
     */
         private String isqdb;


    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDegree() {
        return degree;
    }

    public void setDegree(Integer degree) {
        this.degree = degree;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDateTime getbCreatedate() {
        return bCreatedate;
    }

    public void setbCreatedate(LocalDateTime bCreatedate) {
        this.bCreatedate = bCreatedate;
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

    public String getIsqdb() {
        return isqdb;
    }

    public void setIsqdb(String isqdb) {
        this.isqdb = isqdb;
    }

    @Override
    public String toString() {
        return "TmKnowledge{" +
        ", resId=" + resId +
        ", code=" + code +
        ", name=" + name +
        ", courseId=" + courseId +
        ", status=" + status +
        ", remark=" + remark +
        ", degree=" + degree +
        ", question=" + question +
        ", bCreatedate=" + bCreatedate +
        ", bModifyor=" + bModifyor +
        ", bModifydate=" + bModifydate +
        ", isqdb=" + isqdb +
        "}";
    }
}
