package com.wondersgroup.hiip.empi.service;

import com.wondersgroup.hiip.empi.service.District;

import java.util.List;

/**
 * 业务逻辑中的增删改查方法接口
 */
public interface DistrictService {

    /**
     * 地理地址结构化
     * @return
     */
    public List<District> getDistrict(String text, List<District> districtList);

    /**
     * 加载数据
     * @return
     */
    public List<District> loadDistrict();
}
