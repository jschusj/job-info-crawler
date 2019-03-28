package com.tianba.web.common;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class BaseModel<T extends Model> extends Model<T> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    @JsonIgnore
    @TableField("create_time")
    private Date createTime;
    /**
     * 修改时间
     */
    @JsonIgnore
    @TableField("update_time")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean insert() {
//        if (this.id == null) {
////            this.id = new ObjectId().toString();
//            this.id = UUID.randomUUID().toString().replace("-","");
//        }
        if (this.createTime == null) {
            this.createTime = new Date();
        }
        if (this.updateTime == null) {
            this.updateTime = new Date();
        }
        return super.insert();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
