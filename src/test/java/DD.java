//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * <p>
// * TODO
// * </p>
// *
// * @author xiaohongxin
// * @version 1.0.0
// * @date 2019/9/18 11:20
// */
//public class DD {
//
//    private List<MyTreeVo> getCompanyChildren(Integer companyid, List<Integer> list, List<Integer> companyidList){
//        Map<String, Object> map=new HashMap<String, Object>();
//        List<MyTreeVo> myTreeVoList=new ArrayList<>();
//        map.put("pid", companyid);
//        map.put("list", companyidList);
//        List<MyTreeVo> myTreeVos = companyMapper.selectCompanyList(map);
//        if (myTreeVos!=null&&myTreeVos.size()>0) {
//            for(int i=0;i<myTreeVos.size();i++) {
//                list.add(myTreeVos.get(i).getId());
//                myTreeVoList.add(myTreeVos.get(i));
//                //
//                myTreeVos.get(i).setChildren(getCompanyChildren(myTreeVos.get(i).getId(),list,companyidList);)
//
//
//            }
//        }
//        return myTreeVoList;
//    }
//
//
//}
