/**
 *  Copyright (c) 1999-2017 Syncron AB
 *  Östra Järnvägsgatan 27, SE-111 20 Stockholm, Sweden.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of Syncron AB
 *  ("Confidential Information"). You shall not disclose such Confidential
 *  Information and shall use it only in accordance with the terms of the license
 *  agreement you entered into with Syncron AB.
 */
package com.syncron.serverlessapi;

/**
 * Created by satya.yadav@syncron.com on 09-05-2017.
 */
public final class Constants {

    public static final String AWS_REGION = System.getenv("AWS_REGION") != null ? System.getenv("AWS_REGION") : "us-east-2";
    public static final String MOVIE_TABLE_NAME = System.getenv("MOVIE_TABLE_NAME") != null ? System.getenv("MOVIE_TABLE_NAME") : "ServerlessChemelMovie";

    private Constants() {
    }
}

