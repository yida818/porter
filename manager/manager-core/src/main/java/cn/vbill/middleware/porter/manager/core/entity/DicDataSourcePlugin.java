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

import cn.vbill.middleware.porter.manager.core.enums.InputTypeEnum;

/**
 * 数据源信息字典表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public class DicDataSourcePlugin implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 数据源类型枚举.
     */
    private String sourceType;

    /**
     * 页面字段名称.
     */
    private String fieldName;

    /**
     * 字段英文名.
     */
    private String fieldCode;

    /**
     * 页面展示顺序.
     */
    private Integer fieldOrder;

    /**
     * 页面字段类型
     */
    private InputTypeEnum fieldType;

    /**
     * 页面字段类型对应枚举
     */
    private String fieldTypeKey;

    /**
     * 页面字段对应正则校验
     */
    private String fieldValidate;

    /**
     * 页面字段对应校验说明
     */
    private String fieldExplain;

    /**
     * 状态1启用 -1禁用.
     */
    private Integer state;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 备注字段.
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
     * 数据源类型枚举 get方法.
     */
    public String getSourceType() {
        return sourceType == null ? null : sourceType.trim();
    }

    /**
     * 数据源类型枚举 set方法.
     */
    public void setSourceType(String sourceType) {
        this.sourceType = sourceType == null ? null : sourceType.trim();
    }

    /**
     * 页面字段名称 get方法.
     */
    public String getFieldName() {
        return fieldName == null ? null : fieldName.trim();
    }

    /**
     * 页面字段名称 set方法.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    /**
     * 字段英文名 get方法.
     */
    public String getFieldCode() {
        return fieldCode == null ? null : fieldCode.trim();
    }

    /**
     * 字段英文名 set方法.
     */
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode == null ? null : fieldCode.trim();
    }

    /**
     * 页面展示顺序 get方法.
     */
    public Integer getFieldOrder() {
        return fieldOrder;
    }

    /**
     * 页面展示顺序 set方法.
     */
    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    /**
     * 状态1启用 -1禁用 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态1启用 -1禁用 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 是否作废 get方法.
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废 set方法.
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /**
     * 备注字段 get方法.
     */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /**
     * 备注字段 set方法.
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 页面字段类型 get方法
     */
    public InputTypeEnum getFieldType() {
        return fieldType;
    }

    /**
     * 页面字段类型 set方法
     */
    public void setFieldType(InputTypeEnum fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 页面字段类型对应枚举 get方法
     */
    public String getFieldTypeKey() {
        return fieldTypeKey == null ? null : fieldTypeKey.trim();
    }

    /**
     * 页面字段类型对应枚举 set方法
     */
    public void setFieldTypeKey(String fieldTypeKey) {
        this.fieldTypeKey = fieldTypeKey == null ? null : fieldTypeKey.trim();
    }

    public String getFieldValidate() {
        return fieldValidate;
    }

    public void setFieldValidate(String fieldValidate) {
        this.fieldValidate = fieldValidate;
    }

    public String getFieldExplain() {
        return fieldExplain;
    }

    public void setFieldExplain(String fieldExplain) {
        this.fieldExplain = fieldExplain;
    }
}
