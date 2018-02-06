package com.wondersgroup.hiip.empi.service.impl;

import com.wondersgroup.hiip.empi.service.AhoCorasickDoubleArrayTrie;
import com.wondersgroup.hiip.empi.persist.rdb.dao.DistrictDao;
import com.wondersgroup.hiip.empi.service.District;
import com.wondersgroup.hiip.empi.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 业务逻辑中的增删改查方法接口实现
 */
@Service("districtService")
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictDao districtDao;
    //全局变量
    private AhoCorasickDoubleArrayTrie<String> acdat;
    private Map<String,Object> hashMap;


    /**
     * 地理地址结构化
     *
     * @return
     */
    @Override
    public List<District> getDistrict(String text, List<District> districtList) {
        //识别分词
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList = acdat.parseText(text);
        //分词过滤
        List<AhoCorasickDoubleArrayTrie<String>.Hit<String>> wordList1 = new ArrayList<>();
        wordList1.addAll(wordList);
        for (int i=0;i<wordList.size();i++){
            for (int j=0;j<wordList.size();j++) {
                if (wordList.get(i).value.contains(wordList.get(j).value) && wordList.get(i)!= wordList.get(j)){
                    wordList1.remove(wordList.get(j));
                }
            }
            if (wordList.get(i).value.length()<=1){
                wordList1.remove(wordList.get(i));
            }
        }
        wordList.retainAll(wordList1);
        wordList1.clear();
        //行政区划搜索
        List<District> districts = new ArrayList<>();
        for (int i=0;i<wordList.size();i++){
            for (District district : districtList){
                if (district.getName().indexOf(wordList.get(i).value)!=-1){
                    districts.add(district);
                }
            }
        }
        //去重
        Set set = new HashSet(districts);
        districts.clear();
        districts.addAll(set);
        //级别排序
        Collections.sort(districts, new Comparator<District>() {
            @Override
            public int compare(District o1, District o2) {
                if (o1.getCodelevel() > o2.getCodelevel()){
                    return 1;
                }
                if (o1.getCodelevel() == o2.getCodelevel()) {
                    return 0;
                }
                return -1;
            }
        });
        //归属判断（利用标识符进行判断，结果由flag表示：0代表不作处理）
        int flag = 0;
        //（1）三级行政区划
        if (districts.size() == 3) {
            if (districts.get(0).getIdentifier().equals(districts.get(1).getParent()) && districts.get(1).getIdentifier().equals(districts.get(2).getParent())) {
                flag = 1;
            }
        //（2）两级行政区划
        } else if (districts.size() == 2) {
            //前两级行政区划
            if (districts.get(0).getIdentifier().equals(districts.get(1).getParent()) && districts.get(0).getCodelevel() == 1) {
                flag = 2;
            //后两级行政区划
            } else if (districts.get(0).getIdentifier().equals(districts.get(1).getParent()) && districts.get(0).getCodelevel() == 2) {
                districts.add(0, (District) hashMap.get(districts.get(0).getParent()));
                flag = 3;
            }else{
                //跳级行政区划
                District midDistrict = (District) hashMap.get(districts.get(1).getParent());
                if (midDistrict.getParent().equals(districts.get(0).getIdentifier())){
                    districts.add(1,midDistrict);
                    flag = 4;
                }
            }
        }

        //置空
        if (flag == 0){
            districts.clear();
            wordList.clear();
        }
        return districts;
    }

    /**
     * 加载数据
     * @return
     */
    @Override
    public List<District> loadDistrict() {
        //加载数据源
        List<District> districtList = districtDao.findAll();
        hashMap = new HashMap();
        TreeMap<String, String> map = new TreeMap<String, String>();
        for (District district : districtList) {
            map.put(district.getName(), district.getName());
            map.put(district.getSmallname(),district.getSmallname());
            hashMap.put(district.getIdentifier(),district);
        }
        // 生成一个 AhoCorasickDoubleArrayTrie
        acdat = new AhoCorasickDoubleArrayTrie<String>();
        acdat.build(map);
        return districtList;
    }
}
