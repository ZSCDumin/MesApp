package com.msw.mesapp.bean.quality;


//@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)

public class RawPresomaByCodeBean
{

    /**
     * code : 0
     * message : 成功
     * data : {"code":1915,"operation":null,"auditor":null,"publisher":null,"auditDate":null,"publishDate":null,"status":{"code":2,"name":"已审核-未发布"},"testDate":1482768000000,"batchNumber":"YS-KC16M1243906","insideCode":"KC66456","productDate":1480521600000,"number":3,"judge":{"code":1,"name":"合格"},"c1":2.154,"c2":0.55,"c3":5.2758,"c4":8.65,"c5":3.449,"c6":5.409,"c7":10.304,"c8":18.586,"c9":25.242,"c10":0.01,"c11":28.55,"c12":1.5,"c13":28.55,"c14":28.55,"c15":33.5,"c16":62.27,"c17":12.54,"c18":11.8,"c19":38.24,"c20":59.8,"c21":31.77,"c22":37.96,"c23":2.783,"c24":10.08,"c25":0.1,"c26":28.55,"c27":28.55,"c28":28.55,"c29":28.55,"c30":28.55,"c31":28.55,"c32":28.55,"c33":28.55,"c34":28.55,"c35":28.55,"c36":28.55,"c37":28.55,"c38":28.55,"c39":28.55,"c40":28.55}
     */

    private int code;
    private String message;
    /**
     * code : 1915
     * operation : null
     * auditor : null
     * publisher : null
     * auditDate : null
     * publishDate : null
     * status : {"code":2,"name":"已审核-未发布"}
     * testDate : 1482768000000
     * batchNumber : YS-KC16M1243906
     * insideCode : KC66456
     * productDate : 1480521600000
     * number : 3
     * judge : {"code":1,"name":"合格"}
     * c1 : 2.154
     * c2 : 0.55
     * c3 : 5.2758
     * c4 : 8.65
     * c5 : 3.449
     * c6 : 5.409
     * c7 : 10.304
     * c8 : 18.586
     * c9 : 25.242
     * c10 : 0.01
     * c11 : 28.55
     * c12 : 1.5
     * c13 : 28.55
     * c14 : 28.55
     * c15 : 33.5
     * c16 : 62.27
     * c17 : 12.54
     * c18 : 11.8
     * c19 : 38.24
     * c20 : 59.8
     * c21 : 31.77
     * c22 : 37.96
     * c23 : 2.783
     * c24 : 10.08
     * c25 : 0.1
     * c26 : 28.55
     * c27 : 28.55
     * c28 : 28.55
     * c29 : 28.55
     * c30 : 28.55
     * c31 : 28.55
     * c32 : 28.55
     * c33 : 28.55
     * c34 : 28.55
     * c35 : 28.55
     * c36 : 28.55
     * c37 : 28.55
     * c38 : 28.55
     * c39 : 28.55
     * c40 : 28.55
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int code;
        private Object operation;
        private Object auditor;
        private Object publisher;
        private Object auditDate;
        private Object publishDate;
        /**
         * code : 2
         * name : 已审核-未发布
         */

        private StatusEntity status;
        private long testDate;
        private String batchNumber;
        private String insideCode;
        private long productDate;
        private int number;
        /**
         * code : 1
         * name : 合格
         */

        private JudgeEntity judge;
        private double c1;
        private double c2;
        private double c3;
        private double c4;
        private double c5;
        private double c6;
        private double c7;
        private double c8;
        private double c9;
        private double c10;
        private double c11;
        private double c12;
        private double c13;
        private double c14;
        private double c15;
        private double c16;
        private double c17;
        private double c18;
        private double c19;
        private double c20;
        private double c21;
        private double c22;
        private double c23;
        private double c24;
        private double c25;
        private double c26;
        private double c27;
        private double c28;
        private double c29;
        private double c30;
        private double c31;
        private double c32;
        private double c33;
        private double c34;
        private double c35;
        private double c36;
        private double c37;
        private double c38;
        private double c39;
        private double c40;

        public void setCode(int code) {
            this.code = code;
        }

        public void setOperation(Object operation) {
            this.operation = operation;
        }

        public void setAuditor(Object auditor) {
            this.auditor = auditor;
        }

        public void setPublisher(Object publisher) {
            this.publisher = publisher;
        }

        public void setAuditDate(Object auditDate) {
            this.auditDate = auditDate;
        }

        public void setPublishDate(Object publishDate) {
            this.publishDate = publishDate;
        }

        public void setStatus(StatusEntity status) {
            this.status = status;
        }

