package com.msw.mesapp.base;

/**
 * Created by Mr.Meng on 2018/1/1.
 */

public class GlobalApi {

    public static String BASEURL = "http://218.77.105.241:30080/mes/";

    public final static class AppUpdate {
        public final static String PATH = "appUpdate/getAll";
    }

    public final static class IdManagement {
        public final static String code = "code";
        public final static String enableIc = "enableIc";
        public final static String inteCircCard = "inteCircCard";

        public final static String bindCard = "user/updateInteCircCard";
        public final static String PATH = "department/getDepartmentsAndUsers";
    }

    //待办事项
    public final static class UndoThingsItems {
        public final static String PATH = "messageQueue/getByStatusAndAddresseeCodeByPage";
        public final static String PATH1 = "messageQueue/updateStatusByCode";

        public final static String STATUS = "status";
        public final static String CODE = "code";
        public final static String ADDRESSEECODE = "addresseeCode";
    }

    //用户登录
    public final static class Login {
        public final static String PATH = "user/login";
        public final static String CODE = "code";
        public final static String PASSWORD = "password";
        public final static String PATHNFC = "user/loginByInteCircCard";
        public final static String NFC = "inteCircCard";
    }

    //修改密码
    public final static class UpdatePassword {
        public final static String PATH = "user/updatePassword";
        public final static String CODE = "code";
        public final static String OLDPASS = "oldPassword";
        public final static String NEWPASS = "newPassword";
        public final static String RENEWPASS = "reNewPassword";
    }

    //---------------------------------------------------------------------------
    //品质管理
    public final static class Quality {
        //原料：前驱体
        public final static class RawPresoma {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "rawPresoma/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "rawPresoma/getByCode";
                public final static String CODE = "code";
            }

            public final static class Audit { //审核
                public final static String PATH = "rawPresoma/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "rawPresoma/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //原料：碳酸锂
        public final static class RawLithium {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "rawLithium/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "rawLithium/getByCode";
                public final static String CODE = "code";
            }

            public final static class Audit { //审核
                public final static String PATH = "rawLithium/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "rawLithium/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //产品：
        public final static class Product {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "product/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "product/getByCode";
                public final static String CODE = "code";
            }

            public final static class Audit { //审核
                public final static String PATH = "product/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "product/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //制程：预混
        public final static class ProcessPremix {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "processPremix/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "processPremix/getByCode";
                public final static String CODE = "code";
            }

            public final static class Audit { //审核
                public final static String PATH = "processPremix/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "processPremix/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //制程：粉碎粒度
        public final static class ProcessSize {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "processSize/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "processSize/getByCode";
            }

            public final static class Audit { //审核
                public final static String PATH = "processSize/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "processSize/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //制程: 碳酸锂
        public final static class ProcessLithium {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "processLithium/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "processLithium/getByCode";
            }

            public final static class Audit { //审核
                public final static String PATH = "processLithium/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "processLithium/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }

        //制程：扣电
        public final static class ProcessBuckle {
            public final static class ByPage {  //通过状态编码查询-分页
                public final static String PATH = "processBuckle/getAllByStatusCodeByPage";
                public final static String statusCode = "statusCode"; //状态编码
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class ByCode { //通过编码查询
                public final static String PATH = "processBuckle/getByCode";
                public final static String CODE = "code";
            }

            public final static class Audit { //审核
                public final static String PATH = "processBuckle/updateAuditByCode";
                public final static String code = "code"; //编码
                public final static String auditorCode = "auditorCode"; //审核人编码
                public final static String statusCode = "statusCode";  //状态编码
            }

            public final static class Publish {  //发布
                public final static String PATH = "processBuckle/updatePublishByCode";
                public final static String code = "code";  //状态编码
                public final static String publisherCode = "publisherCode";  //发布人编码
                public final static String statusCode = "statusCode";  //状态编码
            }
        }
    }

    //设备巡检
    public final static class Inspect {
        //工人巡检
        public final static class Worker {

            public final static class ByPage {  //通过时间查询-分页
                public final static String PATH = "tallyTaskHeader/getByUserAndStatus";
                public final static String code = "code"; //用户编码
                public final static String status = "status"; //状态
            }

            public final static class updateTime {  //更新时间
                public final static String PATH = "appMission/updateTimeByCode";
                public final static String code = "code"; //编码
                public final static String updateTime = "updateTime"; //完成时间
            }

