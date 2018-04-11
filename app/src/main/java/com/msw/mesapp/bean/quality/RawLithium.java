package com.msw.mesapp.bean.quality;

import java.util.List;
//原料：碳酸锂

public class RawLithium {

    private int code;
    private String message;
    private Data data;
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public Data getData() {
        return data;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    public class Data {

        private List<Content> content;
        private boolean last;
        private int totalpages;
        private int totalelements;
        private int size;
        private int number;
        private List<Sort> sort;
        private boolean first;
        private int numberofelements;
        public void setContent(List<Content> content) {
            this.content = content;
        }
        public List<Content> getContent() {
            return content;
        }

        public void setLast(boolean last) {
            this.last = last;
        }
        public boolean getLast() {
            return last;
        }

        public void setTotalpages(int totalpages) {
            this.totalpages = totalpages;
        }
        public int getTotalpages() {
            return totalpages;
        }

        public void setTotalelements(int totalelements) {
            this.totalelements = totalelements;
        }
        public int getTotalelements() {
            return totalelements;
        }

        public void setSize(int size) {
            this.size = size;
        }
        public int getSize() {
            return size;
        }

        public void setNumber(int number) {
            this.number = number;
        }
        public int getNumber() {
            return number;
        }

        public void setSort(List<Sort> sort) {
            this.sort = sort;
        }
        public List<Sort> getSort() {
            return sort;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }
        public boolean getFirst() {
            return first;
        }

        public void setNumberofelements(int numberofelements) {
            this.numberofelements = numberofelements;
        }
        public int getNumberofelements() {
            return numberofelements;
        }
        //////////////////////////////////////////////////////////////////////
        public class Content {

            private int code;
            private String operation;
            private String auditor;
            private String publisher;
            private String auditdate;
            private String publishdate;
            private Status status;
            private int testdate;
            private String batchnumber;
            private int productdate;
            private int number;
            private Judge judge;
            private double c1;
            private double c2;
            private double c3;
            private double c4;
            private double c5;
            private double c6;
            private int c7;
            private int c8;
            private int c9;
            private int c10;
            private int c11;
            private double c12;
            private double c13;
            private double c14;
            private double c15;
            private double c16;
            private int c17;
            private int c18;
            private int c19;
            private int c20;
            private int c21;
            private int c22;
            private int c23;
            private int c24;
            private int c25;
            private String c26;
            private int c27;
            private int c28;
            private int c29;
            private int c30;
            private int c31;
            private int c32;
            private int c33;
            private int c34;
            private int c35;
            private int c36;
            private int c37;
            private int c38;
            private int c39;
            private int c40;
            public void setCode(int code) {
                this.code = code;
            }
            public int getCode() {
                return code;
            }

            public void setOperation(String operation) {
                this.operation = operation;
            }
            public String getOperation() {
                return operation;
            }

            public void setAuditor(String auditor) {
                this.auditor = auditor;
            }
            public String getAuditor() {
                return auditor;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }
            public String getPublisher() {
                return publisher;
            }

            public void setAuditdate(String auditdate) {
                this.auditdate = auditdate;
            }
            public String getAuditdate() {
                return auditdate;
            }

            public void setPublishdate(String publishdate) {
                this.publishdate = publishdate;
            }
            public String getPublishdate() {
                return publishdate;
            }

            public void setStatus(Status status) {
                this.status = status;
            }
            public Status getStatus() {
                return status;
            }

            public void setTestdate(int testdate) {
                this.testdate = testdate;
            }
            public int getTestdate() {
                return testdate;
            }

            public void setBatchnumber(String batchnumber) {
                this.batchnumber = batchnumber;
            }
            public String getBatchnumber() {
                return batchnumber;
            }

            public void setProductdate(int productdate) {
                this.productdate = productdate;
            }
            public int getProductdate() {
                return productdate;
            }

            public void setNumber(int number) {
                this.number = number;
            }
            public int getNumber() {
                return number;
            }

            public void setJudge(Judge judge) {
                this.judge = judge;
            }
            public Judge getJudge() {
                return judge;
            }

            public void setC1(double c1) {
                this.c1 = c1;
            }
            public double getC1() {
                return c1;
            }

            public void setC2(double c2) {
                this.c2 = c2;
            }
            public double getC2() {
                return c2;
            }

            public void setC3(double c3) {
                this.c3 = c3;
            }
            public double getC3() {
                return c3;
            }

            public void setC4(double c4) {
                this.c4 = c4;
            }
            public double getC4() {
                return c4;
            }

            public void setC5(double c5) {
                this.c5 = c5;
            }
            public double getC5() {
                return c5;
            }

            public void setC6(double c6) {
                this.c6 = c6;
            }
            public double getC6() {
                return c6;
            }

            public void setC7(int c7) {
                this.c7 = c7;
            }
            public int getC7() {
                return c7;
            }

            public void setC8(int c8) {
                this.c8 = c8;
            }
            public int getC8() {
                return c8;
            }

            public void setC9(int c9) {
                this.c9 = c9;
            }
            public int getC9() {
                return c9;
            }

            public void setC10(int c10) {
                this.c10 = c10;
            }
            public int getC10() {
                return c10;
            }

            public void setC11(int c11) {
                this.c11 = c11;
            }
            public int getC11() {
                return c11;
            }

            public void setC12(double c12) {
                this.c12 = c12;
            }
            public double getC12() {
                return c12;
            }

            public void setC13(double c13) {
                this.c13 = c13;
            }
            public double getC13() {
                return c13;
            }

            public void setC14(double c14) {
                this.c14 = c14;
            }
            public double getC14() {
                return c14;
            }

            public void setC15(double c15) {
                this.c15 = c15;
            }
            public double getC15() {
                return c15;
            }

            public void setC16(double c16) {
                this.c16 = c16;
            }
            public double getC16() {
                return c16;
            }

            public void setC17(int c17) {
                this.c17 = c17;
            }
            public int getC17() {
                return c17;
            }

            public void setC18(int c18) {
                this.c18 = c18;
            }
            public int getC18() {
                return c18;
            }

            public void setC19(int c19) {
                this.c19 = c19;
            }
            public int getC19() {
                return c19;
            }

            public void setC20(int c20) {
                this.c20 = c20;
            }
            public int getC20() {
                return c20;
            }

            public void setC21(int c21) {
                this.c21 = c21;
            }
            public int getC21() {
                return c21;
            }

            public void setC22(int c22) {
                this.c22 = c22;
            }
            public int getC22() {
                return c22;
            }

            public void setC23(int c23) {
                this.c23 = c23;
            }
            public int getC23() {
                return c23;
            }

            public void setC24(int c24) {
                this.c24 = c24;
            }
            public int getC24() {
                return c24;
            }

            public void setC25(int c25) {
                this.c25 = c25;
            }
            public int getC25() {
                return c25;
            }

            public void setC26(String c26) {
                this.c26 = c26;
            }
            public String getC26() {
                return c26;
            }

            public void setC27(int c27) {
                this.c27 = c27;
            }
            public int getC27() {
                return c27;
            }

            public void setC28(int c28) {
                this.c28 = c28;
            }
            public int getC28() {
                return c28;
            }

            public void setC29(int c29) {
                this.c29 = c29;
            }
            public int getC29() {
                return c29;
            }

            public void setC30(int c30) {
                this.c30 = c30;
            }
            public int getC30() {
                return c30;
            }

            public void setC31(int c31) {
                this.c31 = c31;
            }
            public int getC31() {
                return c31;
            }

            public void setC32(int c32) {
                this.c32 = c32;
            }
            public int getC32() {
                return c32;
            }

            public void setC33(int c33) {
                this.c33 = c33;
            }
            public int getC33() {
                return c33;
            }

            public void setC34(int c34) {
                this.c34 = c34;
            }
            public int getC34() {
                return c34;
            }

            public void setC35(int c35) {
                this.c35 = c35;
            }
            public int getC35() {
                return c35;
            }

            public void setC36(int c36) {
                this.c36 = c36;
            }
            public int getC36() {
                return c36;
            }

            public void setC37(int c37) {
                this.c37 = c37;
            }
            public int getC37() {
                return c37;
            }

            public void setC38(int c38) {
                this.c38 = c38;
            }
            public int getC38() {
                return c38;
            }

            public void setC39(int c39) {
                this.c39 = c39;
            }
            public int getC39() {
                return c39;
            }

            public void setC40(int c40) {
                this.c40 = c40;
            }
            public int getC40() {
                return c40;
            }
            //////////////////////////////////////////////////////////////////////
            public class Status {

                private int code;
                private String name;
                public void setCode(int code) {
                    this.code = code;
                }
                public int getCode() {
                    return code;
                }

                public void setName(String name) {
                    this.name = name;
                }
                public String getName() {
                    return name;
                }

            }
            //////////////////////////////////////////////////////////////////////
            public class Judge {

                private int code;
                private String name;
                public void setCode(int code) {
                    this.code = code;
                }
                public int getCode() {
                    return code;
                }

                public void setName(String name) {
                    this.name = name;
                }
                public String getName() {
                    return name;
                }

            }

        }
        //////////////////////////////////////////////////////////////////////////

        public class Sort {
            private String direction;
            private String property;
            private boolean ignorecase;
            private String nullhandling;
            private boolean ascending;
            private boolean descending;
            public void setDirection(String direction) {
                this.direction = direction;
            }
            public String getDirection() {
                return direction;
            }

            public void setProperty(String property) {
                this.property = property;
            }
            public String getProperty() {
                return property;
            }

            public void setIgnorecase(boolean ignorecase) {
                this.ignorecase = ignorecase;
            }
            public boolean getIgnorecase() {
                return ignorecase;
            }

            public void setNullhandling(String nullhandling) {
                this.nullhandling = nullhandling;
            }
            public String getNullhandling() {
                return nullhandling;
            }

            public void setAscending(boolean ascending) {
                this.ascending = ascending;
            }
            public boolean getAscending() {
                return ascending;
            }

            public void setDescending(boolean descending) {
                this.descending = descending;
            }
            public boolean getDescending() {
                return descending;
            }

        }

    }

}