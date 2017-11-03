package com.chen;

import com.chen.kafka.KafkaUtils;
import com.chen.model.TencentMovie;
import com.chen.utils.TencentMovieSpider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by ChenQiang on 2017/10/12.
 */
public class KafkaProducer {

    private static Gson gson = new Gson();

    public static void main(String[] args) {

        List<TencentMovie> movies = TencentMovieSpider.spider();
        for(TencentMovie movie : movies) {
            String message = gson.toJson(movie);
            KafkaUtils.getInstance().send(message);

        }
    }
}
