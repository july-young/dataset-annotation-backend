/**
 * Copyright 2020 Tianshu AI Platform. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =============================================================
 */

package org.dubhe.data.constant;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AutoLabelModelServiceStatusEnum {

    STARTING(101, "启动中"),

    RUNNING(102, "运行中"),

    START_FAILED(103, "启动失败"),

    STOPPING(104, "停止中"),

    STOPPED(105, "已停止")
    ;

    AutoLabelModelServiceStatusEnum(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    private int value;

    private String msg;

}