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

package cn.vbill.middleware.porter.common.util.db.meta;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 16:02
 */
public class TableColumn {
    @Getter @Setter private String name;
    @Getter @Setter private String defaultValue;
    @Getter @Setter private boolean required;
    @Getter @Setter private boolean primaryKey;
    //
    /**
     * 不同的目标类型有不同的表示
     * jdbc : java.sql.Types
     * kudu : org.apache.kudu.Type
     */
    @Getter @Setter private int typeCode;
}