            public final static class CheckHead {  //表头
                public final static String PATH = "tallyTaskHeader/tally";
                public final static String tallyTaskHeaderCode = "tallyTaskHeaderCode"; //指导书头部编码
                public final static String tallyTaskCode = "tallyTaskCode"; //指导书内容编码
                public final static String result = "result"; //结果状态码
                public final static String PATH1 = "appCheckHead/getByCode";
                public final static String code = "code"; //编码
            }

            public final static class AppCheck {  //记录
                public final static String PATH = "appCheck/add";
                public final static String checkHeadCode = "checkHeadCode";//表头编码（确定从属车间）
                public final static String checkPerson = "checkPerson";
                public final static String time = "time";//记录时间
                public final static String examState = "examState";

                public final static String state1 = "state1";
                public final static String abnormal1 = "abnormal1";
                public final static String state2 = "state2";
                public final static String abnormal2 = "abnormal2";
                public final static String state3 = "state3";
                public final static String abnormal3 = "abnormal3";
                public final static String state4 = "state4";
                public final static String abnormal4 = "abnormal4";
                public final static String state5 = "state5";
                public final static String abnormal5 = "abnormal5";
                public final static String state6 = "state6";
                public final static String abnormal6 = "abnormal6";
                public final static String state7 = "state7";
                public final static String abnormal7 = "abnormal7";
                public final static String state8 = "state8";
                public final static String abnormal8 = "abnormal8";
                public final static String state9 = "state9";
                public final static String abnormal9 = "abnormal9";
                public final static String state10 = "state10";
                public final static String abnormal10 = "abnormal10";
                public final static String state11 = "state11";
                public final static String abnormal11 = "abnormal11";
                public final static String state12 = "state12";
                public final static String abnormal12 = "abnormal12";
                public final static String state13 = "state13";
                public final static String abnormal13 = "abnormal13";
                public final static String state14 = "state14";
                public final static String abnormal14 = "abnormal14";

            }
        }

        //班长审核
        public final static class Monitor {
            public final static class ByPage {  //通过审核人查询-分页
                public final static String PATH = "appCheck/getAllByExamStateByPage";
                public final static String examState = "examState";
                public final static String page = "page"; //页码(从0开始，默认是0)
                public final static String size = "size"; //每页记录数(默认是10)
                public final static String sort = "sort"; //排序的字段名（默认是code）
                public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
            }

            public final static class UpdateExamPerson {  //更新审核人
                public final static String PATH = "appCheck/updateExamPersonByCode";
                public final static String code = "code"; //编码
                public final static String examPerson = "examPerson";
                public final static String examState = "examState";
                public final static String examDate = "examDate";

            }
        }

    }

    //设备维修
    public final static class Repair {

        public final static String PATH_ByCode = "equipment/detail"; //通过主键查找
        public final static String PATH_ByFlagInPages = "equipment/findByFlagInPages"; //通过类型查找

        public final static String PATH_REPORT = "equipment/findByApplicationPerson";//申请人找
        public final static String PATH_REPAIR = "equipment/findByRepairMan";  //按维修人查找
        public final static String PATH_SCORE = "equipment/findByEvaluator";  //按评价人查找
        public final static String code = "code"; //人id
        public final static String page = "page"; //页码(从0开始，默认是0)
        public final static String size = "size"; //每页记录数(默认是10)
        public final static String sort = "sort"; //排序的字段名（默认是code）
        public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）

        //上报故障
        public final static class UpErr {
            public final static String getArchiveCode = "archive/getByEquipment";
            public final static String PATH = "equipment/apply";
            public final static String department_code = "department.code"; //所属部门
            public final static String eqArchive_code = "eqArchive.code";//设备
            public final static String equipment_code = "equipment.code";//设备
            public final static String code = "code";//设备
            public final static String productLine_code = "productLine.code"; //所属产品线
            public final static String applicationDescription = "applicationDescription"; //故障描述
            public final static String applicationPerson_code = "applicationPerson.code"; //申请人id
        }

        //维修接单
        public final static class Accept {
            public final static String PATH = "equipment/accept";
            public final static String code = "code"; //主键
            public final static String repairMan_code = "repairMan.code";
        }

        //提交维修
        public final static class Work {
            public final static String PATH = "equipment/finish"; //维修完成
            public final static String code = "code"; //主键
            public final static String repairmanDescription = "repairmanDescription"; //维修描述
        }

