package com.msw.mesapp.bean.quality;

/**
 * Created by Mr.Meng on 2018/3/22.
 */
/**
 * 前驱体
 */
import java.util.List;

public class RawPresomaBean {
    private int code;

    private String message;

    private Data data;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public static class Data {
        public Data(){}
        private List<Content> content;

        private boolean last;

        private int totalPages;

        private int totalElements;

        private int size;

        private int number;

        private List<Sort> sort;

        private boolean first;

        private int numberOfElements;

        public void setContent(List<Content> content) {
            this.content = content;
        }

        public List<Content> getContent() {
            return this.content;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public boolean getLast() {
            return this.last;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalPages() {
            return this.totalPages;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalElements() {
            return this.totalElements;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return this.size;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return this.number;
        }

        public void setSort(List<Sort> sort) {
            this.sort = sort;
        }

        public List<Sort> getSort() {
            return this.sort;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public boolean getFirst() {
            return this.first;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getNumberOfElements() {
            return this.numberOfElements;
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        public static class Content {
            public Content(){}
            private int code;

            private String operation;

            private String auditor;

            private String publisher;

            private String auditDate;

            private String publishDate;

            private Status status;

            private int testDate;

            private String batchNumber;

            private String insideCode;

            private int productDate;

            private int number;

            private Judge judge;

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

            public void setCode(int code){
                this.code = code;
            }
            public int getCode(){
                return this.code;
            }
            public void setOperation(String operation){
                this.operation = operation;
            }
            public String getOperation(){
                return this.operation;
            }
            public void setAuditor(String auditor){
                this.auditor = auditor;
            }
            public String getAuditor(){
                return this.auditor;
            }
            public void setPublisher(String publisher){
                this.publisher = publisher;
            }
            public String getPublisher(){
                return this.publisher;
            }
            public void setAuditDate(String auditDate){
                this.auditDate = auditDate;
            }
            public String getAuditDate(){
                return this.auditDate;
            }
            public void setPublishDate(String publishDate){
                this.publishDate = publishDate;
            }
            public String getPublishDate(){
                return this.publishDate;
            }
            public void setStatus(Status status){
                this.status = status;
            }
            public Status getStatus(){
                return this.status;
            }
            public void setTestDate(int testDate){
                this.testDate = testDate;
            }
            public int getTestDate(){
                return this.testDate;
            }
            public void setBatchNumber(String batchNumber){
                this.batchNumber = batchNumber;
            }
            public String getBatchNumber(){
                return this.batchNumber;
            }
            public void setInsideCode(String insideCode){
                this.insideCode = insideCode;
            }
            public String getInsideCode(){
                return this.insideCode;
            }
            public void setProductDate(int productDate){
                this.productDate = productDate;
            }
            public int getProductDate(){
                return this.productDate;
            }
            public void setNumber(int number){
                this.number = number;
            }
            public int getNumber(){
                return this.number;
            }
            public void setJudge(Judge judge){
                this.judge = judge;
            }
            public Judge getJudge(){
                return this.judge;
            }
            public void setC1(double c1){
                this.c1 = c1;
            }
            public double getC1(){
                return this.c1;
            }
            public void setC2(double c2){
                this.c2 = c2;
            }
            public double getC2(){
                return this.c2;
            }
            public void setC3(double c3){
                this.c3 = c3;
            }
            public double getC3(){
                return this.c3;
            }
            public void setC4(double c4){
                this.c4 = c4;
            }
            public double getC4(){
                return this.c4;
            }
            public void setC5(double c5){
                this.c5 = c5;
            }
            public double getC5(){
                return this.c5;
            }
            public void setC6(double c6){
                this.c6 = c6;
            }
            public double getC6(){
                return this.c6;
            }
            public void setC7(double c7){
                this.c7 = c7;
            }
            public double getC7(){
                return this.c7;
            }
            public void setC8(double c8){
                this.c8 = c8;
            }
            public double getC8(){
                return this.c8;
            }
            public void setC9(double c9){
                this.c9 = c9;
            }
            public double getC9(){
                return this.c9;
            }
            public void setC10(double c10){
                this.c10 = c10;
            }
            public double getC10(){
                return this.c10;
            }
            public void setC11(int c11){
                this.c11 = c11;
            }
            public double getC11(){
                return this.c11;
            }
            public void setC12(double c12){
                this.c12 = c12;
            }
            public double getC12(){
                return this.c12;
            }
            public void setC13(int c13){
                this.c13 = c13;
            }
            public double getC13(){
                return this.c13;
            }
            public void setC14(int c14){
                this.c14 = c14;
            }
            public double getC14(){
                return this.c14;
            }
            public void setC15(double c15){
                this.c15 = c15;
            }
            public double getC15(){
                return this.c15;
            }
            public void setC16(double c16){
                this.c16 = c16;
            }
            public double getC16(){
                return this.c16;
            }
            public void setC17(double c17){
                this.c17 = c17;
            }
            public double getC17(){
                return this.c17;
            }
            public void setC18(double c18){
                this.c18 = c18;
            }
            public double getC18(){
                return this.c18;
            }
            public void setC19(double c19){
                this.c19 = c19;
            }
            public double getC19(){
                return this.c19;
            }
            public void setC20(double c20){
                this.c20 = c20;
            }
            public double getC20(){
                return this.c20;
            }
            public void setC21(double c21){
                this.c21 = c21;
            }
            public double getC21(){
                return this.c21;
            }
            public void setC22(double c22){
                this.c22 = c22;
            }
            public double getC22(){
                return this.c22;
            }
            public void setC23(double c23){
                this.c23 = c23;
            }
            public double getC23(){
                return this.c23;
            }
            public void setC24(double c24){
                this.c24 = c24;
            }
            public double getC24(){
                return this.c24;
            }
            public void setC25(double c25){
                this.c25 = c25;
            }
            public double getC25(){
                return this.c25;
            }
            public void setC26(double c26){
                this.c26 = c26;
            }
            public double getC26(){
                return this.c26;
            }
            public void setC27(int c27){
                this.c27 = c27;
            }
            public double getC27(){
                return this.c27;
            }
            public void setC28(int c28){
                this.c28 = c28;
            }
            public double getC28(){
                return this.c28;
            }
            public void setC29(double c29){
                this.c29 = c29;
            }
            public double getC29(){
                return this.c29;
            }
            public void setC30(int c30){
                this.c30 = c30;
            }
            public double getC30(){
                return this.c30;
            }
            public void setC31(double c31){
                this.c31 = c31;
            }
            public double getC31(){
                return this.c31;
            }
            public void setC32(double c32){
                this.c32 = c32;
            }
            public double getC32(){
                return this.c32;
            }
            public void setC33(double c33){
                this.c33 = c33;
            }
            public double getC33(){
                return this.c33;
            }
            public void setC34(double c34){
                this.c34 = c34;
            }
            public double getC34(){
                return this.c34;
            }
            public void setC35(double c35){
                this.c35 = c35;
            }
            public double getC35(){
                return this.c35;
            }
            public void setC36(double c36){
                this.c36 = c36;
            }
            public double getC36(){
                return this.c36;
            }
            public void setC37(double c37){
                this.c37 = c37;
            }
            public double getC37(){
                return this.c37;
            }
            public void setC38(double c38){
                this.c38 = c38;
            }
            public double getC38(){
                return this.c38;
            }
            public void setC39(double c39){
                this.c39 = c39;
            }
            public double getC39(){
                return this.c39;
            }
            public void setC40(double c40){
                this.c40 = c40;
            }
            public double getC40(){
                return this.c40;
            }

            ////////////////////////////////////////////////////////////////////////////////////////
            public static class Judge {
                public Judge(){}
                private int code;

                private String name;

                public void setCode(int code){
                    this.code = code;
                }
                public int getCode(){
                    return this.code;
                }
                public void setName(String name){
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }

            }
            ///////////////////////////////////////////////////////////////////////////////////////
            public static class Status {
                public Status(){}
                private int code;

                private String name;

                public void setCode(int code){
                    this.code = code;
                }
                public int getCode(){
                    return this.code;
                }
                public void setName(String name){
                    this.name = name;
                }
                public String getName(){
                    return this.name;
                }

            }

        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////
        public static class Sort {
            public Sort(){}
            private String direction;

            private String property;

            private boolean ignoreCase;

            private String nullHandling;

            private boolean ascending;

            private boolean descending;

            public void setDirection(String direction){
                this.direction = direction;
            }
            public String getDirection(){
                return this.direction;
            }
            public void setProperty(String property){
                this.property = property;
            }
            public String getProperty(){
                return this.property;
            }
            public void setIgnoreCase(boolean ignoreCase){
                this.ignoreCase = ignoreCase;
            }
            public boolean getIgnoreCase(){
                return this.ignoreCase;
            }
            public void setNullHandling(String nullHandling){
                this.nullHandling = nullHandling;
            }
            public String getNullHandling(){
                return this.nullHandling;
            }
            public void setAscending(boolean ascending){
                this.ascending = ascending;
            }
            public boolean getAscending(){
                return this.ascending;
            }
            public void setDescending(boolean descending){
                this.descending = descending;
            }
            public boolean getDescending(){
                return this.descending;
            }

        }

    }
}


