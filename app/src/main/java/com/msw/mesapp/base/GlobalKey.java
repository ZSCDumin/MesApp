package com.msw.mesapp.base;

/**
 * Created by Mr.Meng on 2018/2/10.
 */

public class GlobalKey {

    //权限
    public final static class Permission {

        public final static String SPKEY = "000"; //用户权限码

        /**
         * 设备管理
         */
        public final static String ProduceInpsect = "86"; //执行巡检
        public final static String InspectCheck = "87"; //巡检审核
        public final static String Repair_Bill = "129"; //维修单据
        public final static String Repair_Recevive = "90"; //维修接单
        public final static String Repair_Apply = "88"; //维修申请
        public final static String Repair_Comment = "91"; //维修评价
        public final static String QrManagment = "92"; //条码管理

        /**
         * 品质管理
         */
        public final static String ProductCheck = "109"; //产品审核
        public final static String MaterialCheck = "108"; //原料审核
        public final static String ProcessCheck = "110"; //制程审核
        public final static String MaterialRelease = "111"; //原料发布
        public final static String ProcessRelease = "112"; //制程发布
        public final static String ProductRelease = "128"; //产品发布

        /**
         * 仓库管理
         */
        public final static String MaterialInput = "117"; //原料入库
        public final static String MaterialOutput = "122"; //原料出库执行
        public final static String MaterialOutputCheck = "121"; //原料出库审核
        public final static String ProductInput = "112"; //产品入库
        public final static String ProductOutput = "124"; //产品出库执行
        public final static String ProductOutputCheck = "123"; //产品出库审核
        public final static String AddProductVertify = "120"; //新增缴库
        public final static String SampleInput = "118"; //样品入库
        public final static String SampleOutput = "119"; //样品领取

        /**
         * 生产管理
         */
        public final static String ProductCheckScale = "125"; //生产核秤
        public final static String CheckScaleVertify = "134"; //核秤审核
        public final static String ShaiwangCheck = "126"; //筛网检查
        public final static String JobTransform = "127"; //岗位交接

        /**
         * ID管理
         */
        public final static String IDManagement = "131"; //ID管理

    }

    //....
    public final static class Login {
        public final static String DATA = "001";
        public final static String CODE = "001CODE";
        public final static String NFC = "001NFC";
    }
}
