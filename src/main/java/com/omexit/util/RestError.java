/*
 * Copyright 2012 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.omexit.util;

import lombok.Data;

/**
 * @author Les Hazlewood
 */
@Data
public class RestError {

    private Integer status;
    private Integer code;
    private String message;
    private String developerMessage;
    private String exceptionMessage;


    public RestError(Integer status, Integer code, String message, String developerMessage, String exceptionMessage) {
        super();
        if (status == null) {
            throw new NullPointerException("HttpStatus argument cannot be null.");
        }
        this.status = status;
        this.code = code;
        this.message = message;
        this.developerMessage = developerMessage;
        this.exceptionMessage=exceptionMessage;
    }
}