        //评价
        public final static class Score {
            public final static String PATH = "equipment/evaluate"; //维修完成
            public final static String code = "code"; //主键
            public final static String evaluation_code = "evaluation.code"; //维修描述
            public final static String evaluator_code = "evaluator.code";
        }

    }

    //条码管理
    public final static class QrManager {
        public final static String PATH = "equipment/getAllByPage";
        public final static String page = "page"; //页码(从0开始，默认是0)
        public final static String size = "size"; //每页记录数(默认是10)
        public final static String sort = "sort"; //排序的字段名（默认是code）
        public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
    }
    //---------------------------------------------------------------------------

    //仓库管理
    public final static class WareHourse {
        public final static String materialCode = "materialCode";
        public final static String productSendHeaderCode = "productSendHeaderCode";
        public final static String pickingStatus = "pickingStatus"; //原料领取状态
        public final static String curAuditorCode = "curAuditorCode"; //原料领取状态
        public final static String auditStatus = "auditStatus"; //原料审核状态
        public final static String page = "page"; //页码(从0开始，默认是0)
        public final static String size = "size"; //每页记录数(默认是10)
        public final static String sort = "sort"; //排序的字段名（默认是code）
        public final static String asc = "asc"; //排序的方式（0是减序，1是增序，默认是1）
        public final static String pickingApplyHeaderCode = "pickingApplyHeaderCode";
        public final static String code = "code"; //编码
        public final static String note = "note"; //意见
        public final static String auditorCode = "auditorCode"; //审核人编码
        public final static String nextAuditorCode = "nextAuditorCode"; //下一个审核人
        public final static String status = "status"; //状态
        public final static String userCode = "userCode"; //用户id
        public final static String godownerCode = "godownerCode";//入库人编码
        public final static String model = "model"; //类型
        public final static String departmentCode = "departmentCode"; //部门
        public final static String weight = "weight"; //重量
        public final static String payerCode = "payerCode"; //缴库人编码
        public final static String productGodowns = "productGodowns"; //产品入库单
        public final static String batchNumber = "productGodowns"; //批号
        public final static String outStatus = "outStatus"; //出库状态
        public final static String senderCode = "senderCode"; //出库人编码


        //原料入库
        public final static class MaterialIn {
            //第三方发货单
            public final static String PATH_Send_Header_ByPage = "sendEntryHeader/getAllByPage"; //分页获取
            public final static String PATH_Send_Header_ByCode = "sendEntryHeader/getByCode"; //编码获取
            public final static String PATH_Send_Updata = "sendEntryHeader/updateStatusByCode"; //跟新
            //入库单表头
            public final static String PATH_GoDown_Header_ByPage = "godownEntryHeader/getAllByPage"; //表头--通过编码获取
            public final static String PATH_GoDown_Header_ByCode = "godownEntryHeader/getByCode"; //编码获取
        }

        //原料出库
        public final static class MaterialOut {

            public final static String PATH_PickingApply_Header_ByPage = "pickingApplyHeader/getByPickingStatusByPage"; //通过领料状态查询-分页
            public final static String PATH_AUDIT = "pickingApplyHeader/getByAuditStatus"; //通过审核状态查询
            public final static String PATH_CODE = "pickingApplyHeader/getByCode"; //通过code查询
            public final static String PATH_ApplyHeader = "pickingAudit/getByPickingApplyHeader"; //通过申请单表头查询
            public final static String updateAuditStatusByCode = "pickingApplyHeader/updateAuditStatusByCode";//正常审核
            public final static String updateAuditStatusByCodeUrgent = "pickingApplyHeader/updateAuditStatusByCodeUrgent";//紧急审核
            public final static String getRestAuditorByCode = "pickingApplyHeader/getRestAuditorByCode";//获取剩余审批人

        }

        //产品入库
        public final static class ProductIn {
            public final static String getAllDepartment = "department/getAllByPage";
            public final static String getAllRawType = "rawType/getAllByPage";
            public final static String getByMaterialCode = "rawType/getByMaterialCode";
            public final static String getByStatusByPage = "productGodownHeader/getByStatusByPage"; //通过入库状态查询
            public final static String getByCode = "productGodownHeader/getByCode"; //通过编码获取
            public final static String updateStatusAndGodownerByCode = "productGodownHeader/updateStatusAndGodownerByCode";//更新入库状态
            public final static String add_godown_info = "productGodownHeader/add";//新增入库

        }

