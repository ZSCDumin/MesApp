package com.msw.mesapp.bean.quality;

import java.util.List;

//v制程：预混

public class ProcessPremix {

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
    public class Data {

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
            return content;
        }

        public void setLast(boolean last) {
            this.last = last;
        }
        public boolean getLast() {
            return last;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }
        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }
        public int getTotalElements() {
            return totalElements;
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

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }
        public int getNumberOfElements() {
            return numberOfElements;
        }
        public class Content {

            private int code;
            private String operation;
            private String auditor;
            private String publisher;
            private String auditDate;
            private String publishDate;
            private Status status;
            private long testDate;
            private String batchNumber;
            private String type;
            private double lithiumSoluble;
            private String supplier;
            private String bound;
            private double pc1;
            private double pc2;
            private double pc3;
            private double pc4;
            private double pc5;
            private double pc6;
            private double pc7;
            private int pc8;
            private double pc9;
            private double pc10;
            private String pc11;
            private String pc12;
            private String pc13;
            private String pc14;
            private String pc15;
            private String pc16;
            private String pc17;
            private String pc18;
            private String pc19;
            private String pc20;
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

            public void setAuditDate(String auditDate) {
                this.auditDate = auditDate;
            }
            public String getAuditDate() {
                return auditDate;
            }

            public void setPublishDate(String publishDate) {
                this.publishDate = publishDate;
            }
            public String getPublishDate() {
                return publishDate;
            }

            public void setStatus(Status status) {
                this.status = status;
            }
            public Status getStatus() {
                return status;
            }

            public void setTestDate(long testDate) {
                this.testDate = testDate;
            }
            public long getTestDate() {
                return testDate;
            }

            public void setBatchNumber(String batchNumber) {
                this.batchNumber = batchNumber;
            }
            public String getBatchNumber() {
                return batchNumber;
            }

            public void setType(String type) {
                this.type = type;
            }
            public String getType() {
                return type;
            }

            public void setLithiumSoluble(double lithiumSoluble) {
                this.lithiumSoluble = lithiumSoluble;
            }
            public double getLithiumSoluble() {
                return lithiumSoluble;
            }

            public void setSupplier(String supplier) {
                this.supplier = supplier;
            }
            public String getSupplier() {
                return supplier;
            }

            public void setBound(String bound) {
                this.bound = bound;
            }
            public String getBound() {
                return bound;
            }

            public void setPc1(double pc1) {
                this.pc1 = pc1;
            }
            public double getPc1() {
                return pc1;
            }

            public void setPc2(double pc2) {
                this.pc2 = pc2;
            }
            public double getPc2() {
                return pc2;
            }

            public void setPc3(double pc3) {
                this.pc3 = pc3;
            }
            public double getPc3() {
                return pc3;
            }

            public void setPc4(double pc4) {
                this.pc4 = pc4;
            }
            public double getPc4() {
                return pc4;
            }

            public void setPc5(double pc5) {
                this.pc5 = pc5;
            }
            public double getPc5() {
                return pc5;
            }

            public void setPc6(double pc6) {
                this.pc6 = pc6;
            }
            public double getPc6() {
                return pc6;
            }

            public void setPc7(double pc7) {
                this.pc7 = pc7;
            }
            public double getPc7() {
                return pc7;
            }

            public void setPc8(int pc8) {
                this.pc8 = pc8;
            }
            public int getPc8() {
                return pc8;
            }

            public void setPc9(double pc9) {
                this.pc9 = pc9;
            }
            public double getPc9() {
                return pc9;
            }

            public void setPc10(double pc10) {
                this.pc10 = pc10;
            }
            public double getPc10() {
                return pc10;
            }

            public void setPc11(String pc11) {
                this.pc11 = pc11;
            }
            public String getPc11() {
                return pc11;
            }

            public void setPc12(String pc12) {
                this.pc12 = pc12;
            }
            public String getPc12() {
                return pc12;
            }

            public void setPc13(String pc13) {
                this.pc13 = pc13;
            }
            public String getPc13() {
                return pc13;
            }

            public void setPc14(String pc14) {
                this.pc14 = pc14;
            }
            public String getPc14() {
                return pc14;
            }

            public void setPc15(String pc15) {
                this.pc15 = pc15;
            }
            public String getPc15() {
                return pc15;
            }

            public void setPc16(String pc16) {
                this.pc16 = pc16;
            }
            public String getPc16() {
                return pc16;
            }

            public void setPc17(String pc17) {
                this.pc17 = pc17;
            }
            public String getPc17() {
                return pc17;
            }

            public void setPc18(String pc18) {
                this.pc18 = pc18;
            }
            public String getPc18() {
                return pc18;
            }

            public void setPc19(String pc19) {
                this.pc19 = pc19;
            }
            public String getPc19() {
                return pc19;
            }

            public void setPc20(String pc20) {
                this.pc20 = pc20;
            }
            public String getPc20() {
                return pc20;
            }
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

        }

        public class Sort {

            private String direction;
            private String property;
            private boolean ignoreCase;
            private String nullHandling;
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

            public void setIgnoreCase(boolean ignoreCase) {
                this.ignoreCase = ignoreCase;
            }
            public boolean getIgnoreCase() {
                return ignoreCase;
            }

            public void setNullHandling(String nullHandling) {
                this.nullHandling = nullHandling;
            }
            public String getNullHandling() {
                return nullHandling;
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