        public void setTestDate(long testDate) {
            this.testDate = testDate;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public void setInsideCode(String insideCode) {
            this.insideCode = insideCode;
        }

        public void setProductDate(long productDate) {
            this.productDate = productDate;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public void setJudge(JudgeEntity judge) {
            this.judge = judge;
        }

        public void setC1(double c1) {
            this.c1 = c1;
        }

        public void setC2(double c2) {
            this.c2 = c2;
        }

        public void setC3(double c3) {
            this.c3 = c3;
        }

        public void setC4(double c4) {
            this.c4 = c4;
        }

        public void setC5(double c5) {
            this.c5 = c5;
        }

        public void setC6(double c6) {
            this.c6 = c6;
        }

        public void setC7(double c7) {
            this.c7 = c7;
        }

        public void setC8(double c8) {
            this.c8 = c8;
        }

        public void setC9(double c9) {
            this.c9 = c9;
        }

        public void setC10(double c10) {
            this.c10 = c10;
        }

        public void setC11(double c11) {
            this.c11 = c11;
        }

        public void setC12(double c12) {
            this.c12 = c12;
        }

        public void setC13(double c13) {
            this.c13 = c13;
        }

        public void setC14(double c14) {
            this.c14 = c14;
        }

        public void setC15(double c15) {
            this.c15 = c15;
        }

        public void setC16(double c16) {
            this.c16 = c16;
        }

        public void setC17(double c17) {
            this.c17 = c17;
        }

        public void setC18(double c18) {
            this.c18 = c18;
        }

        public void setC19(double c19) {
            this.c19 = c19;
        }

        public void setC20(double c20) {
            this.c20 = c20;
        }

        public void setC21(double c21) {
            this.c21 = c21;
        }

        public void setC22(double c22) {
            this.c22 = c22;
        }

        public void setC23(double c23) {
            this.c23 = c23;
        }

        public void setC24(double c24) {
            this.c24 = c24;
        }

        public void setC25(double c25) {
            this.c25 = c25;
        }

        public void setC26(double c26) {
            this.c26 = c26;
        }

        public void setC27(double c27) {
            this.c27 = c27;
        }

        public void setC28(double c28) {
            this.c28 = c28;
        }

        public void setC29(double c29) {
            this.c29 = c29;
        }

        public void setC30(double c30) {
            this.c30 = c30;
        }

        public void setC31(double c31) {
            this.c31 = c31;
        }

        public void setC32(double c32) {
            this.c32 = c32;
        }

        public void setC33(double c33) {
            this.c33 = c33;
        }

        public void setC34(double c34) {
            this.c34 = c34;
        }

        public void setC35(double c35) {
            this.c35 = c35;
        }

        public void setC36(double c36) {
            this.c36 = c36;
        }

        public void setC37(double c37) {
            this.c37 = c37;
        }

        public void setC38(double c38) {
            this.c38 = c38;
        }

        public void setC39(double c39) {
            this.c39 = c39;
        }

        public void setC40(double c40) {
            this.c40 = c40;
        }

        public int getCode() {
            return code;
        }

        public Object getOperation() {
            return operation;
        }

        public Object getAuditor() {
            return auditor;
        }

        public Object getPublisher() {
            return publisher;
        }

        public Object getAuditDate() {
            return auditDate;
        }

        public Object getPublishDate() {
            return publishDate;
        }

        public StatusEntity getStatus() {
            return status;
        }

        public long getTestDate() {
            return testDate;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public String getInsideCode() {
            return insideCode;
        }

        public long getProductDate() {
            return productDate;
        }

        public int getNumber() {
            return number;
        }

        public JudgeEntity getJudge() {
            return judge;
        }

        public double getC1() {
            return c1;
        }

        public double getC2() {
            return c2;
        }

        public double getC3() {
            return c3;
        }

        public double getC4() {
            return c4;
        }

        public double getC5() {
            return c5;
        }

        public double getC6() {
            return c6;
        }

        public double getC7() {
            return c7;
        }

        public double getC8() {
            return c8;
        }

        public double getC9() {
            return c9;
        }

        public double getC10() {
            return c10;
        }

        public double getC11() {
            return c11;
        }

        public double getC12() {
            return c12;
        }

        public double getC13() {
            return c13;
        }

        public double getC14() {
            return c14;
        }

        public double getC15() {
            return c15;
        }

        public double getC16() {
            return c16;
        }

        public double getC17() {
            return c17;
        }

        public double getC18() {
            return c18;
        }

        public double getC19() {
            return c19;
        }

        public double getC20() {
            return c20;
        }

        public double getC21() {
            return c21;
        }

        public double getC22() {
            return c22;
        }

        public double getC23() {
            return c23;
        }

        public double getC24() {
            return c24;
        }

        public double getC25() {
            return c25;
        }

        public double getC26() {
            return c26;
        }

        public double getC27() {
            return c27;
        }

        public double getC28() {
            return c28;
        }

        public double getC29() {
            return c29;
        }

        public double getC30() {
            return c30;
        }

        public double getC31() {
            return c31;
        }

        public double getC32() {
            return c32;
        }

        public double getC33() {
            return c33;
        }

        public double getC34() {
            return c34;
        }

        public double getC35() {
            return c35;
        }

        public double getC36() {
            return c36;
        }

        public double getC37() {
            return c37;
        }

        public double getC38() {
            return c38;
        }

        public double getC39() {
            return c39;
        }

        public double getC40() {
            return c40;
        }

        public static class StatusEntity {
            private int code;
            private String name;

            public void setCode(int code) {
                this.code = code;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCode() {
                return code;
            }

            public String getName() {
                return name;
            }
        }

        public static class JudgeEntity {
            private int code;
            private String name;

            public void setCode(int code) {
                this.code = code;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCode() {
                return code;
            }

            public String getName() {
                return name;
            }
        }
    }
}





