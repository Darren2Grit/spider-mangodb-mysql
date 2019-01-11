package cn.xueyuetang.questionspider.entity;

import java.time.LocalDateTime;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author darren
 * @since 2018-11-15
 */
@Data
public class TmQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    private String qId;

    private String qDbid;

    private Integer qType;

    private Integer qLevel;

    private String qFrom;

    private Integer qStatus;

    private String qContent;

    private String qKey;

    private String qResolve;

    private String qPoster;

    private LocalDateTime qCreatedate;

    private String qModifyor;

    private LocalDateTime qModifydate;

    private String qData;

    private String orgId;

    private String qKnowages;

    private String resId;

        /**
     * 托福写作听力
     */
         private String res2Id;

    private String tag;

    private Integer qOrder;

}
