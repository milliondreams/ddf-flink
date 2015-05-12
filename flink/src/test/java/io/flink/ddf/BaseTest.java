/*
 * Copyright 2014, Tuplejump Inc.
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
 */

package io.flink.ddf;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import io.ddf.DDFManager;
import io.ddf.content.Schema;
import io.ddf.exception.DDFException;
import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: satya
 */
public class BaseTest {

    public static DDFManager manager;

    static Logger LOG;


    @BeforeClass
    public static void startServer() throws Exception {
        LOG = LoggerFactory.getLogger(BaseTest.class);
        manager = DDFManager.get("flink");

    }

    @AfterClass
    public static void stopServer() throws Exception {
        manager.shutdown();
    }

    public void createTableAirline() throws DDFException {
        Schema schema = new Schema("airline", "Year int,Month int,DayofMonth int,DayOfWeek int,DepTime int,CRSDepTime int,ArrTime int,CRSArrTime int,UniqueCarrier string, FlightNum int,TailNum string, ActualElapsedTime int, CRSElapsedTime int,AirTime int, ArrDelay int, DepDelay int, Origin string,Dest string, Distance int, TaxiIn int, TaxiOut int, Cancelled int,CancellationCode string, Diverted string, CarrierDelay int,WeatherDelay int, NASDelay int, SecurityDelay int, LateAircraftDelay int");
        List<Schema.Column> cols = schema.getColumns();
        List<String> nameAndType = Lists.transform(cols, new Function<Schema.Column, String>() {
            @Override
            public String apply(Schema.Column input) {
                String type = input.getType() == Schema.ColumnType.LOGICAL ? "bool" : input.getType().name();
                return input.getName().toLowerCase() + ":" + type.toLowerCase();
            }
        });
        String sql = "source(line,'resources/test/airline.csv',',',type(<" + StringUtils.join(nameAndType, ",") + ">));";
        manager.sql2ddf(sql, schema);

    }
}