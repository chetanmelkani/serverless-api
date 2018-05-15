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

import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

/**
 * @author chetanmelkani
 *
 */
public class MovieDAO {
    private static final Logger logger = Logger.getLogger(MovieDAO.class);
    private final DynamoDBMapper mapper;

    public MovieDAO() {
        final AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(Constants.AWS_REGION).build();
        this.mapper = new DynamoDBMapper(dynamoDB);
    }


    public Movie get(final Movie movie) {

        logger.debug("getting movie:" + movie);
        try {
            return this.mapper.load(movie);
        } catch (final Exception e) {
            logger.error("get operation for entity Tenant with id " + movie + " failed", e);
            throw new RuntimeException("get operation failed");
        }
    }

    public Movie create(final Movie movie) {
        logger.debug("creating entity of type " + movie);
        try {
            this.mapper.save(movie);
            return movie;
        } catch (final Exception e) {
            logger.error("create operation for entity " + movie + " failed", e);
            throw new RuntimeException("create operation for entity " + movie + " failed");
        }
    }

    public Movie delete(final Movie movie) {
        logger.debug("deleting entity of type " + movie);
        try {

            this.mapper.delete(movie);
            return movie;
        } catch (final Exception e) {
            logger.error("delete operation for entity " + movie + " failed", e);
            throw new RuntimeException("delete operation failed");
        }
    }
    
    public List<Movie> list() {
        logger.info("list entity of type Tenant");

        final DynamoDBScanExpression expression = new DynamoDBScanExpression();

        try {
            final PaginatedScanList<Movie> paginatedScan = this.mapper.scan(Movie.class, expression);
            return paginatedScan;
        } catch (final Exception e) {
            logger.error("list operation for entity Tenant failed", e);
            throw new RuntimeException("list operation for entity Tenant failed");
        }
    }

}
