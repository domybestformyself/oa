package yx.sz.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name="OA_ID_JOB")
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Job implements Serializable {
    private static final long serialVersionUID = 459497377750274376L;
    /**
     * CODE	VARCHAR2(100) 代码 PK主键
     */
    @Id
    @Column(name="CODE", length=100)
    private String code;
    /** NAME VARCHAR2(50) 名称 */
    @Column(name="NAME", length=50)
    private String name;
    /** REMARK	VARCHAR2(300) 职位说明*/
    @Column(name="REMARK", length=300)
    private String remark;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
