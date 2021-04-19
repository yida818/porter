/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.core.entity;

/**
 * 告警配置策略字典表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public class DicAlarmPlugin implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 告警策略.
     */
    private String alertType;

    /**
     * 字段实际名.
     */
    private String fieldCode;

    /**
     * 字段展示名.
     */
    private String fieldName;

    /**
     * 字段页面顺序.
     */
    private Integer fieldOrder;

    /**
     * 字段类型.
     */
    private String fieldType;

    /**
     * 字段类型字典KEY.
     */
    private String fieldTypeKey;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 告警策略 get方法.
     */
    public String getAlertType() {
        return alertType == null ? null : alertType.trim();
    }

    /**
     * 告警策略 set方法.
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType == null ? null : alertType.trim();
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldTypeKey() {
        return fieldTypeKey;
    }

    public void setFieldTypeKey(String fieldTypeKey) {
        this.fieldTypeKey = fieldTypeKey;
    }

    /**
     * 状态 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 备注 get方法.
     */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /**
     * 备注 set方法.
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}
