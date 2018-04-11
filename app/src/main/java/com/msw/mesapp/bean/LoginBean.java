package com.msw.mesapp.bean;

import java.util.List;

/**
 * Auto-generated: 2018-03-15 0:43:14
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class LoginBean {

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

    ///////////////////////////////////////////////////////////////////////////////////////////////

    public class Data {

        private String code;
        private String name;
        private String password;
        private String inteCircCard;
        private String description;
        private String contact;
        private int enable;
        private int enableIc;
        private String department;
        private List<Roles> roles;
        public void setCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public String getPassword() {
            return password;
        }

        public void setInteCircCard(String inteCircCard) {
            this.inteCircCard = inteCircCard;
        }
        public String getInteCircCard() {
            return inteCircCard;
        }

        public void setDescription(String description) {
            this.description = description;
        }
        public String getDescription() {
            return description;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
        public String getContact() {
            return contact;
        }

        public void setEnable(int enable) {
            this.enable = enable;
        }
        public int getEnable() {
            return enable;
        }

        public void setEnableIc(int enableIc) {
            this.enableIc = enableIc;
        }
        public int getEnableIc() {
            return enableIc;
        }

        public void setDepartment(String department) {
            this.department = department;
        }
        public String getDepartment() {
            return department;
        }

        public void setRoles(List<Roles> roles) {
            this.roles = roles;
        }
        public List<Roles> getRoles() {
            return roles;
        }

        ///////////////////////////////////////////////////////////////////////////////////

        public class Roles {

            private int code;
            private String name;
            private String description;
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

            public void setDescription(String description) {
                this.description = description;
            }
            public String getDescription() {
                return description;
            }

        }

    }

}