package com.ruoyi.risk.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 网点信息对象 gis_branch
 */
public class GisBranch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "网点名称")
    private String branchName;

    @Excel(name = "网点编码")
    private String branchCode;

    private String branchType;

    private Double longitude;

    private Double latitude;

    @Excel(name = "省份")
    private String province;

    @Excel(name = "城市")
    private String city;

    private String district;

    private String address;

    private String phone;

    @Excel(name = "负责人")
    private String manager;

    private Integer staffCount;

    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getBranchCode() { return branchCode; }
    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public String getBranchType() { return branchType; }
    public void setBranchType(String branchType) { this.branchType = branchType; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getManager() { return manager; }
    public void setManager(String manager) { this.manager = manager; }
    public Integer getStaffCount() { return staffCount; }
    public void setStaffCount(Integer staffCount) { this.staffCount = staffCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