        //产品出库
        public final static class ProductOut {

            public final static String getByProductSendHeader = "productSendAudit/getByProductSendHeader";
            public final static String getByAuditStatusByPage = "productSendHeader/getByAuditStatusByPage";
            public final static String getByOutStatusAndNumberLike = "productSendHeader/getByOutStatusAndNumberLike"; //通过出库状态查询
            public final static String getByCode = "productSendHeader/getByCode"; //通过编码获取
            public final static String updateOutStatusByCode = "productSendHeader/updateOutStatusByCode"; //通过编码更新出库状态
            public final static String updateAuditStatusByCode = "productSendHeader/updateAuditStatusByCode"; //通过编码更新审核状态
        }

        //样品入库
        public final static class SampleIn {
            public final static String PATH_Header_ByPage = "godownTestInformHeader/getGodownTestInformHeadersByStatusByPage"; //表头
            public final static String PATH_Header_ByCode = "godownTestInformHeader/getByCode"; //内容---通过编码获取
            public final static String updateStatusByCode = "godownEntryHeader/updateStatusByCode"; //更新
        }

        //样品出库
        public final static class SampleOut {
            public final static String updateStatusByCode = "godownTestInformHeader/updateStatusByCode"; //更新
            public final static String getAll = "godownEntryHeader/getAll"; //查询所有
            public final static String getGodownTestInformHeadersByStatusByPage = "godownTestInformHeader/getGodownTestInformHeadersByStatusByPage"; //内容---通过状态查询

        }
    }

    public final static class ProductManagement {

        public final static class CheckScale {
            public final static String getAllScales = "electronicBalance/getAll";
            public final static String getByEquipmentCodeByPage = "audit/getByEquipmentCodeByPage";
            public final static String getByCode = "audit/getByCode";
            public final static String add = "audit/add";
            public final static String update = "audit/update";
            public final static String getByConfirm = "audit/getByEquipmentCodeAndConfirm";
        }

        public final static class ShaiwangCheck {

            public final static String getAll = "screenCheck/getAll";
            public final static String add = "screenCheck/add";
            public final static String upload = "image/upload";
            public final static String getById = "screenCheck/getById";
            public final static String getByShakerCodeLikeByPage = "screenCheck/getByShakerCodeLikeByPage";
            public final static String update = "screenCheck/update";
            public final static String code = "code";
            public final static String shakerCode = "shakerCode";
            public final static String picture = "picture";
            public final static String file = "file";
            public final static String inspectorCode = "inspector";
            public final static String inspectorTime = "inspectorTime";
            public final static String state = "state";

        }

        public final static class Jiaojieban {

            public final static String jobsCode = "jobsCode";
            public final static String headerCode = "headerCode";
            public final static String contentCode = "contentCode";
            public final static String stateCode = "stateCode";
            public final static String handover_date = "handoverDate";
            public final static String handoverTypeCode = "handoverTypeCode";
            public final static String duty_code = "dutyCode";
            public final static String shifter_code = "shifterCode";
            public final static String successor_code = "successorCode";
            public final static String code = "code";
            public final static String getAllByPage = "jobs/getAllByPage";
            public final static String getByJobsCodeAndShifterCode = "handoverHeader/getByJobsCodeAndShifterCode";
            public final static String getByJobsCodeAndSuccessorCode = "handoverHeader/getByJobsCodeAndSuccessorCode";
            public final static String jobsHandoverAdd = "jobsHandover/add";
            public final static String handoverHeaderAdd = "handoverHeader/add";
            public final static String handoverRecordAdd = "handoverRecord/add";
            public final static String deleteByCode = "handoverHeader/deleteByCode";
            public final static String getByCode = "jobsHandover/getByCode";
            public final static String getByCode1 = "handoverHeader/getByCode";
            public final static String getAllPerson = "user/getAllDepartmentUserByUserCode";
            public final static String getByHandoverTypeCode = "jobsHandover/getByHandoverTypeCode";
        }

    }


    //---------------------------------------------------------------------------
    //数据请求提示语
    public final static class ProgressDialog {
        public final static String Login = "正在登陆...";
        public final static String UpdatePassWord = "修改密码...";
        public final static String GetData = "请求数据中...";
        public final static String UpData = "上传数据中...";
        public final static String INTERR = "网络出错！";
    }
}
