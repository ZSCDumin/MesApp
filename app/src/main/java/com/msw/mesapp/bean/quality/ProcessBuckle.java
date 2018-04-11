
package com.msw.mesapp.bean.quality;

import java.util.List;
//制程：扣电
public class ProcessBuckle {

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
            private int furnaceNum;
            private String bound;
            private double pc1;
            private double pc2;
            private double pc3;
            private String pc4;
            private String pc5;
            private String pc6;
            private String pc7;
            private String pc8;
            private String pc9;
            private String pc10;
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

            public void setFurnaceNum(int furnaceNum) {
                this.furnaceNum = furnaceNum;
            }
            public int getFurnaceNum() {
                return furnaceNum;
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

            public void setPc4(String pc4) {
                this.pc4 = pc4;
            }
            public String getPc4() {
                return pc4;
            }

            public void setPc5(String pc5) {
                this.pc5 = pc5;
            }
            public String getPc5() {
                return pc5;
            }

            public void setPc6(String pc6) {
                this.pc6 = pc6;
            }
            public String getPc6() {
                return pc6;
            }

            public void setPc7(String pc7) {
                this.pc7 = pc7;
            }
            public String getPc7() {
                return pc7;
            }

            public void setPc8(String pc8) {
                this.pc8 = pc8;
            }
            public String getPc8() {
                return pc8;
            }

            public void setPc9(String pc9) {
                this.pc9 = pc9;
            }
            public String getPc9() {
                return pc9;
            }

            public void setPc10(String pc10) {
                this.pc10 = pc10;
            }
            public String getPc10() {
                return pc10;
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