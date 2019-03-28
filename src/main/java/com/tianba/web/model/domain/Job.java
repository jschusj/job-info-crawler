package com.tianba.web.model.domain;

import com.baomidou.mybatisplus.annotations.TableName;
import com.tianba.web.common.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * 描述:
 *
 * @author yangtao
 * @create 2019-03-26 14:53
 */
@TableName("job")
@Data
public class Job extends BaseModel<Job> {
    private String url;
    private String company;
    private String city;
    private String district;
    private String businessDistrict;
    private String name;
    private Integer minSalary;
    private Integer maxSalary;
    private String request;
    private Date publishTime;
    private String label;
    private String advantage;
    private String detail;
    private String address;
}
